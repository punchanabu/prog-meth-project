package main.game.character;
import main.game.character.movement.CharacterJump;
import main.game.character.movement.CharacterMovement;
import main.game.character.movement.JumpBehavior;
import main.game.character.movement.MovementBehavior;
import main.game.character.sprite.JumpingSprite;
import main.game.character.sprite.Sprite;
import main.game.character.sprite.WalkingSprite;
import javafx.scene.image.Image;

public class Character {


    private Sprite sprite;
    private MovementBehavior movementBehavior;
    private JumpBehavior jumpBehavior;
    private Image jumpingImage;
    private Image walkingImage;

    public Character(String imagePathWalking, String imagePathJumping) {
        walkingImage = new Image(imagePathWalking);
        jumpingImage = new Image(imagePathJumping);
        setWalkingSprite();
        sprite.setScaleX(2.0);
        sprite.setScaleY(2.0);

        movementBehavior = new CharacterMovement(sprite);
        jumpBehavior = new CharacterJump(sprite);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    public void jump() {
        setJumpingSprite();
        if (!jumpBehavior.isJumping()) {
            jumpBehavior.jump();
        }
    }

    public void setMovingLeft(boolean moving) {
        setWalkingSprite();
        ((CharacterMovement) movementBehavior).setMovingLeft(moving);
    }

    public void setMovingRight(boolean moving) {
        setWalkingSprite();
        ((CharacterMovement) movementBehavior).setMovingRight(moving);
    }

    public void setMovingDown(boolean moving) {
        setWalkingSprite();
        ((CharacterMovement) movementBehavior).setMovingDown(moving);
    }

    public void update() {
        movementBehavior.update();
        jumpBehavior.update();
    }

    public void setJumpingSprite() {
        if (!(sprite instanceof JumpingSprite)) {
            setSprite(new JumpingSprite(jumpingImage));
            sprite.setScaleX(2.0);
            sprite.setScaleY(2.0);
        }
    }

    public void setWalkingSprite() {
        if (!(sprite instanceof WalkingSprite)) {
            setSprite(new WalkingSprite(walkingImage));
            sprite.setScaleX(2.0);
            sprite.setScaleY(2.0);
        }
    }
}
