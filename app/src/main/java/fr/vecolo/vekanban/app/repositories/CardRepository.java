package fr.vecolo.vekanban.app.repositories;

import fr.vecolo.vekanban.plugin_api.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByAssignedBoardAndStatus(Board assignedBoard, CardStatus status);

    List<Card> findByAssignedBoardAndLabelsContaining(Board assignedBoard, CardLabel label);

    List<Card> findAllByAssignedUserAndAssignedBoard(User assignedUser, Board assignedBoard);

    List<Card> findAllByAssignedBoard(Board assignedBoard);

    List<Card> findAllByAssignedUser(User assignedUser);

    Long countCardByAssignedBoard(Board assignedBoard);
}
