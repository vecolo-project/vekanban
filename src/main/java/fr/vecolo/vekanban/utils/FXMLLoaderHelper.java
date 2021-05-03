package fr.vecolo.vekanban.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;


@Component
public class FXMLLoaderHelper {
    private final ApplicationContext applicationContext;

    @Autowired
    public FXMLLoaderHelper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public FXMLLoader loadFXML(Resource resource) {
        try {
            URL url = resource.getURL();
            return loadFXML(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FXMLLoader loadFXML(String ressourcePath) {
        URL url = getClass().getResource("/" + ressourcePath);
        if (url == null) {
            throw new RuntimeException("Can't find FXML file");
        }
        return loadFXML(url);
    }

    public FXMLLoader loadFXML(URL url) {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(url);
        loader.setControllerFactory(applicationContext::getBean);
        try {
            loader.load();
            return loader;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
