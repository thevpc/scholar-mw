package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.JTableHelper;
import net.vpc.scholar.hadrumaths.util.SwingUtils;
import net.vpc.scholar.hadrumaths.util.swingext.*;
import org.jfree.chart.plot.DefaultDrawingSupplier;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;

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
    private int maxPrec = 1000;
    private ExpressionsPlotModel model;
    //    private Expr[] functions;
//    private Map<String, Object> properties = new HashMap<String, Object>();
    private JCheckBox autoUpdate = prepareStatusCheck(true, "auto", "auto update");
    //    private JCheckBox[] functionCheckBoxes;
    private JTextField yValueSliderText = new JTextField(10);
    private JSlider yValueSlider;
    private JTextField yPrecisionSliderText = new JTextField(10);
    private JSlider yPrecisionSlider;
    private JTextField xValueSliderText = new JTextField(10);
    private JSlider xValueSlider;
    private JTextField xPrecisionSliderText = new JTextField(10);
    private Samples baseSamples;
    private JSlider xPrecisionSlider;
    private JPanel mainPanel;
    //    private String title;
    private JTable functionsTable;
    //    private Domain domainXY;
    private Domain domainXY0;
    private JCheckBox fxAxisRadioButton = new JCheckBox("Fx(x,y)", true);
    private JCheckBox fyAxisRadioButton = new JCheckBox("Fy(x,y)", true);
    private JRadioButton cadRealRadioButton = new JRadioButton("Real");
    private JRadioButton cadImagRadioButton = new JRadioButton("Imag");
    private JRadioButton cadDbRadioButton = new JRadioButton("DB");
    private JRadioButton cadDb2RadioButton = new JRadioButton("DB2");
    private JRadioButton cadArgRadioButton = new JRadioButton("Arg");
    private JRadioButton cadComplexRadioButton = new JRadioButton("Complex");
    private JRadioButton cadAbsRadioButton = new JRadioButton("Abs");
    private JRadioButton fx1DRadioButton = new JRadioButton("Curve F(x,y=cst)");
    private JRadioButton fy1DRadioButton = new JRadioButton("Curve F(x=cst,y)");
    private JRadioButton fyPolarRadioButton = new JRadioButton("Polar");
    private JRadioButton fxy2DRadioButton = new JRadioButton("Surface 2D : F(x,y)");
    private JRadioButton fxyMatrixRadioButton = new JRadioButton("Colourful Matrix : F(x,y)");
    private JRadioButton fxy3DRadioButton = new JRadioButton("Mesh 3D : F(x,y)");
    private JRadioButton tableRadioButton = new JRadioButton("Table");
    private Expr[] selectedFunctions = new DoubleToVector[0];
    private int[] selectedFunctionsIndexes = new int[0];
    private PlotWindowManager windowManager;
    //    private JComboBox showType = new JComboBox(ShowType.values());
    private JProgressBar updatingProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
    //    private JLabel yPrecisionLabel = new JLabel("");
//    private JLabel xPrecisionLabel = new JLabel("");
    private JLabel yValueTitleLabel = new JLabel("");
    private JLabel xValueTitleLabel = new JLabel("");
    private JButton updateNow = prepareButton("u", "update plot");
    private JButton selectAllByCellFunctions = prepareButton("+?", "Select By Cell");
    private JButton selectNoneByCellFunctions = prepareButton("-?", "Deselect By Cell");
    private JButton selectAllFunctions = prepareButton("++", "Select All");
    private JButton selectNoneFunctions = prepareButton("--", "Select None");
    //    private ButtonGroup fctGroup = new ButtonGroup();
    private JButton showContent = prepareButton("?", "Show Content");
    private ItemListener updatePlotItemListener = new UpdatePlotItemListener();
    private String layoutConstraints = "";
    PropertyChangeListener modelUpdatesListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updatePlotAsynch(true);
        }
    };
//    private ActionListener updatePlotActionListener = new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//            updatePlot(false);
//        }
    //    };

//    public ExpressionsPlotPanel(String title, int xprec, int yprec, DoubleToComplex[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//        this(title, null, xprec, yprec, ComplexAsDouble.ABS, functions, showType, properties, windowManager);
//    }
//
//    public ExpressionsPlotPanel(String title, int xprec, int yprec, DoubleToVector[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//        this(title, null, xprec, yprec, ComplexAsDouble.ABS, functions, showType, properties, windowManager);
//    }
//
//    public ExpressionsPlotPanel(String title, int xprec, int yprec, DoubleToDouble[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//        this(title, null, xprec, yprec, ComplexAsDouble.REAL, convert(functions), showType, properties, windowManager);
//    }

