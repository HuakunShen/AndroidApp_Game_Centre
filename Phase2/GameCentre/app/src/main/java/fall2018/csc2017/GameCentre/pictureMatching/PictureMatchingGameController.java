package fall2018.csc2017.GameCentre.pictureMatching;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

import static android.content.Context.MODE_PRIVATE;

public class PictureMatchingGameController {

    private final Context context;
    private SQLDatabase db;
    private User user;
    private String gameStateFile;
    private boolean gameRunning;
    private String tempGameStateFile;
    private MatchingBoardManager boardManager;
    private static final String GAME_NAME = "PictureMatch";

    private List<Button> tileButtons;
    private Resources resources;
    private String packageName;

    PictureMatchingGameController(Context context, User user){
        this.context = context;
        this.db = new SQLDatabase(context);
        this.user = user;
        this.packageName = context.getPackageName();
        this.resources = context.getResources();

    }

    public List<Button> getTileButtons() {
        return tileButtons;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void setBoardManager(MatchingBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public String getGameStateFile() {
        return gameStateFile;
    }

    public String getTempGameStateFile() {
        return tempGameStateFile;
    }

    public MatchingBoardManager getBoardManager() {
        return boardManager;
    }
    public MatchingBoard getBoard(){
        return boardManager.getBoard();
    }
    public User getUser() {
        return user;
    }
    String getUserFile(){
        return db.getUserFile(user.getUsername());
    }
    void setupFile(){
        if (!db.dataExists(user.getUsername(), GAME_NAME))
            db.addData(user.getUsername(), GAME_NAME);
        gameStateFile = db.getDataFile(user.getUsername(), GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;
    }
    void createTileButtons(){
        tileButtons = new ArrayList<>();
        for (int row = 0; row != boardManager.getBoard().getDifficulty(); row++) {
            for (int col = 0; col != boardManager.getBoard().getDifficulty(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(R.drawable.picturematching_tile_back);
                tileButtons.add(tmp);
            }
        }
    }

    void updateTileButtons(){
        MatchingBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / boardManager.getDifficulty();
            int col = nextPos % boardManager.getDifficulty();
            PictureTile currentTile = board.getTile(row,col);
            switch (currentTile.getState()){
                case PictureTile.FLIP:
                    String name = "pm_" + boardManager.getTheme() + "_" + Integer.toString(currentTile.getId());
                    int id = resources.getIdentifier(name, "drawable", packageName);
                    b.setBackgroundResource(id);
                    break;
                case PictureTile.COVERED:
                    b.setBackgroundResource(R.drawable.picturematching_tile_back);
                    break;
                case PictureTile.SOLVED:
                    b.setBackgroundResource(R.drawable.picturematching_tile_done);
                    break;
            }
            nextPos++;
        }
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
    Integer calculateScore(Long totalTimeTaken) {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        return 10000 / (timeInSec);
    }

    boolean updateScore(int score){
        boolean newRecord = user.updateScore(GAME_NAME, score);
        db.updateScore(user, GAME_NAME);
        return newRecord;
    }

    public void loadFromFile() {
        try {
            InputStream inputStream = context.openFileInput(tempGameStateFile);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (MatchingBoardManager) input.readObject();
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

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput(fileName, MODE_PRIVATE));
            if (fileName.equals(db.getUserFile(user.getUsername()))) {
                outputStream.writeObject(user);
            } else if (fileName.equals(gameStateFile) || fileName.equals(tempGameStateFile)) {
                outputStream.writeObject(boardManager);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public boolean boardSolved() {
        return boardManager.boardSolved();
    }
}
