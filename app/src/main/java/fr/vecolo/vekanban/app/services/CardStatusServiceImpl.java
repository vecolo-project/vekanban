package fr.vecolo.vekanban.app.services;

import fr.vecolo.vekanban.app.repositories.CardStatusRepository;
import fr.vecolo.vekanban.plugin_api.models.CardStatus;
import fr.vecolo.vekanban.plugin_api.services.CardStatusService;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardStatusServiceImpl implements CardStatusService {
    protected final static Logger logger = LoggerFactory.getLogger(CardStatusServiceImpl.class);
    private final CardStatusRepository cardStatusRepository;

    @Autowired
    public CardStatusServiceImpl(CardStatusRepository cardStatusRepository) {
        this.cardStatusRepository = cardStatusRepository;
    }

    @Override
    @Transactional
    public List<CardStatus> getAllCardStatus() {
        return IteratorUtils.toList(cardStatusRepository.findAll().iterator());
    }
}
