package fr.vecolo.vekanban.listeners;

import fr.vecolo.vekanban.config.StageManager;
import fr.vecolo.vekanban.events.LogoutEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class LogoutEventListener implements ApplicationListener<LogoutEvent> {

    private final StageManager stageManager;

    @Autowired
    public LogoutEventListener(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void onApplicationEvent(LogoutEvent switchUiEvent) {
        stageManager.showLoginStage();
    }
}
