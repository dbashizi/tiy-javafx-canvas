package sample;

/**
 * Created by willi on 5/16/2016.
 */
public class DataUser {
  public String userName;
    public String userFullName;
    public int id;

    public DataUser(int id, String userFullName, String userName) {
        this.id = id;
        this.userFullName = userFullName;
        this.userName = userName;
    }

    public DataUser() {

    }


}
