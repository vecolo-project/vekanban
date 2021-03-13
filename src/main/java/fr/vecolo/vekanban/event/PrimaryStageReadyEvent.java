package fr.vecolo.vekanban.event;

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
