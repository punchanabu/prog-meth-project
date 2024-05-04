package main.game;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import main.game.character.Character;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// out of requirement
import java.util.Random;

import javafx.util.Duration;


public class App extends Application {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static Scene scene;
    private Character character;

    private ImageView itemImageView;
    private boolean isColliding = false;

    @Override
    public void start(Stage stage) throws IOException {
        showStartPage(stage);
    }


    public void initializeGame(Stage stage)  {

        Pane root = new Pane();
        scene = new Scene(root, WIDTH, HEIGHT);
        // Load and set the background image
        Image mapImage = new Image("/map/background.png");
        ImageView mapImageView = new ImageView(mapImage);
        mapImageView.fitWidthProperty().bind(scene.widthProperty());
        mapImageView.fitHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(mapImageView);


        character = new Character("/pink-monster/Pink_Monster_Walk_6.png", "/pink-monster/Pink_Monster_Walk_6.png");
        root.getChildren().add(character.getSprite());


        root.getChildren().add(character.getHealthBar().getProgressBar());
        character.getHealthBar().getProgressBar().setLayoutX(10);
        character.getHealthBar().getProgressBar().setLayoutY(50);


        // Create and add the sword to the scene
        Image itemImage = new Image("/item/ax.png");
        itemImageView = new ImageView(itemImage);
        itemImageView.setFitWidth(50);
        itemImageView.setFitHeight(50);
        itemImageView.setTranslateX(300); // Set the initial X position of the sword
        itemImageView.setTranslateY(300); // Set the initial Y position of the sword

        itemImageView.setStyle("-fx-border-color: green; -fx-border-width: 100;"); // i wanna see border but disappear i cry

        root.getChildren().add(itemImageView);
        startSwordAnimation();


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
                case O :
                    showGameOverPage(stage);
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
                handleCharacterItemCollision();
            }
        };
        timer.start();

        stage.setScene(scene);
        stage.show();


    }

    // out of requirement naa
    private void startSwordAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            private final Random random = new Random();
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 2_000_000_000L) { // Update every 2 seconds
                    double newX = random.nextDouble() * (WIDTH - itemImageView.getFitWidth());
                    double newY = random.nextDouble() * (HEIGHT - itemImageView.getFitHeight());
                    moveSwordAnimated(newX, newY);
                    rotateSwordContinuously();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void moveSwordAnimated(double newX, double newY) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), itemImageView);
        transition.setToX(newX);
        transition.setToY(newY);
        transition.play();
    }

    private void rotateSwordContinuously() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), itemImageView);
        rotateTransition.setByAngle(360); // Rotate by 360 degrees (one full rotation)
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely
        rotateTransition.play();
    }

    private void handleCharacterItemCollision() {
        // Get character and item positions/bounds in the scene
        double characterX = character.getSprite().getLayoutX();
        double characterY = character.getSprite().getLayoutY();
        double characterWidth = character.getSprite().getBoundsInLocal().getWidth();
        double characterHeight = character.getSprite().getBoundsInLocal().getHeight();

//        System.out.println(characterX+" : "+characterY + " : "+characterWidth+" : "+characterHeight);
        double itemX = itemImageView.localToScene(itemImageView.getBoundsInLocal()).getMinX();
        double itemY = itemImageView.localToScene(itemImageView.getBoundsInLocal()).getMinY();

        double itemWidth = itemImageView.getBoundsInLocal().getWidth();
        double itemHeight = itemImageView.getBoundsInLocal().getHeight();

//        System.out.println(itemX+" : "+itemY);

        double characterCenterX = characterX - characterWidth / 2.0;
        double characterCenterY = characterY - characterHeight / 2.0;



        double collisionRadius = characterWidth*2  ;

        double itemCenterX = itemX + itemWidth / 2.0;
        double itemCenterY = itemY + itemHeight / 2.0;

//        System.out.println(itemCenterX+" : "+itemCenterY);

        double distance = Math.sqrt(Math.pow(characterCenterX - itemCenterX, 2) +
                Math.pow(characterCenterY - itemCenterY, 2));
        isColliding = distance <= collisionRadius + itemImageView.getFitWidth() / 2.0;


        if (isColliding ) {
//            System.out.println(distance+" : "+ collisionRadius + itemImageView.getFitWidth() / 2.0);
            character.setCurrentHp(character.getCurrentHp() - 10);
            character.getHealthBar().update(character.getCurrentHp());
            isColliding = false; // Reset flag after health reduction
        }
    }

    private void showStartPage(Stage stage) {

//        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
//                new Stop(0, Color.rgb(58, 19, 92)),
//                new Stop(0.5, Color.rgb(114, 60, 138)),
//                new Stop(1, Color.rgb(158, 77, 181)));
//        Rectangle background = new Rectangle(WIDTH, HEIGHT, gradient);


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
        buttonContainer.setTranslateX(220);
        buttonContainer.setTranslateY(190);
        buttonContainer.setOnMouseClicked(event -> {
            initializeGame(stage);
        });



        // Create a root pane to hold all the elements

        Pane root = new Pane();

        // Load the first frame of the animation
        ImageView animationView = new ImageView(new Image(getClass().getResource("/background/gen00.jpg").toExternalForm()));
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
            animationView.setImage(new Image(getClass().getResource(imagePaths[nextIndex]).toExternalForm()));
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

    private int findImageIndex(String imageUrl, String[] imagePaths) {
        for (int i = 0; i < imagePaths.length; i++) {
            if (imageUrl.endsWith(imagePaths[i])) {
                return i;
            }
        }
        return -1;
    }

    private void showGameOverPage(Stage stage) {
        Pane root = new Pane();

        // Load the first frame of the animation
        ImageView animationView = new ImageView(new Image(getClass().getResource("/background/gen00.jpg").toExternalForm()));
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
            animationView.setImage(new Image(getClass().getResource(imagePaths[nextIndex]).toExternalForm()));
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

    public static void main(String[] args) {
        launch(args);
    }

}

