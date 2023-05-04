package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.vpc.lib.pheromone.application.swing.JOptionPane2;
import net.vpc.lib.pheromone.application.swing.Swings;
import net.vpc.lib.pheromone.ariana.util.Exceptions;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.CMatrixTableModel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildJcoeffAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
//import net.thevpc.scholar.math.plot.CCubeFunctionVector3DPlot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import net.thevpc.common.log.Log;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.str.RequiredRebuildException;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha Date: 2 aout 2003 Time: 14:16:56
 */
public abstract class StructureAction extends TmwlabAction {

    private MomProjectEditor editor;
    private String title;
    private boolean jxyBuildRequired;

    public StructureAction(MomProjectEditor editor, String key, boolean jxyBuildRequired) {
        super(editor.getApplicationRenderer(), key, editor.getResources());
        this.editor = editor;
        this.jxyBuildRequired = jxyBuildRequired;
    }

    public static ComplexMatrix showMatrix(ComplexMatrix matrix) {
        CMatrixTableModel model;
        JTable table = new JTable(model = new CMatrixTableModel(Maths.matrix(matrix)));
        JPanel p = new JPanel(new BorderLayout());
        Box footer = Box.createVerticalBox();
        footer.add(Box.createHorizontalGlue());
        footer.add(new JLabel("Norme 1"));
        footer.add(new JTextField("" + matrix.norm1()));
        footer.add(new JLabel("Norme Inf"));
        footer.add(new JTextField("" + matrix.normInf()));
        footer.add(Box.createHorizontalGlue());

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(footer, BorderLayout.SOUTH);
        JOptionPane2 optionPane = new JOptionPane2(Swings.DEFAULT_PARENT_COMPONENT,
                p,
                "Editeur de Matrices",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.OK_CANCEL_OPTION,
                null,
                null);
        optionPane.getDialog().setResizable(true);
        int o = optionPane.showDialog();
        if (o == JOptionPane.OK_OPTION) {
            return model.getMatrix();
        } else {
            return null;
        }
    }

    public MomProjectEditor getEditor() {
        return editor;
    }

    protected void configure(RunningProjectThread thread) {
    }

    protected abstract void execute(RunningProjectThread thread) throws Exception;

    protected void terminateProcess(RunningProjectThread thread) {
    }

//    protected abstract RunAction createRunAction(HashMap map);
    public void applicationActionPerformed(ActionEvent e) {
        try {
            RunningProjectThread thread = createRunningStructureThread();
            thread.start();
        } catch (Throwable throwable) {
            Log.error(throwable);
            JOptionPane.showMessageDialog(getEditor(),
                    Exceptions.getMessage(throwable));
        }
    }

    protected final RunningProjectThread createRunningStructureThread() {
        return new RunningProjectThread(this);
    }

//    public void showPlot(Plot plot) {
//        getApplicationRenderer().getWindowManager().addWindow(getKey(),
//                getTitle(),
//                Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Graph.gif"),
//                plot);
//    }
    public void showPlotCCubeFunctionVector3D(VDiscrete... z) {
        plotter().title(getTitle()).plot(z);

//        getApplicationRenderer().getWindowManager().addWindow(getKey(),
//                getTitle(),
//                Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Graph.gif"),
//                new CCubeFunctionVector3DPlot(z));
    }

    public void showPlotComplex(double[] x, double[] y, Complex[][] z) {
//        JTabbedPane jTabbedPane = new JTabbedPane();
//        jTabbedPane.addTab("Module", Plot.surface(getTitle() + ", en module", x, y, Complex.abs(z)));
//        jTabbedPane.addTab(Application.getResources().get("complex.real"), Plot.surface(getTitle() + ", en partie r\u00E9elle", x, y, Complex.getReal(z)));
//        jTabbedPane.addTab(Application.getResources().get("complex.img"), Plot.surface(getTitle() + ", en partie imaginaire", x, y, Complex.getImag(z)));

        plotter().title(getTitle()).xsamples(x).ysamples(y).plot(z);
//        Plot plot = Plot.plot(getTitle(), x, y, z, false);
//        plot.setWorkingFolder(getEditor().getProject().getWorkDir());
//        getApplicationRenderer().getWindowManager().addWindow(getKey(),
//                getTitle(),
//                Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Graph.gif"),
//                plot);
    }

    public void showPlotComplex(double[] x, Complex[] z) {
        plotter().title(getTitle()).xsamples(x).plot(z);
        //plot.setWorkingFolder(getEditor().getProject().getWorkDir());
//        getApplicationRenderer().getWindowManager().addWindow(getKey(),
//                getTitle(),
//                Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Graph.gif"),
//                plot);
    }

    public String getTitle() {
        if (title == null) {
            return getName() + " (" + getEditor().getProjectName() + ")";
        } else {
            return title;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected void checkBuildExecute(RunningProjectThread thread) {
        Boolean build = (Boolean) thread.getProperties().get("build");
        MomStrHelper jxy = (MomStrHelper) thread.getHelper(true);
        if (Boolean.TRUE.equals(build)) {
            RunAction a = new BuildJcoeffAction(jxy);
            thread.setRunAction(a);
            a.go();
        }
    }

    protected boolean checkBuildConfigure(RunningProjectThread thread) {
        MomStrHelper jxy = null;
        try {
            jxy = (MomStrHelper) thread.getHelper(true);
            jxy.checkBuildIsRequired();
        } catch (RequiredRebuildException e) {
            int i = JOptionPane.showConfirmDialog(getEditor(),
                    "<HTML>"
                    + "Vous devez tout d'abord relancer le calcul des coefficient J.<BR>"
                    + "Voulez vous proceder maintenant ?"
                    + "</HTML>",
                    "Attention",
                    JOptionPane.OK_CANCEL_OPTION);
            if (i == JOptionPane.OK_OPTION) {
                thread.getProperties().put("build", Boolean.TRUE);
            } else {
                thread.setStopped(true);
                return false;
            }
        }
        return true;
    }

    public boolean isJxyBuildRequired() {
        return jxyBuildRequired;
    }

}
