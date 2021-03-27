package fr.vecolo.vekanban.events;

import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {
    public final String loginEmail;
    public final String loginPassword;

    public LoginEvent(Object source, String loginEmail, String loginPassword) {
        super(source);
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
    }
}
