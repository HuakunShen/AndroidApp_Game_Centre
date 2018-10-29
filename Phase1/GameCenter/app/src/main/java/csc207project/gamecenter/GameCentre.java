package csc207project.gamecenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GameCentre extends AppCompatActivity implements View.OnClickListener {


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
        username = findViewById(R.id.userName);
        password = findViewById(R.id.Password);
        signInButton = findViewById(R.id.SignInButton);
        signUpButton = findViewById(R.id.SignUpButton);
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    /**
     * @param v Buttons, any button that can be clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // When Sign in button is clicked
            case R.id.SignInButton:
                name = username.getText().toString();
                pw = password.getText().toString();
                loginCheck();
                break;
            // When Sign in button is clicked
            case R.id.SignUpButton:
                startActivity(new Intent(GameCentre.this, AccountRegistration.class));
                break;
        }
    }

    private void loginCheck() {
        if (LoginInfo.IsValidUserName(name)) {
            if (LoginInfo.Authenticate(name, pw)) {
                Intent intent = new Intent(GameCentre.this, GameCentreInterface.class);
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
