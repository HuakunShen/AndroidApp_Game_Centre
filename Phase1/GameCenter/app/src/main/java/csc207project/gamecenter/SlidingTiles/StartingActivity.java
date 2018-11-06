package csc207project.gamecenter.SlidingTiles;

import android.app.IntentService;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import csc207project.gamecenter.AutoSave.AutoSave;
import csc207project.gamecenter.Data.WQWDatabase;
import csc207project.gamecenter.GameCenter.GameCentre;
import csc207project.gamecenter.R;

import static android.graphics.Bitmap.createBitmap;

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
    private static final int SELECT_IMAGE = 1801;
    /**
     * The board manager.
     */
    private BoardManager boardManager;

    private String currentUser;

    private EditText undoLimit;

    private final int MAX_UNDO_LIMIT = 20;

    public static String PACKAGE_NAME;
    public static Resources RESOURCES;

    private static final int SELECTED_IMAGE = 1801;

    public RadioButton withImageButton;
    public RadioButton withNumberButton;
    public RadioGroup radioButtonGroup;
    private boolean withImage;

    Uri imageUri;
    ImageButton importButton;
    Bitmap bitmapCut;
    public static Bitmap[] tileImages3x3  = new Bitmap[9];
    public static Bitmap[] tileImages4x4  = new Bitmap[16];
    public static Bitmap[] tileImages5x5  = new Bitmap[25];



    /**
     * The difficulties can be selected.
     */
    Spinner select_diff;
    private String[] list_diff = new String[]{"Easy(3x3)", "Normal(4x4)", "Hard(5x5)"};
    private int selected_diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        RESOURCES = getResources();
        bitmapCut = null;
        withImage = false;

        boardManager = new BoardManager();
        saveToFile(TEMP_SAVE_FILENAME, boardManager);

        currentUser = getIntent().getStringExtra("username");

        Log.d("Starting Activity get username", "Username: " + currentUser);
        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addImportButtonListener();
        addRadioButtonListener();
        withNumberButton.setChecked(true);

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

    private void addRadioButtonListener() {
        radioButtonGroup = findViewById(R.id.radioGroup);
        withImageButton = findViewById(R.id.withImageButton);
        withNumberButton = findViewById(R.id.withNumberButton);
        withImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartingActivity.this, "With Image Selected",
                        Toast.LENGTH_SHORT).show();
                if (bitmapCut != null && tileImages3x3[0] == null) {
                    cutImageToTiles();
                }
                importButton.setVisibility(View.VISIBLE);
                withImage = true;
            }
        });
        withNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartingActivity.this, "With Number Selected",
                        Toast.LENGTH_SHORT).show();
                clearImages();
                importButton.setVisibility(View.INVISIBLE);
                withImage = false;
            }
        });
    }

    private void addImportButtonListener() {
        importButton = findViewById(R.id.select_image);
        importButton.setVisibility(View.INVISIBLE);
        importButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent get_photo = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(get_photo, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();

            try {
                FileInputStream image_fis = new FileInputStream(getContentResolver().openFileDescriptor(imageUri, "r").getFileDescriptor());
                Bitmap bitmapUncut = BitmapFactory.decodeStream(image_fis);
                bitmapCut = cutToTileRatio(bitmapUncut);
                importButton.setImageBitmap(bitmapCut);

                cutImageToTiles();
            }catch (FileNotFoundException e){
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap cutToTileRatio(Bitmap bitmapUncut) {
        int width = bitmapUncut.getWidth();
        int height = bitmapUncut.getHeight();
        if (width * 320 > height * 250) {
            int x = (width - height * 250 / 320) / 2;
            return createBitmap(bitmapUncut, x, 0, width - 2 * x, height, null, false);
        }
        else if (width * 320 < height * 250) {
            int y = (height - width * 320 / 250) / 2;
            return createBitmap(bitmapUncut, 0, y, width, height - 2 * y, null, false);
        }
        else {
            return bitmapUncut;
        }
    }

    private void cutImageToTiles() {
        int width = bitmapCut.getWidth();
        int height = bitmapCut.getHeight();

//        Toast.makeText(this, Integer.toString(width), Toast.LENGTH_SHORT).show();
//        Bitmap temp = createBitmap(bitmapCut, 0 * (width / 3), 0 * (height / 3), width / 3, height / 3, null, false);
//        tileImages3x3[0] = temp;
//        importButton.setImageBitmap(tileImages3x3[0]);

        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tileImages3x3[count] = createBitmap(bitmapCut, i * (width / 3), j * (height / 3), width / 3, height / 3, null, false);
                count++;
            }
        }
        count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tileImages4x4[count] = createBitmap(bitmapCut, i * (width / 4), j * (height / 4), width / 4, height / 4, null, false);
                count++;
            }
        }
        count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tileImages5x5[count] = createBitmap(bitmapCut, i * (width / 5), j * (height / 5), width / 5, height / 5, null, false);
                count++;
            }
        }
    }

    public void clearImages() {
        tileImages3x3  = new Bitmap[9];
        tileImages4x4  = new Bitmap[16];
        tileImages5x5  = new Bitmap[25];
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
                if (withImage && tileImages3x3[0] == null) {
                    Toast.makeText(StartingActivity.this,
                            "You need to import image!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        boardManager = loadFromFile(SAVE_FILENAME).equals(-1) ?
                                new BoardManager() : (BoardManager) loadFromFile(SAVE_FILENAME);
                        if (!boardManager.userExist(currentUser)) {
                            boardManager.addUser(currentUser);
                        }
                        setDifficulty(selected_diff);
                        boardManager.addState(currentUser, (new BoardManager()).getBoard());
                        boardManager.setBoard(boardManager.getBoard(currentUser));
                    } catch (Exception e) {
                        setDifficulty(selected_diff);
                        boardManager = new BoardManager();
                    }

                    setUndoSteps();
                    saveToFile(TEMP_SAVE_FILENAME, boardManager);
                    switchToGame();
                }
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

    @Override
    protected void onStop() {
        super.onStop();
        WQWDatabase userData = (WQWDatabase) loadFromFile(GameCentre.USER_DATA_FILE);
        saveToFile(SAVE_FILENAME, boardManager);
        saveToFile(GameCentre.USER_DATA_FILE, userData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearImages();
        WQWDatabase userData = (WQWDatabase) loadFromFile(GameCentre.USER_DATA_FILE);
        saveToFile(SAVE_FILENAME, boardManager);
        saveToFile(GameCentre.USER_DATA_FILE, userData);
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (withImage && tileImages3x3[0] == null) {
                    Toast.makeText(StartingActivity.this,
                            "You need to import image!", Toast.LENGTH_SHORT).show();
                }
                else {
                    boardManager = loadFromFile(SAVE_FILENAME).equals(-1) ?
                            new BoardManager() : (BoardManager) loadFromFile(SAVE_FILENAME);
                    if (boardManager.userExist(currentUser)) {
                        boardManager.setBoard(boardManager.getBoard(currentUser));
                        saveToFile(TEMP_SAVE_FILENAME, boardManager);
                        setDifficulty(boardManager.getDifficulty());
                        makeToastLoadedText();
                        switchToGame();
                    } else {
                        try {
                            boardManager = loadFromFile(SAVE_FILENAME).equals(-1) ?
                                    new BoardManager() : (BoardManager) loadFromFile(SAVE_FILENAME);
                            if (!boardManager.userExist(currentUser)) {
                                boardManager.addUser(currentUser);
                            }
                            setDifficulty(selected_diff);
                            boardManager.addState(currentUser, (new BoardManager()).getBoard());
                            boardManager.setBoard(boardManager.getBoard(currentUser));
                        } catch (Exception e) {
                            setDifficulty(selected_diff);
                            boardManager = new BoardManager();
                        }
                        saveToFile(TEMP_SAVE_FILENAME, boardManager);
                        switchToGame();
                    }
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
                saveToFile(SAVE_FILENAME, boardManager);
                saveToFile(TEMP_SAVE_FILENAME, boardManager);
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
        Log.d("Switch TO Game", "Username: " + currentUser);

        saveToFile(StartingActivity.TEMP_SAVE_FILENAME, boardManager);
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
    public void saveToFile(String fileName, Object file) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(file);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void setDifficulty(int diff){
        Board.NUM_COLS = diff;
        Board.NUM_ROWS = diff;
    }
}
