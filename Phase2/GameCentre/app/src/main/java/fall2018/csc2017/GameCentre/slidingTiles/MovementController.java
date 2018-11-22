package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Context;
import android.widget.Toast;


public class MovementController {

    private BoardManager boardManager = null;

    public MovementController() {
    }

    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
            Integer[] undoStep = new Integer[2];
            undoStep[0] = (Integer) boardManager.makeMove(position);
            undoStep[1] = (Integer) 0;
            boardManager.addUndo(undoStep);
            if (boardManager.boardSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
