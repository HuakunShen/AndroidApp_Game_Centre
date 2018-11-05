package csc207project.gamecenter.ScoreBoard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import csc207project.gamecenter.R;

public class ScoreBoardActivity extends AppCompatActivity {

    private LinearLayout scoreboardDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        scoreboardDisplay = findViewById(R.id.scoreboardDisplay);
        addText();
    }

    private void addText() {
        int N = 10;
//        TextView[] textViews = new TextView[N];
        for(int i = 0; i < 10; i++){
            TextView thisTextView = new TextView(this);
            thisTextView.setTextSize(30);
            thisTextView.setText("This is row # " + i);
            scoreboardDisplay.addView(thisTextView);
        }
    }
}
