package csc207project.gamecenter;

import android.accounts.Account;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AccountRegistration extends AppCompatActivity {

    private EditText nickname;
    private EditText username;
    private EditText password;
    private EditText password_repeat;
    private Button register_button;
    private String name;
    private String usrname;
    private String pw;
    private String pw_repeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_registration);
        register_button = findViewById(R.id.registerButton);
        nickname = findViewById(R.id.nicknameInput);
        username = findViewById(R.id.usernameInput);
        password = findViewById(R.id.passwordInput);
        password_repeat = findViewById(R.id.password_repeat_Input);
        register_button.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name = nickname.getText().toString();
                usrname = username.getText().toString();
                pw = password.getText().toString();
                pw_repeat = password_repeat.getText().toString();

                String response = LoginInfo.Register(usrname, pw, pw_repeat);
                Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT);
                if (response.equals("Registered!")) {
                    startActivity(new Intent(AccountRegistration.this, GameCentre.class));

                }
            }
        }));
    }
}
