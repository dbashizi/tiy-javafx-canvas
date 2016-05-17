package sample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.html.ListView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Godfather on 5/13/2016.
 */
public class ControllerTest {

    ToDoDatabase todoDatabase;
    Controller controller;

    @Before
    public void setUp() throws Exception {
        controller = new Controller();
        todoDatabase = new ToDoDatabase();

//        todoDatabase.init();
//        controller.todoList = new javafx.scene.control.ListView();


    }

    @After
    public void tearDown() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        todoDatabase.deleteAllToDo(conn);
    }

    @Test
    public void testRemoveItem() throws Exception {


        // test why this method removes duplicate entries when removing one.
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoTextOne = "UnitTest-ToDoOneTest1234567891";
        String todoTextTwo = "UnitTest-ToDoOneTest1234567891";
        String todoTextThree = "UnitTest-ToDoOneTestExtra1234567891";
        todoDatabase.insertToDo(conn, todoTextOne);
        todoDatabase.insertToDo(conn, todoTextTwo);
        todoDatabase.insertToDo(conn, todoTextThree);

        controller.initialize(null, null);

        ArrayList<ToDoItem> currentToDos = todoDatabase.selectToDos(conn);
        assertNotNull(currentToDos);

        String array = currentToDos.toString();
        System.out.println("=>array::" + array);
        controller.setSelectedItemIndex(0);
        controller.removeItem();
        ArrayList<ToDoItem> currentToDo = todoDatabase.selectToDos(conn);
        assertNotNull(currentToDo);
        String array2 = currentToDo.toString();
        System.out.println("=>array::" + array2);

        assertEquals(currentToDos.size()-1, currentToDo.size());
//        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos where text = ?");
//        stmt.setString(1, todoTextOne);
//        ResultSet results = stmt.executeQuery();
//        assertNotNull(results);

    }

    @Test
    public void testRemoveItemByID() throws Exception {
        //

    }

}