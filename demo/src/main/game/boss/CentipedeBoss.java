package main.game.boss;

public class CentipedeBoss extends Boss {

    private int numSegments;

    public CentipedeBoss(String name, int health, int damage, int numSegments) {
        super(name, health, damage);
        this.numSegments = numSegments;
    }

    public int getNumSegments() {
        return numSegments;
    }

    @Override
    public void specialAttack() {
        System.out.println(getName() + " launches a barrage of poisonous spikes!");
        int additionalDamage = getNumSegments() * 5; // Assume each segment adds 5 additional damage
        int totalDamage = getDamage() + additionalDamage;
        System.out.println("Additional damage: " + additionalDamage);
        System.out.println("Total damage inflicted: " + totalDamage);
    }

    @Override
    public void ultimateAttack() {
        System.out.println(getName() + " splits into smaller centipedes!");
        int additionalDamage = getNumSegments() * 10;
        int totalDamage = getDamage() + additionalDamage;
        System.out.println("Additional damage: " + additionalDamage);
        System.out.println("Total damage inflicted: " + totalDamage);
    }

    // Special function for CentipedeBoss: Regenerate health by shedding segments
    public void shedSegments(int segmentsToShed) {
        System.out.println(getName() + " sheds " + segmentsToShed + " segments to regenerate health.");
        int healthRegained = segmentsToShed * 10; // Assume each shed segment regains 10 health
        int newHealth = getHealth() + healthRegained;
        setHealth(newHealth); // Update the boss's health
        System.out.println(getName() + "'s health is now " + newHealth);
    }
}
