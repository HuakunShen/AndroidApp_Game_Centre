package csc207project.gamecenter.Data;

import java.io.Serializable;

import java.lang.Integer;
import java.lang.Long;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * The Wei Qing Wang database.
 */
public class WQWDatabase implements Serializable {
    /**
     * The database recording user's game's ID.
     */
    private HashMap<String, HashMap<String, Integer>> userID;

    /**
     * The database recording user's steps
     */
    private HashMap<Integer, Integer> stepStack;

    /**
     * The database recording user's runtime.
     */
    private HashMap<Integer, Long> timeStack;

    /**
     * The database storing the user's marks for each
     * specific game.
     */
    private HashMap<Integer, Integer> highestScore;

    private final int GAME_TYPE_INDEX = 1;
    private final int STEP_INDEX = 2;
    private final int TIME_INDEX = 3;
    private final int SCORE_INDEX = 4;
    private int id;

    /**
     * Initialize the WQWDatabase class.
     */
    public WQWDatabase() {
        this.userID = new HashMap<String, HashMap<String, Integer>>();
        this.stepStack = new HashMap<Integer, Integer>();
        this.timeStack = new HashMap<Integer, Long>();
        this.highestScore = new HashMap<Integer, Integer>();
        this.id = timeStack.keySet().size();
    }

    /**
     * Set the correct data object to the correct index in the database
     * for the game played by the user.
     */
    private void setData(String username, String gameType, int index, Object info) {
        boolean userExists = userID.containsKey(username) &&
                userID.get(username).containsKey(gameType);
        if (userExists) {
           Integer user = userID.get(username).get(gameType);
           switch (index) {
               case STEP_INDEX : stepStack.put(user, (Integer) info);
                    break;
               case TIME_INDEX : timeStack.put(user, (Long) info);
                    break;
               case SCORE_INDEX : highestScore.put(user, (Integer) info);
                    break;
           }
        } else if (userID.containsKey(username)) {
            userID.get(username).put(gameType, id++);
            stepStack.put(id - 1, (Integer) 0);
            timeStack.put(id - 1, new Long(0));
            highestScore.put(id - 1, (Integer) 0);
            setData(username, gameType, index, info);
        } else {
            userID.put(username, new HashMap<String, Integer>());
            setData(username, gameType, index, info);
        }

    }

    /**
     * Get the data from the specified index in the database
     * for the game played by the user.
     */
    private Object getData(String username, String gameType, int index) {
        if (userID.containsKey(username) && userID.get(username).containsKey(gameType)) {
            int user = userID.get(username).get(gameType);
            switch (index) {
                case STEP_INDEX :
                    return stepStack.containsKey(user) ? stepStack.get(user) : (Integer) 0;
                case TIME_INDEX :
                    return timeStack.containsKey(user) ? timeStack.get(user) : new Long(0);
                case SCORE_INDEX :
                    return highestScore.containsKey(user) ? highestScore.get(user) : (Integer)0;
            }
            return -1;
        } else {
            return -1;
        }
    }

    /**
     * Set the steps user performed in the game.
     */
    public void setStep(String username, String gameType, Integer step) {
        setData(username, gameType, STEP_INDEX, step);
    }

    /**
     * Get the steps user performed in the game.
     */
    public Integer getStep(String username, String gameType) {
        return getData(username, gameType, STEP_INDEX).equals(-1) ?
                0 : (Integer) getData(username, gameType, STEP_INDEX);
    }

    /**
     * Set the time user (has) used playing the game.
     */
    public void setTime(String username, String gameType, long time) {
        setData(username, gameType, TIME_INDEX, time);
    }

    /**
     * Get the time user (has) used playing the game.
     */
    public long getTime(String username, String gameType) {
        return getData(username, gameType, TIME_INDEX).equals(-1) ?
                0L: (long) getData(username, gameType, TIME_INDEX);
    }

    /**
     * Set the score got for the game played by the user
     */
    public void setScore(String username, String gameType, int score) {
        if (isHigherThanSelf(username, gameType, score)) {
            setData(username, gameType, SCORE_INDEX, score);
        }
    }

    /**
     * Get the score for the game played by the user.
     */
    public int getScore(String username, String gameType) {
        return getData(username, gameType, GAME_TYPE_INDEX).equals(-1) ?
                0 : (int) getData(username, gameType, GAME_TYPE_INDEX);
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
        return Collections.max(highestScore.values()) == getScore(username, gameType);
    }



    public ArrayList<Object[]> getDataForScoreBoard() {
        ArrayList<Object[]> result = new ArrayList<Object[]>();
        for (String i : userID.keySet()) {
            for (String j: userID.get(i).keySet()) {
                Object[] cache = new Object[3];
                cache[0] = i;
                cache[1] = j;
                cache[2] = getScore(i, j);
                result.add(cache);
            }
        }
        return result;
    }

}
