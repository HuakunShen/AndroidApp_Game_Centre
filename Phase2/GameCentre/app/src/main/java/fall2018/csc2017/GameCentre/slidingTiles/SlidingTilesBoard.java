package fall2018.csc2017.GameCentre.slidingTiles;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.util.BoardForBoardGames;

/**
 * The sliding tiles board.
 */
public class SlidingTilesBoard extends BoardForBoardGames implements Serializable, Iterable<Integer> {

    /**
     * Level of difficulty of the game.
     */
    private int difficulty;

    /**
     * The tiles on the board in row-major order.
     */
    private Integer[][] tiles;


    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    public SlidingTilesBoard(List<Integer> tiles, int difficulty) {
        this.difficulty = difficulty;
        this.tiles = new Integer[difficulty][difficulty];
        Iterator<Integer> iter = tiles.iterator();

        for (int row = 0; row != difficulty; row++) {
            for (int col = 0; col != difficulty; col++) {
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
        return difficulty * difficulty;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    public Integer getTile(int row, int col) {
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
        int t = this.tiles[row1][col1];
        this.tiles[row1][col1] = this.tiles[row2][col2];
        this.tiles[row2][col2] = t;
        setChanged();
        notifyObservers();
    }
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the iterator of tiles.
     */
    @NonNull
    @Override
    public Iterator<Integer> iterator() {
        return new BoardIterator();
    }

    /**
     * The iterator class for board.
     */
    public class BoardIterator implements Iterator<Integer> {
        int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex != difficulty * difficulty;
        }

        @Override
        public Integer next() {
            int row = nextIndex / difficulty;
            int col = nextIndex % difficulty;
            Integer tile = tiles[row][col];
            nextIndex++;
            return tile;
        }
    }
}
