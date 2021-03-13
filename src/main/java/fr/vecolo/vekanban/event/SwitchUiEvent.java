package fr.vecolo.vekanban.event;

import org.springframework.context.ApplicationEvent;

public class SwitchUiEvent extends ApplicationEvent {
    public SwitchUiEvent(Object source) {
        super(source);
    }
}
