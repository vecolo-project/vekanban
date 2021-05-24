package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.config.exceptions.CardRessourceException;
import fr.vecolo.vekanban.models.*;
import fr.vecolo.vekanban.services.CardLabelServiceImpl;
import fr.vecolo.vekanban.services.CardServiceImpl;
import fr.vecolo.vekanban.services.UserServiceImpl;
import fr.vecolo.vekanban.utils.mdfx.MDFXUtil;
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
public class CreateCardController {
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

    private Card newCard;
    private final List<User> members = new ArrayList<>();
    private final List<CardLabel> boardLabels = new ArrayList<>();
    private final CardServiceImpl cardService;
    private final UserServiceImpl userService;
    private final CardLabelServiceImpl cardLabelService;

    @Autowired
    public CreateCardController(CardServiceImpl cardService, UserServiceImpl userService, CardLabelServiceImpl cardLabelService) {
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
        cardStatusChoiceBox.getItems().addAll(CardStatus.TODO, CardStatus.IN_PROGRESS, CardStatus.DONE);

        saveButton.setOnAction(e -> {
            createNewCard();
            saveButton.getScene().getWindow().hide();
        });
    }

    private void createNewCard() {
        if (StringUtils.hasLength(cardName.getText())) {
            this.newCard.setTitle(cardName.getText());
            this.newCard.setAssignedUser(cardAssignedUserChoiceBox.getValue());
            this.newCard.setStatus(cardStatusChoiceBox.getValue());
            this.newCard.setDueDate(cardDueDate.getValue());
            this.newCard.setContent(cardDescription.getText());
            this.newCard.setLabels(getSelectedCardLabel());
            try {
                cardService.saveOrUpdateCard(newCard);
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

    public void clearField(Board board) {
        this.newCard = new Card();
        newCard.setAssignedBoard(board);
        this.members.clear();
        this.members.addAll(userService.getMembersFromBoard(board));
        this.members.add(board.getOwner());
        this.members.add(null);
        this.boardLabels.clear();
        this.boardLabels.addAll(cardLabelService.getAllCardLabelFromBoard(board));
        cardAssignedUserChoiceBox.getItems().clear();
        cardAssignedUserChoiceBox.getItems().addAll(members);
        cardAssignedUserChoiceBox.setValue(null);
        cardStatusChoiceBox.setValue(CardStatus.TODO);
        cardLabelsCheckComboBox.getItems().clear();
        cardLabelsCheckComboBox.getItems().addAll(boardLabels);
        cardName.setText("");
        cardDueDate.setValue(null);
        cardDescription.setText("");
    }
}
