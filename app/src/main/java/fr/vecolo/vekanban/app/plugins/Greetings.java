package fr.vecolo.vekanban.app.plugins;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Greetings {
    @Autowired
    private List<PluginInterface> pluginInterfaces;

    public void printGreetings() {
        System.out.printf("Found %d extensions for extension point '%s'%n", pluginInterfaces.size(), PluginInterface.class.getName());
        for (PluginInterface pluginInterface : pluginInterfaces) {
            System.out.println(">>> " + pluginInterface.getName());
        }
    }
}
