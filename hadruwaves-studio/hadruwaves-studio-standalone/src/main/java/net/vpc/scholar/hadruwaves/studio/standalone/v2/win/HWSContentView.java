package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.swings.app.Application;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;

import javax.swing.*;
import java.awt.*;

public class HWSContentView extends JPanel {
    private HadruwavesStudioV2 application;
    private HWS3DView hws3DView;
    private HWS2DView hws2DView;

    public HWSContentView(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        JTabbedPane pane = new JTabbedPane();

        pane.addTab("3D View",hws3DView=new HWS3DView(application));
        pane.addTab("2D View",hws2DView=new HWS2DView(application));
        add(pane);
    }

    public HWS3DView getHws3DView() {
        return hws3DView;
    }

    public HWS2DView getHws2DView() {
        return hws2DView;
    }
}
