package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.messages;

import net.thevpc.common.props.ObservableList;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.HWTextEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import org.fife.ui.rtextarea.RTextScrollPane;
import net.thevpc.common.msg.Message;

public class HWSMessageTools extends AbstractToolWindowPanel {

    private HWTextEditor messagesArea;

    public HWSMessageTools(HadruwavesStudio studio) {
        super(studio);
        messagesArea = new HWTextEditor(studio,20, 60);
        messagesArea.setType("java");
        messagesArea.setEditable(false);
        RTextScrollPane messagesPane = new RTextScrollPane(messagesArea);
        setContent(messagesPane);
        updateAll(studio.app().messages());
        studio.app().messages().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.eventType()) {
                    case ADD: {
                        appendMessage(event.newValue());
                        break;
                    }
                    default: {
                        updateAll(studio.app().messages());
                    }
                }
            }
        });
    }

    private void updateAll(ObservableList<Message> m) {
        StringBuilder text = new StringBuilder();
        if (m != null) {
            for (Message appMessage : m) {
                if (text.length() > 0) {
                    text.append("\n");
                }
                text.append(appMessage.toString());
            }
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
