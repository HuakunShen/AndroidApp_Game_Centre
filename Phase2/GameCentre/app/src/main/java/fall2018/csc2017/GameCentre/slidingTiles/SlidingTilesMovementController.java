package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Context;
import android.widget.Toast;


public class SlidingTilesMovementController {

    private SlidingTilesBoardManager boardManager = null;

    public SlidingTilesMovementController() {
    }

    public void setBoardManager(SlidingTilesBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
            boardManager.makeMove(position);
            if (boardManager.boardSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
