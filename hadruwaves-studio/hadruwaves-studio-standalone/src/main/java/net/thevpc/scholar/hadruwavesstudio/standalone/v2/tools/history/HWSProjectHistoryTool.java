package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.history;

import net.thevpc.echo.swing.helpers.list.SwingListComponentModel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import org.jdesktop.swingx.JXList;

import javax.swing.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.thevpc.common.msg.Message;

public class HWSProjectHistoryTool extends AbstractToolWindowPanel {

    public HWSProjectHistoryTool(HadruwavesStudio studio) {
        super(studio);
        JXList view = new JXList(new SwingListComponentModel<Message>(studio.app().history().undoList()));
        setContent(new JScrollPane(view));
    }

}
