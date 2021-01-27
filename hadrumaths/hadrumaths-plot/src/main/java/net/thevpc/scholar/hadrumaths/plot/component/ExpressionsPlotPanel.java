package net.thevpc.scholar.hadrumaths.plot.component;

import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.scholar.hadrumaths.plot.model.ExpressionsPlotModel;
import net.thevpc.scholar.hadrumaths.Samples;
import net.thevpc.common.swing.*;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.*;
import net.thevpc.scholar.hadruplot.util.PlotSwingUtils;
import org.jfree.chart.plot.DefaultDrawingSupplier;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.*;
import java.util.function.ToDoubleFunction;
import net.thevpc.scholar.hadrumaths.plot.FunctionsXYTableModel;
import net.thevpc.scholar.hadrumaths.plot.PlotDomainFromDomain;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

//import net.thevpc.scholar.math.functions.dfxy.DFunctionVector2D;
/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2005 10:41:53
 */
public class ExpressionsPlotPanel extends BasePlotComponent implements PlotPanel {

    //    private double[] x;
    //    private double[] y;
//    JLabel xValueLabel = new JLabel();
//    JLabel yValueLabel = new JLabel();
    //    public int[] getSelectedIndexes() {
//        ArrayList<Integer> arrayList = new ArrayList<Integer>(functionCheckBoxes.length);
//        for (int i = 0; i < functionCheckBoxes.length; i++) {
//            if (functionCheckBoxes[i].isSelected()) {
//                arrayList.add(i);
//            }
//        }
//        int[] ret = new int[arrayList.size()];
//        for (int i = 0; i < arrayList.size(); i++) {
//            ret[i] = (arrayList.get(i));
//        }
//        return ret;
    //    }
    boolean isUpdating = false;
    boolean again = false;
    FunctionsXYTableModel functionsTableModel;
    private final int maxPrec = 1000;
    private ExpressionsPlotModel model;
    //    private Expr[] functions;
//    private Map<String, Object> properties = new HashMap<String, Object>();
    private final JCheckBox autoUpdate = prepareStatusCheck(true, "auto", "auto update");
    private final JCheckBox exclusive = prepareStatusCheck(true, "exclusive", "exclusive selection");
    private JSpinner maxSelectionSpinner;
    //    private JCheckBox[] functionCheckBoxes;
    private final JTextField yValueSliderText = new JTextField(10);
    private final JSlider yValueSlider;
    private final JTextField yPrecisionSliderText = new JTextField(10);
    private final JSlider yPrecisionSlider;
    private final JTextField xValueSliderText = new JTextField(10);
    private final JSlider xValueSlider;
    private final JTextField xPrecisionSliderText = new JTextField(10);
    private PlotSamples baseSamples;
    private final JSlider xPrecisionSlider;
    private final JPanel mainPanel;
    //    private String title;
    private JTable functionsTable;
    //    private Domain domainXY;
    private Domain domainXY0;
    private final JCheckBox fxRadioButton = new JCheckBox("F(x,y=cst)", true);
    private final JCheckBox fyRadioButton = new JCheckBox("F(x=cst,y)", true);

    private final JCheckBox fxAxisRadioButton = new JCheckBox("Fx(x,y)", true);
    private final JCheckBox fyAxisRadioButton = new JCheckBox("Fy(x,y)", true);
    //    private JRadioButton cadRealRadioButton = createJRadioButton("Real", PlotDoubleConverter.REAL);
//    private JRadioButton cadImagRadioButton = createJRadioButton("Imag", PlotDoubleConverter.IMG);
//    private JRadioButton cadDbRadioButton = createJRadioButton("DB", PlotDoubleConverter.DB);
//    private JRadioButton cadDb2RadioButton = createJRadioButton("DB2", PlotDoubleConverter.DB2);
//    private JRadioButton cadArgRadioButton = createJRadioButton("Arg", PlotDoubleConverter.ARG);
//    private JRadioButton cadComplexRadioButton = createJRadioButton("Complex", "COMPLEX");
//    private JRadioButton cadAbsRadioButton = createJRadioButton("Abs", PlotDoubleConverter.ABS);
    private final java.util.List<JRadioButton> cadRadioButtons = new ArrayList<>();

    private JRadioButton[] plotTypeRadioButton;
    private JRadioButton[] stacksRadioButton;
    //    private JRadioButton fx1DRadioButton = new JRadioButton("Curve F(x,y=cst)");
//    private JRadioButton fy1DRadioButton = new JRadioButton("Curve F(x=cst,y)");
//    private JRadioButton fyPolarRadioButton = new JRadioButton("Polar");
//    private JRadioButton fxy2DRadioButton = new JRadioButton("Surface 2D : F(x,y)");
//    private JRadioButton fxyMatrixRadioButton = new JRadioButton("Colourful Matrix : F(x,y)");
//    private JRadioButton fxy3DRadioButton = new JRadioButton("Mesh 3D : F(x,y)");
//    private JRadioButton tableRadioButton = new JRadioButton("Table");
    private Expr[] selectedFunctions = new DoubleToVector[0];
    private int[] selectedFunctionsIndexes = new int[0];
    //    private JComboBox showType = new JComboBox(ShowType.values());
    private final JProgressBar updatingProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
    //    private JLabel yPrecisionLabel = new JLabel("");
//    private JLabel xPrecisionLabel = new JLabel("");
    private final JLabel yValueTitleLabel = new JLabel("");
    private final JLabel xValueTitleLabel = new JLabel("");
    private final JButton updateNow = prepareButton("u", "update plot");
    private final JButton selectAllByCellFunctions = prepareButton("+?", "Select By Cell");
    private final JButton selectNoneByCellFunctions = prepareButton("-?", "Deselect By Cell");
    private final JButton selectAllFunctions = prepareButton("++", "Select All");
    private final JButton selectNoneFunctions = prepareButton("--", "Select None");
    //    private ButtonGroup fctGroup = new ButtonGroup();
    private final JButton showContent = prepareButton("?", "Show Content");
    private final ItemListener updatePlotItemListener = new UpdatePlotItemListener();
    private boolean disableUpdatePlot = false;
    private boolean explodedMode = false;
    PropertyChangeListener modelUpdatesListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updatePlotAsynch(true);
        }
    };
    private final LinkedHashSet<String> selectedRows = new LinkedHashSet<>();

//    private ActionListener updatePlotActionListener = new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//            updatePlot(false);
//        }
    //    };
    public ExpressionsPlotPanel(ExpressionsPlotModel model, PlotWindowManager windowManager) {
        super(new BorderLayout());
        if (model == null) {
            model = new ExpressionsPlotModel();
        }

        for (ToDoubleFunction<Object> value : PlotDoubleConverter.values()) {
            cadRadioButtons.add(createJRadioButton(value.toString(), value));
        }

        this.model = model;
        this.domainXY0 = null;

        setPlotWindowManager(windowManager);

        fxAxisRadioButton.setSelected(false);
        fyAxisRadioButton.setSelected(false);
        ButtonGroup fxyRadioButton = new ButtonGroup();
        fxyRadioButton.add(fxRadioButton);
        fxyRadioButton.add(fyRadioButton);
        fxRadioButton.setSelected(true);
        fyRadioButton.setSelected(false);
        fxRadioButton.addItemListener(updatePlotItemListener);
        setConverter(PlotDoubleConverter.ABS);
        // check domain
//        try {
//            getDomain();
//        }catch(Exception e){
//            System.err.println(e);
//        }

//        yTextField=new JTextField();
        this.yValueSlider = new JSlider(JSlider.HORIZONTAL, 0, maxPrec, 500);
        this.yValueSlider.setName("Y value");
//        this.yValueSlider.setValue(500);
        this.yValueSlider.setToolTipText(String.valueOf(yValueSlider.getValue()));
        this.yValueSlider.setPaintLabels(true);
        this.yValueSliderText.setText(String.valueOf(getFunctionYValueSlider()));
        this.yPrecisionSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 1);
        this.yPrecisionSlider.setName("Y precision");
