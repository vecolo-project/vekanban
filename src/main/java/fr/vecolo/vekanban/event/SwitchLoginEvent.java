package fr.vecolo.vekanban.event;

import org.springframework.context.ApplicationEvent;

public class SwitchLoginEvent extends ApplicationEvent {
    public SwitchLoginEvent(Object source) {
        super(source);
    }
}
