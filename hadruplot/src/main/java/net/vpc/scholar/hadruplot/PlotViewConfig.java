package net.vpc.scholar.hadruplot;

import net.vpc.common.util.BooleanHolder;
import net.vpc.common.util.DoubleHolder;
import net.vpc.common.util.IntegerHolder;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PlotViewConfig {
    public PlotConfigLineStepType lineStepType;
    public BooleanHolder showLegend = new BooleanHolder();
    public IntegerHolder maxLegendCount = new IntegerHolder();
    public BooleanHolder showTooltips = new BooleanHolder();
    public BooleanHolder nodeLabel = new BooleanHolder();
    public BooleanHolder threeD = new BooleanHolder();
    public BooleanHolder alternateColor = new BooleanHolder();
    public BooleanHolder alternateNode = new BooleanHolder();
    public BooleanHolder alternateLine = new BooleanHolder();
    public BooleanHolder clockwise = new BooleanHolder();
    public DoubleHolder polarAngleOffset = new DoubleHolder();

    public Color color;
    public IntegerHolder lineType = new IntegerHolder();
    public IntegerHolder nodeType = new IntegerHolder();
    public BooleanHolder shapesVisible = new BooleanHolder();
    public BooleanHolder lineVisible = new BooleanHolder();
    public BooleanHolder shapesFilled = new BooleanHolder();
    public DoubleHolder xmultiplier = new DoubleHolder();
    public DoubleHolder ymultiplier = new DoubleHolder();
    public DoubleHolder defaultXMultiplier = new DoubleHolder();

    public List<PlotViewConfig> children = new ArrayList<>();

    public PlotViewConfig copy() {
        PlotViewConfig other = new PlotViewConfig();
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getType().equals(IntegerHolder.class)) {
                IntegerHolder h = null;
                IntegerHolder m = null;
                try {
                    h = (IntegerHolder) field.get(other);
                    m = (IntegerHolder) field.get(this);
                    h.set(m.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (field.getType().equals(BooleanHolder.class)) {
                BooleanHolder h = null;
                BooleanHolder  m = null;
                try {
                    h = (BooleanHolder) field.get(other);
                    m = (BooleanHolder) field.get(this);
                    h.set(m.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (field.getType().equals(DoubleHolder.class)) {
                DoubleHolder h = null;
                DoubleHolder  m = null;
                try {
                    h = (DoubleHolder) field.get(other);
                    m = (DoubleHolder) field.get(this);
                    h.set(m.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (field.getName().equals("children")) {
                //do nothing...
            }else{
                try {
                    field.set(other,field.get(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        other.children = new ArrayList<>();
        for (PlotViewConfig child : children) {
            other.children.add(child.copy());
        }
        return other;
    }

    public double getDefaultXMultiplier(double val) {
        return defaultXMultiplier.get(val);
    }

    public double getXMultiplier(double val) {
        return xmultiplier.get(val);
    }

    public double getYMultiplier(double val) {
        return ymultiplier.get(val);
    }

    public double getXMultiplierAt(int index, double val) {
        if (index < children.size()) {
            children.get(index).getXMultiplier(val);
        }
        return val;
    }

    public double getYMultiplierAt(int index, double val) {
        if (index < children.size()) {
            children.get(index).getYMultiplier(val);
        }
        return ymultiplier.get(val);
    }

    public PlotViewConfig getOrCreate(int index) {
        ensureChildrenSize(index + 1);
        return children.get(index);
    }

    public void ensureChildrenSize(int length) {
        while (children.size() < length) {
            children.add(new PlotViewConfig());
        }
        while (children.size() > length) {
            children.remove(children.size() - 1);
        }
    }

    public static PlotViewConfig copy(PlotViewConfig config) {
        if (config == null) {
            config = new PlotViewConfig();
        } else {
            config = config.copy();
        }
        return config;
    }

    public PlotViewConfig validate(int size) {
        PlotViewConfig config = this;

        if (config.clockwise == null) {
            //config.clockwise = true;
        }
        if (config.polarAngleOffset == null) {
            //config.polarAngleOffset = 0;
        }

        config.ensureChildrenSize(size);
        for (int i = 0; i < config.children.size(); i++) {
            PlotViewConfig lineConfig = config.children.get(i);
            lineConfig.xmultiplier.setIfNull(1.0);
            lineConfig.ymultiplier.setIfNull(1.0);
        }
        config.showLegend.setIfNull(true);
        config.maxLegendCount.setIfNull(Plot.Config.getMaxLegendCount());
        config.showTooltips.setIfNull(true);
        config.alternateColor.setIfNull(true);
        config.alternateNode.setIfNull(false);
        config.alternateLine.setIfNull(false);
        if (config.lineStepType == null) {
            config.lineStepType = PlotConfigLineStepType.DEFAULT;
        }
        config.threeD.setIfNull(false);
        config.nodeLabel.setIfNull(false);
        return config;
    }
}
