package fr.vecolo.vekanban.plugin_1;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import fr.vecolo.vekanban.plugin_api.models.Board;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

        GithubPluginService pluginService;

        private String textField;
        private Board board;

        @Autowired
        public SpringPlugin(GithubPluginService pluginService) {
            this.pluginService = pluginService;
        }

        @Override
        public String getName() {
            return "Github issue scrapper";
        }

        @Override
        public String getVersion() {
            return "0.0.1";
        }

        @Override
        public String getDescription() {
            return "Plugin permettant de récupérer les tickets d'un dépôt Github publique et de les rajouter à un des vos projets existants";
        }

        @Override
        public boolean textFieldNeeded() {
            return true;
        }

        @Override
        public void setTextField(String string) {
            this.textField = wrap(string);
        }

        @Override
        public String getTextField() {
            return this.textField;
        }

        @Override
        public String getTextFieldPrompt() {
            return "Entrez le lien (ou 'Utilisateur/Depot') du dépot publique Github";
        }

        @Override
        public void setExistingBoard(Board board) {
            this.board = board;
        }

        @Override
        public Board getExistingBoard() {
            return board;
        }

        @Override
        public String getExistingBoardPrompt() {
            return "Sélectionnez le projet auquel rajouter les tickets du dépot Github";
        }

        @Override
        public boolean textAreaFieldNeeded() {
            return false;
        }

        @Override
        public void setTextAreaField(String string) {
        }

        @Override
        public String getTextAreaField() {
            return null;
        }

        @Override
        public String getTextAreaFieldPrompt() {
            return null;
        }

        @Override
        public boolean existingBoardNeeded() {
            return true;
        }

        @Override
        public boolean filePathNeeded() {
            return false;
        }

        @Override
        public void setFilePath(String path) {

        }

        @Override
        public String getFilePath() {
            return null;
        }

        @Override
        public String getFilePathPrompt() {
            return null;
        }

        @Override
        public void run() throws Exception {
            pluginService.run(board, textField);
        }

        private String wrap(String string) {
            if (string == null) {
                return "";
            }
            return StringUtils.trimWhitespace(string);
        }
    }
}
