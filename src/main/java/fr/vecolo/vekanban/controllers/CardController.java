package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.models.Card;
import fr.vecolo.vekanban.models.CardLabel;
import fr.vecolo.vekanban.services.CardLabelServiceImpl;
import fr.vecolo.vekanban.services.CardServiceImpl;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Controller
@Scope("prototype")
public class CardController {
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
    private final CardLabelServiceImpl cardLabelService;

    @Autowired
    public CardController(CardLabelServiceImpl cardLabelService) {
        this.cardLabelService = cardLabelService;
    }

    @FXML
    public void initialize() {
    }


    public void setCard(Card card) {
        this.card = card;
        setupCardInfo();
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
}
