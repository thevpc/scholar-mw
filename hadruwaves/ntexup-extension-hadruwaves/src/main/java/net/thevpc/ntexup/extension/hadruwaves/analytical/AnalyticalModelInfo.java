package net.thevpc.ntexup.extension.hadruwaves.analytical;

public class AnalyticalModelInfo {
    public double frequency = 3.4e9;
    public double epsilonR = 4.4;
    public double lossTangent = 0.02;
    public double width = 3.1e-3;
    public double length = 30e-3;
    public double height = 1.6e-3;
    public double stubLength = 30e-3; // Length from port reference plane to open end
    public double z0Ref = 50.0;
    public boolean dispersion = false;
    public String geometryId;

    public boolean isResonator = false;
    public double resW = 13.5e-3;
    public double resL = 9.9e-3;
    public double feedLength = 0.0;
    public double insetDepth = 0.0;
    public double feedWidth = 3.1e-3;
}
