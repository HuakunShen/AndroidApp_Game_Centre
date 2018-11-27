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
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.gameCentre.GameCentreInterfaceActivity;
import fall2018.csc2017.GameCentre.gameCentre.ScoreBoardActivity;

public class popScore extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_score);
        setupPopUpWindow();
        setupScoreboardButton();
//        setupBackToMenuButton();
    }

//    private void setupBackToMenuButton() {
//        Button backToMenuButton = findViewById(R.id.backButton);
//        backToMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplication(), GameCentreInterfaceActivity.class);
//
//                startActivity(intent);
//            }
//        });
//    }

    private void setupScoreboardButton() {
        Button scoreboardButton = findViewById(R.id.popScoreToScoreboard);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ScoreBoardActivity.class);
                intent.putExtra("scoreBoardType", "byGame");
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                intent.putExtra("gameType", getIntent().getStringExtra("gameType"));
                startActivity(intent);
            }
        });
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
    }


}
