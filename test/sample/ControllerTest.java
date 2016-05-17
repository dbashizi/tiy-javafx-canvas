package sample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

/**
 * Created by willi on 5/13/2016.
 */
public class ControllerTest {



    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void saveToDoList() throws Exception {

    }

    @Test
    public void addItem() throws Exception {

    }

    @Test
    public void removeItem() throws Exception {

    }
    @Test
    public void toggleItem() throws Exception {
        ToDoDatabase todoDatabase = new ToDoDatabase();

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";
        // adding a call to insertUser, so we have a user to add todos for
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        int userID = todoDatabase.insertUser(conn, username, fullName);

        todoDatabase.insertToDo(conn, todoText, userID);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text= 'UnitTest-ToDo'");
        ResultSet results = stmt.executeQuery();
        results.next();
        System.out.println("is_done:" + results.getString("is_done"));
        todoDatabase.toggleToDo(conn,results.getInt("id"));
        System.out.println("is_done:" + results.getString("is_done"));
        boolean expectedValue = !results.getBoolean("is_done");
        assertNotEquals(expectedValue, results.getBoolean("is_done"));
        //test if we can insert items into the database



    }

    @Test
    public void saveList() throws Exception {

    }

    @Test
    public void retrieveList() throws Exception {

    }
}