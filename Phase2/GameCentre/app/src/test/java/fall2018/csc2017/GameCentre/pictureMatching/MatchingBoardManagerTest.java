package fall2018.csc2017.GameCentre.pictureMatching;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class MatchingBoardManagerTest {

    MatchingBoardManager manager;

    @Before
    public void setUp() throws Exception {
//        List<PictureTile> tiles = generateTiles();
//        MatchingBoard board = new MatchingBoard();
        this.manager = new MatchingBoardManager();
        Iterator<PictureTile> itr = manager.getBoard().iterator();
        for (int i = 1; i <= (MatchingBoard.NUM_ROWS * MatchingBoard.NUM_COLS); i++) {
            if (itr.hasNext()) {
                itr.next().setState("solved");
            }
        }
    }

//    private List<PictureTile> generateTiles(){
//        List<PictureTile> tiles = new ArrayList<>();
//        final int numTiles = MatchingBoard.NUM_COLS * MatchingBoard.NUM_ROWS;
//        for (int tileNum = 0; tileNum < numTiles; tileNum++) {
//            manager.addPictureTileToList(tiles, numTiles, tileNum);
//        }
//        Collections.shuffle(tiles);
//    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public addPictureTileToList(){
//
//    }

    @Test
    public void testGetBoard() {
        assertEquals("MatchingBoard", manager.getClass().getName());
    }

    @Test
    public void testGetDifficulty() {
    }

    @Test
    public void testGetAndSetTimeTaken() {
        manager.setTimeTaken(8);
        assertEquals(8, manager.getTimeTaken());
    }

    @Test
    public void testBoardSolved() {
        assertTrue(manager.boardSolved());

    }

    @Test
    public void testIsValidTap() {
        assertTrue(manager.isValidTap(2));
    }

    @Test
    public void testMakeMove() {

    }
}