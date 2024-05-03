package main.game.item.sprite;

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;
import main.game.character.sprite.Sprite;

public class ThrowingAxeSprite extends Sprite {
    private double width;
    private double height;

    public ThrowingAxeSprite(Image image, double width, double height) {
        super(image);
        this.width = width;
        this.height = height;
        setFitWidth(width);
        setFitHeight(height);
    }

    @Override
    public void updateSprite() {
        // No animation for the throwing axe sprite
    }

    @Override
    public Rectangle2D getFrameViewPort() {
        // No frame viewport for the throwing axe sprite
        return null;
    }
}