package fall2018.csc2017.GameCentre.Soduku;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class Board implements Serializable {

    /**
     * The boxes on the board.
     */
    private Box[][] boxes = new Box[9][9];

    /**
     * A new board of boxes in row-major order.
     * Precondition: len(boxes) == 81.
     *
     * @param boxes the boxes for the board
     */
    Board(List<Box> boxes) {
        Iterator<Box> iterator = boxes.iterator();

        for (int row = 0; row != 9; row++) {
            for (int col = 0; col != 9; col++) {
                this.boxes[row][col] = iterator.next();
            }
        }
    }

    /**
     * Get the box at the ith row and jth column
     */
    public Box getBox(int row, int column) {
        return this.boxes[row][column];
    }

    /**
     * Check whether the box at the ith row and jth column
     * has been correctly solved.
     */
    boolean checkBox(int row, int col) {
        return this.boxes[row][col].checkValue();
    }

    /**
     * Check whether the face value of box at the ith row and
     * jth column can be edited.
     */
    boolean checkEditable(int row, int col) {
        return this.boxes[row][col].isEditable();
    }
}
