package main.game.boss;

public abstract class Boss {
    private String name;
    private int health;
    private int damage;

    public Boss(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void setHealth(int health) {
        this.health = Math.max(health, 0);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
