package fr.vecolo.vekanban.app.repositories;

import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPseudo(String pseudoParam);

    Optional<User> findByEmail(String email);

    List<User> findAllByBoardsMemberIs(Board boardsMember);
}
