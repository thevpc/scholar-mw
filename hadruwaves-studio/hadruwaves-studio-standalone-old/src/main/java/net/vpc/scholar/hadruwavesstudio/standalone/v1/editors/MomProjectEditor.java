package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import net.thevpc.common.log.Log;

import net.vpc.lib.pheromone.application.ApplicationRenderer;
import net.vpc.lib.pheromone.application.swing.ECCheckBox;
import net.vpc.lib.pheromone.application.swing.ECComboBox;
import net.vpc.lib.pheromone.application.swing.ECTextArea;
import net.vpc.lib.pheromone.application.swing.ECTextField;
import net.vpc.lib.pheromone.ariana.util.HashList;
import net.vpc.lib.pheromone.ariana.util.Maps;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.ECExpressionField;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.VarList;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.AllTasksAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.RunningProjectThread;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrBuildAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrEstimateMNAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrListCacheAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrOpenCacheAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotConvengenceJCoeffAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotDurationJCoeffAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotEBaseAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotJBaseAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotJTestingAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotJcoeffsAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotS11Action;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotSourceAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotZinAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrS11Action;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrSaveAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrSaveAsAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrZinAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StructureStopAllAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.panels.MomProject2DPlotEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.TMWLabApplication;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrCapacityAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrDumpAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrPlotCapacityAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrShowAMatrixAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrShowBMatrixAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrShowFnAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrShowGpAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.StrShowGpFnSpAction;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.DataTypeEditorFactory;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.vpc.lib.pheromone.application.swing.JOptionPane2;
import net.vpc.lib.pheromone.application.swing.Swings;
import net.vpc.lib.pheromone.application.viewlets.OutlookViewer;
import net.vpc.lib.pheromone.application.winman.AppWindow;
import net.vpc.lib.pheromone.ariana.types.SimpleDataType;
import net.vpc.lib.pheromone.ariana.util.Resources;
import net.vpc.lib.pheromone.ariana.util.Utils;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruwaves.Physics;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.CircuitType;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;
import net.thevpc.scholar.hadruwaves.mom.MomCache;
import net.thevpc.scholar.hadruwaves.mom.ProjectType;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.MomProjectExtraLayer;
import net.thevpc.scholar.hadruwaves.mom.project.MomProjectListener;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.common.Area;
import net.thevpc.scholar.hadruwaves.mom.project.common.AreaGroup;
import net.thevpc.scholar.hadruwaves.mom.project.common.AreaZone;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelperImpl;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.HWSTools;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.MomUIFactory;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.areashapes.RectAreaShapeEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.gpmeshs.BoxModesGpMesherEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.gpmeshs.ConstantGpMesherEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.gpmeshs.RooftopGpMesherEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials.ModalSourceMaterialEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials.PecMaterialEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials.PlanarSourceMaterialEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials.SurfaceImpedanceMaterialEditor;
import net.vpc.upa.types.ConstraintsException;
import net.vpc.upa.types.ListType;

/**
 * Created by IntelliJ IDEA. User: taha Date: 7 juil. 2003 Time: 11:46:07 To
 * change this template use Options | File Templates.
 */
public class MomProjectEditor extends JPanel {

