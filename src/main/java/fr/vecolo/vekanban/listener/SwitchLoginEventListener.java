package fr.vecolo.vekanban.listener;

import fr.vecolo.vekanban.config.StageManager;
import fr.vecolo.vekanban.event.SwitchLoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SwitchLoginEventListener implements ApplicationListener<SwitchLoginEvent> {

    private final StageManager stageManager;

    @Autowired
    public SwitchLoginEventListener(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void onApplicationEvent(SwitchLoginEvent source) {
        stageManager.showLoginStage();
    }
}
