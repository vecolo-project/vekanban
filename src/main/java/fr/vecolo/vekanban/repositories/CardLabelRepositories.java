package fr.vecolo.vekanban.repositories;

import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.Card;
import fr.vecolo.vekanban.models.CardStatus;
import fr.vecolo.vekanban.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardLabelRepositories extends JpaRepository<Card, Long> {

    List<Card> findByAssignedBoardAndStatus(Board assignedBoard, CardStatus status);

    List<Card> findByAssignedUserAndAssignedBoard(User assignedUser, Board assignedBoard);
}
