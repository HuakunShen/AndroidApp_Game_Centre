package csc207project.gamecenter.SlidingTiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import csc207project.gamecenter.Data.StateStack;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class BoardManager implements Serializable {

    private static int capacity;
    /**
     * The HaspMap used to store each user's username and a stack of their Sliding tile game states.
     */
    private static HashMap<String, StateStack<Board>> gameStates = new HashMap<>();

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    BoardManager(Board board) {
        this.board = board;
    }

    /**
     * Return the current board.
     */
    Board getBoard() {
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

        Collections.shuffle(tiles);
        this.board = new Board(tiles);
    }

    HashMap getGameStates(){
        return gameStates;
    }

    /**
     * Add a new game state into the gameStates.
     * @param userName
     * @param boardToAdd
     */
    void addState(String userName, Board boardToAdd){
        StateStack<Board> theStack = gameStates.get(userName);
        if(theStack.size() < capacity) {
            theStack.push(boardToAdd);
        }
        else{
            theStack.popFirst();
            theStack.push(boardToAdd);

        }
    }

    /**
     * Return the latest state from the gameStates.
     * @param userName
     * @return
     */
    Board getState(String userName){
        return gameStates.get(userName).popLast();
    }

    /**
     * Return true if user exists, otherwise, return false.
     * @param user
     * @return
     */
    boolean userExist(String user){
        return gameStates.containsKey(user);
    }

    /**
     * Return true if removing user succeed, otherwise, return false.
     * @param user
     * @return
     */
    boolean removeUser(String user){
        if(userExist(user)){
            gameStates.remove(user);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Add a new user to gameStates.
     * @param userName
     */
    void addUser(String userName){
        gameStates.put(userName, new StateStack<>());
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
        // tiles is the blank tile, swap by calling Board's swap method.
        if (row != 0 && board.getTile(row - 1, col).getId() == blankId) {
            board.swapTiles(row, col, row - 1, col);    // swap with tile above
        } else if (row != 3 && board.getTile(row + 1, col).getId() == blankId) {
            board.swapTiles(row, col, row + 1, col);    // swap with tile below
        } else if (col != 0 && board.getTile(row, col - 1).getId() == blankId) {
            board.swapTiles(row, col, row, col - 1);    // swap with tile left
        } else if (col != 3 && board.getTile(row, col + 1).getId() == blankId) {
            board.swapTiles(row, col, row, col + 1);    // swap with tile right
        }


    }

}
