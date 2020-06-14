package net.vpc.scholar.hadruplot.extension.defaults;

import net.vpc.scholar.hadruplot.model.value.PlotValueType;
import net.vpc.scholar.hadruplot.extension.PlotValueFactory;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;
import net.vpc.scholar.hadruplot.util.PlotUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JComponent;
import net.vpc.scholar.hadruplot.DefaultPlotValue;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.PlotBuilder;
import net.vpc.scholar.hadruplot.PlotLines;
import net.vpc.scholar.hadruplot.PlotValue;
import net.vpc.scholar.hadruplot.PlotValueAndPriority;

public class DefaultPlotValueFactory implements PlotValueFactory {

    public static final PlotValueFactory INSTANCE = new DefaultPlotValueFactory(0);
    private int priority;

    public DefaultPlotValueFactory(int priority) {
        this.priority = priority;
    }

    public PlotValueAndPriority createPlotValue(String type, Object value) {
        return createPlotValue(type, value, null);
    }

    public PlotValueAndPriority createPlotValue(String type, Object value, Map<String, String> props) {
        DefaultPlotValue t = new DefaultPlotValue(type, value);
        if (props != null) {
            for (Map.Entry<String, String> entry : props.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    t.set(entry.getKey(), entry.getValue());
                }
            }
        }
        return new PlotValueAndPriority(t,priority);
    }

    @Override
    public PlotValueAndPriority createPlotValue(Object obj, PlotBuilder builder) {
        if (obj == null) {
            return createPlotValue("null", obj);
        }
        if (obj instanceof JComponent) {
            return createPlotValue("component", obj);
        }
        obj = Plot.Config.convert(obj);
        PlotValueAndPriority o = createCustomPlotValue(obj, builder);
        if (o != null) {
            return o;
        }
        if (obj instanceof Number) {
            return createPlotValue("number", obj);
        }
        if (obj instanceof PlotLines) {
            return createPlotValue("PlotLines", obj);
        }
        if (PlotUtils.isComponentType(obj)) {
            return createArrayPlotValue(obj, builder);
        }
        if (obj instanceof File) {
            return createPlotValue("file", obj);
        }
        return null;//createPlotValue("object", obj); //FIX ME
    }

    /**
     * handle special types (other than number, file, object, arrays and
     * iterables return null if not supported
     *
     * @param obj
     * @param builder
     * @return
     */
    protected PlotValueAndPriority createCustomPlotValue(Object obj, PlotBuilder builder) {
        return null;
    }

    @Override
    public boolean isColumn(Object obj) {
        return false;
    }

    public PlotValueAndPriority createArrayPlotValue(Object obj, PlotBuilder builder) {
        List initial = new ArrayList();
        boolean column = PlotConfigManager.isColumn(obj);
        if (obj.getClass().isArray()) {
            int l = Array.getLength(obj);
            for (int i = 0; i < l; i++) {
                initial.add(Array.get(obj, i));
            }
        } else if (obj instanceof Iterable) {
            for (Object o : (Iterable) obj) {
                initial.add(o);
            }
        } else {
            return createPlotValue("object", obj);
        }
        PlotValueType t = PlotConfigManager.getPlotValueTypeFactory().getType("null");
        int l = initial.size();
        List<Object> all = new ArrayList<>();
        if (l == 0) {
            return createPlotValue("null[]", all);
        }
        for (Object o : initial) {
            PlotValue ii = PlotConfigManager.createPlotValue(o, builder);
            if (ii != null) {
                PlotValueType itm = ii.getType();
                all.add(ii.getValue());
                if (t == null) {
                    t = itm;
                } else {
                    t = PlotConfigManager.getPlotValueTypeFactory().resolveUmbrellaType(t, itm);
                }
            }
        }
        Map<String, String> props = new HashMap<>();
        if (column) {
            props.put("column", "true");
        }
        return createPlotValue(t.getName() + "[]", all,props);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultPlotValueFactory that = (DefaultPlotValueFactory) o;
        return priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority);
    }
}
