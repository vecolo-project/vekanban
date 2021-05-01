package fr.vecolo.vekanban.services;

import fr.vecolo.vekanban.config.exceptions.UserRessourceException;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.Card;
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
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final BoardServiceImpl boardService;
    private final CardServiceImpl cardService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BoardServiceImpl boardService, CardServiceImpl cardService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.boardService = boardService;
        this.cardService = cardService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return IteratorUtils.toList(userRepository.findAll().iterator());
    }

    @Override
    @Transactional
    public User getUserById(Long id) {
        Optional<User> userFound = userRepository.findById(id);

        if (userFound.isEmpty()) {
            logger.error("User Not Found on id : " + id);
            return null;
        }
        return userFound.get();
    }

    @Override
    @Transactional
    public User findByPseudo(String pseudo) {
        Optional<User> userFound = userRepository.findByPseudo(pseudo);

        if (userFound.isEmpty()) {
            logger.error("User Not Found on pseudo : " + pseudo);
            return null;
        }
        return userFound.get();
    }

    @Override
    @Transactional
    public User saveOrUpdateUser(User user) throws UserRessourceException {
        try {
            if (user.getId() == 0) { //pas d'Id --> création d'un user
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            } else { //sinon, mise à jour d'un user

                User userFromDB = getUserById(user.getId());
                if (userFromDB == null) {
                    logger.error("Modification d'un utilisateur non présent en base !");
                    throw new UserRessourceException("Modification d'un utilisateur non présent en base !");
                }

                if (!bCryptPasswordEncoder.matches(user.getPassword(), userFromDB.getPassword())) {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));//MAJ du mot de passe s'il a été modifié
                } else {
                    user.setPassword(userFromDB.getPassword());//Sinon, on remet le password déjà haché
                }
            }
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Utilisateur non existant", ex);
            throw new UserRessourceException("DuplicateValueError. Un utilisateur existe déjà avec le compte : " + user.getPseudo());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error("Erreur technique de création ou de mise à jour de l'utilisateur");
            throw new UserRessourceException("SaveOrUpdateUserError. Erreur technique de création ou de mise à jour de l'utilisateur: " + user.getPseudo());
        }
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws UserRessourceException {
        try {
            for (Board board : boardService.getUserMemberBoards(user)) {
                board.getMembers().remove(user);
                boardService.saveOrUpdateBoard(board);
            }

            for (Card card : cardService.findAllByAssignedUser(user)) {
                card.setAssignedUser(null);
                cardService.saveOrUpdateCard(card);
            }

            for (Board board : boardService.getUserOwningBoards(user)) {
                boardService.deleteBoard(board);
            }

            userRepository.delete(user);
        } catch (EmptyResultDataAccessException ex) {
            logger.error(String.format("Aucun utilisateur n'existe avec l'identifiant: " + user.getId(), ex));
        } catch (Exception ex) {
            logger.error("DeleteUserError: Erreur de suppression de l'utilisateur avec l'identifiant: " + user.getId());
            throw new UserRessourceException("Erreur lors de la suppression d'un utilisateur");
        }
    }

    @Override
    @Transactional
    public User findByPseudoAndPassword(String pseudo, String password) {
        try {
            User userFound = this.findByPseudo(pseudo);
            if (userFound == null) {
                logger.error("Utilisateur non présent en base !");
                return null;
            }
            if (bCryptPasswordEncoder.matches(password, userFound.getPassword())) {
                return userFound;
            }/* else {
                throw new UserRessourceException("UserNotFound: Mot de passe incorrect");
            }*/
        } catch (Exception ex) {
            logger.error("Une erreur technique est survenue");
//            throw new UserRessourceException("TechnicalError: Une erreur technique est survenue");
        }
        return null;
    }
}
