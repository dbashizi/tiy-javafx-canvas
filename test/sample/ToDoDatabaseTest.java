package sample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Sonjtrez on 5/12/2016.
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
    public void init() throws Exception {
        // test to make sure we can access the new database
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement todoQuery = conn.prepareStatement("SELECT * FROM todos");
        ResultSet results = todoQuery.executeQuery();
        assertNotNull(results);
    }

    @Test
    public void testInsertToDo() throws Exception {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            String todoText = "UnitTest-ToDo";

            // adding a call to insertUser, so we have a user to add todos for
            String username = "unittester@tiy.com";
            String fullName = "Unit Tester";
            int userID = todoDatabase.insertUser(conn, username, fullName);

            todoDatabase.insertToDo(conn, todoText, userID);

            // make sure we can retrieve the todo we just created
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text = ?");
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
            // make sure we remove the test user we added earlier
            todoDatabase.deleteUser(conn, username);

            // make sure there are no more records for our test todo
            results = stmt.executeQuery();
            numResults = 0;
            while (results.next()) {
                numResults++;
            }
            assertEquals(0, numResults);
        }


    @Test
    public void testSelectAllToDos() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String firstToDoText = "UnitTest-ToDo1";
        String secondToDoText = "UnitTest-ToDo2";

        todoDatabase.insertToDo(conn, firstToDoText, 0);
        todoDatabase.insertToDo(conn, secondToDoText, 0);

        ArrayList<ToDoItem> todos = todoDatabase.selectToDos(conn);
        System.out.println("Found " + todos.size() + " todos in the database");

        assertTrue("There should be at least 2 todos in the database (there are " +
                todos.size() + ")", todos.size() > 1);

        todoDatabase.deleteToDo(conn, firstToDoText);
        todoDatabase.deleteToDo(conn, secondToDoText);
    }

    @Test
    public void testToggle() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "Toggle test";

        todoDatabase.insertToDo(conn, todoText, 0);

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text = 'Toggle test'");
        ResultSet results = stmt.executeQuery();
//        results.next();

        todoDatabase.toggleToDo(conn, results.getInt("id"));
        System.out.println("is_done:" + results.getString("is_done"));
        boolean expectedValue = !results.getBoolean("is_done");
        assertNotEquals(expectedValue, results.getBoolean("is_done"));


    }

    @Test
    public void testInsertUser() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        
        int userID = todoDatabase.insertUser(conn, username, fullName);
        System.out.println("User ID = " +  userID);
        
        // make sure we can retrieve the user we just created
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users where username = ?");
        stmt.setString(1, username);
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        results.next();

        int retrievedID = results.getInt("id");
        assertEquals(userID, retrievedID);
        
        todoDatabase.deleteUser(conn, username);
        
        // make sure there are no more records for our test user
        results = stmt.executeQuery();
        int numResults = 0;
        while (results.next()) {
            numResults++;
        }
        assertEquals(0, numResults);
    }

    @Test
    public void testInsertToDoForUser() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";
        String todoText2 = "UnitTest-ToDo2";

        // adding a call to insertUser, so we have a user to add todos for
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        int userID = todoDatabase.insertUser(conn, username, fullName);

        String username2 = "unitester2@tiy.com";
        String fullName2 = "Unit Tester 2";
        int userID2 = todoDatabase.insertUser(conn, username2, fullName2);

        todoDatabase.insertToDo(conn, todoText, userID);
        todoDatabase.insertToDo(conn, todoText2, userID2);

        // make sure each user only has one todo item
        ArrayList<ToDoItem> todosUser1 = todoDatabase.selectToDosForUser(conn, userID);
        ArrayList<ToDoItem> todosUser2 = todoDatabase.selectToDosForUser(conn, userID2);

        assertEquals(1, todosUser1.size());
        assertEquals(1, todosUser2.size());

        // make sure each todo item matches
        ToDoItem todoUser1 = todosUser1.get(0);
        assertEquals(todoText, todoUser1.text);
        ToDoItem todoUser2 = todosUser2.get(0);
        assertEquals(todoText2, todoUser2.text);

        todoDatabase.deleteToDo(conn, todoText);
        todoDatabase.deleteToDo(conn, todoText2);
        // make sure we remove the test user we added earlier
        todoDatabase.deleteUser(conn, username);
        todoDatabase.deleteUser(conn, username2);

    }

    @Test
    public void testSelectUsers() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String username = "trez@gmail.com";
        String fullName = "Sonjtrez Tigner";

        String userNameTwo = "newnew@gmail.com";
        String fullNameTwo = "Money Learn";

        User myUser;

        int userID = todoDatabase.insertUser(conn, username, fullName);
//        int userIDTwo = todoDatabase.insertUser(conn, userNameTwo, fullNameTwo);

        myUser = todoDatabase.selectUser(conn, username);
        myUser.getUserName();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet results = stmt.executeQuery();

        System.out.println(username);
        System.out.println(myUser.getUserName());

        assertEquals(username, myUser.getUserName());
        assertEquals(fullName, myUser.getFullName());


    }
}
