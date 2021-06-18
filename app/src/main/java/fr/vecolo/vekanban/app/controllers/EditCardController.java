package fr.vecolo.vekanban.app.controllers;

import fr.vecolo.vekanban.app.services.CardLabelServiceImpl;
import fr.vecolo.vekanban.app.services.CardServiceImpl;
import fr.vecolo.vekanban.app.services.UserServiceImpl;
import fr.vecolo.vekanban.app.utils.mdfx.MDFXUtil;
import fr.vecolo.vekanban.plugin_api.exceptions.CardRessourceException;
import fr.vecolo.vekanban.plugin_api.models.Card;
import fr.vecolo.vekanban.plugin_api.models.CardLabel;
import fr.vecolo.vekanban.plugin_api.models.CardStatus;
import fr.vecolo.vekanban.plugin_api.models.User;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EditCardController {
    private final List<User> members = new ArrayList<>();
    private final List<CardLabel> boardLabels = new ArrayList<>();
    private final List<CardLabel> cardLabels = new ArrayList<>();
    private final CardServiceImpl cardService;
    private final UserServiceImpl userService;
    private final CardLabelServiceImpl cardLabelService;
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
                    return "non assignée";
                }
                return object.getPseudo() + " (" + object.getEmail() + ")";
            }

            @Override
            public User fromString(String string) {
                return members.stream().filter(object -> (object.getPseudo() + " (" + object.getEmail() + ")").equals(string)).findFirst().orElse(null);
            }
        });

        cardLabelsCheckComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(CardLabel object) {
                return object.getName();
            }

            @Override
            public CardLabel fromString(String string) {
                return boardLabels.stream().filter(object -> object.getName().equals(string)).findFirst().orElse(null);
            }
        });

        cardStatusChoiceBox.getItems().addAll(CardStatus.TODO, CardStatus.IN_PROGRESS, CardStatus.DONE);

        saveButton.setOnAction(e -> {
            saveUpdatedCard();
            saveButton.getScene().getWindow().hide();
        });
    }

    private void saveUpdatedCard() {
        if (StringUtils.hasLength(cardName.getText())) {
            this.editCard.setTitle(cardName.getText());
            this.editCard.setAssignedUser(cardAssignedUserChoiceBox.getValue());
            this.editCard.setStatus(cardStatusChoiceBox.getValue());
            this.editCard.setDueDate(cardDueDate.getValue());
            this.editCard.setContent(cardDescription.getText());
            this.editCard.setLabels(getSelectedCardLabel());
            try {
                cardService.saveOrUpdateCard(editCard);
            } catch (CardRessourceException e) {
                e.printStackTrace();
            }
        }
    }

    private List<CardLabel> getSelectedCardLabel() {
        List<CardLabel> newCardLabelList = new ArrayList<>();
        for (int i = 0; i < boardLabels.size(); i++) {
            BooleanProperty bp = cardLabelsCheckComboBox.getItemBooleanProperty(i);
            if (bp.get()) {
                newCardLabelList.add(boardLabels.get(i));
            }
        }
        return newCardLabelList;
    }

    public void setCard(Card card) {
        this.editCard = card;
        this.members.clear();
        this.members.addAll(userService.getMembersFromBoard(card.getAssignedBoard()));
        this.members.add(card.getAssignedBoard().getOwner());
        this.members.add(null);
        this.boardLabels.clear();
        this.boardLabels.addAll(cardLabelService.getAllCardLabelFromBoard(card.getAssignedBoard()));
        this.cardLabels.clear();
        this.cardLabels.addAll(cardLabelService.getAllCardLabelFromCard(card));
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
        cardLabelsCheckComboBox.getItems().clear();
        cardLabelsCheckComboBox.getItems().addAll(boardLabels);
        for (int i = 0; i < boardLabels.size(); i++) {
            int finalI = i;
            BooleanProperty bp = cardLabelsCheckComboBox.getItemBooleanProperty(finalI);
            bp.setValue(cardLabels.stream().anyMatch(o -> o.getName().equals(boardLabels.get(finalI).getName())));
        }
    }
}
