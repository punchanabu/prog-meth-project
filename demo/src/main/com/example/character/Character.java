package main.com.example.character;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Character extends ImageView {
    private static final int FRAME_WIDTH = 30; // Update to the width of a single frame
    private static final int FRAME_HEIGHT = 100; // Update to the height of a single frame
    private static final int ANIMATION_LENGTH = 5; // Total number of frames in the animation
    private static final int WIDTH = 640;
    private static final int HEIGHT = 550;
    private static final int xOffset = 5;
    private static final double GRAVITY = 0.2; // The strength of gravity
    private double velocityY = 0;
    private int currentFrame = 0;
    private final double speed = 2;
    private boolean moving = false; // A flag to check if the character is moving
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingUp = false;
    private boolean movingDown = false;
    private static final long FRAME_DURATION_NS = 100_000_000; // Duration of each frame in nanoseconds (e.g., 100ms)
    private long lastFrameUpdateNs = 0; // When the last frame update occurred
    private boolean isJumping = false; // Flag to track if the character is currently jumping
    private long lastJumpTimeNs = 0; // The time (in nanoseconds) when the character last jumped
    private static final long JUMP_COOLDOWN_NS = 500_000_000; // Jump cooldown duration in nanoseconds (e.g., 500ms)
    private final Image jumpingImage;
    private final Image walkingImage;

    public Character(String imagePathWalking, String imagePathJumping) {
        super(new Image(imagePathWalking));
        this.walkingImage = new Image(imagePathWalking);
        this.jumpingImage = new Image(imagePathJumping);
        this.setViewport(new Rectangle2D(0, 0, FRAME_WIDTH, FRAME_HEIGHT));
        setScale();
    }

    private void setScale() {
        // Double the size of the character
        setScaleX(2.0);
        setScaleY(2.0);
    }
    public void setMovingLeft(boolean moving) {
        this.movingLeft = moving;
    }

    public void setMovingRight(boolean moving) {
        this.movingRight = moving;
    }

    public void setMovingUp(boolean moving) {
        this.movingUp = moving;
    }

    public void setMovingDown(boolean moving) {
        this.movingDown = moving;
    }

    public void moveLeft() {
        moving = true;
        setTranslateX(getTranslateX() - speed);
        this.setScaleX(-2.0);
        updateSprite();
    }

    public void moveRight() {
        moving = true;
        setTranslateX(getTranslateX() + speed);
        this.setScaleX(2.0);
        updateSprite();
    }

    public void moveUp() {
        long now = System.nanoTime();
        if (!isJumping && (now - lastJumpTimeNs) >= JUMP_COOLDOWN_NS) {
            moving = true;
            setTranslateY(getTranslateY() - speed - 150);
            isJumping = true; // Set the jumping flag
            lastJumpTimeNs = now; // Record the last jump time
            updateSprite();
        }
    }

    public void moveDown() {
        moving = true;
        setTranslateY(getTranslateY() + speed);
        updateSprite();
    }

    public void stop() {
        moving = false;
        currentFrame = 0; // Reset to the first frame when stopped
        updateSprite();
    }

    private void updateSprite() {
        long now = System.nanoTime();
        if ((now - lastFrameUpdateNs) >= FRAME_DURATION_NS) {
            if (moving) {
                if (isJumping) {
                    // Use jumping sprite sheet
                    currentFrame = (currentFrame + 1) % ANIMATION_LENGTH; // Cycle through frames
                    int frameX = currentFrame * FRAME_WIDTH; // No need for xOffset here
                    this.setViewport(new Rectangle2D(frameX, 0, FRAME_WIDTH, FRAME_HEIGHT));
                    this.setImage(jumpingImage); // Set the jumping sprite sheet image
                } else {
                    // Use walking sprite sheet
                    currentFrame = (currentFrame + 1) % ANIMATION_LENGTH; // Cycle through frames
                    int frameX = (currentFrame * FRAME_WIDTH) + xOffset; // Add xOffset to the calculation
                    this.setViewport(new Rectangle2D(frameX, 0, FRAME_WIDTH, FRAME_HEIGHT));
                    this.setImage(walkingImage); // Set the walking sprite sheet image
                }
            } else {
                currentFrame = 0; // Reset to the first frame when not moving
                this.setViewport(new Rectangle2D(xOffset, 0, FRAME_WIDTH, FRAME_HEIGHT)); // Add xOffset here as well
            }
            lastFrameUpdateNs = now;
        }
    }


    public void update() {
        // Gravity effect
        if (!onGround()) {
            velocityY += GRAVITY;
            setTranslateY(getTranslateY() + velocityY);
        } else {
            velocityY = 0;
            isJumping = false;
        }

        // Movement and boundary checks
        if (movingLeft && getTranslateX() - speed >= 0) {
            moveLeft();
        }
        if (movingRight && getTranslateX() + FRAME_WIDTH + speed <= WIDTH) {
            moveRight();
        }
        if (movingUp) {
            moveUp();
        }
        if (movingDown && getTranslateY() + FRAME_HEIGHT + speed <= HEIGHT) {
            moveDown();
        }
    }

    private boolean onGround() {
        // A simple check to see if the character is on the ground
        return getTranslateY() + FRAME_HEIGHT >= HEIGHT;
    }
}
