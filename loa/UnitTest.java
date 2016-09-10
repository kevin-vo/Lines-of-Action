// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.
package loa;

import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

/** The suite of all JUnit tests for the loa package.
 *  @author Kevin Vo
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void placeholderTest() {
    }

    /** A test for initializing a board and setting pieces. */
    //@Test
    // public void initializeTest() {
    // 	Board board = new Board();
    // 	System.out.println("...PRINTING INITIAL BOARD...");
    // 	System.out.println(board.toString());
    // 	board.set(3, 6, Piece.WP);
    // 	board.set(4, 4, Piece.BP);
    // 	board.set(2, 1, Piece.WP);
    // 	System.out.println("...SETTING WHITE PIECE AT c6...");
    // 	System.out.println("...SETTING BLACK PIECE AT d4...");
    // 	System.out.println("...REPLACING BLACK PIECE WITH WHITE PIECE AT b1...");
    // 	System.out.println(board.toString());
    //     assertEquals(board.get(3, 6).fullName(), "white");
    //     assertEquals(board.get(4, 4).fullName(), "black");
    //     assertEquals(board.get(5, 5).fullName(), "empty");
    // }

    /** A test for copying boards. */
    // @Test
    // public void copyFromTest() {
    // 	Board board1 = new Board();
    // 	board1.set(3, 6, Piece.WP, Piece.WP);
    // 	Board board2 = new Board(board1);
    // 	System.out.println("Printing board2, a copy of board1");
    // 	System.out.println(board2.toString());
    // 	board1.set(1, 1, Piece.WP);
    // 	System.out.println("Added piece to board1, does not affect board2");
    // 	System.out.println(board2.toString());
    // 	System.out.println("Printing board1");
    // 	System.out.println(board1.toString());
    // 	assertEquals(board1.turn(), board2.turn());
    // }

    /** A test for getting Direction from a move. */
    @Test
    public void getDirTest() {
        Board board = new Board();
        Move north = Move.create(2, 1, 2, 3, board);
        assertEquals(north.getDir(), Direction.N);

        Move south = Move.create(2, 8, 2, 6, board);
        assertEquals(south.getDir(), Direction.S);

        Move east = Move.create(1, 2, 3, 2, board);
        assertEquals(east.getDir(), Direction.E);

        Move west = Move.create(8, 2, 6, 2, board);
        assertEquals(west.getDir(), Direction.W);

        Move northeast = Move.create(2, 1, 4, 3, board);
        assertEquals(northeast.getDir(), Direction.NE);

        Move northwest = Move.create(7, 1, 5, 3, board);
        assertEquals(northwest.getDir(), Direction.NW);

        Move southeast = Move.create(2, 8, 4, 6, board);
        assertEquals(southeast.getDir(), Direction.SE);

        Move southwest = Move.create(7, 8, 5, 6, board);
        assertEquals(southwest.getDir(), Direction.SW);
    }

    /** A test for pieceCountAlong() when made public. */
    // @Test
    // public void testPieceCountAlong() {
    //     Board board = new Board();
    //     board.set(2, 7, Piece.BP);
    //     board.set(2, 6, Piece.BP);
    //     Move north = Move.create(2, 1, 2, 5, board);
    //     assertEquals(4, board.pieceCountAlong(north));
    //     assertEquals(6, board.pieceCountAlong(2, 1, Direction.E));
    // }

    /** A test for blocked() when made public. */
    // @Test
    // public void testBlocked() {
    //     Board board = new Board();
    //     board.set(3, 4, Piece.WP);
    //     Move valid = Move.create(3, 1, 3, 4, board);
    //     assertEquals(false, board.blocked(valid));

    //     board.clear();
    //     board.set(3, 3, Piece.WP);
    //     Move invalid = Move.create(3, 1, 3, 4, board);
    //     assertEquals(true, board.blocked(invalid));
    // }
    
    /** A test for isLegal. */
    @Test
    public void testIsLegal() {
        Board board = new Board();
        board.set(3, 3, Piece.WP);

        Move jumper = Move.create(4, 1, 8, 8, board);
        assertEquals(false, board.isLegal(jumper));

        Move blocked = Move.create(3, 1, 3, 4, board);
        assertEquals(false, board.isLegal(blocked));

        Move tooShort = Move.create(3, 8, 3, 6, board);
        assertEquals(false, board.isLegal(tooShort));

        Move tooLong = Move.create(3, 8, 3, 4, board);
        assertEquals(false, board.isLegal(tooLong));

        Move goldilocks = Move.create(3, 8, 3, 5, board);
        assertEquals(true, board.isLegal(goldilocks));
    }

    /** A test for MoveIterator. */
    // @Test
    // public void testMoveIterator() {
    //     Board board = new Board();
    //     Iterator<Move> iterator = board.iterator();
    //     System.out.println(board.toString());
    //     board.makeMove(iterator.next());
    //     System.out.println(board.toString());
    //     iterator = board.iterator();
    //     board.makeMove(iterator.next());
    //     System.out.println(board.toString());
    // }

    /** A test for piecesContiguous. */
    @Test
    public void testPiecesContiguous() {
        Board board = new Board();
        assertEquals(false, board.piecesContiguous(Piece.BP));
        for (int i = 2; i <= 7; i++) {
            board.set(4, i, Piece.BP);
        }
        assertEquals(true, board.piecesContiguous(Piece.BP));
        assertEquals(false, board.piecesContiguous(Piece.WP));


        Board board2 = new Board();
        for (int i = 1; i <= 8; i++) {
            board2.set(i, i, Piece.WP);
        }
        assertEquals(true, board2.piecesContiguous(Piece.WP));
        assertEquals(false, board2.piecesContiguous(Piece.BP));

    }

}


