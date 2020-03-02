package net.vpc.scholar.hadruplot;

import net.vpc.common.util.MinMax;
import net.vpc.scholar.hadruplot.backends.simple.heatmap.DefaultPlotNormalizer;
import net.vpc.scholar.hadruplot.util.PlotUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 19 juil. 2007 01:05:06
 */
public class PlotHyperCubePlotPanel extends BasePlotComponent implements PlotPanel {

    private final JPanel pp = new JPanel();
    private JComboBox d2d3 = new JComboBox(new Object[]{"1D", "2D", "Matrix", "3D"});
    private JComboBox xyz = new JComboBox(new Object[]{"X", "Y", "Z", "All"});
    private JComboBox surface = new JComboBox(new Object[]{"YZ", "XZ", "XY"});
    private JComboBox conversions = new JComboBox(PlotDoubleConverter.values());
    private JComboBox norm = new JComboBox(NormalizerType.values());
    private JSlider slider = new JSlider();
    private static final int TYPE_1D = 1;
    private static final int TYPE_2D = 2;
    private static final int TYPE_MATRIX = 3;
    private static final int TYPE_MESH = 4;
    private int typeSurface = TYPE_2D;
    private int[] xyzValue = {0,1,2};
    private int surfaceValue = 2;
    private PlotDoubleConverter convert = PlotDoubleConverter.ABS;
    //    private String title;
    private NormalizerType normalizerType = NormalizerType.FULL;
    private int revalidatingPlot = 0;
    private PlotHyperCubePlotModel model2;

    //    public PlotHyperCubePlotPanel(PlotWindowManager windowManager, String title, Set<ExternalLibrary> preferredLibraries, VDiscrete... _model) {
//
//    }
    public PlotHyperCubePlotPanel(PlotHyperCubePlotModel model, PlotWindowManager windowManager) {
        super(new BorderLayout());
        if (model == null) {
            model = new PlotHyperCubePlotModel();
        }
        this.model2 = model;
        this.convert = model.getConverter();
        setPlotWindowManager(windowManager);
        JToolBar tb = new JToolBar();
        d2d3.setSelectedIndex(typeSurface - 1);
        tb.add(new JLabel("2D/3D"));
        tb.add(d2d3);
        tb.add(new JLabel("Axis"));
        tb.add(xyz);
        tb.add(new JLabel("Norm"));
        tb.add(norm);
        tb.addSeparator();
        tb.add(new JLabel("Surface"));
        tb.add(surface);
        tb.add(new JLabel("Conversion"));
        tb.add(conversions);
        tb.addSeparator();
        tb.add(new JLabel("Value"));
        tb.add(slider);
        add(tb, BorderLayout.PAGE_START);
        xyz.setSelectedIndex(3);
        norm.setSelectedItem(normalizerType);
        pp.setLayout(new GridLayout(1, 0));
        add(pp, BorderLayout.CENTER);
        surface.setSelectedIndex(2);
        conversions.setSelectedIndex(0);
        xyz.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int selectedIndex = xyz.getSelectedIndex();
//                    Axis[] old = xyzValue;
                    if (selectedIndex >= 0 && selectedIndex < 3) {
                        xyzValue = new int[]{selectedIndex};
                    } else {
                        xyzValue = new int[]{0,1,2};
                    }
                    revalidatePlot();
                }
            }
        });
        d2d3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int selectedIndex = d2d3.getSelectedIndex();
                    typeSurface = (selectedIndex + 1);
                    revalidatePlot();
                }
            }
        });
        norm.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    normalizerType = (NormalizerType) norm.getSelectedItem();
                    revalidatePlot();
                }
            }
        });
        surface.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    surfaceValue = surface.getSelectedIndex();
                    revalidatePlot();
                }
            }
        });
        conversions.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    convert = (PlotDoubleConverter) conversions.getSelectedItem();
                    revalidatePlot();
                }
            }
        });
        slider.setValue(0);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
