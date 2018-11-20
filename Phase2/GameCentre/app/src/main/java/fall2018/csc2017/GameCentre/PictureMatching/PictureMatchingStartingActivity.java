package fall2018.csc2017.GameCentre.PictureMatching;

import android.content.Intent;
import android.content.res.Resources;
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

import fall2018.csc2017.GameCentre.Data.DatabaseHandler;
import fall2018.csc2017.GameCentre.Data.User;
import fall2018.csc2017.GameCentre.R;

public class PictureMatchingStartingActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;
    public static Resources RESOURCES;
    private User user;
    private String username;
    private String userFile;
    private DatabaseHandler db;
    /**
     * The main save file.
     */
    private String gameStateFile;
    /**
     * A temporary save file.
     */
    private String tempGameStateFile;
    /**
     * The board manager.
     */
    public static final String GAME_NAME = "PictureMatch";
    private MatchingBoardManager boardManager;

    private String[] list_diff = new String[]{"Easy(4x4)", "Normal(6x6)", "Hard(8x8)"};
    private int selected_difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        RESOURCES = getResources();
        setContentView(R.layout.activity_picturematching_starting);
        db = new DatabaseHandler(this);
        username = getIntent().getStringExtra("user");


        selected_difficulty = 2;

        setupUser();
        setupFile();

        boardManager = new MatchingBoardManager();
        saveToFile(tempGameStateFile);

        addStartButtonListener();
        addLoadButtonListener();
        addDiffSpinnerListener();
    }

    private void addDiffSpinnerListener() {
        Spinner select_diff = findViewById(R.id.list_diff_picturematching);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_diff);
        select_diff.setAdapter(arrayAdapter);

        select_diff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) == list_diff[0]) {
                    selected_difficulty = 2;
                } else if (parent.getItemAtPosition(position) == list_diff[1]) {
                    selected_difficulty = 3;
                } else if (parent.getItemAtPosition(position) == list_diff[2]) {
                    selected_difficulty = 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_difficulty = 4;
            }
        });
    }

    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.PictureMatchingLoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(gameStateFile);
                saveToFile(tempGameStateFile);
                setDifficulty(boardManager.getDifficulty());
                makeToast("Loaded Game");
                switchToGame();
            }
        });
    }
    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, PictureMatchingGameActivity.class);
        saveToFile(tempGameStateFile);
        tmp.putExtra("user", username);
        startActivity(tmp);
    }

    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.PictureMatchingNewGameButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDifficulty(selected_difficulty);
                boardManager = new MatchingBoardManager();
                switchToGame();
            }
        });
    }

    private void setupFile() {
        username = getIntent().getStringExtra("user");
        userFile = db.getUserFile(username);
        loadFromFile(userFile);
    }

    private void setupUser() {
        if (!db.dataExists(username, GAME_NAME)) {
            db.addData(username, GAME_NAME);
        }
        gameStateFile = db.getDataFile(username, GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;
    }

    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (fileName.equals(userFile)) {
                    user = (User) input.readObject();
                } else if (fileName.equals(gameStateFile) || fileName.equals(tempGameStateFile)) {
                    boardManager = (MatchingBoardManager) input.readObject();
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

    /**
     * Set the size of board to match with difficulty.
     *
     * @param diff the difficulty to set as
     */
    private void setDifficulty(int diff) {
        MatchingBoard.NUM_ROWS = diff*2;
        MatchingBoard.NUM_COLS = diff*2;
    }
}
