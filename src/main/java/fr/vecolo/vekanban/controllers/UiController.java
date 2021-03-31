package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.events.LogoutEvent;
import fr.vecolo.vekanban.utils.mdfx.MDFXNode;
import fr.vecolo.vekanban.utils.mdfx.MDFXUtil;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    public TextArea mdtext;

    @FXML
    public HBox mdHbox;

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
        fillMdTextArea();
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

    private void fillMdTextArea() {
        String mdtfx = "# CXML\n" +
                "# Introduction\n" +
                "\n" +
                "XML DTD Validation written in C with GTK interface.\n" +
                "\n" +
                "## Features\n" +
                "\n" +
                "- Parse XML file and check syntax error\n" +
                "- Parse external DTD file and check some of syntax error (not all)\n" +
                "- Handle external DTD whit and without `!DOCTYPE`\n" +
                "- Validate XML file with the given DTD file. Only `element` and `attlist` rules are handled ";

        mdtext.setText(mdtfx);
        mdtext.setMinWidth(750);
        mdHbox.getChildren().add(MDFXUtil.connectMDFXToInput(mdtext.textProperty(), 750));

    }
}
