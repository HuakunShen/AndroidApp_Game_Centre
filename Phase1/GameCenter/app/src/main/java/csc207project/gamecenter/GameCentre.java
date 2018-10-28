package csc207project.gamecenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.PasswordAuthentication;

public class GameCentre extends AppCompatActivity {

    private TextView username;
    private TextView password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_centre);
        username = (TextView) findViewById(R.id.userName);
        password = (TextView) findViewById(R.id.Password);

        addSignInButtonListener();
        addSignUpButtonListener();
    }

    private void addSignUpButtonListener() {
        Button signUpButton = findViewById(R.id.SignUpButton);
        signUpButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeToastText("Sign up");
            }


        }));
    }
    private void makeToastText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void addSignInButtonListener() {
        Button signInButton = findViewById(R.id.SignInButton);

        String name = username.getText().toString();
        String pw = password.getText().toString();
        signInButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeToastText("Sign in");
            }
        })
        );
    }


}
