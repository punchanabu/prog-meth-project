package main.game.item;

public class Sword extends Item {
    public Sword(String name, int value) {
        super(name, value);
    }

    @Override
    public void use() {
        System.out.println("Swinging the sword!");
    }
}
