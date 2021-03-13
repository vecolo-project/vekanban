package fr.vecolo.vekanban.controller;

import fr.vecolo.vekanban.event.SwitchUiEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

    private final ApplicationContext ac;

    @Autowired
    public LoginController(ApplicationContext ac) {
        this.ac = ac;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    void click(ActionEvent event) {
        System.out.println("Click");
        this.ac.publishEvent(new SwitchUiEvent(event));
    }

}
