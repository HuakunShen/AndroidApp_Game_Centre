package fall2018.csc2017.GameCentre.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    private User user;
    @Before
    public void setUp() throws Exception {
        user = new User("username", "password");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkPassword() {
        boolean result = user.checkPassword("password");
        assertTrue(result);
        user.setPassword("newPassword");
        result = user.checkPassword("password");
        assertFalse(result);
        result = user.checkPassword("newPassword");
        assertTrue(result);
    }

    @Test
    public void updateScore() {
    }

    @Test
    public void getFile() {
    }

    @Test
    public void getScore() {
    }

    @Test
    public void getScoreboardData() {
    }
}