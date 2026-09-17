package net.thevpc.ntexup.extension.getdp;

public class GetDPModelInfo {
    public double frequency = 3.4e9;
    public double epsilonR = 4.4;
    public double lossTangent = 0.02;
    public double width = 3.1e-3;
    public double length = 30e-3;
    public double height = 1.6e-3;
    public double stubLength = 30e-3;
    public double z0Ref = 50.0;
    public String geometryId;
    public double meshResolution = 0.5; // mm
    public String dockerImage = "thevpc/getdp:3.2.0";
}
