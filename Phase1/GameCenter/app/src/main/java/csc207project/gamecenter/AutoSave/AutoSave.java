package csc207project.gamecenter.AutoSave;
import java.util.Timer;
import java.util.TimerTask;

public interface AutoSave{

    void saveAfter();

    void saveToFile(String fileName);

}

