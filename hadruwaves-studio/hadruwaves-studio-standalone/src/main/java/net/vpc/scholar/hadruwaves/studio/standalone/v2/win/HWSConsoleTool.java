package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class HWSConsoleTool extends JPanel {
    private HadruwavesStudioV2 application;

    public HWSConsoleTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        RSyntaxTextArea messagesArea = new RSyntaxTextArea(20, 60);
        messagesArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        messagesArea.setCodeFoldingEnabled(true);
        messagesArea.setEditable(false);
        RTextScrollPane messagesPane = new RTextScrollPane(messagesArea);
        add(messagesPane);
    }
}
