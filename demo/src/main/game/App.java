package main.game;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
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

import java.io.IOException;

import main.game.character.Character;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// out of requirement
import java.util.Random;

import javafx.util.Duration;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
        buttonContainer.setTranslateX(200);
        buttonContainer.setTranslateY(240);
        buttonContainer.setOnMouseClicked(event -> {
            initializeGame(stage);
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





    public static void main(String[] args) {
        launch(args);
    }

}