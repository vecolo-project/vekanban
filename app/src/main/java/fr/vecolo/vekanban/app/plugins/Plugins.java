package fr.vecolo.vekanban.app.plugins;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Plugins {
    @Autowired(required = false)
    private List<PluginInterface> pluginInterfaces;

    public void showLoadedPlugins() {
        System.out.printf("Found %d extensions for extension point%n", getPlugins().size());
        for (PluginInterface pluginInterface : getPlugins()) {
            System.out.println(">>> " + pluginInterface.getName() + " - " + pluginInterface.getVersion());
        }
    }

    public List<PluginInterface> getPlugins() {
        return pluginInterfaces != null ? pluginInterfaces : new ArrayList<>();
    }
}
