package main.game.character.sprite;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class JumpingSprite extends Sprite {
    private static final int FRAME_WIDTH = 30;
    private static final int FRAME_HEIGHT = 100;
    private static final int ANIMATION_LENGTH = 5;

    public JumpingSprite(Image image) {        super(image);
    }

    public void updateSprite() {
        long now = System.nanoTime();
        if ((now - lastFrameUpdateNs) >= FRAME_DURATION_NS) {
            currentFrame = (currentFrame + 1) % ANIMATION_LENGTH;
            setViewport(getFrameViewPort());
            lastFrameUpdateNs = now;
        }
    }

    public Rectangle2D getFrameViewPort() {
        int frameX = currentFrame * FRAME_WIDTH;
        return new Rectangle2D(frameX, 0, FRAME_WIDTH, FRAME_HEIGHT);
    }
}
