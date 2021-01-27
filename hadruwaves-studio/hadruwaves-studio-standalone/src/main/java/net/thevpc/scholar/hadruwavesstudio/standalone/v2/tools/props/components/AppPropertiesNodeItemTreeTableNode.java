/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.components;

import net.thevpc.echo.AppPropertiesNodeItem;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.HWSProjectPropertiesTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.PValueViewPropertyE;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 *
 * @author vpc
 */
public class AppPropertiesNodeItemTreeTableNode extends DefaultMutableTreeTableNode {
    
    private final AppPropertiesNodeItem item;
    private final HWSProjectPropertiesTool tool;

    public AppPropertiesNodeItemTreeTableNode(AppPropertiesNodeItem item, HWSProjectPropertiesTool tool) {
        super(item, false);
        this.item = item;
        this.tool = tool;
    }

    @Override
    public Object getValueAt(int column) {
        switch (column) {
            case 0:
                {
                    return (item).name();
                }
            case 1:
                {
                    if (item instanceof PValueViewPropertyE) {
                        PValueViewPropertyE ee = (PValueViewPropertyE) item;
                        if (ee.getExpressionType().equals(Boolean.class)) {
                            Object s = ee.object();
                            if (s == null || s.toString().isEmpty()) {
                                return true;
                            }
                            if (s.toString().trim().equalsIgnoreCase("true")) {
                                return true;
                            }
                            if (s.toString().trim().equalsIgnoreCase("false")) {
                                return false;
                            }
                        }
                    }
                    return item.object();
                }
                //                        case 2: {
                //                            if (item instanceof PValueViewPropertyE) {
                //                                return item.getEvaluatedValue();
                //                            }
                //                            return null;
                //                        }
        }
        return super.getValueAt(column);
    }

    public int getColumnCount() {
        //                    return 3;
        return 2;
    }

    @Override
    public boolean isEditable(int column) {
        return column == 1 && item.isEditable();
    }

    @Override
    public void setValueAt(Object aValue, int column) {
        switch (column) {
            case 1:
                {
                    item.setValue(aValue);
                    item.tree().refresh();
                    tool.updateRoot();
                    return;
                }
        }
        super.setValueAt(aValue, column);
    }
    
}
