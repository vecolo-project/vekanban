package fr.vecolo.vekanban.app.events;

import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {
    public final String loginPseudo;
    public final String loginPassword;

    public LoginEvent(Object source, String loginPseudo, String loginPassword) {
        super(source);
        this.loginPseudo = loginPseudo;
        this.loginPassword = loginPassword;
    }
}
