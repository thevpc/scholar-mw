package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.locks;

import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.locks.components.HWLockManagerComponent;

public class HWSLocksTool extends AbstractToolWindowPanel {

    public HWSLocksTool(HadruwavesStudio studio) {
        super(studio);
        setContent(new JScrollPane(new HWLockManagerComponent(studio)));
    }

}
