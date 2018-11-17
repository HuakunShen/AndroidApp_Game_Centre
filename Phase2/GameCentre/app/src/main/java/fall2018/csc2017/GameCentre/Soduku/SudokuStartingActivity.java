package fall2018.csc2017.GameCentre.Soduku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fall2018.csc2017.GameCentre.Data.DatabaseHandler;
import fall2018.csc2017.GameCentre.Data.User;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.SlidingTiles.BoardManager;

public class SudokuStartingActivity extends AppCompatActivity {

    private static final int MAX_UNDO_LIMIT = 20;
    private User user;
    private String username;
    private String userFile;
    private DatabaseHandler db;
    /**
     * The main save file.
     */
    private String gameStateFile;
    /**
     * A temporary save file.
     */
    private String tempGameStateFile;
    /**
     * The board manager.
     */
    public static final String GAME_NAME = "SlidingTiles";
    private BoardManager boardManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_starting);
    }
}
