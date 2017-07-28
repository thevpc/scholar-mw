package net.vpc.scholar.hadruwaves.examples;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadruwaves.Physics;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsBuilder;

import static net.vpc.scholar.hadrumaths.Domain.forWidth;
import static net.vpc.scholar.hadrumaths.Maths.*;
import net.vpc.scholar.hadrumaths.cache.CacheMode;
import static net.vpc.scholar.hadruwaves.mom.BoxSpaceFactory.matchedLoad;
import static net.vpc.scholar.hadruwaves.mom.BoxSpaceFactory.shortCircuit;
import static net.vpc.scholar.hadruwaves.mom.SourceFactory.createPlanarSource;

/**
 * Created by vpc on 4/7/17.
 */
public class TmwlibExampleTest {

    public static void main(String[] args) {

        Maths.Config.setCacheExpressionPropertiesEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.ENABLED);
        Maths.Config.setCacheEnabled(true); // cache enabled
        Physics.Config.setModeIndexCacheEnabled(false);

        double a = 100.567 * MM;
        double b = 30 * MM;
        double s = 0.786 * MM;
        double d = 2.812 * MM;
        double l = 5.69 * MM;
        double L = 22.760 * MM;
        double W = 5.989 * MM;
        double att = 2 * l / 1.2;
        Domain box = forWidth(0, a, -b / 2, b);
        double f = 4.79 * GHZ;
        int modes = 1000000;
        double substrateEpsr = 2.2;
        double superstrateEpsr = 1;
        Domain lineBox = forWidth(0, l, -d / 2, d);
        Domain patchBox = forWidth(l, L, -W / 2, W);
        Domain attachBox = forWidth(l - att / 2, att, -d / 2, d);
        MomStructure str = MomStructure.EEEE(box, f, modes, shortCircuit(substrateEpsr, 1.59 * MM),
                 matchedLoad(superstrateEpsr));
        str.setSources(createPlanarSource(1, Complex.valueOf(50), Axis.X, forWidth(0, s, -d / 2, d)));
        str.setTestFunctions(new TestFunctionsBuilder()
                .addGeometry(lineBox).setComplexity(6).applyBoxModes() // line test functions
                .addGeometry(patchBox).setComplexity(20).applyBoxModes() //patch test functions
                .addGeometry(attachBox).setComplexity(1).applyBoxModes() //attach test function
                .build()
        );
        str.current().monitor(ComputationMonitorFactory.temporize(ComputationMonitorFactory.logger(), 1000)).computeMatrix(Axis.X, Samples.relative(100));
//        Plot.console().createPlot().setStructure(str)
//                .setX(xyParamSet(100, 50)) // precision (100 samples on X axis, ...)
//                .addY(testFunctions()) //plot test functions
//                .addY(current3D().setPlotType(SURFACE)) // plot current density
//                .addY(electricField3D()) // plot surface electric field
//                .plot();
    }
}
