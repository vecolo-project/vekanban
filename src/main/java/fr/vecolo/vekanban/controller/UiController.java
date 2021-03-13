package fr.vecolo.vekanban.controller;

import fr.vecolo.vekanban.event.SwitchLoginEvent;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class UiController {

    private final HostServices hostServices;

    @FXML
    public Label label;

    @FXML
    public Button button;

    @FXML
    private CheckComboBox<CheckBox> checkComboBox;

    private final ApplicationContext ac;

    @Autowired
    public UiController(HostServices hostServices, ApplicationContext ac) {
        this.hostServices = hostServices;
        this.ac = ac;
    }

    @FXML
    public void initialize() {
        this.button.setOnAction(e -> buttonCLicked());
        checkComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(CheckBox object) {
                return object.getText();
            }

            @Override
            public CheckBox fromString(String string) {
                return new CheckBox(string);
            }
        });
        fillCheckComboBox();
    }

    private void buttonCLicked() {
        this.label.setText(hostServices.getDocumentBase());
        checkComboBox.getCheckModel().getCheckedItems().forEach(System.out::println);
        ac.publishEvent(new SwitchLoginEvent(this));
    }

    private void fillCheckComboBox() {
        checkComboBox.getItems().clear();
        for (int i = 0; i < 10; i++) {
            checkComboBox.getItems()
                    .add(new CheckBox("Button " + (i + 1)));
        }
    }
}
