package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.config.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.events.LogoutEvent;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.services.BoardServiceImpl;
import fr.vecolo.vekanban.services.UserServiceImpl;
import fr.vecolo.vekanban.utils.FXMLLoaderHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.lang.reflect.Member;
import java.util.ArrayList;
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

    @FXML
    private TextField newProjectName;

    @FXML
    private TextField newProjectPrefix;

    @FXML
    private TextField newProjectMemberEmail;

    @FXML
    private VBox newProjectMembersBox;

    private final List<String> newProjectMembersEmailList;

    // Profil Box
    @FXML
    private VBox profilBox;

    private User user;
    private final ApplicationEventPublisher ac;
    private final BoardServiceImpl boardService;
    private final UserServiceImpl userService;
    private final FXMLLoaderHelper fxmlLoaderHelper;

    private final Resource projectCardResource;

    @Autowired
    public UiController(ApplicationEventPublisher ac,
                        BoardServiceImpl boardService,
                        UserServiceImpl userService, FXMLLoaderHelper fxmlLoaderHelper,
                        @Value("classpath:/fxml/projectCard.fxml") Resource projectCardResource) {
        this.ac = ac;
        this.boardService = boardService;
        this.userService = userService;
        this.fxmlLoaderHelper = fxmlLoaderHelper;
        this.projectCardResource = projectCardResource;
        newProjectMembersEmailList = new ArrayList<>();
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
        fillBoardList(boardService.getUserOwningBoards(user), projectOwningListBox);

        projectMemberListBox.getChildren().clear();
        fillBoardList(boardService.getUserMemberBoards(user), projectMemberListBox);
    }

    private void fillBoardList(List<Board> boardList, HBox projectMemberListBox) {
        for (Board board : boardList) {
            FXMLLoader fxmlLoader = fxmlLoaderHelper.loadFXML(projectCardResource);
            UiProjectCardController controller = fxmlLoader.getController();
            controller.setProject(board, user);
            VBox card = fxmlLoader.getRoot();
            card.setOnMouseClicked(e -> showBoard(board.getId()));
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

    @FXML
    private void addNewProjectMember() {
        String memberEmail = newProjectMemberEmail.getText();
        if (isValidEmail(memberEmail)) {
            newProjectMembersEmailList.add(newProjectMemberEmail.getText());
            refreshMemberList();
        }
    }

    @FXML
    private void createProject() {
        if (StringUtils.hasLength(newProjectName.getText())) {
            Board board = new Board(newProjectName.getText(), user);
            board.setCardIdPrefix(newProjectPrefix.getText());
            List<User> members = new ArrayList<>();
            for (String memberEmail : newProjectMembersEmailList) {
                User member = userService.findByEmail(memberEmail);
                if (member != null) {
                    members.add(member);
                }
            }
            board.setMembers(members);
            try {
                boardService.saveOrUpdateBoard(board);
                setVisibleBox(projectBox);
            } catch (BoardRessourceException e) {
                e.printStackTrace();
            }
        }
    }

    private void showBoard(long boardId) {
        System.out.println("Showing "+boardId);
    }

    private void refreshMemberList() {
        newProjectMembersBox.getChildren().clear();
        for (String memberEmail : newProjectMembersEmailList) {
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.setPadding(new Insets(10, 10, 0, 10));
            hbox.setAlignment(Pos.CENTER_LEFT);
            Label label = new Label();
            label.setPrefWidth(250);
            label.setText(memberEmail);
            hbox.getChildren().add(label);
            Button button = new Button();
            button.setText("Retirer");
            button.setOnAction(ae -> removeProjectMember(memberEmail));
            hbox.getChildren().add(button);
            newProjectMembersBox.getChildren().add(hbox);
        }
    }

    private void removeProjectMember(String email) {
        newProjectMembersEmailList.remove(email);
        refreshMemberList();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^(.+)@(.+)$");
    }
}
