package main.game.boss.sprite;

import javafx.scene.image.ImageView;
import main.game.item.ThrowingAxe;

public interface BossSpriteAction {
    void followPlayer(double playerX);
    void recordPlayerPosition(double playerX);
    boolean isColliding(ThrowingAxe axe);
    ImageView getSpriteImage();
    int getHealth();
    void setHealth(int health);
}