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

import csc207project.gamecenter.R;


public class AccountRegistration extends AppCompatActivity implements View.OnClickListener {

    private EditText nickname;
    private EditText username;
    private EditText password;
    private EditText password_repeat;
    private Button register_button;
    private String name;
    private String usrname;
    private String pw;
    private String pw_repeat;
    private LoginInfo loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_registration);
        loadFromFile(GameCentre.TEMP_SAVE_FILENAME);
        register_button = findViewById(R.id.registerButton);
        nickname = findViewById(R.id.nicknameInput);
        username = findViewById(R.id.usernameInput);
        password = findViewById(R.id.passwordInput);
        password_repeat = findViewById(R.id.password_repeat_Input);
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        name = nickname.getText().toString();
        usrname = username.getText().toString();
        pw = password.getText().toString();
        pw_repeat = password_repeat.getText().toString();
        String message = checkPassword(usrname, pw, pw_repeat);
        Boolean response = loginInfo.Register(usrname, pw, pw_repeat);
        saveToFile(GameCentre.TEMP_SAVE_FILENAME);
        saveToFile(GameCentre.SAVE_FILENAME);
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
        if (response.equals(true)) {
            Intent intent = new Intent(AccountRegistration.this, GameCentre.class);
            intent.putExtra("loginInfo", loginInfo);
            startActivity(intent);

        }

    }

    private String checkPassword(String usrname, String pw, String pw_repeat) {
        if (usrname.equals("")) {
            return "Empty Username!";
        } else if (!loginInfo.isValidUserName(usrname)) {
            return "Username Exists!";
        } else if (!pw.equals(pw_repeat)) {
            return "Password Do Not Match!";
        } else {
            return "Successfully Registered!";
        }
    }

    private void loadFromFile(String fileName) {
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


    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(loginInfo);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(GameCentre.TEMP_SAVE_FILENAME);
    }



}

