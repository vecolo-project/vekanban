package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.UserRessourceException;
import fr.vecolo.vekanban.models.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Collection<User> getAllUsers();

    Optional<User> getUserById(Long id) throws UserRessourceException;

    Optional<User> findByPseudo(String pseudo) throws UserRessourceException;

    User saveOrUpdateUser(User user) throws UserRessourceException;

    void deleteUser(User user) throws UserRessourceException;

    Optional<User> findByLoginAndPassword(String login, String password) throws UserRessourceException;
}
