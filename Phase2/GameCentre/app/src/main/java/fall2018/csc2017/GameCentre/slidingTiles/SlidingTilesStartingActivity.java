package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fall2018.csc2017.GameCentre.data.DatabaseHandler;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;

import static android.graphics.Bitmap.createBitmap;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class SlidingTilesStartingActivity extends AppCompatActivity {

    private static final int MAX_UNDO_LIMIT = 20;
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
    public static final String GAME_NAME = "SlidingTiles";
    private SlidingTilesBoardManager boardManager;

    private String[] list_diff = new String[]{"Easy(3x3)", "Normal(4x4)", "Hard(5x5)"};
    private int selected_difficulty;

    public static String PACKAGE_NAME;
    public static Resources RESOURCES;

    private static final int SELECT_IMAGE_CODE = 1801;
    ImageButton importButton;
    Bitmap bitmapCut;
    public static Bitmap[] tileImages3x3 = new Bitmap[9];
    public static Bitmap[] tileImages4x4 = new Bitmap[16];
    public static Bitmap[] tileImages5x5 = new Bitmap[25];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        username = getIntent().getStringExtra("user");
        PACKAGE_NAME = getApplicationContext().getPackageName();
        RESOURCES = getResources();
        selected_difficulty = 4;

        setupUser();
        setupFile();

        boardManager = new SlidingTilesBoardManager();
        saveToFile(tempGameStateFile);

        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addDiffSpinnerListener();
        addImportButtonListener();
        addRadioButtonListener();
    }

    /**
     * setup user object according to username and define the value of userFile (where user
     * object is saved)
     */
    private void setupUser() {
        username = getIntent().getStringExtra("user");
        userFile = db.getUserFile(username);
        loadFromFile(userFile);
//        Toast.makeText(this, "Welcome " + user.getUsername(), Toast.LENGTH_SHORT).show();
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
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        final EditText undoLimit = findViewById(R.id.undoLimitInput);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boardManager = new SlidingTilesBoardManager();
                if (importButton.getVisibility() == View.VISIBLE && tileImages3x3[0] == null) {
                    Toast.makeText(SlidingTilesStartingActivity.this,
                            "You need to import image!", Toast.LENGTH_SHORT).show();
                } else {
                    setDifficulty(selected_difficulty);
                    boardManager = new SlidingTilesBoardManager();
                    if (setUndoSteps(undoLimit))
                        switchToGame();
                }

            }
        });
    }

    /**
     * set up undo limit for steps
     *
     * @param undoLimit
     * @return
     */
    public boolean setUndoSteps(EditText undoLimit) {
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
            return false;
        } else {
            boardManager.setCapacity(input);
            return true;
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
                if (importButton.getVisibility() == View.VISIBLE && tileImages3x3[0] == null) {
                    Toast.makeText(SlidingTilesStartingActivity.this,
                            "You need to import image!", Toast.LENGTH_SHORT).show();
                } else {
                    loadFromFile(gameStateFile);
                    saveToFile(tempGameStateFile);
                    setDifficulty(boardManager.getDifficulty());
                    makeToast("Loaded Game");
                    switchToGame();
                }
            }
        });
    }



    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(gameStateFile);
                saveToFile(tempGameStateFile);
                makeToast("Game Saved");
            }
        });
    }

    /**
     * Activate the spinner for selecting difficulty.
     */
    private void addDiffSpinnerListener() {
        Spinner select_diff = findViewById(R.id.list_diff_sele);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_diff);
        select_diff.setAdapter(arrayAdapter);

        select_diff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) == list_diff[0]) {
                    selected_difficulty = 3;
                } else if (parent.getItemAtPosition(position) == list_diff[1]) {
                    selected_difficulty = 4;
                } else if (parent.getItemAtPosition(position) == list_diff[2]) {
                    selected_difficulty = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_difficulty = 4;
            }
        });
    }

    /**
     * Activate the import ImageButton.
     */
    private void addImportButtonListener() {
        importButton = findViewById(R.id.select_image);
        importButton.setVisibility(View.INVISIBLE);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }


    /**
     * open gallery for image selection
     */
    private void openGallery() {
        Intent get_photo = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(get_photo, SELECT_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            try {
                FileInputStream image_fis = new FileInputStream(getContentResolver().openFileDescriptor(imageUri, "r").getFileDescriptor());
                Bitmap bitmapUncut = BitmapFactory.decodeStream(image_fis);
                bitmapCut = cutToTileRatio(bitmapUncut);
                importButton.setImageBitmap(bitmapCut);

                cutImageToTiles();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Activate the radio buttons.
     */
    private void addRadioButtonListener() {
        RadioButton withImageButton = findViewById(R.id.withImageButton);
        RadioButton withNumberButton = findViewById(R.id.withNumberButton);
        withNumberButton.setChecked(true);
        withImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SlidingTilesStartingActivity.this, "With Image Selected",
                        Toast.LENGTH_SHORT).show();
                if (bitmapCut != null && tileImages3x3[0] == null) {
                    cutImageToTiles();
                }
                importButton.setVisibility(View.VISIBLE);
            }
        });
        withNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SlidingTilesStartingActivity.this, "With Number Selected",
                        Toast.LENGTH_SHORT).show();
                clearImages();
                importButton.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(tempGameStateFile);
    }

    /**
     * Switch to the SlidingTilesGameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SlidingTilesGameActivity.class);
        saveToFile(tempGameStateFile);
        tmp.putExtra("user", username);
        startActivity(tmp);
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
                } else if (fileName.equals(gameStateFile) || fileName.equals(tempGameStateFile)) {
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

    /**
     * Set the size of board to match with difficulty.
     *
     * @param diff the difficulty to set as
     */
    private void setDifficulty(int diff) {
        SlidingTilesBoard.NUM_ROWS = diff;
        SlidingTilesBoard.NUM_COLS = diff;
    }

    public Bitmap cutToTileRatio(Bitmap bitmapUncut) {
        int width = bitmapUncut.getWidth();
        int height = bitmapUncut.getHeight();
        if (width * 320 > height * 250) {
            int x = (width - height * 250 / 320) / 2;
            return createBitmap(bitmapUncut, x, 0, width - 2 * x, height, null, false);
        } else if (width * 320 < height * 250) {
            int y = (height - width * 320 / 250) / 2;
            return createBitmap(bitmapUncut, 0, y, width, height - 2 * y, null, false);
        } else {
            return bitmapUncut;
        }
    }

    private void cutImageToTiles() {
        int width = bitmapCut.getWidth();
        int height = bitmapCut.getHeight();

        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tileImages3x3[count] = createBitmap(bitmapCut, i * (width / 3),
                        j * (height / 3), width / 3, height / 3, null, false);
                count++;
            }
        }
        count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tileImages4x4[count] = createBitmap(bitmapCut, i * (width / 4),
                        j * (height / 4), width / 4, height / 4, null, false);
                count++;
            }
        }
        count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tileImages5x5[count] = createBitmap(bitmapCut, i * (width / 5),
                        j * (height / 5), width / 5, height / 5, null, false);
                count++;
            }
        }
    }


    public void clearImages() {
        tileImages3x3 = new Bitmap[9];
        tileImages4x4 = new Bitmap[16];
        tileImages5x5 = new Bitmap[25];
        importButton.setImageDrawable(getResources().getDrawable(R.drawable.import_image));
    }
}
