package fall2018.csc2017.GameCentre.gameCentre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;

public class NavChangePassword extends AppCompatActivity {

    private String username;

    private SQLDatabase db;

    private User user;

    private Button confirmButton;

    private EditText original_password;

    private EditText new_password;

    private EditText reenter_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_change_password);
        db = new SQLDatabase(this);
        setupUser();
        setupEditTexts();
        addConfirmButton();


    }

    private void setupEditTexts() {
        original_password = findViewById(R.id.original_password);
        new_password = findViewById(R.id.new_password);
        reenter_password = findViewById(R.id.reenter_password);
    }

    private void addConfirmButton() {
        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkOriginalPassword(original_password)) {
                    Toast.makeText(NavChangePassword.this,
                            "Wrong Original Password", Toast.LENGTH_SHORT).show();
                } else if (!compareTwoPassword(new_password, reenter_password)) {
                    Toast.makeText(NavChangePassword.this,
                            "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                } else {
                    user.setPassword(new_password.getText().toString());
                    saveToFile(db.getUserFile(username));
                    Toast.makeText(NavChangePassword.this,
                            "Password change successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkOriginalPassword(EditText originalPassword) {
        String password = originalPassword.getText().toString();
        return user.checkPassword(password);
    }

    private boolean compareTwoPassword(EditText newPassword, EditText ReenterPassword) {
        return newPassword.getText().toString().equals(ReenterPassword.getText().toString());
    }

    private void setupUser() {
        username = getIntent().getStringExtra("user");
        loadFromFile(db.getUserFile(username));
    }

    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                user = (User) input.readObject();
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
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(user);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
