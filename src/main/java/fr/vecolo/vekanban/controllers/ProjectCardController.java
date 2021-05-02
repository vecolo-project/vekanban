package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.services.BoardService;
import fr.vecolo.vekanban.services.CardService;
import fr.vecolo.vekanban.services.CardServiceImpl;
import fr.vecolo.vekanban.utils.FXMLLoaderHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class ProjectCardController {
    @FXML
    private Label projectTitle;

    @FXML
    private Label boardPrefix;

    @FXML
    private Label boardCreatedDate;

    @FXML
    private Label boardOwner;

    @FXML
    private HBox boardOwnerHbox;

    @FXML
    private Label boardCardCount;

    @FXML
    private Button deleteButton;

    private Board board;

    private final CardService cardService;
    private final BoardService boardService;
    private final UiController uiController;
    private final FXMLLoaderHelper fxmlLoaderHelper;
    private final Resource popupYN;
    private final Stage popupYNStage = new Stage();
    PopUpYNController popUpYNController;

    @Autowired
    public ProjectCardController(CardServiceImpl cardService, BoardService boardService, UiController uiController,
                                 FXMLLoaderHelper fxmlLoaderHelper, @Value("classpath:/fxml/popUpYN.fxml") Resource popupYN) {
        this.cardService = cardService;
        this.boardService = boardService;
        this.uiController = uiController;
        this.fxmlLoaderHelper = fxmlLoaderHelper;
        this.popupYN = popupYN;
    }

    @FXML
    public void initialize() {
    }


    public void setProject(Board board, User user) {
        this.board = board;
        this.projectTitle.setText(board.getName());
        this.boardPrefix.setText(board.getCardIdPrefix());
        this.boardCreatedDate.setText(board.getCreatedAt().toLocalDate().toString());
        this.boardCardCount.setText(cardService.countCardByAssignedBoard(board).toString());
        if (board.getOwner().getId() != user.getId()) {
            boardOwner.setText(board.getOwner().getPseudo());
        } else {
            boardOwnerHbox.setVisible(false);
        }

        this.deleteButton.setDisable(this.board.getOwner().getId() != user.getId());
        FXMLLoader loader = fxmlLoaderHelper.loadFXML(popupYN);
        popUpYNController = loader.getController();
        popUpYNController.setPopupLabel("Voulez vous vraiment supprimer\nle projet " + board.getName() + " ?");
        popupYNStage.setScene(new Scene(loader.getRoot()));
        popupYNStage.setResizable(false);
        popupYNStage.initModality(Modality.APPLICATION_MODAL);

    }

    @FXML
    private void deleteBoard() {
        if (popupLaunch()) {
        try {
            boardService.deleteBoard(board);
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
        }
        uiController.deleteBoardCB();
        }
    }

    private boolean popupLaunch() {
        popupYNStage.showAndWait();
        return popUpYNController.getResponse();
    }
}
