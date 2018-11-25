package fall2018.csc2017.GameCentre.slidingTiles;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SlidingTilesBoardManagerTest {

    private SlidingTilesBoardManager boardManager;
    private SlidingTilesBoard board;

    @Before
    public void setUp() {
        List<Integer> tiles = new ArrayList<>();
        boardManager = new SlidingTilesBoardManager();
        board = new SlidingTilesBoard(tiles,3);
    }

    @Test
    public void getBoard() {
        assertEquals(board, boardManager.getBoard());
    }

    @Test
    public void setCapacity() {

    }

    @Test
    public void undoAvailable() {
    }

    @Test
    public void popUndo() {
    }

    @Test
    public void getDifficulty() {
    }

    @Test
    public void getStepsTaken() {
    }

    @Test
    public void setStepsTaken() {
    }

    @Test
    public void getTimeTaken() {
    }

    @Test
    public void setTimeTaken() {
    }

    @Test
    public void solvable() {
    }

    @Test
    public void blankPosition() {
    }

    @Test
    public void getTotalInversion() {
    }

    @Test
    public void getImageBackground() {
    }

    @Test
    public void setImageBackground() {
    }

    @Test
    public void boardSolved() {
    }

    @Test
    public void isValidTap() {
    }

    @Test
    public void move() {
    }

    @Test
    public void makeMove() {
    }
}