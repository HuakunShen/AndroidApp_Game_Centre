package fall2018.csc2017.GameCentre.slidingTiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import fall2018.csc2017.GameCentre.data.StateStack;
import fall2018.csc2017.GameCentre.util.BoardManagerForBoardGames;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class BoardManager extends BoardManagerForBoardGames implements Serializable {

    /**
     * The board being managed.
     */
    private Board board;


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
     * The number of steps the user have taken so far.
     */
    private int stepsTaken;

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
        this.undoStack = new StateStack(DEFAULT_UNDO_LIMIT);
    }

    /**
     * The getter function for the board.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Add a move to the undo stack.
     */
    public void addUndo(Integer[] move) {
        undoStack.put(move);
    }

    /**
     * Set undo limit.
     */
    public void setCapacity(int input) {
        this.undoStack.setCapacity(input);
    }

    /**
     * Returns if undo is available.
     */
    public boolean undoAvailable() {
        return !undoStack.isEmpty();
    }

    /**
     * Get the undo step.
     */
    public Integer[] popUndo() {
        return (Integer[]) undoStack.pop();
    }

    /**
     * Get level of difficulty of the board.
     */
    public int getDifficulty() {
        return board.difficulty;
    }

    /**
     * The getter function for the steps taken.
     */
    public int getStepsTaken() {
        return stepsTaken;
    }

    /**
     * The setter function for the steps taken.
     */
    public void setStepsTaken(int stepsTakenSoFar) {
        this.stepsTaken = stepsTakenSoFar;
    }

    /**
     * The getter function for the time taken.
     */
    public long getTimeTaken() {
        return timeTaken;
    }

    /**
     * The setter function for the time taken.
     */
    public void setTimeTaken(long timeTakenSoFar) {
        this.timeTaken = timeTakenSoFar;
    }

    /**
     * Return whether the tiles are in row-major order.
     */
    public boolean boardSolved() {
        for (int row = 0; row < Board.NUM_ROWS; row++) {
            for (int col = 0; col < Board.NUM_ROWS; col++) {
                if (board.getTile(row, col).getId() != row * 4 + col + 1) {
                    return false;
                }
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
    public void makeMove(int position, int value) {
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
        if (value == 1) {
            Integer[] undoStep = new Integer[2];
            undoStep[0] = blank_pos;
            undoStep[1] = (Integer) 0;
            addUndo(undoStep);
        }
    }
}