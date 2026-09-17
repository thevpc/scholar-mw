package net.thevpc.ntexup.extension.qucs;

public class QucsModelInfo {
    public double frequency = 3.4e9;
    public double epsilonR = 4.4;
    public double lossTangent = 0.02;
    public double width = 3.1e-3;
    public double length = 30e-3;
    public double stubLength = 30e-3;
    public double height = 1.6e-3;
    public double thickness = 0.035e-3;
    public double rho = 0.022e-6;
    public double roughness = 0.15e-6;
    public double z0Ref = 50.0;
    public String dispModel = "Kirschning";
    public String model = "Hammerstad";
    public String dockerImage = QucsProvisioner.DEFAULT_DOCKER_IMAGE;
    public String geometryId;
}
