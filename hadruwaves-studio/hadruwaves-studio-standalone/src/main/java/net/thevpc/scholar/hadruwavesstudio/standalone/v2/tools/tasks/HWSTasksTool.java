package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.tasks;

import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.tasks.components.HWTaskMonitorManagerComponent;

public class HWSTasksTool extends AbstractToolWindowPanel {

    public HWSTasksTool(HadruwavesStudio studio) {
        super(studio);
        setContent(new JScrollPane(new HWTaskMonitorManagerComponent(studio)));
    }
}
