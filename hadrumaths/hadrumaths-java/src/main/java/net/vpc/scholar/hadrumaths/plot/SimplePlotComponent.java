package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;
import java.awt.*;

public class SimplePlotComponent extends BasePlotComponent {
    public SimplePlotComponent(JComponent component) {
        super(new BorderLayout());
        add(component);
    }
}
