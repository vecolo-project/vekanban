package fr.vecolo.vekanban.plugin_api.services;

import fr.vecolo.vekanban.plugin_api.exceptions.UserRessourceException;
import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    List<User> getMembersFromBoard(Board board);

    User getUserById(Long id);

    User findByPseudo(String pseudo);

    User findByEmail(String email);

    User saveOrUpdateUser(User user) throws UserRessourceException;

    void deleteUser(User user) throws UserRessourceException;

    User findByPseudoAndPassword(String pseudo, String password);
}
