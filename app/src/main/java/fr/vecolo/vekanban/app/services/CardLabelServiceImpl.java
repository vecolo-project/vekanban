package fr.vecolo.vekanban.app.services;

import fr.vecolo.vekanban.app.repositories.CardLabelRepository;
import fr.vecolo.vekanban.plugin_api.exceptions.CardLabelRessourceException;
import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.Card;
import fr.vecolo.vekanban.plugin_api.models.CardLabel;
import fr.vecolo.vekanban.plugin_api.services.CardLabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardLabelServiceImpl implements CardLabelService {
    private final static Logger logger = LoggerFactory.getLogger(CardLabelServiceImpl.class);

    private final CardLabelRepository cardLabelRepository;
    private final CardServiceImpl cardService;

    @Autowired
    public CardLabelServiceImpl(CardLabelRepository cardLabelRepository, CardServiceImpl cardService) {
        this.cardLabelRepository = cardLabelRepository;
        this.cardService = cardService;
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
    public List<CardLabel> getAllCardLabelFromCard(Card card) {
        return cardLabelRepository.findAllByAssociatedCardsContains(card);
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
        try {
            for (Card card : cardService.findAllByAssignedBoardAndLabelsContaining(cardLabel.getBoard(), cardLabel)) {
                card.setLabels(card.getLabels().stream().filter(o -> o.getId() != cardLabel.getId()).collect(Collectors.toList()));
                cardService.saveOrUpdateCard(card);
            }

            cardLabelRepository.delete(cardLabel);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new CardLabelRessourceException("Une erreur est survenue lors de la suppression d'un label");
        }
    }
}
