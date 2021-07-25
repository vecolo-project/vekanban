package fr.vecolo.vekanban.app.controllers;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PluginCardController {

    @FXML
    private Label pluginName;

    @FXML
    private Label pluginDescription;

    private PluginInterface plugin;

    @Autowired
    public PluginCardController() {
    }

    @FXML
    public void initialize() {
    }


    public void setPlugin(PluginInterface plugin) {
        this.plugin = plugin;
        fillCardInfo();
    }

    private void fillCardInfo() {
        this.pluginName.setText(this.plugin.getName());
        this.pluginDescription.setText(this.plugin.getDescription());
    }
}
