package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.UserRessourceException;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.repositories.UserRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Transactional
    public Collection<User> getAllUsers() {
        return IteratorUtils.toList(userRepository.findAll().iterator());
    }

    @Override
    @Transactional
    public Optional<User> getUserById(Long id) throws UserRessourceException {
        Optional<User> userFound = userRepository.findById(id);

        if (userFound.isEmpty()) {
            logger.error("User Not Found on id : " + id);
        }
        return userFound;
    }

    @Override
    @Transactional
    public Optional<User> findByPseudo(String pseudo) throws UserRessourceException {
        Optional<User> userFound = userRepository.findByPseudo(pseudo);

        if (userFound.isEmpty()) {
            logger.error("User Not Found on pseudo : " + pseudo);
        }
        return userFound;
    }

    @Override
    @Transactional
    public User saveOrUpdateUser(User user) throws UserRessourceException {
        try {
            if (user.getId() == 0) { //pas d'Id --> création d'un user
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            } else { //sinon, mise à jour d'un user

                Optional<User> userFromDB = getUserById(user.getId());
                if (userFromDB.isEmpty()) {
                    logger.error("Modification d'un utilisateur non présent en base !");
                    return null;
                }

                if (!bCryptPasswordEncoder.matches(user.getPassword(), userFromDB.get().getPassword())) {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));//MAJ du mot de passe s'il a été modifié
                } else {
                    user.setPassword(userFromDB.get().getPassword());//Sinon, on remet le password déjà haché
                }
            }
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Utilisateur non existant", ex);
//            throw new UserRessourceException("DuplicateValueError. Un utilisateur existe déjà avec le compte : " + user.getPseudo());
        } /*catch (UserRessourceException e) {
            logger.error("Utilisateur non existant", e);
            throw new UserRessourceException("UserNotFound. Aucun utilisateur avec l'identifiant: " + user.getId());
        }*/ catch (Exception ex) {
            logger.error("Erreur technique de création ou de mise à jour de l'utilisateur");
//            throw new UserRessourceException("SaveOrUpdateUserError. Erreur technique de création ou de mise à jour de l'utilisateur: " + user.getPseudo());
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws UserRessourceException {

        //TODO delete also all board owning, all assigned card, all board member
        try {
            userRepository.delete(user);
        } catch (EmptyResultDataAccessException ex) {
            logger.error(String.format("Aucun utilisateur n'existe avec l'identifiant: " + user.getId(), ex));
//            throw new UserRessourceException("DeleteUserError: Erreur de suppression de l'utilisateur avec l'identifiant: " + user.getId());
        } catch (Exception ex) {
            logger.error("DeleteUserError: Erreur de suppression de l'utilisateur avec l'identifiant: " + user.getId());
        }
    }

    @Override
    @Transactional
    public Optional<User> findByPseudoAndPassword(String pseudo, String password) throws UserRessourceException {
        try {
            Optional<User> userFound = this.findByPseudo(pseudo);
            if (userFound.isEmpty()) {
                logger.error("Utilisateur non présent en base !");
                return userFound;
            }
            if (bCryptPasswordEncoder.matches(password, userFound.get().getPassword())) {
                return userFound;
            }/* else {
                throw new UserRessourceException("UserNotFound: Mot de passe incorrect");
            }*/
        } catch (UserRessourceException ex) {
            logger.error("Login ou mot de passe incorrect");
//            throw new UserRessourceException(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Une erreur technique est survenue");
//            throw new UserRessourceException("TechnicalError: Une erreur technique est survenue");
        }
        return Optional.empty();
    }
}
