package csc207project.gamecenter.Data;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * User marks system.
 */
public class GameInfo implements Serializable {
    /**
     * The database recording the steps and time used by
     * the user for a specific game.
     */
    private static ArrayList<Object[]> userData;

    /**
     * The database storing the user's marks for each
     * specific game.
     */
    private static ArrayList<Object[]> highestScore;

    private static final int USERNAME_INDEX = 0;
    private static final int GAME_TYPE_INDEX = 1;
    private static final int STEP_INDEX = 2;
    private static final int TIME_INDEX = 3;
    private static final int SCORE_INDEX = 2;


    /**
     * Initialize the GameInfo class.
     */
    public GameInfo(){
        userData = new ArrayList<Object[]>();
        highestScore = new ArrayList<Object[]>();
    }

    /**
     * Set the correct data object to the correct index in the database
     * for the game played by the user.
     */
    private void setData(ArrayList<Object[]> database, String username,
                         String gameType, int index, Object info) {
        for (Object[] data: database) {
            if (data[USERNAME_INDEX].equals(username) &&
                    data[GAME_TYPE_INDEX].equals(gameType)) {
                data[index] = info;
                break;
            }
        }
        Object[] newUser = new Object[4];
        newUser[USERNAME_INDEX] = username;
        newUser[GAME_TYPE_INDEX] = gameType;
        newUser[STEP_INDEX] = 0;
        newUser[TIME_INDEX] = 0L;
        newUser[index] = info;
        database.add(newUser);
    }

    /**
     * Get the data from the specified index in the database
     * for the game played by the user.
     */
    private Object getData(ArrayList<Object[]> database, String username,
                           String gameType, int index) {
        for (Object[] data: database) {
            if (data[USERNAME_INDEX].equals(username) &&
                    data[GAME_TYPE_INDEX].equals(gameType)) {
                return data[index];
            }
        }
        return null;
    }

    /**
     * Set the steps user performed in the game.
     */
    public void setStep(String username, String gameType, int step){
        setData(userData, username, gameType, STEP_INDEX, step);
    }

    /**
     * Get the steps user performed in the game.
     */
    public int getStep(String username, String gameType){
        return (int) getData(userData, username, gameType, STEP_INDEX);
    }

    /**
     * Set the time user (has) used playing the game.
     */
    public void setTime(String username, String gameType, long time){
        setData(userData, username, gameType, TIME_INDEX, time);
    }

    /**
     * Get the time user (has) used playing the game.
     */
    public long getTime(String username, String gameType){
        return (long) getData(userData, username, gameType, TIME_INDEX);
    }

    /**
     * Set the score got for the game played by the user
     */
    public void setScore(String username, String gameType, int score){
        setData(highestScore, username, gameType, SCORE_INDEX, score);
    }

    /**
     * Get the score for the game played by the user.
     */
    public int getScore(String username, String gameType) {
        return (int) getData(highestScore, username, gameType, SCORE_INDEX);
    }

    /**
     * Returns whether the user broke his/her own record.
     */
    public boolean isHigherThanSelf(String username, String gameType, int score){
        int currentScore = !((Integer) getScore(username, gameType)).equals(null) ?
                getScore(username, gameType) : 0;
        return currentScore < score;
    }

    /**
     * Returns whether the user achieved the world record.
     */
    public boolean isHighestInGame(String username, String gameType){
        int currentScore = !((Integer) getScore(username, gameType)).equals(null) ?
                getScore(username, gameType) : 0;
        int maximumScore = 0;
        for (Object[] data: highestScore) {
            if (data[GAME_TYPE_INDEX].equals(gameType) &&
                    (int) data[SCORE_INDEX] > maximumScore) {
                maximumScore = (int) data[SCORE_INDEX];
            }
        }
        return currentScore >= maximumScore;
    }
}