//        this.yPrecisionSlider.setValue(500);
        this.yPrecisionSlider.setToolTipText(String.valueOf(yPrecisionSlider.getValue()));
        this.yPrecisionSlider.setPaintLabels(true);
        this.yPrecisionSliderText.setText(String.valueOf(getYPrecisionValue()));

        this.xValueSlider = new JSlider(JSlider.HORIZONTAL, 0, maxPrec, 500);
        this.xValueSlider.setName("X value");
//        this.xValueSlider.setValue(500);
        this.xValueSlider.setToolTipText(String.valueOf(xValueSlider.getValue()));
        this.xValueSlider.setPaintLabels(true);
        this.xValueSliderText.setText(String.valueOf(getFunctionXValueSlider()));

        this.xPrecisionSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 100);
        this.xPrecisionSlider.setName("X precision");
//        this.xPrecisionSlider.setValue(500);
        this.xPrecisionSlider.setToolTipText(String.valueOf(xPrecisionSlider.getValue()));
        this.xPrecisionSlider.setPaintLabels(true);
        this.xPrecisionSliderText.setText(String.valueOf(getXPrecisionValue()));

        yValueSliderText.setMinimumSize(new Dimension(100, 0));
        yValueSliderText.setMaximumSize(new Dimension(100, 10));
        yValueSliderText.setSize(new Dimension(100, 10));

        yPrecisionSliderText.setMinimumSize(new Dimension(100, 0));
        yPrecisionSliderText.setMaximumSize(new Dimension(100, 10));
        yPrecisionSliderText.setSize(new Dimension(100, 10));

        xValueSliderText.setMinimumSize(new Dimension(100, 0));
        xValueSliderText.setMaximumSize(new Dimension(100, 10));
        xValueSliderText.setSize(new Dimension(100, 10));

        xPrecisionSliderText.setMinimumSize(new Dimension(100, 0));
        xPrecisionSliderText.setMaximumSize(new Dimension(100, 10));
        xPrecisionSliderText.setSize(new Dimension(100, 10));

        yValueSlider.addChangeListener(new SerializableChangeListener() {

            public void stateChanged(ChangeEvent e) {
                onYValueChange();
            }
        });

        xValueSlider.addChangeListener(new SerializableChangeListener() {

            public void stateChanged(ChangeEvent e) {
                onXValueChange();
            }
        });

        yPrecisionSlider.addChangeListener(new SerializableChangeListener() {

            public void stateChanged(ChangeEvent e) {
                onYPrecisionChange();
            }
        });

        xPrecisionSlider.addChangeListener(new SerializableChangeListener() {

            public void stateChanged(ChangeEvent e) {
                onXPrecisionChange();
            }
        });

//        ySlider.setLabelTable((Hashtable) Maps.fill(new Hashtable(),
//                new Integer(0), new JLabel(String.valueOf(area.domain.ymin)),
//                new Integer(500), new JLabel(String.valueOf(area.domain.ymin + (area.domain.ymax - area.domain.ymin) * 0.5)),
//                new Integer(1000), new JLabel(String.valueOf((area.domain.ymax)))));
        xValueTitleLabel.setText(xValueSlider.getName());

        yValueTitleLabel.setText(yValueSlider.getName());

        JPanel p = new JPanel(new BorderLayout());
        p.add(createFunctionTabPane(), BorderLayout.CENTER);
        p.add(createStatusbar(), BorderLayout.SOUTH);

        JTabbedPane config = new JTabbedPane(JTabbedPane.LEFT);
        config.addTab("Functions", p);
        config.addTab("Layout", createGeneralPane(PlotType.CURVE));
        config.addTab("Precision", createPrecisionPane());
//        config.setPreferredSize(new Dimension(100,100));

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(config, BorderLayout.CENTER);
        p2.add(createStatusbar2(), BorderLayout.SOUTH);

        mainPanel = new JPanel(new GridLayout(1, 0));
        mainPanel.setPreferredSize(new Dimension(400, 300));
        JSplitPane verticalSlider = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSlider.setTopComponent(mainPanel);
        verticalSlider.setBottomComponent(p2);
        verticalSlider.setOneTouchExpandable(true);
        verticalSlider.setResizeWeight(0.7);
        verticalSlider.setDividerLocation(0.1);

        verticalSlider.getLeftComponent().setMinimumSize(new Dimension());
        verticalSlider.getRightComponent().setMinimumSize(new Dimension());
        verticalSlider.setDividerLocation(1.0d);
        verticalSlider.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                int maximumDividerLocation = verticalSlider.getMaximumDividerLocation();
                verticalSlider.setDividerLocation(maximumDividerLocation);
//                verticalSlider.setDividerLocation(1.0d);
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {
                int maximumDividerLocation = verticalSlider.getMaximumDividerLocation();
                verticalSlider.setDividerLocation(maximumDividerLocation);
//                verticalSlider.setDividerLocation(1.0d);
            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        //setPreferredSize(new Dimension(400, 400));
//        onYValueChange();
        this.add(verticalSlider);
        yValueSliderText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Domain d = getDomainOrNull();
                if (d == null) {
                    return;
                }
                updateSliderDouble(yValueSlider, yValueSliderText, d.ymin(), d.ymax());
            }
        });
        xValueSliderText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Domain d = getDomainOrNull();
                if (d == null) {
                    return;
                }
                updateSliderDouble(xValueSlider, xValueSliderText, d.xmin(), d.xmax());
            }
        });
        xPrecisionSliderText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateSliderInt(xPrecisionSlider, xPrecisionSliderText);
            }
        });
        yPrecisionSliderText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateSliderInt(yPrecisionSlider, yPrecisionSliderText);
            }
        });
        setModel(model);
    }

    //    public ExpressionsPlotPanel(String title, int xprec, int yprec, DoubleToComplex[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//        this(title, null, xprec, yprec, PlotDoubleConverter.ABS, functions, showType, properties, windowManager);
//    }
//
//    public ExpressionsPlotPanel(String title, int xprec, int yprec, DoubleToVector[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//        this(title, null, xprec, yprec, PlotDoubleConverter.ABS, functions, showType, properties, windowManager);
//    }
//
//    public ExpressionsPlotPanel(String title, int xprec, int yprec, DoubleToDouble[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//        this(title, null, xprec, yprec, PlotDoubleConverter.REAL, convert(functions), showType, properties, windowManager);
//    }
//    public ExpressionsPlotPanel(String title, int xprec,int yprec,DFunctionVector2D[] functions, PlotWindowManager windowManager) {
//        this(title, null, xprec,yprec,PlotDoubleConverter.REAL, convert(functions), windowManager);
//    }
//    public ExpressionsPlotPanel(String title, Domain domainXY, int xprec, int yprec, PlotDoubleConverter complexAsDouble, DoubleToComplex[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//        this(title, domainXY, xprec, yprec, complexAsDouble, convert(functions), showType, properties, windowManager);
//    }
//    public ExpressionsPlotPanel(String title, Domain domainXY, int xprec, int yprec, PlotDoubleConverter complexAsDouble, Expr[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//
//    }
    private static JRadioButton createJRadioButton(String name, Object value) {
        JRadioButton r = new JRadioButton(name);
        r.putClientProperty("Object", value);
        return r;
    }

    private static DoubleToVector[] convert(DoubleToComplex[] f) {
        DoubleToVector[] g = new DoubleToVector[f.length];
        for (int i = 0; i < g.length; i++) {
            g[i] = Maths.vector(f[i], Maths.CZEROXY).toDV();
//            g[i].setTitle(f[i].getName());
        }
        return g;
    }

    private static DoubleToVector[] convert(DoubleToDouble[] f) {
        DoubleToVector[] g = new DoubleToVector[f.length];
        for (int i = 0; i < g.length; i++) {
            g[i] = Maths.vector(f[i], Maths.CZEROXY).toDV();
//            g[i].setTitle(f[i].getName());
        }
        return g;
    }

    private void updateSliderInt(JSlider s, JTextField f) {
        int oldValue = s.getValue();
        String t = f.getText();
        s.setValue((int) getDoubleValue(t, oldValue));
    }

    private void updateSliderDouble(JSlider s, JTextField f, double min, double max) {
        int oldValue = s.getValue();
        String t = f.getText();
        double v2 = getDoubleValue(t, oldValue);
        //((((double) yValueSlider.getValue()) / yValueSlider.getMaximum()) * (d.height)) + d.ymin
        int i = (int) (((v2 - min) / (max - min)) * s.getMaximum());
        s.setValue(i);
    }

    private double getDoubleValue(String s, double oldValue) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return oldValue;
        }
    }

    //    private JComponent createFunctionPane(){
