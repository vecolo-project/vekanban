package fr.vecolo.vekanban.app.services;

import fr.vecolo.vekanban.app.repositories.BoardRepository;
import fr.vecolo.vekanban.plugin_api.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.Card;
import fr.vecolo.vekanban.plugin_api.models.CardLabel;
import fr.vecolo.vekanban.plugin_api.models.User;
import fr.vecolo.vekanban.plugin_api.services.BoardService;
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
    private final CardLabelServiceImpl cardLabelService;
    private final CardServiceImpl cardService;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, CardLabelServiceImpl cardLabelService, CardServiceImpl cardService) {
        this.boardRepository = boardRepository;
        this.cardLabelService = cardLabelService;
        this.cardService = cardService;
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
            logger.error("Board non trouv??. Id :" + id);
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
                    throw new BoardRessourceException("Modification d'un board non pr??sent en base !");
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
        board = getBoardById(board.getId());
        try {
            //Remove all associated members
            board.getMembers().clear();
            boardRepository.save(board);

            // Remove all cards
            for (Card card : cardService.findAllByAssignedBoard(board)) {
                cardService.deleteCard(card);
            }

            // Remove all card labels
            for (CardLabel cardLabel : cardLabelService.getAllCardLabelFromBoard(board)) {
                cardLabelService.deleteCardLabel(cardLabel);
            }

            boardRepository.delete(board);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new BoardRessourceException("Une erreur est survenue lors de la suppression du board");
        }
    }
}
