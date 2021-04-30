package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.models.*;

import java.util.List;

public interface CardService {
    List<Card> findAllByAssignedBoardAndStatus(Board assignedBoard, CardStatus status);

    List<Card> findAllByAssignedBoardAndLabel(Board assignedBoard);

    List<Card> findAllByAssignedUserAndAssignedBoard(User assignedUser, Board assignedBoard);

    List<Card> findAllByAssignedBoard(Board assignedBoard);

    List<Card> findAllByAssignedUser(User assignedUser);

}
