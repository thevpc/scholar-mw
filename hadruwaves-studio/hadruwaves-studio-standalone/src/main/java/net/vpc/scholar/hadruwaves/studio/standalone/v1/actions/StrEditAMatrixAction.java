package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;

import javax.swing.*;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadruwaves.mom.str.RequiredRebuildException;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrEditAMatrixAction extends StructureAction {
    public StrEditAMatrixAction(MomProjectEditor editor) {
        super(editor, "StrEditAMatrixAction", true);
    }

    protected void configure(RunningProjectThread thread) {
        MomStrHelper jxy = thread.getHelper(true);
        ComplexMatrix a = null;
        try {
            jxy.checkBuildIsRequired();
            a = jxy.evalAMatrix(ProgressMonitors.none());
        } catch (RequiredRebuildException e) {
            int r = JOptionPane.showOptionDialog(getEditor(), e.getMessage() + "\nContinuer ?", "Attention", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (r != JOptionPane.OK_OPTION) {
                thread.setStopped(true);
                return;
            }
            a = jxy.evalAMatrix(ProgressMonitors.none());
        }
        ComplexMatrix m = showMatrix(a);
        if (m != null) {
//            HashMap map = new HashMap();
//            map.put("Matrix", m);
            return;
        }
        thread.setStopped(true);
        return;
    }

    public void execute(RunningProjectThread thread) throws Exception {
    }

}
