package fall2018.csc2017.GameCentre.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
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
import fall2018.csc2017.GameCentre.util.LoadSaveSerializable;

public class SudokuGameActivity extends AppCompatActivity implements Observer, LoadSaveSerializable {
//public class SudokuGameActivity extends AppCompatActivity implements Observer, View.OnClickListener
//        , LoadSaveSerializable {

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
    private String username;
    private String userFile;
    private DatabaseHandler db;
    //time
    private LocalTime startingTime;
    private Long preStartTime = 0L;
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
        addWarningTextViewListener();
        addClearButtonListener();
        addUndoButtonListener();
        addEraseButtonListener();
        addHintButtonListener();
    }


    private void setUpButtons() {
        LinearLayout numLayout = findViewById(R.id.numButtons);

        Button numButtons[] = new Button[9];
        for (int tmp = 0; tmp < numButtons.length; tmp++) {
            numButtons[tmp] = new Button(this);
            numButtons[tmp].setId(1800+tmp);
            numButtons[tmp].setText(String.format("%s", Integer.toString(tmp + 1)));

            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams (100,50);
            btParams.leftMargin = 3;
            btParams.topMargin = 5;
            btParams.width = 115;
            btParams.height = 115;
            numLayout.addView(numButtons[tmp],btParams);

            numButtons[tmp].setOnClickListener(new View.OnClickListener() {
                final int value = 1;
                @Override
                public void onClick(View v) {
                    boardManager.updateValue(value + 1, false);
                }
            });
        }

//        numButtons[0] = findViewById(R.id.button_1);
//        numButtons[1] = findViewById(R.id.button_2);
//        numButtons[2] = findViewById(R.id.button_3);
//        numButtons[3] = findViewById(R.id.button_4);
//        numButtons[4] = findViewById(R.id.button_5);
//        numButtons[5] = findViewById(R.id.button_6);
//        numButtons[6] = findViewById(R.id.button_7);
//        numButtons[7] = findViewById(R.id.button_8);
//        numButtons[8] = findViewById(R.id.button_9);
//        numButtons[0].setOnClickListener(this);
//        numButtons[1].setOnClickListener(this);
//        numButtons[2].setOnClickListener(this);
//        numButtons[3].setOnClickListener(this);
//        numButtons[4].setOnClickListener(this);
//        numButtons[5].setOnClickListener(this);
//        numButtons[6].setOnClickListener(this);
//        numButtons[7].setOnClickListener(this);
//        numButtons[8].setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button_1:
//                boardManager.updateValue(1, false);
//                break;
//            case R.id.button_2:
//                boardManager.updateValue(2, false);
//                break;
//            case R.id.button_3:
//                boardManager.updateValue(3, false);
//                break;
//            case R.id.button_4:
//                boardManager.updateValue(4, false);
//                break;
//            case R.id.button_5:
//                boardManager.updateValue(5, false);
//                break;
//            case R.id.button_6:
//                boardManager.updateValue(6, false);
//                break;
//            case R.id.button_7:
//                boardManager.updateValue(7, false);
//                break;
//            case R.id.button_8:
//                boardManager.updateValue(8, false);
//                break;
//            case R.id.button_9:
//                boardManager.updateValue(9, false);
//                break;
//        }
//    }

    private void addClearButtonListener() {
        Button eraseButton = findViewById(R.id.clearButton);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (boardManager.undoAvailable()) {
                    boardManager.undo();
                }
                display();
            }
        });
    }

    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.sudoku_undo_button);
        warning.setError("Exceeds Undo-Limit! ");
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boardManager.undoAvailable()) {
                    boardManager.undo();
                } else {
                    warning.setVisibility(View.VISIBLE);
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
     * Set up the erase button listener.
     */
    private void addEraseButtonListener() {
        Button eraseButton = findViewById(R.id.eraseButton);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boardManager.updateValue(0, false);
                display();
            }
        });
    }

    /**
     * When Hint button is taped, the solution will display on the selected cell.
     */
    private void addHintButtonListener() {
        Button hintButton = findViewById(R.id.hintButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cell currentCell = boardManager.getCurrentCell();
                if (currentCell != null && !currentCell.getFaceValue().equals(currentCell.getSolutionValue()))
                    boardManager.updateValue(currentCell.getSolutionValue(), false);
                display();
            }
        });
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
                timeDisplay.setText(String.format("Time: %s", timeToString(time + preStartTime)));
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
            if (cell.isEditable()){
                b.setTextColor(Color.RED);
            } else {
                b.setTextColor(Color.BLACK);
            }
            if (cell.getFaceValue() == 0){
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
            if (cell.getFaceValue() == 0){
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
            user.updateScore(GAME_NAME, score);
            saveToFile(userFile);
            db.updateScore(user, GAME_NAME);
        }
    }


    private Integer calculateScore() {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        return (new Integer(10000 / timeInSec));
    }


}
