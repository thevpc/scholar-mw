package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.prpbind.PList;
import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.swings.app.AppMessage;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class HWSMessageTools extends JPanel {
    private HadruwavesStudioV2 application;
    private RSyntaxTextArea messagesArea;

    public HWSMessageTools(HadruwavesStudioV2 application0) {
        super(new BorderLayout());
        this.application = application0;

        messagesArea = new RSyntaxTextArea(20, 60);
        messagesArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        messagesArea.setCodeFoldingEnabled(true);
        messagesArea.setEditable(false);
        RTextScrollPane messagesPane = new RTextScrollPane(messagesArea);
        add(messagesPane);
        updateAll(application.getApplication().messages());
        application.getApplication().messages().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()){
                    case ADD:{
                        appendMessage(event.getNewValue());
                        break;
                    }
                    default:{
                        updateAll(application.getApplication().messages());
                    }
                }
            }
        });
    }

    private void updateAll(PList<AppMessage> m) {
        StringBuilder text = new StringBuilder();
        if(m!=null) {
            for (AppMessage appMessage : m) {
                if (text.length() > 0) {
                    text.append("\n");
                }
                text.append(appMessage.toString());
            }
        }
        messagesArea.setText(text.toString());
    }

    private void appendMessage(AppMessage m) {
        StringBuilder text = new StringBuilder(messagesArea.getText());
        if (text.length() > 0) {
            text.append("\n");
        }
        text.append(m.toString());
        messagesArea.setText(text.toString());
    }

}
