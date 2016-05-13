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
 * Created by Godfather on 5/12/2016.
 */
public class ToDoDatabaseTest {

    ToDoDatabase todoDatabase;

    @Before
    public void setUp() throws Exception {

        todoDatabase = new ToDoDatabase();
        todoDatabase.init();
    }

    @After
    public void tearDown() throws Exception {

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
    public void testInsertToDo() throws Exception {

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        // test if we can insert items into the database
        String todoText = "UnitTest-ToDoxxx";
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
            String todosID = results.getString("id");
            String todosText = results.getString("text");
            boolean todosisdone = results.getBoolean("is_done");
            System.out.println(todosID + "::" + todosText + "->" + todosisdone);

        }

        assertEquals(1, numResults);

        todoDatabase.deleteToDo(conn, todoText);

        // make sure there are no more records for our test todo
        results = stmt.executeQuery();
        numResults = 0;
        while (results.next()) {
            numResults++;
            String todosID = results.getString("id");
            String todosText = results.getString("text");
            boolean todosIsDone = results.getBoolean("is_done");
            System.out.println(todosID + "::" + todosText + "->" + todosIsDone);

        }
        assertEquals(0, numResults);
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
        String todoText = "UnitTest-ToDo";
        todoDatabase.insertToDo(conn, todoText);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text= 'UnitTest-ToDo'");
        ResultSet results = stmt.executeQuery();
        results.next();
        System.out.println("is_done:" + results.getString("is_done"));
        todoDatabase.toggleToDo(conn,results.getInt("id"));
        System.out.println("is_done:" + results.getString("is_done"));
        boolean expectedValue = !results.getBoolean("is_done");
        assertNotEquals(expectedValue, results.getBoolean("is_done"));

//        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//
//        // test if we can insert items into the database
//        String todoText = "UnitTest-ToDo";
//        todoDatabase.insertToDo(conn, todoText);

    }
}