package sample;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Scanner;

public class SampleFXMLApp extends Application {

    public static void main(String[] args) {

        System.out.println("Welcome to the TIY TODO App");

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
                controller.saveList();
                System.exit(0);
            }
        });

        primaryStage.show();
    }
}
