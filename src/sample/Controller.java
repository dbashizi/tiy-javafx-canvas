package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {
    @FXML
    ListView todoList;

    @FXML
    TextField todoText;

    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();
    ArrayList<ToDoItem> savableList = new ArrayList<ToDoItem>();
    String fileName = "todos.json";

    public String username;

    ToDoDatabase toDoDatabase = new ToDoDatabase();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        savableList.addAll()
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

        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:h2:./main");
            toDoDatabase.init();

            System.out.println("Checking existing list ...");
            ArrayList<ToDoItem> todos = toDoDatabase.selectToDos(conn);
            for (ToDoItem item : todos) {
                todoItems.add(item);
            }
            todoList.setItems(todoItems);
        } catch (SQLException e) {

        }
    }

    public void saveToDoList() {
        if (todoItems != null && todoItems.size() > 0) {
            System.out.println("Saving " + todoItems.size() + " items in the list");
            savableList = new ArrayList<ToDoItem>(todoItems);
            System.out.println("There are " + savableList.size() + " items in my savable list");
            //saveList();
            //add code to insert into database here!

        } else {
            System.out.println("No items in the ToDo List");
        }
    }

//    public void addItem() {
//        System.out.println("Adding item ...");
//        todoItems.add(new ToDoItem(todoText.getText()));
//        todoText.setText("");
//
//        try {
//            new ToDoDatabase().insertToDo(todoText.getText());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void addItem() { //From Aaron
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            System.out.println("Adding item ...");
            todoItems.add(new ToDoItem(todoText.getText()));
            String randomString = "random";
            toDoDatabase.insertToDo(conn, todoText.getText());
            todoText.setText("");
        } catch(SQLException e){

        }
    }

//    public void removeItem() {
//        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
//        System.out.println("Removing " + todoItem.text + " ...");
//        todoItems.remove(todoItem);
//    }

    public void removeItem() { //From Aaron
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            ToDoItem todoItem = (ToDoItem) todoList.getSelectionModel().getSelectedItem();
            System.out.println("Removing " + todoItem.text + " ...");
            toDoDatabase.deleteToDo(conn, todoItem.text);
            todoItems.remove(todoItem);
        }catch (SQLException e) {

        }
    }

    public void toggleItem() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            int selectedItemIndex = todoList.getSelectionModel().getSelectedIndex();
            ToDoItem todoItem = (ToDoItem) todoList.getSelectionModel().getSelectedItem();
            if (todoItem != null) {
                toDoDatabase.toggleToDo(conn, todoItem.id);
                todoItem.isDone = !todoItem.isDone;
                todoList.setItems(null);
                todoList.setItems(todoItems);
            }
            todoList.getSelectionModel().select(selectedItemIndex);

        } catch(SQLException e) {

        }
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
