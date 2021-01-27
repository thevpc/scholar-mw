package net.thevpc.scholar.hadruplot.libraries.simple.heatmap;

import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.ValuesPlotXYDoubleModelFace;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.common.util.DoubleFormat;
import net.thevpc.common.util.MinMax;
import net.thevpc.common.util.PercentDoubleFormat;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;
import net.thevpc.scholar.hadruplot.util.PlotUtils;
import net.thevpc.scholar.hadruplot.util.SimpleDoubleFormat;
import net.thevpc.scholar.hadruplot.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HeatMapPlot extends JPanel implements PlotComponentPanel {
    private static final long serialVersionUID = 1L;
    private HeatMapPlotArea area;
    private PlotModelProvider plotModelProvider;
    private ValuesPlotModel model;
    private JLabel titleLabel;
    private StatusBar statusbar;
    private HeatMapPlotArea legend;
    private HeatMapPlotUnitsArea legendUnits;
    PropertyChangeListener legendUpdater = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            legend.setColorPalette(area.getColorPaletteContrasted());
        }
    };
    PropertyChangeListener modelUpdatesListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateAreaByModel();
        }
    };


    public HeatMapPlot(PlotModelProvider plotModelProvider) {
        this((ValuesPlotModel) plotModelProvider.getModel());
        this.plotModelProvider = plotModelProvider;
//        popupMenu = Plot.buildJPopupMenu(area, plotModelProvider);
    }

    public HeatMapPlot(ValuesPlotModel model) {
        this(new ValuesPlotXYDoubleModelFace(model, null), model.getPlotType().getType() == PlotType.HEATMAP);
        setModel(model);
    }

    public HeatMapPlot(PlotModelProvider plotModelProvider, ColorPalette palette, int preferredDim) {
        this((ValuesPlotModel) plotModelProvider.getModel(), palette, preferredDim);
        this.plotModelProvider = plotModelProvider;
//        Plot.buildJPopupMenu(area, plotModelProvider);
    }

    public HeatMapPlot(ValuesPlotModel model, ColorPalette palette, int preferredDim) {
        this(new ValuesPlotXYDoubleModelFace(model, null,true),
                model.getPlotType().getType() == PlotType.HEATMAP,
                palette, preferredDim);
        setModel(model);
    }

    public HeatMapPlot(ValuesPlotXYDoubleModelFace model, boolean reverseY) {
        this(model, reverseY, null, 400);
    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return getComponentPopupMenu();
    }

