package main.game.character;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.game.boss.Boss;
import main.game.boss.sprite.BossSprite;
import main.game.character.movement.CharacterJump;
import main.game.character.movement.CharacterMovement;
import main.game.character.movement.JumpBehavior;
import main.game.character.movement.MovementBehavior;
import main.game.character.sprite.JumpingSprite;
import main.game.character.sprite.Sprite;
import main.game.character.sprite.WalkingSprite;
import javafx.scene.image.Image;
import main.game.item.Item;
import main.game.item.Axe;
import main.game.item.ThrowingAxe;
import javafx.scene.layout.Pane;
import main.game.App;
import main.game.boss.sprite.AlienBossSprite;
import main.game.boss.sprite.TrollBossSprite;
import main.game.boss.sprite.BigBloatedBossSprite;
import main.game.boss.sprite.CentipedeBossSprite;
import main.game.ui.HealthBar;

public class Character {
    private Sprite sprite;
    private MovementBehavior movementBehavior;
    private JumpBehavior jumpBehavior;
    private Image jumpingImage;
    private Image walkingImage;
    private Item equippedItem;
    private ThrowingAxe throwingAxe;
    private boolean isThrowing = false;
    private long lastThrowTime = 0;
    private long throwCooldown = 50; // Cooldown time in milliseconds
    private boolean canThrowAxe = true;
    private boolean isJumping = false;
    private int health = 200;
    private long lastHitTime = 0;
    private long hitCooldown = 1000;

    public Character(String imagePathWalking, String imagePathJumping) {
        walkingImage = new Image(imagePathWalking);
        jumpingImage = new Image(imagePathJumping);
        equipItem(new ThrowingAxe("WoodCutter's Axe", 15));
        sprite.setScaleX(2.0);
        sprite.setScaleY(2.0);
        movementBehavior = new CharacterMovement(sprite);
        jumpBehavior = new CharacterJump(sprite);
    }

    public MovementBehavior getMovementBehavior() {
        return movementBehavior;
    }

    public void equipItem(Item item) {
        equippedItem = item;
        if (item instanceof Axe) {
            System.out.println("using axe!");
            setSprite(new WalkingSprite(new Image("/pink-monster/Pink_Monster_Holding_Axe.png")));
            if (item instanceof ThrowingAxe) {
                throwingAxe = (ThrowingAxe) item;
            }
        } else {
            setWalkingSprite();
        }
    }

    public void throwAxe(double sceneWidth, boolean movingLeft, BossSprite boss, Stage stage) {
        if (canThrowAxe && !isJumping) {
            canThrowAxe = false;
            lastThrowTime = System.currentTimeMillis();

            ThrowingAxe axe = new ThrowingAxe("Axe", 10);
            Pane parent = (Pane) sprite.getParent();

            if (parent != null) {
                double yOffset = -50;
                axe.getSprite().setTranslateX(sprite.getTranslateX());
                axe.getSprite().setTranslateY(sprite.getTranslateY() + yOffset);

                parent.getChildren().add(axe.getSprite());

                // Create the TranslateTransition animation
                TranslateTransition axeAnimation = new TranslateTransition(Duration.seconds(1), axe.getSprite());
                double axeDistance = movingLeft ? -sceneWidth : sceneWidth;
                axeAnimation.setByX(axeDistance - axe.getSprite().getTranslateX());
                axeAnimation.setOnFinished(event -> {
                    parent.getChildren().remove(axe.getSprite());
                    checkThrowCooldown();
                });

                // Create the RotateTransition animation
                RotateTransition rotateAnimation = new RotateTransition(Duration.seconds(0.5), axe.getSprite());
                rotateAnimation.setByAngle(360); // Adjust the rotation angle as needed
                rotateAnimation.setCycleCount(Animation.INDEFINITE);

                // Start both animations simultaneously
                ParallelTransition parallelTransition = new ParallelTransition(axeAnimation, rotateAnimation);
                parallelTransition.play();

                // Continuously check for collisions during the axe's movement
                AnimationTimer collisionCheck = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (boss.isColliding(axe)) {
                            int currentHealth = boss.getHealth();
                            int damage = getDamage(); // Get the damage from the equipped item
                            boss.setHealth(currentHealth - damage);
                            System.out.println("Sprite health: " + boss.getHealth());
                            parent.getChildren().remove(axe.getSprite());
                            this.stop(); // Stop the collision check when a collision occurs

                            if (boss.getHealth() <= 0) {
                                // Remove the defeated boss from the scene
                                parent.getChildren().remove(boss.getSpriteImage());

                                // Set the corresponding boss variable to null
                                if (boss instanceof AlienBossSprite) {
                                    App.alienBoss = null;
                                } else if (boss instanceof BigBloatedBossSprite) {
                                    App.bigBoss = null;
                                } else if (boss instanceof CentipedeBossSprite) {
                                    App.centipedeBoss = null;
                                } else if (boss instanceof TrollBossSprite) {
                                    App.trollBoss = null;
                                }

                                // Spawn the next boss
                                App.spawnBoss(stage);
                            }
                        }
                    }
                };
                collisionCheck.start();

                // Stop the collision check when the axe animation finishes
                parallelTransition.setOnFinished(event -> collisionCheck.stop());
            }
        }
    }

    private void checkThrowCooldown() {
        if (!canThrowAxe && System.currentTimeMillis() - lastThrowTime >= throwCooldown) {
            canThrowAxe = true;
        }
    }

    public void hitByBoss(int damage, Stage stage) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHitTime >= hitCooldown) {
            health -= damage;
            System.out.println("Hit by the boss! Health: " + health);
            lastHitTime = currentTime;

            if (health <= 0) {
                // Player has died, handle game over logic
                App.endGame(stage);
                App.currentState = App.currentState.GAMEOVER;
            }
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void jump() {
        if (!jumpBehavior.isJumping()) {
            jumpBehavior.jump();
            isJumping = true;
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
        if (jumpBehavior.isJumping()) {
            isJumping = true;
        } else {
            isJumping = false;
        }
        if (throwingAxe != null) {
            throwingAxe.getSprite().setTranslateX(throwingAxe.getSprite().getTranslateX() + throwingAxe.getThrowingSpeed());
        }
    }


    public void setWalkingSprite() {
        if (!(sprite instanceof WalkingSprite)) {
            setSprite(new WalkingSprite(walkingImage));
            sprite.setScaleX(2.0);
            sprite.setScaleY(2.0);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int i) {
        this.health = i;
    }

    public int getDamage() {
        if (equippedItem != null) {
            return equippedItem.getValue();
        }
        return 0;
    }


}
