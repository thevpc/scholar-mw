package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.PlotConfigData;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.PlotConfigEditor;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public abstract class AbstractStrPlotXYAction extends StructureAction {

    public AbstractStrPlotXYAction(MomProjectEditor editor, String key) {
        super(editor, key, true);
    }

    protected void configure(RunningProjectThread thread) {
        PlotConfigEditor configPanel = new PlotConfigEditor(thread.getMomProjectEditor());
        prepare(configPanel);
        PlotConfigData config = configPanel.showDialog(getEditor(), getTitle(), getEditor().getProject());
        if (config == null) {
            thread.setStopped(true);
            return;
        }
        config.recompile();
        thread.getProperties().put("config", config);
    }

    protected void prepare(PlotConfigEditor configPanel) {
        configPanel.setVisibleAxisPanel(true);
        configPanel.setVisibleLeadingPanel(true);
        configPanel.setVisibleXPanel(true);
        configPanel.setVisibleYPanel(true);
        configPanel.setVisibleIterPanel(false);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        PlotConfigData plotConfig = (PlotConfigData) thread.getProperties().get("config");
        double[] x = plotConfig.getX();
        double[] y = plotConfig.getY();
        double[] z = plotConfig.getZ();


        VDiscrete[] cc = getSeries(thread, x, y, z);

        setTitle(null);
        String t = getTitle();
        setTitle(t);
        showPlotCCubeFunctionVector3D(cc);

    }

    public abstract VDiscrete[] getSeries(RunningProjectThread thread, double[] x, double[] y, double[] z);
}
