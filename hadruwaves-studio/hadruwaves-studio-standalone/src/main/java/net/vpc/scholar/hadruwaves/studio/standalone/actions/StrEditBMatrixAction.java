package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.CMatrixTableModel;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;

import javax.swing.*;
import net.vpc.lib.pheromone.application.swing.JOptionPane2;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadruwaves.mom.str.RequiredRebuildException;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

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
        Matrix b = null;
        try {
            jxy.checkBuildIsRequired();
            b = jxy.computeBMatrix(ProgressMonitorFactory.none());
        } catch (RequiredRebuildException e) {
            int r = JOptionPane.showOptionDialog(getEditor(), e.getMessage() + "\nContinuer ?", "Attention", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (r != JOptionPane.OK_OPTION) {
                thread.setStopped(true);
                return;
            }
            b = jxy.computeBMatrix(ProgressMonitorFactory.none());
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
