package csc207project.gamecenter.Data;

import java.util.ArrayList;
import java.util.HashMap;

public class GameInfo {
    private ArrayList<Object[]> dataBase;
    private HashMap<String, Object[]> highestScore;

    private static final int USERNAME_INDEX = 0;
    private static final int GAMETYPE_INDEX = 1;
    private static final int STEP_INDEX = 2;
    private static final int TIME_INDEX = 3;
    private static final int SCORE_INDEX = 4;



    public GameInfo(){
        this.dataBase = new ArrayList<Object[]>();
        this.highestScore = new HashMap<>();
    }

    public void addGame(String gameType){
        if(!this.highestScore.containsKey(gameType)){
            this.highestScore.put(gameType, new Object[2]);
        }
    }

    public void addGameInfo(String username, String gameType){
        Object[] tuple = {username, gameType, null, null, null};
        if(!gameInfoExist(username, gameType)){
            dataBase.add(tuple);
        }
    }

    public int getIndex(String username, String gameType){
        int index = -1;
        for (int i = 0; i < this.dataBase.size(); i++) {
            if (this.dataBase.get(i)[USERNAME_INDEX].equals(username) &&
                    dataBase.get(i)[GAMETYPE_INDEX].equals(gameType)) {
                index = i;
            }
        }
        return index;
    }

    public void setStep(String username, String gameType, int step){
        this.dataBase.get(getIndex(username, gameType))[STEP_INDEX] = step;
    }

    public int getStep(String username, String gameType){
        return (int) this.dataBase.get(getIndex(username, gameType))[STEP_INDEX];
    }

    public void setTime(String username, String gameType, long time){
        this.dataBase.get(getIndex(username, gameType))[TIME_INDEX] = time;
    }

    public long getTime(String username, String gameType){
        return (long) this.dataBase.get(getIndex(username, gameType))[TIME_INDEX];
    }

    public void setScore(String username, String gameType, int score){
        this.dataBase.get(getIndex(username, gameType))[SCORE_INDEX] = score;
    }

    public int getScore(String username, String gameType) {
        return (int) this.dataBase.get(getIndex(username, gameType))[SCORE_INDEX];
    }

    public boolean isHigherThanSelf(String username, String gameType, int score){
        if(getScore(username, gameType) < score){
            return true;
        }
        return false;
    }

    public boolean isHighestInGame(String gameType, int score){
        boolean result = false;
        for(int )
    }

    public boolean gameInfoExist(String username, String gameType){
        for (int i = 0; i < dataBase.size(); i++){
            if ((dataBase.get(i)[USERNAME_INDEX].equals(username) &&
                    (dataBase.get(i)[GAMETYPE_INDEX].equals(gameType)))){
                return true;
            }
        }
        return false;
    }

}
