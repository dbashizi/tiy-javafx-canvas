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
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        todoDatabase.deleteAllToDo(conn);
        todoDatabase.deleteAllUsers(conn);
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
    public void testInsertToDoWithID() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";

        // adding a call to insertUser, so we have a user to add todos for
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        int userID = todoDatabase.insertUser(conn, username, fullName);

        todoDatabase.insertToDoWithUserID(conn, todoText, userID);

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

        todoDatabase.insertToDoWithUserID(conn, todoText, userID);
        todoDatabase.insertToDoWithUserID(conn, todoText2, userID2);

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
    public void testSelectUser() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        // adding a call to insertUser, so we have a user to add todos for
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        int userID = todoDatabase.insertUser(conn, username, fullName);

        User returnedUser = todoDatabase.selectUser(conn, username);

        System.out.println(returnedUser);
        assertEquals(userID, returnedUser.id); // first assert


        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet results = stmt.executeQuery();
        results.next();
        String returnedUsername = results.getString("username");
        assertEquals(username, returnedUsername); // second assert


        results = stmt.executeQuery();
        results.next();
        String returnedFullname = results.getString("fullname");
        assertEquals(fullName, returnedFullname); // third assert
    }

    @Test
    public void testToggleToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo789";
        todoDatabase.insertToDo(conn, todoText);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text= '" + todoText + "'");
        ResultSet results = stmt.executeQuery();
        results.next();
        boolean expectedValue = !results.getBoolean("is_done");
        System.out.println("is_done:" + results.getString("is_done"));

        todoDatabase.toggleToDo(conn,results.getInt("id"));

        ResultSet resultss = stmt.executeQuery();
        resultss.next();
        System.out.println("is_done:" + resultss.getString("is_done"));

//        assertNotEquals(expectedValue, resultss.getBoolean("is_done"));
        assertEquals(expectedValue, resultss.getBoolean("is_done"));

//        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//
//        // test if we can insert items into the database
//        String todoText = "UnitTest-ToDo";
//        todoDatabase.insertToDo(conn, todoText);

        todoDatabase.deleteToDo(conn, todoText);
    }

    @Test
    public void testSelectDoneToDos() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String firstToDoText = "UnitTest-ToDo1abcd";
        String secondToDoText = "UnitTest-ToDo2abcd";
        // 1. Find out how many items are done before we start adding anything to the database
        ArrayList<ToDoItem> doneToDosBefore = todoDatabase.selectDoneToDos(conn);
        assertNotNull(doneToDosBefore);
        int totalDoneToDosBefore = doneToDosBefore.size();
        System.out.println("number of todos = " + totalDoneToDosBefore);
        // 2. Add the items we're going to use for our integration test
        todoDatabase.insertToDo(conn, firstToDoText);
        todoDatabase.insertToDo(conn, secondToDoText);
        // 3. Validate that we still don't have any additional "done" items
        // since the ones we added should be "not done" by default
        ArrayList<ToDoItem> doneToDosAfterAdd = todoDatabase.selectDoneToDos(conn);
        assertNotNull(doneToDosAfterAdd);
        int totalDoneToDosAfter = doneToDosAfterAdd.size();
        System.out.println("number of todos = " + totalDoneToDosAfter);
        // 4. actual comparison here
        assertEquals(doneToDosBefore, doneToDosAfterAdd);
        // 4.5 get the id
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos where text = ?");
        stmt.setString(1, firstToDoText);
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        results.next();
        int todoID = results.getInt("id");
        // 5. Toggle the one item that we want to have done
        // (Note: in reality, this should go to the database to get the ID
        // of one of the items that we added in step 2)
        todoDatabase.toggleToDo(conn, todoID); // wrong call - don't hardcode the ID!!!
        // 6. Validate that there is now exactly 1 more item that is "done"
        // in the database
        ArrayList<ToDoItem> doneToDosAfterToggle = todoDatabase.selectDoneToDos(conn);
        assertNotNull(doneToDosAfterToggle);
        int totalDoneToDosAfterToggle = doneToDosAfterToggle.size();
        System.out.println("number of todos = " + totalDoneToDosAfterToggle);
        // 7. actual comparison
        assertEquals((totalDoneToDosBefore + 1), totalDoneToDosAfterToggle);
        // 8. cleanup
        todoDatabase.deleteToDo(conn, firstToDoText);
        todoDatabase.deleteToDo(conn, secondToDoText);
    }

    @Test
    public void testInsertUser() throws Exception {

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoUser = "UnitTest-Username-thomas@tsl.com";
        String todoUserfName = "UnitTest-User-Thomas Jackson";
        int returnedID = todoDatabase.insertUser(conn, todoUser, todoUserfName);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, todoUser);
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        int numResults = 0;
        while (results.next()) {
            numResults++;
            int userID = results.getInt("id");
            String username = results.getString("username");
            String userFullname = results.getString("fullname");
            System.out.println(userID + "::" + username + "=>" + userFullname);
        }
        assertEquals(1, numResults);

        results = stmt.executeQuery(); // results doesnt point to anything
        results.next(); // so this is next
        int userID = results.getInt("id");
        assertEquals(returnedID, userID);
    }
}