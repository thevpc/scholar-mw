/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MinMax;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.CategoryPointerAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.xy.XYLine3DRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.*;
import org.jfree.ui.TextAnchor;
import org.jfree.util.TableOrder;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * @author vpc
 */
public abstract class PlotCanvasAnyJFreeChart extends JPanel implements PlotComponentPanel {
    protected PlotModelProvider plotModelProvider;
    protected ChartPanel chartPanel;
    protected JColorPalette paintArray = Maths.DEFAULT_PALETTE;
    protected PlotConfig config;

    public PlotCanvasAnyJFreeChart(PlotModelProvider plotModelProvider) {
        super(new BorderLayout());
        this.plotModelProvider = plotModelProvider;
    }

//    protected double[] createDefaultXX() {
//        return Maths.dtimes(1.0, yAxis[0].length, yAxis[0].length);
//    }

    protected double getDefaultXMultiplier() {
        return 1;
    }



//    private ValuesPlotModel getModel() {
//        return (ValuesPlotModel) plotModelProvider.getModel();
//    }

    protected void loadConfig() {
        ValuesPlotModel model = (ValuesPlotModel) plotModelProvider.getModel();
        config = (PlotConfig) model.getProperty("config", null);
        config = PlotConfig.copy(config).validate(model.getZ().length);
        config.defaultXMultiplier = getDefaultXMultiplier();
    }

    protected abstract int dataSize();

    protected abstract void init();

    protected void onUserAnnotationXY(ChartMouseEvent event, XYPlot plot, XYItemEntity entity, boolean custom) {
        int item = entity.getItem();
        int series = entity.getSeriesIndex();
        double x = entity.getDataset().getX(series, item).doubleValue();
        double y = entity.getDataset().getY(series, item).doubleValue();

        //find old annotation
        XYPointerAnnotation found = null;
        for (Object o : plot.getAnnotations()) {
            if (o instanceof XYPointerAnnotation) {
                XYPointerAnnotation r = (XYPointerAnnotation) o;
                if (r.getX() == x && r.getY() == y) {
                    found = r;
                    break;
                }
            }
        }

        if (found != null) {
            plot.removeAnnotation(found);
        } else {
            String s = String.valueOf(y);
            if (custom) {
                s = (String) JOptionPane.showInputDialog(this, "select message text (default is y=" + s + ")", "Add Annotation", JOptionPane.QUESTION_MESSAGE, null, null, s);
                if (s == null) {
                    return;
                }
            }

            XYPointerAnnotation annotation = new XYPointerAnnotation(s, x, y, 2 * Math.PI / 32 * Maths.randomInt(32));
            annotation.setTipRadius(0.0D);
            annotation.setBaseRadius(35.0D);
            annotation.setFont(new Font("SansSerif", 0, 9));
            annotation.setPaint(Color.blue);
            annotation.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
            plot.addAnnotation(annotation);
        }
    }

    protected void onUserAnnotationCategory(ChartMouseEvent event, CategoryPlot plot, CategoryItemEntity entity, boolean custom) {
        Comparable columnKey = entity.getColumnKey();
        Comparable rowKey = entity.getRowKey();
        double y = entity.getDataset().getValue(rowKey, columnKey).doubleValue();

        //find old annotation
        CategoryPointerAnnotation found = null;
        for (Object o : plot.getAnnotations()) {
            if (o instanceof CategoryPointerAnnotation) {
                CategoryPointerAnnotation r = (CategoryPointerAnnotation) o;
                if (r.getCategory().equals(columnKey)) {
                    found = r;
                    break;
                }
            }
        }

        if (found != null) {
            plot.removeAnnotation(found);
        } else {
            String s = String.valueOf(y);
            if (custom) {
                s = (String) JOptionPane.showInputDialog(this, "select message text (default is y=" + s + ")", "Add Annotation", JOptionPane.QUESTION_MESSAGE, null, null, s);
                if (s == null) {
                    return;
                }
            }

            CategoryPointerAnnotation annotation = new CategoryPointerAnnotation(s, columnKey, y, 2 * Math.PI / 32 * Maths.randomInt(32));
            annotation.setTipRadius(0.0D);
            annotation.setBaseRadius(35.0D);
            annotation.setFont(new Font("SansSerif", 0, 9));
            annotation.setPaint(Color.blue);
            annotation.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
            plot.addAnnotation(annotation);
        }
    }

