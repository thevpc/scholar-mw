package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results;

import java.awt.Component;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.components.ResultsTreeCellRendererImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.LazyTreeBackendImpl;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.CustomLazyNode;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import javax.swing.tree.TreePath;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.echo.AppPopupMenu;
import net.thevpc.echo.swing.SwingApplications;
import net.thevpc.echo.swing.core.swing.JPopupMenuComponentSupplier;
import net.thevpc.echo.swing.core.swing.LazyTree;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateFDM;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateFEM;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateMoM;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.HWSolverAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.RemoveDefaultFileResultAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.RemoveResultAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.RunSolverAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.SaveCopyResultAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.SaveResultAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.ShowResultAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMCurrentAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMDirectivityFieldSphereAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMEFieldCartesianAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMEFieldSphereAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMEFieldSphereModuleAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMHFieldCartesianAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMHFieldSphereAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMHFieldSphereModuleAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMMatrixA;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMMatrixB;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMMatrixX;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMModeFunctions;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMPoyntingSphereAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMSourceAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMTestFunctions;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.mom.MoMTestModeScalarProducts;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.dialogs.ThetaPhiRSamplesDialog;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.dialogs.XyzSamplesDialog;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.DefaultHWSolverActionContext;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionContext;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionResultRegistry;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResult;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResultLocationType;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.AppCompUtils;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXTree;

public class HWSProjectResultsTool extends AbstractToolWindowPanel {

    private final CustomLazyNode root = new CustomLazyNode("/", "Results", "Results", "/", true);
    private final LazyTreeBackendImpl backend = new LazyTreeBackendImpl(root);
    public final JTree tree;
    private final HWSolverActionResultRegistry registry = new HWSolverActionResultRegistry();
    private AppPopupMenu popUpMenu;
    private HWConfigurationRun configuration;
    private ThetaPhiRSamplesDialog polardialog = new ThetaPhiRSamplesDialog(null, null);
    private XyzSamplesDialog xyzdialog = new XyzSamplesDialog(null, null);

    MoMEFieldCartesianAction moMEFieldCartesianAction = new MoMEFieldCartesianAction(xyzdialog);
    MoMEFieldSphereAction moMEFieldSphereAction = new MoMEFieldSphereAction(polardialog);
    MoMEFieldSphereModuleAction moMEFieldSphereModuleAction = new MoMEFieldSphereModuleAction(polardialog);
    MoMHFieldSphereAction moMHFieldSphereAction = new MoMHFieldSphereAction(polardialog);
    MoMHFieldSphereModuleAction moMHFieldSphereModuleAction = new MoMHFieldSphereModuleAction(polardialog);
    MoMDirectivityFieldSphereAction moMDirectivityFieldSphereAction = new MoMDirectivityFieldSphereAction(polardialog);
    MoMPoyntingSphereAction moMPoyntingSphereAction = new MoMPoyntingSphereAction(polardialog);
    MoMMatrixB moMMatrixB = new MoMMatrixB();
    MoMMatrixX moMMatrixX = new MoMMatrixX();
    MoMModeFunctions moMModeFunctions = new MoMModeFunctions();
    MoMSourceAction moMSourceAction = new MoMSourceAction(xyzdialog);
    MoMTestModeScalarProducts moMTestModeScalarProducts = new MoMTestModeScalarProducts();
    MoMHFieldCartesianAction moMHFieldCartesianAction = new MoMHFieldCartesianAction(xyzdialog);
    MoMCurrentAction moMCurrentAction = new MoMCurrentAction(xyzdialog);
    MoMMatrixA moMMatrixA = new MoMMatrixA();
    MoMTestFunctions moMTestFunctions = new MoMTestFunctions();

