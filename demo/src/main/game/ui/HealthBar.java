package main.game.ui;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HealthBar extends StackPane {
    private final ProgressBar progressBar;
    private final Label nameLabel;
    private final Label hpLabel;
    private final int maxHp;
    private int currentHp;

    public HealthBar(String name, int maxHp, Color barColor) {
        this.maxHp = maxHp;
        this.currentHp = maxHp;

        progressBar = new ProgressBar(1); // Set initial progress to 1 (full health)
        progressBar.setPrefWidth(250); // Adjust width as needed
        progressBar.setPrefHeight(30); // Adjust height as needed
        progressBar.setStyle("-fx-accent: " + toHex(barColor) + ";"); // Convert the color to hex and apply as style

        nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;"); // Set text color and size

        hpLabel = new Label();
        hpLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;"); // Set text color and size
        nameLabel.setLayoutX(10);  // Set x position relative to the progress bar
        nameLabel.setLayoutY(2);
        updateLabels(); // Initial update for labels

        // Stack labels on top of progress bar
        StackPane.setAlignment(nameLabel, Pos.CENTER_LEFT); // Align name to the left
        StackPane.setAlignment(hpLabel, Pos.CENTER_RIGHT); // Align health status to the right

        // Add nodes to StackPane
        this.getChildren().addAll(progressBar, nameLabel, hpLabel);
    }

    public void update(int currentHp) {
        this.currentHp = currentHp;
        progressBar.setProgress((double) Math.min(currentHp, maxHp) / maxHp);
        updateLabels();
    }

    private void updateLabels() {
        hpLabel.setText("HP: " + currentHp + "/" + maxHp);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
