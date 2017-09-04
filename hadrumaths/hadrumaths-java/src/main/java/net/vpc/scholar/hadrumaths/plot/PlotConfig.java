package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Plot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlotConfig implements Cloneable{
    public PlotConfigLineStepType lineStepType;
    public Boolean showLegend;
    public Integer maxLegendCount;
    public Boolean showTooltips;
    public Boolean nodeLabel;
    public Boolean threeD;
    public Boolean alternateColor;
    public Boolean alternateNode;
    public Boolean alternateLine;
    public Boolean clockwise;
    public Number polarAngleOffset;

    public Color color;
    public Integer lineType;
    public Integer nodeType;
    public Boolean shapesVisible;
    public Boolean lineVisible;
    public Boolean shapesFilled;
    public Number xmultiplier;
    public Number ymultiplier;
    public Number defaultXMultiplier;

    public List<PlotConfig> children = new ArrayList<>();
    public PlotConfig copy(){
        try {
            PlotConfig other=(PlotConfig) clone();
            other.children=new ArrayList<>();
            for (PlotConfig child : children) {
                other.children.add(child.copy());
            }
            return other;
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Impossible");
        }
    }

    public double getDefaultXMultiplier(double val) {
        return defaultXMultiplier==null?val:defaultXMultiplier.doubleValue();
    }

    public double getXMultiplier(double val) {
        return xmultiplier==null?val:xmultiplier.doubleValue();
    }

    public double getYMultiplier(double val) {
        return ymultiplier==null?val:ymultiplier.doubleValue();
    }

    public double getXMultiplierAt(int index,double val) {
        if(index<children.size()){
            children.get(index).getXMultiplier(val);
        }
        return val;
    }

    public double getYMultiplierAt(int index,double val) {
        if(index<children.size()){
            children.get(index).getYMultiplier(val);
        }
        return ymultiplier==null?val:ymultiplier.doubleValue();
    }

    public PlotConfig getOrCreate(int index) {
        ensureChildrenSize(index+1);
        return children.get(index);
    }

    public void ensureChildrenSize(int length) {
       while (children.size()<length){
           children.add(new PlotConfig());
       }
       while (children.size()>length){
           children.remove(children.size()-1);
       }
    }

    public static PlotConfig copy(PlotConfig config){
        if(config==null){
            config=new PlotConfig();
        }else{
            config=config.copy();
        }
        return config;
    }

    public PlotConfig validate(int size){
        PlotConfig config=this;

        if (config.clockwise == null) {
            config.clockwise = true;
        }
        if (config.polarAngleOffset == null) {
            config.polarAngleOffset = 0;
        }

        config.ensureChildrenSize(size);
        for (int i = 0; i < config.children.size(); i++) {
            PlotConfig lineConfig = config.children.get(i);
            if (lineConfig.xmultiplier == null) {
                lineConfig.xmultiplier = 1.0;
            }
            if (lineConfig.ymultiplier == null) {
                lineConfig.ymultiplier = 1.0;
            }
        }
        if (config.showLegend == null) {
            config.showLegend = true;
        }
        if (config.maxLegendCount == null) {
            config.maxLegendCount = Plot.Config.getMaxLegendCount();
        }

        if (config.maxLegendCount < 0) {
            config.maxLegendCount = Plot.Config.getMaxLegendCount();
        }
        if (config.showTooltips == null) {
            config.showTooltips = true;
        }

        if (config.alternateColor == null) {
            config.alternateColor = true;
        }
        if (config.alternateNode == null) {
            config.alternateNode = true;
        }
        if (config.alternateLine == null) {
            config.alternateLine = true;
        }

        if (config.lineStepType == null) {
            config.lineStepType = PlotConfigLineStepType.DEFAULT;
        }
        if (config.threeD == null) {
            config.threeD = false;
        }
        if (config.nodeLabel == null) {
            config.nodeLabel = false;
        }
        return config;
    }
}
