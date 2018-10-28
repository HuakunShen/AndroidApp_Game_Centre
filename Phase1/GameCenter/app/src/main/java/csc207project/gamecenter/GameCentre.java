package csc207project.gamecenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.PasswordAuthentication;

public class GameCentre extends AppCompatActivity {

    private TextView username;
    private TextView password;
    private Button signInButton;
    private Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_centre);
        username = (TextView) findViewById(R.id.userName);
        password = (TextView) findViewById(R.id.Password);

        String name = username.getText().toString();
        String pw = password.getText().toString();
        addSignInButtonListener();
        addSignUpButtonListener();
    }

    private void addSignUpButtonListener() {
        signUpButton = (Button) findViewById(R.id.SignUpButton);
        signUpButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));
    }

    private void addSignInButtonListener() {
        signInButton = (Button) findViewById(R.id.SignInButton);
        signInButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        })
        );
    }


}
