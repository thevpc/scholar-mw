package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Plot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamplePlot {
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
