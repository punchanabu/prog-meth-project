package main.game.character;
import main.game.character.movement.CharacterJump;
import main.game.character.movement.CharacterMovement;
import main.game.character.movement.JumpBehavior;
import main.game.character.movement.MovementBehavior;
import main.game.character.sprite.JumpingSprite;
import main.game.character.sprite.Sprite;
import main.game.character.sprite.WalkingSprite;
import javafx.scene.image.Image;
import main.game.ui.HealthBar;

public class Character {
    private Sprite sprite;
    private MovementBehavior movementBehavior;
    private JumpBehavior jumpBehavior;
    private Image jumpingImage;
    private Image walkingImage;
    private int maxHp;
    private int currentHp;
    private HealthBar healthBar;


    public Character(String imagePathWalking, String imagePathJumping) {
        setMaxHp(1000);
        setCurrentHp((int) (0.9*getMaxHp()));
        walkingImage = new Image(imagePathWalking);
        jumpingImage = new Image(imagePathJumping);
        setWalkingSprite();
        sprite.setScaleX(2.0);
        sprite.setScaleY(2.0);

        movementBehavior = new CharacterMovement(sprite);
        jumpBehavior = new CharacterJump(sprite);

        healthBar = new HealthBar(getMaxHp());

        healthBar.update(getCurrentHp());
//
//        System.out.println(getCurrentHp()+" : "+getMaxHp());

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
            setCurrentHp(getCurrentHp()+100);
//            healthBar.update(getCurrentHp()+1);
        }
    }

    public void setMovingLeft(boolean moving) {
        setWalkingSprite();
        ((CharacterMovement) movementBehavior).setMovingLeft(moving);
//        setCurrentHp(getCurrentHp()-1);
    }

    public void setMovingRight(boolean moving) {
        setWalkingSprite();
        ((CharacterMovement) movementBehavior).setMovingRight(moving);
//        setCurrentHp(getCurrentHp()+1);
//        healthBar.update(getCurrentHp()-1);
    }

    public void setMovingDown(boolean moving) {
        setWalkingSprite();
        ((CharacterMovement) movementBehavior).setMovingDown(moving);
    }

    public void update() {
        movementBehavior.update();
        jumpBehavior.update();
        healthBar.update(getCurrentHp());

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

    public int getMaxHp() {
        return this.maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrentHp() {
        return this.currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.max(0, currentHp);  // Clamp HP to prevent negatives
    }


    public HealthBar getHealthBar() {
        return healthBar;
    }


}
