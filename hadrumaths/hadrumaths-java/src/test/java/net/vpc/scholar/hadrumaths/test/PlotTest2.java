package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.DoubleFormatter;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.plot.PlotBuilder;
import net.vpc.scholar.hadrumaths.plot.console.PlotConsole;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlotTest2 {
    public static void main(String[] args) {
        try {
            PlotConsole c = new PlotConsole();
            double[] doubles = {1, 15, 13, 3, 4, 1};
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date[] dates = new Date[]{
                    f.parse("2017-12-01"),
                    f.parse("2017-12-02"),
                    f.parse("2017-12-03"),
                    f.parse("2017-12-04"),
                    f.parse("2017-12-05"),
                    f.parse("2017-12-06"),
                    f.parse("2017-12-07"),
            };
            PlotBuilder plotter = c.plotter();
//        PlotBuilder plotter = Plot.builder();//c.plotter();
            plotter.title("P1").cd("/A").xsamples(dates).plot(doubles);
            plotter.title("P2").cd("/A/B").plot(doubles);
            plotter.title("P3").cd("/A/B/C").plot(doubles);
            plotter.title("P4").cd("/A/B/C").plot(doubles);
//        c.plotter().update("Toto").plot(new double[]{1,15,13,3,4,1,9});
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
