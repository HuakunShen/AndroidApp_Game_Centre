package csc207project.gamecenter;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The class that manages login info for users.
 */
public class LoginInfo implements Serializable {

    /**
     * The collection of username and passwords.
     */
    private static HashMap<String, String> userInfo = new HashMap<String, String>(){
         {put(new String("admin"),new String("admin"));
        }
    };

//    private static HashMap<String, String> createMap()
//    {
//        HashMap<String,String> map= new HashMap<String,String>();
//        map.put("admin", "admin");
//        return map;
//    }

    /**
     * Returns whether the username is already registered.
     *
     * @param username The username that the client entered.
     * @return Whether the entered the user name is registered.
     */
    public static boolean IsValidUserName (String username) {
        boolean result = userInfo.containsKey(username);
        return result;
    }

    /**
     * Returns whether the entered username and password matches the one in the system.
     *
     * @param username The username entered by the user.
     * @param password The password that the user attempts to login with.
     * @return Whether the username and password is valid.
     */
    public static boolean Authenticate (String username, String password) {
        return userInfo.get(username).equals(password);

    }

    /**
     * Registers a new user into the system. Returns true if and only if the registration
     * is successful.
     *
     * @param username The username to register.
     * @param password The password to use.
     * @param repeat The password that the user entered for a second time.
     * @return If the registration is successful.
     */
    public static String Register (String username, String password, String repeat) {
        if (username.equals("")) {
            return "Empty Username";
        } else if (password.equals("") || repeat.equals("")) {
            return "Empty Password";
        } else if (userInfo.containsKey(username)) {
            return "Repeat Username!";
        } else if (!password.equals(repeat)) {
            return "Password entered do not match!";
        } else {
            userInfo.put(username, password);
            return "Registered!";
        }

    }
}
