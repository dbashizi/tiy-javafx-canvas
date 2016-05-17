package sample;

/**
 * Created by Godfather on 5/16/2016.
 */
public class DatabaseRunner {

    public static void main(String[] args) throws Exception {
        System.out.println("main()");
        ToDoDatabase db = new ToDoDatabase();
        db.init(); // starts the db and starts a background thread
    }
}
