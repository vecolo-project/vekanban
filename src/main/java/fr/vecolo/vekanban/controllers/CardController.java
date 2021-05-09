package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.Card;
import fr.vecolo.vekanban.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class CardController {
    @FXML
    private Label cardTitle;

    @FXML
    private Label cardAssignee;

    private Card card;

    @Autowired
    public CardController() {
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
        if (card.getAssignedUser() != null) {
            this.cardAssignee.setText(card.getAssignedUser().getPseudo());
        } else {
            cardAssignee.setText("");
        }
    }
}
