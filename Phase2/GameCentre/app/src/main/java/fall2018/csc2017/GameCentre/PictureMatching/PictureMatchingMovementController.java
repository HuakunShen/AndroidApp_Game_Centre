package fall2018.csc2017.GameCentre.PictureMatching;

import android.content.Context;
import android.widget.Toast;


public class PictureMatchingMovementController {

    private MatchingBoardManager boardManager = null;

    public PictureMatchingMovementController() {
    }

    public void setBoardManager(MatchingBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
//            boardManager.getBoard().setHighLightedCell();
//            boardManager.getBoard().getCell(position/9, position%9).setHighlighted(true);
            if (boardManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}

