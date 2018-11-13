package fall2018.csc2017.GameCentre;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.GameCentre.SlidingTiles.Board;
import fall2018.csc2017.GameCentre.SlidingTiles.BoardManager;
import fall2018.csc2017.GameCentre.SlidingTiles.Tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoardAndTileTest {

    /** The board manager for testing. */
    private BoardManager boardManager;

    /**
     * Make a set of tiles that are in order.
     * @return a set of tiles that are in order
     */
    private List<Tile> makeTilesFourByFour() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = 16;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        return tiles;
    }

    /**
     * Make a set of tiles that are in order.
     * @return a set of tiles that are in order
     */
    private List<Tile> makeTilesThreeByThree() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = 9;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        return tiles;
    }

    /**
     * Make a solved Board.
     */
    private void setUpCorrectFourByFour() {
        List<Tile> tiles = makeTilesFourByFour();
        Board.NUM_COLS = 4;
        Board.NUM_ROWS = 4;
        Board board = new Board(tiles);
        boardManager = new BoardManager(board);
    }

    /**
     * Make a solved Board.
     */
    private void setUpCorrectThreeByThree() {
        List<Tile> tiles = makeTilesThreeByThree();
        Board.NUM_COLS = 3;
        Board.NUM_ROWS = 3;
        Board board = new Board(tiles);
        boardManager = new BoardManager(board);
    }

    /**
     * Shuffle a few tiles.
     */
    private void swapFirstTwoTiles() {
        boardManager.getBoard().swapTiles(0, 0, 0, 1);
    }

    /**
     * Test whether swapping two tiles makes a solved board unsolved.
     */
    @Test
    public void testIsSolved() {
        setUpCorrectFourByFour();
        assertTrue(boardManager.puzzleSolved());
        swapFirstTwoTiles();
        assertFalse(boardManager.puzzleSolved());
    }

    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void testSwapFirstTwo() {
        setUpCorrectFourByFour();
        assertEquals(1, boardManager.getBoard().getTile(0, 0).getId());
        assertEquals(2, boardManager.getBoard().getTile(0, 1).getId());
        boardManager.getBoard().swapTiles(0, 0, 0, 1);
        assertEquals(2, boardManager.getBoard().getTile(0, 0).getId());
        assertEquals(1, boardManager.getBoard().getTile(0, 1).getId());
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void testSwapLastTwo() {
        setUpCorrectFourByFour();
        assertEquals(15, boardManager.getBoard().getTile(3, 2).getId());
        assertEquals(16, boardManager.getBoard().getTile(3, 3).getId());
        boardManager.getBoard().swapTiles(3, 3, 3, 2);
        assertEquals(16, boardManager.getBoard().getTile(3, 2).getId());
        assertEquals(15, boardManager.getBoard().getTile(3, 3).getId());
    }

    /**
     * Test whether isValidHelp works.
     */
    @Test
    public void testIsValidTap() {
        setUpCorrectFourByFour();
        assertTrue(boardManager.isValidTap(11));
        assertTrue(boardManager.isValidTap(14));
        assertFalse(boardManager.isValidTap(10));
    }

    /**
     * Test whether solvable method works.
     */
    @Test
    public void testSolvableEvenWidth() {
        setUpCorrectFourByFour();
        assertTrue(boardManager.solvable());
        swapFirstTwoTiles();
        assertFalse(boardManager.solvable());
    }

    /**
     * Test whether solvable method works.
     */
    @Test
    public void testSolvableOddWidth() {
        setUpCorrectThreeByThree();
        assertTrue(boardManager.solvable());
        swapFirstTwoTiles();
        assertFalse(boardManager.solvable());
    }

}

