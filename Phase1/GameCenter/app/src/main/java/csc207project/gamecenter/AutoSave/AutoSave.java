package csc207project.gamecenter.AutoSave;

import java.util.Timer;
import java.util.TimerTask;

public interface AutoSave {

    Object loadFromFile(String fileName);
    void saveToFile(String fileName, Object file);


}