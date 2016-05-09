package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Random;

public class Indicators extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Pane root = new HBox();
        stage.setScene(new Scene(root, 800, 600));

        Canvas canvas = new Canvas(800, 600);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        for (int i = 0; i < 10; i++) {
            final ProgressIndicator pi = new ProgressIndicator(0);
            root.getChildren().add(pi);

            // separate non-FX thread
            new Thread() {

                // runnable for that thread
                public void run() {
                    for (int i = 0; i < 20; i++) {
                        try {
                            // imitating work
                            Thread.sleep(new Random().nextInt(1000));
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        final double progress = i*0.05;
                        // update ProgressIndicator on FX thread
                        Platform.runLater(new Runnable() {

                            public void run() {
                                pi.setProgress(progress);
                            }
                        });
                    }
                }
            }.start();
        }

        stage.show();

    }
}