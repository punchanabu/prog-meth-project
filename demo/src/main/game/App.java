package main.game;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.*;
import javafx.scene.layout.Pane;

import java.io.IOException;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    private static Character character;
    public static State currentState = START;

    // Boss
    public static AlienBossSprite alienBoss;
    public static BigBloatedBossSprite bigBoss;
    public static CentipedeBossSprite centipedeBoss;
    public static TrollBossSprite trollBoss;
    private static AnimationTimer gameLoop;

    public void start(Stage stage) throws IOException {
        showStartPage(stage);

    }

    private static void showStartPage(Stage stage) {
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(58, 19, 92)),
                new Stop(0.5, Color.rgb(114, 60, 138)),
                new Stop(1, Color.rgb(158, 77, 181)));
        Rectangle background = new Rectangle(WIDTH, HEIGHT, gradient);


        // Create the moon
        Pane moonPane = new Pane();

// Create the moon
        Circle moon = new Circle(50, Color.rgb(255, 255, 204));
        moon.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0.1, 0, 0));

// Create the moon craters
        Circle crater1 = new Circle(5, Color.rgb(211, 211, 211));
        crater1.setTranslateX(15);
        crater1.setTranslateY(-15);

        Circle crater2 = new Circle(3, Color.rgb(211, 211, 211));
        crater2.setTranslateX(20);
        crater2.setTranslateY(10);

