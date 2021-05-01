package fr.vecolo.vekanban.listeners;

import fr.vecolo.vekanban.config.StageManager;
import fr.vecolo.vekanban.events.LoginEvent;
import fr.vecolo.vekanban.models.Board;
import fr.vecolo.vekanban.models.User;
import fr.vecolo.vekanban.services.UserService;
import fr.vecolo.vekanban.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoginEventListener implements ApplicationListener<LoginEvent> {

    private final StageManager stageManager;
    private final UserServiceImpl userService;

    @Autowired
    public LoginEventListener(StageManager stageManager, UserServiceImpl userService) {
        this.stageManager = stageManager;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(LoginEvent source) {
        User user = userService.findByPseudoAndPassword(source.loginPseudo, source.loginPassword);
        if (user != null) {
            stageManager.showUiStage(user);
        }
    }
}
