package main.game.boss.sprite;

import javafx.scene.image.ImageView;
import main.game.item.ThrowingAxe;

public interface BossSprite {
    boolean isColliding(ThrowingAxe axe);
    int getHealth();
    void setHealth(int health);
    ImageView getSpriteImage();
}