package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.view2d;

import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import java.awt.*;

public class HWS2DView extends JPanel {
    private HadruwavesStudio application;

    public HWS2DView(HadruwavesStudio application) {
        super(new BorderLayout());
        this.application = application;
        add(Plot.asCurve().title("hello").setLibrary("jfreechart").nodisplay().plot(new double[]{1, 100, 200, 300, 400, 200, 600, 200, 300, 400, 600})
                .toComponent());
    }
}
