package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.config.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.services.BoardServiceImpl;
import fr.vecolo.vekanban.utils.mdfx.MDFXUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Controller
public class EditProjectController {

    @FXML
    private TextField projectName;

    @FXML
    private TextField projectPrefix;

    @FXML
    private HBox projectDescriptionMarkdownHbox;

    @FXML
    private TextArea projectDescription;

    @FXML
    private Button saveButton;

    private Board editBoard;
    private final BoardServiceImpl boardService;

    @Autowired
    public EditProjectController(BoardServiceImpl boardService) {

        this.boardService = boardService;
    }

    @FXML
    public void initialize() {
        projectDescriptionMarkdownHbox.getChildren().add(MDFXUtil.connectMDFXToInput(projectDescription.textProperty(), 350));

        saveButton.setOnAction(e -> {
            if (StringUtils.hasLength(projectName.getText())) {
                saveUpdatedBoard();
                saveButton.getScene().getWindow().hide();
            }
        });
    }

    private void saveUpdatedBoard() {
        editBoard.setName(projectName.getText());
        editBoard.setCardIdPrefix(projectPrefix.getText());
        editBoard.setDescription(projectDescription.getText());
        try {
            boardService.saveOrUpdateBoard(editBoard);
        } catch (BoardRessourceException e) {
            e.printStackTrace();
        }
    }

    public void setProject(Board board) {
        this.editBoard = board;
        refreshData(board);
    }

    private void refreshData(Board board) {
        projectName.setText(board.getName());
        projectPrefix.setText(board.getCardIdPrefix());
        projectDescription.setText(board.getDescription());
    }
}
