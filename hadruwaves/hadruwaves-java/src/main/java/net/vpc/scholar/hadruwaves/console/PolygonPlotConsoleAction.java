package net.vpc.scholar.hadruwaves.console;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.plot.console.*;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.PolygonPlot;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;

public class PolygonPlotConsoleAction implements ConsoleAction {
    public static final long serialVersionUID = -1231231231240000007L;
    private GeometryList geometryList;
    private String title;
    private WindowPath preferredPath;
    private MeshAlgo meshAlgo;
    private GpPattern pattern;
    private Domain domain;

    public PolygonPlotConsoleAction(String title, GeometryList geometryList, MeshAlgo meshAlgo, GpPattern pattern, Domain domain, WindowPath preferredPath) {
        this.geometryList = geometryList;
        this.title = title;
        this.meshAlgo = meshAlgo;
        this.domain = domain;
        this.pattern = pattern;
        this.preferredPath = new WindowPath(preferredPath, "Structure");
    }

    public void execute(PlotConsole plotter) {
        MainPlotterFrame frame = plotter.getMainPlotterFrame();
        ConsoleWindow window = frame.getWindow(preferredPath);
        window.addChild(title, new PolygonPlot(geometryList, meshAlgo,pattern,domain));
    }

}
