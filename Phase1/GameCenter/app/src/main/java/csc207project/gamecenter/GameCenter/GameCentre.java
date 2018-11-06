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

import csc207project.gamecenter.Data.ScoreDatabase;
import csc207project.gamecenter.Data.WQWDatabase;
import csc207project.gamecenter.R;
import csc207project.gamecenter.SlidingTiles.GameActivity;


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
    public WQWDatabase userData;
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
        saveToFile(GameActivity.TEMP_SCORE_SAVE_FILE, new ScoreDatabase());
        loginInfo = new LoginInfo();
        userData = new WQWDatabase();
        saveToFile(TEMP_SAVE_FILENAME, loginInfo);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.Password);
        signInButton = findViewById(R.id.SignInButton);
        signUpButton = findViewById(R.id.SignUpButton);
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    private void saveToFile(String fileName, Object file) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(file);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        loginInfo = loadFromFile(SAVE_FILENAME).equals(-1) ?
                new LoginInfo() : (LoginInfo) loadFromFile(SAVE_FILENAME);
        saveToFile(TEMP_SAVE_FILENAME,loginInfo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(USER_DATA_FILE, userData);
    }

    /**
     * @param v Buttons, any button that can be clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // When Sign in button is clicked
            case R.id.SignInButton:
                loginInfo = loadFromFile(TEMP_SAVE_FILENAME).equals(-1) ?
                        new LoginInfo() : (LoginInfo) loadFromFile(TEMP_SAVE_FILENAME);
                name = username.getText().toString();
                pw = password.getText().toString();
                saveToFile(TEMP_SAVE_FILENAME, userData);
                loginCheck();
                break;
            // When Sign in button is clicked
            case R.id.SignUpButton:
                saveToFile(TEMP_SAVE_FILENAME, loginInfo);
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
