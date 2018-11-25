package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.DatabaseHandler;
import fall2018.csc2017.GameCentre.data.User;

import static android.graphics.Bitmap.createBitmap;

public class SlidingTilesNewGamePop extends AppCompatActivity {

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

    private static final int SELECT_IMAGE_CODE = 1801;
    ImageButton importButton;
    Bitmap bitmapCut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingtiles_pop);

        db = new DatabaseHandler(this);
        setupUser();
        setupFile();

        addImportButtonListener();
    }

    /**
     * Activate the import ImageButton.
     */
    private void addImportButtonListener() {
        importButton = findViewById(R.id.select_image);
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
                Bitmap bitmapUncut = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                bitmapCut = cutToTileRatio(bitmapUncut);
                importButton.setImageBitmap(bitmapCut);

//                int bytes = bitmapCut.getByteCount();
//                ByteBuffer buf = ByteBuffer.allocate(bytes);
//                bitmapCut.copyPixelsToBuffer(buf);
//                boardManager.setImageBackground(buf.array());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap cutToTileRatio(Bitmap bitmapUncut) {
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
}