package csc207project.gamecenter.AutoSave;
import java.util.Timer;
import java.util.TimerTask;

public interface AutoSave {

    int getTimeInterval();

//    default void setTimer(){
//        Timer t = new Timer("AutoSaveTimer");
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                saveToFile();
//            }
//        },getTimeInterval(), getTimeInterval());
//    }

    void saveToFile();

}
