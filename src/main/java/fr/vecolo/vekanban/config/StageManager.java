package fr.vecolo.vekanban.config;

import fr.vecolo.vekanban.util.FXMLLoaderHelper;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class StageManager {
    private final String appTitle;
    private final String loginTitle;
    private final int uiWidth;
    private final int uiHeight;
    private final int loginWidth;
    private final int loginHeight;
    private final Resource fxmlUi;
    private final Resource fxmlLogin;
    private final FXMLLoaderHelper fxmlLoader;
    private Stage primaryStage;


    @Autowired
    public StageManager(
            @Value("${spring.application.ui.title}") String appTitle,
            @Value("${spring.application.login.title}") String loginTitle,
            @Value("${spring.application.ui.width}") int uiWidth,
            @Value("${spring.application.ui.height}") int uiHeight,
            @Value("${spring.application.login.width}") int loginWidth,
            @Value("${spring.application.login.height}") int loginHeight,
            @Value("classpath:/ui.fxml") Resource fxmlUi,
            @Value("classpath:/login.fxml") Resource fxmlLogin,
            FXMLLoaderHelper fxmlLoader) {
        this.appTitle = appTitle;
        this.loginTitle = loginTitle;
        this.uiWidth = uiWidth;
        this.uiHeight = uiHeight;
        this.loginWidth = loginWidth;
        this.loginHeight = loginHeight;
        this.fxmlUi = fxmlUi;
        this.fxmlLogin = fxmlLogin;
        this.fxmlLoader = fxmlLoader;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
    }

    public void showUiStage() {
        primaryStage.close();
        primaryStage.setTitle(appTitle);
        primaryStage.setScene(new Scene(fxmlLoader.loadFXML(fxmlUi), uiWidth, uiHeight));
        primaryStage.show();
    }

    public void showLoginStage() {
        primaryStage.close();
        primaryStage.setTitle(loginTitle);
        primaryStage.setScene(new Scene(fxmlLoader.loadFXML(fxmlLogin), loginWidth, loginHeight));
        primaryStage.show();
    }
}