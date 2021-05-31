/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.components;

import net.thevpc.echo.api.AppPropertiesNode;
import net.thevpc.echo.api.AppPropertiesNodeFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.HWSProjectPropertiesTool;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 *
 * @author vpc
 */
public class AppPropertiesNodeFolderTreeTableNode extends DefaultMutableTreeTableNode {

    private final AppPropertiesNodeFolder t;

    public AppPropertiesNodeFolderTreeTableNode(AppPropertiesNodeFolder t,HWSProjectPropertiesTool tool) {
        super(t, true);
        this.t = t;
        for (AppPropertiesNode n : t.children()) {
            add(tool.create(n));
        }
    }

    @Override
    public boolean isEditable(int column) {
        return false;
    }

    @Override
    public int getColumnCount() {
        //                    return 3;
        return 2;
    }

    @Override
    public Object getValueAt(int column) {
        if (column == 0) {
            return t.name();
        }
        return null;
    }

}
