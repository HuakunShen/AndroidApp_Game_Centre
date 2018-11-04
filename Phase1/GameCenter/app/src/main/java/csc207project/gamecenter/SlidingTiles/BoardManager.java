package csc207project.gamecenter.SlidingTiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import csc207project.gamecenter.Data.StateStack;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class BoardManager implements Serializable {

    /**
     * The size of the capacity with default value 3/
     */
    private static int capacity = 3;

    /**
     * The HaspMap used to store each user's username and a stack of their Sliding tile game states.
     */
    private static HashMap<String, StateStack<Object>> gameStates = new HashMap<>();

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * Manage a new shuffled board.
     */
    BoardManager() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }

        Collections.shuffle(tiles);
        this.board = new Board(tiles);
    }

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    BoardManager(Board board) {
        this.board = board;
    }

    /**
     * Return the latest state from the gameStates.
     *
     * @param userName
     * @return
     */
    Object getState(String userName) {
        return gameStates.get(userName).get();
    }

    /**
     * Return the current board.
     */
    Board getBoard() {
        return board;
    }

    /**
     * Return the current board.
     */
    Board getBoard(String username) {
        if (gameStates.containsKey(username)) {
            return (Board) gameStates.get(username).get();
        } else {
            return getBoard();
        }
    }

    /**
     * Sets the current board
     */
    void setBoard(Board board) {
        this.board = board;
        this.board.notifyObservers();
    }

    /**
     * Returns the status file of the game.
     */
    HashMap getGameStates() {
        return gameStates;
    }

    /**
     * Sets how many times could the user undo.
     */
    public void setCapacity(String username, int capacity) {
        gameStates.get(username).setCapacity(capacity);
    }

    /**
     * Add a new game state into the gameStates.
     *
     * @param userName
     * @param boardToAdd
     */
    void addState(String userName, Board boardToAdd) {
        StateStack<Object> theStack = gameStates.get(userName);
        theStack.put(boardToAdd);
        gameStates.put(userName, theStack);
    }

    /**
     * Returns whether the user has any undo steps.
     */
    boolean undoAvailable(String user) {
        return gameStates.keySet().contains(user) &&
                !gameStates.get(user).isEmpty();
    }

    /**
     * Return true if user exists, otherwise, return false.
     */
    boolean userExist(String user) {
        return gameStates.keySet().contains(user);
    }

    /**
     * Return true if removing user succeed, otherwise, return false.
     */
    boolean removeUser(String user) {
        if (userExist(user)) {
            gameStates.remove(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add a new user to gameStates.
     */
    void addUser(String userName) {
        gameStates.put(userName, new StateStack<>(capacity));
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        Iterator<Tile> itr = board.iterator();
        for (int i = 1; i <= board.numTiles(); i++) {
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
    boolean isValidTap(int position) {
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
    void touchMove(int position) {
        int row = position / Board.NUM_ROWS;
        int col = position % Board.NUM_COLS;
        int blankId = board.numTiles();
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == Board.NUM_ROWS - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        if (above != null && above.getId() == blankId) {
            this.board.swapTiles(row - 1, col, row, col);
        } else if (below != null && below.getId() == blankId) {
            this.board.swapTiles(row + 1, col, row, col);
        } else if (left != null && left.getId() == blankId) {
            this.board.swapTiles(row, col - 1, row, col);
        } else {
            this.board.swapTiles(row, col + 1, row, col);
        }
    }

}
