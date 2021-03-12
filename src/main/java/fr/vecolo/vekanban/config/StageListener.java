package fr.vecolo.vekanban;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private final String appTitle;
    private final Resource fxml;
    private final ApplicationContext applicationContext;
    private Stage primaryStage;

    public StageListener(@Value("${spring.application.ui.title}") String appTitle,
                         @Value("classpath:/ui.fxml") Resource resource,
                         ApplicationContext ac) {
        this.appTitle = appTitle;
        this.fxml = resource;
        this.applicationContext = ac;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        primaryStage = stageReadyEvent.getStage();

        try {
            URL url = this.fxml.getURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.setTitle(appTitle);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ifThisWorks() {
        System.out.println("--------------OMG -------------");
    }
}
