package fr.vecolo.vekanban.plugin_1;

import fr.vecolo.vekanban.plugin_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    private final UserService userService;

    @Autowired
    public SpringConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public GithubPluginService pluginService() {
        return new GithubPluginService();
    }
}
