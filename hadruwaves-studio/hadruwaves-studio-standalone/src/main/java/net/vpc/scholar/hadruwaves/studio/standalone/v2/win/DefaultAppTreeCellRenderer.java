package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;

import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DefaultAppTreeCellRenderer extends DefaultTreeCellRenderer {
    private HadruwavesStudioV2 application;

    public DefaultAppTreeCellRenderer(HadruwavesStudioV2 application) {
        this.application = application;
    }

    public Color getDisabledColor(boolean sel) {
        return
                (new Color(120, 120, 120));
    }
    public Color getActivatedColor(boolean sel) {
        if (sel) {
            return (new Color(120, 201, 228));
        } else {
//                                    super.setForeground(new Color(34, 8, 85));
//                                    super.setForeground(new Color(255, 8, 255));
            return (new Color(161, 3, 161));
//                                    super.setBackground(new Color(194, 158, 255));
        }
    }
}
