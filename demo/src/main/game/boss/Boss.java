package main.game.boss;

import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Boss {
    private String name;
    private int health;
    private int damage;
    private ImageView spriteImage;
    private int currentFrame = 0;
    private int FRAME_WIDTH;
    private int FRAME_HEIGHT;
    private int ANIMATION_LENGTH;
    private final int DELAY_FRAMES; // Delay in terms of number of frames
    private long lastTurnTime;
    private final long turnDelay;

    public Boss(String name, int health, int damage, int frameWidth, int frameHeight, int ANIMATION_LENGTH) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.FRAME_WIDTH = frameWidth;
        this.FRAME_HEIGHT = frameHeight;
        this.ANIMATION_LENGTH = ANIMATION_LENGTH;
        this.DELAY_FRAMES = 60;
        this.lastTurnTime = 0;
        this.turnDelay = 500;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void setHealth(int health) {
        this.health = Math.max(health, 0);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public ImageView getSpriteImage() {
        return spriteImage;
    }

    public void setSpriteImage(ImageView spriteImage) {
        this.spriteImage = spriteImage;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getAnimationLength() {
        return ANIMATION_LENGTH;
    }

    public void setAnimationLength(int animationLength) {
        ANIMATION_LENGTH = animationLength;
    }

    public int getDELAY_FRAMES() {
        return DELAY_FRAMES;
    }

    public long getLastTurnTime() {
        return lastTurnTime;
    }

    public void setLastTurnTime(long lastTurnTime) {
        this.lastTurnTime = lastTurnTime;
    }

    public long getTurnDelay() {
        return turnDelay;
    }

    public int getFRAME_WIDTH() {
        return FRAME_WIDTH;
    }

    public void setFRAME_WIDTH(int FRAME_WIDTH) {
        this.FRAME_WIDTH = FRAME_WIDTH;
    }

    public int getFRAME_HEIGHT() {
        return FRAME_HEIGHT;
    }

    public void setFRAME_HEIGHT(int FRAME_HEIGHT) {
        this.FRAME_HEIGHT = FRAME_HEIGHT;
    }

    public int getANIMATION_LENGTH() {
        return ANIMATION_LENGTH;
    }

    public void setANIMATION_LENGTH(int ANIMATION_LENGTH) {
        this.ANIMATION_LENGTH = ANIMATION_LENGTH;
    }
}