    protected MomProjectListener projectListener;
    protected JProgressBar progressBar;
    protected JButton actionsButton = new JButton("/");
    protected JButton actionsStopButton = new JButton();
    int currentRunningActionIndex = 0;
    protected Timer timer;
    protected ECTextField structTitle;
    private ECComboBox circuitType;
    private ECComboBox projectType;
    private LinkedHashMap<Double, LongOpStruct> operations = new LinkedHashMap<Double, LongOpStruct>();
//    private DielectricEditor dielectricEditor;
    private AreaGroupTreeEditor groupTreeEditor;
    private ECExpressionField freq;
    private JButton freqSelectButton = new JButton("...");
    private ECExpressionField lamda;
    private JLabel lamdaUnit;
    private JLabel freqUnit;
    private MomProject2DPlotEditor project2DPlotEditor;
//    private File workDir;
//    private File configFile;
    private StrDumpAction strDumpAction;
    private StrSaveAction strSaveAction;
    private StrSaveAsAction strSaveAsAction;
    private StrBuildAction strBuildAction;
    private StrPlotEBaseAction strPlotEBaseAction;
    private StrPlotJTestingAction strPlotJTestingAction;
    private StrPlotJBaseAction strPlotJBaseAction;
    private StrEstimateMNAction strEstimateMNAction;
    private StrPlotSourceAction strPlotSourceAction;
    private StrZinAction strZinAction;
    private StrPlotZinAction strPlotZinAction;
    private StrCapacityAction strCapacityAction;
    private StrPlotCapacityAction strPlotCapacityAction;
    private StrPlotConvengenceJCoeffAction strPlotConvengenceJCoeffAction;
    private StrPlotDurationJCoeffAction strPlotDurationJCoeffAction;
    private StrS11Action strS11Action;
    private StrPlotS11Action strPlotS11Action;
    private StrPlotJcoeffsAction strPlotJcoeffsAction;
    private AllTasksAction allTasksAction;
    private StrOpenCacheAction strOpenCacheAction;
    private StrListCacheAction strListCacheAction;
    private StrShowGpAction strShowGpAction;
    private StrShowFnAction strShowFnAction;
    private StrShowGpFnSpAction strShowGpFnSpAction;
    private StrShowAMatrixAction strShowAMatrixAction;
    private StrShowBMatrixAction strShowBMatrixAction;
    private MomProject currentProject;
    private OptimizationPanel optimizationPanel;
    private VarsPanel varListPanel;
    private MiscPanel miscPanel;
    private LayersPanel layersPanel;
    private BoxPanel boxPanel;
    private ApplicationRenderer application;
    private List<MomProjectListener> momProjectListeners=new ArrayList<MomProjectListener>();
    private Hashtable<String, MomUIFactory> factories = new Hashtable<String, MomUIFactory>();
//    public void setWorkDir(File workDir) {
//        this.workDir = workDir;
//    }
    private Hashtable<String, Object> mapping = new Hashtable<String, Object>();
    boolean isUpdatingProject;
    PropertyChangeListener revalidateLamdaPropertyChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            if (currentProject != null) {
                double du = getDimUnitValue();
//                lamdaUnit.setText(((ListType) miscPanel.dimUnitComponent.getHelper().getDataType()).getViewObject(miscPanel.dimUnitComponent.getSelectedItem()).toString());
//                freqUnit.setText(((ListType) miscPanel.freqUnitComponent.getHelper().getDataType()).getViewObject(miscPanel.freqUnitComponent.getSelectedItem()).toString());
                lamdaUnit.setText((miscPanel.dimUnitComponent.getSelectedItem()).toString());
                freqUnit.setText((miscPanel.freqUnitComponent.getSelectedItem()).toString());
                String frString = (String) freq.getHelper().getObject();
                double fr = Physics.lambda(currentProject.evaluateFrequency(frString));
                lamda.getHelper().setObject(String.valueOf(fr / du));

            }
        }
    };

    public void updateView() {
        try {
            isUpdatingProject = true;
            structTitle.getHelper().setObject(currentProject.getName());
            circuitType.getHelper().setObject(currentProject.getCircuitType());
            projectType.getHelper().setObject(currentProject.getProjectType());
            freq.getHelper().setObject(currentProject.getFrequencyExpression());
            groupTreeEditor.setProject(currentProject);

            optimizationPanel.load(currentProject);
            varListPanel.load(currentProject);
            miscPanel.load(currentProject);
            layersPanel.load(currentProject);
            boxPanel.load(currentProject);
            project2DPlotEditor.repaint();
        } finally {
            isUpdatingProject = false;
        }

    }

    public MomProjectEditor(ApplicationRenderer application, File file) throws IOException, ParseException {
        this(application);
        load(file);
    }

    public MomProjectEditor(ApplicationRenderer application) {
        super(new BorderLayout());
        this.application = application;
        registerDefaults();

        strDumpAction = new StrDumpAction(this);
        strSaveAction = new StrSaveAction(this);
        strSaveAsAction = new StrSaveAsAction(this);
        strBuildAction = new StrBuildAction(this);
        strPlotEBaseAction = new StrPlotEBaseAction(this);
        strPlotJTestingAction = new StrPlotJTestingAction(this);
        strPlotJBaseAction = new StrPlotJBaseAction(this);
        strEstimateMNAction = new StrEstimateMNAction(this);
        strPlotSourceAction = new StrPlotSourceAction(this);
        strZinAction = new StrZinAction(this);
        strPlotZinAction = new StrPlotZinAction(this);
        strCapacityAction = new StrCapacityAction(this);
        strPlotCapacityAction = new StrPlotCapacityAction(this);
        strPlotConvengenceJCoeffAction = new StrPlotConvengenceJCoeffAction(this);
        strPlotDurationJCoeffAction = new StrPlotDurationJCoeffAction(this);
        strS11Action = new StrS11Action(this);
        strPlotS11Action = new StrPlotS11Action(this);
        strPlotJcoeffsAction = new StrPlotJcoeffsAction(this);
        allTasksAction = new AllTasksAction(this);
        strOpenCacheAction = new StrOpenCacheAction(this);
        strListCacheAction = new StrListCacheAction(this);
        strShowGpAction = new StrShowGpAction(this);
        strShowFnAction = new StrShowFnAction(this);
        strShowGpFnSpAction = new StrShowGpFnSpAction(this);
        strShowAMatrixAction = new StrShowAMatrixAction(this);
        strShowBMatrixAction = new StrShowBMatrixAction(this);

        circuitType = new ECComboBox("Circuit Type", new ListType("", (java.util.List) Arrays.asList(CircuitType.values()), new SimpleDataType(CircuitType.class, false)));
        circuitType.getHelper().setObject(CircuitType.SERIAL);
        circuitType.getHelper().setDescription("Type Circuit");
        projectType = new ECComboBox("Project Type", new ListType("", (java.util.List) Arrays.asList(ProjectType.values()), new SimpleDataType(ProjectType.class, false)));
        projectType.getHelper().setObject(ProjectType.PLANAR_STRUCTURE);
        projectType.getHelper().setDescription("Type Project");

        varListPanel = new VarsPanel();
        miscPanel = new MiscPanel();
        layersPanel = new LayersPanel();
        boxPanel = new BoxPanel();
        optimizationPanel = new OptimizationPanel();
        currentProject = new MomProject();
        projectListener = new MomProjectListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                for (MomProjectListener momProjectListener : momProjectListeners) {
                    momProjectListener.propertyChange(evt);
                }
            }
        };
        currentProject.addProjectListener(projectListener);
        JTabbedPane jTabbedPane = new JTabbedPane();
        structTitle = new ECTextField("structTitle", 16, 1, 40, false);
        structTitle.setText("NO_NAME");
        structTitle.getHelper().setDescription(getResources().get("struct.label"));

//        dielectricEditor = new DielectricEditor();
        groupTreeEditor = new AreaGroupTreeEditor(this);
        groupTreeEditor.setProject(currentProject);

        groupTreeEditor.setBorder(BorderFactory.createTitledBorder(getResources().get("groups")));

//        JPanel dielectricEditorButtonPanel = new JPanel();
//        JButton configDielectricBtn = new JButton("configurer...");
//        configDielectricBtn.addActionListener(new ActionListener() {
//            public void actionPerformedImpl(ActionEvent e) {
////                configureDielectric();
//            }
//        });
//        dielectricEditorButtonPanel.add(configDielectricBtn);
//
//        dielectricEditorButtonPanel.setBorder(BorderFactory.createTitledBorder("Boitier"));
        ECGroupPanel freqPanel = new ECGroupPanel();
        freq = new ECExpressionField("freq", false);
        freq.getHelper().setDescription("Frequence");
        freq.getHelper().setDescription("Freq");
        lamda = new ECExpressionField("lamda", false);
        lamda.getHelper().setDescription("lamda");
        lamda.getHelper().setDescription("Lamda");
        lamda.setEnabled(false);
        lamdaUnit = new JLabel();
        freqUnit = new JLabel();
        lamda.getHelper().setSuffixComponent(lamdaUnit);

        freqSelectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    MomStrHelper jxy = new MomStrHelperImpl();
                    if (getConfigFile() == null) {
                        throw new IllegalArgumentException("You should save Structure before");
                    }
                    jxy.init(getProject0());
                    Collection<MomCache> c = jxy.getSimilarCaches("frequency");
                    double[] d = new double[c.size()];
                    int index = 0;
                    for (MomCache momCache : c) {
                        d[index++] = momCache.parseCacheDouble("frequency", Double.NaN);
                    }

                    //currentStructure
                    if (d.length == 0) {
                        JOptionPane.showMessageDialog(MomProjectEditor.this, "Aucune freq en cache");
                    } else {
                        Arrays.sort(d);
                        DefaultListModel m = new DefaultListModel();
                        JList list = new JList(m);
                        double fu = getFreqUnitValue();
                        for (double v : d) {
                            m.addElement(String.valueOf(v / fu));
                        }
                        list.setSelectedIndex(0);
                        JScrollPane p = new JScrollPane(list);
                        int r = JOptionPane.showConfirmDialog(MomProjectEditor.this, p, "Selectionner", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (r == JOptionPane.OK_OPTION) {
                            freq.getHelper().setObject(list.getSelectedValue());
                        }
                    }
                } catch (Exception e1) {
                    JOptionPane2.showErrorDialog(freqSelectButton, e1.toString());
                }
            }
        });
        freq.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, revalidateLamdaPropertyChangeListener);
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(freqUnit);
        horizontalBox.add(freqSelectButton);
        freq.getHelper().setSuffixComponent(horizontalBox);

        freqPanel.add(new DataTypeEditor[]{structTitle, projectType, circuitType, freq, lamda}, 1).setBorder(BorderFactory.createTitledBorder(getResources().get("frequence")));

        OutlookViewer viewer = new OutlookViewer();
        viewer.addViewlet("General", null, Swings.createVerticalBox(new Object[]{
            freqPanel/*
                 * , dielectricEditorButtonPanel
         */
        }));
        viewer.addViewlet(getResources().get("groups"), null, groupTreeEditor);

