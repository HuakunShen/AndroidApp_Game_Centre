package csc207project.gamecenter.Data;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreDatabase implements Serializable {

    private ArrayList<ArrayList<Object>> database;
    private final Integer GAMETYPE_INDEX= 0;
    private final Integer USERNAME_INDEX = 1;
    private final Integer SCORE_INDEX = 2;

    public ScoreDatabase(){
        database = new ArrayList<>();
    }

    public void storeData(String username, String gameType, Integer score){
        if(getIndex(username,gameType) == -1) {
            ArrayList<Object> temp = new ArrayList<>();
            temp.add(gameType);
            temp.add(username);
            temp.add(score);
            database.add(temp);
        }else{
            updateScore(username, gameType, score);
        }
    }
    public void updateScore(String username, String gameType, Integer score){
        int index = getIndex(username, gameType);
        Integer currentScore = getScore(username, gameType);
        if (currentScore < score){
            database.get(index).set(SCORE_INDEX, score);
        }
    }

    private int getScore(String username, String gameType) {
        int score = getIndex(username, gameType) == -1 ? 0 :
                (Integer) database.get(getIndex(username, gameType)).get(SCORE_INDEX);
        return score;
    }

    private int getIndex(String username, String gameType) {
        for(int row = 0; row < database.size(); row++){
            String user = (String) database.get(row).get(USERNAME_INDEX);
            Log.d("database", "username: " + user);
            String game = (String) database.get(row).get(GAMETYPE_INDEX);
            Log.d("database", "game: " + game);

            if(((String) database.get(row).get(USERNAME_INDEX)).equals(username) &&
                    database.get(row).get(GAMETYPE_INDEX).equals(gameType)){
                return row;
            }
        }
        return -1;
    }

    public ArrayList<ArrayList<Object>> getDataForScoreBoard(){
        return this.database;
    }
}
