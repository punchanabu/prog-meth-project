package main.game.item;

public class Axe extends Item {
    public Axe(String name, int value) {
        super(name, value);
    }

    @Override
    public void use() {
        System.out.println("Chopping with the axe!");
    }
}