package csc207project.gamecenter.GameCenter;

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

import csc207project.gamecenter.R;

/**
 * A change password activity.
 */
public class NavChangePassword extends AppCompatActivity {

    /**
     * username and password database.
     */
    private LoginInfo loginInfo;

    /**
     * current user username.
     */
    private String username;
    /**
     * A Edit text for original password.
     */
    private EditText original_password;
    /**
     * A Edit text for new password.
     */
    private EditText new_password;
    /**
     * A Edit text for re-enter password.
     */
    private EditText reenter_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        loadFromFile(GameCentre.SAVE_FILENAME);
        username = getIntent().getStringExtra("userName");
        original_password = findViewById(R.id.original_password);
        new_password = findViewById(R.id.new_password);
        reenter_password = findViewById(R.id.reenter_password);
        addConfirmButton();
    }

    /**
     * add confirm Button to change password activity.
     */
    private void addConfirmButton() {
        Button confirm_button = findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkOriginalPassword(username, original_password)) {
                    Toast.makeText(NavChangePassword.this,
                            "Wrong Original Password", Toast.LENGTH_SHORT).show();
                } else if (!compareTwoPassword(new_password, reenter_password)) {
                    Toast.makeText(NavChangePassword.this,
                            "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                } else {
                    loginInfo.resetPassword(username, new_password.getText().toString());
                    saveToFile(GameCentre.TEMP_SAVE_FILENAME);
                    saveToFile(GameCentre.SAVE_FILENAME);
                    Toast.makeText(NavChangePassword.this,
                            "Password change successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * check whether original password match to current user.
     *
     * @param userName current user username.
     * @param password the password user entered.
     * @return true if the entered password match user password.
     */
    private boolean checkOriginalPassword(String userName, EditText password) {
        return loginInfo.Authenticate(userName, password.getText().toString());
    }

    /**
     * compare whether two passwords user entered match.
     *
     * @param new_password     new password that user entered.
     * @param reenter_password re-entered user password.
     * @return true if two passwords match.
     */
    private boolean compareTwoPassword(EditText new_password, EditText reenter_password) {
        return new_password.getText().toString().equals(reenter_password.getText().toString());
    }

    /**
     * Load loginInfo from fileName.
     *
     * @param fileName the name of the file
     */
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

    /**
     * Save loginInfo from fileName.
     *
     * @param fileName the name of the file
     */
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
}
