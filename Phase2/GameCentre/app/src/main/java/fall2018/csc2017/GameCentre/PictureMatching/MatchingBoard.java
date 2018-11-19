package fall2018.csc2017.GameCentre.PictureMatching;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;


/**
 * The sliding tiles board.
 * TODO: Make this implement Iterable<Tile>.
 */
public class MatchingBoard extends Observable implements Serializable, Iterable<PictureTile> {
    /**
     * The number of rows.
     */
    public static int NUM_ROWS = 3;

    /**
     * The number of rows.
     */
    public static int NUM_COLS = 4;

    public int difficulty;

    /**
     * The tiles on the board in row-major order.
     */
    private PictureTile[][] tiles = new PictureTile[NUM_ROWS][NUM_COLS];


    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    public MatchingBoard(List<PictureTile> tiles) {
//        difficulty = NUM_COLS;
        Iterator<PictureTile> iter = tiles.iterator();
        for (int row = 0; row != MatchingBoard.NUM_ROWS; row++) {
            for (int col = 0; col != MatchingBoard.NUM_COLS; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }


    @Override
    public Iterator<PictureTile> iterator() {
        return new MatchingBoard.MatchingBoardIterator();
    }

    public class MatchingBoardIterator implements Iterator<PictureTile> {
        int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex != NUM_COLS * NUM_ROWS;
        }

        @Override
        public PictureTile next() {
            int row = nextIndex / MatchingBoard.NUM_ROWS;
            int col = nextIndex % MatchingBoard.NUM_COLS;
            PictureTile tile = tiles[row][col];
            nextIndex++;
            return tile;
        }
    }


    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    public PictureTile getTile(int row, int col) {
        return tiles[row][col];
    }

//    /**
//     * Swap the tiles at (row1, col1) and (row2, col2)
//     *
//     * @param row1 the first tile row
//     * @param col1 the first tile col
//     * @param row2 the second tile row
//     * @param col2 the second tile col
//     */
//    public void swapTiles(int row1, int col1, int row2, int col2) {
//        // TODO: swap
//        Tile t = this.tiles[row1][col1];
//        this.tiles[row1][col1] = this.tiles[row2][col2];
//        this.tiles[row2][col2] = t;
//        setChanged();
//        notifyObservers();
//    }

}
