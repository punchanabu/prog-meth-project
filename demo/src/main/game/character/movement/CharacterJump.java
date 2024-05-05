package main.game.character.movement;

import main.game.character.sprite.Sprite;
import javafx.geometry.Rectangle2D;

public class CharacterJump implements JumpBehavior {
    private static final double GRAVITY = 0.01;
    private double velocityY = 0;
    private boolean isJumping = false;
    private long lastJumpTimeNs = 0;
    private static final long JUMP_COOLDOWN_NS = 100000000;
    private static final double JUMP_HEIGHT = 250;
    private Sprite sprite;

    public CharacterJump(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setLastJumpTimeNs(long lastJumpTimeNs) {
        this.lastJumpTimeNs = lastJumpTimeNs;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void jump() {
        long now = System.nanoTime();
        if (!isJumping && (now - lastJumpTimeNs) >= JUMP_COOLDOWN_NS) {
            sprite.setTranslateY(sprite.getTranslateY() - JUMP_HEIGHT);
            setJumping(true);
            setLastJumpTimeNs(now);
            sprite.updateSprite();
        }
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void update() {
        if (!onGround()) {
            setVelocityY(velocityY + GRAVITY);
            sprite.setTranslateY(sprite.getTranslateY() + velocityY);
        } else {
            setVelocityY(0);
            setJumping(false);
        }
    }

    private boolean onGround() {
        Rectangle2D viewport = sprite.getFrameViewPort();
        if (viewport != null) {
            return sprite.getTranslateY() + viewport.getHeight() >= 550;
        }
        return false;
    }
}