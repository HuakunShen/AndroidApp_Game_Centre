package csc207project.gamecenter.Data;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * The Wei Qing Wang database.
 */
public class WQWDatabase implements Serializable {
    /**
     * The database recording the steps and time used by
     * the user for a specific game.
     */
    private ArrayList<ArrayList<Object>> userData;

    /**
     * The database storing the user's marks for each
     * specific game.
     */
    private ArrayList<ArrayList<Object>> highestScore;

    private static final int USERNAME_INDEX = 0;
    private static final int GAME_TYPE_INDEX = 1;
    private static final int STEP_INDEX = 2;
    private static final int TIME_INDEX = 3;
    private static final int SCORE_INDEX = 2;


    /**
     * Initialize the WQWDatabase class.
     */
    public WQWDatabase() {
        userData = new ArrayList<ArrayList<Object>>();
        highestScore = new ArrayList<ArrayList<Object>>();
    }

    /**
     * Set the correct data object to the correct index in the database
     * for the game played by the user.
     */
    private void setData(ArrayList<ArrayList<Object>> database, String username,
                         String gameType, int index, Object info) {
        boolean userExists = false;
        for (int i = 0; i < database.size(); i++) {
            ArrayList<Object> data = database.get(i);
            if (data.get(USERNAME_INDEX).equals(username) &&
                    data.get(GAME_TYPE_INDEX).equals(gameType)) {
                database.get(i).set(index, info);
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            ArrayList<Object> newUser = new ArrayList<>();
            newUser.add(username);
            newUser.add(gameType);
            newUser.add(0);
            newUser.add(0L);
            newUser.set(index, info);
            database.add(newUser);
        }

    }

    /**
     * Get the data from the specified index in the database
     * for the game played by the user.
     */
    private Object getData(ArrayList<ArrayList<Object>> database, String username,
                                  String gameType, int index) {
        for (int i = 0; i < database.size(); i++) {
            ArrayList<Object> data = database.get(i);
            if (data.get(USERNAME_INDEX).equals(username) &&
                    data.get(GAME_TYPE_INDEX).equals(gameType)) {
                return data.get(index);
            }
        }
        return -1;
    }

    /**
     * Set the steps user performed in the game.
     */
    public void setStep(String username, String gameType, Integer step) {
        setData(userData, username, gameType, STEP_INDEX, (Integer) step);
    }

    /**
     * Get the steps user performed in the game.
     */
    public int getStep(String username, String gameType) {
        return (getData(userData, username, gameType, STEP_INDEX)).equals(-1) ?
                0 : (Integer) getData(userData, username, gameType, STEP_INDEX);
    }

    /**
     * Set the time user (has) used playing the game.
     */
    public void setTime(String username, String gameType, long time) {
        setData(userData, username, gameType, TIME_INDEX, new Long(time));
    }

    /**
     * Get the time user (has) used playing the game.
     */
    public long getTime(String username, String gameType) {
        return getData(userData, username, gameType, TIME_INDEX).equals(-1) ?
                0L : ((Long) getData(userData, username, gameType, TIME_INDEX)).longValue();
    }

    /**
     * Set the score got for the game played by the user
     */
    public void setScore(String username, String gameType, int score) {
        if (isHigherThanSelf(username, gameType, score)) {
            setData(highestScore, username, gameType, SCORE_INDEX, score);
        }
    }

    /**
     * Get the score for the game played by the user.
     */
    public int getScore(String username, String gameType) {
        return (getData(highestScore, username, gameType, GAME_TYPE_INDEX)).equals(-1)?
                0 : (int) getData(highestScore, username, gameType, GAME_TYPE_INDEX);
    }

    /**
     * Returns whether the user broke his/her own record.
     */
    private boolean isHigherThanSelf(String username, String gameType, int score) {
        return getScore(username, gameType) < score;
    }

    /**
     * Returns whether the user achieved the world record.
     */
    public boolean isHighestInGame(String username, String gameType) {
        int maximumScore = 0;
        for (ArrayList<Object> data : highestScore) {
            if (data.get(GAME_TYPE_INDEX).equals(gameType) &&
                    (int) data.get(SCORE_INDEX) > maximumScore) {
                maximumScore = (int) data.get(SCORE_INDEX);
            }
        }
        return getScore(username, gameType) >= maximumScore;
    }
}
