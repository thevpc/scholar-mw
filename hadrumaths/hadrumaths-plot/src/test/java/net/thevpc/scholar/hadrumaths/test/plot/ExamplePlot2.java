package net.thevpc.scholar.hadrumaths.test.plot;

import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.Plot;

import java.io.File;

public class ExamplePlot2 {
    public static void main(String[] args) {
        Plot.update("a").cd("/Toto/Machin").title("real").asReal().xformat(Maths.frequencyFormat()).plot(new File("/data/vpc/Downloads/zin.jfig"));
        Plot.cd("/Toto/Machin").title("imag").asImag().xformat(Maths.frequencyFormat()).plot(new File("/data/vpc/Downloads/zin.jfig"));
        Plot.update("a").cd("/Toto/Machin").title("image0").asImag().xformat(Maths.frequencyFormat()).plot(new File("/data/vpc/Downloads/zin.jfig"));
//        List<double[]> all=new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            all.add(new double[]{Math.random(),Math.random(),Math.random(),Math.random(),Math.random()});
//        }
//        Plot.xformat(d->"::"+d).asCurve().plot(all);

    }
}
