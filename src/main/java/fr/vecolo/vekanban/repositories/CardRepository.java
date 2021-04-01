package fr.vecolo.vekanban.repositories;

import fr.vecolo.vekanban.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
