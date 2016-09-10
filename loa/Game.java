// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.
package loa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.FileReader;
import java.util.Scanner;

import static loa.Piece.*;
import static loa.Main.*;

/** Represents one game of Lines of Action.
 *  @author Kevin Vo */
class Game {

    /** A new series of Games. */
    Game() {
        _randomSource = new Random();

        _players = new Player[2];
        _input = new BufferedReader(new InputStreamReader(System.in));
        _players[0] = new HumanPlayer(BP, this);
        _players[1] = new MachinePlayer(WP, this);
        _playing = false;
    }

    /** Return the current board. */
    Board getBoard() {
        return _board;
    }

    /** Quit the game. */
    private void quit() {
        System.exit(0);
    }

    /** Return a move.  Processes any other intervening commands as
     *  well.  Exits with null if the value of _playing changes. */
    Move getMove() {
        try {
            boolean playing0 = _playing;
            while (_playing == playing0) {
                prompt();

                String line = _input.readLine();
                if (line == null) {
                    quit();
                }
                line = line.trim();
                if (!processCommand(line)) {
                    Move move = Move.create(line, _board);
                    if (move == null) {
                        error("invalid move: %s%n", line);
                    } else if (!_playing) {
                        error("game not started");
                    } else if (!_board.isLegal(move)) {
                        error("illegal move: %s%n", line);
                    } else {
                        return move;
                    }
                }
            }
        } catch (IOException excp) {
            error(1, "unexpected I/O error on input");
        }
        return null;
    }

    /** Returns true if _playing is true. */
    boolean playing() {
        return _playing;
    }

    /** For the GUI, Return a move from the given string (ex. a3-c3). */
    Move getMove(String str) {
        String line = str;
        Move move = Move.create(line, _board);
        if (move == null) {
            error("invalid move: %s%n", line);
        } else if (!_playing) {
            error("game not started");
        } else if (!_board.isLegal(move)) {
            error("illegal move: %s%n", line);
        } else {
            return move;
        }
        System.out.println();
        prompt();
        return null;
    }

    /** Turns GUI to true. */
    void gui(){
        _hasGUI = true;
    }

    /** Updates _nextMove with from STR for the GUI. */
    void updateNextMove(String str) {
        _nextMove = getMove(str);
        _ready = true;
    }

    void processString(String s) {
        if (s.equals("start")) {
            _playing = true;
        }
        if (s.equals("clear")) {
            _playing = false;
            _board.clear();
        }
        if (s.equals("manual white")) {
            manualCommand("white");
        }
        if (s.equals("manual black")) {
            manualCommand("black");
        }
        if (s.equals("auto white")) {
            autoCommand("white");
        }
        if (s.equals("auto black")) {
            autoCommand("black");
        }
        _readyCommand = false;
    }

    void updateNextCommand(String s) {
        _nextCommand = s;
        _readyCommand = true;
    }

    /** Print a prompt for a move. */
    private void prompt() {
        System.out.print("> ");
        System.out.flush();
    }

    /** Describes a command with up to two arguments. */
    private static final Pattern COMMAND_PATN =
        Pattern.compile("(#|\\S+)\\s*(\\S*)\\s*(\\S*).*");

    /** If LINE is a recognized command other than a move, process it
     *  and return true.  Otherwise, return false. */
    private boolean processCommand(String line) {
        if (line.length() == 0) {
            return true;
        }
        Matcher command = COMMAND_PATN.matcher(line);
        if (command.matches()) {
            switch (command.group(1).toLowerCase()) {
            case "#":
                return true;
            case "manual":
                manualCommand(command.group(2).toLowerCase());
                return true;
            case "auto":
                autoCommand(command.group(2).toLowerCase());
                return true;
            case "seed":
                seedCommand(command.group(2));
                return true;
            case "board": case "b":
                System.out.println(_board.toString2());
                return true;
            case "autoprint":
                autoPrintCommand();
                return true;
            case "set":
                setCommand(command.group(2), command.group(3));
                return true;
            case "dump":
                System.out.println(_board.toString());
                return true;
            case "start":
                _playing = true;
                return true;
            case "clear":
                _playing = false;
                _board.clear();
                return true;
            case "quit":
                quit();
                return true;
            case "help": case "?":
                help();
                return true;
            default:
                return false;
            }
        }
        return false;
    }

    /** Set player PLAYER ("white" or "black") to be a manual player. */
    private void manualCommand(String player) {
        try {
            Piece s = Piece.playerValueOf(player);
            _playing = false;
            _players[s.ordinal()] = new HumanPlayer(s, this);
        } catch (IllegalArgumentException excp) {
            error("unknown player: %s", player);
            System.out.println();
        }
    }

