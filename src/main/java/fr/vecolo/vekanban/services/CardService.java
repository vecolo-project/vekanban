package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.CardRessourceException;
import fr.vecolo.vekanban.models.*;

import java.util.List;

public interface CardService {
    List<Card> findAllByAssignedBoardAndStatus(Board assignedBoard, CardStatus status);

    List<Card> findAllByAssignedBoardAndLabelsContaining(Board assignedBoard, CardLabel label);

    List<Card> findAllByAssignedUserAndAssignedBoard(User assignedUser, Board assignedBoard);

    List<Card> findAllByAssignedBoard(Board assignedBoard);

    List<Card> findAllByAssignedUser(User assignedUser);

    Card getCardById(Long id);

    Card saveOrUpdateCard(Card card) throws CardRessourceException;

    void deleteCard(Card card) throws CardRessourceException;

    Long countCardByAssignedBoard(Board board);

}
