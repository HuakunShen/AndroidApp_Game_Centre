package csc207project.gamecenter.GameCenter;

import android.app.FragmentManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import csc207project.gamecenter.R;
import csc207project.gamecenter.SlidingTiles.StartingActivity;

public class GameCentreInterface extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    public static final String SAVE_NICKNAMES = "save_nick_names.ser";
    public static final String SAVE_AVATARS = "save_avatars.ser";

    private HashMap<String, String> nickNames = new HashMap<>();
    private HashMap<String, String> avatars = new HashMap<>();
    private Button SlidingTiles;
    private String username;
    private TextView userNickName;
    private ImageButton icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadFromFile(SAVE_NICKNAMES);
        loadFromFile(SAVE_AVATARS);
        setContentView(R.layout.activity_game_centre_interface);
        SlidingTiles = findViewById(R.id.SlidingTiles);

        username = getIntent().getStringExtra("username");

        SlidingTiles.setOnClickListener(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout header =  headerView.findViewById(R.id.nav_header);
        icon = header.findViewById(R.id.userIcon);
        icon.setImageResource(R.mipmap.cool_jason);
        if (avatars.containsKey(username)) {
            icon.setImageURI(Uri.parse(avatars.get(username)));
        }else{
            icon.setImageResource(R.mipmap.cool_jason);
        }
        saveToFile(SAVE_AVATARS);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSetting = new Intent(GameCentreInterface.this, NavSetting.class);
                toSetting.putExtra("userName", username);
                startActivity(toSetting);
//                drawer.closeDrawer(GravityCompat.START);
            }
        });
        TextView userAccountName = headerView.findViewById(R.id.userAccountName);
        userAccountName.setText(username);
        userNickName = headerView.findViewById(R.id.userNickName);
        if ( !nickNames.containsKey(username)) {
            nickNames.put(username, username);
            userNickName.setText(username);
        }else{
            userNickName.setText(nickNames.get(username));
        }
        saveToFile(SAVE_NICKNAMES);



//        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
//        Toolbar toolbar =  findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
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
    protected void onPause(){
        super.onPause();
        saveToFile(SAVE_NICKNAMES);
        saveToFile(SAVE_AVATARS);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // When Sign in button is clicked
            case R.id.SlidingTiles:
                Intent intent = new Intent(GameCentreInterface.this,
                        StartingActivity.class);
                intent.putExtra("userName", username);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_first_layout) {
            Intent toChangePassword = new Intent(this, NavChangePassword.class);
            toChangePassword.putExtra("userName",username);
            startActivity(toChangePassword);
        } else if (id == R.id.nav_second_layout) {
            Intent toScoreBoard = new Intent(this, NavScoreBoard.class);
            startActivity(toScoreBoard);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (fileName.equals(SAVE_NICKNAMES)){
                    nickNames = (HashMap<String, String>) input.readObject();
                }else if (fileName.equals(SAVE_AVATARS)){
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
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            if (fileName.equals(SAVE_NICKNAMES)){
                outputStream.writeObject(nickNames);
            }else if (fileName.equals(SAVE_AVATARS)){
                outputStream.writeObject(avatars);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}
