package fr.vecolo.vekanban.app.controllers;

import fr.vecolo.vekanban.app.events.LoginEvent;
import fr.vecolo.vekanban.app.services.UserServiceImpl;
import fr.vecolo.vekanban.plugin_api.exceptions.UserRessourceException;
import fr.vecolo.vekanban.plugin_api.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Controller
public class LoginController {

    private final ApplicationEventPublisher ae;
    private final UserServiceImpl userService;
    @FXML
    private BorderPane loginStack;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private VBox signupStack;
    @FXML
    private TextField profilUserEmail;
    @FXML
    private TextField profilUserPseudo;
    @FXML
    private PasswordField profilUserPassword;
    @FXML
    private PasswordField profilUserConfirmPassword;

    @Autowired
    public LoginController(ApplicationEventPublisher ac, UserServiceImpl userService) {
        this.ae = ac;
        this.userService = userService;
    }

    @FXML
    public void initialize() {
        setVisiblePane(loginStack);
    }

    @FXML
    void login() {
/*
        if (StringUtils.hasText(loginField.getText()) && StringUtils.hasText(passwordField.getText())) {
            this.ae.publishEvent(new LoginEvent(event, loginField.getText(), passwordField.getText()));
        }
*/
        this.ae.publishEvent(new LoginEvent(this, "nospy", "password"));

    }

    @FXML
    void signupForm() {
        setVisiblePane(signupStack);
    }

    @FXML
    void loginForm() {
        setVisiblePane(loginStack);
    }

    @FXML
    void saveUser() {
        if (isValidEmail(profilUserEmail.getText()) &&
                StringUtils.hasLength(profilUserPseudo.getText()) &&
                StringUtils.hasLength(profilUserPassword.getText())
                && profilUserPassword.getText().equals(profilUserConfirmPassword.getText())) {
            User newUser = new User(profilUserEmail.getText(), profilUserPseudo.getText(), profilUserPassword.getText());
            try {
                userService.saveOrUpdateUser(newUser);
                this.ae.publishEvent(new LoginEvent(this, newUser.getPseudo(), profilUserPassword.getText()));
            } catch (UserRessourceException e) {
                e.printStackTrace();
            }
        }
    }

    private void setVisiblePane(Pane pane) {
        loginStack.setVisible(loginStack == pane);
        signupStack.setVisible(signupStack == pane);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^(.+)@(.+)$");
    }

}
