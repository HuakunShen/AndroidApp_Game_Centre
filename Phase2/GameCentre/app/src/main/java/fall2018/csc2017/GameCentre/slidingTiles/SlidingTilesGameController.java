package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Context;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

public class SlidingTilesGameController {
    private final Context context;
    private SQLDatabase db;
    private User user;
    private String gameStateFile;
    private String tempGameStateFile;
    private SlidingTilesBoardManager boardManager;
    private static final String GAME_NAME = "SlidingTiles";
    private ArrayList<Button> tileButtons;

    public SlidingTilesGameController(Context context, SQLDatabase db, User user,
                                      SlidingTilesBoardManager boardManager,
                                      String gameStateFile, String tempGameStateFile) {
        this.context = context;
        this.db = db;
        this.user = user;
        this.gameStateFile = gameStateFile;
        this.tempGameStateFile = tempGameStateFile;
        this.boardManager = boardManager;

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




    public Integer calculateScore(int steps, Long totalTimeTaken) {
        int step = steps;
        int timeInSec = totalTimeTaken.intValue() / 1000;
        Integer score = new Integer(10000 / (step + timeInSec));
        return score;
    }
}
