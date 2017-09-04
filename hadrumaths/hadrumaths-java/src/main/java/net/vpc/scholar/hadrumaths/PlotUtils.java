package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.plot.FrameWindowManager;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.PlotWindowManager;
import net.vpc.scholar.hadrumaths.util.Converter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static net.vpc.scholar.hadrumaths.Maths.PI;
import static net.vpc.scholar.hadrumaths.Maths.cos;
import static net.vpc.scholar.hadrumaths.Maths.sin;

public class PlotUtils {
    public static void main(String[] args) {
//        JFrame frame = defaultWindowManager.getOrCreateFrame();
        Plot.title("A").cd("/").plot(new double[]{5,4,3,4,10});
        Plot.title("B").plot(new double[]{1,2,3,6,5});
        Plot.title("C").cd("New Window").plot(new double[]{1,2,3,6,5});
        Plot.title("D").cd("New Window").plot(new double[]{1,2,3,6,5});
        Plot.title("E").cd("New Window/Some").plot(new double[]{1,2,3,6,5});
        Plot.title("F").cd("New Window/Some").plot(new double[]{1,2,3,6,5});
        Plot.title("G").cd("/New Window/Some").plot(new double[]{1,2,3,6,5});
//        Plot.title("B").cd("/").plot(new double[]{1,2,3,6,5});
//        Plot.title("B").cd("/").plot(new double[]{1,2,3,6,5});
//        Plot.title("C").cd("/").plot(new double[]{1,2,3,6,5});
//        Plot.title("C").cd("0").plot(new double[]{1,2,3,6,5});
//        Plot.title("D").cd("0").plot(new double[]{1,2,3,6,5});
//        System.out.println(frame);
//        JRootPane rootPane = frame.getRootPane();
//        Container contentPane = rootPane.getContentPane();
//        Plot.cd("0").plot(new double[]{1,2,3,6,5});
    }
    public static void main0(String[] args) {
        double[] gte2017 = new double[]{513,
                514,
                581,
                601,
                680,
                683,
                740,
                743,
                754,
                781,
                803,
                863,
                950,
                957,
                60,
                97,
                164,
                224,
                237,
        };
        double[] ia2017 = new double[]{398,
                494,
                614,
                626,
                655,
                657,
                674,
                678,
                716,
                718,
                727,
                729,
                732,
                747,
                750,
                753,
                776,
                794,
                862,
                868,
                875,
                883,
                914,
                925,
                951,
                991,
                1005,
                1007,
                1055,
                132,
                163,
                198,
                214,
                216,
                234,
                247,
                250,
                254,
                269
        };
        double[] ia2016 = new double[]{128,
                160,
                178,
                182,
                311,
                325,
                363,
                373,
                376,
                382,
                401,
                409,
                418,
                427,
                436,
                438,
                444,
                451,
                466,
                491,
                500,
                551,
                566,
                588,
                623,
                630,
                666,
                667,
                673,
                689,
                699,
                703,
                712,
                735,
                743,
                763,
                772,
                775,
                777,
                802,
                804,
                812,
                835,
                836,
                850,
                859,
                888,
                892,
                923,
                928,
                936,
                970,
                978,
                982,
                995,
                995,
                1000,
                1053,
                1063,
                1065,
                1078,
                1083,
                1096,
                1109,
                1115,
                1120
        };
        Arrays.sort(ia2016);
        Arrays.sort(ia2017);
        Arrays.sort(gte2017);
        PlotLines lines=new PlotLines();
//        lines.addValues("IA2016",null,ia2016);
//        lines.addValues("IA2017",null,ia2017);
//        lines.addValues("GTE2017",null,gte2017);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                lines.addValue("RND"+j,i,Complex.valueOf(sin(i*PI/5),cos(j*PI/5)));
            }
        }
        Plot.asCurve().plot(lines);
//        if(true){
//            return;
//        }
        Plot.asCurve().title("values").plot(lines.transformStretchDomain().transformInterpolate(PlotLines.InterpolationStrategy.SMOOTH));
        for (int i = 1; i < 5; i++) {
            final int i100 = i * 100;
            Converter<PlotLines.PlotPoint, Double> group = r -> Math.floor(r.getY().getReal() / i100) * i100+i100;
            PlotType type = PlotType.AREA;
            Plot.asCurve().cd(String.valueOf(i)).title("avg "+ i100).plotType(type).plot(lines.avgBy         (group).transformStretchDomain().transformInterpolate(PlotLines.InterpolationStrategy.SMOOTH));
            Plot.asCurve().cd(String.valueOf(i)).title("count "+ i100).plotType(type).plot(lines.countBy       (group).transformStretchDomain().transformInterpolate(PlotLines.InterpolationStrategy.PREDECESSOR));
            Plot.asCurve().cd(String.valueOf(i)).title("count% "+ i100).plotType(type).plot(lines.countPercentBy(group).transformStretchDomain().transformInterpolate(PlotLines.InterpolationStrategy.PREDECESSOR));
            Plot.asCurve().cd(String.valueOf(i)).title("avg (c) "+ i100).plotType(type).plot(lines.avgBy         (group).accumulateLeft().transformStretchDomain().transformInterpolate(PlotLines.InterpolationStrategy.SMOOTH));
            Plot.asCurve().cd(String.valueOf(i)).title("count (c) "+ i100).plotType(type).plot(lines.countBy       (group).accumulateLeft().transformStretchDomain().transformInterpolate(PlotLines.InterpolationStrategy.PREDECESSOR));
            Plot.asCurve().cd(String.valueOf(i)).title("count% (c) "+ i100).plotType(type).plot(lines.countPercentBy(group).accumulateLeft().transformStretchDomain().transformInterpolate(PlotLines.InterpolationStrategy.PREDECESSOR));
        }
    }


}
