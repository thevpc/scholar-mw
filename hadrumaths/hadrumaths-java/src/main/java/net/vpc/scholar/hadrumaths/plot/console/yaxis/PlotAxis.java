package net.vpc.scholar.hadrumaths.plot.console.yaxis;

import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.plot.ComplexAsDouble;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ComputeTitle;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAction;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.WindowPath;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMessage;
import net.vpc.scholar.hadrumaths.util.StringProgressMessage;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public abstract class PlotAxis implements Serializable, Cloneable, ProgressMonitor {

    private WindowPath preferredPath;
    private YType[] types;
    private String name;
    private PlotType plotType;
    private Set<ExternalLibrary> preferredLibraries;
    private double multiplier = Double.NaN;
    private Map<YType, Double> multipliersMap = null;
    private double infiniteValue = Double.NaN;
    private double progress = 0;
    private ProgressMessage progressMessage = new StringProgressMessage(Level.FINE, "");
    private ComplexAsDouble complexAsDouble = ComplexAsDouble.ABS;

    protected PlotAxis(String name, YType... type) {
        this(name, type, PlotType.CURVE);
    }

    protected PlotAxis(String name, YType[] type, PlotType plotType) {
        this.types = (type == null || type.length == 0) ? YType.values() : type;
        this.name = name;
        this.plotType = plotType;
    }

    protected PlotAxis(String name, YType[] type, PlotType plotType,Set<ExternalLibrary> preferredLibraries) {
        this.types = (type == null || type.length == 0) ? YType.values() : type;
        this.name = name;
        this.plotType = plotType;
        this.preferredLibraries = preferredLibraries;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public double getMultiplier(YType type) {
        Double m = (multipliersMap != null && multipliersMap.containsKey(type)) ? multipliersMap.get(type) : multiplier;
        return m.isNaN() ? multiplier : m;
    }

    public PlotAxis setMultiplier(double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public Set<ExternalLibrary> getPreferredLibraries() {
        return preferredLibraries;
    }

    public PlotAxis setMultiplier(YType type, double multiplier) {
        if (Double.isNaN(multiplier)) {
            if (multipliersMap != null) {
                multipliersMap.remove(type);
            }
        } else {
            if (multipliersMap == null) {
                multipliersMap = new HashMap<YType, Double>(YType.values().length);
            }
            multipliersMap.put(type, multiplier);
        }
        return this;
    }

    public ComplexAsDouble getComplexAsDouble() {
        return complexAsDouble;
    }

    public void setComplexAsDouble(ComplexAsDouble complexAsDouble) {
        this.complexAsDouble = complexAsDouble == null ? ComplexAsDouble.ABS : complexAsDouble;
    }

    public PlotAxis updateComplexAsDouble(ComplexAsDouble complexAsDouble) {
        setComplexAsDouble(complexAsDouble);
        return this;
    }

    public void setPreferredLibraries(Set<ExternalLibrary> preferredLibraries) {
        this.preferredLibraries = preferredLibraries;
    }

    public PlotAxis updatePreferredLibraries(Set<ExternalLibrary> preferredLibraries) {
        this.preferredLibraries = preferredLibraries;
        return this;
    }

    public String getName() {
        return name == null ? getClass().getSimpleName() : name;
    }

    protected PlotAxis setName(String name) {
        this.name = name;
        return this;
    }


    public abstract Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p);

    public String getName(YType type) {
        String n = getName();
        switch (type) {
            case REFERENCE: {
                return "|" + getName() + "[dir]|";
            }
            case MODELED: {
                return "|" + getName() + "[mod]|";
            }
            case RELATIVE_ERROR: {
                return "|" + getName() + "[err_rel]|";
            }
            case ABSOLUTE_ERROR: {
                return "|" + getName() + "[err]|";
            }
        }
        return n;
    }


    public boolean containsType(YType type) {
        for (YType yType : types) {
            if (yType.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public YType[] getTypes() {
        return types;
    }

    public PlotType getPlotType() {
        return plotType;
    }

    public PlotAxis setPlotType(PlotType graphix) {
        this.plotType = graphix;
        return this;
    }

    public Component getComponent() {
        return null;
    }

    public double getInfiniteValue() {
        return infiniteValue;
    }

    public PlotAxis setInfiniteValue(double infiniteValue) {
        this.infiniteValue = infiniteValue;
        return this;
    }

    public double getProgressValue() {
        if (progress < 0 || progress > 1) {
            System.err.println("%= " + progress + "????????????");
        }
        return progress;
    }

    public void setProgress(double progress, ProgressMessage message) {
        if (progress < 0 || progress > 1) {
            System.err.println("%= " + progress + "????????????");
        }
        this.progress = progress;
        this.progressMessage = message;
    }

    //@Override
    public ProgressMessage getProgressMessage() {
        return progressMessage;
    }

    public String getName(ComputeTitle title) {
        String t = title == null ? "" : title.toString();
        if (t.length() > 0) {
            return getName() + " [" + t + "]";
        }
        return getName();
    }

    public PlotAxis clone() {
        try {
            return (PlotAxis) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error();
        }
    }

    public WindowPath getPreferredPath() {
        return preferredPath;
    }

    public void setPreferredPath(WindowPath preferredPath) {
        this.preferredPath = preferredPath;
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void stop() {

    }
}