    /** Set player PLAYER ("white" or "black") to be an automated player. */
    private void autoCommand(String player) {
        try {
            Piece s = Piece.playerValueOf(player);
            _playing = false;
            _players[s.ordinal()] = new MachinePlayer(s, this);
        } catch (IllegalArgumentException excp) {
            error("unknown player: %s", player);
            System.out.println();
        }
    }

    /** Seed random-number generator with SEED (as a long). */
    private void seedCommand(String seed) {
        try {
            _randomSource.setSeed(Long.parseLong(seed));
        } catch (NumberFormatException excp) {
            error("Invalid number: %s", seed);
            System.out.println();
        }
    }

    /** Sets a piece on the board. */
    private void setCommand(String cr, String player) {
        try {
            int c = _board.col(cr);
            int r = _board.row(cr);
            Piece p = Piece.setValueOf(player);
            _board.set(c, r, p, p.opposite());
        } catch (IllegalArgumentException excp) {
            error("Invalid arguments to set: %s, %s", cr, player);
            System.out.println();
        }
    }

    /** Toggles autoprint. */
    private void autoPrintCommand() {
        _autoprint = !_autoprint;
        if (_autoprint) {
            System.out.println("Autoprint mode on.");
        } else {
            System.out.println("Autoprint mode off.");
        }
    }

    /** Play this game, printing any results. */
    public void play() {
        HashSet<Board> positionsPlayed = new HashSet<Board>();
        _board = new Board();

        while (true) {
            int playerInd = _board.turn().ordinal();
            Move next;
            if (_playing) {
                if (_board.gameOver()) {
                    announceWinner();
                    _playing = false;
                    continue;
                }
                if (_hasGUI) {
                    if (_players[playerInd].type().equals("human")) {
                        if (_ready) {
                            next = _nextMove;
                        } else if (_readyCommand) {
                            processCommand(_nextCommand);
                            next = null;
                        } else {
                            continue;
                        }
                    } else {
                        next = _players[playerInd].makeMove();
                    }
                } else {
                    next = _players[playerInd].makeMove();
                }
                assert !_playing || next != null;
            } else {
                if (_hasGUI) {
                    System.out.print("");
                    if (_readyCommand) {
                        processString(_nextCommand);
                    }
                } else {
                    getMove();
                }
                next = null;
            }
            if (next != null) {
                assert _board.isLegal(next);
                _board.makeMove(next);
                if (_players[playerInd].type().equals("machine") && !_hasGUI) {
                    System.out.println(_players[playerInd].side().abbrev().toUpperCase() + "::" + next.toString());
                    if (_autoprint) {
                        System.out.println(_board.toString2());
                    }
                }
                if (_board.gameOver()) {
                    announceWinner();
                    _playing = false;
                }
            }
            _ready = false;
        }
    }

    /** Print an announcement of the winner. */
    private void announceWinner() {
        // FIXME
        if (_board.turn() == Piece.WP) {
            System.out.println("Black wins.");
        } else {
            System.out.println("White wins.");
        }
    }

    /** Return an integer r, 0 <= r < N, randomly chosen from a
     *  uniform distribution using the current random source. */
    int randInt(int n) {
        return _randomSource.nextInt(n);
    }

    /** Print a help message. */
    void help() {
        // FIXME
        try {
            FileReader help = new FileReader("loa/Help.txt");
            Scanner helpPls = new Scanner(help);
            while (helpPls.hasNextLine()) {
                System.out.println(helpPls.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Help file not found.");
        }

    }

    /** The official game board. */
    private Board _board;

    /** The _players of this game. */
    private Player[] _players = new Player[2];

    /** A source of random numbers, primed to deliver the same sequence in
     *  any Game with the same seed value. */
    private Random _randomSource;

    /** Input source. */
    private BufferedReader _input;

    /** True if actually playing (game started and not stopped or finished).
     */
    private boolean _playing;

    /** True if _autoprint is toggled. False otherwise. */
    private boolean _autoprint = false;

    /** True if GUI is on. False otherwise. */
    private boolean _hasGUI = false;

    /** Stores the next GUI move. */
    private Move _nextMove;

    /** States when GUI is ready for the next move. */
    private boolean _ready = false;

    /** Stores the next command from the GUI (ex. start, manual white) */
    private String _nextCommand;

    /** States when GUI is ready for the next command. */
    private boolean _readyCommand = false;

}
