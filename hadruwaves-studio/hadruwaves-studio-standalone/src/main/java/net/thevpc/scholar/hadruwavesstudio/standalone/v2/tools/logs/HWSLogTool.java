package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.logs;

import net.thevpc.common.props.ObservableList;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.HWTextEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
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
        studio.app().logs().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.eventType()) {
                    case ADD: {
                        appendMessage(event.newValue());
                        break;
                    }
                    default: {
                        updateAll(studio.app().logs());
                    }
                }
            }
        });
    }

    private void updateAll(ObservableList<Message> m) {
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
