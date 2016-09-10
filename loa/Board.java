// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.
package loa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Formatter;
import java.util.NoSuchElementException;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Direction.*;

/** Represents the state of a game of Lines of Action.
 *  @author Kevin Vo
 */
class Board implements Iterable<Move> {

    /** Size of a board. */
    static final int M = 8;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row-1][col-1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is MxM.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        clear();
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        clear();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _moves.clear();
        _board = new Piece[M][M];
        // FIXME

        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                set(c, r, contents[r - 1][c - 1]);
            }
        }
        _turn = side;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                set(c, r, board._board[c - 1][r - 1]);
            }
        }
        _moves.clear();
        _moves.addAll(board._moves);
        _turn = board._turn;
        // FIXME
    }

    /** Return the contents of column C, row R, where 1 <= C,R <= 8,
     *  where column 1 corresponds to column 'a' in the standard
     *  notation. */
    Piece get(int c, int r) {
        return _board[c - 1][r - 1]; // FIXME

    }

    /** Return the contents of the square SQ.  SQ must be the
     *  standard printed designation of a square (having the form cr,
     *  where c is a letter from a-h and r is a digit from 1-8). */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /** Return the column number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int col(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(0) - 'a' + 1;
    }

    /** Return the row number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int row(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(1) - '0';
    }

    /** Set the square at column C, row R to V, and make NEXT the next side
     *  to move, if it is not null. */
    void set(int c, int r, Piece v, Piece next) {
        // FIXME
        if (next != null) {
            _turn = next;
        }
        _board[c - 1][r - 1] = v;
    }

    /** Set the square at column C, row R to V. */
    void set(int c, int r, Piece v) {
        set(c, r, v, null);
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        assert isLegal(move);
        _moves.add(move);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        if (replaced != EMP) {
            set(c1, r1, EMP);
        }
        set(c1, r1, move.movedPiece());
        set(c0, r0, EMP);
        _turn = _turn.opposite();
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move move = _moves.remove(_moves.size() - 1);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        Piece movedPiece = move.movedPiece();
        set(c1, r1, replaced);
        set(c0, r0, movedPiece);
        _turn = _turn.opposite();
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff MOVE is legal for the player currently on move. */
    boolean isLegal(Move move) {
        // FIXME
        if (move == null || move.movedPiece() != turn() || blocked(move)) {
            return false;
        }
        return move.length() == pieceCountAlong(move); //fix
    }

    /** Return a sequence of all legal moves from this position. */
    Iterator<Move> legalMoves() {
        return new MoveIterator(this);
    }

    @Override
    public Iterator<Move> iterator() {
        return legalMoves();
    }

    /** Return true if there is at least one legal move for the player
     *  on move. */
    public boolean isLegalMove() {
        return iterator().hasNext();
    }

    /** Return true iff either player has all his pieces continguous. */
    boolean gameOver() {
        return piecesContiguous(BP) || piecesContiguous(WP);
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        // FIXME
        int numPiece = numPieces(side);
        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                if (get(c, r) == side) {
                    int x = countContiguous(side, c, r);
                    return x == numPiece;
                }
            }
        }
        return false;
    }

    /** Counts contiguous pieces starting at col, row. */
    int countContiguous(Piece side, int col, int row) {
        Board tempBoard = new Board();
        tempBoard.copyFrom(this);
        countContig = 0;
        countContiguous(tempBoard, side, col, row);
        return countContig;
    }

    /** Helper for countContiguous. */
    private void countContiguous(Board tempBoard, Piece side, int col, int row) {
        if (!(1 <= col && col <= M && 1 <= row && row <= M)) {
            return; 
        }
        if (tempBoard.get(col, row) != side) {
            return;
        } else {
            countContig += 1;
            tempBoard.set(col, row, EMP);
            for (Direction d = N; d != null; d = d.succ()) {
                countContiguous(tempBoard, side, col + d.dc, row + d.dr);
            }
        }
    }

    /** A counter for contiguous pieces. Helps for countContiguous. */
    private int countContig;

    /** Returns the number of Piece SIDE in the given board. */
    int numPieces(Piece side) {
        int result = 0;
        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                if (get(c, r) == side) {
                    result += 1;
                }
            }
        }
        return result;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return b == this;  // FIXME
    }

    @Override
    public int hashCode() {
        return 0; // FIXME
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = M; r >= 1; r -= 1) {
            out.format("    ");
            for (int c = 1; c <= M; c += 1) {
                out.format("%s ", get(c, r).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Altername toString method. */
    public String toString2() {
        Formatter out = new Formatter();
        for (int r = M; r >= 1; r -= 1) {
            out.format("%s ", r);
            for (int c = 1; c <= M; c += 1) {
                out.format("%s ", get(c, r).abbrev());
            }
            out.format("%n");
        }
        out.format("  a b c d e f g h   %s to move", turn().fullName());
        return out.toString();
    }

    /** Return the number of pieces in the line of action indicated by MOVE. */
    private int pieceCountAlong(Move move) {
        // FIXME
        return pieceCountAlong(move.getCol0(), move.getRow0(), move.getDir());
    }

    /** Return the number of pieces in the line of action in direction DIR and
     *  containing the square at column C and row R. */
    private int pieceCountAlong(int c, int r, Direction dir) {
        // FIXME
        if (dir == Direction.NOWHERE) {
            return 1;
        }
        return pieceCountAlongAdv(c, r, dir) + pieceCountAlongRet(c - dir.dc, r - dir.dr, dir);
    }

    /**  Return the number of pieces in the line of action advancing DIR and
    *    containing the square at column C and row R. */
    private int pieceCountAlongAdv(int c, int r, Direction dir) {
        if (c < 1 || c > M || r < 1 || r > M) {
            return 0;
        } else if (get(c, r) != Piece.EMP) {
            return 1 + pieceCountAlongAdv(c + dir.dc, r + dir.dr, dir);
        }
        return pieceCountAlongAdv(c + dir.dc, r + dir.dr, dir);
    }

    /** Return the number of pieces in the line of action retreating DIR and
    *   containing the square at column C and row R. */
    private int pieceCountAlongRet(int c, int r, Direction dir) {
        if (c < 1 || c > M || r < 1 || r > M) {
            return 0;
        } else if (get(c, r) != Piece.EMP) {
            return 1 + pieceCountAlongRet(c - dir.dc, r - dir.dr, dir);
        }
        return pieceCountAlongRet(c - dir.dc, r - dir.dr, dir);
    }

    /** Return true iff MOVE is blocked by an opposing piece or by a
     *  friendly piece on the target square. */
    private boolean blocked(Move move) {
        // FIXME
        return blocked(move, move.getCol0(), move.getRow0(), move.getCol1(), move.getRow1());
    }

    /** Helper function for blocked. Uses counters to track position. */
    private boolean blocked(Move move, int col0, int row0, int col1, int row1) {
        if (col0 == col1 && row0 == row1) {
            if (move.movedPiece() == get(col0, row0)) {
                return true;
            } else {
                return false;
            }
        } else if (get(col0, row0) == move.movedPiece().opposite()) {
            return true;
        } else {
            return blocked(move, col0 + move.getDir().dc, row0 + move.getDir().dr, col1, row1);
        }
    }

    /** The standard initial configuration for Lines of Action. */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** A 2-D Array of pieces to represent the board */
    private Piece[][] _board;
    // FILL IN

    /** An iterator returning the legal moves from the current board. */
    private class MoveIterator implements Iterator<Move> {
        /** Current piece under consideration. */
        private int _c, _r;
        /** Next direction of current piece to return. */
        private Direction _dir;
        /** Next move. */
        private Move _move;
        /** The board MoveIterator pertains to. */
        private Board _board;

        /** A new move iterator for turn(). */
        MoveIterator(Board b) {
            _c = 1; _r = 1; _dir = NOWHERE; _board = b;
            nextPiece();
            incr();
        }

        @Override
        public boolean hasNext() {
            return _move != null;
        }

        @Override
        public Move next() {
            if (_move == null) {
                throw new NoSuchElementException("no legal move");
            }

            Move move = _move;
            incr();
            return move;
        }

        @Override
        public void remove() {
        }

        /** Advance to the next legal move. */
        private void incr() {
            nextDir();
            if (_c == 8 && _r == 8 && _dir == null) {
                _move = null;
                return;
            }
            //nextDir(); //SKIPS NOWHERE ????
            if (_dir == null) {
                _dir = Direction.NOWHERE;
                nextPiece();
            }
            Move temp = Move.create(_c, _r, pieceCountAlong(_c, _r, _dir), _dir, _board);
            if (temp == null) {
                incr();
            } else if (!isLegal(temp)) {
                incr();
            } else {
                _move = temp;
            }

        }

        /** Advance to the next piece on the board, setting _move to null
         *  if _c and _r are out of the dimensions. */
        private void nextPiece() {
            if (_c == 8 && _r == 8) {
                return;
            } else {
                if (_c < 8) {
                    _c += 1;
                } else {
                    _c = 1;
                    _r += 1;
                }
            }
            if (get(_c, _r) == Piece.EMP) {
                nextPiece();
            }
        }

        /** Advance to the next direction. */
        private void nextDir() {
            _dir = _dir.succ();
        }
    }

}
