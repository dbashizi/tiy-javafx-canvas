package sample;/**
 * Created by Dominique on 4/20/2016.
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LiveSessionApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private void clearScreen(GraphicsContext graphicsContext, Canvas myCanvas) {
        graphicsContext.clearRect(0, 0, myCanvas.getWidth(),
                myCanvas.getHeight());

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Live Coding Session");

        Canvas myCanvas = new Canvas(300, 300);
        myCanvas.setFocusTraversable(true);

        GraphicsContext graphicsContext = myCanvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.BURLYWOOD);

        graphicsContext.setLineWidth(10);

//        drawRandomThings(graphicsContext, 150.0, 63.45);

        Group rootGroup = new Group();
        rootGroup.getChildren().add(myCanvas);

        Scene myScene = new Scene(rootGroup, 300, 300);

        primaryStage.setScene(myScene);

        primaryStage.show();

        myCanvas.setOnMouseMoved(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
//                        System.out.println("x: " + mouseEvent.getX() +
//                                            "y: " + mouseEvent.getY());
                        graphicsContext.clearRect(0, 0, myCanvas.getWidth(),
                                                    myCanvas.getHeight());
                        drawRandomThings(graphicsContext,
                                        mouseEvent.getX(),
                                        mouseEvent.getY());
                    }
                }
        );

        myCanvas.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.println("mouse clicked");
                    }
                }
        );

        myCanvas.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent e) {
                KeyCode keyCode = e.getCode();
                System.out.println(keyCode.getName());
                System.out.println(e.getCode());
                System.out.println(e.getText());
            }
        });


    }

    private void drawRandomThings(GraphicsContext context,
                                  double clickedX,
                                  double clickedY) {
//        context.strokeOval(100 , 100, 50, 50);
        for (int counter = 0; counter < 200; counter++) {
            double xPos = Math.random() * clickedX;
            double yPos = Math.random() * clickedY;
            context.setStroke(
                    Color.color(
                            Math.random(), Math.random(), Math.random()
                        )
                    );
            context.strokeOval(xPos, yPos,
                            5, 5);
        }
    }
}
