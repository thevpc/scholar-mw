package net.thevpc.ntexup.extension.scuffem;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.eval.NTxResolutionContext;

import java.util.ArrayList;
import java.util.List;

public class ScuffEMModelInfo {

    public static class ScuffEMBox {
        public double x1, y1, z1;
        public double x2, y2, z2;
        public String name;
        public String type;
        public boolean isPatch = false;

        public ScuffEMBox(double x1, double y1, double z1, double x2, double y2, double z2, String name, String type) {
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
    public double meshResolution = 2.5; // mm
    /** Triangle size along the conductor edges in mm, where the current is singular; 0 = same as meshResolution. */
    public double meshEdge = 0;
    /**
     * Triangles across the narrowest part of the outline: each outline point takes its local
     * feature size (the distance to the nearest edge not ending on it) divided by this. 0 = the
     * default of 2, which gives a narrow strip two triangles across.
     */
    public double meshAcross = 0;
    /** mm. Smallest outline point size, applied after meshAcross and meshEdge; 0 = no floor. */
    public double meshMin = 0;
    /** Sampling of the outline in gmsh's Distance field when meshEdge is set; 0 = 400. */
    public int meshEdgeSamples = 0;
    /**
     * With meshEdge set, the size grows from meshEdge at the outline to meshResolution over
     * meshEdge + grade (meshResolution - meshEdge) mm; 0 = the default grade of 4.
     */
    public double meshGrade = 0;
    /** mm, de-embedded port: the gap between the extended feed and the stub; 0 = 0.3 mm. */
    public double portGap = 0;
    /** mm, de-embedded port: the stub beyond the gap; 0 = 3 mm. */
    public double portStub = 0;
    /** Nodes on the stub's last side (works around scuff's GetEdgeByIndex bound check); 0 = 5. */
    public int portStubNodes = 0;
    /** De-embedded port: the fraction of the extension the current fit uses, from the reference end; 0 = 0.8. */
    public double portWindow = 0;
    /** mm, de-embedded port: the current bin height; 0 = 1 mm, or less to keep 12 bins over the window. */
    public double portBin = 0;
    /**
     * mm. When positive, the feed is extended by this length behind the port, the port moves to
     * the new end, and S11 is read from the standing wave of the strip current on the extension,
     * referred back to the original port plane. Default 0 keeps the edge-port reading.
     */
    public double portDeembed = 0.0;
    /**
     * How many frequencies SCUFF-EM actually solves; the rest of the sweep is interpolated.
     * 0 solves every requested frequency. Default 10 anchors (plus the solve frequency) keeps
     * existing decks unchanged, but cannot resolve a null narrower than the anchor spacing.
     */
    public int solvePoints = 10;
    public String dockerImage = "thevpc/scuff-em:latest";
    public String mode = "auto";
    public NTxNode sceneNode;
    public NTxResolutionContext resolutionContext;

    public List<ScuffEMBox> groundBoxes = new ArrayList<>();
    public List<ScuffEMBox> substrateBoxes = new ArrayList<>();
    public List<ScuffEMBox> antennaBoxes = new ArrayList<>();
    public List<ScuffEMBox> sourceBoxes = new ArrayList<>();
}
