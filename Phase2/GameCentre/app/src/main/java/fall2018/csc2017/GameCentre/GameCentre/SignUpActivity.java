package fall2018.csc2017.GameCentre.GameCentre;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;

import fall2018.csc2017.GameCentre.Data.DatabaseHandler;
import fall2018.csc2017.GameCentre.Data.User;
import fall2018.csc2017.GameCentre.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText nicknameInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText password_repeat_Input;
    private DatabaseHandler db;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // setup text inputs and register button and database
        db = new DatabaseHandler(this);
        nicknameInput = findViewById(R.id.nicknameInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        password_repeat_Input = findViewById(R.id.password_repeat_Input);
        addRegisterListener();
    }

    private void addRegisterListener() {
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = nicknameInput.getText().toString();
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String password_repeat = password_repeat_Input.getText().toString();
                Object[] message = checkInput(username, password, password_repeat);
                if ((boolean) message[0]){
                    user = new User(username, password);
                    if (!nickname.equals(""))
                        user.setNickname(nickname);
                    db.addUser(user);
                    saveToFile(db.getUserFile(username));
                }


                Toast.makeText(getApplication(), (String) message[1], Toast.LENGTH_SHORT).show();
                if (message[0].equals(true)) {
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                }
            }
        });
    }


    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(user);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private Object[] checkInput(String username, String password, String password_repeat) {
        Object[] result = new Object[2];
        if (username.equals("")) {
            result[0] = false;
            result[1] = "Username Cannot Be Empty";
        } else if (username.contains(" ")){
            result[0] = false;
            result[1] = "Username Should Not Contain Spaces";
        } else if (db.userExists(username)) {
            result[0] = false;
            result[1] = "Username Exists!";
        } else if (!password.equals(password_repeat)) {
            result[0] = false;
            result[1] = "Password Do Not Match!";
        } else {
            result[0] = true;
            result[1] = "Successfully Registered!";
        }
        return result;
    }
}
