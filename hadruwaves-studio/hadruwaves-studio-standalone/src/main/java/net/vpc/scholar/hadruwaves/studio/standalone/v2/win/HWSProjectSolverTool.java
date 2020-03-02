package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.swings.app.Application;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;

import javax.swing.*;
import java.awt.*;

public class HWSProjectSolverTool extends JPanel {
    private HadruwavesStudioV2 application;

    public HWSProjectSolverTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        add(new JTree());
    }
}
