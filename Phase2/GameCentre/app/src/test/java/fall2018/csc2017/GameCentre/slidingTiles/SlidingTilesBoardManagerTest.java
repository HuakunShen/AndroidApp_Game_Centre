package fall2018.csc2017.GameCentre.slidingTiles;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fall2018.csc2017.GameCentre.data.StateStack;

import static org.junit.Assert.*;

public class SlidingTilesBoardManagerTest {

    private SlidingTilesBoardManager boardManager;
    private SlidingTilesBoard board;

    @Before
    public void setUp() {
        List<Integer> tiles = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        board = new SlidingTilesBoard(tiles,3);
        boardManager = new SlidingTilesBoardManager(board);
        boardManager.makeMove(7);
    }

    @Test
    public void getBoard() {
        assertEquals(board, boardManager.getBoard());
    }

    @Test
    public void setAndGetCapacity() {
        boardManager.setCapacity(2);
        assertEquals(2, boardManager.getCapacity());
    }

    @Test
    public void undoAvailable() {
        assertTrue(boardManager.undoAvailable());
    }

    @Test
    public void popUndo() {
        assertEquals((Integer) 8, boardManager.popUndo());
    }
//
//    @Test
//    public void getDifficulty() {
//    }
//
//    @Test
//    public void getStepsTaken() {
//    }
//
//    @Test
//    public void setStepsTaken() {
//    }
//
//    @Test
//    public void getTimeTaken() {
//    }
//
//    @Test
//    public void setTimeTaken() {
//    }
//
//    @Test
//    public void solvable() {
//        assertFalse(boardManager.solvable());
//    }

//    @Test
//    public void blankPosition() {
//        assertEquals(9, boardManager.blankPosition());
//    }
//
//    @Test
//    public void getTotalInversion() {
//        assertEquals(1, boardManager.getTotalInversion());
//    }

//    @Test
//    public void getImageBackground() {
//    }
//
//    /**
//     * This tests the functionality of set
//     */
//    @Test
//    public void setImageBackground() {
//    }

    /**
     * This tests the functionality of boardSolved().
     */
    @Test
    public void boardSolved() {
        assertFalse(boardManager.boardSolved());
    }

    /**
     * This tests the functionality of isValidTap.
     */
    @Test
    public void isValidTap() {
        assertFalse(boardManager.isValidTap(1));
    }

//    /**
//     * This tests makeMove(), undoAvailable(), move(), and addUndo().
//     */
//    @Test
//    public void makeMove() {
//        assertTrue(boardManager.undoAvailable());
//    }
}