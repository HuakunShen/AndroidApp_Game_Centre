package csc207project.gamecenter.SlidingTiles;

import android.content.Context;
import android.widget.Toast;

/**
 * The movement controller.
 */
public class MovementController {

    /**
     * A BoardManager.
     */
    private BoardManager boardManager = null;

    /**
     * The constructor of this class.
     */
    public MovementController() {
    }

    /**
     * Set the the local BoardManager to a BoardManager.
     */
    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * The actions after each tap.
     */
    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
            boardManager.addUndo(boardManager.touchMove(position));
            if (boardManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
