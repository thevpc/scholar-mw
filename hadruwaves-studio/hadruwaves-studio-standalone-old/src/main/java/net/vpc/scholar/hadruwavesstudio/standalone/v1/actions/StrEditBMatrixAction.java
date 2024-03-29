package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.CMatrixTableModel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;

import javax.swing.*;
import net.vpc.lib.pheromone.application.swing.JOptionPane2;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.mom.str.RequiredRebuildException;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrEditBMatrixAction extends StructureAction {

    public StrEditBMatrixAction(MomProjectEditor editor) {
        super(editor, "StrEditBMatrixAction", true);
    }

    protected void configure(RunningProjectThread thread) {
        CMatrixTableModel model;
        MomStrHelper jxy = thread.getHelper(true);
        ComplexMatrix b = null;
        try {
            jxy.checkBuildIsRequired();
            b = jxy.evalBMatrix(ProgressMonitors.none());
        } catch (RequiredRebuildException e) {
            int r = JOptionPane.showOptionDialog(getEditor(), e.getMessage() + "\nContinuer ?", "Attention", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (r != JOptionPane.OK_OPTION) {
                thread.setStopped(true);
                return;
            }
            b = jxy.evalBMatrix(ProgressMonitors.none());
        }
        if (b == null) {
            JOptionPane2.showErrorDialog(getEditor(),
                    "Vous devez relacer le calcul des coefficients de J");
            thread.setStopped(true);
            return;
        }
        JTable table = new JTable(model = new CMatrixTableModel(b));
        int ret = JOptionPane.showConfirmDialog(getEditor(), new JScrollPane(table), "Editer la matrice B", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ret == JOptionPane.OK_OPTION) {
            thread.getProperties().put("Matrix", model.getMatrix());
            return;
        }
        thread.setStopped(true);
        return;
    }

    public void execute(RunningProjectThread thread) throws Exception {
//        Matrix JCoeffs=(Matrix) config.get("Matrix");
    }
}
