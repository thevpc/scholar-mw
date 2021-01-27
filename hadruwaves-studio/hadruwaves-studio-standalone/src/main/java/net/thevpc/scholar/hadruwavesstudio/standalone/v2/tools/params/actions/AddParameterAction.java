/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.HWSProjectParametersTool;

import javax.swing.*;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterValue;

/**
 * @author vpc
 */
public class AddParameterAction extends AddParameterActionBase {

    private UnitType type;

    public AddParameterAction(HWSProjectParametersTool outer, UnitType type) {
        super(outer, "AddParameter" + type.name());
        this.type = type;
    }

    @Override
    protected HWParameterElement read() {
        String newName = "new" + type;
        newName = JOptionPane.showInputDialog(getMainComponent(), "New " + type, newName);
        if (newName != null) {
            return new HWParameterValue(newName, type, outer.configuration().project().get().units().defaultUnitValue(type));
        }
        return null;
    }

}
