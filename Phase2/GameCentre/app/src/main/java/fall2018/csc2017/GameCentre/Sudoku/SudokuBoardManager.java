package fall2018.csc2017.GameCentre.Sudoku;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fall2018.csc2017.GameCentre.Data.StateStack;

public class SudokuBoardManager implements Serializable {

    /**
     * The board begin managed.
     */
    private SudokuBoard board;

    /**
     * The time has taken so far.
     */
    private long timeTaken;

    /**
     * The undoStack storing steps has taken.(limited capacity)
     */
    private StateStack undoStack;

    /**
     * The default number of undo time.
     */
    private static final int DEFAULT_UNDO_LIMIT = 3;

    /**
     * The level of difficulty.
     */
    private static Integer levelOfDifficulty = 2;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    public SudokuBoardManager(SudokuBoard board) {
        this.board = board;
    }

    /**
     * Manage a new shuffled board.
     */
    public SudokuBoardManager() {
        List<Cell> cells = new ArrayList<>();
        Integer[][] newBoard = new BoardGenerator().getBoard();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                cells.add(new Cell(row, column, newBoard[row][column]));
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
            if (!cells.get(index).isEditable()) {
                cells.get(index).makeEditable();
                cells.get(index).setFaceValue(0);
                changed++;
            }
        }
        this.board = new SudokuBoard(cells);
        this.timeTaken = 0L;
    }

    /**
     * Return the current board.
     */
    public SudokuBoard getBoard() {
        return board;
    }


    /**
     * Setter for level of difficulty.
     */
    public static void setLevelOfDifficulty(int levelOfDifficulty) {
        SudokuBoardManager.levelOfDifficulty = (Integer) levelOfDifficulty;
    }

    /**
     * Returns whether the sudoku puzzle has been solved.
     */
    boolean sudokuSolved() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (!board.checkCell(row, column)) {
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
    void makeMove(int position) {
        this.board.setHighLightedCell();
        Cell cell = this.board.getCell(position/9, position%9);
        cell.setHighlighted(true);
        this.board.updateValue(cell.getFaceValue());
    }

    long getTimeTaken() {
        return timeTaken;
    }

    void setTimeTaken(long timeTakenSoFar) {
        this.timeTaken = timeTakenSoFar;
    }
}
