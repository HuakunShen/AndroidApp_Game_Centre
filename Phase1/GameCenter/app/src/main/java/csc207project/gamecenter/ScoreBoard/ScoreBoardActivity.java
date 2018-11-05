package csc207project.gamecenter.ScoreBoard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import csc207project.gamecenter.R;

public class ScoreBoardActivity extends AppCompatActivity {

    private TableLayout scoreboardTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        scoreboardTable = findViewById(R.id.scoreboardTable);
        addText();

    }

    private void addText() {
        TableRow row = new TableRow(this);
        for(int i = 0; i < 4; i++){
            TextView text = new TextView(this);
            text.setText(((Integer) i).toString());
            if(i == 2) {
                text.setWidth(90);
            }else{
                text.setWidth((100));
            }
            text.setGravity(17);
            row.addView(text);
        }
        scoreboardTable.addView(row);
    }
}
