package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.CircuitType;

/**
 *
 */
public final class SinIntelliPattern extends AbstractGpPatternPQ {
//    public static enum Walls {
//        EMEM
//    }

    private int[] selectedFunctions;
    private boolean xinvariance;
    private boolean yinvariance;

    public SinIntelliPattern(int complexity, boolean xinvariance, boolean yinvariance) {
        super(complexity);
        this.xinvariance = xinvariance;
        this.yinvariance = yinvariance;
    }

    public SinIntelliPattern(int complexity) {
        this(complexity,new int[0]);
    }
    public SinIntelliPattern(int complexity,int[] selectedFunctions) {
        super(complexity);
        this.selectedFunctions=selectedFunctions;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("xinvariance", context.elem(xinvariance));
        h.add("yinvariance", context.elem(yinvariance));
        h.add("selectedFunctions", context.elem(selectedFunctions));
        return h.build();
    }

    @Override
    public DoubleToVector createFunction(int index, int p, int q, Domain d, Domain globalDomain, MomStructure str) {
        if(selectedFunctions!=null && selectedFunctions.length>0){
            boolean found=false;
            for (int selectedFunction : selectedFunctions) {
                if(selectedFunction==index){
                    found=true;
                    break;
                }
            }
            if(!found){
                return null;
            }
        }
        if (xinvariance && p > 0) {
            return null;
        }
        if (yinvariance && q > 0) {
            return null;
        }
        return UserSinePattern.createFunction(
                findCellBoundaries(Axis.X,d, globalDomain, str),
                findCellBoundaries(Axis.Y,d, globalDomain, str),
                index, p, q, d, globalDomain
        );
    }

    public CellBoundaries findCellBoundaries(Axis axis,Domain d, Domain globalDomain, MomStructure str) {
        boolean eastReached = Math.abs(d.xmin() - globalDomain.xmin()) < EPS;
        boolean westReached = Math.abs(d.xmax() - globalDomain.xmax()) < EPS;
        boolean northReached = Math.abs(d.ymin() - globalDomain.ymin()) < EPS;
        boolean southReached = Math.abs(d.ymax ()- globalDomain.ymax()) < EPS;
        CircuitType circuit = str.getCircuitType();
        ModeFunctions fn = str.modeFunctions();
        WallBorders b=str.getBorders();
        Boundary eastWall= eastReached ?b.getEast():CircuitType.SERIAL.equals(circuit)?Boundary.MAGNETIC:Boundary.ELECTRIC;
        Boundary westWall= westReached ?b.getWest():CircuitType.SERIAL.equals(circuit)?Boundary.MAGNETIC:Boundary.ELECTRIC;
        Boundary northWall= northReached ?b.getNorth():CircuitType.SERIAL.equals(circuit)?Boundary.MAGNETIC:Boundary.ELECTRIC;
        Boundary southWall= southReached ?b.getSouth():CircuitType.SERIAL.equals(circuit)?Boundary.MAGNETIC:Boundary.ELECTRIC;
        switch(axis){
            case X:{
                boolean eastMax= Boundary.ELECTRIC.equals(eastWall);
                boolean westMax= Boundary.ELECTRIC.equals(westWall);
                boolean northMax= Boundary.MAGNETIC.equals(northWall);
                boolean southMax= Boundary.MAGNETIC.equals(southWall);
                return CellBoundaries.eval(eastMax, westMax, northMax, southMax);
            }
            case Y:{
                boolean eastMax= Boundary.MAGNETIC.equals(eastWall);
                boolean westMax= Boundary.MAGNETIC.equals(westWall);
                boolean northMax= Boundary.ELECTRIC.equals(northWall);
                boolean southMax= Boundary.ELECTRIC.equals(southWall);
                return CellBoundaries.eval(eastMax, westMax, northMax, southMax);
            }
        }
//        switch (str.getCircuitType()) {
//            case SERIAL: {//modeliser le courant
//                if (fn instanceof FnElectricXY) {
//                    return CellBoundaries.process(east, west, !north, !south);
//                } else if (fn instanceof FnEMEMXY) {
//                    return CellBoundaries.process(false, false, !north, !south);
//                } else if (fn instanceof FnMagneticXY) {
//                    return CellBoundaries.process(false, false, true, true);
//                } else if (fn instanceof FnPeriodicXY) {
//                    return CellBoundaries.process(east, west, !north, !south);
//                }
//            }
//            case PARALLEL: {
//                if (fn instanceof FnElectricXY) {
//                    return CellBoundaries.process(!east, !west, north, south);
//                } else if (fn instanceof FnEMEMXY) {
//                    return CellBoundaries.process(true, true, north, south);
//                } else if (fn instanceof FnMagneticXY) {
//                    return CellBoundaries.process(true, true, false, false);
//                } else if (fn instanceof FnPeriodicXY) {
//                    return CellBoundaries.process(!east, !east, north, south);
//                }
//            }
//        }
        throw new IllegalArgumentException("Not supported Fn Type : " + fn.getClass().getName());

    }

    public static final double EPS = 1E-15;

}
