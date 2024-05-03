package main.game.boss;

public class BigBloatedBoss extends Boss {
    public BigBloatedBoss(String name, int health, int damage) {
        super(name, health, damage);
    }

    @Override
    public void specialAttack() {
        teleport();
        System.out.println(getName() + " unleashes a powerful laser beam!");
    }

    @Override
    public void ultimateAttack() {
        System.out.println(getName() + " charges its ultimate attack for a devastating strike!");
        int ultimateDamage = getDamage() * 3;
        System.out.println("Deals " + ultimateDamage + " damage to the player!");

    }

    public void teleport() {
        int newX = (int) (Math.random() * 100); // Example: Random X coordinate within the battlefield
        int newY = (int) (Math.random() * 100); // Example: Random Y coordinate within the battlefield
        System.out.println(getName() + " teleports to (" + newX + ", " + newY + ")");
    }
}
