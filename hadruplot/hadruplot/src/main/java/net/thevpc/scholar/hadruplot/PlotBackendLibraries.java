package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Calc3dLibrary;
import net.thevpc.scholar.hadruplot.libraries.jfreechart.JFreechartLibrary;
import net.thevpc.scholar.hadruplot.libraries.jzy3d.Jzy3DLibrary;
import net.thevpc.scholar.hadruplot.libraries.simple.DefaultLibrary;

import java.util.*;

public class PlotBackendLibraries {
    private static List<PlotLibrary> all = new ArrayList<>();

    static {
        addLibrary(new Jzy3DLibrary());
        all.add(new JFreechartLibrary());
        all.add(new Calc3dLibrary());
        all.add(new DefaultLibrary());
    }

    public static PlotLibrary getLibrary(String name) {
        for (PlotLibrary plotLibrary : all) {
            if (name.equals(plotLibrary.getName())) {
                return plotLibrary;
            }
        }
        throw new NoSuchElementException("Not such plot library : " + name);
    }

    public static void removeLibrary(String name) {
        for (Iterator<PlotLibrary> iterator = all.iterator(); iterator.hasNext(); ) {
            PlotLibrary plotLibrary = iterator.next();
            if (plotLibrary.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }
    }

    public static void addLibrary(PlotLibrary a) {
        for (PlotLibrary plotLibrary : all) {
            if (plotLibrary.getName().equals(a.getName())) {
                throw new IllegalArgumentException("Already registered ");
            }
        }
        all.add(a);
    }

    public static boolean isSupported(PlotType plotType) {
        return isSupported(plotType, null);
    }

    public static boolean isSupported(PlotType plotType, PlotBackendLibraryFilter filter) {
        for (PlotLibrary library : getLibraries()) {
            if (filter == null || filter.accept(library)) {
                int s = library.getSupportLevel(plotType);
                if (s > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static PlotLibrary[] getLibraries() {
        return all.toArray(new PlotLibrary[0]);
    }

    public static PlotComponentPanel createPlotComponentPanel(PlotComponentContext context) {
        int bestSupportLevel = -1;
        PlotLibrary bestPlotLibrary = null;
        for (PlotLibrary library : getLibraries()) {
            LibraryPlotType pt = context.getPlotType();
            if(pt.getLibrary()==null  || library.getName().equalsIgnoreCase(pt.getLibrary())){
                int s = library.getSupportLevel(context.getPlotType().getType());
                if (s > 0 && s > bestSupportLevel) {
                    bestSupportLevel = s;
                    bestPlotLibrary = library;
                }
            }
        }
        if (bestPlotLibrary == null) {
            throw new IllegalArgumentException("Unable to create " + context.getPlotType() + " using libraries : " + Arrays.toString(getLibraries()));
        }
        PlotComponentPanel cc = bestPlotLibrary.createPlotComponentPanel(context);
        Plot.buildJPopupMenu(cc, context.getModelProvider());
        return cc;
    }
}
