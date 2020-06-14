/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.components;

import java.awt.Component;
import javax.swing.JTree;

import net.vpc.common.app.AppIconSet;
import net.vpc.common.app.AppPropertiesNode;
import net.vpc.common.app.AppPropertiesNodeFolder;
import net.vpc.common.app.AppPropertiesNodeItem;
import net.vpc.common.iconset.PIconSet;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.HWSProjectPropertiesTool;
import org.jdesktop.swingx.tree.DefaultXTreeCellRenderer;

/**
 *
 * @author vpc
 */
public class PropsTreeCellRenderer extends DefaultXTreeCellRenderer {

    private final HWSProjectPropertiesTool tool;

    public PropsTreeCellRenderer(final HWSProjectPropertiesTool tool) {
        this.tool = tool;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component u = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        AppPropertiesNode item = (AppPropertiesNode) HWSProjectPropertiesTool.toITem(value);
        AppIconSet iconSet = tool.studio().app().iconSet();
        if (item instanceof AppPropertiesNodeItem) {
            setText(((AppPropertiesNodeItem) item).name());
            super.setIcon(null);
        } else if (item instanceof AppPropertiesNodeFolder) {
            setText(((AppPropertiesNodeFolder) item).name());
            super.setIcon(expanded ? iconSet.icon("OpenFolder").get() : iconSet.icon("Folder").get());
        }
        return u;
    }

}
