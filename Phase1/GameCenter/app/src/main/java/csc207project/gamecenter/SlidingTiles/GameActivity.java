package csc207project.gamecenter.SlidingTiles;


import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.time.Duration;

import csc207project.gamecenter.Data.WQWDatabase;
import csc207project.gamecenter.GameCenter.GameCentre;
import csc207project.gamecenter.R;

/**
 * The game activity.
 */
public class GameActivity extends AppCompatActivity implements Observer{

    /**
     * The username of the current player
     */
    private String username;

    /**
     * The loaded database;
     */
    public WQWDatabase userData;

    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * The moment when the game started.
     */
    private LocalTime startingTime;

    /**
     * Warning message
     */
    private TextView warning;


    /**
     * The time which the user has played before starting this game.
     */
    private Long preStartTime;

    /**
     * Constants for swiping directions. Should be an enum, probably.
     */
    private Button undoButton;
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load the boardManager that Starting Activity loaded/created.
        boardManager = loadFromFile(StartingActivity.TEMP_SAVE_FILENAME).equals(-1) ?
                new BoardManager() : (BoardManager) loadFromFile(StartingActivity.TEMP_SAVE_FILENAME);

        //Set the username to the current user.
        username = StartingActivity.currentUser;

        //Update the user's username to boardManager.
        boardManager.setCurrentUser(username);

        //Load the database.
        userData = loadFromFile(GameCentre.USER_DATA_FILE).equals(-1) ?
                new WQWDatabase() : (WQWDatabase) loadFromFile(GameCentre.USER_DATA_FILE);

        //Update the time which the user played before starting the game.
        preStartTime = userData.getTime(username, "SlidingTiles");


        //Record the starting time of the game.
        startingTime = LocalTime.now();

        //Create buttons according to the board.
        createTileButtons(this);
        setContentView(R.layout.activity_main);

        addWarningTextViewListener();

        addUndoButtonListener();

        //Set up the Auto-saving function.
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                saveToFile(StartingActivity.SAVE_FILENAME, boardManager);
                saveToFile(GameCentre.USER_DATA_FILE, userData);
            }
        };
        timer.schedule(task, 0, 5000);

        //Set up the timer displayed on the system UI.
        TextView timePlayed;
        timePlayed = (TextView) findViewById(R.id.time_display_view);
        final TextView finalTimePlayed = timePlayed;
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                long time = Duration.between(startingTime, LocalTime.now()).toMillis()
                        + preStartTime;
                finalTimePlayed.setText(timeToString(time));
            }
        };
        timer.schedule(task2, 0, 1000);

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(Board.NUM_COLS);
        gridView.setBoardManager(boardManager);
        boardManager.getBoard(username).addObserver(this);

        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                            this);
                    int displayWidth = gridView.getMeasuredWidth();
                    int displayHeight = gridView.getMeasuredHeight();

                    columnWidth = displayWidth / Board.NUM_COLS;
                    columnHeight = displayHeight / Board.NUM_ROWS;

                    display();
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        preStartTime = userData.getTime(username, "SlidingTiles");
        startingTime = LocalTime.now();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        userData.setTime(username, "SlidingTiles",
                Duration.between(startingTime, LocalTime.now()).toMillis()
                        + preStartTime);
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME, boardManager);
        saveToFile(GameCentre.USER_DATA_FILE, userData);
    }

    @Override
    protected void onStop() {
        super.onStop();
        userData.setTime(username, "SlidingTiles",
                Duration.between(startingTime, LocalTime.now()).toMillis()
                        + preStartTime);
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME, boardManager);
        saveToFile(GameCentre.USER_DATA_FILE, userData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userData.setTime(username, "SlidingTiles",
                Duration.between(startingTime, LocalTime.now()).toMillis()
                        + preStartTime);
        saveToFile(StartingActivity.SAVE_FILENAME, boardManager);
        saveToFile(GameCentre.USER_DATA_FILE, userData);
    }

    @Override
    public void update(Observable o, Object arg) {
        userData.setStep(username, "SlidingTiles",
                userData.getStep(username, "SlidingTiles") + 1);
        if (boardManager.userExist(username)) {
            boardManager.addState(username, new Board(boardManager.getBoard().getTiles()));
        } else {
            boardManager.addUser(username);
            boardManager.addState(username, new Board(boardManager.getBoard().getTiles()));
        }
        display();
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
     * Set up the warning message displayed on the UI.
     */
    private void addWarningTextViewListener() {
        warning = findViewById(R.id.warningTextView);
        warning.setVisibility(View.INVISIBLE);
    }

    /**
     * Set up the undo button.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boardManager.undoAvailable(username)) {
                    boardManager.touchMove(boardManager.popUndo(username));
                    userData.setStep(username, "SlidingTiles",
                            userData.getStep(username, "SlidingTiles" + 1));
                }else{
                    warning.setText("Exceeds Undo-Limit!");
                    warning.setVisibility(View.VISIBLE);
                    warning.setError("Exceeds Undo-Limit! ");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            warning.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);
                }
            }
        });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = boardManager.getBoard(username);
        tileButtons = new ArrayList<>();
        for (int row = 0; row != Board.NUM_ROWS; row++) {
            for (int col = 0; col != Board.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = boardManager.getBoard(username);
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / Board.NUM_ROWS;
            int col = nextPos % Board.NUM_COLS;
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private Object loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                Object file = input.readObject();
                inputStream.close();
                return file;
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        return -1;
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName, Object file) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(file);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Formats the time in milliseconds to HH:MM:SS.
     */
    String timeToString(long time){
        Integer hour = (int) (time / 3600000);
        Integer min = (int) ((time % 3600000) / 60000);
        Integer sec = (int) ((time % 3600000 % 60000) / 1000);
        String hourStr = hour.toString();
        String minStr = min.toString();
        String secStr = sec.toString();
        if(hour < 10){
            hourStr = "0" + hourStr;
        }
        if(min < 10){
            minStr = "0" + minStr;
        }
        if(sec < 10){
            secStr = "0" + secStr;
        }
        return hourStr + ":" + minStr + ":" + secStr;
    }

}
