package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

import static android.graphics.Bitmap.createBitmap;

public class SlidingTilesGameController {
    private final Context context;
    private SQLDatabase db;
    private User user;
    private String gameStateFile;
    private int steps;
    private boolean gameRunning;
    private String tempGameStateFile;
    private SlidingTilesBoardManager boardManager;
    private static final String GAME_NAME = "SlidingTiles";
    private List<Button> tileButtons;
    private Bitmap backgroundImage;
    private Bitmap[] tileImages;
    private Resources resources;
    private String packageName;


    public String getUserFile() {
        return db.getUserFile(user.getUsername());
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    List<Button> createTileButtons() {
        tileButtons = new ArrayList<>();
        for (int row = 0; row != boardManager.getBoard().getDifficulty(); row++) {
            for (int col = 0; col != boardManager.getBoard().getDifficulty(); col++) {
                Button tmp = new Button(context);
                tileButtons.add(tmp);
            }
        }
        return tileButtons;
    }

    void updateTileButtons() {
        SlidingTilesBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / boardManager.getBoard().getDifficulty();
            int col = nextPos % boardManager.getBoard().getDifficulty();
            int tile_id = board.getTile(row, col);
            b.setBackground(new BitmapDrawable(context.getResources(), tileImages[tile_id - 1]));
            nextPos++;
        }
    }

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

    public void setupTileImagesAndBackground() {
        tileImages = new Bitmap[boardManager.getDifficulty() * boardManager.getDifficulty()];
        try {
            byte[] tmpImage = boardManager.getImageBackground();
            backgroundImage = BitmapFactory.decodeByteArray(tmpImage, 0, tmpImage.length);
            cutImageToTiles();
        }catch (Exception e) {
            convertNumberToTiles();
        }

    }

    private void cutImageToTiles() {
        int width = backgroundImage.getWidth();
        int height = backgroundImage.getHeight();

        int count = 0;
        for (int i = 0; i < boardManager.getDifficulty(); i++) {
            for (int j = 0; j < boardManager.getDifficulty(); j++) {
                tileImages[count] = createBitmap(backgroundImage, i * (width / boardManager.getDifficulty()),
                        j * (height / boardManager.getDifficulty()), width / boardManager.getDifficulty(), height / boardManager.getDifficulty(), null, false);
                count++;
            }
        }
        tileImages[boardManager.getDifficulty() * boardManager.getDifficulty() - 1]
                = BitmapFactory.decodeResource(resources, R.drawable.tile_empty);
    }

    private void convertNumberToTiles() {
        for (int i = 0; i < boardManager.getDifficulty() * boardManager.getDifficulty(); i++) {
            String name = "tile_"  + Integer.toString(i + 1);
            int numImage = resources.getIdentifier(name, "drawable", packageName);
            tileImages[i] = BitmapFactory.decodeResource(resources, numImage);
        }
        tileImages[boardManager.getDifficulty() * boardManager.getDifficulty() - 1]
                = BitmapFactory.decodeResource(resources, R.drawable.tile_empty);
    }

    public void setResourceAndPackage() {
        this.packageName = context.getApplicationContext().getPackageName();
        this.resources = context.getResources();
    }

    public void updateScore(int score) {
        user.updateScore(GAME_NAME, score);
        db.updateScore(user, GAME_NAME);
    }

    public boolean undo() {
        if (boardManager.undoAvailable()) {
            boardManager.move(boardManager.popUndo());
            return true;
        } else {
            return false;
        }
    }

    public void loadFromFile() {
        try {
            InputStream inputStream = context.openFileInput(tempGameStateFile);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (SlidingTilesBoardManager) input.readObject();
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


    public User getUser() {
        return user;
    }

    public boolean boardSolved() {
        return boardManager.boardSolved();
    }

    public SlidingTilesBoardManager getBoardManager() {
        return this.boardManager;
    }

    public SlidingTilesBoard getBoard() {
        return boardManager.getBoard();
    }
}
