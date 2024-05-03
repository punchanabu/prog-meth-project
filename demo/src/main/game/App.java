package main.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import main.game.boss.sprite.BossSprite;
import main.game.character.Character;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.game.character.movement.CharacterMovement;

public class App extends Application {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 520;
    private static Scene scene;
    private Character character;
    private BossSprite boss;
    private AnimationTimer gameLoop;
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

        boss = new BossSprite("Evil Boss", 100, 20, "/boss/AlienBoss/Walk.png");// Set the initial y-position of the boss
        root.getChildren().add(boss.getSpriteImage());
        character = new Character("/pink-monster/Pink_Monster_Walk_6.png", "/pink-monster/Pink_Monster_Jump_8.png");
        root.getChildren().add(character.getSprite());

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                character.update();
                boss.recordPlayerPosition(character.getSprite().getTranslateX());
                boss.followPlayer();

            }
        };

        gameLoop.start();
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case A:
                    character.setMovingLeft(true);
                    break;
                case D:
                    character.setMovingRight(true);
                    break;
                case W:
                case SPACE:
                    character.jump();
                    break;
                case S:
                    character.setMovingDown(true);
                    break;
                case J:
                    boolean movingLeft = ((CharacterMovement) character.getMovementBehavior()).isMovingLeft();
                    character.throwAxe(scene.getWidth(), movingLeft);
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case A:
                    character.setMovingLeft(false);
                    break;
                case D:
                    character.setMovingRight(false);
                    break;
                case S:
                    character.setMovingDown(false);
                    break;
            }
        });
        AnimationTimer timer2 = new AnimationTimer() {
            @Override
            public void handle(long now) {
                character.update();
            }
        };
        timer2.start();


        stage.setResizable(false); // Disable window resizing
        stage.setWidth(WIDTH); // Set the fixed width
        stage.setHeight(HEIGHT); // Set the fixed height

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startFight() {
        // Implement the fight sequence here
    }


}