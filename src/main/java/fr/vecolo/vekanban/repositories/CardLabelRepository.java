package fr.vecolo.vekanban.repositories;

import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.CardLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardLabelRepository extends JpaRepository<CardLabel, Long> {
    List<CardLabel> findAllByBoard(Board board);
}
