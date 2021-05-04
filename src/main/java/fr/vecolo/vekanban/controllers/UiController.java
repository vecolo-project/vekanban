package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.events.LogoutEvent;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.services.BoardServiceImpl;
import fr.vecolo.vekanban.utils.FXMLLoaderHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.List;

@Controller
public class UiController {
    // Sidebar
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

    // Project box

    @FXML
    private VBox projectBox;

    @FXML
    private HBox projectOwningListBox;

    @FXML
    private HBox projectMemberListBox;

    // New Project Box
    @FXML
    private VBox newProjectBox;

    // Profil Box
    @FXML
    private VBox profilBox;

    private User user;
    private final ApplicationEventPublisher ac;
    private final BoardServiceImpl boardService;
    private final FXMLLoaderHelper fxmlLoaderHelper;

    private final Resource projectCardResource;

    @Autowired
    public UiController(ApplicationEventPublisher ac,
                        BoardServiceImpl boardService,
                        FXMLLoaderHelper fxmlLoaderHelper,
                        @Value("classpath:/fxml/projectCard.fxml") Resource projectCardResource) {
        this.ac = ac;
        this.boardService = boardService;
        this.fxmlLoaderHelper = fxmlLoaderHelper;
        this.projectCardResource = projectCardResource;
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

        if (box == projectBox) {
            fillProjectCards();

        }
    }

    public void deleteBoardCB() {
        setVisibleBox(projectBox);
    }

    private void fillProjectCards() {
        projectOwningListBox.getChildren().clear();
        List<Board> owningProjects = boardService.getUserOwningBoards(user);
        for (Board board : owningProjects) {
            FXMLLoader fxmlLoader = fxmlLoaderHelper.loadFXML(projectCardResource);
            UiProjectCardController controller = fxmlLoader.getController();
            controller.setProject(board, user);
            projectOwningListBox.getChildren().add(fxmlLoader.getRoot());
        }

        projectMemberListBox.getChildren().clear();
        List<Board> memebrProjects = boardService.getUserMemberBoards(user);
        for (Board board : memebrProjects) {
            FXMLLoader fxmlLoader = fxmlLoaderHelper.loadFXML(projectCardResource);
            UiProjectCardController controller = fxmlLoader.getController();
            controller.setProject(board, user);
            projectMemberListBox.getChildren().add(fxmlLoader.getRoot());
        }
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
