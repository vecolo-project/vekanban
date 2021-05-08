package fr.vecolo.vekanban.repositories;

import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPseudo(String pseudoParam);
    Optional<User> findByEmail(String email);
    List<User> findAllByBoardsMemberIs(Board boardsMember);
}
