package fr.vecolo.vekanban.app.controllers;

import fr.vecolo.vekanban.app.services.BoardServiceImpl;
import fr.vecolo.vekanban.app.services.CardLabelServiceImpl;
import fr.vecolo.vekanban.app.utils.mdfx.MDFXUtil;
import fr.vecolo.vekanban.plugin_api.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.plugin_api.exceptions.CardLabelRessourceException;
import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.CardLabel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EditProjectController {


    private final List<CardLabel> boardLabels = new ArrayList<>();
    private final BoardServiceImpl boardService;
    private final CardLabelServiceImpl cardLabelService;
    @FXML
    private TextField projectName;
    @FXML
    private TextField projectPrefix;
    @FXML
    private TextField projectNewLabelName;
    @FXML
    private VBox projectLabelsList;
    @FXML
    private ColorPicker newLabelColorPicker;
    @FXML
    private HBox projectDescriptionMarkdownHbox;
    @FXML
    private TextArea projectDescription;
    @FXML
    private Button saveButton;
    private Board editBoard;

    @Autowired
    public EditProjectController(BoardServiceImpl boardService, CardLabelServiceImpl cardLabelService) {

        this.boardService = boardService;
        this.cardLabelService = cardLabelService;
    }

    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
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
        refreshProjectLabels(board);
    }

    private void refreshProjectLabels(Board board) {
        boardLabels.clear();
        boardLabels.addAll(cardLabelService.getAllCardLabelFromBoard(board));
        projectLabelsList.getChildren().clear();

        for (CardLabel label : boardLabels) {
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.setPadding(new Insets(5, 10, 0, 10));
            hbox.setAlignment(Pos.CENTER_LEFT);

            Label labelName = new Label(label.getName());
            labelName.setPrefWidth(250);
            hbox.getChildren().add(labelName);

            if (StringUtils.hasLength(label.getColor())) {
                Color color = Color.web(label.getColor());
                String textColor = (color.getRed() * 255 * 0.299 + color.getGreen() * 255 * 0.587 + color.getBlue() * 255 * 0.114) > 186 ? "-darkGray" : "-white";
                labelName.setStyle("-fx-background-color:" + label.getColor() + "; -fx-text-fill: " + textColor + ";");
            }

            Button button = new Button();
            button.setText("Retirer");
            button.setOnAction(ae -> removeProjectLabel(label));
            hbox.getChildren().add(button);

            projectLabelsList.getChildren().add(hbox);
        }
    }

    private void removeProjectLabel(CardLabel label) {
        try {
            cardLabelService.deleteCardLabel(label);
            this.refreshProjectLabels(editBoard);
        } catch (CardLabelRessourceException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void addNewLabel() {
        if (StringUtils.hasLength(projectNewLabelName.getText()) && boardLabels.stream().noneMatch(o -> o.getName().equals(projectNewLabelName.getText()))) {
            CardLabel newLabel = new CardLabel(projectNewLabelName.getText(),
                    toRGBCode(newLabelColorPicker.getValue()),
                    editBoard);
            try {
                cardLabelService.saveOrUdateCardLabel(newLabel);
            } catch (CardLabelRessourceException e) {
                e.printStackTrace();
            }
            projectNewLabelName.clear();
            refreshProjectLabels(editBoard);
        }
    }

}
