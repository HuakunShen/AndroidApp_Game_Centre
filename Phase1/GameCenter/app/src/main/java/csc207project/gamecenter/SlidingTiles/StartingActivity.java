package csc207project.gamecenter.SlidingTiles;

import android.app.IntentService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import csc207project.gamecenter.AutoSave.AutoSave;
import csc207project.gamecenter.R;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivity extends AppCompatActivity {

    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "save_file.ser";
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";
    /**
     * The board manager.
     */
    private BoardManager boardManager;

    public static String currentUser;

    private EditText undoLimit;

    private final int MAX_UNDO_LIMIT = 20;

    /**
     * The difficulties can be selected.
     */
    Spinner select_diff;
    private String[] list_diff = new String[]{"Easy(3x3)", "Normal(4x4)", "Hard(5x5)"};
    //this will be used later
    private int selected_diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardManager = new BoardManager();
        saveToFile(TEMP_SAVE_FILENAME);

        currentUser = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();

        select_diff = findViewById(R.id.list_diff_sele);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_diff);
        select_diff.setAdapter(arrayAdapter);

        select_diff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) == list_diff[0]) {
                    selected_diff = 3;
                } else if (parent.getItemAtPosition(position) == list_diff[1]) {
                    selected_diff = 4;
                } else if (parent.getItemAtPosition(position) == list_diff[2]) {
                    selected_diff = 5;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_diff = 4;
            }
        });
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        undoLimit = findViewById(R.id.undoLimitInput);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //original code
                try {
                    boardManager = loadFromFile(SAVE_FILENAME).equals(-1) ?
                            new BoardManager() : (BoardManager) loadFromFile(SAVE_FILENAME);
                    if (!boardManager.userExist(currentUser)) {
                        boardManager.addUser(currentUser);
                    }
                    boardManager.addState(currentUser, (new BoardManager()).getBoard());
                    boardManager.setBoard(boardManager.getBoard(currentUser));
                } catch (Exception e) {
                    boardManager = new BoardManager();
                }

                setUndoSteps();
                saveToFile(TEMP_SAVE_FILENAME);
                switchToGame();
            }
        });
    }


    public void setUndoSteps() {
        String inputStr = undoLimit.getText().toString();

        int input;
        if (inputStr.matches("")) {
            input = 3;
        } else {
            input = Integer.parseInt(inputStr);
        }

        if (input > MAX_UNDO_LIMIT) {
            Toast.makeText(this, "Exceeds Undo Limit: " +
                    MAX_UNDO_LIMIT, Toast.LENGTH_SHORT).show();
        } else {
            boardManager.setCapacity(currentUser, input);
        }
    }


    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = loadFromFile(SAVE_FILENAME).equals(-1) ?
                        new BoardManager() : (BoardManager) loadFromFile(SAVE_FILENAME);
                if (boardManager.userExist(currentUser)) {
                    boardManager.setBoard(boardManager.getBoard(currentUser));
                    saveToFile(TEMP_SAVE_FILENAME);
                    makeToastLoadedText();
                    switchToGame();
                } else {
                    try {
                        boardManager = loadFromFile(SAVE_FILENAME).equals(-1) ?
                                new BoardManager() : (BoardManager) loadFromFile(SAVE_FILENAME);
                        if (!boardManager.userExist(currentUser)) {
                            boardManager.addUser(currentUser);
                        }
                        boardManager.addState(currentUser, (new BoardManager()).getBoard());
                        boardManager.setBoard(boardManager.getBoard(currentUser));
                    } catch (Exception e) {
                        boardManager = new BoardManager();
                    }
                    saveToFile(TEMP_SAVE_FILENAME);
                    switchToGame();
                }

            }
        });
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastSavedText();
            }
        });
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        boardManager = loadFromFile(TEMP_SAVE_FILENAME).equals(-1) ?
                new BoardManager() : (BoardManager) loadFromFile(TEMP_SAVE_FILENAME);
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, GameActivity.class);
        tmp.putExtra("username", currentUser);

        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        startActivity(tmp);
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
}
