package fr.vecolo.vekanban.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class PopUpYNController {

    @FXML
    private Label popupLabel;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    private boolean response = false;

    @Autowired
    public PopUpYNController() {
    }

    @FXML
    public void initialize() {
        yesButton.setOnAction(e -> {
            yesButton.getScene().getWindow().hide();
            response = true;
        });
        noButton.setOnAction(e -> {
            yesButton.getScene().getWindow().hide();
            response = false;
        });
    }

    public boolean getResponse() {
        return response;
    }

    public void setPopupLabel(String text) {
        this.popupLabel.setText(text);
    }
}
