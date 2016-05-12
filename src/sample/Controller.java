package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;

import static sample.ToDoDataBase.DB_URL;
import static sample.ToDoDataBase.selectToDos;

public class Controller implements Initializable {
    @FXML
    ListView todoList;

    @FXML
    TextField todoText;

    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();
    ArrayList<ToDoItem> savableList = new ArrayList<ToDoItem>();
    String fileName = "todos.json";
    public final static String DB_URL = "jdbc:h2:./main";
    ToDoDataBase toDoDataBase = new ToDoDataBase();

    public String username;
    public Connection connect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        System.out.print("Please enter your name: ");
//        Scanner inputScanner = new Scanner(System.in);
//        username = inputScanner.nextLine();
//
//        if (username != null && !username.isEmpty()) {
//            fileName = username + ".json";
//        }
//
//        System.out.println("Checking existing list ...");
//        ToDoItemList retrievedList = retrieveList();
//        if (retrievedList != null) {
//            for (ToDoItem item : retrievedList.todoItems) {
//                todoItems.add(item);
//            }
//        }
//
        todoList.setItems(todoItems);
        try {
            startDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void startDataBase() throws SQLException {
        System.out.println("=========(Starting database)========");
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, text VARCHAR, is_done BOOLEAN)");
        selectToDos(conn);
        toDoDataBase.selectToDos(conn);
        connect = conn;

    }
    public void saveToDoList() {
//        if (todoItems != null && todoItems.size() > 0) {
//            System.out.println("Saving " + todoItems.size() + " items in the list");
//            savableList = new ArrayList<ToDoItem>(todoItems);
//            System.out.println("There are " + savableList.size() + " items in my savable list");
//            saveList();
//        } else {
//            System.out.println("No items in the ToDo List");
//        }
    }

    public void addItem() {
        try {
            System.out.println("Adding item: " + todoText.getText());
            toDoDataBase.insertToDo(connect,todoText.getText());
            todoItems.add(new ToDoItem(todoText.getText()));
            todoText.setText("");
            System.out.println(selectToDos(connect).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println("Adding item ...");
//        todoItems.add(new ToDoItem(todoText.getText()));
//        todoText.setText("");
    }

    public void removeItem() {
        try {
            toDoDataBase.deleteToDo(connect,todoText.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
//        System.out.println("Removing " + todoItem.text + " ...");
//        todoItems.remove(todoItem);
    }

    public void toggleItem() {
            int id =
            toDoDataBase.toggleToDo(connect,);
//        System.out.println("Toggling item ...");
//        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
//        if (todoItem != null) {
//            todoItem.isDone = !todoItem.isDone;
//            todoList.setItems(null);
//            todoList.setItems(todoItems);

    }

    public void saveList() {
        try {

            // write JSON
            JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
            String jsonString = jsonSerializer.serialize(new ToDoItemList(todoItems));

            System.out.println("JSON = ");
            System.out.println(jsonString);

            File sampleFile = new File(fileName);
            FileWriter jsonWriter = new FileWriter(sampleFile);
            jsonWriter.write(jsonString);
            jsonWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ToDoItemList retrieveList() {
        try {

            Scanner fileScanner = new Scanner(new File(fileName));
            fileScanner.useDelimiter("\\Z"); // read the input until the "end of the input" delimiter
            String fileContents = fileScanner.next();
            JsonParser ToDoItemParser = new JsonParser();

            ToDoItemList theListContainer = ToDoItemParser.parse(fileContents, ToDoItemList.class);
            System.out.println("==============================================");
            System.out.println("        Restored previous ToDoItem");
            System.out.println("==============================================");
            return theListContainer;
        } catch (IOException ioException) {
            // if we can't find the file or run into an issue restoring the object
            // from the file, just return null, so the caller knows to create an object from scratch
            return null;
        }
    }
    
}
