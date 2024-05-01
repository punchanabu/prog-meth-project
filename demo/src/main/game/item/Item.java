package main.game.item;

public abstract class Item {
    private String name;
    private int value;

    public Item(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    // Abstract method to be implemented by subclasses
    public abstract void use();

    // Example of a concrete method
    public void displayInfo() {
        System.out.println("Name: " + getName());
        System.out.println("Value: " + getValue());
    }

}
