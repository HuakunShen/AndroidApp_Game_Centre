package fall2018.csc2017.GameCentre.Soduku;

import java.io.Serializable;

import fall2018.csc2017.GameCentre.R;

public class Box implements Serializable {

    /**
     * The correct value of the box
     */
    private Integer solutionValue;

    /**
     * Whether the value of the box is displayed.
     */
    private Integer faceValue;

    /**
     * Whether the face value of the box can be edited.
     */
    private boolean editable;

    /**
     * The background ID to find the box image
     */
    private int background;

    /**
     * The default constructor of Box.
     *
     * @param value The value of the box.
     */
    Box(int value) {
        this.solutionValue = value;
        this.faceValue = value;
        this.editable = false;
        switch (faceValue) {
            case 1:
                background = R.drawable.tile_1;
                break;
            case 2:
                background = R.drawable.tile_2;
                break;
            case 3:
                background = R.drawable.tile_3;
                break;
            case 4:
                background = R.drawable.tile_4;
                break;
            case 5:
                background = R.drawable.tile_5;
                break;
            case 6:
                background = R.drawable.tile_6;
                break;
            case 7:
                background = R.drawable.tile_7;
                break;
            case 8:
                background = R.drawable.tile_8;
                break;
            case 9:
                background = R.drawable.tile_9;
                break;
            case 0:
                background = R.drawable.tile_empty;
                break;
        }
    }

    /**
     * Get the solution of the box.
     */
    public Integer getSolutionValue() {
        return solutionValue;
    }

    /**
     * Get the face value of the box.
     */
    public Integer getFaceValue() {
        return faceValue;
    }

    /**
     * Set the face value of the box.
     */
    void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
        switch (faceValue) {
            case 1:
                background = R.drawable.tile_1;
                break;
            case 2:
                background = R.drawable.tile_2;
                break;
            case 3:
                background = R.drawable.tile_3;
                break;
            case 4:
                background = R.drawable.tile_4;
                break;
            case 5:
                background = R.drawable.tile_5;
                break;
            case 6:
                background = R.drawable.tile_6;
                break;
            case 7:
                background = R.drawable.tile_7;
                break;
            case 8:
                background = R.drawable.tile_8;
                break;
            case 9:
                background = R.drawable.tile_9;
                break;
            case 0:
                background = R.drawable.tile_empty;
                break;
        }
    }


    /**
     * Returns whether the user successfully figured
     * out the value of the box.
     */
    boolean checkValue() {
        return faceValue.equals(solutionValue);
    }

    /**
     * Returns whether the box is editable.
     */
    boolean isEditable() {
        return editable;
    }

    /**
     * Make the box editable;
     */
    void makeEditable() {
        this.editable = true;
    }
}