//        functionCheckBoxes = new JCheckBox[functions.length];
//        JComponent box = new JPanel(new GridLayout(0,1
////                (functionCheckBoxes.length < 6) ? 1
//                        //: (functionCheckBoxes.length < 12) ? 2
//                        //: (functionCheckBoxes.length < 40) ? 3
////                        : 4
//        ));
//        final JPanel plotArea = new JPanel();
//        plotArea.add(Plot.plotCurves(title, new String[0], new double[0], new double[0][]));
//        for (int i = 0; i < functionCheckBoxes.length; i++) {
//            String fname=String.valueOf(i + 1);
//            if(functions[i].getName()!=null){
//                fname="["+fname+"] "+functions[i].getName();
//            }
//            functionCheckBoxes[i] = new JCheckBox(fname);
//            functionCheckBoxes[i].setSelected(i <= DEFAULT_SELECTED);
//
//            box.add(functionCheckBoxes[i]);
//            functionCheckBoxes[i].addActionListener(updatePlotActionListener);
//        }
//        JPanel b = new JPanel(new BorderLayout());
//        b.add(box, BorderLayout.CENTER);
//        Box bb = Box.createHorizontalBox();
//        bb.add(selectAllFunctions);
//        bb.add(selectNoneFunctions);
//        JScrollPane boxScrollPane = new JScrollPane(box);
//        boxScrollPane.setPreferredSize(new Dimension(100, 150));
//        b.add(boxScrollPane, BorderLayout.CENTER);
//        b.add(bb, BorderLayout.SOUTH);
//
//        selectAllFunctions.addActionListener(
//                new ActionListener() {
//                    public void actionPerformed(ActionEvent e) {
//                        boolean b = autoUpdate.isSelected();
//                        autoUpdate.setSelected(false);
//                        for (JCheckBox checkBox : functionCheckBoxes) {
//                            checkBox.setSelected(true);
//                        }
//                        if (b) {
//                            autoUpdate.setSelected(true);
//                            updatePlot(true);
//                        }
//                    }
//                });
//        selectNoneFunctions.addActionListener(
//                new ActionListener() {
//                    public void actionPerformed(ActionEvent e) {
//                        boolean b = autoUpdate.isSelected();
//                        autoUpdate.setSelected(false);
//                        for (JCheckBox checkBox : functionCheckBoxes) {
//                            checkBox.setSelected(false);
//                        }
//                        if (b) {
//                            autoUpdate.setSelected(true);
//                            updatePlot(true);
//                        }
//                    }
//                });
//        return b;
    //    }
    private JComponent createFunctionTabPane() {
        functionsTableModel = new FunctionsXYTableModel(this.model.getExpressions());

        functionsTableModel.addTableModelListener(new SerializableTableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    selectedFunctions = ((FunctionsXYTableModel) e.getSource()).getSelectedFunctions();
                    selectedFunctionsIndexes = ((FunctionsXYTableModel) e.getSource()).getSelectedFunctionsIndexes();
                    updatePlot(false);
                }
            }
        });

        JTableHelper jTableHelper = SwingUtilities3.createIndexedTable(functionsTableModel);
        functionsTable = jTableHelper.getTable();
        jTableHelper.getPane().setPreferredSize(new Dimension(200, 50));

        selectedFunctions = functionsTableModel.getSelectedFunctions();
        selectedFunctionsIndexes = functionsTableModel.getSelectedFunctionsIndexes();
        return jTableHelper.getPane();
    }

    //    private static IVDCxy[] convert(DFunctionVector2D[] f) {
//        IVDCxy[] g = new IVDCxy[f.length];
//        for (int i = 0; i < g.length; i++) {
//            g[i] = Maths.vector(f[i].fx, f[i].fy);
//            g[i].setTitle(f[i].fx.getName() + ";" + f[i].fy.getName());
//        }
//        return g;
//    }
    public PlotType getShowType() {
        for (int i = 0; i < plotTypeRadioButton.length; i++) {
            if (plotTypeRadioButton[i].isSelected()) {
                return (PlotType) plotTypeRadioButton[i].getClientProperty(PlotType.class.getName());
            }
        }
        return PlotType.HEATMAP;
    }

    public void setPlotType(LibraryPlotType showType) {
        if (showType == null) {
            setPlotType(PlotType.CURVE);
        } else {
            setPlotType(showType.getType());
        }
    }

    public void setPlotType(PlotType showType) {
        for (int i = 0; i < plotTypeRadioButton.length; i++) {
            PlotType clientProperty = (PlotType) plotTypeRadioButton[i].getClientProperty(PlotType.class.getName());
            if (clientProperty == showType) {
                plotTypeRadioButton[i].setSelected(true);
                return;
            }
        }
        plotTypeRadioButton[0].setSelected(true);
    }

    private JPanel createPrecisionPane() {
        GridBagLayout2 layout = new GridBagLayout2(
                "[xValueTitleLabel<  ][xValueSlider          +=][xValueLabel         ->]\n"
                        + "[X               <  ][xPrecisionSlider      +=][xPrecisionLabel     ->]\n"
                        + "[yValueTitleLabel<  ][yValueSlider          +=][yValueLabel         ->]\n"
                        + "[Y               <  ][yPrecisionSlider      +=][yPrecisionLabel     ->]\n"
                        + ""
        );
        layout.setInsets("xValueTitleLabel|yValueTitleLabel|X|Y",new Insets(5,10,0,10));
        layout.setInsets("xValueLabel|xPrecisionLabel|yValueLabel|yPrecisionLabel",new Insets(5,5,0,10));
        JPanel south = new JPanel(layout);
        south.add(xValueTitleLabel, "xValueTitleLabel");
        south.add(xValueSlider, "xValueSlider");
        south.add(xValueSliderText, "xValueLabel");

        south.add(new JLabel(xPrecisionSlider.getName()), "X");
        south.add(xPrecisionSlider, "xPrecisionSlider");
        south.add(xPrecisionSliderText, "xPrecisionLabel");

        south.add(yValueTitleLabel, "yValueTitleLabel");
        south.add(yValueSlider, "yValueSlider");
        south.add(yValueSliderText, "yValueLabel");

        south.add(new JLabel(yPrecisionSlider.getName()), "Y");
        south.add(yPrecisionSlider, "yPrecisionSlider");
        south.add(yPrecisionSliderText, "yPrecisionLabel");
        return south;
    }

    private JPanel createGeneralPane(PlotType showType) {
        updateNow.addActionListener(
                new SerializableActionListener() {

            public void actionPerformed(ActionEvent e) {
                updatePlot(true);
            }
        }
        );
        selectAllFunctions.addActionListener(
                new SerializableActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectAll(true);
            }
        }
        );
        selectAllByCellFunctions.addActionListener(
                new SerializableActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectByCell(true);
            }
        }
        );
        selectNoneByCellFunctions.addActionListener(
                new SerializableActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectByCell(false);
            }
        }
        );
        selectNoneFunctions.addActionListener(
                new SerializableActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectAll(false);
            }
        }
        );
        showContent.addActionListener(
                new SerializableActionListener() {

            public void actionPerformed(ActionEvent e) {
                showContent();
            }
        }
        );
        java.util.List<PlotType> allValue = new ArrayList<>();
        for (PlotType plotType : PlotType.values()) {
            if (plotType != PlotType.ALL && plotType != PlotType.AUTO) {
                allValue.add(plotType);
            }
        }
        JPanel plotTypePanel = null;
        {
            ButtonGroup courbeGroup = new ButtonGroup();
            plotTypeRadioButton = new JRadioButton[allValue.size()];
            for (int i = 0; i < plotTypeRadioButton.length; i++) {
                PlotType tt = allValue.get(i);
                plotTypeRadioButton[i] = PlotSwingUtils.createRadioButton(tt.name(), i == 0, PlotType.class.getName(), tt, courbeGroup, updatePlotItemListener);
            }
            plotTypePanel = PlotSwingUtils.createVerticalComponents("Plot Type", plotTypeRadioButton);
        }
        JPanel explodeStackPanel = null;
        {
            ButtonGroup group = new ButtonGroup();
            java.util.List<Component> aa = new ArrayList<>();

            stacksRadioButton = new JRadioButton[]{
                PlotSwingUtils.createRadioButton("Stack", true, "stackType", "stack", group, new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            explodedMode = false;
                            updatePlotAsynch(true);
                        }
                    }
                }),
                PlotSwingUtils.createRadioButton("Explode", false, "stackType", "explode", group, new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            explodedMode = true;
                            updatePlotAsynch(true);
                        }
                    }
                }),};
            aa.addAll(Arrays.asList(stacksRadioButton));
            Box h = Box.createHorizontalBox();
            h.add(new JLabel("Max Elements : "));
            maxSelectionSpinner = new JSpinner(new SpinnerNumberModel(functionsTableModel.getMaxSelected(), 1, FunctionsXYTableModel.MAX_SELECTED_EXPRESSIONS, 1));
            maxSelectionSpinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    Integer maxSelected = (Integer) maxSelectionSpinner.getValue();
                    functionsTableModel.setMaxSelected(maxSelected);
                    updatePlotAsynch(true);
                }
            });
            exclusive.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent ee) {
                    functionsTableModel.setExclusive(exclusive.isSelected());
                    updatePlotAsynch(true);
                }
            });
            h.add(maxSelectionSpinner);
            aa.add(h);
            explodeStackPanel = PlotSwingUtils.createVerticalComponents("Layout", aa.toArray(new Component[0]));
        }
        functionsTableModel.setExclusive(exclusive.isSelected());
        Integer maxSelected = (Integer) maxSelectionSpinner.getValue();
        functionsTableModel.setMaxSelected(maxSelected);

        JPanel xy = PlotSwingUtils.createVerticalComponents("Components", new Component[]{
            fxRadioButton, fyRadioButton, fxAxisRadioButton, fyAxisRadioButton
        });

        ButtonGroup cadGroup = new ButtonGroup();
        JPanel cad = PlotSwingUtils.createVerticalComponents("Conversion", cadRadioButtons.toArray(new Component[0]));
        for (JRadioButton b : cadRadioButtons) {
            cadGroup.add(b);
            b.addItemListener(updatePlotItemListener);
        }

        JPanel all = new JPanel(new GridBagLayout2(
                "[A1+<=$][A2+<=$][A3+<=$][A4+<=$]\n"));
        all.add(plotTypePanel, "A1");
        all.add(explodeStackPanel, "A2");
        all.add(xy, "A3");
        all.add(cad, "A4");
        setPlotType(showType);
