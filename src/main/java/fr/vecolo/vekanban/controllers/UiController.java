package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.events.LogoutEvent;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.services.BoardServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

import java.util.List;

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

    private User user;
    private final ApplicationEventPublisher ac;
    private final BoardServiceImpl boardService;

    @Autowired
    public UiController(ApplicationEventPublisher ac, BoardServiceImpl boardService) {
        this.ac = ac;
        this.boardService = boardService;
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

    public void setUser(User user) {
        this.user = user;
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
