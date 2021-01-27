/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions;

import javax.swing.JOptionPane;
import net.thevpc.scholar.hadruwaves.mom.solver.MomSolverTestTemplate;
import net.thevpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateMesh;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPatternType;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public class MoMNewFunctionGroupMeshAction extends MoMNewFunctionGroupActionBase {

    public MoMNewFunctionGroupMeshAction(HadruwavesStudio studio) {
        super(studio, "MoMNewFunctionGroupMesh");
    }

    @Override
    protected MomSolverTestTemplate read(HWSolverTemplate r) {
        String s = JOptionPane.showInputDialog(getMainComponent(), "New Name");
        if (s != null) {
            MomSolverTestTemplateMesh list = new MomSolverTestTemplateMesh();
            list.name().set(s);
            list.pattern().set(GpPatternType.RWG.name());
            list.complexity().set("3");
            return list;
        }
        return null;
    }

}
