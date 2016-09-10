package loa;

import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.BasicStroke;

import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;

/** A widget that displays a Lines of Action board.
 *  @author Kevin Vo
 */
class GameDisplay extends Pad {

    /** Dimensions of the window. */
    public static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;

    /** Size of the board inside the window. */
    public static final int BOARD_SIZE = 500;

    /** Size of the piece inside the board. */
    public static final int PIECE_SIZE = BOARD_SIZE / 8;

    /** A graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in =
            getClass().getResourceAsStream("/loa/resources/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }

    /** Draw the Piece at X, Y, on G. */
    private void paintPiece(Graphics2D g, Piece piece, int x, int y) {
        if (piece == Piece.BP) {
            g.drawImage(getImage("black.png"), x, y, PIECE_SIZE, PIECE_SIZE, null);
        } else if (piece == Piece.WP) {
            g.drawImage(getImage("white.png"), x, y, PIECE_SIZE, PIECE_SIZE, null);
        }
    }

    /** Draw the Board at X, Y on G. */
    private void paintBoard(Graphics2D g, int x, int y) {
        g.drawImage(getImage("board.png"), x, y, BOARD_SIZE, BOARD_SIZE, null);
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        paintBoard(g, 0, 0);
        for (int c = 1; c <= 8; c += 1) {
            for (int r = 1; r <= 8; r += 1) {
                paintPiece(g, _game.getBoard().get(c, r), PIECE_SIZE * (c - 1), PIECE_SIZE * (8 - r));
            }
        }
        if (_game.getBoard().gameOver()) {
            String winner = _game.getBoard().turn().opposite().fullName();
            int size = WINDOW_WIDTH - BOARD_SIZE;
            g.drawImage(getImage(winner + "_wins.png"), BOARD_SIZE, 0, size, size, null);
        } else {
            String turn = _game.getBoard().turn().fullName();
            int size = WINDOW_WIDTH - BOARD_SIZE;
            g.drawImage(getImage(turn + "_turn.png"), BOARD_SIZE, size, size, size, null);
        }
    }

    /** Game I am displaying. */
    private final Game _game;

}
