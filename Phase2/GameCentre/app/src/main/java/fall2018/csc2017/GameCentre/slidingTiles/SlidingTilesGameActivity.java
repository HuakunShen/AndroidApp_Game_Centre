package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.util.CustomAdapter;
import fall2018.csc2017.GameCentre.util.LoadSaveSerializable;
import fall2018.csc2017.GameCentre.util.GestureDetectGridView;
import fall2018.csc2017.GameCentre.util.popScore;

/**
 * The game activity.
 */
public class SlidingTilesGameActivity extends AppCompatActivity implements Observer {

    private SlidingTilesGameController controller;

    private TextView timeDisplay;

    /**
     * The buttons to display.
     */
    private List<Button> tileButtons;

    /**
     * Display steps
     */
    private TextView displayStep;


    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;
    private static final String GAME_NAME = "SlidingTiles";
    private TextView stepDisplay;
    //time
    private LocalTime startingTime;
    private Long totalTimeTaken;
    /**
     * Warning message
     */
    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startingTime = LocalTime.now();

        setupController();
        controller.setResourceAndPackage();
//
//        controller.setBoardManager(boardManager);
        controller.loadFromFile();
        tileButtons = controller.createTileButtons();
        setContentView(R.layout.activity_main);
        setupTime();
        setUpStep();
        // Add View to activity
        addGridViewToActivity();
        addUndoButtonListener();
        addWarningTextViewListener();
        addStepDisplayListener();
//        tileImages = new Bitmap[difficulty * difficulty];
        controller.setupTileImagesAndBackground();

    }

    private void setupController() {
        controller = new SlidingTilesGameController(this,
                (User) getIntent().getSerializableExtra("user"));
        controller.setupFile();

    }





    /**
     * setup initial step base on the record in boardmanager
     */
    private void setUpStep() {
        stepDisplay = findViewById(R.id.stepDisplayTextView);
        controller.setupSteps();
        stepDisplay.setText(String.format("%s", "Steps: " + Integer.toString(controller.getSteps())));
    }


    /**
     * Time counting, setup initial time based on the record in boardmanager
     */
    private void setupTime() {
        if(!controller.boardSolved())
            controller.setGameRunning(true);
        Timer timer = new Timer();
        final long preStartTime = controller.getBoardManager().getTimeTaken();
        timeDisplay = findViewById(R.id.time_display_view);
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                long time = Duration.between(startingTime, LocalTime.now()).toMillis();
                if(controller.isGameRunning()){
                    totalTimeTaken = time + preStartTime;
                    timeDisplay.setText(controller.convertTime(totalTimeTaken));
                    controller.getBoardManager().setTimeTaken(totalTimeTaken);
                }
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
     * Setup the gridview where the tiles are located
     */
    private void addGridViewToActivity() {
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(controller.getBoard().getDifficulty());
        gridView.setBoardManager(controller.getBoardManager());
        controller.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = (displayWidth / controller.getBoard().getDifficulty());
                        columnHeight = (displayHeight / controller.getBoard().getDifficulty());

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
        controller.updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }


    @Override
    protected void onResume() {
        super.onResume();
        stepDisplay.setText(String.format("%s", "Steps: " + Integer.toString(controller.getSteps())));
        timeDisplay.setText(controller.convertTime(controller.getBoardManager().getTimeTaken()));
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(controller.getTempGameStateFile());
        saveToFile(controller.getGameStateFile());
    }

    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!controller.undo()){
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
//    public void loadFromFile(String fileName) {
//        try {
//            InputStream inputStream = this.openFileInput(fileName);
//            if (inputStream != null) {
//                ObjectInputStream input = new ObjectInputStream(inputStream);
//
//
//
//                if (fileName.equals(controller.getUserFile())) {
//                    user = (User) input.readObject();
//                } else if (fileName.equals(controller.getGameStateFile()) ||
//                        fileName.equals(controller.getTempGameStateFile())) {
//                    boardManager = (SlidingTilesBoardManager) input.readObject();
//                }
//                inputStream.close();
//            }
//        } catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        } catch (ClassNotFoundException e) {
//            Log.e("login activity", "File contained unexpected data type: " + e.toString());
//        }
//    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            if (fileName.equals(controller.getUserFile())) {
                outputStream.writeObject(controller.getUser());
            } else if (fileName.equals(controller.getGameStateFile()) || fileName.equals(controller.getTempGameStateFile())) {
                outputStream.writeObject(controller.getBoardManager());
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
        controller.setSteps(controller.getSteps() + 1);
        this.stepDisplay.setText("Steps: " + Integer.toString(controller.getSteps()));
        if (controller.boardSolved()) {
            Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            Integer score = controller.calculateScore(totalTimeTaken);
            controller.updateScore(score);
            saveToFile(controller.getUserFile());
            controller.setGameRunning(false);
            popScoreWindow(score);
        }
    }

    private void popScoreWindow(Integer score) {
        Intent goToPopWindow = new Intent(getApplication(), popScore.class);
        goToPopWindow.putExtra("score", score);
        goToPopWindow.putExtra("user", controller.getUser());
        goToPopWindow.putExtra("gameType", GAME_NAME);
        startActivity(goToPopWindow);
    }



}
