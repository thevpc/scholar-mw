package net.vpc.scholar.hadrumaths.util.swingext;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 19 oct. 2006 00:17:36
 */
public abstract class JCardPanel extends JPanel {

    public JCardPanel() {
    }

    public JCardPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public JCardPanel(LayoutManager layout) {
        super(layout);
    }

    public JCardPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public abstract void addPage(String id, String title, Icon icon, JComponent c);
}
