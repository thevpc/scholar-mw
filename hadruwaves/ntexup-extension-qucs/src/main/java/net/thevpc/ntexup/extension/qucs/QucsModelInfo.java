package net.thevpc.ntexup.extension.qucs;

public class QucsModelInfo {

    public static class QucsBox {
        public double x1, y1, z1;
        public double x2, y2, z2;
        public String name;
        public String type;

        public QucsBox(double x1, double y1, double z1, double x2, double y2, double z2, String name, String type) {
            this.x1 = Math.min(x1, x2);
            this.y1 = Math.min(y1, y2);
            this.z1 = Math.min(z1, z2);
            this.x2 = Math.max(x1, x2);
            this.y2 = Math.max(y1, y2);
            this.z2 = Math.max(z1, z2);
            this.name = name;
            this.type = type;
        }

        public double xMin() { return x1; }
        public double xMax() { return x2; }
        public double yMin() { return y1; }
        public double yMax() { return y2; }
        public double zMin() { return z1; }
        public double zMax() { return z2; }
        public double width() { return x2 - x1; }
        public double length() { return y2 - y1; }
        public double height() { return z2 - z1; }
    }

    public double frequency = 3.4e9;
    public double epsilonR = 4.4;
    public double lossTangent = 0.02;
    public double thickness = 0.035e-3;
    public double rho = 0.022e-6;
    public double roughness = 0.15e-6;
    public double z0Ref = 50.0;
    public String dispModel = "Kirschning";
    public String model = "Hammerstad";
    public String dockerImage = QucsProvisioner.DEFAULT_DOCKER_IMAGE;
    public String geometryId;

    public java.util.List<QucsBox> groundBoxes = new java.util.ArrayList<>();
    public java.util.List<QucsBox> substrateBoxes = new java.util.ArrayList<>();
    public java.util.List<QucsBox> antennaBoxes = new java.util.ArrayList<>();
    public java.util.List<QucsBox> sourceBoxes = new java.util.ArrayList<>();
}
