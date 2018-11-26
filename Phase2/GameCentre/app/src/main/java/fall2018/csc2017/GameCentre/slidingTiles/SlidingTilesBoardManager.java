package fall2018.csc2017.GameCentre.slidingTiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.data.StateStack;
import fall2018.csc2017.GameCentre.util.BoardManagerForBoardGames;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class SlidingTilesBoardManager extends BoardManagerForBoardGames implements Serializable {

    /**
     * The board being managed.
     */
    private SlidingTilesBoard board;

    /**
     * The time has taken so far.
     */
    private long timeTaken;

    /**
     * The undoStack storing steps has taken.(limited capacity)
     */
    private StateStack<Integer> undoStack;

    /**
     * The default number of undo time.
     */
    private static final int DEFAULT_UNDO_LIMIT = 3;

    /**
     * The number of steps the user have taken so far.
     */
    private int stepsTaken;

    private byte[] imageBackground;

    /**
     * Manage a new shuffled board.
     */
    SlidingTilesBoardManager(int difficulty) {
        List<Integer> tiles = new ArrayList<>();
        final int numTiles = difficulty * difficulty;
        for (int tileNum = 1; tileNum != numTiles + 1; tileNum++) {
            tiles.add(tileNum);
        }
        this.stepsTaken = 0;
        this.timeTaken = 0L;
        Collections.shuffle(tiles);
        this.board = new SlidingTilesBoard(tiles, difficulty);
        boolean solvable = false;
        while (!solvable) {
            Collections.shuffle(tiles);
            this.board = new SlidingTilesBoard(tiles, difficulty);
            solvable = solvable();
        }
        this.undoStack = new StateStack<>(DEFAULT_UNDO_LIMIT);
    }

    /**
     * Manager a prepared board.
     */
    public SlidingTilesBoardManager(SlidingTilesBoard board) {
        this.board = board;
        this.undoStack = new StateStack<>(DEFAULT_UNDO_LIMIT);
    }

    /**
     * The getter function for the board.
     */
    public SlidingTilesBoard getBoard() {
        return this.board;
    }

    /**
     * Add a move to the undo stack.
     */
    public void addUndo(Integer move) {
        undoStack.put(move);
    }

    /**
     * Set undo limit.
     */
    void setCapacity(int input) {
        this.undoStack.setCapacity(input);
    }

    /**
     *
     */
    int getCapacity() {return this.undoStack.getCapacity();}

    /**
     * Returns if undo is available.
     */
    boolean undoAvailable() {
        return !undoStack.isEmpty();
    }

    /**
     * Get the undo step.
     */
    Integer popUndo() {
        return undoStack.pop();
    }

    /**
     * Get level of difficulty of the board.
     */
    public int getDifficulty() {
        return board.getDifficulty();
    }

    /**
     * The getter function for the steps taken.
     */
    int getStepsTaken() {
        return stepsTaken;
    }

    /**
     * The setter function for the steps taken.
     */
    void setStepsTaken(int stepsTakenSoFar) {
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
     * Determines whether the tile board is solvable.
     */
    public boolean solvable() {
        Iterator<Integer> tiles = this.board.iterator();
        ArrayList<Integer> listOfTiles = new ArrayList<>(this.board.numTiles());
        while (tiles.hasNext()) {
            listOfTiles.add(tiles.next());
        }

        int totalInversion = getTotalInversion(listOfTiles);

        if (this.board.numTiles() % 2 != 0) {
            return totalInversion % 2 == 0;
        } else {
            if (board.numTiles() % 2 != 0) {
                return totalInversion % 2 == 0;
            } else {
                return blankPosition() % 2 == 0 && totalInversion % 2 != 0 ||
                        blankPosition() % 2 != 0 && totalInversion % 2 == 0;
            }
        }
    }

    /**
     * Return the index of row which the blank tile is in.
     */
    public int blankPosition() {
        int position = 0;
        for (int i = 0; i < board.getDifficulty(); i++) {
            for (int j = 0; j < board.getDifficulty(); j++) {
                if (board.getTile(i, j) == board.numTiles()) {
                    position = board.getDifficulty() - i;
                    break;
                }
            }
        }
        return position;
    }


    /**
     * Return the number of inversions in a list of Integer.
     */
    public int getTotalInversion(ArrayList<Integer> listOfTiles) {
        int totalInversion = 0;
        for (int i = 0; i < this.board.numTiles() - 1; i++) {
            for (int j = i + 1; j < this.board.numTiles(); j++) {
                if (listOfTiles.get(i) != this.board.numTiles() && listOfTiles.get(i) > listOfTiles.get(j)) {
                    totalInversion++;
                }
            }
        }
        return totalInversion;
    }

    /**
     * Return the current image background in byte array.
     */
    public byte[] getImageBackground() {
        return imageBackground;
    }

    /**
     * Set the image background of the board.
     */
    public void setImageBackground(byte[] imageBackground) {
        this.imageBackground = imageBackground;
    }

    /**
     * Return whether the tiles are in row-major order.
     */
    public boolean boardSolved() {
        Iterator<Integer> iterator = board.iterator();
        for (int i = 1; i < board.numTiles() + 1; i++) {
            if (iterator.next() != i) {
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
        int row = position / board.getDifficulty();
        int col = position % board.getDifficulty();
        int blankId = board.numTiles();
        Integer above = row == 0 ? null : board.getTile(row - 1, col);
        Integer below = row == board.getDifficulty() - 1 ? null : board.getTile(row + 1, col);
        Integer left = col == 0 ? null : board.getTile(row, col - 1);
        Integer right = col == board.getDifficulty() - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below == blankId)
                || (above != null && above == blankId)
                || (left != null && left == blankId)
                || (right != null && right == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    public int move(int position) {
        int row = position / board.getDifficulty();
        int col = position % board.getDifficulty();
        int blankId = board.numTiles();
        int blank_pos;
        Integer above = row == 0 ? null : board.getTile(row - 1, col);
        Integer below = row == board.getDifficulty() - 1 ? null : board.getTile(row + 1, col);
        Integer left = col == 0 ? null : board.getTile(row, col - 1);
        if (above != null && above == blankId) {
            this.board.swapTiles(row - 1, col, row, col);
            blank_pos = (row - 1) * board.getDifficulty() + col;
        } else if (below != null && below == blankId) {
            this.board.swapTiles(row + 1, col, row, col);
            blank_pos = (row + 1) * board.getDifficulty() + col;
        } else if (left != null && left == blankId) {
            this.board.swapTiles(row, col - 1, row, col);
            blank_pos = row * board.getDifficulty() + (col - 1);
        } else {
            this.board.swapTiles(row, col + 1, row, col);
            blank_pos = row * board.getDifficulty() + (col + 1);
        }

        return blank_pos;
    }

    public void makeMove(int position) {
        addUndo(move(position));
    }

}