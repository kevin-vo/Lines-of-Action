// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.
package loa;

import java.util.Iterator;
import java.util.Random;

/** An automated Player.
 *  @author Kevin Vo */
class MachinePlayer extends Player {

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
        // FILL IN
        _type = "machine";

    }

    @Override
    Move makeMove() {
        // REPLACE WITH IMPLEMENTATION
        Iterator<Move> iterator = getBoard().iterator();

        int max = 0;
        Move first = iterator.next();
        Move result = null;

        int resultVal = val(first);

        while (iterator.hasNext()) {
            Move x = iterator.next();
            int xVal = val(x);

            if (xVal > resultVal) {
                resultVal = xVal;
                result = x;
            }
        }
        if (result != null) {
            return result;
        }
        return first;
        
    }

    /** Returns a number denoting the value of a move. */
    private int val(Move m) {
        Board b = test(m);
        if (b.piecesContiguous(side())) {
            return Integer.MAX_VALUE;
        } else if (b.piecesContiguous(side().opposite())) {
            return Integer.MIN_VALUE;
        }

        int preMove = getBoard().countContiguous(side(), m.getCol0(), m.getRow0());
        int postMove = b.countContiguous(side(), m.getCol1(), m.getRow1());
        int num = getBoard().numPieces(side());

        int enemyPreMove = getBoard().countContiguous(side().opposite(), m.getCol1(), m.getRow1());
        int enemyPostMove = b.countContiguous(side().opposite(), m.getCol1(), m.getRow1());
        int enemyNum = getBoard().numPieces(side().opposite());

        Random random = new Random();
        int rand = random.nextInt(5) - 3;
        return Math.max(postMove - preMove, Math.max(enemyPreMove - enemyPostMove, rand)); //not sure about eating pieces

        //int enemyNum = getBoard().numPieces(side().opposite());


    }

    /** Returns a board if M is applied. */
    private Board test(Move m) {
        Board b = new Board(getBoard());
        b.makeMove(m);
        return b;
    }

    // OTHER METHODS AND FIELDS HERE.
    
    /** Returns the type of this Player. */
    String type() {
        return _type;
    }

    /** The type of this Player. */
    private String _type;


}
