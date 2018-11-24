package fall2018.csc2017.GameCentre.slidingTiles;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.util.BoardForBoardGames;

/**
 * The sliding tiles board.
 */
public class SlidingTilesBoard extends BoardForBoardGames implements Serializable, Iterable<Tile> {

    /**
     * The number of rows.
     */
    public static int NUM_ROWS = 4;

    /**
     * The number of rows.
     */
    public static int NUM_COLS = 4;

    /**
     * Level of difficulty of the game.
     */
    public int difficulty;

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles;


    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    public SlidingTilesBoard(List<Tile> tiles) {
        difficulty = NUM_ROWS;
        this.tiles = new Tile[difficulty][difficulty];
        Iterator<Tile> iter = tiles.iterator();

        for (int row = 0; row != SlidingTilesBoard.NUM_ROWS; row++) {
            for (int col = 0; col != SlidingTilesBoard.NUM_COLS; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Return the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    int numTiles() {
        return NUM_COLS * NUM_ROWS;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    public void swapTiles(int row1, int col1, int row2, int col2) {
        Tile t = this.tiles[row1][col1];
        this.tiles[row1][col1] = this.tiles[row2][col2];
        this.tiles[row2][col2] = t;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the iterator of tiles.
     */
    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        return new BoardIterator();
    }

    /**
     * The iterator class for board.
     */
    public class BoardIterator implements Iterator<Tile> {
        int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex != NUM_COLS * NUM_ROWS;
        }

        @Override
        public Tile next() {
            int row = nextIndex / SlidingTilesBoard.NUM_COLS;
            int col = nextIndex % SlidingTilesBoard.NUM_COLS;
            Tile tile = tiles[row][col];
            nextIndex++;
            return tile;
        }
    }
}
