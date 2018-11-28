package fall2018.csc2017.GameCentre.sudoku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.util.CustomAdapter;
import fall2018.csc2017.GameCentre.util.GestureDetectGridView;
import fall2018.csc2017.GameCentre.util.LoadSaveSerializable;
import fall2018.csc2017.GameCentre.util.popScore;

public class SudokuGameActivity extends AppCompatActivity implements Observer, LoadSaveSerializable {

    /**
     * The board manager.
     */
    private SudokuBoardManager boardManager;
    /**
     * The buttons to display.
     */
    private ArrayList<Button> cellButtons;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    private static final String GAME_NAME = "Sudoku";

    private User user;
    //    private String username;
    private String userFile;
    private SQLDatabase db;
    //time
    private LocalTime startingTime;
    private Long preStartTime = 0L;
    private Long totalTimeTaken;
    /**
     * Warning message
     */
    private TextView warning;

    private TextView hintText;

    /**
     * The main save file.
     */
    private String gameStateFile;

    /**
     * A temporary save file.
     */
    private String tempGameStateFile;

    Button numButtons[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingTime = LocalTime.now();
        setContentView(R.layout.activity_sudoku_game);
        startingTime = LocalTime.now();
        db = new SQLDatabase(this);
        setupUser();
        setupFile();
        loadFromFile(tempGameStateFile);
        createCellButtons(this);
        addGridViewToActivity();
        setUpHintDisplay();
        setupTime();
        setUpButtons();
        addWarningTextViewListener();
        addClearButtonListener();
        addUndoButtonListener();
        addEraseButtonListener();
        addHintButtonListener();
    }

    /**
     * Set up hint display.
     */
    private void setUpHintDisplay() {
        hintText = findViewById(R.id.hintTextView);
        String hintDisplay = "Hint: " + String.valueOf(boardManager.getHint());
        hintText.setText(hintDisplay);
    }

