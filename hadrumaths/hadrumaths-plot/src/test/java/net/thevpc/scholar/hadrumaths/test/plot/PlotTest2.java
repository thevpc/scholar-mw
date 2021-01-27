package net.thevpc.scholar.hadrumaths.test.plot;

import net.thevpc.scholar.hadruplot.PlotBuilder;
import net.thevpc.scholar.hadruplot.console.PlotConsole;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.thevpc.scholar.hadrumaths.Maths.GHZ;

public class PlotTest2 {
    public static void main(String[] args) {
        try {
            PlotConsole c = new PlotConsole();
            double[] doubles = {1 * GHZ, 15 * GHZ, 13 * GHZ, 3 * GHZ, 4 * GHZ, 1 * GHZ};
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
            PlotBuilder plotter = c.newPlot().zformat("frequency");
//        PlotBuilder newPlot = Plot.builder();//c.newPlot();
            plotter.asHeatMap().title("P1").cd("/A").xsamples(dates).plot(doubles);
//            newPlot.title("P2").cd("/A/B").plot(doubles);
//            newPlot.title("P3").cd("/A/B/C").plot(doubles);
//            newPlot.title("P4").cd("/A/B/C").plot(doubles);
//        c.newPlot().update("Toto").plot(new double[]{1,15,13,3,4,1,9});
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
