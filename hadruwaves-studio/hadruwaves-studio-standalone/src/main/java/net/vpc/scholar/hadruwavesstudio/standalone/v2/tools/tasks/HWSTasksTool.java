package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.tasks;

import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.tasks.components.HWTaskMonitorManagerComponent;

public class HWSTasksTool extends AbstractToolWindowPanel {

    public HWSTasksTool(HadruwavesStudio studio) {
        super(studio);
        setContent(new JScrollPane(new HWTaskMonitorManagerComponent(studio)));
    }
}
