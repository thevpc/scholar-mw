package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.logs;

import net.thevpc.common.props.PList;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.HWTextEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import net.thevpc.common.msg.Message;


public class HWSLogTool extends AbstractToolWindowPanel {

    private HWTextEditor messagesArea;

    public HWSLogTool(HadruwavesStudio studio) {
        super(studio);
        messagesArea = new HWTextEditor(studio,20, 60);
        messagesArea.setType("java");
        messagesArea.setEditable(false);
        setContent(messagesArea);
        updateAll(studio.app().messages());
        studio.app().logs().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()) {
                    case ADD: {
                        appendMessage(event.getNewValue());
                        break;
                    }
                    default: {
                        updateAll(studio.app().logs());
                    }
                }
            }
        });
    }

    private void updateAll(PList<Message> m) {
        StringBuilder text = new StringBuilder();
        for (Message appMessage : m) {
            if (text.length() > 0) {
                text.append("\n");
            }
            text.append(appMessage.toString());
        }
        messagesArea.setText(text.toString());
    }

    private void appendMessage(Message m) {
        StringBuilder text = new StringBuilder(messagesArea.getText());
        if (text.length() > 0) {
            text.append("\n");
        }
        text.append(m.toString());
        messagesArea.setText(text.toString());
    }

}
