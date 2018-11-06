package csc207project.gamecenter.GameCenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import csc207project.gamecenter.R;
import csc207project.gamecenter.ScoreBoard.ScoreBoardActivity;
import csc207project.gamecenter.SlidingTiles.StartingActivity;

/**
 * A Game Center Interface.
 */
public class GameCentreInterface extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * File name where we save to and load from
     */
    public static final String SAVE_NICKNAMES = "save_nick_names.ser";
    public static final String SAVE_AVATARS = "save_avatars.ser";
    /**
     * A nickName HashMap to store user and their corresponding nickName.
     */
    private HashMap<String, String> nickNames = new HashMap<>();
    /**
     * A avatar HashMap to store user and their corresponding avatar.
     */
    private HashMap<String, String> avatars = new HashMap<>();
    /**
     * current user name.
     */
    private String username;
    /**
     * A TextView to display.
     */
    private TextView userNickName;
    /**
     * A image Button to display.
     */
    private ImageButton icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_centre_interface);
        loadFromFile(SAVE_NICKNAMES);
        loadFromFile(SAVE_AVATARS);
        addSlidingTilesButton();
        // get name from previous activity.
        username = getIntent().getStringExtra("username");
        addNavigationView();

    }

    /**
     * add Navigation View to GameCenterInterface.
     */
    private void addNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        LinearLayout header = headerView.findViewById(R.id.nav_header);
        addIconButton(header);
        setTextView(headerView);
    }

    /**
     * add textView to headerView
     *
     * @param headerView A view where we should put textView.
     */
    private void setTextView(View headerView) {
        TextView userAccountName = headerView.findViewById(R.id.userAccountName);
        userAccountName.setText(username);
        userNickName = headerView.findViewById(R.id.userNickName);
        if (!nickNames.containsKey(username)) {
            nickNames.put(username, username);
            userNickName.setText(username);
        } else {
            userNickName.setText(nickNames.get(username));
        }
        saveToFile(SAVE_NICKNAMES);
    }

    /**
     * add Icon button to linearLayout header.
     *
     * @param header A Linear Layout where we put Icon button.
     */
    private void addIconButton(LinearLayout header) {
        icon = header.findViewById(R.id.userIcon);
        icon.setImageResource(R.mipmap.cool_jason);
        saveToFile(SAVE_AVATARS);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSetting = new Intent(GameCentreInterface.this, NavSetting.class);
                toSetting.putExtra("userName", username);
                startActivity(toSetting);
            }
        });
    }

    /**
     * add sliding tile button to GameCenterInterface.
     */
    private void addSlidingTilesButton() {
        Button slidingTiles = findViewById(R.id.SlidingTiles);
        slidingTiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameCentreInterface.this,
                        StartingActivity.class);
                intent.putExtra("userName", username);
                startActivity(intent);
            }
        });
    }


    /**
     * When Click Items in Navigation layout, intent to another Activity
     *
     * @param item menu items in Navigation layout
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.change_password) {
            goToSetting();
        } else if (id == R.id.score_board) {
            goToScoreBoard();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * go to scoreBoard.
     */
    private void goToScoreBoard() {
        Intent toScoreBoard = new Intent(this, ScoreBoardActivity.class);
        startActivity(toScoreBoard);
    }

    /**
     * things need to do when click setting.
     */
    private void goToSetting() {
        if (username.equals("admin")) {
            Toast.makeText(GameCentreInterface.this,
                    "admin cannot change password!", Toast.LENGTH_SHORT).show();
        } else {
            Intent toChangePassword = new Intent(this, NavChangePassword.class);
            toChangePassword.putExtra("userName", username);
            startActivity(toChangePassword);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(SAVE_NICKNAMES);
        userNickName.setText(nickNames.get(username));
        loadFromFile(SAVE_AVATARS);
        if (avatars.containsKey(username)) {
            icon.setImageURI(Uri.parse(avatars.get(username)));
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        avatars.remove(username);
        saveToFile(SAVE_NICKNAMES);
        saveToFile(SAVE_AVATARS);
    }

    /**
     * Load the nickNames or avatars from fileName.
     *
     * @param fileName the name of the file
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (fileName.equals(SAVE_NICKNAMES)) {
                    nickNames = (HashMap<String, String>) input.readObject();
                } else if (fileName.equals(SAVE_AVATARS)) {
                    avatars = (HashMap<String, String>) input.readObject();
                }
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
     * Save the nickNames or avatars to fileName.
     *
     * @param fileName the name of the file
     */
    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            if (fileName.equals(SAVE_NICKNAMES)) {
                outputStream.writeObject(nickNames);
            } else if (fileName.equals(SAVE_AVATARS)) {
                outputStream.writeObject(avatars);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}
