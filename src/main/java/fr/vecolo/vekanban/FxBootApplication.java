package fr.vecolo.vekanban;

import fr.vecolo.vekanban.config.VekanbanApplication;
import fr.vecolo.vekanban.plugins.Greetings;
import fr.vecolo.vekanban.plugins.PluginConfiguration;
import javafx.application.Application;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class FxBootApplication {
    public static void main(String[] args) {

        Application.launch(VekanbanApplication.class, args);
    }
}
