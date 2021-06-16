package fr.vecolo.vekanban.plugin_api;

import fr.vecolo.vekanban.plugin_api.models.Board;
import org.pf4j.ExtensionPoint;

public interface PluginInterface extends ExtensionPoint {
    String getName();

    String getVersion();

    String getDescription();

    boolean textFieldNeeded();

    void setTextField(String string);

    String getTextField();

    String getTextFieldPrompt();

    boolean textAreaFieldNeeded();

    void setTextAreaField(String string);

    String getTextAreaField();

    String getTextAreaFieldPrompt();

    boolean existingBoardNeeded();

    void setExistingBoard(Board board);

    Board getExistingBoard();

    String getExistingBoardPrompt();

    boolean filePathNeeded();

    void setFilePath(String path);

    String getFilePath();

    String getFilePathPrompt();

    void run() throws Exception;
}
