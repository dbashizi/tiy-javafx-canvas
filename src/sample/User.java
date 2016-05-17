package sample;

/**
 * Created by Godfather on 5/16/2016.
 */
public class User {

    public String userName;
    public String userFullName;
    public int id;

    public User(int id, String userFullName, String userName) {
        this.id = id;
        this.userFullName = userFullName;
        this.userName = userName;
    }

//    public User(String text) {
//        this.text = text;
//        this.isDone = false;
//    }

    public User() {
    }

//    @Override
//    public String toString() {
//        if (isDone) {
//            return text + " (done)";
//
//
//        // A one-line version of the logic above:
//        // return text + (isDone ? " (done)" : "");
//    }
}
