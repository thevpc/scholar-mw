package net.thevpc.ntexup.extension.getdp;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.eval.NTxResolutionContext;

import java.util.ArrayList;
import java.util.List;

public class GetDPModelInfo {

    public static class GetDPBox {
        public double x1, y1, z1;
        public double x2, y2, z2;
        public String name;
        public String type;
        public boolean isPatch = false;

        public GetDPBox(double x1, double y1, double z1, double x2, double y2, double z2, String name, String type) {
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

    public double frequency = 2.4e9;
    public double epsilonR = 4.4;
    public double lossTangent = 0.02;
    public double z0Ref = 50.0;
    public String geometryId;
    public double meshResolution = 0.5; // mm
    public String dockerImage = "thevpc/getdp:3.2.0";
    public String mode = "auto"; // "auto", "2d", "3d"
    public NTxNode sceneNode;
    public NTxResolutionContext resolutionContext;

    public List<GetDPBox> groundBoxes = new ArrayList<>();
    public List<GetDPBox> substrateBoxes = new ArrayList<>();
    public List<GetDPBox> antennaBoxes = new ArrayList<>();
    public List<GetDPBox> sourceBoxes = new ArrayList<>();
}
