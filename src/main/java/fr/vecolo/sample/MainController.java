package fr.vecolo.sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.ResourceBundle;

public class MainController {

    private final String javafxVersion = System.getProperty("javafx.version");

    @FXML
    private Label color;

    @FXML
    private Label label;

    @FXML
    private ResourceBundle resources;

    public void initialize() {
        label.setText(String.format(resources.getString("label.text"), "JavaFX", javafxVersion));
    }


    @FXML
    private void updateColor() {
        Random random = new Random();
        this.color.setTextFill(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
    }
}
