package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Polyhedron;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

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

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("x", context.elem(x));
        h.add("y", context.elem(y));
        h.add("gridx", context.elem(gridx));
        h.add("gridy", context.elem(gridy));
        return h.build();
    }

    public int getCount() {
        return 1;
    }

    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        switch (zone.getShape()) {
            default: {
                DoubleToVector f = Maths.vector(
                        (
                                x ? DDiscrete.of(new Polyhedron(1, zone.getPolygon()), gridx, gridy) : Maths.DZEROXY
                        ),
                        (
                                y ? DDiscrete.of(new Polyhedron(1, zone.getPolygon()), gridx, gridy) : Maths.DZEROXY
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