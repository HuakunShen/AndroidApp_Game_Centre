package csc207project.gamecenter.GameCenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import csc207project.gamecenter.R;
import csc207project.gamecenter.SlidingTiles.StartingActivity;

public class GameCentreInterface extends AppCompatActivity implements View.OnClickListener{


    private Button SlidingTiles;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_centre_interface);
        SlidingTiles = findViewById(R.id.SlidingTiles);

        username = getIntent().getStringExtra("username");
        SlidingTiles.setOnClickListener(this);
//        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // When Sign in button is clicked
            case R.id.SlidingTiles:
                Intent intent = new Intent(GameCentreInterface.this,
                        StartingActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
        }
    }


}
