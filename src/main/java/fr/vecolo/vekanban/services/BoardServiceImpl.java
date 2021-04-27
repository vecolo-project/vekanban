package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.repositories.BoardRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {
    private final static Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
    private final BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public List<Board> getUserMemberBoards(User user) {
        return boardRepository.findByMembersContaining(user);
    }

    @Override
    @Transactional
    public List<Board> getUserOwningBoards(User user) {
        return boardRepository.findByOwner(user);
    }

    @Override
    @Transactional
    public List<Board> getUserAllBoards(User user) {
        return boardRepository.findByOwnerOrMembersContaining(user, user);
    }

    @Override
    @Transactional
    public Board getBoardById(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            logger.error("Board non trouvé. Id :" + id);
            return null;
        }
        return board.get();
    }

    @Override
    @Transactional
    public Board saveOrUpdateBoard(Board board) throws BoardRessourceException {
        try {
            if (board.getId() != 0) {
                Board boardFromDB = getBoardById(board.getId());
                if (boardFromDB == null) {
                    throw new BoardRessourceException("Modification d'un board non présent en base !");
                }
            }
            return boardRepository.save(board);
        } catch (DataIntegrityViolationException ex) {
            logger.error(ex.getMessage());
            logger.error("Erreur BDD lors de la sauvegarde d'un board");
            throw new BoardRessourceException("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new BoardRessourceException("Erreur lors de la sauvegarde du board.");
        }
    }

    @Override
    @Transactional
    public void deleteBoard(Board board) throws BoardRessourceException {
        //TODO delete associated, labels, members
        try {
            boardRepository.delete(board);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new BoardRessourceException("Une erreur est survenue lors de la suppression du board");
        }
    }
}
