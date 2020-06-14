/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.components;

import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.HWSProjectParametersTool;

/**
 *
 * @author vpc
 */
public class HWParametersTreeTableNodeImpl extends HWParameterTreeTableNodeBase {

    public HWParametersTreeTableNodeImpl(HWConfigurationRun configuration, CustomJXTreeTable model) {
        super(configuration, true, configuration, model);
        if (configuration!=null && configuration.project() != null) {
            for (HWParameterElement n : configuration.project().get().parameters().children().values()) {
                add(HWSProjectParametersTool.create(n, configuration, model));
            }
        }
    }

    @Override
    public Object getValueAt(int column) {
        if (column == 0) {
            if (configuration()==null || configuration().project().get() == null) {
                return null;
            }
            return configuration().project().get().name().get() + " : " + configuration().name().get();
        }
        return null;
    }

    @Override
    public boolean isEditable(int column) {
        return false;
    }

}
