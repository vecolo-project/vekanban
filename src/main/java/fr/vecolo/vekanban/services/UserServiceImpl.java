package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.UserRessourceException;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.repositories.UserRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Collection<User> getAllUsers() {
        return IteratorUtils.toList(userRepository.findAll().iterator());
    }

    @Override
    public Optional<User> getUserById(Long id) throws UserRessourceException {
        Optional<User> userFound = userRepository.findById(id);

        if (userFound.isEmpty()) {
            throw new UserRessourceException("User Not Found on id : " + id);
        }
        return userFound;
    }

    @Override
    public Optional<User> findByPseudo(String pseudo) throws UserRessourceException {
        Optional<User> userFound = userRepository.findByPseudo(pseudo);

        if (userFound.isEmpty()) {
            throw new UserRessourceException("User Not Found on pseudo : " + pseudo);
        }
        return userFound;
    }

    @Override
    public User saveOrUpdateUser(User user) throws UserRessourceException {
        return null;
    }

    @Override
    public void deleteUser(User user) throws UserRessourceException {

    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) throws UserRessourceException {
        return Optional.empty();
    }
}
