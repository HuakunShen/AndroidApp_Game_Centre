package fall2018.csc2017.GameCentre.SlidingTiles;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import fall2018.csc2017.GameCentre.Data.DatabaseHandler;
import fall2018.csc2017.GameCentre.Data.User;
import fall2018.csc2017.GameCentre.R;

/**
 * The game activity.
 */
public class GameActivity extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * Display steps
     */
    private TextView displayStep;


    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    private static final String GAME_NAME = "SlidingTiles";

    private TextView stepDisplay;
    private User user;
    private String username;
    private String userFile;
    private DatabaseHandler db;
    //time
    private LocalTime startingTime;
    private Long preStartTime;
    private Long totalTimeTaken;
    private int steps;

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
        setContentView(R.layout.activity_main);
        setupTime();
        setUpStep();
        // Add View to activity
        addGridViewToActivity();
        addUndoButtonListener();
        addWarningTextViewListener();
        addStepDisplayListener();
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
     * setup initial step base on the record in boardmanager
     */
    private void setUpStep() {
        stepDisplay = findViewById(R.id.stepDisplayTextView);
        this.steps = boardManager.getStepsTaken();
//        this.steps = 0;
        stepDisplay.setText("Steps: " + Integer.toString(steps));
    }


    /**
     * Time counting, setup initial time based on the record in boardmanager
     */
    private void setupTime() {
        Timer timer = new Timer();
        preStartTime = boardManager.getTimeTaken();
        final TextView timeDisplay = findViewById(R.id.time_display_view);
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                long time = Duration.between(startingTime, LocalTime.now()).toMillis();
                timeDisplay.setText(timeToString(time + preStartTime));
//                timeDisplay.setText(timeToString(time));
                totalTimeTaken = time + preStartTime;
                boardManager.setTimeTaken(time + preStartTime);
//                saveToFile(gameStateFile);
            }
        };
        timer.schedule(task2, 0, 1000);
    }

    /**
     * Set up the warning message displayed on the UI.
     */
    private void addWarningTextViewListener() {
        warning = findViewById(R.id.warningTextView);
        warning.setVisibility(View.INVISIBLE);
    }

    /**
     * Set up the step display textView
     */
    private void addStepDisplayListener() {
        displayStep = findViewById(R.id.stepDisplayTextView);
        displayStep.setText("Step: 0");
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
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(Board.NUM_COLS);
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

                        columnWidth = displayWidth / Board.NUM_COLS;
                        columnHeight = displayHeight / Board.NUM_ROWS;

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
        Board board = boardManager.getBoard();
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
        Board board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / Board.NUM_ROWS;
            int col = nextPos % Board.NUM_COLS;
            int tile_id = board.getTile(row, col).getId();
            if (tile_id == Board.NUM_ROWS * Board.NUM_COLS
                    || StartingActivity.tileImages3x3[0] == null) {
                b.setBackgroundResource(board.getTile(row, col).getBackground());
            } else if (board.difficulty == 3) {
                b.setBackground(new BitmapDrawable(getResources(),
                        StartingActivity.tileImages3x3[tile_id]));
            } else if (board.difficulty == 4) {
                b.setBackground(new BitmapDrawable(getResources(),
                        StartingActivity.tileImages4x4[tile_id]));
            } else if (board.difficulty == 5) {
                b.setBackground(new BitmapDrawable(getResources(),
                        StartingActivity.tileImages5x5[tile_id]));
            }
            nextPos++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stepDisplay.setText("Steps: " + Integer.toString(this.steps));
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

    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boardManager.undoAvailable()) {
                    boardManager.touchMove(boardManager.popUndo());
                } else {
                    warning.setText("Exceeds Undo-Limit!");
                    warning.setVisibility(View.VISIBLE);
                    warning.setError("Exceeds Undo-Limit! ");
                    displayStep.setVisibility(View.INVISIBLE);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            warning.setVisibility(View.INVISIBLE);
                            displayStep.setVisibility(View.VISIBLE);
                        }
                    }, 1000);
                }
            }
        });
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
                    boardManager = (BoardManager) input.readObject();
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
        this.steps++;
        this.stepDisplay.setText("Steps: " + Integer.toString(steps));
        boardManager.setStepsTaken(steps);
        if (boardManager.puzzleSolved()) {
            Integer score = calculateScore();
            user.updateScore(GAME_NAME, score);
            saveToFile(userFile);
            db.updateScore(user, GAME_NAME);
        }
    }

    private Integer calculateScore() {
        int step = steps;
        int timeInSec = totalTimeTaken.intValue() / 1000;
        Integer score = new Integer(10000 / (step + timeInSec));
        return score;
    }
}