    protected void onUserAnnotationPie(ChartMouseEvent event, PiePlot plot, PieSectionEntity entity, boolean custom) {
        Comparable columnKey = entity.getSectionKey();
        int index = entity.getPieIndex();
        double y = entity.getDataset().getValue(index).doubleValue();

//        //annotations are not supported!
//        CategoryPointerAnnotation found = null;
//        for (Object o : plot.getAnnotations()) {
//            if (o instanceof CategoryPointerAnnotation) {
//                CategoryPointerAnnotation r = (CategoryPointerAnnotation) o;
//                if (r.getCategory().equals(columnKey)) {
//                    found = r;
//                    break;
//                }
//            }
//        }
//
//        if (found != null) {
//            plot.removeAnnotation(found);
//        } else {
//            String s = String.valueOf(y);
//            if (custom) {
//        s = (String)JOptionPane.showInputDialog(this,"select message text (default is y=" + s + ")", "Add Annotation",JOptionPane.QUESTION_MESSAGE,null,null,s);
//                if (s == null) {
//                    return;
//                }
//            }
//
//            CategoryPointerAnnotation annotation = new CategoryPointerAnnotation(s, columnKey, y, 2 * Math.PI / 32 * Maths.randomInt(32));
//            annotation.setTipRadius(0.0D);
//            annotation.setBaseRadius(35.0D);
//            annotation.setFont(new Font("SansSerif", 0, 9));
//            annotation.setPaint(Color.blue);
//            annotation.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
//            plot.addAnnotation(annotation);
//        }
    }

    protected void onUserAnnotation(ChartMouseEvent event, boolean custom) {
        if (event.getChart().getPlot() instanceof CategoryPlot && event.getEntity() instanceof CategoryItemEntity) {
            onUserAnnotationCategory(event, event.getChart().getCategoryPlot(), (CategoryItemEntity) event.getEntity(), custom);
        } else if (event.getChart().getPlot() instanceof XYPlot && event.getEntity() instanceof XYItemEntity) {
            onUserAnnotationXY(event, event.getChart().getXYPlot(), (XYItemEntity) event.getEntity(), custom);
        } else if (event.getChart().getPlot() instanceof PiePlot && event.getEntity() instanceof PieSectionEntity) {
            onUserAnnotationPie(event, (PiePlot) event.getChart().getPlot(), (PieSectionEntity) event.getEntity(), custom);
        }
    }

