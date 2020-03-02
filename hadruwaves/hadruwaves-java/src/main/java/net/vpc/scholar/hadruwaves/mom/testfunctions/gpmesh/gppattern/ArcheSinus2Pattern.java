package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:42:02
 */
public final class ArcheSinus2Pattern extends RectMeshAttachGpPattern {

    public ArcheSinus2Pattern(boolean attachX, boolean attachY) {
        super(attachX, attachY);
    }

    public int getCount() {
        return 1;
    }

    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
//            type=Zone.Type.MAIN;
        switch (zone.getType().getValue()) {
            case MeshZoneType.ID_MAIN: {
                DoubleToVector f = Maths.vector(
                        FunctionFactory.archeSinus(Axis.X, 1, zone.getDomain())
                        ,
                        FunctionFactory.archeSinus(Axis.Y, 1, zone.getDomain())
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_ATTACHY: {
                DoubleToVector f = Maths.vector(
                        Maths.DZEROXY
                        ,
                        FunctionFactory.archeSinus(Axis.Y, 1, zone.getDomain())
                )
                .setProperty("Type", getClass().getSimpleName() + "-AttachY")
                        .setProperty("p", index).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_ATTACHX: {
                DoubleToVector f = Maths.vector(
                        FunctionFactory.archeSinus(Axis.X, 1, zone.getDomain())
                        ,
                        Maths.DZEROXY
                )
                        .setProperty("Type", getClass().getSimpleName() + "-AttachX")
                        .setProperty("p", index).toDV();
//                f.setProperties(properties);
                return f;
            }
        }
        return null;
    }
}
