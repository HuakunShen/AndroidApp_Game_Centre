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
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

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
    public boolean boardSolved() {
        Iterator<PictureTile> itr = board.iterator();
        for (int i = 1; i <= (MatchingBoard.NUM_ROWS * MatchingBoard.NUM_COLS); i++) {
            if (itr.hasNext() && !itr.next().getState().equals(PictureTile.SOLVED)) {
                return false;
            }
        }
        return true;
    }

    public void makeMove(int position, int value){
        int row = position / MatchingBoard.NUM_COLS;
        int col = position % MatchingBoard.NUM_COLS;
        this.board.flipTile(row,col);

        //set time here
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                MatchingBoard board = getBoard();
                int col1 = board.getCol1();
                int col2 = board.getCol2();
                if (col1 != -1 && col2 != -1){
                    board.solveTile();
                }
            }
        }, 1000);


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

    @Override
    public int getStepsTaken() {
        return 0;
    }

    @Override
    public void setStepsTaken(int stepsTakenSoFar) {

    }

    public long getTimeTaken() {
        return this.time;
    }
}