    protected void prepareChartPanel() {
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                if (event.getTrigger().getClickCount() == 2) {
                    onUserAnnotation(event, !event.getTrigger().isControlDown());
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {

            }
        });
    }

    protected void prepareJFreeChart(JFreeChart chart, MinMax x_minmax) {

    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return chartPanel.getPopupMenu();
    }

    protected abstract JFreeChart createChart(String theYTitle, Boolean legend, Boolean tooltips);

    protected AreaRenderer prepareAreaRenderer() {
        AreaRenderer r = null;
        switch (config.lineStepType) {
            case DEFAULT: {
                r = new AreaRenderer() {
                    @Override
                    public Paint getItemPaint(int row, int column) {
                        Paint paint = lookupSeriesPaint(row);
                        if (paint instanceof Color) {
                            Color c = (Color) paint;
                            return new Color(
                                    c.getRed(),
                                    c.getGreen(),
                                    c.getBlue(),
                                    127
                            );
                        }
                        return paint;
                    }
                };
                break;
            }
        }
        if (config.nodeLabel) {
//            localXYStepRenderer.setSeriesStroke(0, new BasicStroke(2.0F));
//            localXYStepRenderer.setSeriesStroke(1, new BasicStroke(2.0F));
            r.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
//            localXYStepRenderer.setDefaultEntityRadius(6);
            r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            r.setBaseItemLabelsVisible(true);
            r.setBaseItemLabelFont(new Font("Dialog", Font.PLAIN, 8));
        }
        int jfreeChartIndex = 0;
        for (int i = 0; i < dataSize(); i++) {
            PlotConfig lineConfig = config.children.get(i);
            Color color = null;
            if (config.alternateColor) {
                color = lineConfig.color;
                if (color == null) {
                    color = paintArray.getColor(i * 1.0f / dataSize());
                }
            } else {
                color = lineConfig.color;
                if (color == null) {
                    color = config.color;
                }
                if (color == null) {
                    color = Color.BLACK;
                }
            }
            r.setSeriesPaint(jfreeChartIndex, color);

            Integer nodeType = null;
            if (config.alternateNode) {
                nodeType = lineConfig.nodeType;
                if (nodeType == null) {
                    nodeType = -1; //zero will alternate
                } else {
                    nodeType = nodeType;
                }
            } else {
                nodeType = lineConfig.nodeType;
                if (nodeType == null) {
                    nodeType = config.nodeType;
                }
                if (nodeType == null) {
                    nodeType = 0;
                } else {
                    nodeType = nodeType + 1;
                }
            }
            if (nodeType == -1) {
                //will alternate by default
            } else {
                switch (Maths.abs(nodeType) % 3) {
                    case 0: {
                        if (lineConfig.shapesVisible == null) {
                            lineConfig.shapesVisible = false;
                        }
                        if (lineConfig.shapesFilled == null) {
                            lineConfig.shapesFilled = true;
                        }
                        //leave defaults
//                        r.setSeriesShapesFilled(jfreeChartIndex, lineConfig.shapesFilled);
//                        r.setSeriesShapesVisible(jfreeChartIndex, lineConfig.shapesVisible);
                        break;
                    }
                    case 1: {
                        if (lineConfig.shapesVisible == null) {
                            lineConfig.shapesVisible = true;
                        }
                        if (lineConfig.shapesFilled == null) {
                            lineConfig.shapesFilled = false;
                        }
//                        r.setSeriesShapesFilled(jfreeChartIndex, lineConfig.shapesFilled);
//                        r.setSeriesShapesVisible(jfreeChartIndex, lineConfig.shapesVisible);
                        break;
                    }
                    case 2: {
                        if (lineConfig.shapesVisible == null) {
                            lineConfig.shapesVisible = true;
                        }
                        if (lineConfig.shapesFilled == null) {
                            lineConfig.shapesFilled = true;
                        }
//                        r.setSeriesShapesVisible(jfreeChartIndex, lineConfig.shapesFilled);
//                        r.setSeriesShapesFilled(jfreeChartIndex, lineConfig.shapesVisible);
                        break;
                    }
                }
            }
            Integer lineType = null;
            if (config.alternateLine) {
                lineType = lineConfig.lineType;
                if (lineType == null) {
                    lineType = i;
                }
            } else {
                lineType = lineConfig.lineType;
                if (lineType == null) {
                    lineType = config.lineType;
                }
                if (lineType == null) {
                    lineType = 0;
                }
            }
            if (lineConfig.lineVisible == null) {
                lineConfig.lineVisible = true;
            }
//            r.setSeriesLinesVisible(jfreeChartIndex, lineConfig.lineVisible);

            switch (Maths.abs(lineType) % 11) {
                case 0: {
                    break;
                }
                case 1: {
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{10F, 6F}, 0.0F));
                    break;
                }
                case 2: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{6F, 6F}, 0.0F));
                    break;
                }
                case 3: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{2.0F, 6F}, 0.0F));
                    break;
                }
                case 4: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{1.0F, 1F}, 0.0F));
                    break;
                }
                case 5: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{2.0F, 2F}, 0.0F));
                    break;
                }
                case 6: {
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{10F, 6F}, 0.0F));
                    break;
                }
                case 7: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{6F, 6F}, 0.0F));
                    break;
                }
                case 8: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{2.0F, 6F}, 0.0F));
                    break;
                }
                case 9: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{1.0F, 1F}, 0.0F));
                    break;
                }
                case 10: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{2.0F, 2F}, 0.0F));
                    break;
                }
            }

            jfreeChartIndex++;
        }

        return r;
    }

    protected XYLineAndShapeRenderer prepareXYRenderer() {
//        ValuesPlotModel model = getModel();


//        DefaultXYItemRenderer localXYStepRenderer = new DefaultXYItemRenderer();
////        localXYStepRenderer.setSeriesStroke(0, new BasicStroke(1.0F));
////        localXYStepRenderer.setSeriesStroke(1, new BasicStroke(1.0F));
////        localXYStepRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
////        localXYStepRenderer.setDefaultEntityRadius(20);
////        localXYStepRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
////        localXYStepRenderer.setBaseItemLabelsVisible(true);
////        localXYStepRenderer.setBaseItemLabelFont(new Font("Dialog", 0, 8));
//        localXYPlot.setRenderer(localXYStepRenderer);
//
////        XYStepRenderer localXYStepRenderer = new XYStepRenderer();
//////        localXYStepRenderer.setSeriesStroke(0, new BasicStroke(1.0F));
//////        localXYStepRenderer.setSeriesStroke(1, new BasicStroke(1.0F));
////        localXYStepRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
////        localXYStepRenderer.setDefaultEntityRadius(20);
////        localXYStepRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
////        localXYStepRenderer.setBaseItemLabelsVisible(true);
////        localXYStepRenderer.setBaseItemLabelFont(new Font("Dialog", 1, 14));
////        localXYPlot.setRenderer(localXYStepRenderer);
////


//        String[] ytitles = model.getYtitles();
        XYLineAndShapeRenderer r = null;
        switch (config.lineStepType) {
            case STEP: {
                r = new XYStepRenderer();
                break;
            }
            case SPLINE: {
                r = new XYSplineRenderer();
                break;
            }
            case DEFAULT: {
                if (config.threeD) {
                    r = new XYLine3DRenderer();
                } else {
                    r = new XYLineAndShapeRenderer();
                }
                break;
            }
        }
        if (config.nodeLabel) {
//            localXYStepRenderer.setSeriesStroke(0, new BasicStroke(2.0F));
//            localXYStepRenderer.setSeriesStroke(1, new BasicStroke(2.0F));
            r.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
//            localXYStepRenderer.setDefaultEntityRadius(6);
            r.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
            r.setBaseItemLabelsVisible(true);
            r.setBaseItemLabelFont(new Font("Dialog", Font.PLAIN, 8));
        }
        int jfreeChartIndex = 0;
        for (int i = 0; i < dataSize(); i++) {
            PlotConfig lineConfig = config.children.get(i);
            Color color = null;
            if (config.alternateColor) {
                color = lineConfig.color;
                if (color == null) {
                    color = paintArray.getColor(i * 1.0f / dataSize());
                }
            } else {
                color = lineConfig.color;
                if (color == null) {
                    color = config.color;
                }
                if (color == null) {
                    color = Color.BLACK;
                }
            }
            r.setSeriesPaint(jfreeChartIndex, color);

            Integer nodeType = null;
            if (config.alternateNode) {
                nodeType = lineConfig.nodeType;
                if (nodeType == null) {
                    nodeType = -1; //zero will alternate
                } else {
//                    nodeType = nodeType;
                }
            } else {
                nodeType = lineConfig.nodeType;
                if (nodeType == null) {
                    nodeType = config.nodeType;
                }
                if (nodeType == null) {
                    nodeType = 0;
                } else {
                    nodeType = nodeType + 1;
                }
            }
            if (nodeType == -1) {
                //will alternate by default
            } else {


                switch (Maths.abs(nodeType) % 3) {
                    case 0: {
                        if (lineConfig.shapesVisible == null) {
                            lineConfig.shapesVisible = false;
                        }
                        if (lineConfig.shapesFilled == null) {
                            lineConfig.shapesFilled = true;
                        }
                        //leave defaults
                        r.setSeriesShapesFilled(jfreeChartIndex, lineConfig.shapesFilled);
                        r.setSeriesShapesVisible(jfreeChartIndex, lineConfig.shapesVisible);
                        break;
                    }
                    case 1: {
                        if (lineConfig.shapesVisible == null) {
                            lineConfig.shapesVisible = true;
                        }
                        if (lineConfig.shapesFilled == null) {
                            lineConfig.shapesFilled = false;
                        }
                        r.setSeriesShapesFilled(jfreeChartIndex, lineConfig.shapesFilled);
                        r.setSeriesShapesVisible(jfreeChartIndex, lineConfig.shapesVisible);
                        break;
                    }
                    case 2: {
                        if (lineConfig.shapesVisible == null) {
                            lineConfig.shapesVisible = true;
                        }
                        if (lineConfig.shapesFilled == null) {
                            lineConfig.shapesFilled = true;
                        }
                        r.setSeriesShapesVisible(jfreeChartIndex, lineConfig.shapesFilled);
                        r.setSeriesShapesFilled(jfreeChartIndex, lineConfig.shapesVisible);
                        break;
                    }
                }
            }
            Integer lineType = null;
            if (config.alternateLine) {
                lineType = lineConfig.lineType;
                if (lineType == null) {
                    lineType = i;
                }
            } else {
                lineType = lineConfig.lineType;
                if (lineType == null) {
                    lineType = config.lineType;
                }
                if (lineType == null) {
                    lineType = 0;
                }
            }
            if (lineConfig.lineVisible == null) {
                lineConfig.lineVisible = true;
            }
            r.setSeriesLinesVisible(jfreeChartIndex, lineConfig.lineVisible);

            switch (Maths.abs(lineType) % 11) {
                case 0: {
                    break;
                }
                case 1: {
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{10F, 6F}, 0.0F));
                    break;
                }
                case 2: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{6F, 6F}, 0.0F));
                    break;
                }
                case 3: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{2.0F, 6F}, 0.0F));
                    break;
                }
                case 4: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{1.0F, 1F}, 0.0F));
                    break;
                }
                case 5: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(1.0F, 1, 1, 1.0F, new float[]{2.0F, 2F}, 0.0F));
                    break;
                }
                case 6: {
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{10F, 6F}, 0.0F));
                    break;
                }
                case 7: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{6F, 6F}, 0.0F));
                    break;
                }
                case 8: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{2.0F, 6F}, 0.0F));
                    break;
                }
                case 9: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{1.0F, 1F}, 0.0F));
                    break;
                }
                case 10: {
                    //visible line
                    r.setSeriesStroke(jfreeChartIndex, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{2.0F, 2F}, 0.0F));
                    break;
                }
            }

            jfreeChartIndex++;
        }
        return r;
    }

}
