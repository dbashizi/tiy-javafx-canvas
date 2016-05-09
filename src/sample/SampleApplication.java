package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SampleApplication extends Application {

    final boolean DISPLAY_GRIDLINES = false;
    final double DEFAULT_SCENE_HEIGHT = 275;
    final double DEFAULT_SCENE_WIDTH = 300;
    Stage mainStage = null;


    Scene loginScene = null;
    Scene welcomeScene = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.mainStage = primaryStage;
        mainStage.setTitle("Welcome to JavaFX");

        setWelcomeScene();
        setLoginScene();

        // start with the login scene
        mainStage.setScene(loginScene);

        mainStage.show();
    }

    private void setLoginScene() {
        GridPane grid = createDefaultGrid();
        addLoginControls(grid);

        Group rootGroup = new Group();

        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);


        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
        gc.setLineWidth(5);
//        gc.strokeLine(40, 10, 10, 40);
//        gc.fillOval(10, 60, 30, 30);
//        gc.strokeOval(60, 60, 30, 30);
//        drawShapes(gc);

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
//                System.out.println("x: " + e.getX() + ", y: " + e.getY());
                gc.strokeOval(e.getX(), e.getY(), 10, 10);
            }
        });

        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent e) {
                System.out.println(e.getCode());
                System.out.println(e.getText());
            }
        });


        rootGroup.getChildren().add(canvas);

        rootGroup.getChildren().add(grid);

        loginScene = new Scene(rootGroup, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }

    private void addWelcomeControls(GridPane grid) {
        Text sceneTitle = new Text("Welcome to TIY JavaFX Demo");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Button button = new Button("Go Back");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbButton.getChildren().add(button);
        grid.add(hbButton, 1, 4);

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                System.out.println("Handling it!!!");
                mainStage.setScene(loginScene);
            }
        });

        grid.add(sceneTitle, 0, 0, 4, 2);
    }

    private void setWelcomeScene() {
        GridPane grid = createDefaultGrid();
        addWelcomeControls(grid);
        welcomeScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
    }

    private void procesLogin() {
//        Stage welcomeStage = new Stage();
        mainStage.setScene(welcomeScene);
//        welcomeStage.show();
    }

    private void addLoginControls(GridPane grid) {
        Text scenetitle = new Text("Please sign in");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button button = new Button("Sign in");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbButton.getChildren().add(button);
        grid.add(hbButton, 1, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1,6);
//        actionTarget.setText("Testing here ...");

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actionTarget.setFill(Color.FIREBRICK);
                actionTarget.setText("Sign in button pressed");

                procesLogin();
            }
        });

        grid.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                actionTarget.setText("x: " + e.getX() + ", y: " + e.getY());
            }
        });

    }

    private GridPane createDefaultGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.setGridLinesVisible(DISPLAY_GRIDLINES);

        return grid;
    }

    public void startWithFXML(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("todolist.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
