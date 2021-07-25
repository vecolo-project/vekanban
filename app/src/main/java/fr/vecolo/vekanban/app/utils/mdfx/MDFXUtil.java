package fr.vecolo.vekanban.app.utils.mdfx;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;

public class MDFXUtil {

    public static MDFXNode getMDFXNode(String markdown_text) {
        MDFXNode mdfxNode = new MDFXNode((markdown_text));
        mdfxNode.getStylesheets().add("mdfx/mdfx.css");
        mdfxNode.setPadding(new Insets(12, 12, 12, 12));
        return mdfxNode;
    }

    public static void connectMDFXToInput(MDFXNode mdfxNode, StringProperty stringProperty) {
        mdfxNode.mdStringProperty.bind(stringProperty);
    }

    public static ScrollPane connectMDFXToInput(StringProperty stringProperty, int width) {
        MDFXNode node = MDFXUtil.getMDFXNode(stringProperty.toString());
        MDFXUtil.connectMDFXToInput(node, stringProperty);
        ScrollPane content = new ScrollPane(node);
        content.setFitToWidth(true);
        content.setFitToHeight(true);
        if (width > 0) {
            content.setPrefWidth(width);
            content.setMaxWidth(width);
        }
        return content;
    }

    public static ScrollPane createMDFXStaticView(String string) {
        MDFXNode node = MDFXUtil.getMDFXNode(string);
        ScrollPane content = new ScrollPane(node);
        content.setFitToWidth(true);
        content.setFitToHeight(true);
        return content;
    }
}
