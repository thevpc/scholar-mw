package net.thevpc.scholar.hadruplot.console.yaxis;

import net.thevpc.common.mon.*;
import net.thevpc.common.msg.Message;
import net.thevpc.scholar.hadruplot.PlotDoubleConverter;
import net.thevpc.scholar.hadruplot.console.ComputeTitle;
import net.thevpc.scholar.hadruplot.console.ConsoleAction;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.common.swing.win.WindowPath;
import net.thevpc.scholar.hadruplot.PlotType;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class PlotAxis extends AbstractProgressMonitor implements Serializable, Cloneable{

    private WindowPath preferredPath;
    private YType[] types;
    private PlotType plotType;
    private String libraries;
    private double multiplier = Double.NaN;
    private Map<YType, Double> multipliersMap = null;
    private double infiniteValue = Double.NaN;
    private double progress = Double.NaN;
    private Message message=EMPTY_MESSAGE;
    private PlotDoubleConverter toDoubleConverter = PlotDoubleConverter.ABS;
//    private ProgressMonitorHelper h=new ProgressMonitorHelper();

    protected PlotAxis(String name, YType... type) {
        this(name, type, PlotType.CURVE);
    }

    protected PlotAxis(String name, YType[] type, PlotType plotType) {
        super(nextId());
        this.types = (type == null || type.length == 0) ? YType.values() : type;
        setName(name);
        this.plotType = plotType;
    }

    protected PlotAxis(String name, YType[] type, PlotType plotType, String libraries) {
        super(nextId());
        this.types = (type == null || type.length == 0) ? YType.values() : type;
        setName(name);
        this.plotType = plotType;
        this.libraries = libraries;
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

    public PlotDoubleConverter getDoubleConverter() {
        return toDoubleConverter;
    }

    public void setDoubleConverter(PlotDoubleConverter toDoubleConverter) {
        this.toDoubleConverter = toDoubleConverter == null ? PlotDoubleConverter.ABS : toDoubleConverter;
    }

    public PlotAxis updateDoubleConverter(PlotDoubleConverter toDoubleConverter) {
        setDoubleConverter(toDoubleConverter);
        return this;
    }

    public String getLibraries() {
        return libraries;
    }

    public PlotAxis setLibraries(String libraries) {
        this.libraries = libraries;
        return this;
    }

    public String getName() {
        String name=super.getName();
        return name == null ? getClass().getSimpleName() : name;
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
    protected void setProgressImpl(double progress) {
        this.progress=progress;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    protected void setMessageImpl(Message message) {
        this.message=message;
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
