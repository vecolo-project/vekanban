package fr.vecolo.vekanban.plugin_api.services;

import fr.vecolo.vekanban.plugin_api.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.User;

import java.util.List;

public interface BoardService {

    List<Board> getUserMemberBoards(User user);

    List<Board> getUserOwningBoards(User user);

    List<Board> getUserAllBoards(User user);

    Board getBoardById(Long id);

    Board saveOrUpdateBoard(Board board) throws BoardRessourceException;

    void deleteBoard(Board board) throws BoardRessourceException;
}
