package fall2018.csc2017.GameCentre.sudoku;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuBoardManagerTest {


    private SudokuBoardManager manager;
    private int moveTaken;
    @Before
    public void setUp() {
        manager = new SudokuBoardManager();
        moveTaken = findEditablePosition(manager.getBoard());
        manager.makeMove(moveTaken);
        int move = findEditablePosition(manager.getBoard());
        manager.updateValue(2, false);
        manager.undo();
        manager.setHint(5);
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
    public void getAndSetHint() {
        assertEquals(5, manager.getHint());
    }

    @Test
    public void reduceHint() {
        manager.reduceHint();
        assertEquals(4, manager.getHint());
    }

    @Test
    public void getBoard() {
        assertNotNull(manager.getBoard());
    }

    @Test
    public void getAndSetTimeTaken() {
        manager.setTimeTaken(6);
        assertEquals(6, manager.getTimeTaken());
    }

//    @Test
//    public void setCapacity() {
//    }

    @Test
    public void setAndGetCurrentCell() {
        manager.setCurrentCell(new Cell(1, 2, 3));
        assertNotNull(manager.getCurrentCell());
    }

    @Test
    public void undoAvailable() {
        assertFalse(manager.undoAvailable());
    }

//    @Test
//    public void popUndo() {
//
//    }

    @Test
    public void boardSolved() {
        assertFalse(manager.boardSolved());
    }

    @Test
    public void isValidTap() {
        assertTrue(manager.isValidTap(moveTaken));
    }

//    @Test
//    public void makeMove() {
//
//    }

//    @Test
//    public void updateValue() {
//
//    }

//    @Test
//    public void undo() {
//    }
}