//        fx1DRadioButton.addItemListener(updatePlotItemListener);
//        fy1DRadioButton.addItemListener(updatePlotItemListener);
//        fyPolarRadioButton.addItemListener(updatePlotItemListener);
//        fxy2DRadioButton.addItemListener(updatePlotItemListener);
//        fxy3DRadioButton.addItemListener(updatePlotItemListener);
//        tableRadioButton.addItemListener(updatePlotItemListener);
        fxAxisRadioButton.addItemListener(updatePlotItemListener);
        fyAxisRadioButton.addItemListener(updatePlotItemListener);
        return all;

    }

    private JComponent createStatusbar() {
        JPanel p = new JPanel(
                new GridBagLayout2("[push==-][auto<+][refresh][selectAllFunctions][selectNoneFunctions][selectAllByCellFunctions][selectNoneByCellFunctions][showContent]").setInsets(".*", new Insets(4, 4, 4, 4)));
        p.add(new JLabel(), "push");
        p.add(selectAllFunctions, "selectAllFunctions");
        p.add(selectNoneFunctions, "selectNoneFunctions");
        p.add(selectAllByCellFunctions, "selectAllByCellFunctions");
        p.add(selectNoneByCellFunctions, "selectNoneByCellFunctions");
        p.add(showContent, "showContent");
        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(p, BorderLayout.CENTER);
//        p2.add(updatingProgressBar, BorderLayout.SOUTH);
        return p2;
    }

    private JComponent createStatusbar2() {
        JPanel p = new JPanel(
                new GridBagLayout2("[<updatingProgressBar===-][exclusive<+][auto<+][refresh]").setInsets(".*", new Insets(0, 0, 0, 0)));
        p.add(updatingProgressBar, "updatingProgressBar");
        p.add(autoUpdate, "auto");
        p.add(exclusive, "exclusive");
        p.add(updateNow, "refresh");
        return p;
    }

    private JCheckBox prepareStatusCheck(boolean value, String name, String tooltip) {
        JCheckBox b = new JCheckBox(name);
        b.setSelected(value);
        b.setToolTipText(tooltip);
        Font oldFont = b.getFont();
        b.setFont(oldFont.deriveFont(Font.PLAIN, 9f));
        b.setMargin(new Insets(1, 1, 1, 1));
//        b.setPreferredSize(new Dimension(40,15));
        return b;
    }

    private JButton prepareButton(String name, String tooltip) {
        JButton b = new JButton(name);
        b.setToolTipText(tooltip);
        Font oldFont = b.getFont();
        b.setFont(oldFont.deriveFont(Font.PLAIN, 9f));
        b.setMargin(new Insets(1, 1, 1, 1));
        b.setPreferredSize(new Dimension(40, 15));
        return b;
    }

    public double getFunctionYValue() {
        return getDoubleValue(yValueSliderText.getText(), getFunctionYValueSlider());
    }

    public double getFunctionXValue() {
        return getDoubleValue(xValueSliderText.getText(), getFunctionXValueSlider());
    }

    public double getFunctionYValueSlider() {
        Domain d = getDomainOrNull();
        if (d == null) {
            return 0;
        }
        double v = ((double) yValueSlider.getValue()) / yValueSlider.getMaximum();
        return (v * (d.ywidth())) + d.ymin();
    }

    public double getFunctionXValueSlider() {
        Domain d = getDomainOrNull();
        if (d == null) {
            return 0;
        }
        return ((((double) xValueSlider.getValue()) / xValueSlider.getMaximum()) * (d.xwidth())) + d.xmin();
    }

    public Domain getDomainOrNull() {
        try {
            return getDomain();
        } catch (Exception e) {
            return null;
        }
    }

    private PlotDomain pdom(Domain d) {
        if (d == null) {
            return null;
        }
        return PlotConfigManager.getPlotDomainResolvers().resolve(d);
    }

    private Domain dom(PlotDomain d) {
        if (d == null) {
            return null;
        }
        PlotDomainFromDomain pd = (PlotDomainFromDomain) d;
        return pd.getDomain();
    }

    public Domain getDomain() {
        Domain ret = null;
        if (this.model.getDomain() != null) {
            ret = dom(this.model.getDomain());
        } else if (domainXY0 == null) {
            Domain d = null;
            Domain m = null;
            for (Expr function : this.model.getExpressions()) {
                DoubleToMatrix ddf = function.toDM();
                Domain dd = ddf.getDomain();
                if (dd.isInfinite() && (ddf.isZero() || dd.isNaN())) {
                    //do nothing
                } else {
                    m = m == null ? dd : m.expand(dd);
                }
            }
            for (Expr function : this.model.getExpressions()) {
                for (CellPosition i : getAxis()) {
                    DoubleToComplex ddf = getAxisExpression(function, i.getRow(), i.getColumn());
                    Domain dd = ddf.getDomain();
                    if (dd.isInfinite() && (ddf.isZero() || dd.isNaN())) {
                        //do nothing
                    } else {
                        d = d == null ? dd : d.expand(dd);
                    }
                }
            }
            if (d == null && m == null) {
                domainXY0 = Domain.FULLX;
            } else if (m != null && d == null) {
                domainXY0 = m;
            } else if (d != null && m == null) {
                domainXY0 = d;
            } else {
                domainXY0 = d.intersect(m);
            }
            ret = domainXY0.toDomainXYZ().ensureFiniteBounds(100, 100, 100);
        } else {
            ret = domainXY0.toDomainXYZ().ensureFiniteBounds(100, 100, 100);
        }
        if (ret == null) {
            //never
            throw new IllegalArgumentException("Null Domain to plot");
        }
        if (ret.isEmpty()) {
            throw new IllegalArgumentException("Empty Domain to plot");
        }
        if (ret.isUnbounded()) {
            throw new IllegalArgumentException("Infinite Domain to plot");
        }
        if (ret.isNaN()) {
            throw new IllegalArgumentException("NaN Domain to plot");
        }

        return ret;
    }

    public int getYPrecisionValue() {
        return yPrecisionSlider.getValue() * 1;
    }

    public int getXPrecisionValue() {
        return xPrecisionSlider.getValue() * 1;
    }

    //    private void onEchantillonXChange() {
