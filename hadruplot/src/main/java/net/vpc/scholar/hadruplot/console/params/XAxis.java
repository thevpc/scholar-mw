package net.vpc.scholar.hadruplot.console.params;

import java.io.Serializable;

public class XAxis implements Serializable {
    public double[] iteration;
    public double[] iterationZ;
    public XAxisType type;

    public XAxis(XAxisType type, double[] iteration) {
        this.type = type;
        this.iteration = iteration;
    }

    public XAxis(XAxisType type, double[] iteration, double[] iterationZ) {
        this.type = type;
        this.iteration = iteration;
        this.iterationZ = iterationZ;
    }
}
