package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.console.PlotConfigManager;
import net.vpc.scholar.hadruplot.util.PlotUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DefaultPlotValueFactory implements PlotValueFactory {
    public static final PlotValueFactory INSTANCE = new DefaultPlotValueFactory(0);
    private int priority;

    public DefaultPlotValueFactory(int priority) {
        this.priority = priority;
    }

    public PlotValue createPlotValue(String type, Object value) {
        return new DefaultPlotValue(priority,PlotConfigManager.getPlotValueTypeFactory().getType(type), value);
    }

    @Override
    public PlotValue createPlotValue(Object obj, PlotBuilder builder) {
        if (obj == null) {
            return createPlotValue("null", obj);
        }
        obj = Plot.Config.convert(obj);
        PlotValue o = createCustomPlotValue(obj,builder);
        if(o!=null){
            return o;
        }
        if (obj instanceof Number) {
            return createPlotValue("number", obj);
        }
        if (obj instanceof PlotLines) {
            return createPlotValue("PlotLines", obj);
        }
        if (PlotUtils.isComponentType(obj)) {
            return createArrayPlotValue(obj,builder);
        }
        if (obj instanceof File) {
            return createPlotValue("file", obj);
        }
        return  null;//createPlotValue("object", obj); //FIX ME
    }


    /**
     * handle special types (other than number, file, object, arrays and iterables
     * return null if not supported
     * @param obj
     * @return
     */
    protected PlotValue createCustomPlotValue(Object obj, PlotBuilder builder) {
        return null;
    }

    public boolean isColumn(Object obj) {
        return false;
    }

    public PlotValue createArrayPlotValue(Object obj, PlotBuilder builder) {
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
            PlotValueType itm = ii.getType();
            all.add(ii.getValue());
            if (t == null) {
                t = itm;
            } else {
                t = PlotConfigManager.getPlotValueTypeFactory().resolveUmbrellaType(t, itm);
            }
        }
        t = PlotConfigManager.getPlotValueTypeFactory().getType(t.getName() + "[]");
        PlotValue t1 = new DefaultPlotValue(priority,t, all);
        if (column) {
            t1.set("column", "true");
        }
        return compress(t1);
    }

    protected PlotValue compress(PlotValue t) {
        return t;
    }


}