//        xSlider.setToolTipText(String.valueOf(xSlider.getValue()));
//        xValuelabel.setText(String.valueOf(xSlider.getValue()));
//        if (xSlider.getValueIsAdjusting()) {
//            return;
//        }
//        xtimes = xSlider.getValue();
//        x = null;
//        updatePlot();
    //    }
    private void onYValueChange() {
        double yy = getFunctionYValueSlider();
        yValueSlider.setToolTipText(String.valueOf(yy));
        yValueSliderText.setText(String.valueOf(yy));
        if (yValueSlider.getValueIsAdjusting()) {
            return;
        }

//            for (int i = 0; i < checkBoxes.length; i++) {
//                if (!checkBoxes[i].isEnabled() && checkBoxes[i].isSelected()) {
//                    checkBoxes[i].setSelected(false);
//                }
//            }
//            int[] indexes = getSelectedIndexes();
//            int x = indexes.length;
//            if (x == 0) {
//                for (int i = 0; x < 10 && i < checkBoxes.length; i++) {
//                    if (checkBoxes[i].isEnabled() && !checkBoxes[i].isSelected()) {
//                        checkBoxes[i].setSelected(true);
//                        x++;
//                    }
//                }
//            }
        updatePlot(false);
    }

    private void onXValueChange() {

        double xx = getFunctionXValueSlider();
        xValueSlider.setToolTipText(String.valueOf(xx));
        xValueSliderText.setText(String.valueOf(xx));
        if (xValueSlider.getValueIsAdjusting()) {
            return;
        }

//            for (int i = 0; i < checkBoxes.length; i++) {
//                if (!checkBoxes[i].isEnabled() && checkBoxes[i].isSelected()) {
//                    checkBoxes[i].setSelected(false);
//                }
//            }
//            int[] indexes = getSelectedIndexes();
//            int x = indexes.length;
//            if (x == 0) {
//                for (int i = 0; x < 10 && i < checkBoxes.length; i++) {
//                    if (checkBoxes[i].isEnabled() && !checkBoxes[i].isSelected()) {
//                        checkBoxes[i].setSelected(true);
//                        x++;
//                    }
//                }
//            }
        updatePlot(false);
    }

    private void setSamples(PlotSamples samples, boolean update) {
        baseSamples = samples;
        if (update) {
            updatePlot(false);
        }
    }

    private void setXPrecision(int value, boolean update) {
        boolean update0 = false;
        String svalue = String.valueOf(value);
        if (!xPrecisionSliderText.getText().equals(svalue)) {
            xPrecisionSlider.setToolTipText(svalue);
            xPrecisionSliderText.setText(svalue);
            update0 = true;
        }
        if (!xPrecisionSlider.getValueIsAdjusting() && xPrecisionSlider.getValue() != value) {
            xPrecisionSlider.setValue(value);
            update0 = true;
        }
        if (update && update0) {
            updatePlot(false);
        }
    }

    private void setYPrecision(int value, boolean update) {
        boolean update0 = false;
        String svalue = String.valueOf(value);
        if (!yPrecisionSliderText.getText().equals(svalue)) {
            yPrecisionSlider.setToolTipText(svalue);
            yPrecisionSliderText.setText(svalue);
            update0 = true;
        }
        if (!yPrecisionSlider.getValueIsAdjusting() && yPrecisionSlider.getValue() != value) {
            yPrecisionSlider.setValue(value);
            update0 = true;
        }
        if (update0 && update) {
            updatePlot(false);
        }
    }

    private void onXPrecisionChange() {
        baseSamples = null;
        int xx = getXPrecisionValue();
        xPrecisionSlider.setToolTipText(String.valueOf(xx));
        xPrecisionSliderText.setText(String.valueOf(xx));
        if (xPrecisionSlider.getValueIsAdjusting()) {
            return;
        }
        updatePlot(false);
    }

    private void onYPrecisionChange() {
        baseSamples = null;
        int xx = getYPrecisionValue();
        yPrecisionSlider.setToolTipText(String.valueOf(xx));
        yPrecisionSliderText.setText(String.valueOf(xx));
        if (yPrecisionSlider.getValueIsAdjusting()) {
            return;
        }
        updatePlot(false);
    }

    public void showContent() {
        JTextArea a = new JTextArea();
        a.setEditable(false);
        int c = functionsTable.getSelectedColumn();
        c = functionsTable.convertColumnIndexToModel(c);
        int r = functionsTable.getSelectedRow();
        if (r < 0 || c < 0) {
            return;
        }
        FunctionsXYTableModel model = ((FunctionsXYTableModel) (functionsTable.getModel()));
        Object o = model.getValueAt(r, c);
        a.setText(o == null ? "" : o.toString());
        JScrollPane jsp = new JScrollPane(a);
        JOptionPane.showMessageDialog(null, jsp);
    }

    public void selectAll(final boolean selectAll) {
        FunctionsXYTableModel model = ((FunctionsXYTableModel) (functionsTable.getModel()));
        model.setAllSelected(selectAll);
    }

    public void selectByCell(final boolean selectAll) {
        int c = functionsTable.getSelectedColumn();
        c = functionsTable.convertColumnIndexToModel(c);
        int r = functionsTable.getSelectedRow();
        FunctionsXYTableModel model = ((FunctionsXYTableModel) (functionsTable.getModel()));
        if (r < 0 || c < 0) {
            model.setAllSelected(false);
            return;
        }
        model.setAllSelectedByCell(c, r, selectAll);
    }

    public void updatePlot(final boolean alwaysUpdate) {
        if (disableUpdatePlot) {
            return;
        }
        if (isUpdating) {
            again = true;
            return;
        }
        isUpdating = true;
        new Thread("UpdatePlotThread") {

            public void run() {
                try {
                    updatePlotAsynch(alwaysUpdate);
                    while (again) {
                        again = false;
                        updatePlotAsynch(alwaysUpdate);
                    }
                } finally {
                    isUpdating = false;
                }
            }
        }.start();
    }

    public void updatePlotAsynch(boolean alwaysUpdate) {
        try {
            updatingProgressBar.setIndeterminate(true);
            updateNow.setEnabled(false);
            domainXY0 = null;
            switch (getShowType()) {
                case ALL:
                case AUTO:
                case CURVE:
                case POLAR:
                case BUBBLE:
                case FIELD:
                case PIE:
                case BAR:
                case RING:
                case AREA: {
                    setXValueEditable(fyRadioButton.isSelected());
                    setYValueEditable(fxRadioButton.isSelected());
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotCurveFxOrFyAsync(getShowType());
                    }
                    break;
                }
                case HEATMAP: {
                    setXValueEditable(false);
                    setYValueEditable(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotSurfaceAsynch();
                    }
                    break;
                }
                case MATRIX: {
                    setXValueEditable(false);
                    setYValueEditable(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotMatrixAsynch();
                    }
                    break;
                }
                case TABLE: {
                    setXValueEditable(false);
                    setYValueEditable(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotTableAsynch();
                    }
                    break;
                }
                case MESH: {
                    setXValueEditable(false);
                    setYValueEditable(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotMeshAsynch();
                    }
                    break;
                }
            }
        } finally {
            updatingProgressBar.setIndeterminate(false);
            updateNow.setEnabled(true);
        }
    }

    public void setXValueEditable(boolean value) {
        xValueSliderText.setVisible(value);
        xValueTitleLabel.setVisible(value);
        xValueSlider.setVisible(value);
    }

    public void setYValueEditable(boolean value) {
        yValueSliderText.setVisible(value);
        yValueTitleLabel.setVisible(value);
        yValueSlider.setVisible(value);
    }

    public double[] resolveX() {
        Domain d = getDomainOrNull();
        if (baseSamples == null) {
            return d == null ? Maths.dtimes(0, 99, getXPrecisionValue())
                    : Maths.dtimes(d.xmin(), d.xmax(), getXPrecisionValue());
        } else {
            if (d == null) {
                if (baseSamples instanceof AbsolutePlotSamples) {
                    return ((AbsolutePlotSamples) baseSamples).getX();
                }
                if (baseSamples instanceof RelativePlotSamples) {
                    throw new IllegalArgumentException("Unable to resolve Domain for relative Samples");
                }
                if (baseSamples instanceof AdaptivePlotSamples) {
                    throw new IllegalArgumentException("Unsupported Adaptive Samples");
                }
                throw new IllegalArgumentException("Unsupported " + baseSamples.getClass().getName());
            }
            double[] doubles = null;
            if (baseSamples instanceof AbsolutePlotSamples) {
                doubles = ((AbsolutePlotSamples) baseSamples).getX();
            } else if (baseSamples instanceof RelativePlotSamples) {
                doubles = ((RelativePlotSamples) baseSamples).toAbsolute(pdom(d)).getX();
            } else {
                throw new IllegalArgumentException("Unsupported Adaptive Samples");
            }
            if (doubles != null) {
                doubles = d.intersect(doubles);
            }
            if (doubles == null) {
                doubles = new double[]{0};
            }
            return doubles;
        }
    }

    public double[] resolveY() {
        Domain d = getDomainOrNull();
        if (baseSamples == null) {
            return d == null ? new double[]{0} : Maths.dtimes(d.ymin(), d.ymax(), getYPrecisionValue());
        } else {
            if (d == null) {
                if (baseSamples instanceof AbsolutePlotSamples) {
                    return ((AbsolutePlotSamples) baseSamples).getY();
                }
                if (baseSamples instanceof RelativePlotSamples) {
                    throw new IllegalArgumentException("Unable to resolve Domain for relative Samples");
                }
                if (baseSamples instanceof AdaptivePlotSamples) {
                    throw new IllegalArgumentException("Unsupported Adaptive Samples");
                }
                throw new IllegalArgumentException("Unsupported " + baseSamples.getClass().getName());
            }
            double[] doubles = null;
            if (baseSamples instanceof AbsolutePlotSamples) {
                doubles = ((AbsolutePlotSamples) baseSamples).getY();
            } else if (baseSamples instanceof RelativePlotSamples) {
                doubles = ((RelativePlotSamples) baseSamples).toAbsolute(pdom(d)).getY();
            } else {
                throw new IllegalArgumentException("Unsupported Adaptive Samples");
            }
            if (doubles != null) {
                doubles = d.intersect(doubles);
            }
            if (doubles == null) {
                doubles = new double[]{0};
            }
            return doubles;
        }
    }

    public void updatePlotCurveFxOrFyAsync(PlotType plotType) {
        if (fxRadioButton.isSelected()) {
            updatePlotCurveFxAsync(plotType);
        } else {
            updatePlotCurveFyAsync(plotType);
        }
    }

    public Exploded[] explode() {
        if (explodedMode && selectedFunctions.length > 1) {
            Exploded[] ee = new Exploded[selectedFunctions.length];
            int rowCount = functionsTableModel.getRowCount();
            for (int i = 0; i < ee.length; i++) {
                ee[i] = new Exploded(
                        "Expression " + (selectedFunctionsIndexes[i] + 1) + "/" + (rowCount),
                        new Expr[]{selectedFunctions[i]}, new int[]{selectedFunctionsIndexes[i]}
                );
            }
            return ee;
        } else {
            return new Exploded[]{
                new Exploded(
                selectedFunctions.length > 1 ? "Stacked" : selectedFunctions.length == 1 ? "Expression 1" : "No Expression",
                 selectedFunctions, selectedFunctionsIndexes)
            };
        }
    }

    public void updatePlotCurveFxAsync(PlotType plotType) {
        CellPosition[] axis = getAxis();
        Exploded[] exploded = explode();
        mainPanel_reconfigure(exploded.length);
        int c = isqrt(axis.length);
        Domain d = getDomainOrNull();

        double[] x = resolveX();
        for (Exploded exp : exploded) {
            JPanel pp = new JPanel(new GridLayout(c, c));
            Expr[] exprs = exp.fct;
            for (CellPosition ax : axis) {
                ArrayList<Paint> colors = new ArrayList<Paint>();
                Complex[][] yz;
                yz = new Complex[exprs.length][];

                double y = getFunctionYValue();
                String[] titles = new String[exprs.length];
                PlotDoubleConverter complexValue = PlotDoubleConverter.REAL;
                LinkedHashSet<String> title = new LinkedHashSet<String>();
                for (int i = 0; i < exprs.length; i++) {
                    DoubleToComplex expr = getAxisExpression(exprs[i], ax);
                    titles[i] = expr.getTitle();
                    if (titles[i] == null) {
                        titles[i] = exprs[i].getTitle();
                    }
                    if (titles[i] == null) {
                        if (exprs.length == 1) {
                            titles[i] = "Expr-" + ax;
                        } else {
                            titles[i] = "Expr-" + (i + 1) + "-" + ax;
                        }
                    }
                    yz[i] = expr.evalComplex(x, new double[]{y})[0];
                    complexValue = getComplexAsDouble();
                    colors.add(DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE[exp.sels[i] % DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE.length]);
                    title.add(titles[i]);
                }

                PlotBuilder pb = Plot.nodisplay().plotType(plotType).converter(complexValue).title(
                        String.join(",", title)
                ).titles(titles);
                if (this.model.getProperties() != null) {
                    for (Map.Entry<String, Object> ee : this.model.getProperties().entrySet()) {
                        pb.param(ee.getKey(), ee.getValue());
                    }
                }
                PlotComponent chartPanel = pb.samples(Samples.absolute(x)).plot(yz);
                pp.add(wrapWithTitle(ax, chartPanel));
            }
            pp.setBorder(BorderFactory.createTitledBorder(exp.name));
            mainPanel_add(pp);
        }

        mainPanel_invalidate();
    }

    public void updatePlotCurveFyAsync(PlotType type) {
        CellPosition[] axis = getAxis();
        Exploded[] exploded = explode();
        mainPanel_reconfigure(exploded.length);
        int c = isqrt(axis.length);

        Domain d = getDomainOrNull();
        double[] y = d == null ? new double[0] : Maths.dtimes(d.ymin(), d.ymax(), yPrecisionSlider.getValue());
        HashSet<String> title = new HashSet<String>();
        for (Exploded exp : exploded) {
            JPanel pp = new JPanel(new GridLayout(c, c));
            Expr[] exprs = exp.fct;
            for (CellPosition ax : axis) {
                Complex[][] yz;
                yz = new Complex[exprs.length][];

                double x = getFunctionXValue();
                String[] titles = new String[exprs.length];
                for (int i = 0; i < exprs.length; i++) {
                    titles[i] = String.valueOf(i + 1);
                    DoubleToComplex f = getAxisExpression(exprs[i], ax);
                    yz[i] = f.evalComplex(x, y, null, null);
                    String t = exprs[i].toDM().getComponentTitle(ax.getRow(), ax.getColumn());
                    if (t == null) {
                        t = "[" + (ax.getRow() + 1) + "," + (ax.getColumn() + 1) + "]";
                    }
                    title.add(t);
                }
                PlotDoubleConverter complexValue = getComplexAsDouble();
                PlotBuilder pb = Plot.nodisplay().converter(complexValue).title(title.toString()).titles(titles).plotType(type);
                if (this.model.getProperties() != null) {
                    for (Map.Entry<String, Object> ee : this.model.getProperties().entrySet()) {
                        pb.param(ee.getKey(), ee.getValue());
                    }
                }
                PlotComponent chartPanel = pb.samples(Samples.absolute(y)).plot(yz);
                pp.add(wrapWithTitle(ax, chartPanel));
            }
            pp.setBorder(BorderFactory.createTitledBorder(exp.name));
            mainPanel_add(pp);
        }
        mainPanel_invalidate();
    }

    private JPanel wrapWithTitle(CellPosition cp, PlotComponent c) {
        return wrapWithTitle(cp, c.toComponent());
    }

    private JPanel wrapWithTitle(CellPosition cp, Component c) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(c);
        p.setBorder(BorderFactory.createTitledBorder(cp.toString()));
        return p;
    }

    public PlotDoubleConverter getComplexAsDouble() {
        for (JRadioButton r : cadRadioButtons) {
            if (r.isSelected()) {
                Object o = r.getClientProperty("Object");
                if (o instanceof PlotDoubleConverter) {
                    return (PlotDoubleConverter) o;
                }
            }
        }
        return PlotDoubleConverter.ABS;
    }

    public void setConverter(ToDoubleFunction<Object> toDoubleConverter) {
        for (JRadioButton r : cadRadioButtons) {
            Object o = r.getClientProperty("Object");
            if (o != null && o.equals(toDoubleConverter)) {
                r.setSelected(true);
                return;
            }
        }
    }

    public synchronized void updatePlotSurfaceAsynch() {
        CellPosition[] axis = getAxis();
        Exploded[] exploded = explode();
        mainPanel_reconfigure(exploded.length);
        int c = isqrt(axis.length);
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        double[] y = resolveY();
        for (Exploded exp : exploded) {
            JPanel pp = new JPanel(new GridLayout(c, c));
            Expr[] exprs = exp.fct;

            for (CellPosition ax : axis) {
                Out<Range> r1 = new Out<>();
                Complex[][] yz = exprs.length == 0 ? new Complex[0][] : getAxisExpression(exprs[0], ax).evalComplex(x, y, d, r1);
                if (exprs.length > 0) {
                    try {
                        ExpressionsDebug.debug_check(yz, r1);
                    } catch (Exception ex) {
                        yz = exprs.length == 0 ? new Complex[0][] : getAxisExpression(exprs[0], ax).evalComplex(x, y, d, r1);
                    }
                }
                for (int i = 1; i < exprs.length; i++) {
                    Out<Range> r2 = new Out<>();
                    Complex[][] yz0 = getAxisExpression(exprs[i], ax).evalComplex(x, y, d, r2);
                    try {
                        ExpressionsDebug.debug_check(yz0, r2);
                    } catch (Exception ex) {
                        yz0 = getAxisExpression(exprs[i], ax).evalComplex(x, y, d, r2);
                    }
                    for (int j = 0; j < yz0.length; j++) {
                        Complex[] complexes = yz0[j];
                        for (int k = 0; k < complexes.length; k++) {
                            yz[j][k] = yz[j][k].plus(complexes[k]);
                        }
                    }
                }
                PlotBuilder pb = Plot.nodisplay().asCurve().converter(getComplexAsDouble()).title(this.model.getTitle()).asHeatMap();
                PlotComponent chartPanel = pb.xsamples(x).ysamples(y).plot(yz);
                pp.add(wrapWithTitle(ax, chartPanel));
            }
            pp.setBorder(BorderFactory.createTitledBorder(exp.name));
            mainPanel_add(pp);
        }
        mainPanel_invalidate();
    }

    public synchronized void updatePlotMatrixAsynch() {
        CellPosition[] axis = getAxis();
        Exploded[] exploded = explode();
        mainPanel_reconfigure(exploded.length);
        int c = isqrt(axis.length);
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        double[] y = resolveY();
        for (Exploded exp : exploded) {
            JPanel pp = new JPanel(new GridLayout(c, c));
            Expr[] exprs = exp.fct;
            for (CellPosition ax : axis) {
                Out<Range> r1 = new Out<>();
                Complex[][] yz = exprs.length == 0 ? new Complex[0][] : getAxisExpression(exprs[0], ax).evalComplex(x, y, d, r1);
                ExpressionsDebug.debug_check(yz, r1);
                for (int i = 1; i < exprs.length; i++) {
                    Out<Range> r2 = new Out<>();
                    Complex[][] yz0 = getAxisExpression(exprs[i], ax).evalComplex(x, y, d, r2);
                    ExpressionsDebug.debug_check(yz0, r2);
                    for (int j = 0; j < yz0.length; j++) {
                        Complex[] complexes = yz0[j];
                        for (int k = 0; k < complexes.length; k++) {
                            yz[j][k] = yz[j][k].plus(complexes[k]);
                        }
                    }
                }

                ValuesPlotModel model2 = new ValuesPlotModel();
                model2.setTitle(this.model.getTitle());
                model2.setPlotType(new LibraryPlotType(PlotType.MATRIX, null));
                model2.setXVector(x);
                model2.setYVector(y);
                model2.setZ(yz);
                model2.setConverter(getComplexAsDouble());
                PlotBuilder pb = Plot.nodisplay().asCurve().converter(getComplexAsDouble()).title(this.model.getTitle()).asMatrix();
                PlotComponent chartPanel = pb.xsamples(x).ysamples(y).plot(yz);
                pp.add(wrapWithTitle(ax, chartPanel));
            }
            pp.setBorder(BorderFactory.createTitledBorder(exp.name));
            mainPanel_add(pp);
        }
        mainPanel_invalidate();
    }

    public synchronized void updatePlotTableAsynch() {
        CellPosition[] axis = getAxis();
        Exploded[] exploded = explode();
        mainPanel_reconfigure(exploded.length);
        int c = isqrt(axis.length);
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        double[] y = resolveY();
        for (Exploded exp : exploded) {
            JPanel pp = new JPanel(new GridLayout(c, c));
            Expr[] exprs = exp.fct;
            for (CellPosition ax : axis) {
                Out<Range> r1 = new Out<>();
                Complex[][] yz = exprs.length == 0 ? new Complex[0][] : getAxisExpression(exprs[0], ax).evalComplex(x, y, d, r1);
                ExpressionsDebug.debug_check(yz, r1);
                for (int i = 1; i < exprs.length; i++) {
                    Out<Range> r2 = new Out<>();
                    Complex[][] yz0 = getAxisExpression(exprs[i], ax).evalComplex(x, y, d, r2);
                    ExpressionsDebug.debug_check(yz0, r2);
                    for (int j = 0; j < yz0.length; j++) {
                        Complex[] complexes = yz0[j];
                        for (int k = 0; k < complexes.length; k++) {
                            yz[j][k] = yz[j][k].plus(complexes[k]);
                        }
                    }
                }
                PlotBuilder pb = Plot.nodisplay().asCurve().converter(getComplexAsDouble()).title(this.model.getTitle()).asTable();
                PlotComponent chartPanel = pb.xsamples(x).ysamples(y).plot(yz);
                pp.add(wrapWithTitle(ax, chartPanel));
            }
            pp.setBorder(BorderFactory.createTitledBorder(exp.name));
            mainPanel_add(pp);
        }
        mainPanel_invalidate();
    }

    private void mainPanel_add(final Component component) {
        try {
            SwingUtilities3.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mainPanel.add(component);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int isqrt(int count) {
        int c = count;
        if (c < 0) {
            c = 1;
        }
        c = (int) Math.sqrt(c);
        if (c == 0) {
            c = 1;
        }
        return c;
    }

    private void mainPanel_reconfigure(int count) {
        try {
            SwingUtilities3.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (mainPanel != null) {
                        int c = isqrt(count);
                        mainPanel.setLayout(new GridLayout(c, c));
                        mainPanel.removeAll();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mainPanel_invalidate() {
        try {
            SwingUtilities3.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (mainPanel != null) {
                        mainPanel.invalidate();
                        mainPanel.validate();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updatePlotMeshAsynch() {
        CellPosition[] axis = getAxis();
        Exploded[] exploded = explode();
        mainPanel_reconfigure(exploded.length);
        int c = isqrt(axis.length);
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        double[] y = resolveY();
        for (Exploded exp : exploded) {
            JPanel pp = new JPanel(new GridLayout(c, c));
            Expr[] exprs = exp.fct;

            for (CellPosition ax : axis) {
                if (exprs.length > 0) {
                    Complex[][] yz = getAxisExpression(exprs[0], ax).evalComplex(x, y);
                    for (int i = 1; i < exprs.length; i++) {
                        Complex[][] yz0 = getAxisExpression(exprs[i], ax).evalComplex(x, y);
                        for (int j = 0; j < yz0.length; j++) {
                            Complex[] complexes = yz0[j];
                            for (int k = 0; k < complexes.length; k++) {
                                yz[j][k] = yz[j][k].plus(complexes[k]);
                            }
                        }
                    }
                    final ValuesPlotModel model = new ValuesPlotModel();
                    model.setX(new double[][]{x});
                    model.setY(new double[][]{y});
                    model.setZ(yz);
                    model.setConverter(getComplexAsDouble());
                    model.setTitle(this.model.getTitle());
                    PlotComponentPanel chartPanel = PlotBackendLibraries.createPlotComponentPanel(new DefaultPlotComponentContext(new LibraryPlotType(PlotType.MESH, null), model));
                    pp.add(wrapWithTitle(ax, chartPanel.toComponent()));
                }
            }
            pp.setBorder(BorderFactory.createTitledBorder(exp.name));
            mainPanel_add(pp);
        }
        mainPanel_invalidate();
    }

    public ComponentDimension getComponentDimension() {

        ComponentDimension m = ComponentDimension.SCALAR;
        for (Expr selectedFunction : selectedFunctions) {
            ComponentDimension d = selectedFunction.getComponentDimension();
            m = ComponentDimension.of(Math.max(d.rows, m.rows), Math.max(d.columns, m.columns));
        }

        return m;
    }

    public CellPosition[] getAxisAvailable() {
        ComponentDimension m = getComponentDimension();
        CellPosition[] available = new CellPosition[m.rows * m.columns];
        int t = 0;
        for (int r = 0; r < m.rows; r++) {
            for (int c = 0; c < m.columns; c++) {
                available[t++] = new CellPosition(r, c);
            }
        }
        return available;
    }

    public CellPosition[] getAxis() {
        CellPosition[] selectedAxis1 = model.getSelectedAxis();
        CellPosition[] axisAvailable = getAxisAvailable();
        if (selectedAxis1 == null) {
            selectedAxis1 = axisAvailable;
        }
        java.util.List<CellPosition> axisOk = new ArrayList<CellPosition>();
        for (int i = 0; i < axisAvailable.length; i++) {
            switch (i) {
                case 0: {
                    if (fxAxisRadioButton.isSelected()) {
                        axisOk.add(axisAvailable[i]);
                    }
                    break;
                }
                case 1: {
                    if (fyAxisRadioButton.isSelected()) {
                        axisOk.add(axisAvailable[i]);
                    }
                    break;
                }
                default: {
                    axisOk.add(axisAvailable[i]);
                }
            }
        }
        if (axisOk.size() == 0) {
            axisOk.addAll(Arrays.asList(axisAvailable));
        }
        return axisOk.toArray(new CellPosition[0]);
//        if (fxAxisRadioButton.isSelected() && fyAxisRadioButton.isSelected()) {
//            return new int[]{VDCxy.X, VDCxy.Y};
//        } else {
//            return fyAxisRadioButton.isSelected() ? new int[]{VDCxy.Y} : new int[]{VDCxy.X};
//        }
    }

    public String getPlotTitle() {
        String baseTitle = super.getPlotTitle();
        if (baseTitle == null) {
            return this.model.getTitle();
        }
        return baseTitle;
    }

    public DoubleToComplex getAxisExpression(Expr e, CellPosition rc) {
        return getAxisExpression(e, rc.getRow(), rc.getColumn());
    }

    public DoubleToComplex getAxisExpression(Expr e, int r, int c) {
//        if(!e.isDM() && r==0 && c==0){
//            return e.toDC();
//        }
        DoubleToMatrix m = e.toDM();
        ComponentDimension d = m.getComponentDimension();
        if (d.rows > r && d.columns > c) {
            return m.getComponent(r, c).toDC();
        }
        return Maths.CZEROXY;
    }

    @Override
    public PlotModel getModel() {
        return model;
    }

    @Override
    public void setModel(PlotModel model) {
        disableUpdatePlot = true;
        try {
            ExpressionsPlotModel m = (ExpressionsPlotModel) model;
            this.model = m;

//        if (m.getProperties() != null) {
//            for (Map.Entry<String, Object> e : getProperties().entrySet()) {
//                if (e.getValue() != null) {
//                    this.properties.put(e.getKey(), e.getValue());
//                }
//            }
//        }
            if (m.getXprec() <= 0) {
                m.setXprec(100);
            }
            if (m.getYprec() <= 0) {
                m.setYprec(100);
            }
            setConverter(m.getConverter());

            boolean withX = false;
            boolean withY = false;
            Expr[] functions = m.getExpressions();
            if (functions == null) {
                m.setExpressions(functions = ArrayUtils.EMPTY_EXPR_ARRAY);
            }
            for (Expr ff : functions) {
                if (!ff.isInvariant(Axis.X)) {
                    withX = true;
                }
                if (!ff.isInvariant(Axis.Y)) {
                    withY = true;
                }
                if (withX && withY) {
                    break;
                }
            }
            fxAxisRadioButton.setSelected(withX);
            fyAxisRadioButton.setSelected(withY);
            fxRadioButton.setSelected(!m.isConstX());
            fyRadioButton.setSelected(m.isConstX());
            this.xPrecisionSlider.setModel(new DefaultBoundedRangeModel(m.getXprec(), 0, 1, m.getXprec() * 1000));
            this.yPrecisionSlider.setModel(new DefaultBoundedRangeModel(m.getYprec(), 0, 1, m.getYprec() * 1000));
            setXPrecision(m.getXprec(), false);
            setYPrecision(m.getYprec(), false);
            PlotSamples samples = m.getSamples();
            setPlotType(m.getPlotType());
            functionsTableModel.setExpressions(functions);
            if (samples != null) {
                setSamples(samples, true);
            }
        } finally {
            disableUpdatePlot = false;
        }
        updatePlot(true);

    }

    @Override
    public boolean accept(PlotModel model) {
        return model instanceof ExpressionsPlotModel;
    }

    private static class Exploded {

        Expr[] fct;
        int[] sels;
        String name;

        public Exploded(String name, Expr[] fct, int[] sels) {
            this.name = name;
            this.fct = fct;
            this.sels = sels;
        }
    }

    //    public enum ShowType {
//
//        CURVE_FX,
//        CURVE_FY,
//        CURVE, BAR, AREA, FIELD, PIE, RING, BUBBLE, MESH, HEATMAP, MATRIX, POLAR, AUTO, TABLE, ALL
//    }
    private class UpdatePlotItemListener implements ItemListener, Serializable {

        public void itemStateChanged(ItemEvent e) {
            updatePlot(false);
        }
    }
}
