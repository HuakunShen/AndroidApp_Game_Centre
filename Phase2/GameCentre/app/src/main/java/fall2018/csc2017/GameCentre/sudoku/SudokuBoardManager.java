package fall2018.csc2017.GameCentre.sudoku;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fall2018.csc2017.GameCentre.data.StateStack;
import fall2018.csc2017.GameCentre.util.BoardManagerForBoardGames;

public class SudokuBoardManager extends BoardManagerForBoardGames implements Serializable {

    /**
     * The board begin managed.
     */
    private SudokuBoard board;

    /**
     * The time has taken so far.
     */
    private long timeTaken;

    /**
     * The undoStack storing steps has taken.(limited capacity)
     */
    private StateStack<Integer[]> undoStack;

    /**
     * The default number of undo time.
     */
    private static final int DEFAULT_UNDO_LIMIT = 3;

    /**
     * The level of difficulty.
     */
    private static Integer levelOfDifficulty = 2;

    /**
     * The number of steps the user have taken so far.
     */
    private int stepsTaken;

    /**
     * Manage a new shuffled board.
     */
    public SudokuBoardManager() {
        super();
        List<Cell> cells = new ArrayList<>();
        Integer[][] newBoard = new BoardGenerator().getBoard();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                cells.add(new Cell(row, column, newBoard[row][column]));
            }
        }
        Integer editable = 0;
        if (levelOfDifficulty == 1) {
            editable = 18;
        } else if (levelOfDifficulty == 2) {
            editable = 36;
        } else if (levelOfDifficulty == 3) {
            editable = 54;
        }
        Integer changed = 0;
        while (!changed.equals(editable)) {
            Random r = new Random();
            int index = r.nextInt(81);
            if (!cells.get(index).isEditable()) {
                cells.get(index).makeEditable();
                cells.get(index).setFaceValue(0);
                changed++;
            }
        }
        this.board = new SudokuBoard(cells);
        this.timeTaken = 0L;
        this.undoStack = new StateStack<Integer[]>(DEFAULT_UNDO_LIMIT);
    }

    /**
     * Return the current board.
     */
    public SudokuBoard getBoard() {
        return board;
    }

    /**
     * Get the time which the user has already used.
     */
    public long getTimeTaken() {
        return timeTaken;
    }

    /**
     * Setter function for time taken.
     */
    public void setTimeTaken(long timeTakenSoFar) {
        this.timeTaken = timeTakenSoFar;
    }

    /**
     * Getter function for time taken.
     */
    public int getStepsTaken() {
        return (int) timeTaken;
    }

    /**
     * Setter function for steps taken.
     */
    public void setStepsTaken(int stepsTakenSoFar) {
        this.stepsTaken = stepsTakenSoFar;
    }

    /**
     * Add a move to the undo stack.
     */
    private void addUndo(Integer[] move) {
        undoStack.put(move);
    }

    /**
     * Set undo limit.
     */
    public void setCapacity(int input) {
        this.undoStack.setCapacity(input);
    }

    /**
     * Returns if undo is available.
     */
    boolean undoAvailable() {
        return !undoStack.isEmpty();
    }

    /**
     * Get the undo step.
     */
    Integer[] popUndo() {
        return (Integer[]) undoStack.pop();
    }


    /**
     * Setter for level of difficulty.
     */
    public static void setLevelOfDifficulty(int levelOfDifficulty) {
        SudokuBoardManager.levelOfDifficulty = (Integer) levelOfDifficulty;
    }

    /**
     * Returns whether the sudoku puzzle has been solved.
     */
    public boolean boardSolved() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (!board.checkCell(row, column)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Return whether the tap is valid.
     */
    public boolean isValidTap(int position) {
        return board.checkEditable(position / 9, position % 9);
    }

    /**
     * Performs changes to the board.
     */
    public void makeMove(int position, int value) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getCell(i, j).isHighlighted()) {
                    board.getCell(i, j).setHighlighted();
                    board.getCell(i,
                            j).setFaceValue(board.getCell(i,
                            j).getFaceValue());
                }
            }
        }
        board.getCell(position / 9, position % 9).setHighlighted();
        board.getCell(position / 9, position % 9).setFaceValue(value);
        setChanged();
        notifyObservers();
    }

    /**
     * Update the face value of the board.
     */
    void updateValue(int value, boolean undo) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getCell(i, j).isHighlighted() && !undo) {
                    Integer[] move = new Integer[2];
                    move[0] = 9 * i + j;
                    move[1] = board.getCell(i, j).getFaceValue();
                    addUndo(move);
                } if (board.getCell(i, j).isHighlighted()) {
                    board.getCell(i, j).setFaceValue(value);
                    setChanged();
                    notifyObservers();
                }
            }
        }
    }
}
