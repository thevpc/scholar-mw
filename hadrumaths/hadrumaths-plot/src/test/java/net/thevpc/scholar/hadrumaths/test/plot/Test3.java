package net.thevpc.scholar.hadrumaths.test.plot;

//package net.thevpc.scholar.math.test;
//
//import org.jzy3d.analysis.AbstractAnalysis;
//import org.jzy3d.analysis.AnalysisLauncher;
//import org.jzy3d.analysis.IAnalysis;
//import org.jzy3d.chart.Chart;
//import org.jzy3d.chart.ChartLauncher;
//import org.jzy3d.chart.Settings;
//import org.jzy3d.chart.controllers.mouse.camera.ICameraMouseController;
//import org.jzy3d.chart.factories.AWTChartComponentFactory;
//import org.jzy3d.chart.factories.SwingChartComponentFactory;
//import org.jzy3d.colors.Color;
//import org.jzy3d.colors.ColorMapper;
//import org.jzy3d.colors.colormaps.ColorMapRainbow;
//import org.jzy3d.maths.*;
//import org.jzy3d.maths.Rectangle;
//import org.jzy3d.plot3d.builder.Builder;
//import org.jzy3d.plot3d.builder.Mapper;
//import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
//import org.jzy3d.plot3d.primitives.Shape2D;
//import org.jzy3d.plot3d.rendering.canvas.Quality;
//
//import javax.swing.*;
//import java.awt.*;
//
///**
// * Created by vpc on 1/3/17.
// */
//public class Test3 extends AbstractAnalysis{
//    public static void main(String[] args) throws Exception {
////        AnalysisLauncher.open(new Test3());
//        Rectangle bounds = new Rectangle(0, 0, 600, 400);
//        Chart chart = new Test3().init(bounds);
//
////        Settings.getInstance().setHardwareAccelerated(true);
////        String title = "test";
////        ChartLauncher.openChart(chart, bounds, title);
////        ChartLauncher.frame(chart,bounds,title);
////
//        ICameraMouseController mouse = ChartLauncher.configureControllers(chart, "test", true, true);
//        chart.render();
//
//
//        JFrame f=new JFrame();
//        f.setTitle("HEllo");
//        f.getContentPane().add((Component) chart.getCanvas());
//        f.setVisible(true);
//    }
//
//    public void init(){
//        chart=init(new Rectangle(0, 0, 600, 400));
//    }
//
//    public Chart init(Rectangle bounds){
//        Settings.getInstance().setHardwareAccelerated(true);
//        // Define a function to plot
//        Mapper mapper = new Mapper() {
//            @Override
//            public double f(double x, double y) {
//                return 1E10*x * Math.sin(x * y);
//            }
//        };
//
//        // Define range and precision for the function to plot
//        Range range = new Range(-3, 3);
//        int steps = 80;
//
//        // Create the object to represent the function over the given range.
//        final Shape2D surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
//        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
//        surface.setFaceDisplayed(true);
//        surface.setWireframeDisplayed(false);
//
//        // Create a chart
//        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced, "awt");
//        chart.getScene().getGraph().add(surface);
//        return chart;
//
//    }
//
//}
