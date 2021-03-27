package fr.vecolo.vekanban.controllers;

import fr.vecolo.vekanban.events.LoginEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

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
        System.out.println("Click");
        this.ae.publishEvent(new LoginEvent(event, "loginEmail", "loginPassword"));
    }

}
