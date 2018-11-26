package fall2018.csc2017.GameCentre.gameCentre;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.GameCentre.data.DatabaseHandler;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.pictureMatching.MatchingBoardManager;

public class ScoreBoardActivity extends AppCompatActivity {

    private String username;
    private List<List<String>> dataList;
    private TableLayout scoreboard;
    private DatabaseHandler db;
    private String type;
    private String game_type;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        db = new DatabaseHandler(this);
        scoreboard = findViewById(R.id.tableView);
        setupUser();

        if (type.equals("byGame"))
            game_type = getIntent().getStringExtra("gameType");
        if (type.equals("byUser"))
            dataList = user.getScoreboardData();
        else if (type.equals("byGame"))
            dataList = db.getScoreByGame(game_type);
        addTable();

    }

    private void setupUser() {
        username = getIntent().getStringExtra("user");
        type = getIntent().getStringExtra("scoreBoardType");
        String user_file = db.getUserFile(username);
        loadFromFile(user_file);
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


    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                user = (User) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

}

