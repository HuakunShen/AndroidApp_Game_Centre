package csc207project.gamecenter.AutoSave;

import java.util.Timer;
import java.util.TimerTask;

public interface AutoSave {
    String TEMP_SAVE_FILENAME = null;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            AutoSave.saveToFile(TEMP_SAVE_FILENAME);
        }
    };

    timer.schedule(task, 0, 5000);

    void saveToFile(String fileName);

}