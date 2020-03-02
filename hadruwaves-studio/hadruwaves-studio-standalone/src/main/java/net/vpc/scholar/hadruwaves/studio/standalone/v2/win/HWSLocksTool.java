package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.swings.app.Application;
import net.vpc.scholar.hadrumaths.plot.AppLockManagerComponent;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;

import javax.swing.*;
import java.awt.*;

public class HWSLocksTool extends JPanel {
    private HadruwavesStudioV2 application;

    public HWSLocksTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        add(new JScrollPane(new AppLockManagerComponent()));
    }
}
