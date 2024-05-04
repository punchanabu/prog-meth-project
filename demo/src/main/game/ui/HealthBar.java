package main.game.ui;

import javafx.scene.control.ProgressBar;

public class HealthBar {
    private final ProgressBar progressBar;
    private int maxHp;
    private int currentHp;


    public HealthBar(int maxHp) {
        progressBar = new ProgressBar(maxHp);
        progressBar.setPrefWidth(250); // Adjust width as needed
        progressBar.setPrefHeight(30); // Adjust height as needed
        progressBar.setStyle("-fx-accent: #D45454;");
        this.maxHp=maxHp;// Set color style (optional)
    }

    public void update(int currentHp) {
        progressBar.setProgress((double)Math.min(currentHp,this.maxHp)/this.maxHp);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
