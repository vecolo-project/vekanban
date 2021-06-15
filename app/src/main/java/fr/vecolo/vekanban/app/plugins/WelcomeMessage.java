package fr.vecolo.vekanban.app.plugins;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import org.pf4j.Extension;

@Extension
public class WelcomeMessage implements PluginInterface {

    @Override
    public String getName() {
        return "Mon beau plugin";
    }
}
