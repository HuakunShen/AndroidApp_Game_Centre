package fall2018.csc2017.GameCentre.util;
// Source: https://www.youtube.com/watch?v=fn5OlqQuOCk

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.TextView;

import fall2018.csc2017.GameCentre.R;

public class popScore extends Activity {
    private int score;
    private TextView scoreDisplay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_score);
        setupPopUpWindow();

    }

    private void setupPopUpWindow() {
        score = getIntent().getIntExtra("score",0);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(((int) (width * .8)), (int) (height * .3));
        scoreDisplay = findViewById(R.id.scoreDisplay);
        scoreDisplay.setText(String.valueOf(score));
    }
}
