package fr.vecolo.vekanban.plugin_1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public GithubPluginProvider pluginProvider() {
        return new GithubPluginProvider();
    }

    @Bean
    public GithubPluginService pluginService() {
        return new GithubPluginService();
    }
}
