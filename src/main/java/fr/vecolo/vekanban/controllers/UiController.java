package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.events.LogoutEvent;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.services.BoardServiceImpl;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Controller
public class UiController {
    @FXML
    private SplitPane root;

    @FXML
    private Label helloLabel;

    @FXML
    private ToggleButton projectButton;

    @FXML
    private ToggleButton newProjectButton;

    @FXML
    private ToggleButton profilButton;

    @FXML
    private StackPane rootStackPane;

    @FXML
    private VBox projectBox;

    @FXML
    private VBox newProjectBox;

    @FXML
    private VBox profilBox;

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
        freezeSplitPaneBar();
        setVisibleBox(projectBox);
        helloLabel.setText("Bonjour " + StringUtils.capitalize(user.getPseudo()));
    }

    private void freezeSplitPaneBar() {
        root.getDividers().get(0).positionProperty().addListener(o -> root.setDividerPosition(0, 0.2));
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void logout() {
        ac.publishEvent(new LogoutEvent(this));
    }

    private void setVisibleBox(VBox box) {
        projectButton.setSelected(box == projectBox);
        newProjectButton.setSelected(box == newProjectBox);
        profilButton.setSelected(box == profilBox);

        projectBox.setVisible(box == projectBox);
        newProjectBox.setVisible(box == newProjectBox);
        profilBox.setVisible(box == profilBox);
    }

    @FXML
    private void changeBox(ActionEvent event) {
        ToggleButton buttonClicked = (ToggleButton) event.getSource();
        if (buttonClicked == projectButton) {
            setVisibleBox(projectBox);
        } else if (buttonClicked == newProjectButton) {
            setVisibleBox(newProjectBox);
        } else if (buttonClicked == profilButton) {
            setVisibleBox(profilBox);
        }
    }
}
