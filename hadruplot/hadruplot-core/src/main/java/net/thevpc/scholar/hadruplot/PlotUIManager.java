package net.thevpc.scholar.hadruplot;

import net.thevpc.nuts.reflect.NReflectUtils;
import net.thevpc.scholar.hadruplot.extension.PlotBuilderSupport;

import java.util.*;

public class PlotUIManager {
    private static Map<String, PlotUI> loaded = new HashMap<>();
    private static PlotUI current;

    static {
        for (PlotUI lib : NReflectUtils.listServices(PlotUI.class,PlotUIManager.class)) {
            add(lib);
        }
        for (PlotUIFactory lib : NReflectUtils.listServices(PlotUIFactory.class,PlotUIManager.class)) {
            List<PlotUI> all = lib.findPlotUI();
            if (all != null) {
                for (PlotUI ui : all) {
                    add(ui);
                }
            }
        }
    }

    public static void add(PlotUI ui) {
        if (ui != null) {
            String newId = ui.getId();
            if (newId == null) {
                throw new IllegalArgumentException("invalid plot ui");
            }
            PlotUI old = loaded.get(newId);
            if (old != null) {
                throw new IllegalArgumentException("already added " + ui);
            }
            loaded.put(newId, ui);
            if (current == null) {
                for (String s : loaded.keySet()) {
                    set(s);
                    break;
                }
            }
        }
    }

    public static void set(String id) {
        PlotUI old = current;
        PlotUI newVal = loaded.get(id);
        if (newVal == null) {
            throw new IllegalArgumentException("invalid " + id);
        }
        if (old != null) {
            old.uninstall();
        }
        current = newVal;
        newVal.install();
    }

    public static PlotUI getOrDefault(String id) {
        PlotUI e = loaded.get(id);
        if (e == null) {
            return current;
        }
        return e;

    }

    public static PlotUI apply(String id) {
        PlotUI r = getOrDefault(id);
        if (r != null) {

        }
        return r;
    }

    public static PlotUI get(String id) {
        if (id == null) {
            return current;
        }
        return loaded.get(id);
    }
}
