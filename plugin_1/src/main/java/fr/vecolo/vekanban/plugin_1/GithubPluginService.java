package fr.vecolo.vekanban.plugin_1;

import fr.vecolo.vekanban.plugin_api.models.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubPluginService {

    @Autowired
    public GithubPluginService() {

    }

    public void run(Board board, String githubUrl) {
        System.out.println("Running retrieve on '" + githubUrl + "' for " + board.getName());
    }
}
