package net.thevpc.scholar.hadruwaves.console;

import net.thevpc.common.swing.win.WindowPath;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruplot.console.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.PolygonPlot;
import net.thevpc.scholar.hadrumaths.geom.GeometryList;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgo;

public class PolygonPlotConsoleAction implements ConsoleAction {
    private static final long serialVersionUID = 1L;
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
        PlotConsoleFrame frame = plotter.getPlotConsoleFrame();
        ConsoleWindow window = frame.getWindow(preferredPath);
        window.addChild(title, new PolygonPlot(geometryList, meshAlgo,pattern,domain));
    }

}
