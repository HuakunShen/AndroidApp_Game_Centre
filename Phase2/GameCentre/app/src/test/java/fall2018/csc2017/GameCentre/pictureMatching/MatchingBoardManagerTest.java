package fall2018.csc2017.GameCentre.pictureMatching;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MatchingBoardManagerTest {

    private MatchingBoardManager boardManager;

    /**
     * Set up a board for testing.
     */
    public void setUp(int difficulty){
        boardManager = new MatchingBoardManager(difficulty, "number");
        List<PictureTile> tiles = new ArrayList<>();
        for (int tileNum = 0; tileNum < difficulty * difficulty; tileNum++) {
            boardManager.addPictureTileToList(tiles, difficulty * difficulty, tileNum);
        }
        Collections.shuffle(tiles);
        boardManager.setBoard(new MatchingBoard(tiles, difficulty));
    }


    @Test
    public void testGetBoard() {
        assertEquals("fall2018.csc2017.GameCentre.pictureMatching.MatchingBoardManager",
                boardManager.getClass().getName().toString());
    }

    @Test
    public void testGetDifficulty() {
    }

    @Test
    public void testGetAndSetTimeTaken() {
        boardManager.setTimeTaken(8);
        assertEquals(8, boardManager.getTimeTaken());
    }

    @Test
    public void testBoardSolved() {
        assertTrue(boardManager.gameFinished());

    }

    @Test
    public void testIsValidTap() {
        assertFalse(boardManager.isValidTap(2));
    }
}