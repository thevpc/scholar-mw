package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import net.thevpc.echo.AppIconSet;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import javax.swing.JTree;
import net.thevpc.common.iconset.PIconSet;

public class DefaultAppTreeCellRenderer extends DefaultTreeCellRenderer {

    private HadruwavesStudio studio;

    public DefaultAppTreeCellRenderer(HadruwavesStudio application) {
        this.studio = application;
    }

    public HadruwavesStudio studio() {
        return studio;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        AppIconSet iconSet = studio().app().iconSet();
        if (leaf) {
//            super.setIcon(iconSet.icon("Item").get());
        } else {
            super.setIcon(expanded ? iconSet.icon("OpenFolder").get() : iconSet.icon("Folder").get());
        }
        return c;
    }

    public Color getDisabledColor(boolean sel) {
        return (new Color(120, 120, 120));
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
