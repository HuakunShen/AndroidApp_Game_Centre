package csc207project.gamecenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameCentreInterface extends AppCompatActivity implements View.OnClickListener{


    private Button SlidingTiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_centre_interface);
        SlidingTiles = findViewById(R.id.SlidingTiles);
        SlidingTiles.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // When Sign in button is clicked
            case R.id.SlidingTiles:
                startActivity(new Intent(GameCentreInterface.this,
                        StartingActivity.class));
        }
    }


}
