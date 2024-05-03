package main.game.character.movement;
import main.game.character.sprite.Sprite;

public class CharacterMovement implements MovementBehavior {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 550;
    private final double speed = 2;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingUp = false;
    private boolean movingDown = false;

    private Sprite sprite;

    public CharacterMovement(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void moveLeft() {
        sprite.setTranslateX(sprite.getTranslateX() - speed);
        sprite.setScaleX(-2.0);
        sprite.updateSprite();
    }

    @Override
    public void moveRight() {
        sprite.setTranslateX(sprite.getTranslateX() + speed);
        sprite.setScaleX(2.0);
        sprite.updateSprite();
    }

    @Override
    public void moveUp() {
        sprite.setTranslateY(sprite.getTranslateY() - speed - 150);
        sprite.updateSprite();
    }

    @Override
    public void moveDown() {
        sprite.setTranslateY(sprite.getTranslateY() + speed);
        sprite.updateSprite();
    }

    @Override
    public void stop() {
        movingLeft = false;
        movingRight = false;
        movingUp = false;
        movingDown = false;
        sprite.setCurrentFrame(0);
        sprite.updateSprite();

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

    public void update() {
        if (movingLeft && sprite.getTranslateX() - speed >= 0) {
            moveLeft();
        }
        if (movingRight && sprite.getTranslateX() + sprite.getFrameViewPort().getWidth() + speed <= WIDTH) {
            moveRight();
        }
        if (movingUp) {
            moveUp();
        }
        if (movingDown && sprite.getTranslateY() + sprite.getFrameViewPort().getHeight() + speed <= HEIGHT) {
            moveDown();
        }
    }


    public boolean isMovingLeft() {
        return this.movingLeft;
    }
}