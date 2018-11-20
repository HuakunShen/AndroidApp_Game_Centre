package fall2018.csc2017.GameCentre.Sudoku;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class SudokuBoard extends Observable implements Serializable {

    public static final int NUM_ROWS_SUDOKU = 9;
    public static final int NUM_COLS_SUDOKU = 9;
    /**
     * The cells on the board.
     */
    private Cell[][] cells = new Cell[NUM_ROWS_SUDOKU][NUM_COLS_SUDOKU];

    /**
     * A new board of cells in row-major order.
     * Precondition: len(cells) == 81.
     *
     * @param cells the cells for the board
     */
    SudokuBoard(List<Cell> cells) {
        Iterator<Cell> iterator = cells.iterator();

        for (int row = 0; row != NUM_ROWS_SUDOKU; row++) {
            for (int col = 0; col != NUM_COLS_SUDOKU; col++) {
                this.cells[row][col] = iterator.next();
            }
        }
    }

    /**
     * Get the box at the ith row and jth column
     */
    public Cell getCell(int row, int column) {
        return this.cells[row][column];
    }

    /**
     * Check whether the box at the ith row and jth column
     * has been correctly solved.
     */
    boolean checkCell(int row, int col) {
        return this.cells[row][col].checkValue();
    }

    /**
     * Check whether the face value of box at the ith row and
     * jth column can be edited.
     */
    boolean checkEditable(int row, int col) {
        return this.cells[row][col].isEditable();
    }
}
