package fr.vecolo.vekanban.repositories;

import fr.vecolo.vekanban.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByAssignedBoardAndStatus(Board assignedBoard, CardStatus status);

    List<Card> findAllByAssignedBoardAndLabels(Board assignedBoard, List<CardLabel> labels);

    List<Card> findAllByAssignedBoardAndLabels(Board assignedBoard, CardLabel labels);

    List<Card> findAllByAssignedUserAndAssignedBoard(User assignedUser, Board assignedBoard);

    List<Card> findAllByAssignedBoard(Board assignedBoard);

}
