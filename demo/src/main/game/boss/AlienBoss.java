package main.game.boss;

public class AlienBoss extends Boss {
    private int numMinions;
    private int energyLevel;

    public AlienBoss(String name, int health, int damage, int numMinions) {
        super(name, health, damage);
        this.numMinions = numMinions;
        this.energyLevel = 0;
    }

    public int getNumMinions() {
        return numMinions;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    // Special function: Energy Blast
    public void energyBlast() {
        if (energyLevel >= 100) {
            System.out.println(getName() + " unleashes a devastating Energy Blast!");
            int blastDamage = 150; // Adjust damage as needed
            System.out.println("Deals " + blastDamage + " damage to the player!");
            // Reset energy level after using Energy Blast
            energyLevel = 0;
        } else {
            System.out.println(getName() + "'s energy is still charging...");
        }
    }

    @Override
    public void specialAttack() {
        energyLevel += 20;
        System.out.println(getName() + " charges up its energy!");
    }

    @Override
    public void ultimateAttack() {
        System.out.println(getName() + " charges its ultimate attack for a devastating strike!");
        int ultimateDamage = getDamage() * 2;
        System.out.println("Deals " + ultimateDamage + " damage to the player!");
    }
}
