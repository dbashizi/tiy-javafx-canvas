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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {
    @FXML
    ListView todoList;

    @FXML
    TextField todoText;

    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();
    ArrayList<ToDoItem> savableList = new ArrayList<ToDoItem>();
    ToDoDatabase toDoDatabase = new ToDoDatabase();
    String fileName = "todos.json";

    public String username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        newQuestions();

//        System.out.print("Please enter your name: ");
//        Scanner inputScanner = new Scanner(System.in);
//        username = inputScanner.nextLine();

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
//        todoList.setItems(todoItems);

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            ArrayList<ToDoItem> todos = toDoDatabase.selectToDos(conn);
            System.out.println("Retrieving toDoItem List ...");
            ToDoItemList retrievedList = retrieveList();
            for (ToDoItem item : todos) {
                todoItems.add(item);
            }
            todoList.setItems(todoItems);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void saveToDoList() {
        if (todoItems != null && todoItems.size() > 0) {
            System.out.println("Saving " + todoItems.size() + " items in the list");
            savableList = new ArrayList<ToDoItem>(todoItems);
            System.out.println("There are " + savableList.size() + " items in my savable list");
            saveList();
        } else {
            System.out.println("No items in the ToDo List");
        }
    }

    public void addItem() {
        try {
        System.out.println("Adding item ...");
        Connection conn =  DriverManager.getConnection("jdbc:h2:./main");
        toDoDatabase.insertToDo(conn, todoText.getText(), 0);
        todoItems.add(new ToDoItem(todoText.getText()));
        todoText.setText("");




        } catch (SQLException exception) {

        }
    }

    public void removeItem() {
        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
        System.out.println("Removing " + todoItem.text + " ...");
        todoItems.remove(todoItem);
    }

    public void toggleItem() {
        System.out.println("Toggling item ...");
        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            if (todoItem != null) {
                todoItem.isDone = !todoItem.isDone;
                todoList.setItems(null);
                todoList.setItems(todoItems);


                toDoDatabase.toggleToDo(conn, todoItem.id);
            }
            todoList.getSelectionModel().getSelectedItem();

        } catch (SQLException exception) {

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

    public void deleteToDo() {
        try {
            ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
            System.out.println("Deleting item " + todoItem.text + "...");
            todoItems.remove(todoItem);

            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            toDoDatabase.deleteToDo(conn,todoItem.text);

        } catch (SQLException sqlexception) {

        }
    }

    public void newQuestions() {
        System.out.println("Create and Account or Log in...");
        System.out.println("================================");
        System.out.println("[1] - Login");
        System.out.println("[1] - Create Account");

        Scanner answer = new Scanner(System.in);
        String userAnswer = answer.nextLine();

        if (userAnswer.equals("1")) {
            toDoDatabase.insertUser(conn, );
        }
    }

}
