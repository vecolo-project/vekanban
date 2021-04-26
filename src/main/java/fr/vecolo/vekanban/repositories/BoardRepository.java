package fr.vecolo.vekanban.repositories;

import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByOwner(User owner);

    List<Board> findByMembersContaining(User member);

    List<Board> findByOwnerOrMembersContaining(User owner, User member);
}
