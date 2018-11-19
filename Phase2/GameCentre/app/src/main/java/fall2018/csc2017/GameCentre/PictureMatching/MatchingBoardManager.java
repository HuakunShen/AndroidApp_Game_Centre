package fall2018.csc2017.GameCentre.PictureMatching;

import android.graphics.Picture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class MatchingBoardManager implements Serializable {
    /**
     * The board being managed.
     */
    private MatchingBoard board;
    private long time;

    public MatchingBoardManager(MatchingBoard board){this.board = board;}

    /**
     * Return the current board.
     */
    public MatchingBoard getBoard() {
        return board;
    }

    /**
     * Manage a new shuffled board.
     */
    MatchingBoardManager() {
        List<PictureTile> tiles = new ArrayList<>();
        final int numTiles = MatchingBoard.NUM_ROWS * MatchingBoard.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles/2; tileNum++) {
            tiles.add(new PictureTile(tileNum));
        }
        for (int tileNum = 0; tileNum != numTiles/2; tileNum++) {
            tiles.add(new PictureTile(tileNum));
        }
        Collections.shuffle(tiles);
        this.board = new MatchingBoard(tiles);
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    public boolean puzzleSolved() {
        Iterator<PictureTile> itr = board.iterator();
        for (int i = 1; i <= (MatchingBoard.NUM_ROWS * MatchingBoard.NUM_COLS); i++) {

            if (itr.hasNext() && itr.next().getId() != MatchingBoard.NUM_ROWS * MatchingBoard.NUM_COLS) {
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

        int row = position / MatchingBoard.NUM_ROWS;
        int col = position % MatchingBoard.NUM_COLS;
        int blankId = 0;
        // Are any of the 4 the blank tile?
        PictureTile currentTile = this.board.getTile(row, col);
        return currentTile.getId() != MatchingBoard.NUM_COLS*MatchingBoard.NUM_ROWS;
    }

    public void cancelPicture(int row, int col){
        this.board.getHighlightedTile().setBackground(this.board.getTotalTiles()-1);
        this.board.getTile(row,col).setBackground(this.board.getTotalTiles()-1);
        this.board.getHighlightedTile().setHighlighted(false);
    }

    int getDifficulty() {
        return board.difficulty;
    }

    public void setTimeTaken(long l) {
        this.time = l;
    }

    public Long getTimeTaken() {
        return this.time;
    }
}
