package fr.vecolo.vekanban.listener;

import fr.vecolo.vekanban.config.StageManager;
import fr.vecolo.vekanban.event.SwitchUiEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SwitchUIEventListener implements ApplicationListener<SwitchUiEvent> {

    private final StageManager stageManager;

    @Autowired
    public SwitchUIEventListener(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void onApplicationEvent(SwitchUiEvent switchUiEvent) {
        stageManager.showUiStage();
    }
}
