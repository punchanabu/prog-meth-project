package main.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

import javafx.stage.Stage;
import main.game.boss.sprite.AlienBossSprite;
import main.game.boss.sprite.BigBloatedBossSprite;
import main.game.boss.sprite.CentipedeBossSprite;
import main.game.boss.sprite.TrollBossSprite;
import main.game.character.Character;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.game.character.movement.CharacterMovement;
import main.game.boss.sprite.BossSprite;
import static main.game.State.START;

public class App extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 520;
    private static Scene scene;
    private Character character;
    public static State currentState = START;

    // Boss
    public static AlienBossSprite alienBoss;
    public static BigBloatedBossSprite bigBoss;
    public static CentipedeBossSprite centipedeBoss;
    public static TrollBossSprite trollBoss;
    private AnimationTimer gameLoop;

    public void start(Stage stage) throws IOException {

        Pane root = new Pane();
        scene = new Scene(root, WIDTH, HEIGHT);
        // Load and set the background image
        Image mapImage = new Image("/map/background.png");
        ImageView mapImageView = new ImageView(mapImage);
        mapImageView.fitWidthProperty().bind(scene.widthProperty());
        mapImageView.fitHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(mapImageView);

        // Spawing the Boss
        spawnBoss();

        character = new Character("/pink-monster/Pink_Monster_Walk_6.png", "/pink-monster/Pink_Monster_Jump_8.png");
        root.getChildren().add(character.getSprite());

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                character.update();
                if (alienBoss != null) {
                    alienBoss.recordPlayerPosition(character.getSprite().getTranslateX());
                    alienBoss.followPlayer();
                    if (isCollidingWithBoss(alienBoss)) {
                        character.hitByBoss(alienBoss.getDamage());
                    }
                }
                if (bigBoss != null) {
                    bigBoss.recordPlayerPosition(character.getSprite().getTranslateX());
                    bigBoss.followPlayer();
                    if (isCollidingWithBoss(bigBoss)) {
                        character.hitByBoss(bigBoss.getDamage());
                    }
                }
                if (centipedeBoss != null) {
                    centipedeBoss.recordPlayerPosition(character.getSprite().getTranslateX());
                    centipedeBoss.followPlayer();
                    if (isCollidingWithBoss(centipedeBoss)) {
                        character.hitByBoss(centipedeBoss.getDamage());
                    }
                }
                if (trollBoss != null) {
                    trollBoss.recordPlayerPosition(character.getSprite().getTranslateX());
                    trollBoss.followPlayer();
                    if (isCollidingWithBoss(trollBoss)) {
                        character.hitByBoss(trollBoss.getDamage());
                    }
                }
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

                    if (alienBoss != null) {
                        character.throwAxe(scene.getWidth(), movingLeft, alienBoss);
                    } else if (bigBoss != null) {
                        character.throwAxe(scene.getWidth(), movingLeft, bigBoss);
                    } else if (centipedeBoss != null) {
                        character.throwAxe(scene.getWidth(), movingLeft, centipedeBoss);
                    } else if (trollBoss != null) {
                        character.throwAxe(scene.getWidth(), movingLeft, trollBoss);
                    }
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


    public static void spawnBoss() {
        switch (currentState) {
            case START:
                // Spawn Alien Boss
                alienBoss = new AlienBossSprite("Alien Boss", 100, 20, "/boss/AlienBoss/Attack1.png");
                alienBoss.setHealth(100);
                System.out.println("Spawning Alien Boss!");
                ((Pane) scene.getRoot()).getChildren().add(alienBoss.getSpriteImage());
                currentState = State.FIRST;
                break;
            case FIRST:
                System.out.println("123232323232323");
                    // Spawn Big Bloated Boss
                    bigBoss = new BigBloatedBossSprite("Big Bloated Boss", 100, 20, "/boss/BigBloatedBoss/Big_bloated_attack1.png");
                    bigBoss.setHealth(100);
                    ((Pane) scene.getRoot()).getChildren().add(bigBoss.getSpriteImage());
                    currentState = State.SECOND;
                break;
            case SECOND:
                    // Spawn Centipede Boss
                    centipedeBoss = new CentipedeBossSprite("Centipede Boss", 100, 20, "/boss/Centipede/Centipede_attack3.png");
                    centipedeBoss.setHealth(100);
                    ((Pane) scene.getRoot()).getChildren().add(centipedeBoss.getSpriteImage());
                    currentState = State.THIRD;
                break;
            case THIRD:
                    // Spawn Troll Boss
                    trollBoss = new TrollBossSprite("Troll Boss", 100, 20, "/boss/TrollBoss/Attack1.png");
                    trollBoss.setHealth(100);
                    ((Pane) scene.getRoot()).getChildren().add(trollBoss.getSpriteImage());
                    currentState = State.FOURTH;
                break;
            case FOURTH:
                    // All bosses defeated, end the game
                    endGame();
                    currentState = State.WIN;
                break;
            case GAMEOVER:
                // Handle game over logic
                break;
            case WIN:
                // Handle game win logic
                break;
        }
    }

    public static void endGame() {
        // Implement the game ending logic here
        if (currentState == State.WIN) {
            System.out.println("Congratulations! You have defeated all the bosses and won the game!");
        } else if (currentState == State.GAMEOVER) {
            System.out.println("Game Over! You have been defeated by the boss.");
        }
        // You can show a game over screen, display a message, or perform any other desired actions
    }

    private boolean isCollidingWithBoss(BossSprite boss) {
        return character.getSprite().getBoundsInParent().intersects(boss.getSpriteImage().getBoundsInParent());
    }

}