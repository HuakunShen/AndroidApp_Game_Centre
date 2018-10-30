package csc207project.gamecenter.AutoSave;
import java.util.Timer;
import java.util.TimerTask;

public interface AutoSave{
    int timeInterval = 3;

//    default int getTimeInterval(){return timeInterval;}

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
