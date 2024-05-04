package main.game.boss.sprite;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.game.boss.Boss;
import main.game.boss.sprite.BossSprite;
import main.game.item.ThrowingAxe;

import java.util.LinkedList;
import java.util.Queue;

public class TrollBossSprite extends Boss implements BossSprite {
    private ImageView spriteImage;
    private int currentFrame = 0;
    private static final int FRAME_WIDTH = 95;
    private static final int FRAME_HEIGHT = 200;
    private static final int ANIMATION_LENGTH = 6;
    private Queue<Double> positions = new LinkedList<>();
    private final int DELAY_FRAMES = 60; // Delay in terms of number of frames
    private long lastTurnTime = 0;
    private final long turnDelay = 500;

    public TrollBossSprite(String name, int health, int damage, String imagePath) {
        super(name, health, damage);
        this.spriteImage = new ImageView(new Image(imagePath));
        initializeSprite();
        startAnimation();
    }

    public void followPlayer(double playerX) {
        if (!positions.isEmpty()) {
            double delayedPosition = positions.peek(); // Get the oldest recorded position
            double bossX = spriteImage.getX();
            double distance = Math.abs(playerX - bossX);
            double move = delayedPosition > bossX ? 1 : -1;

            if (distance < 100) {
                // If the sprite is close to the player, add a delay before turning
                if (System.currentTimeMillis() - lastTurnTime >= turnDelay) {
                    spriteImage.setX(bossX + move);
                    lastTurnTime = System.currentTimeMillis();
                }
            } else {
                // If the sprite is far from the player, turn immediately
                spriteImage.setX(bossX + move);
            }

            // Flip the sprite image based on the movement direction
            if (move > 0) {
                spriteImage.setScaleX(-1); // Flip to the right
            } else if (move < 0) {
                spriteImage.setScaleX(1); // Flip to the left
            }
        }
    }
    public void recordPlayerPosition(double playerX) {
        positions.add(playerX);
        if (positions.size() > DELAY_FRAMES) {
            positions.poll(); // Remove the oldest position to maintain the size
        }
    }

    private void initializeSprite() {
        // Assuming each frame is 200x180 pixels and the sprite sheet is correctly formatted
        spriteImage.setFitHeight(400);  // Actual height of one frame
        spriteImage.setFitWidth(200);   // Actual width of one frame
        spriteImage.setViewport(new Rectangle2D(0, 0, FRAME_WIDTH, FRAME_HEIGHT));
        spriteImage.setX(400);  // Adjust as necessary
        spriteImage.setY(280);  // Adjust as necessary
    }

    private void startAnimation() {
        // Reduce the duration if the animation appears too slow
        Timeline animationTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> updateSprite())); // 200 ms per frame change
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }

    private void updateSprite() {
        currentFrame = (currentFrame + 1) % ANIMATION_LENGTH;
        int x = currentFrame * FRAME_WIDTH;
        spriteImage.setViewport(new Rectangle2D(x, 0, FRAME_WIDTH, FRAME_HEIGHT));
    }

    public ImageView getSpriteImage() {
        return spriteImage;
    }

    @Override
    public void specialAttack() {
        // Implement special attack mechanics
    }

    @Override
    public void ultimateAttack() {
        // Implement ultimate attack mechanics
    }

    public boolean isColliding(ThrowingAxe axe) {
        return spriteImage.getBoundsInParent().intersects(axe.getSprite().getBoundsInParent());
    }
}
