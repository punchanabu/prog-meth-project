package main.com.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.com.example.character.Character;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class App extends Application {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static Scene scene;
    private Character character;

    @Override
    public void start(Stage stage) throws IOException {
        Pane root = new Pane();
        scene = new Scene(root, WIDTH, HEIGHT);
        // Load and set the background image
        Image mapImage = new Image("/map/background.png");
        ImageView mapImageView = new ImageView(mapImage);
        mapImageView.fitWidthProperty().bind(scene.widthProperty());
        mapImageView.fitHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(mapImageView);


        character = new Character("/pink-monster/Pink_Monster_Walk_6.png", "/pink-monster/Pink_Monster_Jump_8.png");
        root.getChildren().add(character);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case A: character.setMovingLeft(true); break;
                case D: character.setMovingRight(true); break;
                case W: character.setMovingUp(true); break;
                case S: character.setMovingDown(true); break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case A: character.setMovingLeft(false); break;
                case D: character.setMovingRight(false); break;
                case W: character.setMovingUp(false); break;
                case SPACE: character.setMovingUp(false); break;
                case S: character.setMovingDown(false); break;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                character.update();
            }
        };
        timer.start();

        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}


