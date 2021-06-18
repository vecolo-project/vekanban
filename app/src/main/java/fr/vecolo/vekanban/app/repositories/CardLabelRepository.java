package fr.vecolo.vekanban.app.repositories;

import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.Card;
import fr.vecolo.vekanban.plugin_api.models.CardLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardLabelRepository extends JpaRepository<CardLabel, Long> {
    List<CardLabel> findAllByBoard(Board board);

    List<CardLabel> findAllByAssociatedCardsContains(Card associatedCard);
}
