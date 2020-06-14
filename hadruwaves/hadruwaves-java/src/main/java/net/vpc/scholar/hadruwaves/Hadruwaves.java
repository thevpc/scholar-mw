package net.vpc.scholar.hadruwaves;

import net.vpc.hadralang.stdlib.JExports;

@JExports({
        //import static methods and exports
        "net.vpc.scholar.hadrumaths.Hadrumaths.**",
        "net.vpc.scholar.hadrumaths.plot.HadrumathsPlot.**",
        //import static methods and exports
        "net.vpc.scholar.hadruwaves.Physics.**",
        //import all classes under hadruwaves. No Name conflict should occur!
        "net.vpc.scholar.hadruwaves.**",
})
public class Hadruwaves {
}
