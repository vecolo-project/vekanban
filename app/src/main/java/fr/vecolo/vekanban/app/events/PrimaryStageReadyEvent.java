package fr.vecolo.vekanban.app.events;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class PrimaryStageReadyEvent extends ApplicationEvent {
    public PrimaryStageReadyEvent(Stage source) {
        super(source);
    }

    public Stage getStage() {
        return (Stage) getSource();
    }
}
