package csc207project.gamecenter.Data;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreDatabase implements Serializable {

    /**
     * database array to store users and their corresponding scores.
     */
    private ArrayList<ArrayList<Object>> database;
    /**
     * The specific index for how to store data in arrayList.
     */
    private final Integer GAMETYPE_INDEX= 0;
    private final Integer USERNAME_INDEX = 1;
    private final Integer SCORE_INDEX = 2;

    /**
     * ScoreDatabase constructor.
     */
    public ScoreDatabase(){
        database = new ArrayList<>();
    }

    /**
     * Store data to ArrayList database.
     * @param username the name of current user.
     * @param gameType the type of the current game.
     * @param score the score of this user.
     */
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

    /**
     * if same user and game type exist, then update score if it get higher score.
     */
    public void updateScore(String username, String gameType, Integer score){
        int index = getIndex(username, gameType);
        Integer currentScore = getScore(username, gameType);
        if (currentScore < score){
            database.get(index).set(SCORE_INDEX, score);
        }
    }

    /**
     * get the score of the current user and this game type.
     * @param username current username.
     * @param gameType current game type.
     * @return the score of the current user who played this game.
     */
    private int getScore(String username, String gameType) {
        int score = getIndex(username, gameType) == -1 ? 0 :
                (Integer) database.get(getIndex(username, gameType)).get(SCORE_INDEX);
        return score;
    }

    /**
     * get the index of Array with username and game type.
     * @param username the current username.
     * @param gameType the current game type.
     * @return the index of the array with username and game type.
     */
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

    /**
     * @return the data of the score board.
     */
    public ArrayList<ArrayList<Object>> getDataForScoreBoard(){
        return this.database;
    }
}
