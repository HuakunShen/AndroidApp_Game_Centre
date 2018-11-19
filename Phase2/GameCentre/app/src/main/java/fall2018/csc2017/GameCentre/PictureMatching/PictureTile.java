package fall2018.csc2017.GameCentre.PictureMatching;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Observable;

import fall2018.csc2017.GameCentre.R;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class PictureTile implements Comparable<PictureTile>, Serializable {

    /**
     * The unique id.
     */
    private int id;
    public static final String SOLVED = "solved";
    public static final String FLIP = "flip";
    public static final String COVERED = "covered";

    private String state;
    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param id
     */
    public PictureTile(int id, int row, int col) {
        this.id = id ;
        this.state = COVERED;
        this.row = row;
        this.col = col;
        }


    @Override
    public int compareTo(@NonNull PictureTile o) {
        return o.id - this.id;
    }
}
