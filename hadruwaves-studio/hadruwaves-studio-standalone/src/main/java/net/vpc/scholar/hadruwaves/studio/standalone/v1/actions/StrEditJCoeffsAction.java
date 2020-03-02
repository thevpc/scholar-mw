package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.CMatrixTableModel;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;

import javax.swing.*;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrEditJCoeffsAction extends StructureAction {
    public StrEditJCoeffsAction(MomProjectEditor editor) {
        super(editor, "StrEditJCoeffsAction", true);
    }

    protected void configure(RunningProjectThread thread) {
        CMatrixTableModel model;
        MomStrHelper jxy = thread.getHelper(true);
        jxy.checkBuildIsRequired();
        JTable table = new JTable(model = new CMatrixTableModel(Maths.matrix(jxy.evalXMatrix(ProgressMonitors.none()))));
        int ret = JOptionPane.showConfirmDialog(getEditor(), new JScrollPane(table), "Changer les coefficients de J", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ret == JOptionPane.OK_OPTION) {
            thread.getProperties().put("JCoeffs", model.getMatrix().getColumn(0));
            return;
        }
        thread.setStopped(true);
        return;
    }

    public void execute(RunningProjectThread thread) throws Exception {
//        CVector JCoeffs = (CVector) config.get("JCoeffs");
//        getEditor().getJxy().setJcoeffs(JCoeffs);
    }

}
