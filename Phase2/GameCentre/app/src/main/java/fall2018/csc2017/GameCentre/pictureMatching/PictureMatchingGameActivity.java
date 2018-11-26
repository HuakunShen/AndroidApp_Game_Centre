package fall2018.csc2017.GameCentre.pictureMatching;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import fall2018.csc2017.GameCentre.data.DatabaseHandler;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.util.CustomAdapter;
import fall2018.csc2017.GameCentre.util.GestureDetectGridView;

public class PictureMatchingGameActivity extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private MatchingBoardManager boardManager;

    /**
     * The buttons to display.
     */
    private List<Button> tileButtons;
    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;
    /**
     * The name of the current game.
     */
    private static final String GAME_NAME = "PictureMatch";
    /**
     * Current User.
     */
    private User user;
    /**
     * the name of current user.
     */
    private String username;
    /**
     * the file of current user.
     */
    private String userFile;
    /**
     * the database for saving and loading information.
     */
    private DatabaseHandler db;
    /**
     * the time you start the game
     */
    private LocalTime startingTime;
    /**
     * the time duration of the last time you saved the game.
     */
    private Long preStartTime;
    /**
     * the total time
     */
    private Long totalTimeTaken;
    /**
     * The main save file.
     */
    private String gameStateFile;
    /**
     * A temporary save file.
     */
    private String tempGameStateFile;

    private String PACKAGE_NAME;
    private Resources RESOURCES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        RESOURCES = getResources();
        startingTime = LocalTime.now();
        db = new DatabaseHandler(this);
        setupUser();
        setupFile();
        loadFromFile(tempGameStateFile);
        createTileButtons(this);
        setContentView(R.layout.activity_picturematching_game);
        setupTime();
        addGridViewToActivity();
    }

    /**
     * setup user object according to username and define the value of userFile (where user
     * object is saved)
     */
    private void setupUser() {
        username = getIntent().getStringExtra("user");
        userFile = db.getUserFile(username);
        loadFromFile(userFile);
    }

    /**
     * setup file of the game
     * get the filename of where the game state should be saved
     */
    private void setupFile() {
        if (!db.dataExists(username, GAME_NAME)) {
            db.addData(username, GAME_NAME);
        }
        gameStateFile = db.getDataFile(username, GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;
    }

    /**
     * Time counting, setup initial time based on the record in boardmanager
     */
    private void setupTime() {
        Timer timer = new Timer();
        preStartTime = boardManager.getTimeTaken();
        final TextView timeDisplay = findViewById(R.id.time_display_view_in_picturematching);
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                long time = Duration.between(startingTime, LocalTime.now()).toMillis();
                String text = "Time: " + timeToString(time + preStartTime);
                timeDisplay.setText(text);
                totalTimeTaken = time + preStartTime;
                boardManager.setTimeTaken(time + preStartTime);
            }
        };
        timer.schedule(task2, 0, 1000);
    }

    /**
     * convert time in milli seconds (long type) to String which will be displayed
     */
    String timeToString(long time) {
        Integer hour = (int) (time / 3600000);
        Integer min = (int) ((time % 3600000) / 60000);
        Integer sec = (int) ((time % 3600000 % 60000) / 1000);
        String hourStr = hour.toString();
        String minStr = min.toString();
        String secStr = sec.toString();
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        if (min < 10) {
            minStr = "0" + minStr;
        }
        if (sec < 10) {
            secStr = "0" + secStr;
        }
        return hourStr + ":" + minStr + ":" + secStr;
    }


    /**
     * Setup the gridview where the tiles are located
     */
    private void addGridViewToActivity() {
        gridView = findViewById(R.id.PictureMatchingGrid);
        gridView.setNumColumns(boardManager.getDifficulty());
        gridView.setBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();
                        columnWidth = (displayWidth / boardManager.getDifficulty());
                        columnHeight = (displayHeight / boardManager.getDifficulty());
                        display();
                    }
                });
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }


    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        tileButtons = new ArrayList<>();
        for (int row = 0; row != boardManager.getDifficulty(); row++) {
            for (int col = 0; col != boardManager.getDifficulty(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(R.drawable.picturematching_tile_back);
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        MatchingBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / boardManager.getDifficulty();
            int col = nextPos % boardManager.getDifficulty();
            PictureTile currentTile = board.getTile(row,col);
            switch (currentTile.getState()){
                case PictureTile.FLIP:
                    String name = "pm_num_"  + Integer.toString(currentTile.getId());
                    int id = RESOURCES.getIdentifier(name, "drawable", PACKAGE_NAME);
                    b.setBackgroundResource(id);
                    break;
                case PictureTile.COVERED:
                    b.setBackgroundResource(R.drawable.picturematching_tile_back);
                    break;
                case PictureTile.SOLVED:
                    b.setBackgroundResource(R.drawable.picturematching_tile_done);
                    break;
            }
            nextPos++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(tempGameStateFile);
        saveToFile(gameStateFile);
    }


    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (fileName.equals(userFile)) {
                    user = (User) input.readObject();
                } else if (fileName.equals(gameStateFile) ||
                        fileName.equals(tempGameStateFile)) {
                    boardManager = (MatchingBoardManager) input.readObject();
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            if (fileName.equals(userFile)) {
                outputStream.writeObject(user);
            } else if (fileName.equals(gameStateFile) || fileName.equals(tempGameStateFile)) {
                outputStream.writeObject(boardManager);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (boardManager.check2tiles()){
            final android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        boardManager.getBoard().solveTile();
                    } catch (Exception e) {
                        Toast.makeText(getApplication(), "slow down!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 1000);
        }
        display();
        if (boardManager.boardSolved()) {
            Toast.makeText(PictureMatchingGameActivity.this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            Integer score = calculateScore();
            user.updateScore(GAME_NAME, score);
            saveToFile(userFile);
            db.updateScore(user, GAME_NAME);
        }
    }

    private Integer calculateScore() {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        return 10000 / (timeInSec);
    }
}
