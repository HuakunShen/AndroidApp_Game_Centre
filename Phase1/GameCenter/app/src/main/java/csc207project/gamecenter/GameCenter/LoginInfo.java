package csc207project.gamecenter.GameCenter;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The class that manages login info for users.
 */
public class LoginInfo implements Serializable {


    private String admin = new String("admin");

    /**
     * The collection of username and passwords.
     */
    private HashMap<String, String> userInfo;

//    private static HashMap<String, String> createMap()
//    {
//        HashMap<String,String> map= new HashMap<String,String>();
//        map.put("admin", "admin");
//        return map;
//    }

    public LoginInfo(){
        this.userInfo = new HashMap<String, String>();
        this.userInfo.put("admin", "admin");
    }



    /**
     * Returns whether the username is already registered.
     *
     * @param username The username that the client entered.
     * @return Whether the entered the user name is registered.
     */
    public boolean isValidUserName(String username) {
        boolean result = !userInfo.containsKey(username) && !username.equals("");
        return result;
    }


    public boolean checkUsername(String username){
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
    public boolean Authenticate (String username, String password) {
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
    public boolean Register (String username, String password, String repeat) {

        if(password.equals(repeat) && isValidUserName(username)){
            userInfo.put(username, password);
            return true;
        }else{
            return false;
        }

    }

    public void resetPassword(String userName, String password){
        userInfo.put(userName, password);
    }
}
