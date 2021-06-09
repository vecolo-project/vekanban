package fr.vecolo.vekanban.config;

import fr.vecolo.vekanban.FxBootApplication;
import fr.vecolo.vekanban.events.PrimaryStageReadyEvent;
import fr.vecolo.vekanban.plugins.Greetings;
import fr.vecolo.vekanban.plugins.PluginConfiguration;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;



public class VekanbanApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {

        ApplicationContextInitializer<GenericApplicationContext> initializers =
                ac -> {
                    ac.registerBean(Application.class, () -> VekanbanApplication.this);
                    ac.registerBean(Parameters.class, this::getParameters);
                    ac.registerBean(HostServices.class, this::getHostServices);
                };

        loadPlugins();

        this.context = new SpringApplicationBuilder()
                .sources(FxBootApplication.class)
                .initializers(initializers)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    private void loadPlugins() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PluginConfiguration.class);
        Greetings greetings = applicationContext.getBean(Greetings.class);
        greetings.printGreetings();

        SpringPluginManager pluginManager = applicationContext.getBean(SpringPluginManager.class);
        pluginManager.stopPlugins();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.context.publishEvent(new PrimaryStageReadyEvent(primaryStage));
    }

    @Override
    public void stop() throws Exception {
        this.context.close();
        Platform.exit();
    }
}

