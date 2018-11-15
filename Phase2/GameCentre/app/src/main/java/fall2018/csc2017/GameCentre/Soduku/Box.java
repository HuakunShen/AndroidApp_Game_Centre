package fall2018.csc2017.GameCentre.Soduku;

import java.io.Serializable;

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
     * Whether the facevalue of the box can be edited.
     */
    private boolean editable;

    /**
     * The default constructor of Box.
     *
     * @param value The value of the box.
     */
    Box(int value) {
        this.solutionValue = value;
        this.faceValue = value;
        this.editable = false;
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
    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Returns whether the user successfully figured
     * out the value of the box.
     */
    public boolean checkValue() {
        return faceValue.equals(solutionValue);
    }

    /**
     * Returns whether the box is editable.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Make the box editable;
     */
    public void makeEditable() {
        this.editable = true;
    }
}