    public HWSProjectResultsTool(HadruwavesStudio studio) {
        super(studio);
        tree = (JXTree) LazyTree.of(new JXTree(), backend);
        tree.setRootVisible(false);
        setContent(new JScrollPane(tree));
        studio.proc().selectedConfiguration().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                updateRoot();
            }
        });
        updateRegistry();
        popUpMenu = SwingApplications.Components.createPopupMenu(studio.app());
        AppCompUtils.bind(popUpMenu, tree, this::preparePopupBeforeShowing);

        createPopUpMenu();

        updateRoot();
    }

    private void createPopUpMenu() {
        popUpMenu.tools().addAction(new RunSolverAction(this), "/runSolver");
        popUpMenu.tools().addAction(new ShowResultAction(this), "/showResult");
        popUpMenu.tools().addAction(new SaveResultAction(this), "/saveResult");
        popUpMenu.tools().addAction(new SaveCopyResultAction(this), "/saveCopyResult");
        popUpMenu.tools().addAction(new RemoveResultAction(this), "/removeResult");
        popUpMenu.tools().addAction(new RemoveDefaultFileResultAction(this), "/removeDefaultFileResult");
    }

    protected void updateRegistry() {
//        registry.register("", resultType);
    }

    public void refreshTools() {
        popUpMenu.tools().refresh();
        app().tools().refresh();
    }

    protected void preparePopupBeforeShowing() {
    }

    protected void onLookChanged() {
        tree.setCellRenderer(new ResultsTreeCellRendererImpl(studio()));
        if (popUpMenu != null) {
            SwingUtilities.updateComponentTreeUI(((JPopupMenuComponentSupplier) popUpMenu).component());
        }
    }

    public void updateRoot() {
        root.getList().clear();

        configuration = studio().proc().selectedConfiguration().get();
        if (configuration != null && configuration.project().get() != null) {
            DefaultHWSolverActionContext context = solverContext();
            HWSolverTemplate solver = configuration.solver().get();
            if (solver instanceof HWSolverTemplateMoM) {
                updateMoMSolver((HWSolverTemplateMoM) solver, context);
            }
            if (solver instanceof HWSolverTemplateFDM) {
                root.add("FDM Solver/E Field/E Field Cube", "E Field Cube", null, null, false);
                root.add("FDM Solver/E Field/E Field XY", "E Field XY", null, null, false);
                root.add("FDM Solver/E Field/E Field XZ", "E Field XZ", null, null, false);
                root.add("FDM Solver/E Field/E Field YZ", "E Field YZ", null, null, false);
                root.add("FDM Solver/H Field/H Field Cube", "H Field Cube", null, null, false);
                root.add("FDM Solver/H Field/H Field XY", "H Field XY", null, null, false);
                root.add("FDM Solver/H Field/H Field XZ", "H Field XZ", null, null, false);
                root.add("FDM Solver/H Field/H Field YZ", "H Field YZ", null, null, false);
            }
            if (solver instanceof HWSolverTemplateFEM) {
                root.add("FEM Solver/E Field/E Field Cube", "E Field Cube", null, null, false);
                root.add("FEM Solver/E Field/E Field XY", "E Field XY", null, null, false);
                root.add("FEM Solver/E Field/E Field XZ", "E Field XZ", null, null, false);
                root.add("FEM Solver/E Field/E Field YZ", "E Field YZ", null, null, false);
                root.add("FEM Solver/H Field/H Field Cube", "H Field Cube", null, null, false);
                root.add("FEM Solver/H Field/H Field XY", "H Field XY", null, null, false);
                root.add("FEM Solver/H Field/H Field XZ", "H Field XZ", null, null, false);
                root.add("FEM Solver/H Field/H Field YZ", "H Field YZ", null, null, false);
            }

            root.add("Saved Results", "Saved Results", null, null, true);

        }
        LazyTree.reload(tree);
        ((JXTree) tree).expandAll();
    }

    public DefaultHWSolverActionContext solverContext() {
        return new DefaultHWSolverActionContext(studio(), configuration, registry);
    }

    public void addResult(HWSolverResult result) {
        if (configuration != null) {
            getConfResults().addActionResult(result);
            updateRoot();
        }
    }

    public String generateCustomName(String path) {
        if (configuration != null) {
            String projectFilePath = configuration.project().get().filePath().get();
            if (!StringUtils.isEmpty(projectFilePath)) {
                return FileUtils.generateFileName(path, new File(projectFilePath, "results/saved").getPath());
            }
        }
        return null;
    }

    public HWSolverResult[] reloadResults(HWSolverAction action, HWSolverActionContext context) {
        List<HWSolverResult> all = new ArrayList<>();
        if (configuration != null) {
            String projectFilePath = context.project().filePath().get();
            if (!StringUtils.isEmpty(projectFilePath)) {
                // + name + extension()
                File[] files = new File(projectFilePath, "results/default/" + action.path() + "/").listFiles(x -> action.acceptResultFile(x));
                if (files != null) {
                    for (File file : files) {
                        try {
                            HWSolverResult r = context.loadResult(file.getPath(), HWSolverResultLocationType.ACTION);
                            all.add(r);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return all.toArray(new HWSolverResult[0]);
    }

    public ConfResults getConfResults() {
        ConfResults u = (ConfResults) configuration.userObjects().getUserObject("results");
        if (u == null) {
            u = new ConfResults();
            configuration.userObjects().putUserObject("results", u);
        }
        return u;
    }

    protected void addAction(HWSolverAction action, DefaultHWSolverActionContext context) {
        root.add(action.path(), action.name(), action.id(), action, true);
        int index = 1;
        for (HWSolverResult result : reloadResults(action, context)) {
            root.add(action.path() + "/" + index, result.defaultName(), action.id(), result, false);
            index++;
        }
        for (HWSolverResult result : getConfResults().getActionResults(action.id())) {
            root.add(action.path() + "/" + index, result.defaultName(), action.id(), result, false);
            index++;
        }
    }

    protected void updateMoMSolver(HWSolverTemplateMoM solver, DefaultHWSolverActionContext context) {
        Component mc = (Component)context.app().mainWindow().get().component();
        xyzdialog.setParent(mc);
        polardialog.setParent(mc);
        
        addAction(moMEFieldCartesianAction, context);
        addAction(moMEFieldSphereAction, context);
        addAction(moMEFieldSphereModuleAction, context);

        addAction(moMHFieldCartesianAction, context);
        addAction(moMHFieldSphereAction, context);
        addAction(moMHFieldSphereModuleAction, context);

        addAction(moMCurrentAction, context);
        addAction(moMDirectivityFieldSphereAction, context);
        addAction(moMPoyntingSphereAction, context);

        addAction(moMMatrixA, context);
        addAction(moMMatrixB, context);
        addAction(moMMatrixX, context);

        addAction(moMModeFunctions, context);
        addAction(moMTestFunctions, context);
        addAction(moMSourceAction, context);
        addAction(moMTestModeScalarProducts, context);
//        root.add("MoM Solver/E Field/E Field Cube", "E Field Cube", null, null, false);
//        root.add("MoM Solver/E Field/E Field XY", "E Field XY", null, null, false);
//        root.add("MoM Solver/E Field/E Field XZ", "E Field XZ", null, null, false);
//        root.add("MoM Solver/E Field/E Field YZ", "E Field YZ", null, null, false);
//        root.add("MoM Solver/H Field/H Field Cube", "H Field Cube", null, null, false);
//        root.add("MoM Solver/H Field/H Field XY", "H Field XY", null, null, false);
//        root.add("MoM Solver/H Field/H Field XZ", "H Field XZ", null, null, false);
//        root.add("MoM Solver/H Field/H Field YZ", "H Field YZ", null, null, false);
//        root.add("MoM Solver/Input Impedance/Zin 8-10GHz", "Zin 8-10GHz", null, null, false);
//        root.add("MoM Solver/S Parameters/S11 8-10GHz", "S11 8-10GHz", null, null, false);
//        root.add("MoM Solver/Source/Source Functions", "Source Functions", null, null, false);
//        root.add("MoM Solver/Source/Projected Sources", "Projected Sources", null, null, false);
//        root.add("MoM Solver/Source/Projected Sources Error", "Projected Sources Error", null, null, false);
//        root.add("MoM Solver/Mode Functions", "Mode Functions", null, null, false);
//        root.add("MoM Solver/Test Functions", "Test Functions", null, null, false);
//        root.add("MoM Solver/Matrices/A Matrix", "A Matrix", null, null, false);
//        root.add("MoM Solver/Matrices/B Matrix", "B Matrix", null, null, false);
//        root.add("MoM Solver/Matrices/X Matrix", "X Matrix", null, null, false);
//        root.add("MoM Solver/Matrices/Test**Mode", "Test**Mode", null, null, false);
    }

    public Object getSelectedItem(Predicate<Object> filter) {
        Object[] a = getSelectedItems(filter);
        if (a.length == 1) {
            return a[0];
        }
        return null;
    }

    public Object[] getSelectedItems() {
        return getSelectedItems(null);
    }

    public Object[] getSelectedItems(Predicate<Object> filter) {
        java.util.List<Object> all = new ArrayList<>();
        TreePath[] paths = tree.getSelectionPaths();
        for (TreePath path : paths) {
            Object a = resolveValidItem(path);
            if (a != null) {
                if (filter == null || filter.test(a)) {
                    all.add(a);
                }
            }
        }
        return all.toArray(new Object[0]);
    }

    public Object resolveValidItem(TreePath path) {
        CustomLazyNode ln = (CustomLazyNode) LazyTree.resolveLazyTreeNodeValue(path.getLastPathComponent());
        if (ln != null) {
            return ln.getValue();
        }
        return null;
    }

    public HWConfigurationRun configuration() {
        return configuration;
    }

}
