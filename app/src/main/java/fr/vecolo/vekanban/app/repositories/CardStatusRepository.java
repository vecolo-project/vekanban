package fr.vecolo.vekanban.app.repositories;

import fr.vecolo.vekanban.plugin_api.models.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardStatusRepository extends JpaRepository<CardStatus, Long> {
}
