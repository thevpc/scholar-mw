package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.backends.jfreechart.JFreechartLibrary;
import net.vpc.scholar.hadruplot.backends.jzy3d.Jzy3DLibrary;
import net.vpc.scholar.hadruplot.backends.simple.DefaultLibrary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PlotBackendLibraries {
    private static  List<PlotLibrary> all=new ArrayList<>();
    static {
        addLibrary(new Jzy3DLibrary());
        all.add(new JFreechartLibrary());
        all.add(new DefaultLibrary());
    }

    public static PlotLibrary getLibrary(String name) {
        for (PlotLibrary plotLibrary : all) {
            if(name.equals(plotLibrary.getName())){
                return plotLibrary;
            }
        }
        throw new NoSuchElementException("Not such plot library : "+name);
    }

    public static void removeLibrary(String name){
        for (Iterator<PlotLibrary> iterator = all.iterator(); iterator.hasNext(); ) {
            PlotLibrary plotLibrary = iterator.next();
            if (plotLibrary.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }
    }

    public static void addLibrary(PlotLibrary a){
        for (PlotLibrary plotLibrary : all) {
            if(plotLibrary.getName().equals(a.getName())){
                throw new IllegalArgumentException("Already registered ");
            }
        }
        all.add(a);
    }

    public static PlotLibrary[] getLibraries() {
        return all.toArray(new PlotLibrary[0]);
    }

    public static boolean isSupported(PlotType plotType){
        return isSupported(plotType,null);
    }

    public static boolean isSupported(PlotType plotType,PlotBackendLibraryFilter filter){
        for (PlotLibrary library : getLibraries()) {
            if(filter==null || filter.accept(library)) {
                int s = library.getSupportLevel(plotType);
                if (s > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static PlotComponentPanel createPlotComponentPanel(PlotComponentContext context){
        int bestSupportLevel=-1;
        PlotLibrary bestPlotLibrary =null;
        for (PlotLibrary library : getLibraries()) {
            String libraries = context.getModelProvider().getModel().getLibraries();
            PlotBackendLibraryFilter filter = new DefaultPlotBackendLibraryFilter(libraries);
            if(filter.accept(library)) {
                int s = library.getSupportLevel(context.getPlotType());
                if (s > 0 && s > bestSupportLevel) {
                    bestSupportLevel = s;
                    bestPlotLibrary = library;
                }
            }
        }
        if(bestPlotLibrary ==null){
            throw new IllegalArgumentException("Unable to create "+context.getPlotType());
        }
        PlotComponentPanel cc = bestPlotLibrary.createPlotComponentPanel(context);
        Plot.buildJPopupMenu(cc,context.getModelProvider());
        return cc;
    }
}
