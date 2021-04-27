package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.BoardRessourceException;
import fr.vecolo.vekanban.config.exceptions.CardLabelRessourceException;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.CardLabel;
import fr.vecolo.vekanban.repositories.CardLabelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardLabelServicesImpl implements CardLabelServices {
    private final static Logger logger = LoggerFactory.getLogger(CardLabelServicesImpl.class);

    private final CardLabelRepository cardLabelRepository;

    @Autowired
    public CardLabelServicesImpl(CardLabelRepository cardLabelRepository) {
        this.cardLabelRepository = cardLabelRepository;
    }

    @Override
    @Transactional
    public CardLabel getCardLabelById(Long id) {
        Optional<CardLabel> cardLabel = cardLabelRepository.findById(id);
        if (cardLabel.isEmpty()) {
            logger.error("Label introuvable à l'id " + id);
            return null;
        }
        return cardLabel.get();
    }

    @Override
    @Transactional
    public List<CardLabel> getAllCardLabelFromBoard(Board board) {
        return cardLabelRepository.findAllByBoard(board);
    }

    @Override
    @Transactional
    public CardLabel saveOrUdateCardLabel(CardLabel cardLabel) throws CardLabelRessourceException {
        try {
            if (cardLabel.getId() != 0) {
                CardLabel cardLabelFromDB = getCardLabelById(cardLabel.getId());
                if (cardLabelFromDB == null) {
                    throw new CardLabelRessourceException("Modification d'un label non présent en base");
                }
            }
            return cardLabelRepository.save(cardLabel);
        } catch (DataIntegrityViolationException ex) {
            logger.error(ex.getMessage());
            logger.error("Erreur BDD lors de la sauvegarde d'un label");
            throw new CardLabelRessourceException("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new CardLabelRessourceException("Erreur lors de la sauvegarde du label.");
        }
    }

    @Override
    @Transactional
    public void deleteCardLabel(CardLabel cardLabel) throws CardLabelRessourceException {
        //TODO remove car_labels associations
        try {
            cardLabelRepository.delete(cardLabel);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new CardLabelRessourceException("Une erreur est survenue lors de la suppression d'un label");
        }
    }
}
