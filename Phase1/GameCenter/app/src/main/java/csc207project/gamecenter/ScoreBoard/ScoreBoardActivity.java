package csc207project.gamecenter.ScoreBoard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import csc207project.gamecenter.Data.ScoreDatabase;
import csc207project.gamecenter.Data.WQWDatabase;
import csc207project.gamecenter.GameCenter.GameCentre;
import csc207project.gamecenter.R;
import csc207project.gamecenter.SlidingTiles.GameActivity;

public class ScoreBoardActivity extends AppCompatActivity {

    private String game;
    private String username;
    private String score;
    private WQWDatabase database;
    private ArrayList<ArrayList<Object>> dataList;
    private TableLayout scoreboardTable;
    private ScoreDatabase scoreDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_score_board);
        scoreboardTable = findViewById(R.id.scoreboardTable);
        scoreDatabase = loadFromFile(GameActivity.SCORE_SAVE_FILE).equals(-1) ?
                new ScoreDatabase(): (ScoreDatabase) loadFromFile(GameActivity.SCORE_SAVE_FILE);
        dataList = scoreDatabase.getDataForScoreBoard();     // ArrayList<String[]>


        addText();
    }

    private void addText() {
        for (int rowNum = 0; rowNum < dataList.size(); rowNum++) {
            TableRow row = new TableRow(this);
            for (int colNum = 0; colNum < dataList.get(rowNum).size(); colNum++) {
                TextView text = new TextView(this);
                if(colNum != 2){
                    text.setText((String) dataList.get(rowNum).get(colNum));
                }else{
                    text.setText((dataList.get(rowNum).get(colNum)).toString());
                }
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
                text.setGravity(Gravity.CENTER);
                row.addView(text);
            }
            scoreboardTable.addView(row);
        }
    }


    private Object loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                Object file = input.readObject();
                inputStream.close();
                return file;
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        return -1;
    }
}
