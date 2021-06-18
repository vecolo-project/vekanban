package fr.vecolo.vekanban.app.services;

import fr.vecolo.vekanban.app.repositories.CardRepository;
import fr.vecolo.vekanban.plugin_api.exceptions.CardRessourceException;
import fr.vecolo.vekanban.plugin_api.models.*;
import fr.vecolo.vekanban.plugin_api.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    private final static Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);
    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    @Transactional
    public List<Card> findAllByAssignedBoardAndStatus(Board assignedBoard, CardStatus status) {
        return cardRepository.findAllByAssignedBoardAndStatus(assignedBoard, status);
    }

    @Override
    @Transactional
    public List<Card> findAllByAssignedBoardAndLabelsContaining(Board assignedBoard, CardLabel label) {
        return cardRepository.findByAssignedBoardAndLabelsContaining(assignedBoard, label);
    }

    @Override
    @Transactional
    public List<Card> findAllByAssignedUserAndAssignedBoard(User assignedUser, Board assignedBoard) {
        return cardRepository.findAllByAssignedUserAndAssignedBoard(assignedUser, assignedBoard);
    }

    @Override
    @Transactional
    public List<Card> findAllByAssignedBoard(Board assignedBoard) {
        return cardRepository.findAllByAssignedBoard(assignedBoard);
    }

    @Override
    @Transactional
    public List<Card> findAllByAssignedUser(User assignedUser) {
        return cardRepository.findAllByAssignedUser(assignedUser);
    }

    @Override
    @Transactional
    public Card getCardById(Long id) {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isEmpty()) {
            logger.error("Carte non trouvé. Id:" + id);
            return null;
        }
        return card.get();
    }

    @Override
    @Transactional
    public Card saveOrUpdateCard(Card card) throws CardRessourceException {
        try {
            if (card.getId() != 0) {
                Card cardFromDB = getCardById(card.getId());
                if (cardFromDB == null) {
                    throw new CardRessourceException("Modification d'une carte non présente en base");
                }
            }
            return cardRepository.save(card);
        } catch (DataIntegrityViolationException ex) {
            logger.error(ex.getMessage());
            throw new CardRessourceException("Erreur BDD lors de la sauvegarde d'une carte");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new CardRessourceException("Erreur lors de la sauvegarde d'une carte");
        }
    }

    @Override
    @Transactional
    public void deleteCard(Card card) throws CardRessourceException {
        card = getCardById(card.getId());
        try {
            // Remove associated card labels
            card.getLabels().clear();
            cardRepository.save(card);

            cardRepository.delete(card);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new CardRessourceException("Une erreur est survenue lors de la suppression d'une carte");
        }
    }

    @Override
    public Long countCardByAssignedBoard(Board board) {
        return cardRepository.countCardByAssignedBoard(board);
    }
}
