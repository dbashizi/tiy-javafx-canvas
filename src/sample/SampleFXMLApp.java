package sample;/**
 * Created by Dominique on 4/21/2016.
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SampleFXMLApp extends Application {

    public static void main(String[] args) {

        System.out.println("Welcome to the TIY TODO App");
//        Server.createWebServer().start();
//        Connection conn = DriverManager.getConnection("jdbc:h2:./main"); //create a database connect to the aforementioned JDBC URL:
//        Statement stmt = conn.createStatement();
//        stmt.execute("CREATE TABLE IF NOT EXISTS todoitems (text VARCHAR, is_done BOOLEAN)");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("todolist.fxml"));

//        String fileName = getParameters().getRaw().get(0);

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("todolist.fxml").openStream());
        Controller controller = (Controller) fxmlLoader.getController();

        primaryStage.setTitle("TIY ToDo App");
        primaryStage.setScene(new Scene(root, 800, 600));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing -> saving the todo list! ");
//                controller.saveList();

            }
        });

        primaryStage.show();
    }
}
