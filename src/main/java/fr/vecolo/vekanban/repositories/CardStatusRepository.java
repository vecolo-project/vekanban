package fr.vecolo.vekanban.repositories;

import fr.vecolo.vekanban.models.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardStatusRepository extends JpaRepository<CardStatus, Long> {
}
