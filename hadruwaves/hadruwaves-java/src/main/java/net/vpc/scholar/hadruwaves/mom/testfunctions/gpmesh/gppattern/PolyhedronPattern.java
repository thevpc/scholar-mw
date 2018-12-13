package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.Polyhedron;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:41:08
 */
public final class PolyhedronPattern extends AbstractGpPattern {
    boolean x;
    boolean y;
    private int gridx = 100;
    private int gridy = 100;

    public PolyhedronPattern(boolean x, boolean y, int gridx, int gridy) {
        this.x = x;
        this.y = y;
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public PolyhedronPattern(boolean x, boolean y) {
        this(x,y,100,100);
    }

    public PolyhedronPattern(boolean x, boolean y,int grid) {
        this(x,y,grid,grid);
    }

    public Dumper getDumper() {
        Dumper h = super.getDumper();
        h.add("x",x);
        h.add("y",y);
        h.add("gridx",gridx);
        h.add("gridy",gridy);
        return h;
    }
    public int getCount() {
        return 1;
    }

    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        switch (zone.getShape()) {
            default: {
                DoubleToVector f = Maths.vector(
                        (
                                x ? DDiscrete.discretize(new Polyhedron(1, zone.getPolygon()), gridx, gridy) : FunctionFactory.DZEROXY
                        ),
                        (
                                y ? DDiscrete.discretize(new Polyhedron(1, zone.getPolygon()), gridx, gridy) : FunctionFactory.DZEROXY
                        )
                )
                        .setProperty("Type", (x & y) ? "Polyedre" : x ? "PolyedreX" : y ? "PolyedreY" : "0")
                        .setProperty("p", index).toDV();
//                f.setProperties(properties);
                return f;
            }
        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    public boolean isAttachX() {
        return false;
    }

    public boolean isAttachY() {
        return false;
    }
}