//    public ExpressionsPlotPanel(String title, int xprec,int yprec,DFunctionVector2D[] functions, PlotWindowManager windowManager) {
//        this(title, null, xprec,yprec,ComplexAsDouble.REAL, convert(functions), windowManager);
//    }

//    public ExpressionsPlotPanel(String title, Domain domainXY, int xprec, int yprec, ComplexAsDouble complexAsDouble, DoubleToComplex[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//        this(title, domainXY, xprec, yprec, complexAsDouble, convert(functions), showType, properties, windowManager);
//    }

//    public ExpressionsPlotPanel(String title, Domain domainXY, int xprec, int yprec, ComplexAsDouble complexAsDouble, Expr[] functions, ShowType showType, Map<String, Object> properties, PlotWindowManager windowManager) {
//
//    }

    public ExpressionsPlotPanel(ExpressionsPlotModel model, PlotWindowManager windowManager) {
        super(new BorderLayout());
        if (model == null) {
            model = new ExpressionsPlotModel();
        }
        this.model = model;
        this.domainXY0 = null;

        this.windowManager = windowManager;

        fxAxisRadioButton.setSelected(false);
        fyAxisRadioButton.setSelected(false);

        setComplexAsDouble(ComplexAsDouble.ABS);
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
        config.addTab("Layout", createGeneralPane(ShowType.CURVE_FX));
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
        updatePlot(true);
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

    private static DoubleToVector[] convert(DoubleToComplex[] f) {
        DoubleToVector[] g = new DoubleToVector[f.length];
        for (int i = 0; i < g.length; i++) {
            g[i] = Maths.vector(f[i], FunctionFactory.CZEROXY);
//            g[i].setTitle(f[i].getName());
        }
        return g;
    }

    private static DoubleToVector[] convert(DoubleToDouble[] f) {
        DoubleToVector[] g = new DoubleToVector[f.length];
        for (int i = 0; i < g.length; i++) {
            g[i] = Maths.vector(f[i], FunctionFactory.CZEROXY);
//            g[i].setTitle(f[i].getName());
        }
        return g;
    }

    @Override
    public boolean accept(PlotModel model) {
        return model instanceof ExpressionsPlotModel;
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

        JTableHelper jTableHelper = JTableHelper.prepareIndexedTable(functionsTableModel);
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

    public ShowType getShowType() {
        if (fx1DRadioButton.isSelected()) {
            return ShowType.CURVE_FX;
        }
        if (fy1DRadioButton.isSelected()) {
            return ShowType.CURVE_FY;
        }
        if (fyPolarRadioButton.isSelected()) {
            return ShowType.POLAR;
        }
        if (fxy2DRadioButton.isSelected()) {
            return ShowType.SURFACE_2D;
        }
        if (fxyMatrixRadioButton.isSelected()) {
            return ShowType.MATRIX;
        }
        if (fxy3DRadioButton.isSelected()) {
            return ShowType.MESH;
        }
        if (tableRadioButton.isSelected()) {
            return ShowType.TABLE;
        }
        return ShowType.SURFACE_2D;
    }

    public void setShowType(ShowType showType) {
        switch (showType) {
            case CURVE_FX: {
                fx1DRadioButton.setSelected(true);
                break;
            }
            case CURVE_FY: {
                fy1DRadioButton.setSelected(true);
                break;
            }
            case SURFACE_2D: {
                fxy2DRadioButton.setSelected(true);
                break;
            }
            case POLAR: {
                fyPolarRadioButton.setSelected(true);
                break;
            }
            case MATRIX: {
                fxyMatrixRadioButton.setSelected(true);
                break;
            }
            case MESH: {
                fxy3DRadioButton.setSelected(true);
                break;
            }
            case TABLE: {
                tableRadioButton.setSelected(true);
                break;
            }
        }
    }

    private JPanel createPrecisionPane() {
        JPanel south = new JPanel(new GridBagLayout2(
                "[xValueTitleLabel<  ][xValueSlider          +=][xValueLabel         ->]\n"
                        + "[X               <  ][xPrecisionSlider      +=][xPrecisionLabel     ->]\n"
                        + "[yValueTitleLabel<  ][yValueSlider          +=][yValueLabel         ->]\n"
                        + "[Y               <  ][yPrecisionSlider      +=][yPrecisionLabel     ->]\n"
                        + ""
        ));
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

    private JPanel createGeneralPane(ShowType showType) {
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
        ButtonGroup courbeGroup = new ButtonGroup();
        fx1DRadioButton.setSelected(true);
        courbeGroup.add(fx1DRadioButton);
        fy1DRadioButton.setSelected(false);
        courbeGroup.add(fy1DRadioButton);
        fyPolarRadioButton.setSelected(false);
        courbeGroup.add(fyPolarRadioButton);
        fxy2DRadioButton.setSelected(false);
        courbeGroup.add(fxy2DRadioButton);
        fxyMatrixRadioButton.setSelected(false);
        courbeGroup.add(fxyMatrixRadioButton);
        fxy3DRadioButton.setSelected(false);
        courbeGroup.add(fxy3DRadioButton);
        tableRadioButton.setSelected(false);
        courbeGroup.add(tableRadioButton);

        JPanel f = new JPanel(new GridBagLayout2(
                "[A1<+]\n"
                        + "[A2<+]\n"
                        + "[A2b<+]\n"
                        + "[A3<+]\n"
                        + "[A4<+]\n"
                        + "[A5<+]\n"
                        + "[A6<+]\n"
        ));
        f.add(fx1DRadioButton, "A1");
        f.add(fy1DRadioButton, "A2");
        f.add(fyPolarRadioButton, "A2b");
        f.add(fxy2DRadioButton, "A3");
        f.add(fxyMatrixRadioButton, "A4");
        f.add(fxy3DRadioButton, "A5");
        f.add(tableRadioButton, "A6");
        f.setBorder(BorderFactory.createTitledBorder("Plot Type"));

//        fxAxisRadioButton.setSelected(true);
//        fyAxisRadioButton.setSelected(true);
        JPanel xy = new JPanel(new GridBagLayout2(
                "[A1+<]\n"
                        + "[A2+<]\n"
        ));
        xy.add(fxAxisRadioButton, "A1");
        xy.add(fyAxisRadioButton, "A2");
        xy.setBorder(BorderFactory.createTitledBorder("Components"));

        JPanel cad = new JPanel(new GridBagLayout2(
                "[A1+<]\n"
                        + "[A2+<]\n"
                        + "[A3+<]\n"
                        + "[A4+<]\n"
                        + "[A5+<]\n"
                        + "[A6+<]\n"
                        + "[A7+<]\n"
        ));
        cad.add(cadAbsRadioButton, "A1");
        cad.add(cadRealRadioButton, "A2");
        cad.add(cadImagRadioButton, "A3");
        cad.add(cadDbRadioButton, "A4");
        cad.add(cadDb2RadioButton, "A5");
        cad.add(cadArgRadioButton, "A6");
        cad.add(cadComplexRadioButton, "A7");
        ButtonGroup cadGroup = new ButtonGroup();
        cadGroup.add(cadAbsRadioButton);
        cadGroup.add(cadDbRadioButton);
        cadGroup.add(cadDb2RadioButton);
        cadGroup.add(cadArgRadioButton);
        cadGroup.add(cadImagRadioButton);
        cadGroup.add(cadRealRadioButton);
        cadGroup.add(cadComplexRadioButton);

        cad.setBorder(BorderFactory.createTitledBorder("Conversion"));

        JPanel all = new JPanel(new GridBagLayout2(
                "[A1+<=$][A2+<=$][A3+<=$][A4+<=$]\n"));
        all.add(f, "A1");
        all.add(xy, "A2");
        all.add(cad, "A3");
        setShowType(showType);
        fx1DRadioButton.addItemListener(updatePlotItemListener);
        fy1DRadioButton.addItemListener(updatePlotItemListener);
        fyPolarRadioButton.addItemListener(updatePlotItemListener);
        fxy2DRadioButton.addItemListener(updatePlotItemListener);
        fxy3DRadioButton.addItemListener(updatePlotItemListener);
        tableRadioButton.addItemListener(updatePlotItemListener);
        fxAxisRadioButton.addItemListener(updatePlotItemListener);
        fyAxisRadioButton.addItemListener(updatePlotItemListener);

        cadAbsRadioButton.addItemListener(updatePlotItemListener);
        cadDbRadioButton.addItemListener(updatePlotItemListener);
        cadDb2RadioButton.addItemListener(updatePlotItemListener);
        cadImagRadioButton.addItemListener(updatePlotItemListener);
        cadRealRadioButton.addItemListener(updatePlotItemListener);

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
                new GridBagLayout2("[<updatingProgressBar===-][auto<+][refresh]").setInsets(".*", new Insets(0, 0, 0, 0)));
        p.add(updatingProgressBar, "updatingProgressBar");
        p.add(autoUpdate, "auto");
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

    public Domain getDomain() {
        Domain ret = null;
        if (this.model.getDomain() != null) {
            ret = this.model.getDomain();
        } else if (domainXY0 == null) {
            Domain d = Domain.EMPTYX;
            Domain m = Domain.EMPTYX;
            for (Expr function : this.model.getExpressions()) {
//                for (CellPosition i : getAxis()) {
                DoubleToMatrix ddf = function.toDM();
                Domain dd = ddf.getDomain();
                if (dd.isInfinite() && (ddf.isZero() || dd.isNaN())) {
                    //do nothing
                } else {
                    m = m.expand(dd);
                }
//                }
            }
            for (Expr function : this.model.getExpressions()) {
                for (CellPosition i : getAxis()) {
                    DoubleToComplex ddf = getAxisExpression(function, i.getRow(), i.getColumn());
                    Domain dd = ddf.getDomain();
                    if (dd.isInfinite() && (ddf.isZero() || dd.isNaN())) {
                        //do nothing
                    } else {
                        d = d.expand(dd);
                    }
                }
            }
            d = d.intersect(m);
            domainXY0 = d;
            ret = domainXY0.toDomainXYZ().ensureBounded(100, 100, 100);
        } else {
            ret = domainXY0.toDomainXYZ().ensureBounded(100, 100, 100);
        }
        if (ret == null) {
            //never
            throw new IllegalArgumentException("Null Domain to plot");
        }
        if (ret.isEmpty()) {
            throw new IllegalArgumentException("Empty Domain to plot");
        }
        if (ret.isInfinite()) {
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

    private void setSamples(Samples samples, boolean update) {
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
        baseSamples=null;
        int xx = getXPrecisionValue();
        xPrecisionSlider.setToolTipText(String.valueOf(xx));
        xPrecisionSliderText.setText(String.valueOf(xx));
        if (xPrecisionSlider.getValueIsAdjusting()) {
            return;
        }
        updatePlot(false);
    }

    private void onYPrecisionChange() {
        baseSamples=null;
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
                case CURVE_FX: {
                    xValueSliderText.setVisible(false);
                    xValueTitleLabel.setVisible(false);
                    xValueSlider.setVisible(false);
                    yValueSliderText.setVisible(true);
                    yValueTitleLabel.setVisible(true);
                    yValueSlider.setVisible(true);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotCurveFxAsync();
                    }
                    break;
                }
                case CURVE_FY: {
                    xValueSliderText.setVisible(true);
                    xValueTitleLabel.setVisible(true);
                    xValueSlider.setVisible(true);
                    yValueSliderText.setVisible(false);
                    yValueTitleLabel.setVisible(false);
                    yValueSlider.setVisible(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotCurveFyAsync();
                    }
                    break;
                }
                case POLAR: {
                    xValueSliderText.setVisible(true);
                    xValueTitleLabel.setVisible(true);
                    xValueSlider.setVisible(true);
                    yValueSliderText.setVisible(false);
                    yValueTitleLabel.setVisible(false);
                    yValueSlider.setVisible(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotPolarAsync();
                    }
                    break;
                }
                case SURFACE_2D: {
                    xValueSliderText.setVisible(false);
                    xValueTitleLabel.setVisible(false);
                    xValueSlider.setVisible(false);
                    yValueSliderText.setVisible(false);
                    yValueTitleLabel.setVisible(false);
                    yValueSlider.setVisible(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotSurfaceAsynch();
                    }
                    break;
                }
                case MATRIX: {
                    xValueSliderText.setVisible(false);
                    xValueTitleLabel.setVisible(false);
                    xValueSlider.setVisible(false);
                    yValueSliderText.setVisible(false);
                    yValueTitleLabel.setVisible(false);
                    yValueSlider.setVisible(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotMatrixAsynch();
                    }
                    break;
                }
                case TABLE: {
                    xValueSliderText.setVisible(false);
                    xValueTitleLabel.setVisible(false);
                    xValueSlider.setVisible(false);
                    yValueSliderText.setVisible(false);
                    yValueTitleLabel.setVisible(false);
                    yValueSlider.setVisible(false);
                    if (alwaysUpdate || autoUpdate.isSelected()) {
                        updatePlotTableAsynch();
                    }
                    break;
                }
                case MESH: {
                    xValueSliderText.setVisible(false);
                    xValueTitleLabel.setVisible(false);
                    xValueSlider.setVisible(false);
                    yValueSliderText.setVisible(false);
                    yValueTitleLabel.setVisible(false);
                    yValueSlider.setVisible(false);
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

    public double[] resolveX() {
        Domain d = getDomainOrNull();
        if(baseSamples==null) {
            return d == null ? new double[0] : Maths.dtimes(d.xmin(), d.xmax(), getXPrecisionValue());
        }else{
            if(d==null){
                if(baseSamples instanceof AbsoluteSamples){
                    return ((AbsoluteSamples) baseSamples).getX();
                }
                if(baseSamples instanceof RelativeSamples){
                    throw new IllegalArgumentException("Unable to resolve Domain for relative Samples");
                }
                if(baseSamples instanceof AdaptiveSamples){
                    throw new IllegalArgumentException("Unsupported Adaptive Samples");
                }
                throw new IllegalArgumentException("Unsupported "+baseSamples.getClass().getName());
            }
            double[] doubles=null;
            if(baseSamples instanceof AbsoluteSamples){
                doubles=((AbsoluteSamples) baseSamples).getX();
            }else if(baseSamples instanceof RelativeSamples){
                doubles=((RelativeSamples) baseSamples).toAbsolute(d).getX();
            }else{
                throw new IllegalArgumentException("Unsupported Adaptive Samples");
            }
            if(doubles!=null) {
                doubles = d.intersect(doubles);
            }
            if(doubles==null){
                doubles=new double[]{0};
            }
            return doubles;
        }
    }
    public double[] resolveY() {
        Domain d = getDomainOrNull();
        if(baseSamples==null) {
            return d == null ? new double[0] : Maths.dtimes(d.ymin(), d.ymax(), getYPrecisionValue());
        }else{
            if(d==null){
                if(baseSamples instanceof AbsoluteSamples){
                    return ((AbsoluteSamples) baseSamples).getY();
                }
                if(baseSamples instanceof RelativeSamples){
                    throw new IllegalArgumentException("Unable to resolve Domain for relative Samples");
                }
                if(baseSamples instanceof AdaptiveSamples){
                    throw new IllegalArgumentException("Unsupported Adaptive Samples");
                }
                throw new IllegalArgumentException("Unsupported "+baseSamples.getClass().getName());
            }
            double[] doubles=null;
            if(baseSamples instanceof AbsoluteSamples){
                doubles=((AbsoluteSamples) baseSamples).getY();
            }else if(baseSamples instanceof RelativeSamples){
                doubles=((RelativeSamples) baseSamples).toAbsolute(d).getY();
            }else{
                throw new IllegalArgumentException("Unsupported Adaptive Samples");
            }
            if(doubles!=null) {
                doubles = d.intersect(doubles);
            }
            if(doubles==null){
                doubles=new double[]{0};
            }
            return doubles;
        }
    }

    public void updatePlotCurveFxAsync() {
        mainPanel_removeAll();
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        for (CellPosition ax : getAxis()) {
            ArrayList<Paint> colors = new ArrayList<Paint>();
            Complex[][] yz;
            yz = new Complex[selectedFunctions.length][];

            double y = getFunctionYValue();
            String[] titles = new String[selectedFunctions.length];
            ComplexAsDouble complexValue = ComplexAsDouble.REAL;
            HashSet<String> title = new HashSet<String>();
            for (int i = 0; i < selectedFunctions.length; i++) {
                titles[i] = getAxisExpression(selectedFunctions[i], ax).getTitle();
                if (titles[i] == null) {
                    titles[i] = (String) selectedFunctions[i].getTitle();
                }
                if (titles[i] == null) {
                    titles[i] = String.valueOf(i + 1);
                }
                DoubleToComplex f = getAxisExpression(selectedFunctions[i], ax);
                yz[i] = f.computeComplex(x, new double[]{y})[0];
//                for (int j = 0; j < yz[i].length; j++) {
//                    if (yz[i][j].getImag() != 0) {
//                        complexValue = ComplexAsDouble.ABS;
//                    }
//                }
                complexValue = getComplexAsDouble();
                colors.add(DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE[selectedFunctionsIndexes[i] % DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE.length]);
                String t = selectedFunctions[i].toDM().getComponentTitle(ax.getRow(), ax.getColumn());
                if (t == null) {
                    t = "[" + (ax.getRow() + 1) + "," + (ax.getColumn() + 1) + "]";
                }
                title.add(t);
            }


            PlotBuilder pb = Plot.nodisplay().asCurve().converter(complexValue).title(title.toString()).titles(titles);
            if (this.model.getProperties() != null) {
                for (Map.Entry<String, Object> ee : this.model.getProperties().entrySet()) {
                    pb.param(ee.getKey(), ee.getValue());
                }
            }
            PlotComponent chartPanel = pb.samples(Samples.absolute(x)).plot(yz);
            mainPanel_add(chartPanel.toComponent());
        }
        mainPanel_invalidate();
    }

    public void updatePlotPolarAsync() {
        mainPanel_removeAll();
        double[] x = resolveX();
        for (CellPosition ax : getAxis()) {
            ArrayList<Paint> colors = new ArrayList<Paint>();
            Complex[][] yz;
            yz = new Complex[selectedFunctions.length][];

            double y = getFunctionYValue();
            String[] titles = new String[selectedFunctions.length];
            ComplexAsDouble complexValue = ComplexAsDouble.REAL;
            HashSet<String> title = new HashSet<String>();
            for (int i = 0; i < selectedFunctions.length; i++) {
                titles[i] = getAxisExpression(selectedFunctions[i], ax).getTitle();
                if (titles[i] == null) {
                    titles[i] = (String) selectedFunctions[i].getTitle();
                }
                if (titles[i] == null) {
                    titles[i] = String.valueOf(i + 1);
                }
                DoubleToComplex f = getAxisExpression(selectedFunctions[i], ax);
                yz[i] = f.computeComplex(x, new double[]{y})[0];
                complexValue = getComplexAsDouble();
                colors.add(DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE[selectedFunctionsIndexes[i] % DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE.length]);
                String t = selectedFunctions[i].toDM().getComponentTitle(ax.getRow(), ax.getColumn());
                if (t == null) {
                    t = "[" + (ax.getRow() + 1) + "," + (ax.getColumn() + 1) + "]";
                }
                title.add(t);
            }


            PlotBuilder pb = Plot.nodisplay().asPolar().converter(complexValue).title(title.toString()).titles(titles);
            if (this.model.getProperties() != null) {
                for (Map.Entry<String, Object> ee : this.model.getProperties().entrySet()) {
                    pb.param(ee.getKey(), ee.getValue());
                }
            }
            PlotComponent chartPanel = pb.samples(Samples.absolute(x)).plot(yz);
            mainPanel_add(chartPanel.toComponent());
        }
        mainPanel_invalidate();
    }

    public void updatePlotCurveFyAsync() {
        mainPanel_removeAll();
        Domain d = getDomainOrNull();
        double[] y = d == null ? new double[0] : Maths.dtimes(d.ymin(), d.ymax(), yPrecisionSlider.getValue());
        HashSet<String> title = new HashSet<String>();
        for (CellPosition ax : getAxis()) {
            Complex[][] yz;
            yz = new Complex[selectedFunctions.length][];

            double x = getFunctionXValue();
            String[] titles = new String[selectedFunctions.length];
            for (int i = 0; i < selectedFunctions.length; i++) {
                titles[i] = String.valueOf(i + 1);
                DoubleToComplex f = getAxisExpression(selectedFunctions[i], ax);
                yz[i] = f.computeComplex(x, y, null, null);
                String t = selectedFunctions[i].toDM().getComponentTitle(ax.getRow(), ax.getColumn());
                if (t == null) {
                    t = "[" + (ax.getRow() + 1) + "," + (ax.getColumn() + 1) + "]";
                }
                title.add(t);
            }
            ComplexAsDouble complexValue = getComplexAsDouble();
            PlotBuilder pb = Plot.nodisplay().converter(complexValue).title(title.toString()).titles(titles).asCurve();
            if (this.model.getProperties() != null) {
                for (Map.Entry<String, Object> ee : this.model.getProperties().entrySet()) {
                    pb.param(ee.getKey(), ee.getValue());
                }
            }
            PlotComponent chartPanel = pb.samples(Samples.absolute(y)).plot(yz);
            mainPanel_add(chartPanel.toComponent());

//        XYSeriesCollection data = new XYSeriesCollection();
//        for (int i = 0; i < yz.length; i++) {
//            XYSeries series = new XYSeries(String.valueOf(i + 1));
//            for (int j = 0; j < x.length; j++) {
//                series.add(x[j], yz[i][j]);
//            }
//            data.addSeries(series);
//        }
//
//        JFreeChart chart = ChartFactory.createXYLineChart(area.getName() + " (OX)", "X", "Y", data, PlotOrientation.VERTICAL, true, true, false);
//
//        XYPlot plot = (XYPlot) chart.getPlot();
//        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
//        axis.setAutoRangeIncludesZero(false);
//        axis.setAutoRangeMinimumSize(1.0);
//        ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        }
        mainPanel_invalidate();
    }

    public ComplexAsDouble getComplexAsDouble() {
        if (cadAbsRadioButton.isSelected()) {
            return ComplexAsDouble.ABS;
        }
        if (cadRealRadioButton.isSelected()) {
            return ComplexAsDouble.REAL;
        }
        if (cadImagRadioButton.isSelected()) {
            return ComplexAsDouble.IMG;
        }
        if (cadDbRadioButton.isSelected()) {
            return ComplexAsDouble.DB;
        }
        if (cadDb2RadioButton.isSelected()) {
            return ComplexAsDouble.DB2;
        }
        if (cadArgRadioButton.isSelected()) {
            return ComplexAsDouble.ARG;
        }
        if (cadComplexRadioButton.isSelected()) {
            return ComplexAsDouble.COMPLEX;
        }
        return ComplexAsDouble.ABS;
    }

    public void setComplexAsDouble(ComplexAsDouble complexAsDouble) {
        switch (complexAsDouble) {
            case ABS: {
                cadAbsRadioButton.setSelected(true);
                break;
            }
            case DB: {
                cadDbRadioButton.setSelected(true);
                break;
            }
            case DB2: {
                cadDb2RadioButton.setSelected(true);
                break;
            }
            case ARG: {
                cadArgRadioButton.setSelected(true);
                break;
            }
            case REAL: {
                cadRealRadioButton.setSelected(true);
                break;
            }
            case IMG: {
                cadImagRadioButton.setSelected(true);
                break;
            }
            case COMPLEX: {
                cadComplexRadioButton.setSelected(true);
                break;
            }
        }
    }

    public synchronized void updatePlotSurfaceAsynch() {
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        double[] y = resolveY();
        mainPanel_removeAll();
        for (CellPosition ax : getAxis()) {
            Complex[][] yz = selectedFunctions.length == 0 ? new Complex[0][] : getAxisExpression(selectedFunctions[0], ax).computeComplex(x, y, d, null);
            for (int i = 1; i < selectedFunctions.length; i++) {
                Complex[][] yz0 = getAxisExpression(selectedFunctions[i], ax).computeComplex(x, y, d, null);
                for (int j = 0; j < yz0.length; j++) {
                    Complex[] complexes = yz0[j];
                    for (int k = 0; k < complexes.length; k++) {
                        yz[j][k] = yz[j][k].add(complexes[k]);
                    }
                }
            }
            PlotBuilder pb = Plot.nodisplay().asCurve().converter(getComplexAsDouble()).title(this.model.getTitle()).asHeatMap();
            JComponent jComponent = pb.xsamples(x).ysamples(y).plot(yz).toComponent();
            mainPanel_add(jComponent);
        }
        mainPanel_invalidate();
    }

    public synchronized void updatePlotMatrixAsynch() {
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        double[] y = resolveY();
        mainPanel_removeAll();
        for (CellPosition ax : getAxis()) {
            Complex[][] yz = selectedFunctions.length == 0 ? new Complex[0][] : getAxisExpression(selectedFunctions[0], ax).computeComplex(x, y, d, null);
            for (int i = 1; i < selectedFunctions.length; i++) {
                Complex[][] yz0 = getAxisExpression(selectedFunctions[i], ax).computeComplex(x, y, d, null);
                for (int j = 0; j < yz0.length; j++) {
                    Complex[] complexes = yz0[j];
                    for (int k = 0; k < complexes.length; k++) {
                        yz[j][k] = yz[j][k].add(complexes[k]);
                    }
                }
            }

            ValuesPlotModel model2=new ValuesPlotModel();
            model2.setTitle(this.model.getTitle());
            model2.setPlotType(PlotType.MATRIX);
            model2.setXVector(x);
            model2.setYVector(y);
            model2.setZ(yz);
            model2.setZDoubleFunction(getComplexAsDouble());
            PlotBuilder pb = Plot.nodisplay().asCurve().converter(getComplexAsDouble()).title(this.model.getTitle()).asMatrix();
            JComponent jComponent = pb.xsamples(x).ysamples(y).plot(yz).toComponent();
            mainPanel_add(jComponent);
        }
        mainPanel_invalidate();
    }

    public synchronized void updatePlotTableAsynch() {
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        double[] y = resolveY();
        mainPanel_removeAll();
        for (CellPosition ax : getAxis()) {
            Complex[][] yz = selectedFunctions.length == 0 ? new Complex[0][] : getAxisExpression(selectedFunctions[0], ax).computeComplex(x, y, d, null);
            for (int i = 1; i < selectedFunctions.length; i++) {
                Complex[][] yz0 = getAxisExpression(selectedFunctions[i], ax).computeComplex(x, y, d, null);
                for (int j = 0; j < yz0.length; j++) {
                    Complex[] complexes = yz0[j];
                    for (int k = 0; k < complexes.length; k++) {
                        yz[j][k] = yz[j][k].add(complexes[k]);
                    }
                }
            }
            PlotBuilder pb = Plot.nodisplay().asCurve().converter(getComplexAsDouble()).title(this.model.getTitle()).asTable();
            JComponent jComponent = pb.xsamples(x).ysamples(y).plot(yz).toComponent();
            mainPanel_add(jComponent);
        }
        mainPanel_invalidate();
    }

//    private void mainPanel_add(PlotComponentPanel component,ValuesPlotModel model2) {
//        JComponent jComponent = component.toComponent();
//        Plot.buildJPopupMenu((PlotComponentPanel) component,new SimplePlotModelProvider(model2, jComponent));
//        model2.addPropertyChangeListener(modelUpdatesListener);
//        mainPanel_add(jComponent);
//
//    }
    private void mainPanel_add(final Component component) {
        try {
            SwingUtils.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    mainPanel.add(component);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mainPanel_removeAll() {
        try {
            SwingUtils.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    mainPanel.removeAll();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mainPanel_invalidate() {
        try {
            SwingUtils.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    mainPanel.invalidate();
                    mainPanel.validate();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updatePlotMeshAsynch() {
        Domain d = getDomainOrNull();
        double[] x = resolveX();
        double[] y = resolveY();

        mainPanel_removeAll();
        for (CellPosition ax : getAxis()) {
            if (selectedFunctions.length > 0) {
                Complex[][] yz = getAxisExpression(selectedFunctions[0], ax).computeComplex(x, y);
                for (int i = 1; i < selectedFunctions.length; i++) {
                    Complex[][] yz0 = getAxisExpression(selectedFunctions[i], ax).computeComplex(x, y);
                    for (int j = 0; j < yz0.length; j++) {
                        Complex[] complexes = yz0[j];
                        for (int k = 0; k < complexes.length; k++) {
                            yz[j][k] = yz[j][k].add(complexes[k]);
                        }
                    }
                }
                final ValuesPlotModel model = new ValuesPlotModel();
                model.setX(new double[][]{x});
                model.setY(new double[][]{y});
                model.setZ(yz);
                model.setZDoubleFunction(getComplexAsDouble());
                model.setTitle(this.model.getTitle());
                PlotComponentPanel mesh = ChartFactory.createMesh(model, null);
                Plot.buildJPopupMenu(mesh,new SimplePlotModelProvider(model,mesh.toComponent()));
                mainPanel_add(mesh.toComponent());
            }
        }
        mainPanel_invalidate();
    }

    public ComponentDimension getComponentDimension() {

        ComponentDimension m = ComponentDimension.SCALAR;
        for (Expr selectedFunction : selectedFunctions) {
            ComponentDimension d = selectedFunction.getComponentDimension();
            m = ComponentDimension.create(Math.max(d.rows, m.rows), Math.max(d.columns, m.columns));
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
        return axisOk.toArray(new CellPosition[axisOk.size()]);
//        if (fxAxisRadioButton.isSelected() && fyAxisRadioButton.isSelected()) {
//            return new int[]{VDCxy.X, VDCxy.Y};
//        } else {
//            return fyAxisRadioButton.isSelected() ? new int[]{VDCxy.Y} : new int[]{VDCxy.X};
//        }
    }

    public String getPlotTitle() {
        String baseTitle = super.getPlotTitle();
        if(baseTitle ==null){
            return this.model.getTitle();
        }
        return baseTitle;
    }

    public JComponent toComponent() {
        return (JComponent) this;
    }

    public void display() {
        windowManager.add(this);
    }

    public DoubleToComplex getAxisExpression(Expr e, CellPosition rc) {
        return getAxisExpression(e, rc.getRow(), rc.getColumn());
    }

    public DoubleToComplex getAxisExpression(Expr e, int r, int c) {
        DoubleToMatrix m = e.toDM();
        ComponentDimension d = m.getComponentDimension();
        if (d.rows > r && d.columns > c) {
            return m.getComponent(r, c).toDC();
        }
        return FunctionFactory.CZEROXY;
    }

    @Override
    public String getLayoutConstraints() {
        return layoutConstraints;
    }

    public void setLayoutConstraints(String layoutConstraints) {
        this.layoutConstraints = layoutConstraints;
    }

    @Override
    public PlotModel getModel() {
        return model;
    }

    @Override
    public void setModel(PlotModel model) {
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
        setComplexAsDouble(m.getComplexAsDouble());

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

        this.xPrecisionSlider.setModel(new DefaultBoundedRangeModel(m.getXprec(), 0, 1, m.getXprec() * 1000));
        this.yPrecisionSlider.setModel(new DefaultBoundedRangeModel(m.getYprec(), 0, 1, m.getYprec() * 1000));
        setXPrecision(m.getXprec(), false);
        setYPrecision(m.getYprec(), false);
        Samples samples = m.getSamples();
        setShowType(m.getShowType());
        functionsTableModel.setExpressions(functions);
        if (samples != null) {
            setSamples(samples,true);
        }
    }

    public enum ShowType {

        CURVE_FX,
        CURVE_FY,
        POLAR,
        SURFACE_2D,
        MATRIX,
        MESH,
        TABLE
    }

    private class UpdatePlotItemListener implements ItemListener, Serializable {

        public void itemStateChanged(ItemEvent e) {
            updatePlot(false);
        }
    }
}
