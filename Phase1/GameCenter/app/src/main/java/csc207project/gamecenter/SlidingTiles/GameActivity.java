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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.time.Duration;
import java.time.LocalTime;

import csc207project.gamecenter.AutoSave.AutoSave;
import csc207project.gamecenter.R;

/**
 * The game activity.
 */
public class GameActivity extends AppCompatActivity implements Observer{

    private String username;
    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    private static LocalTime startingTime;

    private TextView warning;

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

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveToFile(StartingActivity.SAVE_FILENAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFromFile(StartingActivity.TEMP_SAVE_FILENAME);
        createTileButtons(this);
        setContentView(R.layout.activity_main);
        username = StartingActivity.currentUser;
        boardManager.setCurrentUser(username);
        addWarningTextViewListener();
        startingTime = LocalTime.now();

        addUndoButtonListener();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                saveToFile(StartingActivity.SAVE_FILENAME);
            }
        };
        timer.schedule(task, 0, 5000);

        TextView timePlayed = new TextView(this);
        timePlayed = (TextView) findViewById(R.id.time_display_view);

        Timer timer2 = new Timer();
        final TextView finalTimePlayed = timePlayed;
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                long time = Duration.between(GameActivity.startingTime, LocalTime.now()).toMillis();
                finalTimePlayed.setText(timeToString(time));
            }
        };
        timer2.schedule(task2, 0, 1000);

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

    private void addWarningTextViewListener() {
        warning = findViewById(R.id.warningTextView);
        warning.setVisibility(View.INVISIBLE);
    }

    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boardManager.undoAvailable(username)) {
                    boardManager.touchMove(boardManager.popUndo(username));
                }else{
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
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
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
                boardManager = (BoardManager) input.readObject();
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
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (boardManager.userExist(username)) {
            boardManager.addState(username, new Board(boardManager.getBoard().getTiles()));
        } else {
            boardManager.addUser(username);
            boardManager.addState(username, new Board(boardManager.getBoard().getTiles()));
        }
        display();
    }


}
