package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.models.*;
import fr.vecolo.vekanban.services.CardLabelServiceImpl;
import fr.vecolo.vekanban.services.CardServiceImpl;
import fr.vecolo.vekanban.services.UserServiceImpl;
import fr.vecolo.vekanban.utils.mdfx.MDFXUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EditCardController {
    @FXML
    private TextField cardName;

    @FXML
    private DatePicker cardDueDate;

    @FXML
    private ChoiceBox<CardStatus> cardStatusChoiceBox;

    @FXML
    private ChoiceBox<User> cardAssignedUserChoiceBox;

    @FXML
    private CheckComboBox<CardLabel> cardLabelsCheckComboBox;

    @FXML
    private HBox cardDescriptionMarkdownHbox;

    @FXML
    private TextArea cardDescription;

    @FXML
    private Button saveButton;

    private Card editCard;
    private final List<User> members = new ArrayList<>();
    private final CardServiceImpl cardService;
    private final UserServiceImpl userService;
    private final CardLabelServiceImpl cardLabelService;

    @Autowired
    public EditCardController(CardServiceImpl cardService, UserServiceImpl userService, CardLabelServiceImpl cardLabelService) {
        this.cardService = cardService;
        this.userService = userService;
        this.cardLabelService = cardLabelService;
    }

    @FXML
    public void initialize() {
        cardDescriptionMarkdownHbox.getChildren().add(MDFXUtil.connectMDFXToInput(cardDescription.textProperty(), 350));

        cardStatusChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(CardStatus object) {
                return object.getStatus();
            }

            @Override
            public CardStatus fromString(String string) {
                switch (string) {
                    case "IN PROGRESS":
                        return CardStatus.IN_PROGRESS;
                    case "DONE":
                        return CardStatus.DONE;
                    default:
                        return CardStatus.TODO;
                }
            }
        });

        cardAssignedUserChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(User object) {
                if (object == null) {
                    return "INCONNU";
                }
                return object.getPseudo() + " (" + object.getEmail() + ")";
            }

            @Override
            public User fromString(String string) {
                return members.stream().filter(object -> (object.getPseudo() + " (" + object.getEmail() + ")").equals(string)).findFirst().orElse(null);
            }
        });

        cardStatusChoiceBox.getItems().addAll(CardStatus.TODO, CardStatus.IN_PROGRESS, CardStatus.DONE);

        saveButton.setOnAction(e -> {
            saveUpdatedCard();
            saveButton.getScene().getWindow().hide();
        });
    }

    private void saveUpdatedCard() {
        System.out.println("Save card");
    }

    public void setCard(Card card) {
        this.editCard = card;
        this.members.clear();
        this.members.addAll(userService.getMembersFromBoard(card.getAssignedBoard()));
        this.members.add(card.getAssignedBoard().getOwner());
        refreshData();
    }

    private void refreshData() {
        cardName.setText(editCard.getTitle());
        cardDueDate.setValue(editCard.getDueDate());
        cardDescription.setText(editCard.getContent());
        cardStatusChoiceBox.setValue(editCard.getStatus());
        cardAssignedUserChoiceBox.getItems().clear();
        cardAssignedUserChoiceBox.getItems().addAll(members);
        cardAssignedUserChoiceBox.setValue(editCard.getAssignedUser());
    }
}
