package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Context;
import android.widget.Button;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.util.BoardManagerForBoardGames;

public class SlidingTilesGameController {
    private final Context context;
    private SQLDatabase db;
    private User user;
    private String gameStateFile;
    private int steps;

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    private boolean gameRunning;


    private String tempGameStateFile;
    private SlidingTilesBoardManager boardManager;
    private static final String GAME_NAME = "SlidingTiles";
    private ArrayList<Button> tileButtons;

    public SlidingTilesGameController(Context context, User user) {
        this.context = context;
        this.db = new SQLDatabase(context);
        this.user = user;

    }

    void setupFile() {
        if (!db.dataExists(user.getUsername(), GAME_NAME))
            db.addData(user.getUsername(), GAME_NAME);
        gameStateFile = db.getDataFile(user.getUsername(), GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;



    }




    String convertTime(long time) {
        Integer hour = (int) (time / 3600000);
        Integer min = (int) ((time % 3600000) / 60000);
        Integer sec = (int) ((time % 3600000 % 60000) / 1000);
        String hourStr = hour.toString();
        String minStr = min.toString();
        String secStr = sec.toString();
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        if (min < 10) {
            minStr = "0" + minStr;
        }
        if (sec < 10) {
            secStr = "0" + secStr;
        }
        return hourStr + ":" + minStr + ":" + secStr;
    }



    public Integer calculateScore(Long totalTimeTaken) {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        Integer score = new Integer(10000 / (steps + timeInSec));
        return score;
    }

    public void setBoardManager(SlidingTilesBoardManager boardManager) {
        this.boardManager = boardManager;
    }


    public int getSteps() {
        return steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
        this.boardManager.setStepsTaken(steps);
    }


    public void setupSteps() {
        this.steps = boardManager.getStepsTaken();
    }


    public String getGameStateFile() {
        return gameStateFile;
    }

    public String getTempGameStateFile() {
        return tempGameStateFile;
    }
}
