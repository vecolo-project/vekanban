package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.events.LogoutEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class UiController {

    @FXML
    public Label label;

    @FXML
    public Button button;

    @FXML
    private CheckComboBox<CheckBox> checkComboBox;

    private final ApplicationEventPublisher ac;

    @Autowired
    public UiController(ApplicationEventPublisher ac) {
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
        checkComboBox.getCheckModel().getCheckedItems().forEach(System.out::println);
        ac.publishEvent(new LogoutEvent(this));
    }

    private void fillCheckComboBox() {
        checkComboBox.getItems().clear();
        for (int i = 0; i < 10; i++) {
            checkComboBox.getItems()
                    .add(new CheckBox("Button " + (i + 1)));
        }
    }
}
