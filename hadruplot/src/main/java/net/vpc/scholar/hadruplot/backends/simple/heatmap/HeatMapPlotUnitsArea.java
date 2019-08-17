package net.vpc.scholar.hadruplot.backends.simple.heatmap;


import net.vpc.common.util.DoubleFormat;
import net.vpc.scholar.hadruplot.util.SimpleDoubleFormat;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class HeatMapPlotUnitsArea extends JComponent {
    private static final long serialVersionUID = 1L;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private int ticksCount = 10;
    private boolean horizontal;
    private double min;
    private double max;
    private DoubleFormat doubleFormat;
    private Font font;
    private Font font0;

    public HeatMapPlotUnitsArea(boolean horizontal, double min, double max, DoubleFormat doubleFormat, int ticksCount, Dimension preferredDimension) {
        if (ticksCount <= 2) {
            ticksCount = 2;
        }
        this.horizontal = horizontal;
        this.ticksCount = ticksCount;
        this.min = min;
        this.max = max;
        this.doubleFormat = doubleFormat;
        setPreferredDimension(preferredDimension);
    }

    public double getMin() {
        return min;
    }

    public HeatMapPlotUnitsArea setMin(double min) {
        this.min = min;
        return this;
    }

    public double getMax() {
        return max;
    }

    public HeatMapPlotUnitsArea setMax(double max) {
        this.max = max;
        return this;
    }

    public void setPreferredDimension(Dimension preferredDimension) {
        float factor = 1;
        Dimension dim = (factor <= 1) ?
                new Dimension(preferredDimension.width, (int) (preferredDimension.height * factor))
                : new Dimension((int) (preferredDimension.width / factor), preferredDimension.height);
        setMinimumSize(dim);
        setPreferredSize(dim);
        setSize(dim);
    }


    public void paint(Graphics g) {
        if (font == null) {
            if (font0 == null) {
                font0 = getFont();
            }
            font = font0.deriveFont(9.0f);
        }
        g.setFont(font);
        Dimension d = getSize();
        g.setColor(getBackground());
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.black);
        FontMetrics fm = g.getFontMetrics();
        double hh = (d.getHeight() - fm.getHeight()) / (ticksCount - 1);
        for (int i = 0; i < ticksCount; i++) {
            double percent = i * 100.0 / (ticksCount - 1);
            String ss = formatPercent(percent);
//            System.out.println(percent+"==>"+ss);
            int ww = fm.stringWidth(ss);
            g.drawString(ss, d.width - ww - 5, (int) (d.height - i * hh));
            g.drawLine(d.width - 3, (int) (d.height - (i + 0.0) * hh), d.width, (int) (d.height - (i + 0.0) * hh));
        }
    }

    public String formatPercent(double v) {
        if (Double.isNaN(min) || Double.isNaN(max) || min == max) {
            return df.format(v) + "%";
        } else {
            double v2 = min + (v / 100.0) * (max - min);
            if (doubleFormat == null) {
                return SimpleDoubleFormat.INSTANCE.formatDouble(v2);
            }
            return doubleFormat.formatDouble(v2);
        }
    }


}