//        JSplitPane vjSplitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,sourcesList,metalsList);
//        vjSplitPane.setOneTouchExpandable(true);
//
//
//        JSplitPane vjSplitPane1=new JSplitPane(JSplitPane.VERTICAL_SPLIT,Swings.createVerticalBox(new Object[]{
//            freqPanel,dielectricEditorButtonPanel
//        }),vjSplitPane);
//        vjSplitPane1.setOneTouchExpandable(true);
        JSplitPane hjSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, viewer, project2DPlotEditor = new MomProject2DPlotEditor(this));

//        JSplitPane hjSplitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,vjSplitPane1,structure2DPlotEditor = new Structure2DPlotEditor(this));
        hjSplitPane.setOneTouchExpandable(true);

        jTabbedPane.addTab(getResources().get("schema"), hjSplitPane);
        jTabbedPane.addTab(getResources().get("boxPanel"), boxPanel);
        jTabbedPane.addTab(getResources().get("layersPanel"), layersPanel);
        jTabbedPane.addTab(getResources().get("optimizationPanel"), optimizationPanel);
        jTabbedPane.addTab(getResources().get("varListPanel"), varListPanel);
        jTabbedPane.addTab(getResources().get("miscPanel"), miscPanel);
//        dielectricEditor.addPropertyChangeListener("DielectricChanged", new PropertyChangeListener() {
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (!isUpdatingStructure) {
//                    try {
//                        structure2DPlotEditor.setStructure(getStructure());
//                        groupTreeEditor.setStructure(getStructure());
//                    } catch (IllegalArgumentException e1) {
//                    }
//                }
//            }
//        });

        progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();
        JPanel jp1 = new JPanel(new BorderLayout());
        jp1.add(jTabbedPane);
        add(jp1);
        JToolBar b = new JToolBar();
        b.setFloatable(false);
        actionsStopButton.setIcon(Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Stop.gif"));
        b.add(progressBar);
        b.add(actionsButton);
        b.add(actionsStopButton);
        add(b, BorderLayout.SOUTH);
        structTitle.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateWindowTitle();
            }
        });
        actionsStopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stopOpeartion(currentRunningActionIndex);
            }
        });
        JMenuBar jMenuBar = new JMenuBar();
        add(jMenuBar, BorderLayout.NORTH);

        JMenu fileMenu = new JMenu(Swings.getResources().get("file"));
        jMenuBar.add(fileMenu);
        fileMenu.add(strSaveAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        fileMenu.add(strSaveAsAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
        fileMenu.addSeparator();
        fileMenu.add(strDumpAction);
//        fileMenu.add(strSaveJCoeffsAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
//        fileMenu.add(strLoadJCoeffsAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, KeyEvent.SHIFT_MASK));
//        fileMenu.add(strEditJCoeffsAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
//
//        fileMenu.addSeparator();
//        fileMenu.add(strEditAMatrixAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
//        fileMenu.add(strEditBMatrixAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F19, 0));
        fileMenu.add(new StructureStopAllAction(this, "StopAll"));

        JMenu eMenu = new JMenu("Taches");
        jMenuBar.add(eMenu);
        eMenu.add(strPlotEBaseAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK));
        eMenu.addSeparator();
        eMenu.add(strPlotJTestingAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
        eMenu.add(strPlotJBaseAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_MASK));
        eMenu.addSeparator();
        eMenu.add(strPlotSourceAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
        eMenu.addSeparator();
        eMenu.add(strZinAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
        eMenu.add(strPlotZinAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK));
        eMenu.add(strCapacityAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
        eMenu.add(strPlotCapacityAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK));
        eMenu.add(strS11Action).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK + KeyEvent.ALT_MASK));
        eMenu.add(strPlotS11Action).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK + KeyEvent.ALT_MASK));

        JMenu convMenu = new JMenu("Convergence");

        jMenuBar.add(convMenu);
        convMenu.add(allTasksAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
        convMenu.addSeparator();
        convMenu.add(strBuildAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK));
        convMenu.add(strEstimateMNAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
        convMenu.addSeparator();
        convMenu.add(strPlotConvengenceJCoeffAction);
        convMenu.add(strPlotDurationJCoeffAction);

        JMenu momMenu = new JMenu("MoM");
        jMenuBar.add(momMenu);
        momMenu.add(strShowGpAction);
        momMenu.add(strShowFnAction);
        momMenu.add(strShowGpFnSpAction);
        momMenu.add(strShowAMatrixAction);
        momMenu.add(strShowBMatrixAction);
        momMenu.add(strPlotJcoeffsAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK));

        JMenu cMenu = new JMenu("Config");
        jMenuBar.add(cMenu);
        cMenu.add(strOpenCacheAction);
        cMenu.add(strListCacheAction);

        addAncestorListener(new AncestorListener() {

            public void ancestorAdded(AncestorEvent event) {
                updateWindowTitle();
            }

            public void ancestorRemoved(AncestorEvent event) {
            }

            public void ancestorMoved(AncestorEvent event) {
            }
        });

        actionsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                currentRunningActionIndex++;
                updateActionsButton();
            }
        });

        getProject();
        freq.getHelper().setObject("1");
    }

