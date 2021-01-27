/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions;

import javax.swing.JOptionPane;
import net.thevpc.scholar.hadruwaves.mom.solver.MomSolverTestTemplate;
import net.thevpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateList;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public class MoMNewFunctionGroupListAction extends MoMNewFunctionGroupActionBase {

    public MoMNewFunctionGroupListAction(HadruwavesStudio studio) {
        super(studio, "MoMNewFunctionGroupList");
    }

    @Override
    protected MomSolverTestTemplate read(HWSolverTemplate r) {
        String s = JOptionPane.showInputDialog(getMainComponent(), "New Name");
        if (s != null) {
            MomSolverTestTemplateList list = new MomSolverTestTemplateList();
            list.name().set(s);
            list.name().set(s);
            return list;
        }
        return null;
    }

}
