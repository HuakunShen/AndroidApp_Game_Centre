package csc207project.gamecenter.GameCenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import csc207project.gamecenter.R;

/**
 * A Setting Activity.
 */
public class NavSetting extends AppCompatActivity {

    /**
     * A nickName HashMap to store user and their corresponding nickName.
     */
    private HashMap<String, String> nickNames = new HashMap<>();
    /**
     * A avatar HashMap to store user and their corresponding avatar.
     */
    private HashMap<String, String> avatars = new HashMap<>();
    /**
     * current user name.
     */
    private String username;
    /**
     * Code to identify which intent you came back.
     */
    private static final int SELECT_IMAGE = 42;
    /**
     * the image view in setting activity.
     */
    private ImageView image_selected;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_setting);
        username = getIntent().getStringExtra("userName");
        loadFromFile(GameCentreInterface.SAVE_NICKNAMES);
        loadFromFile(GameCentreInterface.SAVE_AVATARS);
        final EditText nickName = findViewById(R.id.nick_name);
        nickName.setText(nickNames.get(username));
        addApplyButton(nickName);
        addCancelButton();
        addAvatarButton();
        setImageSelected();
    }

    /**
     * Set image from gallery to the imageView.
     */
    private void setImageSelected() {
        image_selected = findViewById(R.id.imageView);
        if (avatars.containsKey(username)) {
            image_selected.setImageURI(Uri.parse(avatars.get(username)));
        }
    }

    /**
     * add avatar button to setting activity.
     */
    private void addAvatarButton() {
        Button avatarButton = findViewById(R.id.import_button);
        avatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    /**
     * add cancel button to setting activity.
     */
    private void addCancelButton() {
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavSetting.this,
                        "Nothing happens, haha!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * add apply button to setting activity.
     *
     * @param nickName new nick name that we need to change to.
     */
    private void addApplyButton(final EditText nickName) {
        Button applyButton = findViewById(R.id.apply_Button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickname = nickName.getText().toString();
                nickNames.put(username, newNickname);
                saveToFile(GameCentreInterface.SAVE_NICKNAMES);

                Toast.makeText(NavSetting.this,
                        "change nickname and avatar Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * open gallery
     */
    private void openGallery() {
        Intent get_phote = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(get_phote, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            image_selected.setImageURI(imageUri);
            String stringURI = imageUri.toString();
            avatars.put(username, stringURI);
            saveToFile(GameCentreInterface.SAVE_AVATARS);
        }
    }

    /**
     * Load the nick names and avatars from fileName.
     *
     * @param fileName the name of the file
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (fileName.equals(GameCentreInterface.SAVE_NICKNAMES)) {
                    nickNames = (HashMap<String, String>) input.readObject();
                } else if (fileName.equals(GameCentreInterface.SAVE_AVATARS)) {
                    avatars = (HashMap<String, String>) input.readObject();
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
     * Save the nickNames or avatars to fileName.
     *
     * @param fileName the name of the file
     */
    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            if (fileName.equals(GameCentreInterface.SAVE_NICKNAMES)) {
                outputStream.writeObject(nickNames);
            } else if (fileName.equals(GameCentreInterface.SAVE_AVATARS)) {
                outputStream.writeObject(avatars);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
