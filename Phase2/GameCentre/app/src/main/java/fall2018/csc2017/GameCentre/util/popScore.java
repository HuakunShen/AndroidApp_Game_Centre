package fall2018.csc2017.GameCentre.util;
// Source: https://www.youtube.com/watch?v=fn5OlqQuOCk

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.gameCentre.GameCentreInterfaceActivity;
import fall2018.csc2017.GameCentre.gameCentre.ScoreBoardActivity;
import fall2018.csc2017.GameCentre.pictureMatching.PictureMatchingStartingActivity;

public class popScore extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_score);
        setupPopUpWindow();

    }

    private void setupPopUpWindow() {
        int score = getIntent().getIntExtra("score", 0);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(((int) (width * .8)), (int) (height * .3));
        TextView scoreDisplay = findViewById(R.id.scoreDisplay);
        scoreDisplay.setText(String.valueOf(score));
        Button goToScoreBoardButton = findViewById(R.id.popScoreToScoreboard);
        goToScoreBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), ScoreBoardActivity.class));
            }
        });
        Button goToGameCentreInterface = findViewById(R.id.backButton);
        goToGameCentreInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), GameCentreInterfaceActivity.class));
            }
        });

    }
}
