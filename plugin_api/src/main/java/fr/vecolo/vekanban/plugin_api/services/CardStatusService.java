package fr.vecolo.vekanban.plugin_api.services;

import fr.vecolo.vekanban.plugin_api.models.CardStatus;

import java.util.List;

public interface CardStatusService {

    List<CardStatus> getAllCardStatus();
}
