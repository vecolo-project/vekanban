package fr.vecolo.vekanban;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UiController {

    private final HostServices hostServices;

    private final StageListener stageListener;
    @FXML
    public Label label;

    @FXML
    public Button button;

    @Autowired
    public UiController(HostServices hostServices, StageListener stageListener) {
        this.hostServices = hostServices;
        this.stageListener = stageListener;
    }

    @FXML
    public void initialize() {
        this.button.setOnAction(e -> buttonCLicked());
    }

    private void buttonCLicked() {
        this.label.setText(hostServices.getDocumentBase());
        stageListener.ifThisWorks();
    }

}
