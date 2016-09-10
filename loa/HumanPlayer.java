// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.
package loa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/** A Player that prompts for moves and reads them from its Game.
 *  @author Kevin Vo */
class HumanPlayer extends Player {

    /** A HumanPlayer that plays the SIDE pieces in GAME.  It uses
     *  GAME.getMove() as a source of moves.  */
    HumanPlayer(Piece side, Game game) {
        super(side, game);
        // FILL IN
        _type = "human";
    }

    @Override
    Move makeMove() {
        // REPLACE WITH IMPLEMENTATION
        return getGame().getMove();
    }

    // OTHER METHODS AND FIELDS HERE.
    /** Returns the type of this Player. */
    String type() {
        return _type;
    }
    
    /** The type of this Player. */
    private String _type;

}
