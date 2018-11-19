package fall2018.csc2017.GameCentre.PictureMatching;

import android.content.Context;
import android.widget.Toast;


public class PictureMatchingMovementController {

    private MatchingBoardManager boardManager;
    private int count = 0;

    public PictureMatchingMovementController() {
    }

    public void setBoardManager(MatchingBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
            int row = position / MatchingBoard.NUM_COLS;
            int col = position % MatchingBoard.NUM_COLS;
            if(count == 0){
                boardManager.getBoard().setHighlightedTile(row, col);
                boardManager.getBoard().getHighlightedTile().setHighlighted(true);
                count += 1;
            }else if (count == 1){
                count = 0;
                boardManager.cancelPicture(row, col);
            if (boardManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}}

