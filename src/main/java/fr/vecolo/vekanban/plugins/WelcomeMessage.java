package fr.vecolo.vekanban.plugins;

import org.pf4j.Extension;

@Extension
public class WelcomeMessage implements Greeting {
    @Override
    public String getGreeting() {
        return "welcome";
    }
}
