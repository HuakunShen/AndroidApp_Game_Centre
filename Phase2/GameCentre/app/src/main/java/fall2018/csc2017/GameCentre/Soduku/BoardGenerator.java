package fall2018.csc2017.GameCentre.Soduku;

import java.util.Arrays;
import java.util.Random;

public class BoardGenerator {

    public Integer[][] getBoard() {
        return board;
    }

    public Integer[][] board = new Integer[9][9];

    public BoardGenerator() {
        boardInitializer();
        juniorBoardShuffler(1);
        juniorBoardShuffler(0);
        System.out.println(Arrays.deepToString(board));
    }

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

    private void juniorBoardShuffler(int check) {
        int k1, k2;
        int max = 2;
        int min = 0;
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            k1 = r.nextInt(max - min + 1) + min;
            do {
                k2 = r.nextInt(max - min + 1) + min;
            } while (k1 == k2);
            max += 3;
            min += 3;
            if (check == 1)
                switchRows(k1, k2);
            else if (check == 0)
                switchColumns(k1, k2);
        }
    }

    public void seniorBoardShuffler(int k1, int k2) {
        int row_from;
        int row_to;
        int col_from;
        int col_to;
        int i, j, b, c;
        int rem1, rem2;
        int flag;
        int count = 9;
        for (i = 1; i <= 9; i++) {
            flag = 1;
            for (j = 0; j < 9; j++) {
                if (j != k2) {
                    if (i == board[k1][j]) {
                        flag = 0;
                        break;
                    }
                }
            }
            if (flag == 1) {
                for (c = 0; c < 9; c++) {
                    if (c != k1) {
                        if (i == board[c][k2]) {
                            flag = 0;
                            break;
                        }
                    }
                }
            }
            if (flag == 1) {
                rem1 = k1 % 3;
                rem2 = k2 % 3;
                row_from = k1 - rem1;
                row_to = k1 + (2 - rem1);
                col_from = k2 - rem2;
                col_to = k2 + (2 - rem2);
                for (c = row_from; c <= row_to; c++) {
                    for (b = col_from; b <= col_to; b++) {
                        if (c != k1 && b != k2) {
                            if (i == board[c][b]) {
                                flag = 0;
                                break;
                            }
                        }
                    }
                }
            }
            if (flag == 0)
                count--;
        }
        if (count == 1) {
            board[k1][k2] = 0;
        }
    }


    private void switchRows(int row1, int row2) {
        int cache;
        for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
            cache = board[row1][columnIndex];
            board[row1][columnIndex] = board[row2][columnIndex];
            board[row2][columnIndex] = cache;
        }
    }

    private void switchColumns(int col1, int col2) {
        int cache;
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            cache = board[rowIndex][col1];
            board[rowIndex][col1] = board[rowIndex][col2];
            board[rowIndex][col2] = cache;
        }
    }

    public void switchHorizontalGroups(int group1, int group2) {
        int cache;
        for (int n = 1; n <= 3; n++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                cache = board[group1][columnIndex];
                board[group1][columnIndex] = board[group2][columnIndex];
                board[group2][columnIndex] = cache;
            }
            group1++;
            group2++;
        }
    }

    public void switchVerticalGroups(int group1, int group2) {
        int cache;
        for (int n = 1; n <= 3; n++) {
            for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
                cache = board[rowIndex][group1];
                board[rowIndex][group1] = board[rowIndex][group2];
                board[rowIndex][group2] = cache;
            }
            group1++;
            group2++;
        }
    }
}
