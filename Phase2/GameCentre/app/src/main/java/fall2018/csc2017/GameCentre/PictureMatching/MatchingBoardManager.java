package fall2018.csc2017.GameCentre.PictureMatching;

import android.graphics.Picture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MatchingBoardManager implements Serializable {
    /**
     * The board being managed.
     */
    private MatchingBoard board;
    private long time;


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
        final int numTiles = MatchingBoard.NUM_COLS*MatchingBoard.NUM_ROWS;
        for(int tileNum=0; tileNum < numTiles; tileNum++){
            int row = tileNum / MatchingBoard.NUM_COLS;
            int col = tileNum % MatchingBoard.NUM_COLS;
            if (tileNum < numTiles/4){
                tiles.add(new PictureTile(tileNum+1, row, col));
            }else if ( tileNum >= numTiles/4 && tileNum < numTiles/2){
                tiles.add(new PictureTile(tileNum-numTiles/4 + 1,row, col));
            }else if(tileNum >= numTiles/2 && tileNum < (numTiles*3)/4){
                tiles.add(new PictureTile(tileNum-numTiles/2 +1, row, col));
            }else{
                tiles.add(new PictureTile(tileNum-(numTiles*3)/4+1, row, col));
            }
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
            if (itr.hasNext() && !itr.next().getState().equals(PictureTile.SOLVED)) {
                return false;
            }
        }
        return true;
    }

    public void processTiles(int row, int col){
        this.board.flipTile(row,col);
        int col1 = this.board.getCol1();
        int col2 = this.board.getCol2();
        //set time here
        if (col1 != -1 && col2 != -1){
            this.board.solveTile();
        }
//        Timer duration = new Timer();
//        StuffToDo task = new StuffToDo(col1,col2, this.board);
//        duration.schedule(task, 1000);
//        this.board = task.board;
//        this.board.notifies();
    }
    class StuffToDo extends TimerTask{

        private final int col1;
        private final int col2;
        private final MatchingBoard board;

        StuffToDo(int col1, int col2, MatchingBoard board){
            this.col1 = col1;
            this.col2 = col2;
            this.board = board;
        }
        @Override
        public void run() {
            if (col1 != -1 && col2 != -1){
                this.board.solveTile();
            }
        }
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
