package fall2018.csc2017.GameCentre.pictureMatching;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fall2018.csc2017.GameCentre.data.DatabaseHandler;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.gameCentre.ScoreBoardActivity;
import fall2018.csc2017.GameCentre.sudoku.SudokuBoardManager;
import fall2018.csc2017.GameCentre.sudoku.SudokuStartingActivity;

public class PictureMatchingStartingActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picturematching_starting);
        db = new DatabaseHandler(this);
        username = getIntent().getStringExtra("user");

        boardManager = new MatchingBoardManager(4);

        setupUser();
        setupFile();

        saveToFile(tempGameStateFile);

        addStartButtonListener();
        addLoadButtonListener();
        addScoreboardButtonListener();
    }

    private void addScoreboardButtonListener() {
        Button scoreboardButton = findViewById(R.id.scoreboardButton_picturematching);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ScoreBoardActivity.class);
                intent.putExtra("user", user.getUsername());
                intent.putExtra("gameType", GAME_NAME);
                intent.putExtra("scoreBoardType", "byGame");
                startActivity(intent);

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
                makeToast("Loaded Game");
                switchToGame();
            }
        });
    }
    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Switch to the SlidingTilesGameActivity view to play the game.
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PictureMatchingStartingActivity.this);
                builder.setTitle("Choose a difficulty:");
                builder.setItems(list_diff, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int diff)
                    {
                        boardManager = new MatchingBoardManager(diff * 2 + 4);
                        switchToGame();
                        Toast.makeText(PictureMatchingStartingActivity.this, list_diff[diff] + " selected", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
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
}
