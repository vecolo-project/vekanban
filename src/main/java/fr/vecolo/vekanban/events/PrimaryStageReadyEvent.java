package fr.vecolo.vekanban.events;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class PrimaryStageReadyEvent extends ApplicationEvent {
    public Stage getStage() {
        return (Stage) getSource();
    }

    public PrimaryStageReadyEvent(Stage source) {
        super(source);
    }
}
