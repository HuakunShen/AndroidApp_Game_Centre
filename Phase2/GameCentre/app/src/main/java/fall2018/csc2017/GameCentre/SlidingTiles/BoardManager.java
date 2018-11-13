package fall2018.csc2017.GameCentre.SlidingTiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.Data.StateStack;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class BoardManager implements Serializable {

    /**
     * The board being managed.
     */
    private Board board;
    private int stepsTaken;
    private long timeTaken;
    private StateStack undoStack;
    private static final int DEFAULT_UNDO_LIMIT = 3;


    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    public BoardManager(Board board) {
        this.board = board;
    }

    /**
     * Return the current board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Manage a new shuffled board.
     */
    BoardManager() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        this.stepsTaken = 0;
        this.timeTaken = 0L;
        Collections.shuffle(tiles);
        this.board = new Board(tiles);
        boolean solvable = false;
        while (!solvable) {
            Collections.shuffle(tiles);
            this.board = new Board(tiles);
            solvable = solvable();
        }
        this.undoStack = new StateStack(DEFAULT_UNDO_LIMIT);
    }


    int getStepsTaken() {
        return stepsTaken;
    }

    void setStepsTaken(int stepsTakenSoFar) {
        this.stepsTaken = stepsTakenSoFar;
    }

    long getTimeTaken() {
        return timeTaken;
    }

    void setTimeTaken(long timeTakenSoFar) {
        this.timeTaken = timeTakenSoFar;
    }

    /**
     * Determines whether the tile board is solvable.
     */
    public boolean solvable() {
        Iterator<Tile> tiles = this.board.iterator();
        ArrayList<Integer> listOfTiles = new ArrayList<>(this.board.numTiles());
        while (tiles.hasNext()) {
            listOfTiles.add(tiles.next().getId());
        }
        int totalInversion = 0;
        for (int i = 0; i < this.board.numTiles(); i++) {
            for (int j = this.board.numTiles() - 1; j > i; j--) {
                if (listOfTiles.get(i) > listOfTiles.get(j)) {
                    totalInversion += 1;
                }
            }
        }
        if (this.board.numTiles() % 2 == 1) {
            return totalInversion % 2 == 0;
        } else {
            boolean solvable = false;
            for (int i = 0; i < Board.NUM_ROWS; i++) {
                for (int j = 0; j < Board.NUM_COLS; j++) {
                    if (board.getTile(i, j).getId() == board.numTiles()) {
                        solvable = ((Board.NUM_ROWS - i) % 2 == 0 && totalInversion % 2 == 1) ||
                                ((Board.NUM_ROWS - i) == 1 && totalInversion % 2 == 0);
                        break;
                    }
                }
            }
            return solvable;
        }
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    public boolean puzzleSolved() {
        Iterator<Tile> itr = board.iterator();
        for (int i = 1; i <= (Board.NUM_ROWS * Board.NUM_COLS); i++) {

            if (itr.hasNext() && itr.next().getId() != i) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    public boolean isValidTap(int position) {

        int row = position / Board.NUM_COLS;
        int col = position % Board.NUM_COLS;
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == Board.NUM_ROWS - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == Board.NUM_COLS - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    int touchMove(int position) {
        int row = position / Board.NUM_ROWS;
        int col = position % Board.NUM_COLS;
        int blankId = board.numTiles();
        int blank_pos;
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == Board.NUM_ROWS - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        if (above != null && above.getId() == blankId) {
            this.board.swapTiles(row - 1, col, row, col);
            blank_pos = (row - 1) * Board.NUM_ROWS + col;
        } else if (below != null && below.getId() == blankId) {
            this.board.swapTiles(row + 1, col, row, col);
            blank_pos = (row + 1) * Board.NUM_ROWS + col;
        } else if (left != null && left.getId() == blankId) {
            this.board.swapTiles(row, col - 1, row, col);
            blank_pos = row * Board.NUM_ROWS + (col - 1);
        } else {
            this.board.swapTiles(row, col + 1, row, col);
            blank_pos = row * Board.NUM_ROWS + (col + 1);
        }
        return blank_pos;
    }

    void addUndo(int move) {
        this.undoStack.put(move);
    }

    void setCapacity(int input) {
        this.undoStack.setCapacity(input);
    }

    boolean undoAvailable() {
        return !undoStack.isEmpty();
    }

    int popUndo() {
        return (int) undoStack.pop();
    }

    int getDifficulty() {
        return board.difficulty;
    }
}
