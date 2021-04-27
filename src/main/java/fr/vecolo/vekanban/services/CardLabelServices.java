package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.CardLabel;

import java.util.List;

public interface CardLabelServices {

    CardLabel getCardLabelById(Long id);

    List<CardLabel> getAllCardLabelFromBoard(Board board);

    CardLabel saveOrUdateCardLabel(CardLabel cardLabel);

    void deleteCardLabel(CardLabel cardLabel);
}
