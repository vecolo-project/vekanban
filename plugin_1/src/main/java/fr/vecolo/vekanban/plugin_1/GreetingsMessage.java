package fr.vecolo.vekanban.plugin_1;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class GreetingsMessage extends Plugin {

    public GreetingsMessage(PluginWrapper wrapper) {
        super(wrapper);
    }


    @Extension
    public static class WelcomeMessage implements PluginInterface {
        @Override
        public String getName() {
            return "Mon beau plugin 2";
        }
    }

}
