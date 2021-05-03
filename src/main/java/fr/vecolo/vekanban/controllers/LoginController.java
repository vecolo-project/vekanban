package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.events.LoginEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Controller
public class LoginController {

    @FXML
    private TextField passwordField;

    @FXML
    private TextField loginField;

    private final ApplicationEventPublisher ae;

    @Autowired
    public LoginController(ApplicationEventPublisher ac) {
        this.ae = ac;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    void click(ActionEvent event) {
/*
        if (StringUtils.hasText(loginField.getText()) && StringUtils.hasText(passwordField.getText())) {
            this.ae.publishEvent(new LoginEvent(event, loginField.getText(), passwordField.getText()));
        }
*/
        this.ae.publishEvent(new LoginEvent(event, "nospy", "password"));
    }

}
