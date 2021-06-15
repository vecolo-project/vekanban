package fr.vecolo.vekanban.app.controllers;

import fr.vecolo.vekanban.app.events.LogoutEvent;
import fr.vecolo.vekanban.app.services.BoardServiceImpl;
import fr.vecolo.vekanban.app.services.CardServiceImpl;
import fr.vecolo.vekanban.app.services.UserServiceImpl;
import fr.vecolo.vekanban.app.utils.FXMLLoaderHelper;
import fr.vecolo.vekanban.app.utils.mdfx.MDFXUtil;
import fr.vecolo.vekanban.plugin_api.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.plugin_api.exceptions.UserRessourceException;
import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.Card;
import fr.vecolo.vekanban.plugin_api.models.CardStatus;
import fr.vecolo.vekanban.plugin_api.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UiController {
    private final EditProjectController editProjectController;
    private final CreateCardController createCardController;
    private final EditCardController editCardController;
    private final List<String> newProjectMembersEmailList;
    private final ApplicationEventPublisher ac;
    private final BoardServiceImpl boardService;

    // Project list box
    private final CardServiceImpl cardService;
    private final UserServiceImpl userService;
    private final FXMLLoaderHelper fxmlLoaderHelper;

    // Project view box
    private final Resource popupYN;
    private final Resource editProjectRessource;
    private final Resource editCardRessource;
    private final Resource createCardRessource;
    private final Resource projectCardResource;
    private final Resource cardResource;
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
    @FXML
    private VBox projectBox;
    @FXML
    private HBox projectOwningListBox;
    @FXML
    private HBox projectMemberListBox;
    @FXML
    private VBox viewProjectBox;
    @FXML
    private Button viewProjectEditButton;
    @FXML
    private Label viewProjectTitle;
    @FXML
    private Label viewProjectCreationDate;
    @FXML
    private AnchorPane viewProjectDescriptionPane;
    @FXML
    private VBox viewProjectMembersBox;
    @FXML
    private TextField viewProjectMemberEmail;
    @FXML
    private HBox viewProjectAddMemberBox;
    @FXML
    private VBox todoBox;
    @FXML
    private VBox doingBox;
    @FXML
    private VBox doneBox;
    private Stage removeUserPopUpStage;
    private PopUpYNController removeUserPopUpController;
    private Stage editProjectPopUpStage;
    private Stage createCardPopUpStage;
    private Stage editCardPopUpStage;
    private Board currentBoard;
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
    @FXML
    private TextArea newProjectDescription;
    @FXML
    private HBox newProjectDescriptionMarkdownHbox;
    // Profil Box
    @FXML
    private VBox profilBox;
    @FXML
    private TextField profilUserEmail;
    @FXML
    private TextField profilUserPseudo;
    @FXML
    private PasswordField profilUserPassword;
    @FXML
    private PasswordField profilUserConfirmPassword;
    private User user;

    @Autowired
    public UiController(CreateCardController createCardController, EditCardController editCardController, ApplicationEventPublisher ac,
                        BoardServiceImpl boardService,
                        UserServiceImpl userService, FXMLLoaderHelper fxmlLoaderHelper,
                        EditProjectController editProjectController,
                        CardServiceImpl cardService,
                        @Value("classpath:/fxml/popUpYN.fxml") Resource popupYN,
                        @Value("classpath:/fxml/editProject.fxml") Resource editProject,
                        @Value("classpath:/fxml/editCard.fxml") Resource editCardRessource,
                        @Value("classpath:/fxml/newCard.fxml") Resource newCardRessource,
                        @Value("classpath:/fxml/projectCard.fxml") Resource projectCardResource,
                        @Value("classpath:/fxml/card.fxml") Resource cardResource) {
        this.createCardController = createCardController;
        this.editCardController = editCardController;
        this.ac = ac;
        this.boardService = boardService;
        this.userService = userService;
        this.fxmlLoaderHelper = fxmlLoaderHelper;
        this.editProjectController = editProjectController;
        this.cardService = cardService;
        this.popupYN = popupYN;
        this.editProjectRessource = editProject;
        this.editCardRessource = editCardRessource;
        this.createCardRessource = newCardRessource;
        this.projectCardResource = projectCardResource;
        this.cardResource = cardResource;
        newProjectMembersEmailList = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        freezeSplitPaneBar();
        setVisibleBox(projectBox);
        helloLabel.setText("Bonjour " + StringUtils.capitalize(user.getPseudo()));

        newProjectDescriptionMarkdownHbox.getChildren().add(MDFXUtil.connectMDFXToInput(newProjectDescription.textProperty(), 350));

        removeUserPopUpStage = new Stage();
        FXMLLoader loader = fxmlLoaderHelper.loadFXML(popupYN);
        removeUserPopUpController = loader.getController();
        removeUserPopUpStage.setScene(new Scene(loader.getRoot()));
        removeUserPopUpStage.setResizable(false);
        removeUserPopUpStage.initModality(Modality.APPLICATION_MODAL);

        editProjectPopUpStage = new Stage();
        loader = fxmlLoaderHelper.loadFXML(editProjectRessource);
        editProjectPopUpStage.setScene(new Scene(loader.getRoot()));
        editProjectPopUpStage.setResizable(false);
        editProjectPopUpStage.initModality(Modality.APPLICATION_MODAL);

        editCardPopUpStage = new Stage();
        loader = fxmlLoaderHelper.loadFXML(editCardRessource);
        editCardPopUpStage.setScene(new Scene(loader.getRoot()));
        editCardPopUpStage.setResizable(false);
        editCardPopUpStage.initModality(Modality.APPLICATION_MODAL);

        createCardPopUpStage = new Stage();
        loader = fxmlLoaderHelper.loadFXML(createCardRessource);
        createCardPopUpStage.setScene(new Scene(loader.getRoot()));
        createCardPopUpStage.setResizable(false);
        createCardPopUpStage.initModality(Modality.APPLICATION_MODAL);
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

    @FXML
    private void deleteAccount() {
        if (this.popupLaunch()) {
            try {
                userService.deleteUser(user);
            } catch (UserRessourceException e) {
                e.printStackTrace();
            }
            ac.publishEvent(new LogoutEvent(this));
        }
    }

    private boolean popupLaunch() {
        removeUserPopUpController.setPopupLabel("Voulez vous vraiment supprimer votre compte ?\nTout les projets dont vous êtes propriétaire\nseront également supprimés");
        removeUserPopUpStage.showAndWait();
        return removeUserPopUpController.getResponse();

    }

    private void setVisibleBox(VBox box) {
        projectButton.setSelected(box == projectBox || box == viewProjectBox);
        newProjectButton.setSelected(box == newProjectBox);
        profilButton.setSelected(box == profilBox);

        projectBox.setVisible(box == projectBox);
        newProjectBox.setVisible(box == newProjectBox);
        profilBox.setVisible(box == profilBox);
        viewProjectBox.setVisible(box == viewProjectBox);

        if (box == projectBox) {
            fillProjectListCards();
        }
        if (box == profilBox) {
            fillUserInfo();
        }
    }

    private void fillUserInfo() {
        profilUserEmail.setText(user.getEmail());
        profilUserPseudo.setText(user.getPseudo());
        profilUserPassword.clear();
        profilUserConfirmPassword.clear();
    }

    public void deleteBoardCB() {
        setVisibleBox(projectBox);
    }

    private void fillProjectListCards() {
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
            creationRefreshMemberList();
        }
    }

    @FXML
    private void addProjectMember() {
        String memberEmail = viewProjectMemberEmail.getText();
        User member = userService.findByEmail(memberEmail);
        if (member != null && currentBoard.getMembers().stream().noneMatch(m -> m.getEmail().equals(member.getEmail()))) {
            currentBoard.getMembers().add(member);
            try {
                boardService.saveOrUpdateBoard(currentBoard);
            } catch (BoardRessourceException e) {
                //TODO popup error
                e.printStackTrace();
            }
            viewProjectMemberEmail.setText("");
            refreshBoardMembers();
        }
    }

    @FXML
    private void createProject() {
        if (StringUtils.hasLength(newProjectName.getText())) {
            Board board = new Board(newProjectName.getText(), user);
            board.setCardIdPrefix(newProjectPrefix.getText());
            board.setDescription(newProjectDescription.getText());
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

    @FXML
    private void editProjectClick() {
        editProjectController.setProject(currentBoard);
        editProjectPopUpStage.showAndWait();
        showBoard(currentBoard.getId());
    }

    @FXML
    private void newCardClick() {
        createCardController.clearField(currentBoard);
        createCardPopUpStage.showAndWait();
        refreshBoardCards();
    }

    @FXML
    private void saveUser() {
        if (isValidEmail(profilUserEmail.getText()) &&
                StringUtils.hasLength(profilUserPseudo.getText())) {
            user.setEmail(profilUserEmail.getText());
            user.setPseudo(profilUserPseudo.getText());
            if (StringUtils.hasLength(profilUserPassword.getText()) &&
                    profilUserPassword.getText().equals(profilUserConfirmPassword.getText())) {
                user.setPassword(profilUserPassword.getText());
            }
            try {
                userService.saveOrUpdateUser(user);
            } catch (UserRessourceException e) {
                e.printStackTrace();
            }
            setVisibleBox(projectBox);
        }
    }

    private void showBoard(long boardId) {
        setVisibleBox(viewProjectBox);
        currentBoard = boardService.getBoardById(boardId);
        currentBoard.setMembers(userService.getMembersFromBoard(currentBoard));
        viewProjectTitle.setText(currentBoard.getName());
        viewProjectCreationDate.setText(currentBoard.getCreatedAt().toLocalDate().toString());
        viewProjectDescriptionPane.getChildren().clear();
        viewProjectDescriptionPane.getChildren().add(MDFXUtil.createMDFXStaticView(currentBoard.getDescription()));
        viewProjectAddMemberBox.setVisible(currentBoard.getOwner().getId() == user.getId());
        viewProjectEditButton.setVisible(currentBoard.getOwner().getId() == user.getId());
        refreshBoardMembers();
        refreshBoardCards();
    }

    private void refreshBoardMembers() {

        viewProjectMembersBox.getChildren().clear();
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 0, 10));
        hbox.setAlignment(Pos.CENTER_LEFT);

        Label ownerL = new Label();
        ownerL.setPadding(new Insets(5, 5, 5, 5));
        ownerL.setText(currentBoard.getOwner().getPseudo() + " (" + currentBoard.getOwner().getEmail() + ") Propriétaire");

        hbox.getChildren().add(ownerL);
        viewProjectMembersBox.getChildren().add(hbox);

        for (User member : currentBoard.getMembers()) {
            hbox = new HBox();
            hbox.setSpacing(10);
            hbox.setPadding(new Insets(10, 10, 0, 10));
            hbox.setAlignment(Pos.CENTER_LEFT);

            Label memberL = new Label();
            memberL.setPadding(new Insets(5, 5, 5, 5));
            memberL.setText(member.getPseudo() + " (" + member.getEmail() + ")");
            memberL.setPrefWidth(250);

            hbox.getChildren().add(memberL);
            if (currentBoard.getOwner().getId() == user.getId()) {
                Button button = new Button();
                button.setText("Retirer");
                button.setOnAction(ae -> removeProjectMember(member));
                hbox.getChildren().add(button);
            }

            viewProjectMembersBox.getChildren().add(hbox);
        }
    }

    public void refreshBoardCards() {
        todoBox.getChildren().clear();
        fillCardList(todoBox, cardService.findAllByAssignedBoardAndStatus(currentBoard, CardStatus.TODO));
        doingBox.getChildren().clear();
        fillCardList(doingBox, cardService.findAllByAssignedBoardAndStatus(currentBoard, CardStatus.IN_PROGRESS));
        doneBox.getChildren().clear();
        fillCardList(doneBox, cardService.findAllByAssignedBoardAndStatus(currentBoard, CardStatus.DONE));
    }

    private void fillCardList(VBox list, List<Card> cards) {
        for (Card card : cards) {
            FXMLLoader fxmlLoader = fxmlLoaderHelper.loadFXML(cardResource);
            CardController cardController = fxmlLoader.getController();
            cardController.setCard(card);
            ((VBox) fxmlLoader.getRoot()).setOnMouseClicked(e -> editCardClick(card));
            list.getChildren().add(fxmlLoader.getRoot());
        }
    }

    private void editCardClick(Card card) {
        editCardController.setCard(card);
        editCardPopUpStage.showAndWait();
        refreshBoardCards();
    }

    private void removeProjectMember(User member) {
        if (popupLaunch(member)) {
            currentBoard.getMembers().remove(member);
            try {
                boardService.saveOrUpdateBoard(currentBoard);
            } catch (BoardRessourceException e) {
                //TODO popup error
                e.printStackTrace();
            }
            refreshBoardMembers();
        }
    }

    private boolean popupLaunch(User member) {
        removeUserPopUpController.setPopupLabel("Voulez vous vraiment retirer\n" + member.getPseudo() + " du projet ?");
        removeUserPopUpStage.showAndWait();
        return removeUserPopUpController.getResponse();
    }

    private void creationRefreshMemberList() {
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
            button.setOnAction(ae -> removeNewProjectMember(memberEmail));
            hbox.getChildren().add(button);
            newProjectMembersBox.getChildren().add(hbox);
        }
    }

    private void removeNewProjectMember(String email) {
        newProjectMembersEmailList.remove(email);
        creationRefreshMemberList();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^(.+)@(.+)$");
    }
}
