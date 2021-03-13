package fr.vecolo.vekanban.listener;

import fr.vecolo.vekanban.config.StageManager;
import fr.vecolo.vekanban.event.PrimaryStageReadyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PrimaryStageEventListener implements ApplicationListener<PrimaryStageReadyEvent> {

    private final StageManager stageManager;

    @Autowired
    public PrimaryStageEventListener(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void onApplicationEvent(PrimaryStageReadyEvent stageReadyEvent) {
        stageManager.setPrimaryStage(stageReadyEvent.getStage());
        stageManager.showLoginStage();
    }
}
