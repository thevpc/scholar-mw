package net.thevpc.ntexup.extension.palace;

import java.util.ArrayList;
import java.util.List;

/**
 * The problem Palace is given, read from the ntx: geometry boxes in mm (scene3d), materials, the
 * wave port and the mesh. Palace solves it; the extension only writes its inputs and reads S11.
 */
public class PalaceModelInfo {

    public static class PalaceBox {
        public double x1, y1, z1;
        public double x2, y2, z2;
        public String name;

        public PalaceBox(double x1, double y1, double z1, double x2, double y2, double z2, String name) {
            this.x1 = Math.min(x1, x2);
            this.y1 = Math.min(y1, y2);
            this.z1 = Math.min(z1, z2);
            this.x2 = Math.max(x1, x2);
            this.y2 = Math.max(y1, y2);
            this.z2 = Math.max(z1, z2);
            this.name = name;
        }

        @Override
        public String toString() {
            return name + "[" + x1 + "," + y1 + "," + z1 + "," + x2 + "," + y2 + "," + z2 + "]";
        }
    }

    public double frequency = 2.4e9;
    public double epsilonR = 4.4;
    public double lossTangent = 0.02;
    public String geometryId;
    /** ground box tagged @(infinite); Palace's wave port needs it, see PalaceParser */
    public boolean infiniteGround = false;

    /** mm: metal-to-domain clearance on every side but the port side */
    public double padding = 33.0;
    /** mm: air above the metal */
    public double airHeight = 15.0;
    /** mm: feed line between the port face and the reference plane (the source box's outer edge) */
    public double portExtension = 20.0;
    /** wave port mode (1: the quasi-TEM microstrip mode) */
    public int portMode = 1;

    /** mm: gmsh target edge lengths on the metal, in the substrate, in the air */
    public double meshMetal = 0.4;
    public double meshSubstrate = 1.5;
    public double meshAir = 3.5;
    /** FE order */
    public int order = 2;
    /** Palace's adaptive fast frequency sweep tolerance */
    public double adaptiveTol = 1e-3;
    public int processes = 16;
    /** Palace's Solver.Linear.Type; "Default" lets Palace choose (SuperLU on the patch decks). */
    public String linearSolver = "Default";

    public List<PalaceBox> groundBoxes = new ArrayList<>();
    public List<PalaceBox> substrateBoxes = new ArrayList<>();
    public List<PalaceBox> antennaBoxes = new ArrayList<>();
    public List<PalaceBox> sourceBoxes = new ArrayList<>();
}