//                    System.out.println(slider.getValue());
                    revalidatePlot();
                }
            }
        });
        revalidatePlot();
    }

    @Override
    public boolean accept(PlotModel model) {
        return model instanceof PlotHyperCubePlotModel;
    }

    @Override
    public void setModel(PlotModel model) {
        if (model == null) {
            model = new PlotHyperCubePlotModel();
        }
        this.model2 = (PlotHyperCubePlotModel) model;
    }

    public void revalidatePlot() {
        if (revalidatingPlot != 0) {
            revalidatingPlot = 2;
            return;
        }
        try {
            revalidatingPlot = 1;
            synchronized (pp) {
                pp.removeAll();
                int axisIndex = 0;
                for (final int axis : xyzValue) {
                    int componentDimension = model2.getHyperCubes()[0].getCubesCount();
                    if (axisIndex >= componentDimension) {
                        break;
                    }
                    axisIndex++;
                    PlotCube discrete = null;
                    try {
                        discrete = model2.getHyperCubes()[0].getCube(axis);
                    } catch (Exception ee) {
                        //
                    }
                    if (discrete != null) {
                        int max = discrete.getCount(surfaceValue) - 1;
                        if (slider.getMaximum() != max) {
                            slider.setMaximum(max);
                        }
                        int value = slider.getValue();
                        if (value < 0) {
                            value = 0;
                        }
                        if (value > max) {
                            value = max;
                        }
                        Object[][] renderedValues0 = discrete.getArray(surfaceValue, value);
                        Object[][] renderedValues = new Object[model2.getHyperCubes().length * renderedValues0.length][renderedValues0.length == 0 ? 0 : renderedValues0[0].length];
                        for (int i = 0; i < model2.getHyperCubes().length; i++) {
                            PlotCube cube2 = model2.getHyperCubes()[i].getCube(axis);
                            Object[][] cube_i = cube2.getArray(surfaceValue, value);
                            System.arraycopy(cube_i, 0, renderedValues, i * renderedValues0.length + 0, renderedValues0.length);
                        }
                        double[] axis1 = discrete.getX();
                        double[] axis2 = discrete.getY();
                        switch (surfaceValue) {
                            case 2: {
                                axis1 = discrete.getX();
                                axis2 = discrete.getY();
                                break;
                            }
                            case 0: {
                                axis1 = discrete.getY();
                                axis2 = discrete.getZ();
                                break;
                            }
                            case 1: {
                                axis1 = discrete.getX();
                                axis2 = discrete.getZ();
                                break;
                            }
                        }
                        ValuesPlotModel pmodel = new ValuesPlotModel(
                                "Axe " + axis + " ; Coupe " + surface.getSelectedItem(), null, null, null, axis1, axis2, renderedValues,
                                convert,
                                new LibraryPlotType(PlotType.HEATMAP,null),
                                null
                        );
                        SimplePlotModelProvider modelProvider = new SimplePlotModelProvider(pmodel, this);
                        switch (typeSurface) {
                            case TYPE_2D: {
                                LibraryPlotType plotType = new LibraryPlotType(PlotType.HEATMAP, null);
                                pmodel.setPlotType(plotType);
                                pp.add(PlotBackendLibraries.createPlotComponentPanel(
                                        new DefaultPlotComponentContext(plotType, modelProvider)
                                                .setNormalizer(new ExtendedNormalizer(axis))
                                                .setPreferredWidth(200)
                                ).toComponent());
                                break;
                            }
                            case TYPE_MATRIX: {
                                LibraryPlotType plotType = new LibraryPlotType(PlotType.MATRIX, null);
                                pmodel.setPlotType(plotType);
                                pp.add(PlotBackendLibraries.createPlotComponentPanel(
                                        new DefaultPlotComponentContext(plotType, modelProvider)
                                        .setNormalizer(new ExtendedNormalizer(axis))
                                        .setPreferredWidth(200)
                                ).toComponent());

                                break;
                            }
                            case TYPE_MESH: {
                                LibraryPlotType plotType = new LibraryPlotType(PlotType.MESH, null);
                                pmodel.setPlotType(plotType);
                                pp.add(PlotBackendLibraries.createPlotComponentPanel(new DefaultPlotComponentContext(plotType, modelProvider)).toComponent());
                                break;
                            }
                            case TYPE_1D: {
                                LibraryPlotType plotType = new LibraryPlotType(PlotType.CURVE, null);
                                pmodel.setPlotType(plotType);
                                pp.add(PlotBackendLibraries.createPlotComponentPanel(new DefaultPlotComponentContext(plotType, modelProvider)).toComponent());
                                break;
                            }
                            default:
                                break;
                        }
                    }
                }
                pp.revalidate();
                pp.repaint();
            }
        } finally {
            if (revalidatingPlot == 2) {
                revalidatingPlot = 0;
                revalidatePlot();
            } else {
                revalidatingPlot = 0;
            }
        }
    }

    public String getPlotTitle() {
        String baseTitle = super.getPlotTitle();
        if (baseTitle == null) {
            return this.model2.getTitle();
        }
        return baseTitle;
    }

    public enum NormalizerType {
        MATRIX, CUBE, FULL
    }

    private class ExtendedNormalizer extends DefaultPlotNormalizer {

        private int axis;

        public ExtendedNormalizer(int axis) {
            this.axis = axis;
        }

        public double[][] normalize(double[][] baseValues) {
            MinMax minMax = new MinMax();
            switch (normalizerType) {
                case MATRIX: {
                    for (double[] y : baseValues) {
                        for (double x : y) {
                            minMax.registerValue(x);
                        }
                    }
                    break;
                }
                case FULL: {
                    for (PlotHyperCube c3D : model2.getHyperCubes()) {
                        int d = c3D.getCubesCount();
                        for (int i = 0; i < d; i++) {
                            Object[][][] c3 = c3D.getCube(i).getValues();
                            PlotDoubleConverter dc = convert;
                            if (dc == null) {
                                dc = PlotUtils.resolveDoubleConverter(c3);
                            }
                            for (Object[][] z : c3) {
                                for (Object[] y : z) {
                                    for (Object x : y) {
                                        minMax.registerValue(dc.toDouble(x));
                                    }
                                }
                            }
                        }
                    }
                }
                case CUBE: {
                    for (PlotHyperCube c3D : model2.getHyperCubes()) {
                        Object[][][] c3 = c3D.getCube(axis).getValues();
                        PlotDoubleConverter dc = convert;
                        if (dc == null) {
                            dc = PlotUtils.resolveDoubleConverter(c3);
                        }

                        for (Object[][] z : c3) {
                            for (Object[] y : z) {
                                for (Object x : y) {
                                    minMax.registerValue(dc.toDouble(x));
                                }
                            }
                        }
                    }
                    break;
                }
            }
            return normalize(baseValues, minMax.getMin(), minMax.getMax());
        }
    }

    @Override
    public PlotModel getModel() {
        return getModel();
    }
}
