package fall2018.csc2017.GameCentre.Sudoku;

import java.io.Serializable;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.SlidingTiles.Board;
import fall2018.csc2017.GameCentre.SlidingTiles.StartingActivity;

public class Cell implements Serializable {

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

    private boolean highlighted;

    private int row;
    private int col;

    /**
     * The default constructor of Cell.
     *
     * @param value The value of the box.
     */

    Cell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.solutionValue = value;
        this.editable = false;
        setFaceValue(value);
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
        String currentCellName;

        if (row / 3 == 0 || row / 3 == 2) {
            if (col / 3 == 1) {
                if (this.editable){
                    currentCellName = "red_blue_" + Integer.toString(faceValue);
                }else{
                    currentCellName = "black_blue_" + Integer.toString(faceValue);
                }

            } else {
                if (this.editable){
                    currentCellName = "red_white_" + Integer.toString(faceValue);
                }else{
                    currentCellName = "black_white_" + Integer.toString(faceValue);
                }

            }
        } else {
            if (col / 3 == 0 || col / 3 == 2) {
                if (this.editable){
                    currentCellName = "red_blue_" + Integer.toString(faceValue);
                }else{
                    currentCellName = "black_blue_" + Integer.toString(faceValue);
                }
            } else {
                if (this.editable){
                    currentCellName = "red_white_" + Integer.toString(faceValue);
                }else{
                    currentCellName = "black_white_" + Integer.toString(faceValue);
                }
            }
        }
        int currentCellID = SudokuStartingActivity.RESOURCES.getIdentifier(currentCellName, "drawable", SudokuStartingActivity.PACKAGE_NAME);
        background = currentCellID;
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

    int getBackground() {
        return background;
    }

    boolean isHighlighted() {
        return highlighted;
    }

    void setHighlighted(boolean value) {
        highlighted = value;
    }

}