    /**
     * Set up all buttons.
     */
    private void setUpButtons() {
        LinearLayout numLayout = findViewById(R.id.numButtons);

        numButtons = new Button[9];
        for (int tmp = 0; tmp < numButtons.length; tmp++) {
            numButtons[tmp] = new Button(this);
            numButtons[tmp].setId(1800 + tmp);
            numButtons[tmp].setText(String.format("%s", Integer.toString(tmp + 1)));

            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams(100, 50);
            btParams.leftMargin = 3;
            btParams.topMargin = 5;
            btParams.width = 115;
            btParams.height = 115;
            numLayout.addView(numButtons[tmp], btParams);

            numButtons[tmp].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int tmp = 0; tmp < numButtons.length; tmp++) {
                        if (v == numButtons[tmp])
                            boardManager.updateValue(tmp + 1, false);
                    }
                }
            });
        }
    }

    /**
     * Activate Clear button
     */
    private void addClearButtonListener() {
        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (boardManager.undoAvailable()) {
                    boardManager.undo();
                }
                Cell currentCell = boardManager.getCurrentCell();
                if (currentCell != null) {
                    currentCell.setHighlighted(false);
                    currentCell.setFaceValue(currentCell.getFaceValue());
                    boardManager.setCurrentCell(null);
                }
                display();
            }
        });
    }

    /**
     * Activate Undo button
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.sudoku_undo_button);
        warning.setError("Exceeds Undo-Limit! ");
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boardManager.undoAvailable())
                    boardManager.undo();
                else
                    displayWarning("Exceeds Undo-Limit!");

            }
        });
    }


    /**
     * Set up the erase button listener.
     */
    private void addEraseButtonListener() {
        Button eraseButton = findViewById(R.id.eraseButton);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boardManager.getCurrentCell() != null &&
                        boardManager.getCurrentCell().getFaceValue() != 0)
                    boardManager.updateValue(0, false);
                display();
            }
        });
    }

    /**
     * When Hint button is taped, the solution will display on the selected cell.
     */
    private void addHintButtonListener() {
        final Button hintButton = findViewById(R.id.hintButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cell currentCell = boardManager.getCurrentCell();
                if (boardManager.getHint() > 0) {
                    if (currentCell != null &&
                            !currentCell.getFaceValue().equals(currentCell.getSolutionValue())) {
                        boardManager.updateValue(currentCell.getSolutionValue(), false);
                        boardManager.reduceHint();
                        String hintDisplay = "Hint: " + String.valueOf(boardManager.getHint());
                        hintText.setText(hintDisplay);
                    }
                } else {
                    displayWarning("No More Hint!");
                }
                display();
            }
        });
    }

    /**
     * Display the warning.
     *
     * @param msg the input message
     */
    private void displayWarning(String msg) {
        warning.setVisibility(View.VISIBLE);
        warning.setText(msg);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                warning.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    /**
     * Set up the warning message displayed on the UI.
     */
    private void addWarningTextViewListener() {
        warning = findViewById(R.id.sudokuWarningTextView);
        warning.setVisibility(View.INVISIBLE);
    }

    /**
     * setup user object according to username and define the value of userFile (where user
     * object is saved)
     */
    private void setupUser() {
        user = (User) getIntent().getSerializableExtra("user");
//        username = getIntent().getStringExtra("user");
        userFile = db.getUserFile(user.getUsername());
        loadFromFile(userFile);
    }

    /**
     * setup file of the game
     * get the filename of where the game state should be saved
     */
    private void setupFile() {
        if (!db.dataExists(user.getUsername(), GAME_NAME)) {
            db.addData(user.getUsername(), GAME_NAME);
        }
        gameStateFile = db.getDataFile(user.getUsername(), GAME_NAME);
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
                timeDisplay.setText(String.format("Time: %s", timeToString(time + preStartTime)));
                totalTimeTaken = time + preStartTime;
                boardManager.setTimeTaken(time + preStartTime);
            }
        };
        timer.schedule(task2, 0, 1000);
    }

    /**
     * Convert time in milli seconds (long type) to String which will be displayed
     *
     * @param time input time to be converted
     * @return the string format of the input time
     */
    String timeToString(long time) {
        Integer hour = (int) (time / 3600000);
        Integer min = (int) ((time % 3600000) / 60000);
        Integer sec = (int) ((time % 3600000 % 60000) / 1000);
        String hourStr = hour.toString();
        String minStr = min.toString();
        String secStr = sec.toString();
        if (hour < 10)
            hourStr = "0" + hourStr;
        if (min < 10)
            minStr = "0" + minStr;
        if (sec < 10)
            secStr = "0" + secStr;
        return hourStr + ":" + minStr + ":" + secStr;
    }

    /**
     * Setup the gridview where the tiles are located
     */
    private void addGridViewToActivity() {
        gridView = findViewById(R.id.SudokuGrid);
        gridView.setNumColumns(9);
        gridView.setBoardManager(boardManager);
        boardManager.addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        columnWidth = gridView.getMeasuredWidth() / 9;
                        columnHeight = gridView.getMeasuredHeight() / 9;
                        initializeCellButtons();
                    }
                });
    }

    /**
     * Initialize the backgrounds on the buttons to match the tiles.
     */
    private void initializeCellButtons() {
        SudokuBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : cellButtons) {
            Cell cell = board.getCell(nextPos / 9, nextPos % 9);
            b.setTextSize(20);
            if (cell.isEditable()) {
                b.setTextColor(Color.RED);
            } else {
                b.setTextColor(Color.BLACK);
            }
            if (cell.getFaceValue() == 0) {
                b.setText("");
            } else {
                b.setText(String.format("%s", cell.getFaceValue().toString()));
            }
            b.setBackgroundResource(cell.getBackground());
            nextPos++;
        }
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
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
        for (int row = 0; row != 9; row++) {
            for (int col = 0; col != 9; col++) {
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
            Cell cell = board.getCell(nextPos / 9, nextPos % 9);
            if (cell.getFaceValue() == 0) {
                b.setText("");
            } else {
                b.setText(String.format("%s", cell.getFaceValue().toString()));
            }
            b.setBackgroundResource(cell.getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // De-highlight current Cell, make sure no cell the selected when game is loaded
        // optional, not necessary, need discussion.
        if (boardManager.getCurrentCell() != null) {
            boardManager.getCurrentCell().setHighlighted(false);
            boardManager.getCurrentCell().setFaceValue(boardManager.getCurrentCell().getFaceValue());
        }
        boardManager.setCurrentCell(null);
        saveToFile(tempGameStateFile);
        saveToFile(gameStateFile);
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
        if (boardManager.boardSolved()) {
            Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            Integer score = calculateScore();
            boolean newRecord = user.updateScore(GAME_NAME, score);
            SudokuBoardManager.setLevelOfDifficulty(2);
            boardManager = new SudokuBoardManager();
            saveToFile(userFile);
            db.updateScore(user, GAME_NAME);
            popScoreWindow(score, newRecord);
        }
    }


    private void popScoreWindow(Integer score, boolean newRecord) {
        Intent goToPopWindow = new Intent(getApplication(), popScore.class);
        goToPopWindow.putExtra("score", score);
        goToPopWindow.putExtra("user", user);
        goToPopWindow.putExtra("gameType", GAME_NAME);
        goToPopWindow.putExtra("newRecord", newRecord);

        startActivity(goToPopWindow);
    }

    /**
     * Calculate the score for current player.
     *
     * @return the play's score.
     */
    private Integer calculateScore() {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        return (new Integer(10000 / timeInSec));
    }


}
