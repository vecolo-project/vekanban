package fr.vecolo.vekanban.app;

import fr.vecolo.vekanban.app.config.VekanbanApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"fr.vecolo.vekanban.plugin_api"})
public class FxBootApplication {
    public static void main(String[] args) {

        Application.launch(VekanbanApplication.class, args);
    }
}
