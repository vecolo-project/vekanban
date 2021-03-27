package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.models.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Collection<User> getAllUsers();
    Optional<User> getUserById(Long id) throws
}
