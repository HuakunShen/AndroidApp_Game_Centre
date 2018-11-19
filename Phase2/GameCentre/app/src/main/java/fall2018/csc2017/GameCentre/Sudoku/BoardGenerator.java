package fall2018.csc2017.GameCentre.Sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BoardGenerator {

    /**
     * The board
     */
    private Integer[][] board = new Integer[9][9];

    /**
     * Constructor for the BoardGenerator Class.
     */
    BoardGenerator() {
        boardInitializer();
        for (int n = 0; n < 100; n++) {
            juniorBoardShuffler(0);
            seniorBoardShuffler(0);
        }
        for (int n = 0; n < 100; n++) {
            juniorBoardShuffler(1);
            seniorBoardShuffler(1);
        }
    }

    /**
     * Getter function for the board.
     */
    Integer[][] getBoard() {
        return board;
    }

    /**
     * Initialize a board that satisfies the Sudoku requirements.
     */
    private void boardInitializer() {
        int boxValue;
        int firstBoxValue = 1;
        for (int row = 0; row < 9; row++) {
            boxValue = firstBoxValue;
            for (int column = 0; column < 9; column++) {
                if (boxValue <= 9) {
                    board[row][column] = boxValue;
                    boxValue++;
                } else {
                    boxValue = 1;
                    board[row][column] = boxValue;
                    boxValue++;
                }
            }
            firstBoxValue = boxValue + 3;
            if (boxValue == 10)
                firstBoxValue = 4;
            if (firstBoxValue > 9)
                firstBoxValue = (firstBoxValue % 9) + 1;
        }
    }

    /**
     * Shuffle the game board.
     */
    private void juniorBoardShuffler(int check) {
        int k1 = 0, k2 = 0;
        int startingIndex = 0;
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            while (k1 == k2) {
                k1 = r.nextInt(3) + startingIndex;
                k2 = r.nextInt(3) + startingIndex;
            }
            if (check == 1)
                switchRows(k1, k2);
            else if (check == 0)
                switchColumns(k1, k2);
            startingIndex += 3;
        }
    }

    /**
     * Further Shuffle the GameBoard.
     */
    private void seniorBoardShuffler(int check) {
        Random r = new Random();
        int k1;
        int k2;
        for (int n = 0; n < 100; n++) {
            if (check == 0) {
                k1 = r.nextInt(3) + 1;
                k2 = r.nextInt(3) + 1;
                while (k1 == k2) {
                    k1 = r.nextInt(3) + 1;
                    k2 = r.nextInt(3) + 1;
                }
                switchVerticalGroups(k1, k2);
            } else if (check == 1) {
                k1 = r.nextInt(3) + 1;
                k2 = r.nextInt(3) + 1;
                while (k1 == k2) {
                    k1 = r.nextInt(3) + 1;
                    k2 = r.nextInt(3) + 1;
                }
                switchHorizontalGroups(k1, k2);
            }
        }
    }

    /**
     * Switch two rows on the game board.
     */
    private void switchRows(int row1, int row2) {
        int cache;
        for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
            cache = board[row1][columnIndex];
            board[row1][columnIndex] = board[row2][columnIndex];
            board[row2][columnIndex] = cache;
        }
    }

    /**
     * Switch two columns on the game board.
     */
    private void switchColumns(int col1, int col2) {
        int cache;
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            cache = board[rowIndex][col1];
            board[rowIndex][col1] = board[rowIndex][col2];
            board[rowIndex][col2] = cache;
        }
    }

    /**
     * Switch two groups of rows in the game board.
     */
    private void switchHorizontalGroups(int group1, int group2) {
        int row1 = 3 * group1 - 3;
        int row2 = 3 * group2 - 3;
        int cache;
        for (int n = 1; n <= 3; n++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                cache = board[row1][columnIndex];
                board[row1][columnIndex] = board[row2][columnIndex];
                board[row2][columnIndex] = cache;
            }
            row1++;
            row2++;
        }
    }

    /**
     * Switch two groups of columns in the game board.
     */
    private void switchVerticalGroups(int group1, int group2) {
        int row1 = 3 * group1 - 3;
        int row2 = 3 * group2 - 3;
        int cache;
        for (int n = 1; n <= 3; n++) {
            for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
                cache = board[rowIndex][row1];
                board[rowIndex][row1] = board[rowIndex][row2];
                board[rowIndex][row2] = cache;
            }
            row1++;
            row2++;
        }
    }

    public boolean sudokuSolvable(){
        return solvableRow() && solvableColumn() && solvableSquare();
    }

    public boolean solvableRow(){
        for(int i = 0; i < 9; i++){
            if (!rowIsValid(board[i])){
                return false;
            }
        }
        return true;
    }

    public boolean rowIsValid(Integer[] row){
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        if(row.length!=9){
            return false;
        }
        for(int i = 0; i < 9; i++) {
            if (data.contains(row[i])) {
                data.remove(row[i]);
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean solvableColumn(){
        for(int i = 0; i < 9; i++){
            if (!columnIsValid(i)){
                return false;
            }
        }
        return true;
    }

    public boolean columnIsValid(int colIndex){
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        if(board.length!=9){
            return false;
        }
        for(int i =0;i<9;i++){
            if (data.contains(board[i][colIndex])) {
                data.remove(board[i][colIndex]);
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean solvableSquare(){
        for(int i =0; i<3; i++){
            if(!groupValid(i)){
                return false;
            }
        }
        return true;
    }

    public boolean groupValid(int row){
        return squareValid(row, 0) &&
                squareValid(row, 3) &&
                squareValid(row, 6);
    }

    public boolean squareValid(int groupNum, int groupCol){
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        ArrayList<Integer> cells = new ArrayList<>();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                cells.add(board[groupNum*3+i][groupCol+j]);
            }
        }
        for(int i =0;i<9;i++){
            if (data.contains(cells.get(i))) {
                data.remove(cells.get(i));
            } else {
                return false;
            }
        }
        return true;
    }


}
