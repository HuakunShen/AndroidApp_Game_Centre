package csc207project.gamecenter.SlidingTiles;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.widget.ImageView;

import java.io.IOException;
import java.io.ObjectOutputStream;

import csc207project.gamecenter.R;

/**
 * An extension of StartingActivity on clicking NewGame
 * The basic function of this activity is to let user be able to
 * - select difficulty
 * - import their own image
 */
public class NewGameActivity extends AppCompatActivity {

    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";
    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * Variable to store Uri of imported image.
     */
    private static final int SELECT_IMAGE = 1801;
    Uri imageUri;
    ImageView image_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        boardManager = new BoardManager();
        image_selected = (ImageView) findViewById(R.id.newgame_im_preview);
        addStartButtonListener();
        addImportButtonListener();
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.newgame_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(TEMP_SAVE_FILENAME);
                Intent to_game = new Intent(NewGameActivity.this, GameActivity.class);
                startActivity(to_game);
            }
        });
    }

    private void addImportButtonListener() {
        Button startButton = findViewById(R.id.newgame_import);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            //TODO we need to chop this photo to be a square
            image_selected.setImageURI(imageUri);
        }
    }


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
