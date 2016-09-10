package loa;

import ucb.gui.TopLevel;
import ucb.gui.LayoutSpec;

import java.awt.event.MouseEvent;

import static loa.Piece.*;
import static loa.Main.*;

/** A GUI for Lines of Action.
 *  @author Kevin Vo
 */

class GUIGame extends TopLevel {
    /** A window with NAME and displaying GAME. */
    GUIGame(String name, Game game) {
        super(name, true);
        _game = game;
        _board = _game.getBoard();
        addMenuButton("Game->New Game", "newGame");
        addMenuButton("Game->Quit", "quit");
        addButton("Start Game", "start", new LayoutSpec("y", 1, "x", 0));
        addMenuButton("Player->White->Manual White", "manualWhite");
        addMenuButton("Player->White->Auto White", "autoWhite");
        addMenuButton("Player->Black->Manual Black", "manualBlack");
        addMenuButton("Player->Black->Auto Black", "autoBlack");
        _display = new GameDisplay(game);
        add(_display, new LayoutSpec("y", 2, "width", 2));
        _display.setMouseHandler("click", this, "mouseClicked");

        display(true);
        //showMessage("test", "title", "type");
    }

    public void newGame(String dummy) {
        _game.updateNextCommand("clear");
    }

    public void start(String dummy) {
        _game.updateNextCommand("start");
        _display.repaint();
    }

    public void manualBlack(String dummy) {
        _game.updateNextCommand("manual black");
        _display.repaint();
    }

    public void manualWhite(String dummy) {
        _game.updateNextCommand("manual white");
        _display.repaint();
    }

    public void autoBlack(String dummy) {
        _game.updateNextCommand("auto black");
        _display.repaint();
    }

    public void autoWhite(String dummy) {
        _game.updateNextCommand("auto white");
        _display.repaint();
    }

    public void quit(String dummy) {
        System.exit(1);
    }

    /** Piece being dragged. */
    private Piece draggedPiece;
    /** Location of piece being dragged (a1, d4). */
    private String draggedLocation;

    /** Action in response to mouse-clicking event EVENT. */
    public synchronized void mouseClicked(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        //if (_board.get(x, y))
        if (draggedLocation != null) {
            String destination = getPos(x, y);

            String move = draggedLocation + "-" + destination;
            if (!_game.playing()) {
                showMessage("Press 'Start Game' to play!", "Game has not been started.", null);
            }
            _game.updateNextMove(move);
            draggedLocation = null;
            _display.repaint();
        } else {
            String location = getPos(x, y);
            if (_game.getBoard().get(location) != Piece.EMP) {
                draggedLocation = getPos(x, y);
            }
        }
    }

    /** Returns the piece on coordinate (x, y). */
    private Piece getPiece(int x, int y) {
        if (x > 500 || y > 500) {
            return null;
        }
        int col = 0;
        int row = 9;
        for (int c = 0; c < _display.BOARD_SIZE; c += _display.PIECE_SIZE) {
            col++;
            if (c <= x && x <= c + _display.PIECE_SIZE) {
                break;
            }
        }
        for (int r = 0; r < _display.BOARD_SIZE; r += _display.PIECE_SIZE) {
            row--;
            if (r <= y && y <= r + _display.PIECE_SIZE) {
                break;
            }
        }
        return _game.getBoard().get(col, row);
    }

    /** Returns the position (a1, d4, etc) on board on coordinate (x, y). */
    private String getPos(int x, int y) {
        if (x > 500 || y > 500) {
            return null;
        }
        int col = 0;
        int row = 9;
        for (int c = 0; c < _display.BOARD_SIZE; c += _display.PIECE_SIZE) {
            col++;
            if (c <= x && x <= c + _display.PIECE_SIZE) {
                break;
            }
        }
        for (int r = 0; r < _display.BOARD_SIZE; r += _display.PIECE_SIZE) {
            row--;
            if (r <= y && y <= r + _display.PIECE_SIZE) {
                break;
            }
        }
        String colS = "abcdefgh".substring(col - 1, col);
        return colS + row;
    }


    /** The game this GUI is using. */
    private final Game _game;

    /** The board this GUI is using. */
    private Board _board;

    /** The _players of this game. */
    private Player[] _players = new Player[2];

    /** True if actually playing (game started and not stopped or finished).
     */
    private boolean _playing;

    /** The game widget. */
    private final GameDisplay _display;

}