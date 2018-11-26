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

    /**
     * This sets up necessary steps for the following tests.
     */
    @Before
    public void setUp() {
        List<Integer> tiles = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        board = new SlidingTilesBoard(tiles, 3);
        boardManager = new SlidingTilesBoardManager(board);
        boardManager.makeMove(7);
        boardManager.setStepsTaken(1);
    }

    /**
     * This tests the functionality of getBoard().
     */
    @Test
    public void getBoard() {
        assertEquals(board, boardManager.getBoard());
    }

    /**
     * This tests the functionality of setCapacity() and getCapacity().
     */
    @Test
    public void setAndGetCapacity() {
        boardManager.setCapacity(2);
        assertEquals(2, boardManager.getCapacity());
    }

    /**
     * This tests the functionality of undoAvailable().
     */
    @Test
    public void undoAvailable() {
        assertTrue(boardManager.undoAvailable());
    }

    /**
     * This tests the functionality of popUndo().
     */
    @Test
    public void popUndo() {
        assertEquals((Integer) 8, boardManager.popUndo());
    }

    /**
     * This tests the functionality of getDifficulty().
     */
    @Test
    public void getDifficulty() {
        assertEquals(3, boardManager.getDifficulty());
    }

    /**
     * This tests the functionality of getStepsTaken().
     */
    @Test
    public void getStepsTaken() {
        assertEquals(1, boardManager.getStepsTaken());
    }

    /**
     * This tests the functionality of getTimeTaken() and setTimeTaken().
     */
    @Test
    public void getAndSetTimeTaken() {
        boardManager.setTimeTaken(5);
        assertEquals(5, boardManager.getTimeTaken());
    }

    /**
     * This tests the functionality of solvable().
     */
    @Test
    public void solvable() {
        assertTrue(boardManager.solvable());
    }

    /**
     * This tests the functionality of getImageBackground().
     */
    @Test
    public void getImageBackground() {
        assertNull(boardManager.getImageBackground());
    }

    /**
     * This tests the functionality of setImageBackground().
     */
    @Test
    public void setImageBackground() {
    }

    /**
     * This tests the functionality of boardSolved().
     */
    @Test
    public void boardSolved() {
        assertFalse(boardManager.boardSolved());
    }

    /**
     * This tests the functionality of isValidTap().
     */
    @Test
    public void isValidTap() {
        assertFalse(boardManager.isValidTap(1));
    }

    /**
     * This tests the functionality of move().
     */
    @Test
    public void move() {
        boardManager.move(8);
    }

    /**
     * This tests the functionality of addUndo().
     */
    @Test
    public void addUndo() {
        boardManager.addUndo(8);
        assertEquals((Integer) 8, boardManager.popUndo());
    }
}