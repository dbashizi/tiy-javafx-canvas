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
 * Created by Justins PC on 5/12/2016.
 */
public class ToDoDataBaseTest {
    ToDoDataBase todoDatabase;

    @Before
    public void setUp() throws Exception {
        todoDatabase = new ToDoDataBase();
        todoDatabase.init();


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testStartUp() throws Exception {
        // test to make sure we can access the new database
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement todoQuery = conn.prepareStatement("SELECT * FROM todos");
        ResultSet results = todoQuery.executeQuery();
        assertNotNull(results);


    }
    @Test
    public void testSelectAllToDos() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main"); //connect to database
        String firstToDoText = "UnitTest-ToDo1"; //makes new todo item text
        String secondToDoText = "UnitTest-ToDo2";

        todoDatabase.insertToDo(conn, firstToDoText); //adds the todo items to the data base useing are insert method
        todoDatabase.insertToDo(conn, secondToDoText);

        ArrayList<ToDoItem> todos = todoDatabase.selectToDos(conn); // adds the todos to the array list useing select todos method
        System.out.println("Found " + todos.size() + " todos in the database"); // prints out the ammount of items in the array

        assertTrue("There should be at least 2 todos in the database (there are " +
                todos.size() + ")", todos.size() > 1); //checks to make sure that the two to do items were added to the data base

        todoDatabase.deleteToDo(conn, firstToDoText); //deletes both to do items in order to return the data base to its regular state.
        todoDatabase.deleteToDo(conn, secondToDoText);
    }
//    @Test
//    public void testInsertToDo() throws Exception {
//        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//        String todoText = "UnitTest-ToDo";
//        todoDatabase.insertToDo(conn, todoText);
//        // make sure we can retrieve the todo we just created
//        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos where text = ?");
//        stmt.setString(1, todoText);
//        ResultSet results = stmt.executeQuery();
//        assertNotNull(results);
//        // count the records in results to make sure we get what we expected
//        int numResults = 0;
//        while (results.next()) {
//            numResults++;
//        }
//        assertEquals(1, numResults);
//    }
    @Test
    public void testInsertToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main"); //connect to server
        String todoText = "UnitTest-ToDo"; // creates new text

        todoDatabase.insertToDo(conn, todoText); // inserts todo text into the data base
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
    
}