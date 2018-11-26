package fall2018.csc2017.GameCentre.sudoku;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuBoardManagerTest {


    private SudokuBoardManager manager;
    @Before
    public void setUp() {
        manager = new SudokuBoardManager();
        int position = findEditablePosition(manager.getBoard());
        manager.makeMove(position);
    }

    private int findEditablePosition(SudokuBoard board){
        int position = -1;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(board.getCell(i, j).isEditable()){
                    position = i * 9 + j;
                    break;
                }
            }
        }
        return position;
    }

    @Test
    public void getHint() {
    }

    @Test
    public void setHint() {
    }

    @Test
    public void reduceHint() {
    }

    @Test
    public void getBoard() {
    }

    @Test
    public void getTimeTaken() {
    }

    @Test
    public void setTimeTaken() {
    }

    @Test
    public void setCapacity() {
    }

    @Test
    public void setCurrentCell() {
    }

    @Test
    public void getCurrentCell() {
    }

    @Test
    public void undoAvailable() {
    }

    @Test
    public void popUndo() {
    }

    @Test
    public void setLevelOfDifficulty() {
    }

    @Test
    public void boardSolved() {
    }

    @Test
    public void isValidTap() {
    }

    @Test
    public void makeMove() {
    }

    @Test
    public void updateValue() {
    }

    @Test
    public void undo() {
    }
}