// Create the golden glow effect
        Glow glow = new Glow(0.8);
        glow.setLevel(0.7);
        glow.setInput(new GaussianBlur(1.5));

        Lighting lighting = new Lighting();
        lighting.setLight(new javafx.scene.effect.Light.Distant(45, 45, Color.rgb(255, 228, 138)));
        lighting.setSurfaceScale(5.0);
        lighting.setContentInput(glow);

        moonPane.setEffect(lighting);
        moonPane.getChildren().addAll(moon, crater1, crater2);

        moonPane.setTranslateX(550);
        moonPane.setTranslateY(70);

        // Create the stars
        Circle star1 = new Circle(3, Color.WHITE);
        star1.setTranslateX(50);
        star1.setTranslateY(100);

        Circle star2 = new Circle(3, Color.WHITE);
        star2.setTranslateX(600);
        star2.setTranslateY(200);

        // Create the clouds
        Ellipse cloud1 = new Ellipse(80, 30);
        cloud1.setFill(Color.WHITE);
        cloud1.setTranslateX(100);
        cloud1.setTranslateY(100);

        Ellipse cloud2 = new Ellipse(60, 20);
        cloud2.setFill(Color.WHITE);
        cloud2.setTranslateX(150);
        cloud2.setTranslateY(115);

        Ellipse cloud3 = new Ellipse(40, 15);
        cloud3.setFill(Color.WHITE);
        cloud3.setTranslateX(130);
        cloud3.setTranslateY(105);

        Ellipse cloud4 = new Ellipse(60, 25);
        cloud4.setFill(Color.WHITE);
        cloud4.setTranslateX(400);
        cloud4.setTranslateY(400);

        Ellipse cloud5 = new Ellipse(40, 20);
        cloud5.setFill(Color.WHITE);
        cloud5.setTranslateX(440);
        cloud5.setTranslateY(415);

        // Create the "Press Start" button
        Text startText = new Text("Start Game");
        startText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        startText.setFill(Color.WHITE);

        Rectangle buttonBackground = new Rectangle(200, 50, Color.TRANSPARENT);
        buttonBackground.setEffect(new InnerShadow(10, Color.rgb(0, 0, 0, 0.5)));

        LinearGradient gradient2 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 153, 51)),
                new Stop(0.5, Color.rgb(255, 102, 0)),
                new Stop(1, Color.rgb(204, 51, 0)));

        Rectangle buttonFill = new Rectangle(180, 40, gradient2);
        buttonFill.setArcWidth(20);
        buttonFill.setArcHeight(20);

        StackPane buttonContainer = new StackPane(buttonBackground, buttonFill, startText);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setTranslateX(400);
        buttonContainer.setTranslateY(230);
        buttonContainer.setOnMouseClicked(event -> {
            initGame(stage);
        });

        // Create animations
        FadeTransition fadeCloud1 = new FadeTransition(Duration.millis(3000), cloud1);
        fadeCloud1.setFromValue(0.0);
        fadeCloud1.setToValue(1.0);
        fadeCloud1.setCycleCount(TranslateTransition.INDEFINITE);
        fadeCloud1.setAutoReverse(true);

        FadeTransition fadeCloud2 = new FadeTransition(Duration.millis(3000), cloud4);
        fadeCloud2.setFromValue(0.0);
        fadeCloud2.setToValue(1.0);
        fadeCloud2.setCycleCount(TranslateTransition.INDEFINITE);
        fadeCloud2.setAutoReverse(true);
        fadeCloud2.setDelay(Duration.millis(1500));

        ParallelTransition cloudAnimation = new ParallelTransition(fadeCloud1, fadeCloud2);
        cloudAnimation.play();

        // Create a root pane to hold all the elements
        Pane root = new Pane(background, moonPane, star1, star2, cloud1, cloud2, cloud3,cloud4,cloud5, buttonContainer);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root,WIDTH,HEIGHT);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Start Game");
        stage.show();
    }

    public static void initGame(Stage stage) {
        Pane root = new Pane();
        scene = new Scene(root, WIDTH, HEIGHT);
        // Load and set the background image
        Image mapImage = new Image("/map/background.png");
        ImageView mapImageView = new ImageView(mapImage);
        mapImageView.fitWidthProperty().bind(scene.widthProperty());
        mapImageView.fitHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(mapImageView);

        // Spawing the Boss
        spawnBoss(stage);

        character = new Character("/pink-monster/Pink_Monster_Walk_6.png", "/pink-monster/Pink_Monster_Jump_8.png");
        root.getChildren().add(character.getSprite());

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                character.update();
                if (alienBoss != null) {
                    alienBoss.recordPlayerPosition(character.getSprite().getTranslateX());
                    alienBoss.followPlayer(character.getSprite().getTranslateX());
                    if (isCollidingWithBoss(alienBoss)) {
                        character.hitByBoss(alienBoss.getDamage(),stage);
                    }
                }
                if (bigBoss != null) {
                    bigBoss.recordPlayerPosition(character.getSprite().getTranslateX());
                    bigBoss.followPlayer(character.getSprite().getTranslateX());
                    if (isCollidingWithBoss(bigBoss)) {
                        character.hitByBoss(bigBoss.getDamage(),stage);
                    }
                }
                if (centipedeBoss != null) {
                    centipedeBoss.recordPlayerPosition(character.getSprite().getTranslateX());
                    centipedeBoss.followPlayer(character.getSprite().getTranslateX());
                    if (isCollidingWithBoss(centipedeBoss)) {
                        character.hitByBoss(centipedeBoss.getDamage(),stage);
                    }
                }
                if (trollBoss != null) {
                    trollBoss.recordPlayerPosition(character.getSprite().getTranslateX());
                    trollBoss.followPlayer(character.getSprite().getTranslateX());
                    if (isCollidingWithBoss(trollBoss)) {
                        character.hitByBoss(trollBoss.getDamage(),stage);
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
                        character.throwAxe(scene.getWidth(), movingLeft, alienBoss, stage);
                    } else if (bigBoss != null) {
                        character.throwAxe(scene.getWidth(), movingLeft, bigBoss, stage);
                    } else if (centipedeBoss != null) {
                        character.throwAxe(scene.getWidth(), movingLeft, centipedeBoss, stage);
                    } else if (trollBoss != null) {
                        character.throwAxe(scene.getWidth(), movingLeft, trollBoss, stage);
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

    private static void showGameOverPage(Stage stage) {
        // Create a dark semi-transparent background
        Rectangle background = new Rectangle(WIDTH, HEIGHT, Color.rgb(0, 0, 0, 0.8));

        // Create the "Game Over" text
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gameOverText.setFill(Color.RED);

        // Create the "Play Again" button
        Text playAgainText = new Text("Play Again");
        playAgainText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        playAgainText.setFill(Color.WHITE);

        Rectangle buttonBackground = new Rectangle(200, 50, Color.TRANSPARENT);
        buttonBackground.setEffect(new InnerShadow(10, Color.rgb(0, 0, 0, 0.5)));

        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 153, 51)),
                new Stop(0.5, Color.rgb(255, 102, 0)),
                new Stop(1, Color.rgb(204, 51, 0)));

        Rectangle buttonFill = new Rectangle(180, 40, gradient);
        buttonFill.setArcWidth(20);
        buttonFill.setArcHeight(20);

        StackPane buttonContainer = new StackPane(buttonBackground, buttonFill, playAgainText);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setTranslateY(100);
        buttonContainer.setOnMouseClicked(event -> {
            currentState = State.START;
            showStartPage(stage);
        });

        // Create a VBox to hold the text and button
        VBox vbox = new VBox(20, gameOverText, buttonContainer);
        vbox.setAlignment(Pos.CENTER);

        // Create a root pane to hold all the elements
        Pane root = new Pane(background, vbox);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Game Over");
        stage.show();
    }

    private static void showWinPage(Stage stage) {
        // Create a gradient background
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 215, 0)),
                new Stop(0.5, Color.rgb(255, 165, 0)),
                new Stop(1, Color.rgb(255, 69, 0)));
        Rectangle background = new Rectangle(WIDTH, HEIGHT, gradient);

        // Create the "You Win!" text
        Text winText = new Text("You Win!");
        winText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        winText.setFill(Color.WHITE);

        // Create the "Play Again" button
        Text playAgainText = new Text("Play Again");
        playAgainText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        playAgainText.setFill(Color.WHITE);

        Rectangle buttonBackground = new Rectangle(200, 50, Color.TRANSPARENT);
        buttonBackground.setEffect(new InnerShadow(10, Color.rgb(0, 0, 0, 0.5)));

        LinearGradient buttonGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(34, 139, 34)),
                new Stop(0.5, Color.rgb(50, 205, 50)),
                new Stop(1, Color.rgb(0, 128, 0)));

        Rectangle buttonFill = new Rectangle(180, 40, buttonGradient);
        buttonFill.setArcWidth(20);
        buttonFill.setArcHeight(20);

        StackPane buttonContainer = new StackPane(buttonBackground, buttonFill, playAgainText);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setTranslateY(100);
        buttonContainer.setOnMouseClicked(event -> {
            currentState = State.START;
            showStartPage(stage);
        });

        // Create a VBox to hold the text and button
        VBox vbox = new VBox(20, winText, buttonContainer);
        vbox.setAlignment(Pos.CENTER);

        // Create a root pane to hold all the elements
        Pane root = new Pane(background, vbox);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("You Win");
        stage.show();
    }

    public static void spawnBoss(Stage stage) {
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
                currentState = State.WIN;
                    endGame(stage);
                break;
            case GAMEOVER:
                // Handle game over logic
                break;
            case WIN:
                // Handle game win logic
                break;
        }
    }

    public static void endGame(Stage stage) {
        // Implement the game ending logic here
        if (currentState == State.WIN) {
            Platform.runLater(() -> {
                showWinPage(stage);
            });
        } else if (currentState == State.GAMEOVER) {
            Platform.runLater(() -> {
                showGameOverPage(stage);
            });
        }

    }

    private static boolean isCollidingWithBoss(BossSprite boss) {
        return character.getSprite().getBoundsInParent().intersects(boss.getSpriteImage().getBoundsInParent());
    }

}