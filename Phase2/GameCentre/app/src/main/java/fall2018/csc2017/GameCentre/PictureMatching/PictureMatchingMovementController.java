package fall2018.csc2017.GameCentre.PictureMatching;

import android.content.Context;
import android.widget.Toast;


public class PictureMatchingMovementController {

    private MatchingBoardManager boardManager;

    public PictureMatchingMovementController() {
    }

    public void setBoardManager(MatchingBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
            int row = position / MatchingBoard.NUM_COLS;
            int col = position % MatchingBoard.NUM_COLS;
            this.boardManager.processTiles(row,col);
            if (boardManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}

