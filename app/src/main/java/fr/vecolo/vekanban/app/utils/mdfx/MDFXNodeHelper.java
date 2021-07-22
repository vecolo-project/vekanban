package fr.vecolo.vekanban.app.utils.mdfx;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.ext.attributes.AttributeNode;
import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.attributes.AttributesNode;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem;
import com.vladsch.flexmark.ext.tables.*;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;
import com.vladsch.flexmark.util.misc.Extension;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

class MDFXNodeHelper extends VBox {
    final static String ITALICE_CLASS_NAME = "markdown-italic";
    final static String BOLD_CLASS_NAME = "markdown-bold";
    //    private String mdString;
    private final MDFXNode parent;
    private final List<String> elemStyleClass = new LinkedList<>();

    private final List<ConsumerHelper<Pair<Node, String>>> elemFunctions = new LinkedList<>();

    private Boolean nodePerWord = false;

    private final List<String> styles = new LinkedList<>();

    private VBox root = new VBox();

    private GridPane grid = null;
    private int gridx = 0;
    private int gridy = 0;
    private TextFlow flow = null;

    private final int[] currentChapter = new int[6];

    public MDFXNodeHelper(MDFXNode parent, String mdstring) {
        this.parent = parent;

        root.getStyleClass().add("markdown-paragraph-list");
        root.setFillWidth(true);

        LinkedList<Extension> extensions = new LinkedList<>();
        extensions.add(TablesExtension.create());
        extensions.add(AttributesExtension.create());
        extensions.add(TaskListExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();

        Document node = parser.parse(mdstring);

        new MDParser(node).visitor.visitChildren(node);

        this.getChildren().add(root);
    }

    public boolean shouldShowContent() {
        return parent.showChapter(currentChapter);
    }

    public void newParagraph() {
        TextFlow newFlow = new TextFlow();
        newFlow.getStyleClass().add("markdown-normal-flow");
        root.getChildren().add(newFlow);
        flow = newFlow;
    }

    public void addText(String text, String wholeText) {
        if (!text.isEmpty()) {

            Text toAdd = new Text(text);

            toAdd.getStyleClass().add("markdown-text");

            addFeatures(toAdd, wholeText);

            flow.getChildren().add(toAdd);
        }
    }

    public void addFeatures(Node toAdd, String wholeText) {
        for (String elemStyleClass : elemStyleClass) {
            toAdd.getStyleClass().add(elemStyleClass);
        }
        for (ConsumerHelper<Pair<Node, String>> f : elemFunctions) {
            f.accept(new Pair(toAdd, wholeText));
        }
        if (!styles.isEmpty()) {
            String tmp = "";
            for (String style : styles) {
                tmp = tmp + style + ";";
            }
            toAdd.setStyle(toAdd.getStyle() + tmp);
        }
    }

    class MDParser {

        Document document;
        NodeVisitor visitor = new NodeVisitor(
                //new VisitHandler<>(com.vladsch.flexmark.ast.Node.class, this::visit),
                new VisitHandler<>(Code.class, this::visit),
                new VisitHandler<>(Document.class, this::visit),
                new VisitHandler<>(Emphasis.class, this::visit),
                new VisitHandler<>(StrongEmphasis.class, this::visit),
                new VisitHandler<>(FencedCodeBlock.class, this::visit),
                new VisitHandler<>(SoftLineBreak.class, this::visit),
                new VisitHandler<>(HardLineBreak.class, this::visit),
                new VisitHandler<>(Heading.class, this::visit),
                new VisitHandler<>(ListItem.class, this::visit),
                new VisitHandler<>(BulletListItem.class, this::visit),
                new VisitHandler<>(OrderedListItem.class, this::visit),
                new VisitHandler<>(TaskListItem.class, this::visit),
                new VisitHandler<>(BulletList.class, this::visit),
                new VisitHandler<>(OrderedList.class, this::visit),
                new VisitHandler<>(Paragraph.class, this::visit),
                new VisitHandler<>(com.vladsch.flexmark.ast.Image.class, this::visit),
                new VisitHandler<>(Link.class, this::visit),
                new VisitHandler<>(com.vladsch.flexmark.ast.TextBase.class, this::visit),
                new VisitHandler<>(com.vladsch.flexmark.ast.Text.class, this::visit),
                new VisitHandler<>(TableHead.class, this::visit),
                new VisitHandler<>(TableBody.class, this::visit),
                new VisitHandler<>(TableRow.class, this::visit),
                new VisitHandler<>(TableCell.class, this::visit)
        );

        MDParser(Document document) {
            this.document = document;
        }

        public void visit(Code code) {

            Label label = new Label(code.getText().normalizeEndWithEOL());
            label.getStyleClass().add("markdown-code");

            Region bgr1 = new Region();
            bgr1.setManaged(false);
            bgr1.getStyleClass().add("markdown-code-background");
            label.boundsInParentProperty().addListener((p, oldV, newV) -> {
                bgr1.setTranslateX(newV.getMinX() + 2);
                bgr1.setTranslateY(newV.getMinY() - 2);
                bgr1.resize(newV.getWidth() - 4, newV.getHeight() + 4);
            });

            flow.getChildren().add(bgr1);
            flow.getChildren().add(label);

            //visitor.visitChildren(code);
        }

        public void visit(Document document) {
            visitor.visitChildren(document);
        }

        public void visit(Emphasis emphasis) {
            elemStyleClass.add(ITALICE_CLASS_NAME);
            visitor.visitChildren(emphasis);
            elemStyleClass.remove(ITALICE_CLASS_NAME);
        }

        public void visit(StrongEmphasis strongEmphasis) {
            elemStyleClass.add(BOLD_CLASS_NAME);
            visitor.visitChildren(strongEmphasis);
            elemStyleClass.remove(BOLD_CLASS_NAME);
        }

        public void visit(FencedCodeBlock fencedCodeBlock) {

            if (!shouldShowContent()) return;

            Label label = new Label(fencedCodeBlock.getContentChars().toString());
            label.getStyleClass().add("markdown-codeblock");
            VBox vbox = new VBox(label);
            vbox.getStyleClass().add("markdown-codeblock-box");

            root.getChildren().add(vbox);
            //flow.styleClass ::= "markdown-normal-flow"
            //flow <++ new Text("\n")
            //visitor.visitChildren(fencedCodeBlock);
        }

        public void visit(SoftLineBreak softLineBreak) {
            //flow <++ new Text("\n")
            addText(" ", "");
            visitor.visitChildren(softLineBreak);
        }

        public void visit(HardLineBreak hardLineBreak) {
            flow.getChildren().add(new Text("\n"));
            visitor.visitChildren(hardLineBreak);
        }


        public void visit(Heading heading) {

            if (heading.getLevel() == 1 || heading.getLevel() == 2) {
                currentChapter[heading.getLevel()] += 1;

                for (int i = heading.getLevel() + 1; i <= currentChapter.length - 1; i += 1) {
                    currentChapter[i] = 0;
                }
                ;
            }

            if (shouldShowContent()) {
                newParagraph();

                flow.getStyleClass().add("markdown-heading-" + heading.getLevel());
                flow.getStyleClass().add("markdown-heading");

                visitor.visitChildren(heading);
            }
        }


        public void visit(ListItem listItem) {

            if (!shouldShowContent()) return;

            // add new listItem
            VBox oldRoot = root;

            VBox newRoot = new VBox();
            newRoot.getStyleClass().add("markdown-vbox1");
            newRoot.getStyleClass().add("markdown-paragraph-list");
            newRoot.setFillWidth(true);


            Label label = new Label(" --- ");
            if (listItem instanceof BulletListItem) {
                label = new Label(" • ");
            } else if (listItem instanceof OrderedListItem) {
                OrderedListItem item = (OrderedListItem) listItem;
                label = new Label(item.getOpeningMarker().toString());
            } else if (listItem instanceof TaskListItem) {
                TaskListItem item = (TaskListItem) listItem;

                label = new Label(item.isItemDoneMarker() ? "\uD83D\uDDF9" : "☐");
            }


            label.getStyleClass().add("markdown-listitem-dot");
            label.getStyleClass().add("markdown-text");
            label.setMinWidth(20);

            HBox hbox = new HBox();
            hbox.getStyleClass().add("markdown-hbox1");
            hbox.getChildren().add(label);
            hbox.setAlignment(Pos.TOP_LEFT);
            hbox.getChildren().add(newRoot);


            oldRoot.getChildren().add(hbox);

            root = newRoot;

            visitor.visitChildren(listItem);
            root = oldRoot;
        }

        public void visit(BulletList bulletList) {

            if (!shouldShowContent()) return;

            VBox oldRoot = root;
            root = new VBox();
            oldRoot.getChildren().add(root);
            newParagraph();
            flow.getStyleClass().add("markdown-normal-flow");
            visitor.visitChildren(bulletList);
            root = oldRoot;
        }

        public void visit(OrderedList orderedList) {
            VBox oldRoot = root;
            root = new VBox();
            oldRoot.getChildren().add(root);
            newParagraph();
            flow.getStyleClass().add("markdown-normal-flow");
            visitor.visitChildren(orderedList);
            root = oldRoot;
        }

        public void visit(Paragraph paragraph) {
            if (!shouldShowContent()) return;

            List<AttributesNode> atts = AttributesExtension.NODE_ATTRIBUTES.getFrom(document).get(paragraph);
            newParagraph();
            flow.getStyleClass().add("markdown-normal-flow");
            setAttrs(atts, true);
            visitor.visitChildren(paragraph);
            setAttrs(atts, false);
        }

        public void visit(Image image) {
            String url = image.getUrl().toString();
            //System.out.println("imgUrl: " + image.getUrl());
            //System.out.println("img.getUrlContent: " + image.getUrlContent());
            //System.out.println("img.nodeName: " + image.getNodeName());
            Node node = parent.generateImage(url);
            addFeatures(node, "");
            flow.getChildren().add(node);
            //visitor.visitChildren(image);
        }

        public void visit(Link link) {

            LinkedList<Node> nodes = new LinkedList<>();

            ConsumerHelper<Pair<Node, String>> addProp = (pair) -> {
                Node node = pair.getKey();
                String txt = pair.getValue();
                nodes.add(node);

                node.getStyleClass().add("markdown-link");
                parent.setLink(node, link.getUrl().normalizeEndWithEOL(), txt);
            };
            Platform.runLater(() -> {
                BooleanProperty lastValue = new SimpleBooleanProperty(false);
                Runnable updateState = () -> {
                    boolean isHover = false;
                    for (Node node : nodes) {
                        if (node.isHover()) {
                            isHover = true;
                        }
                    }
                    if (isHover != lastValue.get()) {
                        lastValue.set(isHover);
                        for (Node node : nodes) {
                            if (isHover) {
                                node.getStyleClass().add("markdown-link-hover");
                            } else {
                                node.getStyleClass().remove("markdown-link-hover");
                            }

                        }
                        ;
                    }

                };

                for (Node node : nodes) {
                    node.hoverProperty().addListener((p, o, n) -> updateState.run());
                }
                ;
                updateState.run();
            });

            boolean oldNodePerWord = nodePerWord;
            nodePerWord = true;
            elemFunctions.add(addProp);
            visitor.visitChildren(link);
            nodePerWord = oldNodePerWord;
            elemFunctions.remove(addProp);
        }

        public void visit(com.vladsch.flexmark.ast.TextBase text) {
            List<AttributesNode> atts = AttributesExtension.NODE_ATTRIBUTES.getFrom(document).get(text);
            setAttrs(atts, true);
            visitor.visitChildren(text);
            setAttrs(atts, false);
        }

        public void visit(com.vladsch.flexmark.ast.Text text) {
            visitor.visitChildren(text);

            String wholeText = text.getChars().normalizeEOL();

            String[] textsSplitted = null;
            if (nodePerWord) {
                textsSplitted = text.getChars().normalizeEOL().split(" ");
            } else {
                textsSplitted = new String[1];
                textsSplitted[0] = text.getChars().normalizeEOL();
            }
            final String[] textsSplittedFinal = textsSplitted;

            for (int i = 0; i <= textsSplitted.length - 1; i += 1) {
                if (i == 0) {
                    addText(textsSplittedFinal[i], wholeText);
                } else {
                    addText(" " + textsSplittedFinal[i], wholeText);
                }
            }
        }

        public void visit(TableHead customNode) {

            if (!shouldShowContent()) return;

            TextFlow oldFlow = flow;
            grid = new GridPane();
            grid.getStyleClass().add("markdown-table-table");
            gridx = 0;
            gridy = -1;
            root.getChildren().add(grid);

            visitor.visitChildren(customNode);

            for (int i = 1; i <= gridx; i += 1) {
                ColumnConstraints constraint = new ColumnConstraints();
                if (i == gridx) {
                    constraint.setPercentWidth(100.0 * (2.0 / (gridx + 1.0)));
                }
                grid.getColumnConstraints().add(constraint);
            }

            flow = oldFlow;
            newParagraph();
            //flow.styleClass ::= "markdown-normal-flow"
        }

        public void visit(TableBody customNode) {
            if (!shouldShowContent()) return;
            visitor.visitChildren(customNode);
            //} else if(customNode instanceof TableBlock) {
            //  super.visit(customNode);
        }

        public void visit(TableRow customNode) {
            if (customNode.getRowNumber() != 0) {
                gridx = 0;
                gridy += 1;
                visitor.visitChildren(customNode);
            }
        }

        public void visit(TableCell customNode) {
            TextFlow oldFlow = flow;
            flow = new TextFlow();
            flow.getStyleClass().add("markdown-normal-flow");
            TextFlow container = flow;
            //  println("grid: " + grid)
            //  javafx.scene.layout.GridPane.setHgrow(container,Priority.ALWAYS)
            flow.setPrefWidth(9999);
            flow.getStyleClass().add("markdown-table-cell");
            if (gridy == 0) {
                flow.getStyleClass().add("markdown-table-cell-top");
            }
            if (gridy % 2 == 0) {
                flow.getStyleClass().add("markdown-table-odd");
            } else {
                flow.getStyleClass().add("markdown-table-even");
            }
            grid.add(container, gridx, gridy);
            gridx += 1;
            visitor.visitChildren(customNode);
        }

        public void setAttrs(List<AttributesNode> atts, boolean add) {
            if (atts == null) return;

            List<com.vladsch.flexmark.util.ast.Node> atts2 = new LinkedList<>();
            for (AttributesNode att : atts) {
                for (com.vladsch.flexmark.util.ast.Node attChild : att.getChildren()) {
                    atts2.add(attChild);
                }
            }


            List<AttributeNode> atts3 = (List<AttributeNode>) (Object) atts2;

            atts3.forEach(att -> {
                if (att.getName().equalsIgnoreCase("style")) {
                    if (add) styles.add(att.getValue().toString());
                    else styles.remove(att.getValue().toString());
                }
                if (att.isClass()) {
                    if (add) elemStyleClass.add(att.getValue().toString());
                    else elemStyleClass.remove(att.getValue().toString());
                }
            });
        }

    }
}
