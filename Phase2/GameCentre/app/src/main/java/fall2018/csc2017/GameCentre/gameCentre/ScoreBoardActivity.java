package fall2018.csc2017.GameCentre.gameCentre;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.data.DatabaseHandler;
import fall2018.csc2017.GameCentre.R;

public class ScoreBoardActivity extends AppCompatActivity {

    private String username;
    private ArrayList<ArrayList<String>> dataList;
    private TableLayout scoreboard;
    private DatabaseHandler db;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        db = new DatabaseHandler(this);
        scoreboard = findViewById(R.id.tableView);
        username = getIntent().getStringExtra("user");
        type = getIntent().getStringExtra("scoreBoardType");
        displayBoard();
    }

    private void displayBoard() {
        if (type.equals("byUser")) {
            dataList = db.getAllScore(username);
            addByUserTable();
        } else if (type.equals("byGame")) {

            addByGameTable();
        }

    }

    private void addByGameTable() {

    }


    private void addByUserTable() {
        TableRow row = new TableRow(this);
        TextView text;
        setUpTitleForByUserType(row);

        for (int rowNum = 0; rowNum < dataList.size(); rowNum++) {
            row = new TableRow(this);
            for (int colNum = 0; colNum < dataList.get(rowNum).size(); colNum++) {
                text = new TextView(this);
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

    private void setUpTitleForByUserType(TableRow row) {
        TextView text;
        String[] titles = {"Game", "Username", "Highest Score"};
        for (String title: titles){
            text = new TextView(this);
            text.setText(title);
            text.setTextColor(Color.parseColor("#FFFFFF"));
            text.setGravity(Gravity.CENTER);
            text.setTextSize(18);
            row.addView(text);
        }
        scoreboard.addView(row);
        row = new TableRow(this);
        row.setMinimumHeight(5);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 10);
        row.setLayoutParams(params);
        row.setBackgroundColor(ContextCompat.getColor(this, R.color.scoreBoardTileLine));
        scoreboard.addView(row);
    }
}