//    public void configureDielectric() {
//        dielectricEditor.setDielectric(currentDielectric);
//        dielectricEditor.startEditing();
//        while (true) {
//            try {
//                int ret = JOptionPane.showConfirmDialog(this,
//                        dielectricEditor,
//                        "Box",
//                        JOptionPane.OK_CANCEL_OPTION,
//                        JOptionPane.PLAIN_MESSAGE);
//                if (ret == JOptionPane.OK_OPTION) {
//                    currentDielectric = dielectricEditor.getDielectric();
//                    dielectricEditor.commitEditing();
//                    return;
//                } else {
//                    dielectricEditor.rollbackEditing();
//                    return;
//                }
//            } catch (ConstraintsException e) {
//                JOptionPane2.showErrorDialog(this, e);
//            }
//        }
//    }
//    public double[][] getXY(){
//        double[][] d=new double[0][];
//        try {
//            d = new double[2][];
//
//            double xmin=((Double) plotXmin.getObject()).doubleValue();
//            double xmax=((Double) plotXmax.getObject()).doubleValue();;
//            int xcount=((Integer)plotXcount.getObject()).intValue();
//
//            double[] x=Math2.iterate(xmin,xmax,xcount);
//
//            double ymin=((Double) plotYmin.getObject()).doubleValue();
//            double ymax=((Double) plotYmax.getObject()).doubleValue();;
//            int ycount=((Integer)plotYcount.getObject()).intValue();
//
//            double[] y=Math2.iterate(ymin,ymax,ycount);
//
//            d[0]=x;
//            d[1]=y;
//            return d;
//        } catch (ConstraintsException e) {
//            throw new IllegalArgumentException(application.getResources().get("err.plot"));
//        }
//    }
    public void addMomProjectListener(MomProjectListener listener){
        momProjectListeners.add(listener);
    }
    
    private void updateWindowTitle() {
        try {
            AppWindow window = (AppWindow) SwingUtilities.getAncestorOfClass(AppWindow.class, this);
            if (window != null) {
                String s = (String) structTitle.getHelper().getObject(false);
                if (s == null) {
                    s = "*";
                }
                String o = (String) miscPanel.structDesc.getHelper().getObject(false);
                if (o != null) {
                    int x = o.indexOf('\n');
                    if (x > 0) {
                        o = o.substring(0, x);
                    }
                }
                if (o != null) {
                    s += (" " + o);
                }
                window.setTitle(s);
            }
        } catch (ConstraintsException e) {
            e.printStackTrace();
        }
    }

    public void load(File file) throws IOException, ParseException {
        currentProject.load(file);
        updateView();
        project2DPlotEditor.loadConfiguration(currentProject.getConfiguration(), "editor");
    }

    public void load() throws IOException, ParseException {
        load(currentProject.getConfigFile());
    }

    public void saveStruct(File file) throws IOException {
        MomProject s = getProject();
        project2DPlotEditor.storeConfiguration(s.getConfiguration(), "editor");
        s.setFile(file);
        s.store();
    }

