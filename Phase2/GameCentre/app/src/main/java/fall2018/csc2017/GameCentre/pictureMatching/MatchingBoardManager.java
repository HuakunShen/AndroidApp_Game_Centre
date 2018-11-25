package fall2018.csc2017.GameCentre.pictureMatching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.util.BoardManagerForBoardGames;


public class MatchingBoardManager extends BoardManagerForBoardGames implements Serializable {

    /**
     * The board being managed.
     */
    private MatchingBoard board;

    /**
     * The time has taken so far.
     */
    private long timeTaken;

    /**
     * Manage a new shuffled board.
     */
    MatchingBoardManager() {
        List<PictureTile> tiles = new ArrayList<>();
        final int numTiles = MatchingBoard.NUM_COLS * MatchingBoard.NUM_ROWS;
        for (int tileNum = 0; tileNum < numTiles; tileNum++) {
            addPictureTileToList(tiles, numTiles, tileNum);
        }
        Collections.shuffle(tiles);
        this.board = new MatchingBoard(tiles);
    }

    /**
     * Add pictureTiles to the list.
     * @param tiles the tiles List that contains pictureTile.
     * @param numTiles the id of the picture Tile.
     * @param tileNum the Total number of tiles need to be added.
     */
    private void addPictureTileToList(List<PictureTile> tiles, int numTiles, int tileNum) {
        if (tileNum < numTiles / 4) {
            tiles.add(new PictureTile(tileNum + 1));
        } else if (tileNum >= numTiles / 4 && tileNum < numTiles / 2) {
            tiles.add(new PictureTile(tileNum - numTiles / 4 + 1));
        } else if (tileNum >= numTiles / 2 && tileNum < (numTiles * 3) / 4) {
            tiles.add(new PictureTile(tileNum - numTiles / 2 + 1));
        } else {
            tiles.add(new PictureTile(tileNum - (numTiles * 3) / 4 + 1));
        }
    }

    /**
     * Return the current board.
     */
    public MatchingBoard getBoard() {
        return board;
    }

    /**
     * Getter function for level of difficulty for the game.
     */
    int getDifficulty() {
        return board.difficulty;
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
     * Return true if all pictureTiles are solved.
     *
     * @return whether the tiles are all solved.
     */
    public boolean boardSolved() {
        Iterator<PictureTile> itr = board.iterator();
        for (int i = 1; i <= (MatchingBoard.NUM_ROWS * MatchingBoard.NUM_COLS); i++) {
            if (itr.hasNext() && !itr.next().getState().equals(PictureTile.SOLVED)) {
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
        int row = position / MatchingBoard.NUM_COLS;
        int col = position % MatchingBoard.NUM_COLS;
        PictureTile currentTile = this.board.getTile(row, col);
        return !currentTile.getState().equals(PictureTile.SOLVED)
                && !currentTile.getState().equals(PictureTile.FLIP);
    }

    /**
     * Performs changes to the board.
     *
     * @param position the position that the user clicked on the grid view
     */
    public void makeMove(int position) {
        int row = position / MatchingBoard.NUM_COLS;
        int col = position % MatchingBoard.NUM_COLS;
        this.board.flipTile(row, col);

        //set timeTaken here
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MatchingBoard board = getBoard();
                int col1 = board.getCol1();
                int col2 = board.getCol2();
                if (col1 != -1 && col2 != -1) {
                    board.solveTile();
                }
            }
        }, 1000);
    }

}
