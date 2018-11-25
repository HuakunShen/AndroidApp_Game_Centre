package fall2018.csc2017.GameCentre.pictureMatching;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import fall2018.csc2017.GameCentre.data.DatabaseHandler;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.slidingTiles.CustomAdapter;
import fall2018.csc2017.GameCentre.util.GestureDetectGridView;

public class PictureMatchingGameActivity extends AppCompatActivity implements Observer {
    /**
     * The board manager.
     */
    private MatchingBoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;



    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    private static final String GAME_NAME = "PictureMatch";

    private User user;
    private String username;
    private String userFile;
    private DatabaseHandler db;
    //time
    private LocalTime startingTime;
    private Long preStartTime;
    private Long totalTimeTaken;

    /**
     * Warning message
     */
    private TextView warning;

    /**
     * The main save file.
     */
    private String gameStateFile;
    /**
     * A temporary save file.
     */
    private String tempGameStateFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingTime = LocalTime.now();
        db = new DatabaseHandler(this);
        setupUser();
        setupFile();
        loadFromFile(tempGameStateFile);
        createTileButtons(this);
        setContentView(R.layout.activity_picturematching_game);
        setupTime();
        // Add View to activity
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
                timeDisplay.setText("Time: " + timeToString(time + preStartTime));
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
        gridView.setNumColumns(MatchingBoard.NUM_COLS);
        gridView.setBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = (displayWidth / MatchingBoard.NUM_COLS);
                        columnHeight = (displayHeight / MatchingBoard.NUM_ROWS);

                        display();
                    }
                });
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
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
        for (int row = 0; row != MatchingBoard.NUM_ROWS; row++) {
            for (int col = 0; col != MatchingBoard.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(R.drawable.black_blue_0);
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
            int row = nextPos / MatchingBoard.NUM_COLS;
            int col = nextPos % MatchingBoard.NUM_COLS;
            PictureTile currentTile = board.getTile(row,col);
            if (currentTile.getState().equals(PictureTile.FLIP)){
                String name = "tile_"  + Integer.toString(currentTile.getId());
                int id = PictureMatchingStartingActivity.RESOURCES.getIdentifier(name,
                        "drawable", PictureMatchingStartingActivity.PACKAGE_NAME);
                b.setBackgroundResource(id);
            }else if(currentTile.getState().equals(PictureTile.COVERED)){
                int id = PictureMatchingStartingActivity.RESOURCES.getIdentifier("black_blue_0",
                        "drawable", PictureMatchingStartingActivity.PACKAGE_NAME);
                b.setBackgroundResource(id);
            }else if(currentTile.getState().equals(PictureTile.SOLVED)){
                int id = PictureMatchingStartingActivity.RESOURCES.getIdentifier("tile_empty",
                        "drawable", PictureMatchingStartingActivity.PACKAGE_NAME);
                b.setBackgroundResource(id);
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
        Integer score = 10000 / (timeInSec);
        return score;
    }
}
