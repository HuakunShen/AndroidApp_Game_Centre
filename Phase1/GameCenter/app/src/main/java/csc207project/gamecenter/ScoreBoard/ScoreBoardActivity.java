package csc207project.gamecenter.ScoreBoard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import csc207project.gamecenter.R;

public class ScoreBoardActivity extends AppCompatActivity {

    private String game;
    private String username;
    private String score;
    private ArrayList<String[]> data;
    private TableLayout scoreboardTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        scoreboardTable = findViewById(R.id.scoreboardTable);

//        game = "Sliding Tiles";
//        username = "admin";
//        score = "10";
        data = new ArrayList<String[]>();
        String[] array = {"Sliding Tiles", "Cool_Jason", "10"};
        data.add(array);



        addText();
    }

    private void addText() {
        TableRow row = new TableRow(this);
        for(int i = 0; i < 3; i++){
            TextView text = new TextView(this);
            if(i == 0){
                text.setText(data.get(0)[i]);
            }else if(i == 1){
                text.setText(data.get(0)[i]);
            }else{
                text.setText(data.get(0)[i]);
            }

//            text.setText(((Integer)i).toString());
            if(i == 0) {
                text.setWidth(70);
            }else if(i == 1){
                text.setWidth((100));
            }else{
                text.setWidth(150);
            }
            text.setGravity(Gravity.CENTER);
//            if(i == 0){
//                text.setGravity(Gravity.LEFT);
//            }
            row.addView(text);
        }
        scoreboardTable.addView(row);
    }
}
