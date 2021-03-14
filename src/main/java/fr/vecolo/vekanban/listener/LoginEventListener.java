package fr.vecolo.vekanban.listener;

import fr.vecolo.vekanban.config.StageManager;
import fr.vecolo.vekanban.event.LoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class LoginEventListener implements ApplicationListener<LoginEvent> {

    private final StageManager stageManager;

    @Autowired
    public LoginEventListener(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void onApplicationEvent(LoginEvent source) {
        stageManager.showUiStage();
    }
}
