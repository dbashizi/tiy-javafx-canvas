package sample;

/**
 * Created by Sonjtrez on 5/16/2016.
 */
public class User {
    private String userName;
    private String fullName;
    private int userID;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public User(String userName, String fullName, int userID) {
        this.userID = userID;
        this.userName = userName;

        this.fullName = fullName;
    }
}
