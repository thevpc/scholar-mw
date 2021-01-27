/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.components;

import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadrumaths.units.ParamUnit;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterValue;

/**
 *
 * @author vpc
 */
public class HWParameterValueTreeTableNode extends HWParameterTreeTableNodeBase {

    public HWParameterValueTreeTableNode(HWParameterValue t, HWConfigurationRun configuration, CustomJXTreeTable model) {
        super(t, false, configuration, model);
    }

    public HWParameterValue getParameterValue() {
        return (HWParameterValue) getUserObject();
    }

    @Override
    public Object getValueAt(int column) {
        switch (column) {
            case 0: {
                return getParameterValue().name().get();
            }
            case 1: {
                return getParameterValue().type().get();
            }
            case 2: {
                return configuration().getExpression(getParameterValue().name().get());
            }
            case 3: {
                return configuration().evalParamResult(getParameterValue().name().get());
            }
            case 4: {
                ParamUnit tunit = getParameterValue().unit().get();
                if (tunit == null) {
                    UnitType ttype = getParameterValue().type().get();
                    if (ttype == null) {
                        return null;
                    }
                    HWProject project = configuration().project().get();
                    if (project != null) {
                        tunit = project.units().defaultUnitValue(ttype);
                    }
                    if (tunit == null) {
                        tunit = ttype.defaultValue();
                    }
                }
                return tunit;
            }
            case 5: {
                return getParameterValue().discriminator().get();
            }
            case 6: {
                return getParameterValue().description().get();
            }

        }
        return super.getValueAt(column);
    }

    @Override
    public boolean isEditable(int column) {
        if (column == 4) {
            UnitType r = getParameterValue().type().get();
            if (r.unitValues().length > 0) {
                return true;
            }
            return false;
        }
        if (column == 3) {
            return false;
        }
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int column) {
        switch (column) {
            case 0: {
                getParameterValue().name().set((String) aValue);
                refreshModel();
                break;
            }
            case 1: {
                UnitType u = (UnitType) aValue;
                if (u == null) {
                    u = UnitType.String;
                }
                ParamUnit uu = getParameterValue().unit().get();
                getParameterValue().unit().set(null);
                getParameterValue().type().set(u);
//                if (uu != null && uu.type() != u) {
//                    uu = null;
//                }
                if (uu == null) {
                    if (configuration().project() != null) {
                        uu = configuration().project().get().units().defaultUnitValue(getParameterValue().type().get());
                    }
                    if (uu == null) {
                        uu = (ParamUnit) u.defaultValue();
                    }
                }
                getParameterValue().unit().set(uu);
                refreshModel();
                return;
            }

            case 2: {
                configuration().parameters().put(getParameterValue().name().get(), (String) aValue);
                refreshModel();
                return;
            }
            case 4: {
                ParamUnit u = (ParamUnit) aValue;
                if (u == null) {
                    u = getParameterValue().type().get().defaultValue();
                }
                getParameterValue().unit().set(u);
                refreshModel();
                return;
            }
            case 5: {
                getParameterValue().discriminator().set((Boolean) aValue);
                break;
            }
            case 6: {
                getParameterValue().description().set((String) aValue);
                break;
            }
        }
    }

}
