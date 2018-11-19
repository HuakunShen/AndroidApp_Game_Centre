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
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The unique id.
     */
    private int id;

    private boolean highlighted = false;

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    public void setBackground(int background){
        this.background = background;
    }

//    /**
//     * A Tile with id and background. The background may not have a corresponding image.
//     *
//     * @param id         the id
//     * @param background the background
//     */
//    public PictureTile(int id, int background) {
//        this.id = id;
//        this.background = background;
//    }
    public void setHighlighted(boolean highlighted){
        this.highlighted = highlighted;
    }
    public boolean getHighlighted(){
        return this.highlighted;
    }
    /**
     * A tile with a background id; look up and set the id.
     *
     * @param backgroundId
     */
    public PictureTile(int backgroundId) {
        id = backgroundId + 1;
        int numTiles = MatchingBoard.NUM_ROWS * MatchingBoard.NUM_COLS;
        String currentTileName = "tile_" + Integer.toString(id);
        int currentTileID = PictureMatchingStartingActivity.RESOURCES.getIdentifier(currentTileName,
                "drawable", PictureMatchingStartingActivity.PACKAGE_NAME);
        int invisible = PictureMatchingStartingActivity.RESOURCES.getIdentifier("black_blue_0",
                "drawable", PictureMatchingStartingActivity.PACKAGE_NAME);
        if (id == numTiles) {
            background = R.drawable.tile_empty;
        }else if (highlighted){
            background = currentTileID;
        }else{
            background = invisible;
        }
}

    @Override
    public int compareTo(@NonNull PictureTile o) {
        return o.id - this.id;
    }
}
