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
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import csc207project.gamecenter.R;

public class NavSetting extends AppCompatActivity {

    private HashMap<String, String> nickNames;
    private HashMap<String, Uri> avatars;
    private String username;

    private static final int SELECT_IMAGE = 1801;
    Uri imageUri;
    ImageView image_selected;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_setting);
        username = getIntent().getStringExtra("userName");

        loadFromFile(GameCentreInterface.SAVE_NICKNAMES);

        final EditText nickName = findViewById(R.id.nick_name);
        nickName.setText(nickNames.get(username));

        Button applyButton = findViewById(R.id.apply_Button);
        Button cancelButton = findViewById(R.id.cancel_button);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickname = nickName.getText().toString();
                nickNames.put(username, newNickname);
                saveToFile(GameCentreInterface.SAVE_NICKNAMES);
                Toast.makeText(NavSetting.this, "change nickname Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavSetting.this, "Nothing happens, haha!", Toast.LENGTH_SHORT).show();

            }
        });

        Button avatarButton = findViewById(R.id.import_button);
        avatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        image_selected = findViewById(R.id.imageView);


    }

    private void openGallery(){
        Intent get_phote = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(get_phote,SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            image_selected.setImageURI(imageUri);
            if (avatars == null){
                avatars = new HashMap<>();
            }
            avatars.put(username, imageUri);
            saveToFile(GameCentreInterface.SAVE_NICKNAMES);
        }
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
                nickNames = (HashMap<String, String>) input.readObject();
                avatars = (HashMap<String, Uri>) input.readObject();
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

    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(nickNames);
            outputStream.writeObject(avatars);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
