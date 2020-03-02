package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.swings.app.Application;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;

import javax.swing.*;
import java.awt.*;

public class HWS2DView extends JPanel {
    private HadruwavesStudioV2 application;

    public HWS2DView(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        add(Plot.asCurve().title("hello").setLibrary("jfreechart").nodisplay().plot(new double[]{1, 100, 200, 300, 400, 200, 600, 200, 300, 400, 600})
                .toComponent());
    }
}
