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
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {
    @FXML
    ListView todoList;

    @FXML
    TextField todoText;


    int selectedItemIndex = -1;

    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();
    ArrayList<ToDoItem> savableList = new ArrayList<ToDoItem>();
    String fileName = "todos.json";

    ToDoDatabase toDoDatabase;
    int userID;


    public String username;

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startWithUserName();
    }

    public void startWithDatabase() {
        try {
            toDoDatabase = new ToDoDatabase();
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            toDoDatabase.init();

            System.out.println("Checking existing list ...");
            ArrayList<ToDoItem> todos = toDoDatabase.selectToDos(conn);
                for (ToDoItem item : todos) {
                    todoItems.add(item);
                }
            if (todoList != null) {
                todoList.setItems(todoItems);
            }
        } catch (SQLException e) {

        }
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
            String userResponce = userScanner.nextLine();

            if (userResponce.equalsIgnoreCase("existing user")) {
                System.out.print("Enter email address: ");
                String username = userScanner.nextLine();
                userID = toDoDatabase.selectUser(conn, username).id;

                System.out.println("Checking existing list ...");
                ArrayList<ToDoItem> todos = toDoDatabase.selectToDosForUser(conn, userID);
                for (ToDoItem item : todos) {
                    todoItems.add(item);
                }
                if (todoList != null) {
                    todoList.setItems(todoItems);
                }
            } else if (userResponce.equalsIgnoreCase("new user")) {
                System.out.println("");
                System.out.println("Welcome New User!");
                System.out.print("Enter Email Address: ");
                String username = userScanner.nextLine();
                System.out.print("Enter First and Last Name: ");
                String fullname = userScanner.nextLine();
                userID = toDoDatabase.insertUser(conn, username, fullname);

                System.out.println("Adding User list ...");
                ArrayList<ToDoItem> todos = toDoDatabase.selectToDosForUser(conn, userID);
                for (ToDoItem item : todos) {
                    todoItems.add(item);
                }
                if (todoList != null) {
                    todoList.setItems(todoItems);
                }
            } else {
//                System.out.println("ENTER VALID RESPONSE");
                System.out.println("ERROR");
            }

        } catch (SQLException e) {

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
        ToDoItemList retrievedList = retrieveJsonList();
        if (retrievedList != null) {
            for (ToDoItem item : retrievedList.todoItems) {
                todoItems.add(item);
            }
        }
        todoList.setItems(todoItems);
    }

    public void saveToDoList() {
        if (todoItems != null && todoItems.size() > 0) {
            System.out.println("Saving " + todoItems.size() + " items in the list");
            savableList = new ArrayList<ToDoItem>(todoItems);
            System.out.println("There are " + savableList.size() + " items in my savable list");
            saveListToJson();
        } else {
            System.out.println("No items in the ToDo List");
        }
    }

    public void addItem() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//            int userID = toDoDatabase.selectUser(conn, username).id;
            System.out.println("Adding item ...");
            todoItems.add(new ToDoItem(todoText.getText()));
//            toDoDatabase.insertToDo(conn, todoText.getText());
            toDoDatabase.insertToDoWithUserID(conn, todoText.getText(), userID);
            todoText.setText("");
        } catch(SQLException e){

        }
    }

    public void addItemWithoutID() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            System.out.println("Adding item ...");
            todoItems.add(new ToDoItem(todoText.getText()));
            toDoDatabase.insertToDo(conn, todoText.getText());
            todoText.setText("");
        } catch(SQLException e){

        }
    }

    public void removeItem() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//            ToDoItem todoItem = (ToDoItem) todoList.getSelectionModel().getSelectedItem();

            ToDoItem todoItem = todoItems.get(selectedItemIndex);
//            int selectedItemIndex = todoList.getSelectionModel().getSelectedIndex();
            ToDoItem id = toDoDatabase.selectToDos(conn).get(selectedItemIndex);
            System.out.println("Removing " + todoItem.text + " ...");
            toDoDatabase.deleteToDoByID(conn, id.id);
//           toDoDatabase.deleteToDo(conn, todoItem.text);
            todoItems.remove(todoItem);
        }catch (SQLException e) {

        }catch( ArrayIndexOutOfBoundsException ex){

        }
    }

    public void toggleItem() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");

            System.out.println("Toggling item ...");
            selectedItemIndex = todoList.getSelectionModel().getSelectedIndex();
            ToDoItem todoItem = (ToDoItem) todoList.getSelectionModel().getSelectedItem();
            ToDoItem id = toDoDatabase.selectToDos(conn).get(selectedItemIndex);

            if (todoItem != null) {
                todoItem.isDone = !todoItem.isDone;
                toDoDatabase.toggleToDo(conn, id.id);
                todoList.setItems(null);
                todoList.setItems(todoItems);
            }
            todoList.getSelectionModel().select(selectedItemIndex);

        } catch(SQLException e) {

        }catch( ArrayIndexOutOfBoundsException ex){

        }
    }

    public void saveListToJson() {
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

    public ToDoItemList retrieveJsonList() {
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
