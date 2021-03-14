package fr.vecolo.vekanban.controller;

import fr.vecolo.vekanban.event.LoginEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

    private final ApplicationEventPublisher ac;

    @Autowired
    public LoginController(ApplicationEventPublisher ac) {
        this.ac = ac;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    void click(ActionEvent event) {
        System.out.println("Click");
        this.ac.publishEvent(new LoginEvent(event, "loginEmail", "loginPassword"));
    }

}
