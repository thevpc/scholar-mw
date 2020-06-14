/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.components;

import net.vpc.scholar.hadrumaths.units.UnitType;
import net.vpc.scholar.hadruwaves.project.EvalExprResult;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 *
 * @author vpc
 */
public class CustomDefaultTreeTableModelImpl extends DefaultTreeTableModel {

    public CustomDefaultTreeTableModelImpl(TreeTableNode ttn) {
        super(ttn);
    }

    public void refresh() {
//        setRoot(getRoot());
        modelSupport.fireNewRoot();
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Type";
            case 2:
                return "Expression";
            case 3:
                return "Value";
            case 4:
                return "Unit";
            case 5:
                return "Discr.";
            case 6:
                return "Desc.";
        }
        return super.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return UnitType.class;
            case 2:
                return String.class;
            case 3:
                return EvalExprResult.class;
            case 4:
                return String.class;
            case 5:
                return Boolean.class;
            case 6:
                return String.class;
        }
        return super.getColumnClass(column);
    }

}
