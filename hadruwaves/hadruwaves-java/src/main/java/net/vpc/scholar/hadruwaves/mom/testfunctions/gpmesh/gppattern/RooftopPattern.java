package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneTypeFilter;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:41:33
 */
public final class RooftopPattern extends RectMeshAttachGpPattern {

    public RooftopPattern(MeshZoneTypeFilter filter) {
        super(filter);
    }


    public RooftopPattern() {
        this(true, true);
    }

    public RooftopPattern(boolean attachX, boolean attachY) {
        super(attachX, attachY);
    }


    @Override
    public int getCount() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
//            type=Zone.Type.MAIN;
        switch (zone.getType().getValue()) {
            case MeshZoneType.ID_MAIN: {
                DoubleToVector f = Maths.vector(
                        FunctionFactory.rooftop(Axis.X, 1, false, zone.getDomain())
                        ,
                        FunctionFactory.rooftop(Axis.Y, 1, false, zone.getDomain())
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperties(zone.getProperties()).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_ATTACHX: {
                DoubleToVector f = Maths.vector(
                        FunctionFactory.rooftop(Axis.X, 1, false, zone.getDomain())
                        ,
                        FunctionFactory.DZEROXY// FunctionFactory.rooftop(Axis.X, 1, false, zone.getDomain())
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zone.getProperties()).toDV();
                return f;
            }
            case MeshZoneType.ID_ATTACHY: {
                DoubleToVector f = Maths.vector(
                        FunctionFactory.DZEROXY// FunctionFactory.rooftop(Axis.Y, 1, false, zone.getDomain())
                        ,
                        FunctionFactory.rooftop(Axis.Y, 1, false, zone.getDomain())
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zone.getProperties()).toDV();
                return f;
            }
            case MeshZoneType.ID_BORDER_NORTH: {
                Domain dd = zone.getDomain();
                DoubleToVector f = Maths.vector(
                        FunctionFactory.DZEROXY// FunctionFactory.rooftop(Axis.Y, 1, false, new DomainXY(dd.xmin,dd.ymin-dd.height*1,dd.width,dd.height*2, DomainXY.Type.LENGTH)).getSegmentAt(1)
                        ,
                        FunctionFactory.rooftop(Axis.Y, 1, false, Domain.forWidth(dd.xmin(), dd.xwidth(), dd.ymin() - dd.ywidth() * 1, dd.ywidth() * 2)).getSegmentAt(1)
                )

                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zone.getProperties()).toDV();
                return f;
            }
            case MeshZoneType.ID_BORDER_SOUTH: {
                Domain dd = zone.getDomain();
                DoubleToVector f = Maths.vector(
                        FunctionFactory.DZEROXY// FunctionFactory.rooftop(Axis.Y, 1, false, new DomainXY(dd.xmin,dd.ymax-dd.height,dd.width,dd.height*2, DomainXY.Type.LENGTH)).getSegmentAt(0)
                        ,
                        FunctionFactory.rooftop(Axis.Y, 1, false, Domain.forWidth(dd.xmin(), dd.xwidth(), dd.ymax() - dd.ywidth(), dd.ywidth() * 2)).getSegmentAt(0)
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zone.getProperties()).toDV();
                return f;
            }
            case MeshZoneType.ID_BORDER_EAST: {
                Domain dd = zone.getDomain();
                DoubleToVector f = Maths.vector(
                        FunctionFactory.rooftop(Axis.X, 1, false, Domain.forWidth(dd.xmax() - dd.xwidth(), dd.xwidth() * 2, dd.ymin(), dd.ywidth())).getSegmentAt(0)
                        ,
                        FunctionFactory.DZEROXY// FunctionFactory.rooftop(Axis.Y, 1, false, new DomainXY(dd.xmax-dd.width,dd.ymin,dd.width*2,dd.height, DomainXY.Type.LENGTH)).getSegmentAt(0)
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zone.getProperties()).toDV();
                return f;
            }
            case MeshZoneType.ID_BORDER_WEST: {
                Domain dd = zone.getDomain();
                DoubleToVector f = Maths.vector(
                        FunctionFactory.rooftop(Axis.X, 1, false, Domain.forWidth(dd.xmin() - dd.xwidth(), dd.xwidth() * 2, dd.ymin(), dd.ywidth())).getSegmentAt(1)
                        ,
                        FunctionFactory.DZEROXY// FunctionFactory.rooftop(Axis.Y, 1, false, new DomainXY(dd.xmin-dd.width,dd.ymin,dd.width*2,dd.height, DomainXY.Type.LENGTH)).getSegmentAt(1)
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zone.getProperties()).toDV();
                return f;
            }
//            case MeshZoneType.ID_BORDER_SOUTH: {
//                DomainXY dd = zone.getDomain();
//                CFunctionVector2D f = new CFunctionVector2D(
//                        FunctionFactory.DZEROXY
//                        ,
//                        FunctionFactory.rooftop(Axis.Y, 1, false, new DomainXY(dd.xmin,dd.ymin,dd.width,dd.height*2,false)).getSegmentAt(1)
//                )
//                        .setProperty("Type", getClass().getSimpleName())
//                    .setProperty("p", index)
//                    .setProperties(zone.getProperties()).toDV();
//                return f;
//            }
        }
        throw new IllegalArgumentException("Unknown ZoneType " + zone.getType());
    }
}
