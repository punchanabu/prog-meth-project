package main.game.item;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.game.character.sprite.Sprite;
import main.game.character.sprite.WalkingSprite;
import main.game.item.sprite.ThrowingAxeSprite;

public class ThrowingAxe extends Axe {
    private Sprite sprite;
    private double throwingSpeed = 5.0;
    private double axeWidth = 50;
    private double axeHeight = 50;


    public ThrowingAxe(String name, int value) {
        super(name, value);
        Image axeImage = new Image("/item/ax.png");
        ImageView axeImageView = new ImageView(axeImage);
        axeImageView.setFitWidth(axeWidth);
        axeImageView.setFitHeight(axeHeight);
        setSprite(new ThrowingAxeSprite(axeImage, axeWidth, axeHeight));
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    public double getThrowingSpeed() {
        return throwingSpeed;
    }


}