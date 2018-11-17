package fall2018.csc2017.GameCentre.Sudoku;

import android.content.Context;
import android.widget.Toast;


public class SudokuMovementController {

    private SudokuBoardManager boardManager = null;

    public SudokuMovementController() {
    }

    public void setBoardManager(SudokuBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
//            boardManager.makeMove(position);
            if (boardManager.sudokuSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
