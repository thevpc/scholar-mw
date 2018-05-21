package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Domain;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DDiscrete;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:41:08
 */
public final class EchelonPattern extends RectMeshAttachGpPattern {
    private int gridx = 100;
    private int gridy = 100;
    private boolean xaxis = true;
    private boolean yaxis = true;
    private double value = Double.NaN;

    public EchelonPattern() {
        super(false, false);
    }

    public EchelonPattern(int gridx, int gridy, Double value) {
        super(false, false);
        this.gridx = gridx;
        this.gridy = gridy;
        this.value = value == null ? 1 : value.doubleValue();
    }

    public Dumper getDumper() {
        Dumper h = super.getDumper();
        h.add("gridx", gridx);
        h.add("gridy", gridy);
        return h;
    }

    public int getCount() {
        return 1;
    }

    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        switch (zone.getShape()) {
            case RECTANGLE: {
                DoubleToVector f = Maths.vector(
                        (
                                expr(zone.getDomain())
                        ),
                        (
                                expr(zone.getDomain())
                        )
                )
                        .setProperty("Type", "Echelon")
                        .setProperty("p", index).toDV();
//                f.setProperties(properties);
                return f;
            }
            default: {
                DoubleToVector f = Maths.vector(
                        DDiscrete.discretize(expr(1, zone.getPolygon()), gridx, gridy)
                        ,
                        DDiscrete.discretize(expr(1, zone.getPolygon()), gridx, gridy)
                )
                        .setProperty("Type", "Echelon")
                .setProperty("p", index).toDV();
//                f.setProperties(properties);
                return f;
            }
        }
    }

    public boolean isXaxis() {
        return xaxis;
    }

    public void setXaxis(boolean xaxis) {
        this.xaxis = xaxis;
    }

    public boolean isYaxis() {
        return yaxis;
    }

    public void setYaxis(boolean yaxis) {
        this.yaxis = yaxis;
    }
}
