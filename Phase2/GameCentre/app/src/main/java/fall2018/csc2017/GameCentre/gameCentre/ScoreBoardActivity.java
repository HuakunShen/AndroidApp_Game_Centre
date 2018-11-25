package fall2018.csc2017.GameCentre.gameCentre;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
    private String game_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        db = new DatabaseHandler(this);
        scoreboard = findViewById(R.id.tableView);
        username = getIntent().getStringExtra("user");
        type = getIntent().getStringExtra("scoreBoardType");
        if (type.equals("byGame"))
            game_type = getIntent().getStringExtra("gameType");
        if (type.equals("byUser"))
            dataList = db.getScoreByUser(username);
        else if (type.equals("byGame"))
            dataList = db.getScoreByGame(game_type);
        addTable();

    }

    private void addTable() {
        TableRow row;
        TextView text;
        setupTitle();

        for (int rowNum = 0; rowNum < dataList.size(); rowNum++) {
            row = new TableRow(this);
            for (int colNum = 0; colNum < dataList.get(rowNum).size(); colNum++) {
                text = new TextView(this);
                text.setText(dataList.get(rowNum).get(colNum));
                switch (colNum) {
                    case 0:
                        if (type.equals("byUser"))
                            text.setWidth(70);
                        else
                            text.setWidth(200);
                        break;
                    case 1:
                        if (type.equals("byUser"))
                            text.setWidth(100);
                        else
                            text.setWidth(300);
                        break;
                    case 2:
                        if (type.equals("byUser"))
                            text.setWidth(150);
                        else
                            text.setWidth(200);
                        break;
                    case 3:
                        text.setWidth(300);
                        break;
                }
                text.setTextColor(Color.parseColor("#FFFFFF"));
                text.setGravity(Gravity.CENTER);
                row.addView(text);
            }
            scoreboard.addView(row);
        }
    }

    private void setupTitle() {
        if (type.equals("byGame")) {
            TableLayout table = findViewById(R.id.tableView);
            TableRow row = findViewById(R.id.formatRow);
            row.setVisibility(View.GONE);
            TableRow newRow = new TableRow(this);

            TextView firstText = new TextView(this);
            firstText.setWidth(200);
            newRow.addView(firstText);
            TextView secondText = new TextView(this);
            secondText.setWidth(300);
            newRow.addView(secondText);

            TextView thirdText = new TextView(this);
            thirdText.setWidth(200);
            newRow.addView(thirdText);

            TextView fourthText = new TextView(this);
            fourthText.setWidth(300);
            newRow.addView(fourthText);
            table.addView(newRow);
        }

        TextView text;
        TableRow row = new TableRow(this);
        String[] titles = {};
        switch (type) {
            case "byUser":
                titles = new String[]{"Game", "Username", "Highest Score"};
                break;
            case "byGame":
                titles = new String[]{"Rank", "Game", "User", "Score"};
        }


        for (String title : titles) {
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

