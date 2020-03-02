package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneTypeFilter;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.Wall;
import net.vpc.scholar.hadruwaves.WallBorders;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:41:47
 */
public final class ArcheSinusPattern extends RectMeshAttachGpPattern {
    private double factor;

    public ArcheSinusPattern() {
        this(true, true, 1);
    }

    public ArcheSinusPattern(double factor) {
        this(true, true, factor);
    }

    public ArcheSinusPattern(boolean attachX, boolean attachY, double factor) {
        super(attachX, attachY);
        this.factor = factor;
    }

    public ArcheSinusPattern(MeshZoneTypeFilter filter, double factor) {
        super(filter);
        this.factor = factor;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("factor", context.elem(factor));
        return h.build();
    }

    public int getCount() {
        return 1;
    }

    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        WallBorders walls = BoxModesPattern.getVirtualWalls(zone, globalDomain, str);
//            type=Zone.Type.MAIN;
        switch (zone.getType().getValue()) {
            case MeshZoneType.ID_MAIN: {
                DoubleToVector f = Maths.vector(
                        FunctionFactory.archeSinus(Axis.X, factor, zone.getDomain())
                        ,
                        FunctionFactory.archeSinus(Axis.Y, factor, zone.getDomain())
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperty("factor", factor).toDV();
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
                        .setProperty("p", index)
                        .setProperty("factor", factor).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_BORDER_NORTH: {
                Domain dd = zone.getDomain();
                DoubleToVector f = Maths.vector(
                        walls.getNorth().equals(Wall.MAGNETIC) ? FunctionFactory.archeSinus(Axis.Y, 1, Domain.ofWidth(dd.xmin(), dd.xwidth(), dd.ymin() - dd.ywidth() * 1, dd.ywidth() * 2)).getSegmentAt(1) : Maths.DZEROXY
                        ,
                        walls.getNorth().equals(Wall.ELECTRIC) ? FunctionFactory.archeSinus(Axis.Y, 1, Domain.ofWidth(dd.xmin(), dd.xwidth(), dd.ymin() - dd.ywidth() * 1, dd.ywidth() * 2)).getSegmentAt(1) : Maths.DZEROXY

                )
                        .setProperty("Type", getClass().getSimpleName() + "-BorderNorth")
                        .setProperty("p", index)
                        .setProperty("factor", factor).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_BORDER_SOUTH: {
                Domain dd = zone.getDomain();
                DoubleToVector f = Maths.vector(
                        walls.getSouth().equals(Wall.MAGNETIC) ? FunctionFactory.archeSinus(Axis.Y, 1, Domain.ofWidth(dd.xmin(), dd.xwidth(), dd.ymax() - dd.ywidth() * 1, dd.ywidth() * 2)).getSegmentAt(0) : Maths.DZEROXY
                        ,
                        walls.getSouth().equals(Wall.ELECTRIC) ? FunctionFactory.archeSinus(Axis.Y, 1, Domain.ofWidth(dd.xmin(), dd.xwidth(), dd.ymax() - dd.ywidth() * 1, dd.ywidth() * 2)).getSegmentAt(0) : Maths.DZEROXY

                )
                        .setProperty("Type", getClass().getSimpleName() + "-BorderSouth")
                        .setProperty("p", index)
                        .setProperty("factor", factor).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_BORDER_WEST: {
                Domain dd = zone.getDomain();
                DoubleToVector f = Maths.vector(
                        FunctionFactory.archeSinus(Axis.X, 1, Domain.ofWidth(dd.xmin() - dd.xwidth(), dd.xwidth() * 2, dd.ymin(), dd.ywidth())).getSegmentAt(1)
                        ,
                        Maths.DZEROXY
                )
                        .setProperty("Type", getClass().getSimpleName() + "-BorderWest")
                        .setProperty("p", index)
                        .setProperty("factor", factor).toDV();
//                f.setProperties(properties);
                return f;
            }
//            case MeshZoneType.ID_BORDER_SOUTH: {
//                DomainXY dd = zone.getDomain();
//                CFunctionVector2D f = new CFunctionVector2D(
//                        FunctionFactory.DZEROXY
//                        ,
//                        FunctionFactory.rooftop(Axis.Y, 1, false, new DomainXY(dd.xmin,dd.ymin,dd.width,dd.height*2,false)).getSegmentAt(1)
//                );
//                LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();
//                properties.put("Type", getClass().getSimpleName() + "-BorderSouth");
//                properties.put("p", index);
//                f.setProperties(properties);
//                return f;
//            }
            case MeshZoneType.ID_ATTACHX: {
                DoubleToVector f = Maths.vector(
                        FunctionFactory.archeSinus(Axis.X, 1, zone.getDomain())
                        ,
                        Maths.DZEROXY
                )
                        .setProperty("Type", getClass().getSimpleName() + "-AttachX")
                        .setProperty("p", index)
                        .setProperty("factor", factor).toDV();
//                f.setProperties(properties);
                return f;
            }
        }
        return null;
    }

}
