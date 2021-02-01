package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWUtils;
import net.thevpc.swing.plaf.UIPlaf;
import net.thevpc.swing.plaf.UIPlafManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

public class HWTextEditor extends JPanel {
    private HadruwavesStudio studio;
    private RSyntaxTextArea textArea;
    private RTextScrollPane textScrollPane;

    public HWTextEditor(HadruwavesStudio studio, int rows, int cols) {
        super(new BorderLayout());
        this.studio = studio;
        textArea = new RSyntaxTextArea(rows, cols);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setEditable(true);
        textArea.setWrapStyleWord(true);
        textArea.setCloseMarkupTags(true);
        textArea.setCloseCurlyBraces(true);
        textArea.setHighlightSecondaryLanguages(true);
        textArea.setAutoIndentEnabled(true);
        textArea.setAnimateBracketMatching(true);
        textScrollPane = new RTextScrollPane(textArea);
        add(textScrollPane, BorderLayout.CENTER);
        HWUtils.onLookChanged(studio, () -> onLookChanged());
        onLookChanged();
    }

    private void onLookChanged() {
        Theme theme= null;
        try {
            String id = studio.editorThemes().id().get();
            HWTextEditorTheme t = studio.editorThemes().editorThemes().get(id);
            if(t==null){
                theme = Theme.load(getClass().getClassLoader().getResourceAsStream("org/fife/ui/rsyntaxtextarea/themes/default.xml"));
            }else{
                theme = t.loadTheme();
            }
            if(studio.editorThemes().usePlaf().get()) {
                theme.bgColor = HWUtils.getUIColor(theme.bgColor, "TextArea.background");
                theme.gutterBackgroundColor = theme.bgColor;
                theme.caretColor = HWUtils.getUIColor(theme.caretColor, "TextArea.caretForeground");
                theme.marginLineColor = HWUtils.getUIColor(theme.marginLineColor, "TextArea.background");
                theme.selectionFG = HWUtils.getUIColor(theme.selectionFG, "TextArea.selectionForeground");
                theme.selectionBG = HWUtils.getUIColor(theme.selectionBG, "TextArea.selectionBackground");
                theme.currentLineHighlight = HWUtils.getUIColor(theme.marginLineColor, "Button.toolbar.hoverBackground");
                theme.foldIndicatorFG = HWUtils.getUIColor(theme.foldIndicatorFG, "CheckBox.focus");
                theme.foldBG = HWUtils.getUIColor(theme.foldBG, "armedFoldBG");
                theme.armedFoldBG = HWUtils.getUIColor(theme.armedFoldBG, "TextField.highlight");
                theme.armedFoldBG = HWUtils.getUIColor(theme.armedFoldBG, "TextField.foreground");
                int sc=theme.scheme.getStyleCount();
                UIPlaf plaf = UIPlafManager.INSTANCE.getCurrent();
                boolean invert=false;
                if(plaf!=null && t != null){
                    if(plaf.isDark()) {
                        if (!t.getId().equals("dark")) {
                            invert=true;
                        }
                    }else if(plaf.isLight()){
                        if (t.getId().equals("dark")) {
                            invert=true;
                        }
                    }
                }
                if(invert) {
                    for (int i = 0; i < sc; i++) {
                        Style c = theme.scheme.getStyle(i);
                        if (c.foreground != null) {
                            c.foreground = invertColor(c.foreground);
                        }
                        if (c.background != null) {
                            c.background = invertColor(c.background);
                        }
                        theme.scheme.setStyle(i, c);
                    }
                }
            }
            theme.apply(textArea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Color invertColor(Color c){
        if(c==null){
            return c;
        }
        return new Color(255-c.getRed(),
                255-c.getGreen(),
                255-c.getBlue());
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    public void setEditable(boolean editable) {
        getTextArea().setEditable(editable);
    }

    public void setText(String text) {
        this.textArea.setText(text);
    }

    public void setTypeByFileName(String fileName) {
        if(fileName!=null) {
            if (fileName.endsWith(".hl")) {
                setType("text/hadralang");
            } else if (fileName.endsWith(".tson")) {
                setType("text/tson");
            } else if (fileName.endsWith(".scala")) {
                setType("text/scala");
            } else if (fileName.endsWith(".java")) {
                setType("text/java");
            } else {
                setType(URLConnection.guessContentTypeFromName(fileName));
            }
        }
    }

    public static boolean isSupportedFile(File file) {
        String n = file.getName();
        if (
                n.endsWith(".java")
                        || n.endsWith(".scala")
                        || n.endsWith(".tson")
                        || n.endsWith(".json")
                        || n.endsWith(".xml")
                        || n.endsWith(".txt")
                        || n.endsWith(".hl")
        ) {
            return true;
        }
        return false;
    }
    public void setType(String type) {
//        this.type = type;
        switch (type) {
            case "tson":
            case "java":
            case "text/tson":
            case "text/java": {
                type = RSyntaxTextArea.SYNTAX_STYLE_JAVA;
                break;
            }
            case "hadra":
            case "text/hadra":
            {
                type = RSyntaxTextArea.SYNTAX_STYLE_JAVA;
                break;
            }

            case "scala":
            case "text/scala":
                {
                type = RSyntaxTextArea.SYNTAX_STYLE_SCALA;
                break;
            }
            case "xml":
            case "application/xml":
                {
                type = RSyntaxTextArea.SYNTAX_STYLE_XML;
                break;
            }
            case "json":
            case "application/json":
                {
                type = RSyntaxTextArea.SYNTAX_STYLE_JSON;
                break;
            }
            case "none":
                {
                type = SyntaxConstants.SYNTAX_STYLE_NONE;
                break;
            }
            case "shell": {
                type = SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL;
                break;
            }
        }
        textArea.setSyntaxEditingStyle(type);
    }

    public String getText() {
        return getTextArea().getText();
    }

    public void insertOrReplaceSelection(String text){
        int s1 = getTextArea().getSelectionStart();
        int s2 = getTextArea().getSelectionEnd();
        int cp = getTextArea().getCaretPosition();
        if (s1 >= 0) {
            getTextArea().replaceRange(text, s1, s2);
        } else {
            getTextArea().insert(text, cp);
        }
    }
}
