package fr.vecolo.vekanban.plugin_api.services;

import fr.vecolo.vekanban.plugin_api.exceptions.CardLabelRessourceException;
import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.Card;
import fr.vecolo.vekanban.plugin_api.models.CardLabel;

import java.util.List;

public interface CardLabelService {

    CardLabel getCardLabelById(Long id);

    List<CardLabel> getAllCardLabelFromBoard(Board board);

    CardLabel saveOrUdateCardLabel(CardLabel cardLabel) throws CardLabelRessourceException;

    void deleteCardLabel(CardLabel cardLabel) throws CardLabelRessourceException;

    public List<CardLabel> getAllCardLabelFromCard(Card card);
}
