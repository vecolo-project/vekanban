package fr.vecolo.vekanban.app.plugins;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class Plugins {
    @Autowired(required = false)
    private List<PluginInterface> pluginInterfaces;

    public void printGreetings() {
        System.out.printf("Found %d extensions for extension point '%s'%n", pluginInterfaces.size(), PluginInterface.class.getName());
        for (PluginInterface pluginInterface : pluginInterfaces) {
            System.out.println(">>> " + pluginInterface.getName());
        }
    }

    public void runAll() throws Exception {
        for (PluginInterface pluginInterface : pluginInterfaces) {
            pluginInterface.run();
        }
    }
}
