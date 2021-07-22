package fr.vecolo.vekanban.app.controllers;

import fr.vecolo.vekanban.plugin_api.PluginInterface;
import fr.vecolo.vekanban.plugin_api.models.Board;
import fr.vecolo.vekanban.plugin_api.models.User;
import fr.vecolo.vekanban.plugin_api.services.BoardService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PluginController {

    @FXML
    private Label pluginName;

    @FXML
    private Label pluginVersion;

    @FXML
    private Label pluginDescription;

    @FXML
    private VBox textFieldBox;

    @FXML
    private Label textFieldPrompt;

    @FXML
    private TextField textField;

    @FXML
    private VBox textAreaBox;

    @FXML
    private Label textAreaPrompt;

    @FXML
    private TextArea textArea;

    @FXML
    private VBox existingBoardBox;

    @FXML
    private Label existingBoardPrompt;

    @FXML
    private ComboBox<Board> existingBoardComboBox;

    @FXML
    private VBox fileNameBox;

    @FXML
    private Label filenamePrompt;

    @FXML
    private TextField filenameField;

    @FXML
    private Button runButton;


    private PluginInterface plugin;
    private User currentUser;
    private List<Board> boardList;
    private final BoardService boardService;

    @Autowired
    public PluginController(BoardService boardService) {
        this.boardService = boardService;
        boardList = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        existingBoardComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Board board) {
                if (board == null) {
                    return "";
                }
                return board.getName();
            }

            @Override
            public Board fromString(String s) {
                return boardList.stream().filter(o -> o.getName().equals(s)).findFirst().orElse(null);
            }
        });
    }


    public void setPlugin(PluginInterface plugin, User user) {
        this.plugin = plugin;
        this.currentUser = user;
        fillBoardList();
        fillPluginStage();
        onChange();
    }

    private void fillBoardList() {
        boardList.clear();
        existingBoardComboBox.getItems().clear();
        boardList.addAll(boardService.getUserOwningBoards(currentUser));
        boardList.addAll(boardService.getUserMemberBoards(currentUser));
        existingBoardComboBox.getItems().addAll(boardList);
    }

    private void fillPluginStage() {
        pluginName.setText(this.plugin.getName());
        pluginVersion.setText(this.plugin.getVersion());
        pluginDescription.setText(this.plugin.getDescription());

        if (plugin.textFieldNeeded()) {
            textFieldBox.setVisible(true);
            textFieldPrompt.setText(this.plugin.getTextFieldPrompt());
            textField.setText("");
        } else {
            textFieldBox.setVisible(false);
        }
        if (plugin.textAreaFieldNeeded()) {
            textAreaBox.setVisible(true);
            textAreaPrompt.setText(this.plugin.getTextAreaFieldPrompt());
            textArea.setText("");
        } else {
            textAreaBox.setVisible(false);
        }
        if (plugin.existingBoardNeeded()) {
            existingBoardBox.setVisible(true);
            fillBoardList();
            existingBoardPrompt.setText(this.plugin.getExistingBoardPrompt());
        } else {
            existingBoardBox.setVisible(false);
        }
        if (plugin.filePathNeeded()) {
            fileNameBox.setVisible(true);
            filenamePrompt.setText(this.plugin.getFilePathPrompt());
            filenameField.setText("");
        } else {
            fileNameBox.setVisible(false);
        }
    }

    @FXML
    protected void onRun() throws Exception {
        if (!checkValidFields()) {
            return;
        }

        plugin.run();
        runButton.getScene().getWindow().hide();
    }

    @FXML
    protected void onChange() {
        this.runButton.setDisable(!checkValidFields());
    }

    private boolean checkValidFields() {
        if (plugin.textFieldNeeded()) {
            if (!StringUtils.hasLength(textField.getText())) {
                return false;
            }
            plugin.setTextField(textField.getText());
        }
        if (plugin.textAreaFieldNeeded()) {
            if (!StringUtils.hasLength(textArea.getText())) {
                return false;
            }
            plugin.setTextAreaField(textArea.getText());
        }
        if (plugin.existingBoardNeeded()) {
            if (existingBoardComboBox.getValue() == null) {
                return false;
            }
            plugin.setExistingBoard(existingBoardComboBox.getValue());
        }
        if (plugin.filePathNeeded()) {
            if (!StringUtils.hasLength(filenameField.getText())) {
                return false;
            }
            plugin.setFilePath(filenameField.getText());
        }
        return true;
    }
}
