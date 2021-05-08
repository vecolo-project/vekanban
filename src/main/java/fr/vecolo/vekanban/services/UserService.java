package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.UserRessourceException;
import fr.vecolo.vekanban.models.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User findByPseudo(String pseudo);

    User findByEmail(String email);

    User saveOrUpdateUser(User user) throws UserRessourceException;

    void deleteUser(User user) throws UserRessourceException;

    User findByPseudoAndPassword(String pseudo, String password);
}
