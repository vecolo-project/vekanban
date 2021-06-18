package fr.vecolo.vekanban.plugin_1;

import fr.vecolo.vekanban.plugin_api.services.CardLabelService;
import fr.vecolo.vekanban.plugin_api.services.CardService;
import fr.vecolo.vekanban.plugin_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SpringConfiguration {

    private final UserService userService;
    private final CardService cardService;
    private final CardLabelService cardLabelService;

    @Autowired
    public SpringConfiguration(UserService userService, CardService cardService, CardLabelService cardLabelService) {
        this.userService = userService;
        this.cardService = cardService;
        this.cardLabelService = cardLabelService;
    }

    @Bean
    public GithubPluginService pluginService() throws IOException {
        return new GithubPluginService(userService, cardService, cardLabelService);
    }
}
