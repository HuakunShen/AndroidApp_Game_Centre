package csc207project.gamecenter.GameCenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import csc207project.gamecenter.Data.GameInfo;
import csc207project.gamecenter.R;


public class GameCentre extends AppCompatActivity implements View.OnClickListener {

    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "login.ser";
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "login_tmp.ser";

    /**
     * A permanent userData file
     */
    public static final String USER_DATA_FILE = "user_info.ser";

    /**
     * A permanent userData file
     */
    public static final String TEMP_USER_DATA_FILE = "user_info_temp.ser";

    private LoginInfo loginInfo;
    private GameInfo userData;
    private String name;
    private String pw;
    private Button signInButton;
    private Button signUpButton;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_centre);
        loginInfo = new LoginInfo();
        userData = new GameInfo();
        saveToFile1(TEMP_SAVE_FILENAME);
        saveToFile2(TEMP_USER_DATA_FILE);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.Password);
        signInButton = findViewById(R.id.SignInButton);
        signUpButton = findViewById(R.id.SignUpButton);
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    private void saveToFile1(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(loginInfo);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void saveToFile2(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(userData);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private void loadFromFile1(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                loginInfo = (LoginInfo) input.readObject();
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

    private void loadFromFile2(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                userData = (GameInfo) input.readObject();
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

    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile1(SAVE_FILENAME);
        loadFromFile2(USER_DATA_FILE);
        saveToFile1(TEMP_SAVE_FILENAME);
        saveToFile2(TEMP_USER_DATA_FILE);
    }


    /**
     * @param v Buttons, any button that can be clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // When Sign in button is clicked
            case R.id.SignInButton:
                loadFromFile1(TEMP_SAVE_FILENAME);
                loadFromFile2(TEMP_USER_DATA_FILE);
                name = username.getText().toString();
                pw = password.getText().toString();
                loginCheck();
                break;
            // When Sign in button is clicked
            case R.id.SignUpButton:
                saveToFile1(TEMP_SAVE_FILENAME);
                saveToFile2(TEMP_USER_DATA_FILE);
                startActivity(new Intent(GameCentre.this, AccountRegistration.class));
                break;
        }
    }

    private void loginCheck() {
        if (loginInfo.checkUsername(name)) {
            if (loginInfo.Authenticate(name, pw)) {
                Intent intent = new Intent(GameCentre.this, GameCentreInterface.class);
                intent.putExtra("username", name);
                startActivity(intent);
                makeToastText("Successfully Signed In!");
            } else {
                makeToastText("Wrong Password!");
            }
        } else {
            makeToastText("Username does not exist");
        }
    }

    private void makeToastText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
