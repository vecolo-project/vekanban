package fr.vecolo.vekanban.repositories;

import fr.vecolo.vekanban.models.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {

}
