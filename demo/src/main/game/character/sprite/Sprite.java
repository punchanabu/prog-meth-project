package main.game.character.sprite;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public abstract class Sprite extends ImageView {

    protected int currentFrame = 0;
    protected static final long FRAME_DURATION_NS = 100_000_000;
    protected long lastFrameUpdateNs = 0;

    public Sprite(Image image) {
        super(image);
    }

    public abstract void updateSprite();
    public abstract Rectangle2D getFrameViewPort();

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public long getLastFrameUpdateNs() {
        return lastFrameUpdateNs;
    }

    public void setLastFrameUpdateNs(long lastFrameUpdateNs) {
        this.lastFrameUpdateNs = lastFrameUpdateNs;
    }

}