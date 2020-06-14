package net.vpc.scholar.hadruwaves.examples;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.Samples;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.Physics;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsBuilder;

import static net.vpc.scholar.hadrumaths.Domain.ofWidth;
import static net.vpc.scholar.hadrumaths.Maths.*;
import net.vpc.scholar.hadrumaths.cache.CacheMode;
import static net.vpc.scholar.hadruwaves.mom.BoxSpace.matchedLoad;
import static net.vpc.scholar.hadruwaves.mom.BoxSpace.shortCircuit;
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
        Domain box = Domain.ofWidth(0, a, -b / 2, b);
        double f = 4.79 * GHZ;
        int modes = 1000000;
        Material substrateEpsr = Material.substrate("substrate",2.2);
        double superstrateEpsr = 1;
        Domain lineBox = Domain.ofWidth(0, l, -d / 2, d);
        Domain patchBox = Domain.ofWidth(l, L, -W / 2, W);
        Domain attachBox = Domain.ofWidth(l - att / 2, att, -d / 2, d);
        MomStructure str = MomStructure.EEEE(box, f, modes, shortCircuit(substrateEpsr, 1.59 * MM),
                 matchedLoad(Material.VACUUM));
        str.setSources(createPlanarSource(1, Complex.of(50), Axis.X, Domain.ofWidth(0, s, -d / 2, d)));
        str.setTestFunctions(new TestFunctionsBuilder()
                .addGeometry(lineBox).setComplexity(6).applyBoxModes() // line test functions
                .addGeometry(patchBox).setComplexity(20).applyBoxModes() //patch test functions
                .addGeometry(attachBox).setComplexity(1).applyBoxModes() //attach test function
                .build()
        );
        str.current().monitor(ProgressMonitors.temporize(ProgressMonitors.logger(), 1000)).evalMatrix(Axis.X, Samples.relative(100));
//        Plot.console().createPlot().setStructure(str)
//                .setX(xyParamSet(100, 50)) // precision (100 samples on X axis, ...)
//                .addY(testFunctions()) //plot test functions
//                .addY(current3D().setPlotType(SURFACE)) // plot current density
//                .addY(electricField3D()) // plot surface electric field
//                .plot();
    }
}
