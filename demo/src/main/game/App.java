package main.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import main.game.character.Character;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// out of requirement


public class App extends Application {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static Scene scene;
    private Character character;

    private ImageView itemImageView;

    private static final int CHAR_WIDTH = 500;
    private static final int CHAR_HEIGHT = 100;

    private ImageView boss;
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;

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
        root.getChildren().add(character.getSprite());

        // Create and add the sword to the scene
//        Image itemImage = new Image("/item/ax.png");
//        itemImageView = new ImageView(itemImage);
//        itemImageView.setFitWidth(50);
//        itemImageView.setFitHeight(50);
//        itemImageView.setTranslateX(300); // Set the initial X position of the sword
//        itemImageView.setTranslateY(300); // Set the initial Y position of the sword
//        root.getChildren().add(itemImageView);
//        startSwordAnimation();

        // Create boss
        Image characterImage = new Image("/boss/AlienBoss/Attack1.png");
        boss = new ImageView(characterImage);
        boss.setFitWidth(CHAR_WIDTH);
        boss.setFitHeight(CHAR_HEIGHT);
        boss.setLayoutX((SCENE_WIDTH - CHAR_WIDTH) / 2);
        boss.setLayoutY((SCENE_HEIGHT - CHAR_HEIGHT) / 2);

        root.getChildren().add(boss);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case I:
                    boss.setLayoutY(boss.getLayoutY() - 10);
                    break;
                case K:
                    boss.setLayoutY(boss.getLayoutY() + 10);
                    break;
                case J:
                    boss.setLayoutX(boss.getLayoutX() - 10);
                    break;
                case L:
                    boss.setLayoutX(boss.getLayoutX() + 10);
                    break;
            }
        });


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

    // out of requirement naa
//    private void startSwordAnimation() {
//        AnimationTimer timer = new AnimationTimer() {
//            private final Random random = new Random();
//            private long lastUpdate = 0;
//
//            @Override
//            public void handle(long now) {
//                if (now - lastUpdate >= 2_000_000_000L) { // Update every 2 seconds
//                    double newX = random.nextDouble() * (WIDTH - itemImageView.getFitWidth());
//                    double newY = random.nextDouble() * (HEIGHT - itemImageView.getFitHeight());
//                    moveSwordAnimated(newX, newY);
//                    rotateSwordContinuously();
//                    lastUpdate = now;
//                }
//            }
//        };
//        timer.start();
//    }
//
//    private void moveSwordAnimated(double newX, double newY) {
//        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), itemImageView);
//        transition.setToX(newX);
//        transition.setToY(newY);
//        transition.play();
//    }
//
//    private void rotateSwordContinuously() {
//        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), itemImageView);
//        rotateTransition.setByAngle(360); // Rotate by 360 degrees (one full rotation)
//        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely
//        rotateTransition.play();
//    }


    public static void main(String[] args) {
        launch(args);
    }

}