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
            this.boardManager.processTiles(position);
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_LONG).show();
        }
    }
}

