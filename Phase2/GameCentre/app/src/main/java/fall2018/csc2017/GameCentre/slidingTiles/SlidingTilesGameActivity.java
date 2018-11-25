package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import fall2018.csc2017.GameCentre.pictureMatching.PictureMatchingStartingActivity;
import fall2018.csc2017.GameCentre.util.LoadSaveSerializable;

import static android.graphics.Bitmap.createBitmap;

/**
 * The game activity.
 */
public class SlidingTilesGameActivity extends AppCompatActivity implements Observer,
        LoadSaveSerializable {

    /**
     * The board manager.
     */
    private SlidingTilesBoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * Display steps
     */
    private TextView displayStep;


    // Grid View and calculated column height and width based on device size
    private SlidingTilesGestureDetectGridView gridView;
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

    private int difficulty;
    private Bitmap backgroundImage;
    private Bitmap[] tileImages;

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
        setContentView(R.layout.activity_main);
        setupTime();
        setUpStep();
        // Add View to activity
        addGridViewToActivity();
        addUndoButtonListener();
        addWarningTextViewListener();
        addStepDisplayListener();

        difficulty = boardManager.getBoard().getDifficulty();
        tileImages = new Bitmap[difficulty * difficulty];

        try {
            byte[] tmpImage = boardManager.getImageBackground();
            backgroundImage = BitmapFactory.decodeByteArray(tmpImage, 0, tmpImage.length);
            cutImageToTiles();
        }catch (Exception e) {
            convertNumberToTiles();
        }
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
                totalTimeTaken = time + preStartTime;
                boardManager.setTimeTaken(time + preStartTime);
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
        gridView.setNumColumns(boardManager.getBoard().getDifficulty());
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

                        columnWidth = (displayWidth / boardManager.getBoard().getDifficulty());
                        columnHeight = (displayHeight / boardManager.getBoard().getDifficulty());

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
        for (int row = 0; row != boardManager.getBoard().getDifficulty(); row++) {
            for (int col = 0; col != boardManager.getBoard().getDifficulty(); col++) {
                Button tmp = new Button(context);
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        SlidingTilesBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / boardManager.getBoard().getDifficulty();
            int col = nextPos % boardManager.getBoard().getDifficulty();
            int tile_id = board.getTile(row, col);
            b.setBackground(new BitmapDrawable(getResources(), tileImages[tile_id - 1]));
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
                    boardManager.move(boardManager.popUndo());
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
    public void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (fileName.equals(userFile)) {
                    user = (User) input.readObject();
                } else if (fileName.equals(gameStateFile) ||
                        fileName.equals(tempGameStateFile)) {
                    boardManager = (SlidingTilesBoardManager) input.readObject();
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
        if (boardManager.boardSolved()) {
            Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
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

    private void cutImageToTiles() {
        int width = backgroundImage.getWidth();
        int height = backgroundImage.getHeight();

        int count = 0;
        for (int i = 0; i < difficulty; i++) {
            for (int j = 0; j < difficulty; j++) {
                tileImages[count] = createBitmap(backgroundImage, i * (width / difficulty),
                        j * (height / difficulty), width / difficulty, height / difficulty, null, false);
                count++;
            }
        }
        tileImages[difficulty * difficulty - 1]
                = BitmapFactory.decodeResource(RESOURCES, R.drawable.tile_empty);
    }

    private void convertNumberToTiles() {
        for (int i = 0; i < difficulty * difficulty; i++) {
            String name = "tile_"  + Integer.toString(i + 1);
            int numImage = RESOURCES.getIdentifier(name, "drawable", PACKAGE_NAME);
            tileImages[i] = BitmapFactory.decodeResource(RESOURCES, numImage);
        }
        tileImages[difficulty * difficulty - 1]
                = BitmapFactory.decodeResource(RESOURCES, R.drawable.tile_empty);
    }
}
