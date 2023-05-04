package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import javax.swing.JOptionPane;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;

import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.vpc.lib.pheromone.application.swing.ECNumericField;
import net.vpc.lib.pheromone.application.swing.NumberLayout;
import net.vpc.lib.pheromone.application.swing.Swings;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildEstimateMNAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.TMWLabApplication;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrEstimateMNAction extends StructureAction {
    public StrEstimateMNAction(MomProjectEditor editor) {
        super(editor, "StrEstimateMNAction",false);
    }

    protected void configure(RunningProjectThread thread) {
        TMWLabApplication application = thread.getApplication();
        ECGroupPanel p = new ECGroupPanel();
        ECNumericField max_mn = new ECNumericField("max_mn", NumberLayout.DEFAULT, 8, 1, 1000000, 8, false);
        max_mn.setDescription(application.getResources().get("estimateMN.max_mn"));
        ECNumericField delta = new ECNumericField("max_mn", NumberLayout.DEFAULT, 8, 0.0000001, 1.0, 1, 10, false);
        delta.setDescription(application.getResources().get("estimateMN.delta"));
        ECNumericField threshold = new ECNumericField("threshold", NumberLayout.DEFAULT, 8, 1, 1000000, 5, false);
        threshold.setDescription(application.getResources().get("estimateMN.threshold"));

        ECNumericField nbrEstim = new ECNumericField("nbrEstim", NumberLayout.DEFAULT, 8, 0, 1000000, 8, false);
        nbrEstim.setDescription(application.getResources().get("estimateMN.nbrEstim"));

//        modeTE.setObject(new Boolean(getEditor().getConfiguration().getBoolean("estimateMNforJ.modeTE", true)));
//        modeTM.setObject(new Boolean(getEditor().getConfiguration().getBoolean("estimateMNforJ.modeTM", true)));
        max_mn.setObject(new Integer(getEditor().getProject().getConfiguration().getInt("estimateMNforJ.max_mn", 500)));
        delta.setObject(new Double(getEditor().getProject().getConfiguration().getDouble("estimateMNforJ.delta", 0.01)));
        threshold.setObject(new Integer(getEditor().getProject().getConfiguration().getInt("estimateMNforJ.threshold", 5)));
        nbrEstim.setObject(new Integer(getEditor().getProject().getConfiguration().getInt("estimateMNforJ.nbrEstim", 1)));

        p.add(new DataTypeEditor[]{max_mn, delta, threshold, nbrEstim}, 1);
        if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(getEditor(), p, application.getResources().get("estimateMN.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)) {
            thread.setStopped(true);
            return;
        }
        thread.getProperties().put("estimateMNforJ.max_mn", max_mn.getObject());
        thread.getProperties().put("estimateMNforJ.delta", delta.getObject());
        thread.getProperties().put("estimateMNforJ.threshold", threshold.getObject());
        thread.getProperties().put("estimateMNforJ.nbrEstim", nbrEstim.getObject());

        getEditor().getProject().getConfiguration().setProperty("estimateMNforJ.max_mn", max_mn.getObject());
        getEditor().getProject().getConfiguration().setProperty("estimateMNforJ.delta", delta.getObject());
        getEditor().getProject().getConfiguration().setProperty("estimateMNforJ.threshold", threshold.getObject());
        getEditor().getProject().getConfiguration().setProperty("estimateMNforJ.nbrEstim", nbrEstim.getObject());
    }

    public void execute(RunningProjectThread thread) throws Exception {
        int max_mn_value = ((Integer) thread.getProperties().get("estimateMNforJ.max_mn")).intValue();
        double delta_value = ((Double) thread.getProperties().get("estimateMNforJ.delta")).doubleValue();
        int threshold_value = ((Integer) thread.getProperties().get("estimateMNforJ.threshold")).intValue();
        int nbrEstim_value = ((Integer) thread.getProperties().get("estimateMNforJ.nbrEstim")).intValue();
        BuildEstimateMNAction buildEstimateMNAction = new BuildEstimateMNAction(thread.getHelper(true),
                max_mn_value,
                delta_value,
                threshold_value,
                nbrEstim_value);
        thread.setRunAction(buildEstimateMNAction);
        int mn = buildEstimateMNAction.go0();
        JOptionPane.showMessageDialog(getEditor(), getResources().get("estimateMN.result", new Integer(mn)), Swings.getResources().get("info"), JOptionPane.INFORMATION_MESSAGE);
    }

}
