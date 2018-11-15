package fall2018.csc2017.GameCentre.Soduku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardManager {

    /**
     * The board begin managed.
     */
    private Board board;

    /**
     * The level of difficulty.
     */
    private Integer levelOfDifficulty = 2;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    public BoardManager(Board board) {
        this.board = board;
    }

    /**
     * Manage a new shuffled board.
     */
    BoardManager() {
        List<Box> boxes = new ArrayList<>();
        Integer[][] newBoard = new BoardGenerator().getBoard();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                boxes.add(new Box(newBoard[row][column]));
            }
        }
        Integer editable = 0;
        if (levelOfDifficulty == 1) {
            editable = 18;
        } else if (levelOfDifficulty == 2) {
            editable = 36;
        } else if (levelOfDifficulty == 3) {
            editable = 54;
        }
        Integer changed = 0;
        while (!changed.equals(editable)) {
            Random r = new Random();
            int index = r.nextInt(81);
            if (boxes.get(index).isEditable()) {
                boxes.get(index).makeEditable();
                boxes.get(index).setFaceValue(0);
                changed++;
            }
        }
        this.board = new Board(boxes);
    }


    /**
     * Setter for level of difficulty.
     */
    public void setLevelOfDifficulty(int levelOfDifficulty) {
        this.levelOfDifficulty = (Integer) levelOfDifficulty;
    }

    /**
     * Returns whether the sudoku puzzle has been solved.
     */
    boolean sudokuSolved() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (!board.checkBox(row, column)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Return whether the tap is valid.
     */
    boolean isValidTap (int position) {
        return board.checkEditable(position / 9, position % 9);
    }

    /**
     * Performs changes to the board.
     */
    void makeMove(int position, int value) {
        this.board.getBox(position / 9, position % 9).setFaceValue(value);
    }
}
