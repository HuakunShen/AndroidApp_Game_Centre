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

public class SudokuGameActivity extends AppCompatActivity implements Observer {

    private SudokuGameController controller;
    private TextView timeDisplay;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    private static final String GAME_NAME = "Sudoku";
    //time
    private LocalTime startingTime;
    private Long preStartTime = 0L;
    private Long totalTimeTaken;
    /**
     * Warning message
     */
    private TextView warning;

    private TextView hintText;
    private Button[] numButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingTime = LocalTime.now();
        setupController();
        controller.loadFromFile();
        controller.createCellButton();
        setContentView(R.layout.activity_sudoku_game);
        setupTime();

        addGridViewToActivity();
        setUpHintDisplay();
        setUpButtons();
        addWarningTextViewListener();
        addClearButtonListener();
        addUndoButtonListener();
        addEraseButtonListener();
        addHintButtonListener();
    }

    private void setupController() {
        controller = new SudokuGameController(this, (User)getIntent().getSerializableExtra("user"));
        controller.setupFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = "Time: " + controller.convertTime(controller.getBoardManager().getTimeTaken());
        timeDisplay.setText(text);
    }

    /**
     * Set up hint display.
     */
    private void setUpHintDisplay() {
        hintText = findViewById(R.id.hintTextView);
        String hintDisplay = "Hint: " + String.valueOf(controller.getBoardManager().getHint());
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
                            controller.getBoardManager().updateValue(tmp + 1, false);
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
                while (controller.getBoardManager().undoAvailable()) {
                    controller.getBoardManager().undo();
                }
                Cell currentCell = controller.getBoardManager().getCurrentCell();
                if (currentCell != null) {
                    currentCell.setHighlighted(false);
                    currentCell.setFaceValue(currentCell.getFaceValue());
                    controller.getBoardManager().setCurrentCell(null);
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
                if (controller.getBoardManager().undoAvailable())
                    controller.getBoardManager().undo();
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
                if (controller.getBoardManager().getCurrentCell() != null &&
                        controller.getBoardManager().getCurrentCell().getFaceValue() != 0)
                    controller.getBoardManager().updateValue(0, false);
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
                Cell currentCell = controller.getBoardManager().getCurrentCell();
                if (controller.getBoardManager().getHint() > 0) {
                    if (currentCell != null &&
                            !currentCell.getFaceValue().equals(currentCell.getSolutionValue())) {
                        controller.getBoardManager().updateValue(currentCell.getSolutionValue(), false);
                        controller.getBoardManager().reduceHint();
                        String hintDisplay = "Hint: " + String.valueOf(controller.getBoardManager().getHint());
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
     * Time counting, setup initial time based on the record in boardmanager
     */
    private void setupTime() {
        if (!controller.boardSolved())
            controller.setGameRunning(true);
        Timer timer = new Timer();
        preStartTime = controller.getBoardManager().getTimeTaken();
        timeDisplay = findViewById(R.id.sudoku_time_text);
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                long time = Duration.between(startingTime, LocalTime.now()).toMillis();
                if(controller.isGameRunning()){
                    timeDisplay.setText(String.format("Time: %s", controller.convertTime(time + preStartTime)));
                    totalTimeTaken = time + preStartTime;
                    controller.getBoardManager().setTimeTaken(time + preStartTime);
                }
            }
        };
        timer.schedule(task2, 0, 1000);
    }

    /**
     * Setup the gridview where the tiles are located
     */
    private void addGridViewToActivity() {
        gridView = findViewById(R.id.SudokuGrid);
        gridView.setNumColumns(9);
        gridView.setBoardManager(controller.getBoardManager());
        controller.getBoardManager().addObserver(this);
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
        SudokuBoard board = controller.getBoard();
        int nextPos = 0;
        for (Button b : controller.getCellButtons()) {
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
        gridView.setAdapter(new CustomAdapter(controller.getCellButtons(), columnWidth, columnHeight));
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        controller.updateCellButtons();
        gridView.setAdapter(new CustomAdapter(controller.getCellButtons(), columnWidth, columnHeight));
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (controller.getBoardManager().getCurrentCell() != null) {
            controller.getBoardManager().getCurrentCell().setHighlighted(false);
            controller.getBoardManager().getCurrentCell().setFaceValue(controller.getBoardManager().getCurrentCell().getFaceValue());
        }
        controller.getBoardManager().setCurrentCell(null);
        controller.saveToFile(controller.getTempGameStateFile());
        controller.saveToFile(controller.getGameStateFile());
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
        if (controller.getBoardManager().boardSolved()) {
            Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            Integer score = controller.calculateScore(totalTimeTaken);
            boolean newRecord = controller.updateScore(score);
            controller.saveToFile(controller.getUserFile());
            controller.setGameRunning(false);
            popScoreWindow(score, newRecord);
        }
    }


    private void popScoreWindow(Integer score, boolean newRecord) {
        Intent goToPopWindow = new Intent(getApplication(), popScore.class);
        goToPopWindow.putExtra("score", score);
        goToPopWindow.putExtra("user", controller.getUser());
        goToPopWindow.putExtra("gameType", GAME_NAME);
        goToPopWindow.putExtra("newRecord", newRecord);

        startActivity(goToPopWindow);
    }



}