//    private void dimUnitChanged(double oldValue,double newValue){
//      dielectricEditor.dimUnitChanged(oldValue,newValue);
//    }
//
//    private void freqUnitChanged(double oldValue,double newValue){
//        Object oldFreq=((Double) freq.getObject(false));
//        if(oldFreq==null){
//            return;
//        }
//        freq.setObject(
//            new Double(
//                ((Double) freq.getObject()).doubleValue()* oldValue / newValue
//            )
//        );
//    }
    public String getProjectName() {
        String s = (String) structTitle.getHelper().getObject(false);
        return s == null ? "SANS_NOM" : s;
    }

    public MomProject getProject0() throws IllegalArgumentException {
        return currentProject;
    }

    public MomProject getProject() throws IllegalArgumentException {

        currentProject.setName(structTitle.getText());
        boxPanel.store(currentProject);
        currentProject.setFrequencyExpression(freq.getText());
        currentProject.setProjectType((ProjectType) projectType.getHelper().getObject());
        currentProject.setCircuitType((CircuitType) circuitType.getHelper().getObject());
        optimizationPanel.store(currentProject);
        miscPanel.store(currentProject);
        layersPanel.store(currentProject);
        varListPanel.store(currentProject);

        currentProject.recompile();
        return currentProject;
    }

    private static class LongOpStruct {

        private RunningProjectThread runner;
        String title;

        public LongOpStruct(RunningProjectThread runner, String title) {
            this.runner = runner;
            this.title = title;
        }

        public void stop() {
            runner.stop();
        }

        public net.thevpc.scholar.hadruwaves.mom.project.common.RunAction getRunAction() {
            return runner.getRunAction();
        }
    }

    public Object startLongOperation(RunningProjectThread src, String desc) {
        Double o = new Double(Math.random());
        if (operations.size() == 0) {
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            progressBar.setStringPainted(true);
//            progressBar.setIndeterminate(true);
        }
        operations.put(o, new LongOpStruct(src, desc));
        progressBar.setString(operations.size() + " op en cours...");
        updateActionsButton();
        tick();
        return o;
    }

    public void stopOpeartion(int index) {
        int cindex = 0;
        for (Iterator<LongOpStruct> i = operations.values().iterator(); i.hasNext();) {
            LongOpStruct o = i.next();
            if (cindex == index) {
                try {
                    o.stop();
                    Log.trace("Op\u00E9ration interrompue : " + o.title);
                } catch (Exception e) {
                    Log.trace("Op\u009Eration interrompue avec erreur : " + o.title + " : " + e);
                }
                i.remove();
                break;
            }
            cindex++;
        }
        progressBar.setIndeterminate(false);
        progressBar.setString("");
        updateActionsButton();
    }

    public void stopAllOpeartions() {
        for (Iterator<LongOpStruct> i = operations.values().iterator(); i.hasNext();) {
            LongOpStruct o = i.next();
            try {
                o.stop();
                Log.trace("Op\u00E9ration interrompue : " + o.title);
            } catch (Exception e) {
                Log.trace("Op\u009Eration interrompue avec erreur : " + o.title + " : " + e);
            }

        }
        operations.clear();
        progressBar.setIndeterminate(false);
        progressBar.setString("");
        updateActionsButton();
    }
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00%");

    private void tick() {
        int index = 0;
        for (Iterator<LongOpStruct> i = operations.values().iterator(); i.hasNext();) {
            LongOpStruct o = i.next();
            if (currentRunningActionIndex == index) {
                RunAction runAction = o.getRunAction();
                if (runAction != null) {
                    double d = runAction.getProgressValue();
                    long evolved = runAction.getDuration();
                    long remaining = runAction.getEstimatedRemainingDuration();
                    progressBar.setIndeterminate(false);
                    progressBar.setValue((int) (d * 100));
                    progressBar.setString("Completed : " + decimalFormat.format(d) + " - "
                            + "Time evolved : " + Utils.getPeriodDescription(evolved, Calendar.SECOND) + " - "
                            + "Time remaining : " + (remaining < 0 ? "???" : Utils.getPeriodDescription(remaining, Calendar.SECOND)));
                } else {
                    progressBar.setIndeterminate(true);
                    progressBar.setString("");
                }
                return;
            }
            index++;
        }
    }

    public void endLongOperation(Object key) {
        if (key == null) {
            return;
        }
        operations.remove(key);
        if (operations.size() == 0) {
            progressBar.setIndeterminate(false);
            progressBar.setValue(0);
            progressBar.setString("");
        } else {
            progressBar.setString(operations.size() + " op en cours...");
        }
        updateActionsButton();
    }

    public ECExpressionField getFreq() {
        return freq;
    }

    public void showAddArea(AreaMaterial material) {
        AreaEditor areaEditor = new AreaEditor(this, material);
        while (true) {
            int btn = JOptionPane.showConfirmDialog(this, areaEditor, getResources().get("addArea"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (btn == JOptionPane.OK_OPTION) {
                try {
                    Area a = areaEditor.getArea();
                    currentProject.addArea(a);
                    return;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                }
            } else {
                return;
            }
        }
    }

    public void removeArea(Area area) {
        currentProject.removeArea(area);
    }

    public double getDimUnitValue() {
        return ((Double) miscPanel.dimUnitComponent.getHelper().getObject(false)).doubleValue();
    }

    public double getFreqUnitValue() {
        return ((Double) miscPanel.freqUnitComponent.getHelper().getObject(false)).doubleValue();
    }

    public void addArea(Area a) {
        currentProject.addArea(a);
    }

    public void showEdit(AreaZone zone) {
        if (zone == null) {
            return;
        }
        if (zone instanceof Area) {
            Area area = (Area) zone;
            if (area.getParentGroup() == null) {
                System.out.println("How come?");
            }
//        if(area instanceof BoxArea){
//            configureDielectric();
//            return;
//        }
            AreaEditor areaEditor = new AreaEditor(this, area.getMaterial());
            areaEditor.setArea(area);
            while (true) {
                int btn = JOptionPane.showConfirmDialog(this, areaEditor, getResources().get("editArea"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (btn == JOptionPane.OK_OPTION) {
                    try {
                        Area area2 = areaEditor.getArea();
                        currentProject.updateArea(area, area2);
                        return;
                    } catch (ConstraintsException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        } else if (zone instanceof AreaGroup) {
            AreaGroup group = (AreaGroup) zone;
            AreaGroupEditor areaEditor = new AreaGroupEditor(this);
            areaEditor.setAreaGroup(group);
            while (true) {
                int btn = JOptionPane.showConfirmDialog(this, areaEditor, getResources().get("editArea"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (btn == JOptionPane.OK_OPTION) {
                    try {
                        areaEditor.getAreaGroup(group);
                        return;
                    } catch (ConstraintsException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        }
    }

    private void updateActionsButton() {
        if (currentRunningActionIndex >= operations.size()) {
            currentRunningActionIndex = 0;
        }
        if (currentRunningActionIndex >= 0 && operations.size() > 0) {
            actionsButton.setText((currentRunningActionIndex + 1) + "/" + operations.size());
        } else {
            actionsButton.setText("/");
        }
        tick();
    }

    public void setSelectedArea(Area area) {
        //TODO selectonne dans l'arbre
    }

    public MomProject2DPlotEditor getPlotEditor() {
        return project2DPlotEditor;
    }

    public File getConfigFile() {
        return getProject().getConfigFile();
    }

    public File getWorkDir() {
        return getProject().getWorkDir();
    }

    private class MiscPanel extends JPanel {

        protected ECComboBox dimUnitComponent;
        protected ECComboBox freqUnitComponent;
        protected ECTextArea structDesc;
        protected ECCheckBox structReduceMemoryUsage;

        public MiscPanel() {
            super(new BorderLayout());
            structDesc = new ECTextArea("structDesc", 0, 512, true);
            structReduceMemoryUsage = new ECCheckBox("structReduceMemoryUsage");
            structDesc.getHelper().setDescription(getResources().get("struct.desc"));
            structReduceMemoryUsage.getHelper().setDescription(getResources().get("struct.reduceMemoryUsage"));

            dimUnitComponent = (ECComboBox) DataTypeEditorFactory.forMap("structDimUnit", Maps.fill(new HashList(),
                    new Double(1), "m",
                    new Double(1E-1), "dm",
                    new Double(1E-2), "cm",
                    new Double(1E-3), "mm",
                    new Double(1E-4), "E-4m",
                    new Double(1E-5), "E-5m",
                    new Double(1E-6), "micro-m"), Double.class, false);
            dimUnitComponent.getHelper().setObject(new Double(1E-3));
            dimUnitComponent.getHelper().setDescription(getResources().get("structDimUnit"));
            freqUnitComponent = (ECComboBox) DataTypeEditorFactory.forMap("structFreqUnit", Maps.fill(new HashList(),
                    new Double(1), "Hz",
                    new Double(1E3), "KHz",
                    new Double(1E6), "MHz",
                    new Double(1E9), "GHz"), Double.class, false);
            freqUnitComponent.getHelper().setObject(new Double(1E9));
            freqUnitComponent.getHelper().setDescription(getResources().get("structFreqUnit"));
            freqUnitComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, revalidateLamdaPropertyChangeListener);
            dimUnitComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, revalidateLamdaPropertyChangeListener);
            ECGroupPanel titlePanel = new ECGroupPanel();
            titlePanel.add(new DataTypeEditor[]{dimUnitComponent, freqUnitComponent, structReduceMemoryUsage, structDesc}, 1);
            structDesc.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    updateWindowTitle();
                }
            });
            add(titlePanel);
        }

        public void load(MomProject str) {
            structReduceMemoryUsage.getHelper().setObject(str.isReduceMemoryUsage());
            structDesc.getHelper().setObject(str.getDescription());
            dimUnitComponent.getHelper().setObject(new Double(str.getDimensionUnit()));
            freqUnitComponent.getHelper().setObject(new Double(str.getFrequencyUnit()));
        }

        public void store(MomProject str) {
            str.setDimensionUnit(((Double) dimUnitComponent.getHelper().getObject(false)).doubleValue());
            str.setFrequencyUnit(((Double) freqUnitComponent.getHelper().getObject(false)).doubleValue());
            str.setDescription(structDesc.getString());
            str.setReduceMemoryUsage(structReduceMemoryUsage.isSelected());
        }
    }

    private class LayersPanel extends JPanel {

        private ECComboBox topLimitType;
        private ECComboBox bottomLimitType;
        private ECExpressionField topEpsr;
        private ECExpressionField bottomEpsr;
        private ECExpressionField topThikness;
        private ECExpressionField bottomThikness;
        private LayersList layersList;

        public LayersPanel() {
            super(new BorderLayout());
            layersList = new LayersList(MomProjectEditor.this);
            bottomLimitType = (ECComboBox) DataTypeEditorFactory.forEnum("bottomLimitType", Boundary.class, false, getResources());
            bottomLimitType.getHelper().setObject(Boundary.ELECTRIC);
            bottomLimitType.getHelper().setDescription(getResources().get("bottomLimitType"));
            topEpsr = new ECExpressionField("topEpsr", false);
            topEpsr.getHelper().setDescription(getResources().get("topEpsr"));
            topEpsr.getHelper().setObject("1");

            bottomEpsr = new ECExpressionField("bottomEpsr", false);
            bottomEpsr.getHelper().setDescription(getResources().get("bottomEpsr"));
            bottomEpsr.getHelper().setObject("1");

            topThikness = new ECExpressionField("topThikness", false);
            topThikness.getHelper().setDescription(getResources().get("topThikness"));
            topThikness.getHelper().setObject("0");

            bottomThikness = new ECExpressionField("bottomThikness", false);
            bottomThikness.getHelper().setDescription(getResources().get("bottomThikness"));
            bottomThikness.getHelper().setObject("0");
            topLimitType = (ECComboBox) DataTypeEditorFactory.forEnum("topLimitType", Boundary.class, false, getResources());
            topLimitType.getHelper().setDescription(getResources().get("topLimitType"));
            topLimitType.getHelper().setObject(Boundary.ELECTRIC);

            ECGroupPanel ecGroupPanel = new ECGroupPanel();
            ecGroupPanel.add(new DataTypeEditor[]{topLimitType, topThikness, topEpsr, bottomLimitType, bottomThikness, bottomEpsr}, 3).setBorder(BorderFactory.createTitledBorder(getResources().get("box.limits")));
            add(ecGroupPanel, BorderLayout.PAGE_START);
            add(layersList, BorderLayout.CENTER);
            PropertyChangeListener p = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    revalidateAll();
                }
            };
            ecGroupPanel.getGroup().addValueChangeListener(new String[]{"topLimitType", "bottomLimitType"}, p);
            projectType.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, p);
            revalidateAll();
        }

        public void load(MomProject str) {
            topLimitType.getHelper().setObject(str.getLayers().getTopLimit());
            topEpsr.getHelper().setObject(str.getLayers().getTopEpsrExpression());
            topThikness.getHelper().setObject(str.getLayers().getTopThicknessExpression());
            bottomEpsr.getHelper().setObject(str.getLayers().getBottomEpsrExpression());
            bottomThikness.getHelper().setObject(str.getLayers().getBottomThicknessExpression());
            bottomLimitType.getHelper().setObject(str.getLayers().getBottomLimit());
            List<MomProjectExtraLayer> pl = str.getLayers().getExtraLayers();
            layersList.setLayers(pl.toArray(new MomProjectExtraLayer[pl.size()]));
        }

        public void store(MomProject str) {
            str.getLayers().setTopLimit((Boundary) topLimitType.getHelper().getObject());
            str.getLayers().setBottomLimit((Boundary) bottomLimitType.getHelper().getObject());
            str.getLayers().setTopThicknessExpression(topThikness.getString());
            str.getLayers().setBottomThicknessExpression(bottomThikness.getString());
            str.getLayers().setTopEpsrExpression(topEpsr.getString());
            str.getLayers().setBottomEpsrExpression(bottomEpsr.getString());
            str.getLayers().setExtraLayers(Arrays.asList(layersList.getLayers()));
        }

        private void revalidateAll() {
            if (ProjectType.PLANAR_STRUCTURE.equals(projectType.getHelper().getObject())) {
                bottomThikness.setEnabled(true);
                topThikness.setEnabled(true);
                topLimitType.setEnabled(true);
                bottomLimitType.setEnabled(true);

                Boundary top = (Boundary) topLimitType.getHelper().getObject();
                switch (top) {
                    //matched load
                    case INFINITE: {
                        topThikness.getHelper().setObject("infinity");
                        topThikness.setEnabled(false);
                        break;
                    }
                    case OPEN: {
                        topThikness.getHelper().setObject("1");
                        topThikness.setEnabled(true);
                        break;
                    }
                    case ELECTRIC: {
                        topThikness.getHelper().setObject("1");
                        topThikness.setEnabled(true);
                        break;
                    }
                    case NOTHING: {
                        bottomThikness.getHelper().setObject("0");
                        bottomThikness.setEnabled(false);
                        break;
                    }
                }
                Boundary bottom = (Boundary) bottomLimitType.getHelper().getObject();
                switch (bottom) {
                    //matched load
                    case INFINITE: {
                        bottomThikness.getHelper().setObject("infinity");
                        bottomThikness.setEnabled(false);
                        break;
                    }
                    case OPEN: {
                        bottomThikness.getHelper().setObject("1");
                        bottomThikness.setEnabled(true);
                        break;
                    }
                    case ELECTRIC: {
                        bottomThikness.getHelper().setObject("1");
                        bottomThikness.setEnabled(true);
                        break;
                    }
                    case NOTHING: {
                        bottomThikness.getHelper().setObject("0");
                        bottomThikness.setEnabled(false);
                        break;
                    }
                }
            } else {
                bottomThikness.setEnabled(false);
                topThikness.setEnabled(false);
                topLimitType.setEnabled(false);
                bottomLimitType.setEnabled(false);
                bottomLimitType.getHelper().setObject(Boundary.INFINITE);
                topLimitType.getHelper().setObject(Boundary.INFINITE);
            }
        }
    }

    public Resources getResources() {
        if (application == null) {
            return HWSTools.RESOURCES;
        }
        return application.getResources();
    }

    private class BoxPanel extends JPanel {

        private ECComboBox wallBorderNorth;
        private ECComboBox wallBorderSouth;
        private ECComboBox wallBorderEast;
        private ECComboBox wallBorderWest;
        private ECExpressionField areaX;
        private ECExpressionField areaY;
        private ECExpressionField areaWidth;
        private ECExpressionField areaHeight;
        private boolean loading = false;

        public BoxPanel() {
            super(new BorderLayout());
            wallBorderNorth = (ECComboBox) DataTypeEditorFactory.forEnum("wallBorderNorth", Boundary.class, false, null);
            wallBorderNorth.getHelper().setDescription("Mur du Nord");
            wallBorderNorth.getHelper().setObject(Boundary.ELECTRIC);
            wallBorderSouth = (ECComboBox) DataTypeEditorFactory.forEnum("wallBorderSouth", Boundary.class, false, null);
            wallBorderSouth.getHelper().setDescription("Mur du Sud");
            wallBorderSouth.getHelper().setObject(Boundary.ELECTRIC);
            wallBorderWest = (ECComboBox) DataTypeEditorFactory.forEnum("wallBorderWest", Boundary.class, false, null);
            wallBorderWest.getHelper().setDescription("Mur du Ouest");
            wallBorderWest.getHelper().setObject(Boundary.ELECTRIC);
            wallBorderEast = (ECComboBox) DataTypeEditorFactory.forEnum("wallBorderEast", Boundary.class, false, null);
            wallBorderEast.getHelper().setDescription("Mur de l'Est");
            wallBorderEast.getHelper().setObject(Boundary.ELECTRIC);

            areaX = new ECExpressionField("x", false);
            areaX.getHelper().setDescription(getResources().get("x"));
            areaY = new ECExpressionField("y", false);
            areaY.getHelper().setDescription(getResources().get("y"));
            areaWidth = new ECExpressionField("width", false);
            areaWidth.getHelper().setDescription(getResources().get("width"));
            areaHeight = new ECExpressionField("height", false);
            areaHeight.getHelper().setDescription(getResources().get("height"));
            areaX.getHelper().setObject("0");
            areaY.getHelper().setObject("0");
            areaWidth.getHelper().setObject("100");
            areaHeight.getHelper().setObject("100");

            ECGroupPanel ecGroupPanel = new ECGroupPanel();
            ecGroupPanel.add(new DataTypeEditor[]{areaX, areaWidth, areaY, areaHeight}, 2).setBorder(BorderFactory.createTitledBorder(getResources().get("Dimension.title")));
            ecGroupPanel.add(new DataTypeEditor[]{
                null, wallBorderNorth, null,
                wallBorderWest, null, wallBorderEast,
                null, wallBorderSouth, null
            }, 3).setBorder(BorderFactory.createTitledBorder(getResources().get("box.walls")));
            add(ecGroupPanel);
            ecGroupPanel.getGroup().addValueChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if (!loading) {
                        getProject();
                    }
                }
            });

        }

        public void load(MomProject str) {
            loading = true;
            try {
                wallBorderWest.getHelper().setObject(str.getWallBorders().west);
                wallBorderEast.getHelper().setObject(str.getWallBorders().east);
                wallBorderNorth.getHelper().setObject(str.getWallBorders().north);
                wallBorderSouth.getHelper().setObject(str.getWallBorders().south);

                areaX.getHelper().setObject(str.xExpression);
                areaY.getHelper().setObject(str.yExpression);
                areaWidth.getHelper().setObject(str.widthExpression);
                areaHeight.getHelper().setObject(str.heightExpression);
            } finally {
                loading = false;
            }
        }

        public void store(MomProject str) {
            str.setWallBorders(WallBorders.valueOf((Boundary) (wallBorderNorth.getHelper().getObject()),
                            (Boundary) (wallBorderEast.getHelper().getObject()),
                            (Boundary) (wallBorderSouth.getHelper().getObject()),
                            (Boundary) (wallBorderWest.getHelper().getObject())));
            str.setXExpression((String) areaX.getHelper().getObject());
            str.setYExpression((String) areaY.getHelper().getObject());
            str.setWidthExpression((String) areaWidth.getHelper().getObject());
            str.setHeightExpression((String) areaHeight.getHelper().getObject());
        }
    }

    private class VarsPanel extends JPanel {

        private VarList varList;

        public VarsPanel() {
            super(new BorderLayout());
            varList = new VarList(MomProjectEditor.this);
            varList.setBorder(BorderFactory.createTitledBorder(getResources().get("variables")));
            varList.addVariableListener(revalidateLamdaPropertyChangeListener);
            varList.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if (!isUpdatingProject && evt.getPropertyName().startsWith("VariableExpressionList.")) {
                        try {
                            MomProject prj = getProject();
                            groupTreeEditor.repaint();
                            project2DPlotEditor.repaint();
                        } catch (Exception e1) {
                        }
                    }
                }
            });
            add(varList);
        }

        public void load(MomProject str) {
            varList.setVariableExpressions(str.getVariableExpressions());
        }

        public void store(MomProject str) {
        }
    }

    private class OptimizationPanel extends JPanel {

        private ECExpressionField maxModes;
        ECComboBox hintGpFnAxisType;
        ECCheckBox[] hintFnModes;
        private ECCheckBox hintRegularZOperator;
        private ECCheckBox hintAmatrixSparsify;
        private ECCheckBox hintBmatrixSparsify;
        private ECExpressionField hintDiscardFnByScalarProduct;

        public OptimizationPanel() {
            super(new BorderLayout());
            ECGroupPanel panel = new ECGroupPanel();
            hintGpFnAxisType = (ECComboBox) DataTypeEditorFactory.forEnum("hintGpFnAxisType", HintAxisType.class, true, null);
            hintGpFnAxisType.getHelper().setDescription("hintGpFnAxisType");
            //hintGpFnAxisType.setObject(HintGpFnAxisType.XY_DEFAULT);

            hintRegularZOperator = new ECCheckBox("hintRegularZOperator");
            hintRegularZOperator.getHelper().setDescription("hintRegularZOperator");
            hintAmatrixSparsify = new ECCheckBox("hintAmatrixSparsify");
            hintAmatrixSparsify.getHelper().setDescription("hintAmatrixSparsify");
            hintBmatrixSparsify = new ECCheckBox("hintBmatrixSparsify");
            hintBmatrixSparsify.getHelper().setDescription("hintBmatrixSparsify");

            hintDiscardFnByScalarProduct = new ECExpressionField("hintDiscardFnByScalarProduct", true);
            hintDiscardFnByScalarProduct.getHelper().setDescription("hintDiscardFnByScalarProduct");

            maxModes = new ECExpressionField("maxMTE", false);
            maxModes.getHelper().setDescription(getResources().get("maxFn"));
            maxModes.getHelper().setObject("1000");
//        maxMTE.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY,handler);
            maxModes.getHelper().setSuffixComponent(new JLabel());
            maxModes.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    DataTypeEditor e = (DataTypeEditor) evt.getSource();
                    JLabel l = (JLabel) e.getHelper().getSuffixComponent();
                    String n = (String) e.getHelper().getObject(false);
                    if (n == null) {
                        l.setText("");
                    } else {
                        //TODO reste a voir comment faire ca
                    }
//                l.setText(n==null ? "" : String.valueOf(
//                        n.intValue()*n.intValue()));
                }
            });

            ModeType[] allModes = ModeType.values();
            hintFnModes = new ECCheckBox[ModeType.values().length];
            for (int i = 0; i < hintFnModes.length; i++) {
                hintFnModes[i] = new ECCheckBox(allModes[i].name());
                hintFnModes[i].getHelper().setObject(true);
                hintFnModes[i].getHelper().setDescription("Enable " + allModes[i].name());
            }
            DataTypeEditor[] comps = new DataTypeEditor[hintFnModes.length + 1];
            System.arraycopy(hintFnModes, 0, comps, 1, hintFnModes.length);
            comps[0] = maxModes;
            panel.add(comps, 1).setBorder(BorderFactory.createTitledBorder(getResources().get("OptimizationPanel.modes")));
            panel.add(new DataTypeEditor[]{hintGpFnAxisType, hintRegularZOperator, hintAmatrixSparsify, hintBmatrixSparsify, hintDiscardFnByScalarProduct}, 1).setBorder(BorderFactory.createTitledBorder(getResources().get("OptimizationPanel.hints")));

            this.add(panel);
        }

        public void load(MomProject str) {
            hintGpFnAxisType.getHelper().setObject(str.getHintAxisType());
            ModeType[] allSelectedModes = str.getHintFnModes();
            if (allSelectedModes != null) {
                for (ModeType mode : allSelectedModes) {
                    hintFnModes[mode.ordinal()].getHelper().setObject(Boolean.TRUE);
                }
            }

            hintRegularZOperator.getHelper().setObject(str.isHintRegularZOperator());
            hintAmatrixSparsify.getHelper().setObject(str.isHintAmatrixSparsify());
            hintBmatrixSparsify.getHelper().setObject(str.isHintBmatrixSparsify());
            hintDiscardFnByScalarProduct.getHelper().setObject(str.getHintDiscardFnByScalarProductExpression());
            maxModes.getHelper().setObject(str.getMaxModesExpression());
        }

        public void store(MomProject str) {
            str.setMaxModesExpression((String) maxModes.getHelper().getObject());
            str.setHintAxisType((HintAxisType) hintGpFnAxisType.getHelper().getObject());
            ModeType[] allModes = ModeType.values();

            ArrayList<ModeType> allSelectedModes = new ArrayList<ModeType>();
            for (int i = 0; i < hintFnModes.length; i++) {
                ECCheckBox eCCheckBox = hintFnModes[i];
                if (eCCheckBox.isSelected()) {
                    allSelectedModes.add(allModes[i]);
                }
            }
            str.setHintFnModes(allSelectedModes.toArray(new ModeType[allSelectedModes.size()]));
            str.setHintRegularZOperator((Boolean) hintRegularZOperator.getHelper().getObject());
            str.setHintAmatrixSparsify((Boolean) hintAmatrixSparsify.getHelper().getObject());
            str.setHintBmatrixSparsify((Boolean) hintBmatrixSparsify.getHelper().getObject());
            str.setHintDiscardFnByScalarProductExpression((String) hintDiscardFnByScalarProduct.getHelper().getObject());

        }
    }

    public ProjectType getProjectType() {
        return (ProjectType) projectType.getHelper().getObject();
    }

    public TMWLabApplication getApplication() {
        return application == null ? null : (TMWLabApplication) (application.getApplication());
    }

    public ApplicationRenderer getApplicationRenderer() {
        return application;
    }

    public void registerUIFactory(MomUIFactory item) {
        factories.put(item.getId(), item);
    }

    public List<MomUIFactory> getAllByType(Class clz) {
        ArrayList<MomUIFactory> a = new ArrayList<MomUIFactory>();
        for (MomUIFactory momProjectItem : factories.values()) {
            if (clz.isInstance(momProjectItem)) {
                a.add(momProjectItem.create());
            }
        }
        return a;
    }

    public MomUIFactory getUIFactory(String id) {
        MomUIFactory item = factories.get(id);
        if (item == null) {
            throw new NoSuchElementException("Unknown UIFactory " + id);
        }
        return item;
    }

    public void registerDefaults() {
        registerUIFactory(new RectAreaShapeEditor(this));
        registerUIFactory(new RooftopGpMesherEditor(this));
        registerUIFactory(new BoxModesGpMesherEditor(this));
        registerUIFactory(new ConstantGpMesherEditor(this));
        registerUIFactory(new PecMaterialEditor(this));
        registerUIFactory(new PlanarSourceMaterialEditor(this));
        registerUIFactory(new ModalSourceMaterialEditor(this));
        registerUIFactory(new SurfaceImpedanceMaterialEditor(this));
    }

    public void registerUIFactory(MomUIFactory item, Class mappedTo, String asProperty) {
        registerUIFactory(item);
        addMapping(mappedTo, asProperty, item);
    }

    private Object getMapping(Class type, String property) {
        return getMapping(type.getName(), property);
    }

    private Object getMapping(String type, String property) {
        String compoundKey = type + "\n" + property;
        return mapping.get(compoundKey);
    }

    private void addMapping(Class type, String property, Object value) {
        addMapping(type.getName(), property, value);
    }

    private void addMapping(String type, String property, Object value) {
        String compoundKey = type + "\n" + property;
        mapping.put(compoundKey, value);
    }

}
