package fr.vecolo.vekanban.app.controllers;

import fr.vecolo.vekanban.app.services.CardLabelServiceImpl;
import fr.vecolo.vekanban.app.services.CardServiceImpl;
import fr.vecolo.vekanban.app.utils.FXMLLoaderHelper;
import fr.vecolo.vekanban.plugin_api.models.Card;
import fr.vecolo.vekanban.plugin_api.models.CardLabel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Controller
@Scope("prototype")
public class CardController {
    private final CardLabelServiceImpl cardLabelService;
    private final CardServiceImpl cardService;
    private final FXMLLoaderHelper fxmlLoaderHelper;
    private final UiController uiController;
    private final Resource popupYN;
    private final Stage popupYNStage = new Stage();
    PopUpYNController popUpYNController;
    @FXML
    private Label cardTitle;
    @FXML
    private Label cardDueDate;
    @FXML
    private FlowPane cardLabelsPane;
    @FXML
    private Label cardAssignee;
    @FXML
    private ImageView cardNotes;
    private Card card;


    @Autowired
    public CardController(CardLabelServiceImpl cardLabelService,
                          CardServiceImpl cardService, FXMLLoaderHelper fxmlLoaderHelper,
                          UiController uiController, @Value("classpath:/fxml/popUpYN.fxml") Resource popupYN) {
        this.cardLabelService = cardLabelService;
        this.cardService = cardService;
        this.fxmlLoaderHelper = fxmlLoaderHelper;
        this.uiController = uiController;
        this.popupYN = popupYN;
    }

    @FXML
    public void initialize() {
    }

    private void setupDeletePopUp() {
        FXMLLoader loader = fxmlLoaderHelper.loadFXML(popupYN);
        popUpYNController = loader.getController();
        popUpYNController.setPopupLabel("Voulez vous vraiment supprimer\ncette carte ?");
        popupYNStage.setScene(new Scene(loader.getRoot()));
        popupYNStage.setResizable(false);
        popupYNStage.initModality(Modality.APPLICATION_MODAL);
    }


    public void setCard(Card card) {
        this.card = card;
        setupCardInfo();
        setupDeletePopUp();
    }

    private void setupCardInfo() {
        this.cardTitle.setText(card.getTitle());
        if (card.getDueDate() != null) {
            this.cardDueDate.setText("Date de fin " + card.getDueDate().toString());
        } else {
            this.cardDueDate.setText("");
        }
        this.cardNotes.setVisible(StringUtils.hasLength(card.getContent()));
        for (CardLabel label : cardLabelService.getAllCardLabelFromCard(card)) {
            Label newLabel = new Label(label.getName());
            newLabel.setPadding(new Insets(5, 5, 5, 5));
            newLabel.getStyleClass().add("h5");
            if (StringUtils.hasLength(label.getColor())) {
                Color color = Color.web(label.getColor());
                String textColor = (color.getRed() * 255 * 0.299 + color.getGreen() * 255 * 0.587 + color.getBlue() * 255 * 0.114) > 186 ? "-darkGray" : "-white";
                newLabel.setStyle("-fx-background-color:" + label.getColor() + "; -fx-text-fill: " + textColor + ";");
            } else {
                newLabel.setStyle("-fx-background-color: -darkGray;");

            }
            cardLabelsPane.getChildren().add(newLabel);
        }
        if (card.getAssignedUser() != null) {
            this.cardAssignee.setText(card.getAssignedUser().getPseudo());
        } else {
            cardAssignee.setText("");
        }
    }

    @FXML
    private void deleteCard() {
        if (popupLaunch()) {
            try {
                cardService.deleteCard(card);
            } catch (Exception ignored) {
            }
        }

        this.uiController.refreshBoardCards();
    }

    private boolean popupLaunch() {
        popupYNStage.showAndWait();
        return popUpYNController.getResponse();
    }

}
