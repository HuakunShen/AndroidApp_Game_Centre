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
    public static int NUM_ROWS = 4;

    /**
     * The number of rows.
     */
    public static int NUM_COLS = 4;

    public int difficulty;

    private int col1=-1;
    private int col2=-1;
    private int row1=-1;
    private int row2=-1;

    public int getCol1() {
        return col1;
    }


    public int getCol2() {
        return col2;
    }


    /**
     * The tiles on the board in row-major order.
     */
    private PictureTile[][] tiles = new PictureTile[NUM_ROWS][NUM_COLS];



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

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    public MatchingBoard(List<PictureTile> tiles) {
        difficulty = NUM_COLS/2;
        Iterator<PictureTile> iter = tiles.iterator();
        for (int row = 0; row != MatchingBoard.NUM_ROWS; row++) {
            for (int col = 0; col != MatchingBoard.NUM_COLS; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    public void flipTile(int row, int col){
        if(col1 == -1 && row1 == -1){
            tiles[row][col].setState(PictureTile.FLIP);
            col1 = col;
            row1 = row;
        }else if (col2 == -1 && row2 == -1){
            tiles[row][col].setState(PictureTile.FLIP);
            col2 = col;
            row2 = row;
        }
        setChanged();
        notifyObservers();
    }
    public void solveTile(){
        PictureTile tile1 = tiles[row1][col1];
        PictureTile tile2 = tiles[row2][col2];
        if (tile1.getId() == tile2.getId()){
            tile1.setState(PictureTile.SOLVED);
            tile2.setState(PictureTile.SOLVED);
        }else{
            tile1.setState(PictureTile.COVERED);
            tile2.setState(PictureTile.COVERED);
        }
        this.col1 = -1;
        this.row1 = -1;
        this.col2 = -1;
        this.row2 = -1;
        setChanged();
        notifyObservers();
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
            int row = nextIndex / MatchingBoard.NUM_COLS;
            int col = nextIndex % MatchingBoard.NUM_COLS;
            PictureTile tile = tiles[row][col];
            nextIndex++;
            return tile;
        }
    }



}
