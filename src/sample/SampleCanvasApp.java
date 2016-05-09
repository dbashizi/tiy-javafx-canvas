package sample;/**
 * Created by Dominique on 4/20/2016.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class SampleCanvasApp extends Application {

    final double DEFAULT_SCENE_HEIGHT = 275;
    final double DEFAULT_SCENE_WIDTH = 300;
    double strokeSize = 2;

    GraphicsContext globalGC;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Playing with Canvas");
        Group rootGroup = new Group();

        addCanvas(rootGroup, primaryStage);

        primaryStage.show();
    }

    private void increaseStrokeSize() {
        strokeSize += 1;
    }

    private void decreaseStrokeSize() {
        if (strokeSize > 2) {
            strokeSize--;
        }
    }

    private void addCanvas(Group rootGroup, Stage stage) {
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
        canvas.setFocusTraversable(true);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        this.globalGC = graphicsContext;

        graphicsContext.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
        graphicsContext.setLineWidth(2);

        drawShapes(graphicsContext);


        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
//                System.out.println("x: " + e.getX() + ", y: " + e.getY());
                graphicsContext.strokeOval(e.getX(), e.getY(), strokeSize, strokeSize);
            }
        });

        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent e) {
                KeyCode keyCode = e.getCode();
                if (keyCode.getName().equals("A")) {
                    graphicsContext.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
                } else if (keyCode.isArrowKey()) {
                    if (keyCode.getName().equals("Up")) {
                        increaseStrokeSize();
                    } else if (keyCode.getName().equals("Down")) {
                        decreaseStrokeSize();
                    }
                    System.out.println(keyCode.getName());
                } else if (keyCode.getName().equals("C")) {
                    graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                }

//                System.out.println(keyCode.getName());
//                System.out.println(e.getCode());
//                System.out.println(e.getText());
            }
        });

        rootGroup.getChildren().add(canvas);
        Scene scene = new Scene(rootGroup, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
        stage.setScene(scene);
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
//        gc.strokeLine(40, 10, 10, 40);
//        gc.fillOval(10, 60, 30, 30);
//        gc.strokeOval(60, 60, 30, 30);
//        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
//        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
//        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
//        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
//        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
//        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
//        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
//        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
//        gc.fillPolygon(new double[]{10, 40, 10, 40},
//                new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolygon(new double[]{60, 90, 60, 90},
//                new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolyline(new double[]{110, 140, 110, 140},
//                new double[]{210, 210, 240, 240}, 4);

//        Platform.runLater(new ThreadSample());
    }


    class ThreadSample implements Runnable {
        public void run() {
            try {
                while (true) {
                    double pos = Math.random() * 200;
                    System.out.println(Thread.currentThread().getId() + ":pausing");
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getId() + ":running");
                    Platform.runLater(new Runnable() {
                        public void run() {
                            globalGC.fillOval(pos, pos, 30, 30);
                        }
                    });
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

