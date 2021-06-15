package fr.vecolo.vekanban.app.listeners;

import fr.vecolo.vekanban.app.config.StageManager;
import fr.vecolo.vekanban.app.events.LoginEvent;
import fr.vecolo.vekanban.app.services.UserServiceImpl;
import fr.vecolo.vekanban.plugin_api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

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
