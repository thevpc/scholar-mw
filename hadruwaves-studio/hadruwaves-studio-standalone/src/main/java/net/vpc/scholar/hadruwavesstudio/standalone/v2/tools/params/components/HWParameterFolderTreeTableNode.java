/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.components;

import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterFolder;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.HWSProjectParametersTool;

/**
 *
 * @author vpc
 */
public class HWParameterFolderTreeTableNode extends HWParameterTreeTableNodeBase {

    public HWParameterFolderTreeTableNode(HWParameterFolder pe, HWConfigurationRun configuration, CustomJXTreeTable model) {
        super(pe, true, configuration, model);
        for (HWParameterElement n : pe.children().values()) {
            add(HWSProjectParametersTool.create(n, configuration, model));
        }
    }

    public HWParameterFolder getParameterValue() {
        return (HWParameterFolder) getUserObject();
    }

    @Override
    public Object getValueAt(int column) {
        if (column == 0) {
            return getParameterValue().name().get();
        }
        return null;
    }

    @Override
    public boolean isEditable(int column) {
        return column == 0;
    }

    @Override
    public void setValueAt(Object aValue, int column) {
        switch (column) {
            case 0: {
                getParameterValue().name().set((String) aValue);
                refreshModel();
                break;
            }
        }
    }

}
