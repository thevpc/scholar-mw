package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.plot.mesh.Jzy3dMeshPlot;
import net.vpc.scholar.hadrumaths.plot.mesh.Mesh3DPlot;

import javax.swing.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by vpc on 1/3/17.
 */
public class ChartFactory {

    public static PlotComponentPanel createMesh(PlotModelProvider modelProvider, JColorPalette palette, Set<ExternalLibrary> preferredLibraries) {
        if (preferredLibraries == null) {
            preferredLibraries = EnumSet.allOf(ExternalLibrary.class);
        }
        try {
            if (preferredLibraries.contains(ExternalLibrary.CHARTS_JZY3D)) {
                return new Jzy3dMeshPlot(modelProvider, palette);
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
        return new Mesh3DPlot(modelProvider, palette);
    }

    public static PlotComponentPanel createMesh(ValuesPlotModel modelProvider, JColorPalette palette) {
        try {
            if (modelProvider.getPreferredLibraries().contains(ExternalLibrary.CHARTS_JZY3D)) {
                return new Jzy3dMeshPlot(modelProvider, palette);
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
        return new Mesh3DPlot(modelProvider, palette);
    }
}