//    public void setPlotModel(ValuesPlotModel model) {
//        area.setxAxis(model.getXVector());
//        area.setyAxis(model.getY() == null || model.getY().length == 0 ? Maths.dsteps(1, model.getX().length, 1.0) : model.getY()[0]);
//        area.setData(model.getZd());
//        repaint();
//    }

    public PlotNormalizer getNormalizer() {
        return area.getNormalizer();
    }

    public void setNormalizer(PlotNormalizer normalizer) {
        area.setNormalizer(normalizer);
    }

    public HeatMapPlot(ValuesPlotXYDoubleModelFace model, boolean reverseY, ColorPalette colorPalette, int preferredDimension) {
        super(new BorderLayout());
        area = new HeatMapPlotArea(model, reverseY, colorPalette, new Dimension(preferredDimension <= 1 ? 400 : preferredDimension, preferredDimension <= 1 ? 400 : preferredDimension));
        area.addPropertyChangeListener("colorPaletteContrasted", legendUpdater);
        area.addPropertyChangeListener("zMinMax", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                MinMax m = (MinMax) evt.getNewValue();
                legendUnits.setMin(m.getMin());
                legendUnits.setMax(m.getMax());
            }
        });
        legend = new HeatMapPlotArea(false, -1,colorPalette, new Dimension(10, 400));
        legendUnits = new HeatMapPlotUnitsArea(false, area.getMinValue(), area.getMaxValue(), model.getZformat(), 5, new Dimension(50, 400));
        titleLabel = new JLabel(StringUtils.trim(model.getTitle()), SwingConstants.CENTER);
        if (titleLabel.getText().length() == 0) {
            titleLabel.setVisible(false);
        }
        statusbar = new StatusBar();
        add(titleLabel, BorderLayout.NORTH);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(BorderFactory.createTitledBorder(""));
        p.add(legendUnits, BorderLayout.PAGE_START);
        p.add(legend, BorderLayout.CENTER);

        add(area, BorderLayout.CENTER);
        add(p, BorderLayout.LINE_START);
        add(statusbar, BorderLayout.SOUTH);
        area.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
            }

            public void mouseMoved(MouseEvent e) {
                HeatMapCell cc = area.getCurrentCell(e);
                if (cc == null) {
                    statusbar.getiValueLabel().setAnyValue("");
                    statusbar.getjValueLabel().setAnyValue("");
                    statusbar.getxValueLabel().setAnyValue("");
                    statusbar.getyValueLabel().setAnyValue("");
                    statusbar.getzValueLabel().setAnyValue("");
                    statusbar.getzPercentValueLabel().setAnyValue("");
                } else {
                    statusbar.getiValueLabel().setAnyValue(cc.getxIndex() + 1);
                    statusbar.getjValueLabel().setAnyValue(cc.getyIndex() + 1);
                    statusbar.getxValueLabel().setAnyValue(cc.getxValue());
                    statusbar.getyValueLabel().setAnyValue(cc.getyValue());
                    statusbar.getzValueLabel().setAnyValue((cc.getzValue()));
                    statusbar.getzPercentValueLabel().setAnyValue((cc.getzPercent()));
                }
            }
        });
    }

    public void setModel(ValuesPlotModel model) {
        if (this.model != null) {
            this.model.removePropertyChangeListener(modelUpdatesListener);
        }
        LibraryPlotType oldPlotType = null;
        if (this.model != null) {
            oldPlotType = this.model.getPlotType();
        }
        this.model = model;
        this.model.addPropertyChangeListener(modelUpdatesListener);
        LibraryPlotType plotType = this.model.getPlotType();
        updateAreaByModel();
    }

    private void updateAreaByModel() {
        area.setModel(new ValuesPlotXYDoubleModelFace(model, null,true), model.getPlotType() .getType()== PlotType.HEATMAP);
        titleLabel.setText(StringUtils.trim(model.getTitle()));
        repaint();
    }

    public void setAreaPreferredDimension(int preferredDimension) {
        area.setPreferredDimension(new Dimension(preferredDimension, preferredDimension));
    }

    public void setData(double[][] matrix) {
        area.setData(matrix);
    }

    public Object[] getxAxis() {
        return area.getxAxis();
    }

    public void setxAxis(Object[] xAxis) {
        area.setxAxis(xAxis);
    }

    public Object[] getyAxis() {
        return area.getyAxis();
    }

    public void setyAxis(Object[] yAxis) {
        area.setyAxis(yAxis);
    }

    public void setyAxis(double[] yAxis) {
        area.setyAxis(yAxis);
    }

    public void setxAxis(double[] xAxis) {
        area.setxAxis(xAxis);
    }

    public ColorPalette getColorPalette() {
        return area.getColorPalette();
    }

    public void setColorPalette(ColorPalette colorPalette) {
        area.setColorPalette(colorPalette);
    }

    public void flipHorizontally() {
        area.flipHorizontally();
    }

    public void flipVertically() {
        area.flipVertically();
    }

    public void rotateLeft() {
        area.rotateLeft();
    }

    public void rotateRight() {
        area.rotateRight();
    }

    private class PercentStatusBarElement extends StatusBarElement {
        public PercentStatusBarElement(String name) {
            super(name);
            doubleFormat = PercentDoubleFormat.INSTANCE;
        }
    }

    private class StatusBarElement extends JLabel {
        protected DoubleFormat doubleFormat = SimpleDoubleFormat.INSTANCE;

        //        Dimension d=new Dimension(100, 10);
        public StatusBarElement(String name) {
            super("");
            setName(name);
//            setMinimumSize(d);
//            setAllDimensions(d);
//            setOpaque(true);
//            setBackground(Color.GREEN);
//            setBorder(BorderFactory.createEtchedBorder());
        }

//        public DecimalFormat getFormat() {
//            return format;
//        }
//
//        public void setFormat(DecimalFormat format) {
//            this.format = format;
//        }
//
//        public StatusBarElement setSimpleFormat(DecimalFormat simpleFormat) {
//            this.simpleFormat = simpleFormat;
//            return this;
//        }

        public void setAnyValue(Object d) {
            if (d == null) {
                setText("");
                d = "";
            } else {
                setText(doubleFormat.formatDouble(PlotConfigManager.Numbers.toDouble(d)));
            }
            setToolTipText(String.valueOf(d));
        }

        public void setDoubleValue(double d) {
            setAnyValue(d);
        }

        public void setIntValue(int d) {
            setText(String.valueOf(d));
            setToolTipText(String.valueOf(d));
        }

        public void setAllDimensions(Dimension d) {
//            setPreferredSize(d);
//            setMaximumSize(d);
//            setMinimumSize(d);
        }

//        public Dimension getSize(Dimension rv) {
//            return d;
//        }
    }

    private class StatusBar extends JPanel {
        StatusBarElement xValueLabel = new StatusBarElement("xValueLabel");
        StatusBarElement yValueLabel = new StatusBarElement("yValueLabel");
        StatusBarElement zValueLabel = new StatusBarElement("zValueLabel");
        PercentStatusBarElement zPercentValueLabel = new PercentStatusBarElement("zPercentValueLabel");
        StatusBarElement iValueLabel = new StatusBarElement("iValueLabel");
        StatusBarElement jValueLabel = new StatusBarElement("jValueLabel");

        public StatusBar() {
            super();
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//            setFloatable(false);
            setBorder(BorderFactory.createEtchedBorder());
            add(new JLabel("x : "));
            add(xValueLabel);
            addSeparator2();
            add(new JLabel("y : "));
            add(yValueLabel);
            addSeparator2();
            add(new JLabel("z : "));
            add(zValueLabel);
            addSeparator2();
            add(new JLabel("z% : "));
            add(zPercentValueLabel);
            addSeparator2();
            add(new JLabel("i : "));
            add(jValueLabel);
            addSeparator2();
            add(new JLabel("j : "));
            add(iValueLabel);
            addSeparator2();
            add(Box.createHorizontalGlue());
            add(Box.createVerticalStrut(20));
        }

        public void setSize(int width, int height) {
            super.setSize(width, height);
            if (xValueLabel != null) {
                Dimension d = new Dimension(width / 8, xValueLabel.getHeight());
                xValueLabel.setAllDimensions(d);
                yValueLabel.setAllDimensions(d);
                zValueLabel.setAllDimensions(d);
                zPercentValueLabel.setAllDimensions(d);
                iValueLabel.setAllDimensions(d);
                jValueLabel.setAllDimensions(d);
            }
        }

        private void addSeparator2() {
            JComponent c;
            c = (JComponent) Box.createRigidArea(new Dimension(6, 20));
            add(c);
            c = (JComponent) Box.createRigidArea(new Dimension(2, 20));
            c.setBorder(BorderFactory.createEtchedBorder());
            add(c);
            c = (JComponent) Box.createRigidArea(new Dimension(6, 20));
            add(c);
        }

        public StatusBarElement getxValueLabel() {
            return xValueLabel;
        }

        public StatusBarElement getyValueLabel() {
            return yValueLabel;
        }

        public StatusBarElement getzValueLabel() {
            return zValueLabel;
        }

        public StatusBarElement getiValueLabel() {
            return iValueLabel;
        }

        public StatusBarElement getjValueLabel() {
            return jValueLabel;
        }

        public StatusBarElement getzPercentValueLabel() {
            return zPercentValueLabel;
        }

    }

    @Override
    public JPopupMenu getComponentPopupMenu() {
        return area.getComponentPopupMenu();
//        return PlotUtils.getOrCreateComponentPopupMenu(area.getComponentPopupMenu());
    }

    @Override
    public void setComponentPopupMenu(JPopupMenu popup) {
        area.setComponentPopupMenu(popup);
    }

    public void setVisibleStatusBar(boolean value) {
        statusbar.setVisible(value);
    }

    public void setVisibleLegend(boolean value) {
        legend.setVisible(value);
    }

}
