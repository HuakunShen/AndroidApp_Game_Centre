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
public class BoardManager extends BoardManagerForBoardGames implements Serializable {

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
    private StateStack<Integer> undoStack;

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
        boolean solvable = false;
        while (!solvable) {
            Collections.shuffle(tiles);
            this.board = new Board(tiles);
            solvable = solvable();
        }
        this.undoStack = new StateStack<Integer>(DEFAULT_UNDO_LIMIT);
    }

    /**
     * Manager a prepared board.
     */
    public BoardManager(Board board) {
        this.board = board;
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
    private void addUndo(Integer move) {
        undoStack.put(move);
    }

    /**
     * Set undo limit.
     */
    void setCapacity(int input) {
        this.undoStack.setCapacity(input);
    }

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
     * Determines whether the tile board is solvable.
     */
    public boolean solvable() {
        Iterator<Tile> tiles = this.board.iterator();
        ArrayList<Integer> listOfTiles = new ArrayList<>(this.board.numTiles());
        while (tiles.hasNext()) {
            listOfTiles.add(tiles.next().getId());
        }

        int totalInversion = getTotalInversion(listOfTiles);

        if (this.board.numTiles() % 2 != 0) {
            return totalInversion % 2 == 0;
        } else {
            if(board.numTiles()%2!=0){
                return totalInversion%2==0;
            }else{
                return blankPosition()%2==0 && totalInversion%2!=0 ||
                        blankPosition()%2!=0 && totalInversion%2==0;
            }
        }
    }

    public int blankPosition() {
        int position = 0;
        for (int i = 0; i < Board.NUM_ROWS; i++) {
            for (int j = 0; j < Board.NUM_COLS; j++) {
                if (board.getTile(i, j).getId() == board.numTiles()) {
                    position = Board.NUM_ROWS - i;
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
        for (int i = 0; i < this.board.numTiles()-1; i++) {
            for (int j = i + 1; j < this.board.numTiles(); j++) {
                if (listOfTiles.get(i) != this.board.numTiles() && listOfTiles.get(i) > listOfTiles.get(j)) {
                    totalInversion ++;
                }
            }
        }
        return totalInversion;
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
            addUndo(blank_pos);
        }
    }
}