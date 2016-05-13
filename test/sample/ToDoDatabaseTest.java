package sample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by danarchy on 5/12/16.
 */
public class ToDoDatabaseTest {

    ToDoDatabase todoDatabase;
//    Connection conn;

    @Before
    public void setUp() throws Exception {
        todoDatabase = new ToDoDatabase();
        todoDatabase.init();
//        conn = DriverManager.getConnection("jdbc:h2:./main");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInsertToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";

        todoDatabase.insertToDo(conn, todoText);

        // make sure we can retrieve the todo we just created
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos where text = ?");
        stmt.setString(1, todoText);
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        // count the records in results to make sure we get what we expected
        int numResults = 0;
        while (results.next()) {
            numResults++;
        }

        assertEquals(1, numResults);

        todoDatabase.deleteToDo(conn, todoText);

        // make sure there are no more records for our test todo
        results = stmt.executeQuery();
        numResults = 0;
        while (results.next()) {
            numResults++;
        }
        assertEquals(0, numResults);
    }

    @Test
    public void testInit() throws Exception {
        // test to make sure we can access the new database
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement todoQuery = conn.prepareStatement("SELECT * FROM todos");
        ResultSet results = todoQuery.executeQuery();
        assertNotNull(results);
    }

    @Test
    public void testSelectAllToDos() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String firstToDoText = "UnitTest-ToDo1";
        String secondToDoText = "UnitTest-ToDo2";

        todoDatabase.insertToDo(conn, firstToDoText);
        todoDatabase.insertToDo(conn, secondToDoText);

        ArrayList<ToDoItem> todos = todoDatabase.selectToDos(conn);
        System.out.println("Found " + todos.size() + " todos in the database");

        assertTrue("There should be at least 2 todos in the database (there are " +
                todos.size() + ")", todos.size() > 1);

        todoDatabase.deleteToDo(conn, firstToDoText);
        todoDatabase.deleteToDo(conn, secondToDoText);
    }

    @Test
    public void testToggleToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "Test the toggle method";
        todoDatabase.insertToDo(conn, todoText);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text= ?");
        stmt.setString(1, todoText);
        ResultSet results = stmt.executeQuery();
        results.next();
        boolean beforeToggleModel = results.getBoolean("is_done");
        todoDatabase.toggleToDo(conn, results.getInt("id"));
        results = stmt.executeQuery(); //this and the following line pull up our new, updated results!
        results.next(); //loads in our results, updated from the toggle!
        boolean afterToggleModel = results.getBoolean("is_done");
        assertNotEquals(beforeToggleModel, afterToggleModel);

    }
}