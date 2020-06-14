package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.history;

import net.vpc.common.app.swing.core.swing.PListComponentModel;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import org.jdesktop.swingx.JXList;

import javax.swing.*;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.vpc.common.msg.Message;

public class HWSProjectHistoryTool extends AbstractToolWindowPanel {

    public HWSProjectHistoryTool(HadruwavesStudio studio) {
        super(studio);
        JXList view = new JXList(new PListComponentModel<Message>(studio.app().history().undoList()));
        setContent(new JScrollPane(view));
    }

}
