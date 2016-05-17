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
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;

import static junit.framework.TestCase.assertTrue;

public class Controller implements Initializable {
    @FXML
    ListView todoList;

    @FXML
    TextField todoText;

    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();
    ArrayList<ToDoItem> savableList = new ArrayList<ToDoItem>();
    String fileName = "todos.json";
    ToDoDatabase toDoDatabase;

    public String username;
    int selectedItemIndex = -1;
    int userID;

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startWithUserName();
    }

    public void startWithUserName() {
        try {
            toDoDatabase = new ToDoDatabase();
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            toDoDatabase.init();

            System.out.println("startWithUserName()");
            System.out.println("");
            System.out.print("New User or Existing User: ");
            Scanner userScanner = new Scanner(System.in);
            String userResponse = userScanner.nextLine();

            if (userResponse.equalsIgnoreCase("existing user")) {
                System.out.print("Enter email address: ");
                String username = userScanner.nextLine();
                userID = toDoDatabase.selectUser(conn, username).id;

                System.out.println("CHecking existing list ..");
                ArrayList<ToDoItem> todos = toDoDatabase.selectToDosForUser(conn, userID);
                for (ToDoItem item : todos) {
                    todoItems.add(item);
                }
                if (todoList != null) {
                    todoList.setItems(todoItems);
                }
            } else if (userResponse.equalsIgnoreCase("new user")) {
                System.out.println("");
                System.out.println("Welcome New User !!");
                System.out.print(" Enter Email Address ::");
                String username = userScanner.nextLine();
                System.out.print("Enter First and Last Name ::");
                String fullname = userScanner.nextLine();
                userID = toDoDatabase.insertUser(conn, username, fullname);

                System.out.println("Adding User list ..");
                ArrayList<ToDoItem> todos = toDoDatabase.selectToDosForUser(conn, userID);
                for (ToDoItem item : todos) {
                    todoItems.add(item);
                }
                if (todoList != null) {
                    todoList.setItems(todoItems);
                }
            } else {
                System.out.println("ERROR 404 ::");
            }
        } catch (SQLException e) {
        }
    }
    public void startWithDatabase(){
        try {
            toDoDatabase = new ToDoDatabase();
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            toDoDatabase.init();
            ArrayList<ToDoItem> todos = toDoDatabase.selectToDos(conn);
            for (ToDoItem item : todos) {
                todoItems.add(item);
            }
            if (todoList != null) {
                todoList.setItems(todoItems);
            }
        } catch (SQLException exception) {

        }
    }

public void startWithUserNameToJson() {
               System.out.print("Please enter your name: ");
               Scanner inputScanner = new Scanner(System.in);
                username = inputScanner.nextLine();

                if (username != null && !username.isEmpty()) {
                   fileName = username + ".json";
               }

                System.out.println("Checking existing list ...");
                ToDoItemList retrievedList = retrieveList();
                if (retrievedList != null) {
                    for (ToDoItem item : retrievedList.todoItems) {
                        todoItems.add(item);
                    }
                }
        }


    public void saveToDoList() {
        if (todoItems != null && todoItems.size() > 0) {
            System.out.println("Saving " + todoItems.size() + " items in the list");
            savableList = new ArrayList<ToDoItem>(todoItems);
            System.out.println("There are " + savableList.size() + " items in my savable list");
            saveList();
         //   saveListToJson();
        } else {
            System.out.println("No items in the ToDo List");
        }
    }

    public void addItem() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");

            System.out.println("Adding item ...");
            todoItems.add(new ToDoItem(todoText.getText()));
            toDoDatabase.insertToDo(conn, todoText.getText(), userID);
            todoText.setText("");
        } catch (SQLException e) {

        }
    }

    public void removeItem() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            ToDoItem toDoItem = (ToDoItem) todoList.getSelectionModel().getSelectedItem();
//            ToDoItem toDoItem = todoItems.get(selectedItemIndex);
           int selectedItemIndex = todoList.getSelectionModel().getSelectedIndex();
         //   ToDoItem id = toDoDatabase.selectToDos(conn).get(selectedItemIndex);
            System.out.println("Removing " + toDoItem.text + " ...");
        //    toDoDatabase.deleteToDoId(conn, id.id);
            toDoDatabase.deleteToDo(conn, toDoItem.text);

            todoItems.remove(toDoItem);


        } catch (SQLException e) {

        }
    }



    public void toggleItem() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");

            System.out.println("Toggling item ...");
            int selectedItemIndex = todoList.getSelectionModel().getSelectedIndex();
            ToDoItem todoItem = (ToDoItem) todoList.getSelectionModel().getSelectedItem();

            if (todoItem != null) {
                toDoDatabase.toggleToDo(conn, todoItem.id);
                todoItem.isDone = !todoItem.isDone;
                todoList.setItems(null);
                todoList.setItems(todoItems);
            }
            todoList.getSelectionModel().select(selectedItemIndex);
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
    
}
