package fr.vecolo.vekanban.plugin_1;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

public class GithubPlugin extends SpringPlugin {

    public GithubPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    protected ApplicationContext createApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        SpringPluginManager pluginManager = (SpringPluginManager) getWrapper().getPluginManager();
        applicationContext.setParent(pluginManager.getApplicationContext());
        applicationContext.setClassLoader(getWrapper().getPluginClassLoader());
        applicationContext.register(SpringConfiguration.class);
        applicationContext.refresh();

        return applicationContext;
    }

    @Extension(ordinal = 1)
    @Service
    public static class SpringPlugin implements PluginInterface {

        GithubPluginProvider pluginProvider;
        GithubPluginService pluginService;

        @Autowired
        public SpringPlugin(GithubPluginProvider pluginProvider) {
            this.pluginProvider = pluginProvider;
        }

        @Override
        public String getName() {
            return pluginProvider.getName();
        }

        @Override
        public void run() throws Exception {
            pluginService.retrieveUsers();
        }
    }
}
