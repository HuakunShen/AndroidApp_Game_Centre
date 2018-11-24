package fall2018.csc2017.GameCentre.sudoku;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.util.BoardForBoardGames;

public class SudokuBoard extends BoardForBoardGames implements Serializable {

    /**
     * The cells on the board.
     */
    private Cell[][] cells = new Cell[9][9];

    public static final int NUM_ROW = 9;
    public static final int NUM_COL = 9;

    /**
     * A new board of cells in row-major order.
     * Precondition: len(cells) == 81.
     *
     * @param cells the cells for the board
     */
    SudokuBoard(List<Cell> cells) {
        Iterator<Cell> iterator = cells.iterator();

        for (int row = 0; row != 9; row++) {
            for (int col = 0; col != 9; col++) {
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
