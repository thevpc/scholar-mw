package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.console;

import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.HWTextEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;


public class HWSConsoleTool extends AbstractToolWindowPanel {

    public HWSConsoleTool(HadruwavesStudio studio) {
        super(studio);
        HWTextEditor messagesArea = new HWTextEditor(studio,20, 60);
        messagesArea.setType("java");
        messagesArea.setEditable(false);
        setContent(messagesArea);
    }
}
