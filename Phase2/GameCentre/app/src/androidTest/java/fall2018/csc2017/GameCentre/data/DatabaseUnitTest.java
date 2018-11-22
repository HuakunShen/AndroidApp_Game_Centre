package fall2018.csc2017.GameCentre.data;;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Please Wipe data on the emulator before using this unit test, memory could result in error;
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseUnitTest {

    @Test
    public void userExists() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHandler db = new DatabaseHandler(context);
        User user = new User("admin", "admin");
        db.addUser(user);
        assertTrue(db.userExists("admin"));
        assertFalse(db.userExists("admin1"));

    }

    @Test
    public void dataExists() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHandler db = new DatabaseHandler(context);
        db.addData("admin", "game");
        assertTrue(db.dataExists("admin", "game"));
        assertFalse(db.dataExists("admin1", "game"));
        assertFalse(db.dataExists("admin", "game1"));
    }

    @Test
    public void addUser() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHandler db = new DatabaseHandler(context);
        User user = new User("admin", "admin");
        db.addUser(user);
        assertTrue(db.userExists("admin"));
        assertFalse(db.userExists("admin1"));

    }

    @Test
    public void addData() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHandler db = new DatabaseHandler(context);
        db.addData("admin", "game");
        assertTrue(db.dataExists("admin", "game"));
        assertFalse(db.dataExists("admin1", "game"));
        assertFalse(db.dataExists("admin", "game1"));
    }

    @Test
    public void getUserFile() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHandler db = new DatabaseHandler(context);
        User user = new User("admin", "admin");
        db.addUser(user);
        String output = db.getUserFile("admin");
        assertEquals("admin_user.ser", output);
    }

    @Test
    public void getScore() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHandler db = new DatabaseHandler(context);
        db.addData("user", "game");
        assertEquals(0, db.getScore("user", "game"));
        assertEquals(-1, db.getScore("user1", "game"));

    }

    @Test
    public void getDataFile() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHandler db = new DatabaseHandler(context);
        db.addData("user", "game");
        assertEquals("user_game_data.ser", db.getDataFile("user", "game"));
        assertEquals("File Does Not Exist!", db.getDataFile("user1", "game"));
    }

    @Test
    public void updateScore() {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHandler db = new DatabaseHandler(context);
        User user = new User("admin", "game");
        if (!db.dataExists("admin", "game"))
            db.addData("admin", "game");
        db.updateScore(user, "game");
        assertEquals(-1, db.getScore("admin", "game"));
        user.updateScore("game", 80);
        db.updateScore(user, "game");
        assertEquals(80, db.getScore("admin", "game"));


    }
}
