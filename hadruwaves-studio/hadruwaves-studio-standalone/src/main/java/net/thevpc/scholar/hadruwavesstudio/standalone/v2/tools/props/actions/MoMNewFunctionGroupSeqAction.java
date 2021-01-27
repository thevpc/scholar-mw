/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions;

import javax.swing.JOptionPane;
import net.thevpc.scholar.hadruwaves.mom.solver.MomSolverTestTemplate;
import net.thevpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateSeq;
import net.thevpc.scholar.hadruwaves.mom.solver.test.ParamSeqTemplate;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public class MoMNewFunctionGroupSeqAction extends MoMNewFunctionGroupActionBase {

    public MoMNewFunctionGroupSeqAction(HadruwavesStudio studio) {
        super(studio, "MoMNewFunctionGroupSeq");
    }

    @Override
    protected MomSolverTestTemplate read(HWSolverTemplate r) {
        String s = JOptionPane.showInputDialog(getMainComponent(), "New Name");
        if (s != null) {
            MomSolverTestTemplateSeq list = new MomSolverTestTemplateSeq();
            list.name().set(s);
            ParamSeqTemplate p1 = new ParamSeqTemplate();
            p1.name().set("m");
            list.params().add(p1);
            ParamSeqTemplate p2 = new ParamSeqTemplate();
            p2.name().set("n");
            list.params().add(p2);
            ParamSeqTemplate p3 = new ParamSeqTemplate();
            p3.name().set("p");
            list.params().add(p3);
            ParamSeqTemplate p4 = new ParamSeqTemplate();
            p4.name().set("q");
            list.params().add(p4);
            return list;
        }
        return null;
    }

}
