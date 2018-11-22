package fall2018.csc2017.GameCentre.sudoku;

import java.io.Serializable;

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

    /**
     * Whether the cell is highlighted.
     */
    private boolean highlighted;

    /**
     * The face value when it was not highlighted.
     */
    private Integer fadeValue;

    /**
     * The row which the cell is in.
     */
    private int row;

    /**
     * The column which the cell is in.
     */
    private int col;

    /**
     * The constructor for Cell.
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

        // The number displayed is red if the cell
        // is editable, other wise black.
        if (this.editable)
            currentCellName = "red_";
        else
            currentCellName = "black_";

        // If the number is highlighted, the background
        // colour of the cell will be blue.
        if (this.highlighted) {
            currentCellName += "blue_";
        } else
        // In order to distinguish different 3 X 3 grids
        // on the board, adjacent grids will have different
        // background colours.
        if (row / 3 == 0 || row / 3 == 2) {
            if (col / 3 == 1) {
                currentCellName += "grey_";
            } else {
                currentCellName += "white_";
            }
            this.fadeValue = faceValue;
        } else {
            if (col / 3 == 0 || col / 3 == 2) {
                currentCellName += "grey_";
            } else {
                currentCellName += "white_";
            }
            this.fadeValue = faceValue;
        }

        // Match the background colour to the numeral value
        // to be displayed
        currentCellName += Integer.toString(faceValue);

        background = SudokuStartingActivity.RESOURCES.getIdentifier(currentCellName,
                "drawable", SudokuStartingActivity.PACKAGE_NAME);
    }

    /**
     * Getter function for fadevalue.
     */
    public Integer getFadeValue() {
        return fadeValue;
    }

    /**
     * Setter function for fadevalue.
     */
    public void setFadeValue(int fadeValue) {
        this.fadeValue = fadeValue;
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

    /**
     * Returns the background of the cell.
     */
    int getBackground() {
        return background;
    }

    /**
     * Returns whether the cell is highlighted.
     */
    boolean isHighlighted() {
        return highlighted;
    }

    /**
     * Set the cell to be highlighted / regular.
     */
    void setHighlighted() {
        highlighted = !highlighted;
    }

}
