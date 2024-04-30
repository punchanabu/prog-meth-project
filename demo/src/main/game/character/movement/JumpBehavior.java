package main.game.character.movement;

public interface JumpBehavior {
    void jump();
    boolean isJumping();
    void update();
}