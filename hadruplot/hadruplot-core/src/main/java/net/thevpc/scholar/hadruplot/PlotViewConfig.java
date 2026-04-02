package net.thevpc.scholar.hadruplot;

import net.thevpc.nuts.util.NBooleanRef;
import net.thevpc.nuts.util.NDoubleRef;
import net.thevpc.nuts.util.NIntRef;
import net.thevpc.nuts.util.NUtils;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PlotViewConfig {
    public PlotConfigLineStepType lineStepType;
    public NBooleanRef showLegend = NBooleanRef.of();
    public NIntRef maxLegendCount = NIntRef.of();
    public NBooleanRef showTooltips = NBooleanRef.of();
    public NBooleanRef nodeLabel = NBooleanRef.of();
    public NBooleanRef threeD = NBooleanRef.of();
    public NBooleanRef alternateColor = NBooleanRef.of();
    public NBooleanRef alternateNode = NBooleanRef.of();
    public NBooleanRef alternateLine = NBooleanRef.of();
    public NBooleanRef clockwise = NBooleanRef.of();
    public NDoubleRef polarAngleOffset = NDoubleRef.of();

    public Color color;
    public NIntRef lineType = NIntRef.of();
    public NIntRef nodeType = NIntRef.of();
    public NBooleanRef shapesVisible = NBooleanRef.of();
    public NBooleanRef lineVisible = NBooleanRef.of();
    public NBooleanRef shapesFilled = NBooleanRef.of();
    public NDoubleRef xmultiplier = NDoubleRef.of();
    public NDoubleRef ymultiplier = NDoubleRef.of();
    public NDoubleRef defaultXMultiplier = NDoubleRef.of();

    public List<PlotViewConfig> children = new ArrayList<>();

    public PlotViewConfig copy() {
        PlotViewConfig other = new PlotViewConfig();
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getType().equals(NIntRef.class)) {
                NIntRef h = null;
                NIntRef m = null;
                try {
                    h = (NIntRef) field.get(other);
                    m = (NIntRef) field.get(this);
                    h.set(m.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (field.getType().equals(NBooleanRef.class)) {
                NBooleanRef h = null;
                NBooleanRef  m = null;
                try {
                    h = (NBooleanRef) field.get(other);
                    m = (NBooleanRef) field.get(this);
                    h.set(m.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (field.getType().equals(NDoubleRef.class)) {
                NDoubleRef h = null;
                NDoubleRef  m = null;
                try {
                    h = (NDoubleRef) field.get(other);
                    m = (NDoubleRef) field.get(this);
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
        return NUtils.firstNonNull(defaultXMultiplier.get(),val);
    }

    public double getXMultiplier(double val) {
        return NUtils.firstNonNull(xmultiplier.get(),val);
    }

    public double getYMultiplier(double val) {
        return NUtils.firstNonNull(ymultiplier.get(),val);
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
        return NUtils.firstNonNull(ymultiplier.get(),val);
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
