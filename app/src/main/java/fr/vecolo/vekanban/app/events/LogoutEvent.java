package fr.vecolo.vekanban.app.events;

import org.springframework.context.ApplicationEvent;

public class LogoutEvent extends ApplicationEvent {
    public LogoutEvent(Object source) {
        super(source);
    }
}
