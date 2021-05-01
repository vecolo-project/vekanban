package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;

import java.util.List;
import java.util.Optional;

public interface BoardService {

    List<Board> getUserMemberBoards(User user);

    List<Board> getUserOwningBoards(User user);

    List<Board> getUserAllBoards(User user);

    Board getBoardById(Long id);

    Board saveOrUpdateBoard(Board board) throws BoardRessourceException;

    void deleteBoard(Board board) throws BoardRessourceException;
}
