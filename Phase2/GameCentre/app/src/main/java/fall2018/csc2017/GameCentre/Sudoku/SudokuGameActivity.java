package fall2018.csc2017.GameCentre.Sudoku;

import android.content.Context;
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
import fall2018.csc2017.GameCentre.SlidingTiles.CustomAdapter;

public class SudokuGameActivity extends AppCompatActivity implements Observer, View.OnClickListener {

    /**
     * The board manager.
     */
    private SudokuBoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> cellButtons;

    // Grid View and calculated column height and width based on device size
    private SudokuGestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    private static final String GAME_NAME = "Sudoku";

    private User user;
    private String username;
    private String userFile;
    private DatabaseHandler db;
    //time
    private LocalTime startingTime;
    private Long preStartTime = 0L;
    private Long totalTimeTaken;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;

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
        setContentView(R.layout.activity_sudoku_game);
        startingTime = LocalTime.now();
        db = new DatabaseHandler(this);
        setupUser();
        setupFile();
        loadFromFile(tempGameStateFile);
        createCellButtons(this);
        addGridViewToActivity();
        setupTime();
        setUpButtons();

    }

    private void setUpButtons() {
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_1:
                boardManager.getBoard().updateValue(1);
                break;
            case R.id.button_2:
                boardManager.getBoard().updateValue(2);
                break;
            case R.id.button_3:
                boardManager.getBoard().updateValue(3);
                break;
            case R.id.button_4:
                boardManager.getBoard().updateValue(4);
                break;
            case R.id.button_5:
                boardManager.getBoard().updateValue(5);
                break;
            case R.id.button_6:
                boardManager.getBoard().updateValue(6);
                break;
            case R.id.button_7:
                boardManager.getBoard().updateValue(7);
                break;
            case R.id.button_8:
                boardManager.getBoard().updateValue(8);
                break;
            case R.id.button_9:
                boardManager.getBoard().updateValue(9);
                break;
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
     * Time counting, setup initial time based on the record in boardmanager
     */
    private void setupTime() {
        Timer timer = new Timer();
        preStartTime = boardManager.getTimeTaken();
        final TextView timeDisplay = findViewById(R.id.sudoku_time_text);
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
        gridView = findViewById(R.id.SudokuGrid);
        gridView.setNumColumns(SudokuBoard.NUM_COLS_SUDOKU);
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

                        columnWidth = displayWidth / SudokuBoard.NUM_COLS_SUDOKU;
                        columnHeight = displayHeight / SudokuBoard.NUM_ROWS_SUDOKU;

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
        updateCellButtons();
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
    }


    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createCellButtons(Context context) {
        SudokuBoard board = boardManager.getBoard();
        cellButtons = new ArrayList<>();
        for (int row = 0; row != SudokuBoard.NUM_ROWS_SUDOKU; row++) {
            for (int col = 0; col != SudokuBoard.NUM_COLS_SUDOKU; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getCell(row, col).getBackground());
                this.cellButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateCellButtons() {
        SudokuBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : cellButtons) {
            int row = nextPos / SudokuBoard.NUM_ROWS_SUDOKU;
            int col = nextPos % SudokuBoard.NUM_COLS_SUDOKU;
            b.setBackgroundResource(board.getCell(row, col).getBackground());
            nextPos++;
        }
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
                    boardManager = (SudokuBoardManager) input.readObject();
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
        if (boardManager.sudokuSolved()) {
            Integer score = calculateScore();
            user.updateScore(GAME_NAME, score);
            saveToFile(userFile);
            db.updateScore(user, GAME_NAME);
        }
    }

    private Integer calculateScore() {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        Integer score = new Integer(10000 / timeInSec);
        return score;
    }


}
