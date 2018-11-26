package fall2018.csc2017.GameCentre.gameCentre;

import android.graphics.Color;
import android.support.annotation.NonNull;
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
import java.util.List;

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.User;

public class ScoreBoardActivity extends AppCompatActivity {

    private List<List<String>> dataList;
    private TableLayout scoreboard;
    private SQLDatabase db;
    private String type;
    private String game_type;
    private User user;
    private final String[] byUserTitle = new String[]{"Game", "Username", "Highest Score"};
    private final String[] byGameTitle = new String[]{"Rank", "Game", "User", "Score"};
    private final String byUser = "byUser";
    private final String byGame = "byGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        db = new SQLDatabase(this);
        scoreboard = findViewById(R.id.tableView);
        setupUser();
        setupData();
        addTable();
    }


    private void setupUser() {
        String username = getIntent().getStringExtra("user");
        type = getIntent().getStringExtra("scoreBoardType");
        String user_file = db.getUserFile(username);
        loadFromFile(user_file);
    }

    private void setupData() {
        if (type.equals(byGame))
            game_type = getIntent().getStringExtra("gameType");
        if (type.equals(byUser))
            dataList = user.getScoreboardData();
        else
            dataList = db.getScoreByGame(game_type);
    }


    private void addTable() {
        setupTitle();
        TableRow row;
        TextView text;
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
        initializeColumnsWidth();
        String[] titles = setupTitleContent();
        addConfigTitles(titles);
        addLine();
    }

    private String[] setupTitleContent() {
        String[] titles;
        if (type.equals(byUser))
            titles = byUserTitle;
        else
            titles = byGameTitle;
        return titles;
    }

    private void addLine() {
        TableRow row = new TableRow(this);
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

    @NonNull
    private void addConfigTitles(String[] titles) {
        TextView text;
        TableRow row = new TableRow(this);
        for (String title : titles) {
            text = new TextView(this);
            text.setText(title);
            text.setTextColor(Color.parseColor("#FFFFFF"));
            text.setGravity(Gravity.CENTER);
            text.setTextSize(18);
            row.addView(text);
        }
        scoreboard.addView(row);
    }

    private void initializeColumnsWidth() {
        TableRow newRow = new TableRow(this);
        int numCol = type.equals(byGame) ? 4 : 3;
        int colWidth = type.equals(byUser) ? 300 : 250;
        for (int i = 0; i < numCol; i++) {
            TextView tmp = new TextView(this);
            tmp.setWidth(colWidth);
            newRow.addView(tmp);
        }
        scoreboard.addView(newRow);
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

