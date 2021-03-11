package fr.vecolo.vekanban;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FxBootApplication {
    public static void main(String[] args) {
        Application.launch(VekanbanApplication.class, args);
    }
}
