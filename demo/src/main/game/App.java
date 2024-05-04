package main.game;

import javafx.animation.*;
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
import main.game.ui.HealthBar;

import static main.game.State.START;

public class App extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 520;
    private static Scene scene;
    private static Character character;
    public static State currentState = START;

    // HealthBar
    private static HealthBar characterHealthBar;
    private static HealthBar bossHealthBar;

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

        // Create the "Press Start" button
        Text startText = new Text("Press Start");
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
        buttonContainer.setTranslateX(390);
        buttonContainer.setTranslateY(250);
        buttonContainer.setOnMouseClicked(event -> {
            initGame(stage);
        });



        // Create a root pane to hold all the elements

        Pane root = new Pane();

        // Load the first frame of the animation
        ImageView animationView = new ImageView(new Image(App.class.getResource("/background/gen00.jpg").toExternalForm()));
        animationView.setFitWidth(WIDTH);
        animationView.setFitHeight(HEIGHT);
        root.getChildren().add(animationView);
        root.getChildren().add(buttonContainer);


        // Array of image paths for the animation
        String[] imagePaths = {
                "/background/gen00.jpg","/background/gen02.jpg","/background/gen04.jpg","/background/gen06.jpg","/background/gen08.jpg","/background/gen10.jpg","/background/gen12.jpg","/background/gen14.jpg","/background/gen16.jpg","/background/gen18.jpg","/background/gen20.jpg","/background/gen22.jpg","/background/gen24.jpg","/background/gen26.jpg","/background/gen28.jpg","/background/gen30.jpg","/background/gen32.jpg","/background/gen34.jpg","/background/gen36.jpg","/background/gen38.jpg"
        };

        // Timeline for animation loop
        Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            int currentIndex = findImageIndex(animationView.getImage().getUrl(), imagePaths);
            int nextIndex = (currentIndex + 1) % imagePaths.length;
            animationView.setImage(new Image(App.class.getResource(imagePaths[nextIndex]).toExternalForm()));
        }));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
        animationTimeline.play();

        // Create the scene and set it on the stage
        Scene scene = new Scene(root,WIDTH,HEIGHT);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Start Game");
        stage.show();
    }

    private static int findImageIndex(String imageUrl, String[] imagePaths) {
        for (int i = 0; i < imagePaths.length; i++) {
            if (imageUrl.endsWith(imagePaths[i])) {
                return i;
            }
        }
        return -1;
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


        HealthBar bossHealthBar = new HealthBar("Boss", 100, Color.RED);
        HealthBar characterHealthBar = new HealthBar("Player", 100, Color.LIGHTGREEN);



        root.getChildren().addAll(characterHealthBar.getProgressBar(), bossHealthBar.getProgressBar());

        // Position health bars
        characterHealthBar.getProgressBar().setLayoutX(20);
        characterHealthBar.getProgressBar().setLayoutY(20);
        bossHealthBar.getProgressBar().setLayoutX(WIDTH - 270);
        bossHealthBar.getProgressBar().setLayoutY(20);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                character.update();
                characterHealthBar.update(character.getHealth());
                if (alienBoss != null) {
                    bossHealthBar.update(alienBoss.getHealth());
                } else if (bigBoss != null) {
                    bossHealthBar.update(bigBoss.getHealth());
                } else if (centipedeBoss != null) {
                    bossHealthBar.update(centipedeBoss.getHealth());
                } else if (trollBoss != null) {
                    bossHealthBar.update(trollBoss.getHealth());
                }
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
        Pane root = new Pane();

        // Load the first frame of the animation
        ImageView animationView = new ImageView(new Image(App.class.getResource("/background/gen00.jpg").toExternalForm()));
        animationView.setFitWidth(WIDTH);
        animationView.setFitHeight(HEIGHT);
        root.getChildren().add(animationView);



        // Array of image paths for the animation
        String[] imagePaths = {
                "/GameOver/gameover00.jpg","/GameOver/gameover01.jpg","/GameOver/gameover02.jpg","/GameOver/gameover03.jpg","/GameOver/gameover04.jpg","/GameOver/gameover05.jpg","/GameOver/gameover06.jpg","/GameOver/gameover07.jpg","/GameOver/gameover08.jpg","/GameOver/gameover09.jpg","/GameOver/gameover10.jpg","/GameOver/gameover11.jpg","/GameOver/gameover12.jpg","/GameOver/gameover13.jpg","/GameOver/gameover14.jpg","/GameOver/gameover15.jpg","/GameOver/gameover16.jpg","/GameOver/gameover17.jpg","/GameOver/gameover18.jpg","/GameOver/gameover19.jpg"
        };

        // Timeline for animation loop
        Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            int currentIndex = findImageIndex(animationView.getImage().getUrl(), imagePaths);
            int nextIndex = (currentIndex + 1) % imagePaths.length;
            animationView.setImage(new Image(App.class.getResource(imagePaths[nextIndex]).toExternalForm()));
        }));
        animationTimeline.setCycleCount(20);
        animationTimeline.setOnFinished(event -> {
            showStartPage(stage); // Transition back to start page
        });
        animationTimeline.play();

        // Create the scene and set it on the stage
        Scene scene = new Scene(root,WIDTH,HEIGHT);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Start Game");
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