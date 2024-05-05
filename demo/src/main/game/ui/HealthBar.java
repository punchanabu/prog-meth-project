package main.game.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HealthBar extends StackPane {
    private static final String HP_LABEL_STYLE = "-fx-text-fill: white; -fx-font-size: 16px;";

    private final ProgressBar progressBar;
    private final Label nameLabel;
    private final Label hpLabel;
    private final int maxHp;
    private int currentHp;

    public HealthBar(String name, int maxHp, Color barColor) {
        this.maxHp = maxHp;
        this.currentHp = maxHp;

        progressBar = createProgressBar(barColor);
        nameLabel = createNameLabel(name);
        hpLabel = createHpLabel();
        updateLabels();

        configureLayout();
    }

    private ProgressBar createProgressBar(Color barColor) {
        ProgressBar progressBar = new ProgressBar(1);
        progressBar.setPrefWidth(250);
        progressBar.setPrefHeight(30);
        progressBar.setStyle("-fx-accent: " + toHex(barColor) + ";");
        return progressBar;
    }

    private Label createNameLabel(String name) {
        Label nameLabel = new Label(name);
        nameLabel.setStyle(HP_LABEL_STYLE);
        nameLabel.setLayoutX(10);
        nameLabel.setLayoutY(2);
        return nameLabel;
    }

    private Label createHpLabel() {
        Label hpLabel = new Label();
        hpLabel.setStyle(HP_LABEL_STYLE);
        return hpLabel;
    }

    private void configureLayout() {
        StackPane.setAlignment(nameLabel, Pos.CENTER_LEFT);
        StackPane.setAlignment(hpLabel, Pos.CENTER_RIGHT);
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