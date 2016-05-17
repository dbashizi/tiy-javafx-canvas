package sample;

import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Godfather on 5/12/2016.
 */
public class ToDoDatabase {

    public final static String DB_URL = "jdbc:h2:./main";


    public void init() throws SQLException {
        // we'll add some implementation code here once we have a unit test method for it
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, text VARCHAR, is_done BOOLEAN, user_id INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, fullname VARCHAR)");
    }

    public int insertUser(Connection conn, String username, String fullname) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, fullname);
        stmt.execute();

        stmt = conn.prepareStatement("SELECT * FROM users where username = ?");
        stmt.setString(1, username);
        ResultSet results = stmt.executeQuery();
        results.next();
        return results.getInt("id");
    }

    public User selectUser(Connection conn, String username) throws SQLException {
        System.out.println("selectUser()");
        User newUser;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet results = stmt.executeQuery();
        boolean storedResults = results.next();
        if (!storedResults) {
            System.out.println("selectUser() is null");
            newUser = null;
        } else {
            int resultID = results.getInt("id");
            String resultUsername = results.getString("username");
            String resultUserFullName = results.getString("fullname");
            newUser = new User(resultID, resultUsername, resultUserFullName);
            System.out.println(resultID + " " + resultUsername + " " + resultUserFullName);

        }
        return newUser;
    }

    public void deleteUser(Connection conn, String username) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users where username = ?");
        stmt.setString(1, username);
        stmt.execute();
    }

    public void deleteAllUsers(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users");
        stmt.execute();
    }

        // this method will insert a single ToDo item
    public void insertToDo(Connection conn, String text) throws SQLException {
        System.out.println("insertToDo()");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL, ?, false)");
        stmt.setString(1, text);
        stmt.execute();
        printList(conn);
    }

    public void insertToDoWithUserID(Connection conn, String text, int userID) throws SQLException {
        System.out.println("insertToDoWithUserID()");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL, ?, false, ?)");
        stmt.setString(1, text);
        stmt.setInt(2, userID);
        stmt.execute();
//        printList(conn);
    }

    public void deleteToDo(Connection conn, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos WHERE text = ?");
        stmt.setString(1, text);
        stmt.execute();
    }

    public void deleteToDoByID(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public void deleteAllToDo(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos");
        stmt.execute();
    }

    public static void toggleToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE todos SET is_done = NOT is_done WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
        new ToDoDatabase().printList(conn);
    }

    public static ArrayList<ToDoItem> selectToDos(Connection conn) throws SQLException {
        System.out.println("selectToDos()");
        ArrayList<ToDoItem> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM todos");
        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            items.add(new ToDoItem(id, text, isDone));
           // System.out.println(id + " " + text + " " + isDone);
        }
        return items;
    }

    public static ArrayList<ToDoItem> selectDoneToDos(Connection conn) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM todos WHERE is_done = TRUE");
        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            items.add(new ToDoItem(id, text, isDone));
        }
        return items;
    }

    // this allows us to select a todo for a specific user
    public ArrayList<ToDoItem> selectToDosForUser(Connection conn, int userID) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos " +
                "INNER JOIN users ON todos.user_id = users.id " +
                "WHERE users.id = ?"); // restrict rows what I want to get back
        stmt.setInt(1, userID);
        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            items.add(new ToDoItem(id, text, isDone));
        }
        return items;
    }

    public void printList(Connection conn) throws SQLException {
        System.out.println("printList()");
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM todos");
        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            System.out.println(id + " " + text + " " + isDone);
        }
        System.out.println("printList()end");
    }
}
