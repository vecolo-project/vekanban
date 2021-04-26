package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.models.CardStatus;
import fr.vecolo.vekanban.repositories.CardStatusRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardStatusServiceImpl implements CardStatusService {
    private final static Logger logger = LoggerFactory.getLogger(CardStatusServiceImpl.class);
    private final CardStatusRepository cardStatusRepository;

    @Autowired
    public CardStatusServiceImpl(CardStatusRepository cardStatusRepository) {
        this.cardStatusRepository = cardStatusRepository;
    }

    @Override
    public List<CardStatus> getAllCardStatus() {
        return IteratorUtils.toList(cardStatusRepository.findAll().iterator());
    }
}
