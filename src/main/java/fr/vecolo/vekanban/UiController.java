package fr.vecolo.vekanban;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class UiController {

    private final HostServices hostServices;

    @FXML
    public Label label;

    @FXML
    public Button button;

    public UiController(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize() {
        this.button.setOnAction(e -> this.label.setText(hostServices.getDocumentBase()));
    }

}
