package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.scholar.hadruplot.console.TaskMonitorManagerComponent;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;

import javax.swing.*;
import java.awt.*;

public class HWSTasksTool extends JPanel {
    private HadruwavesStudioV2 application;

    public HWSTasksTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        add(new JScrollPane(new TaskMonitorManagerComponent(application.proc().getTaskMonitorManager())));
    }
}
