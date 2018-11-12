package fall2018.csc2017.GameCentre.GameCentre;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.Data.DatabaseHandler;
import fall2018.csc2017.GameCentre.R;

public class ScoreBoardActivity extends AppCompatActivity {

    private String username;
    private ArrayList<ArrayList<String>> dataList;
    private TableLayout scoreboard;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        db = new DatabaseHandler(this);
        scoreboard = findViewById(R.id.tableView);
        username = getIntent().getStringExtra("user");
        dataList = db.getAllScore(username);
        addText();
    }

    private void addText() {
        for (int rowNum = 0; rowNum < dataList.size(); rowNum++) {
            TableRow row = new TableRow(this);
            for (int colNum = 0; colNum < dataList.get(rowNum).size(); colNum++) {
                TextView text = new TextView(this);
                text.setText(dataList.get(rowNum).get(colNum));
                switch (colNum) {
                    case 0:
                        text.setWidth(70);
                        break;
                    case 1:
                        text.setWidth(100);
                        break;
                    case 2:
                        text.setWidth(150);
                        break;
                }
                text.setTextColor(Color.parseColor("#FFFFFF"));
                text.setGravity(Gravity.CENTER);
                row.addView(text);
            }
            scoreboard.addView(row);
        }
    }
}

