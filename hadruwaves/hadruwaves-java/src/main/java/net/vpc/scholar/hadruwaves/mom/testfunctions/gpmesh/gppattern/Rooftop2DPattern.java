package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import java.util.Map;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneTypeFilter;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.symbolic.double2double.AbstractDoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Rooftop2DFunctionXY;
import net.vpc.scholar.hadrumaths.symbolic.double2double.RooftopType;
import net.vpc.scholar.hadruwaves.mom.CircuitType;
import net.vpc.scholar.hadruwaves.Boundary;
import net.vpc.scholar.hadruwaves.mom.util.MomUtils;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:41:33
 */
public final class Rooftop2DPattern extends RectMeshAttachGpPattern {

    private Axis invariance;
    private boolean alwaysAttachForX = false;
    private boolean alwaysAttachForY = false;

    public Rooftop2DPattern(MeshZoneTypeFilter filter) {
        this(filter, null);
    }

    public Rooftop2DPattern() {
        this((Axis) null);
    }

    public Rooftop2DPattern(Axis invariance) {
        this(true, true, invariance);
    }

    public Rooftop2DPattern(boolean attachX, boolean attachY) {
        this(attachX, attachY, null);
    }


    public Rooftop2DPattern(MeshZoneTypeFilter filter, Axis invariance) {
        super(filter);
        this.invariance = invariance;
    }

    public Rooftop2DPattern(boolean attachX, boolean attachY, Axis invariance) {
        super(attachX, attachY);
        this.invariance = invariance;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("invariance", context.elem(invariance));
        h.add("alwaysAttachForX", context.elem(alwaysAttachForX));
        h.add("alwaysAttachForY", context.elem(alwaysAttachForY));
        return h.build();
    }

    public boolean isAlwaysAttachForX() {
        return alwaysAttachForX;
    }

    public Rooftop2DPattern setAlwaysAttachForX(boolean alwaysAttachForX) {
        this.alwaysAttachForX = alwaysAttachForX;
        return this;
    }

    public boolean isAlwaysAttachForY() {
        return alwaysAttachForY;
    }

    public void setAlwaysAttachForY(boolean alwaysAttachForY) {
        this.alwaysAttachForY = alwaysAttachForY;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        Domain zoneDomain = zone.getDomain();
        Map zoneProperties = zone.getProperties();
        MeshZoneType zoneType = zone.getType();
//            type=Zone.Type.MAIN;
        String edgeType = (String) zoneProperties.get("edgeType");
        CircuitType circuitType = str.getCircuitType();
        boolean neededAttachForX = false;
        boolean neededAttachForY = false;
        Boundary defaultWall = CircuitType.SERIAL.equals(circuitType) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary wall = null;
        switch (zoneType.getValue()) {
            case MeshZoneType.ID_MAIN: {
                DoubleToVector f = Maths.vector(
                        new Rooftop2DFunctionXY(zoneDomain, invariance, RooftopType.FULL),
                        new Rooftop2DFunctionXY(zoneDomain, invariance, RooftopType.FULL))
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperties(zoneProperties).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_ATTACHX: {
                DoubleToVector f = Maths.vector(
                        new Rooftop2DFunctionXY(zoneDomain, invariance, RooftopType.FULL),
                        new Rooftop2DFunctionXY(zoneDomain, invariance, RooftopType.FULL)// FunctionFactory.rooftop(Axis.X, 1, false, zone.getDomain())
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zoneProperties).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_ATTACHY: {
                DoubleToVector f = Maths.vector(
                        new Rooftop2DFunctionXY(zoneDomain, invariance, RooftopType.FULL)// FunctionFactory.rooftop(Axis.Y, 1, false, zone.getDomain())
                        ,
                        new Rooftop2DFunctionXY(zoneDomain, invariance, RooftopType.FULL))
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zoneProperties).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_BORDER_NORTH: {
                Domain dd = zoneDomain;
                Axis wallAxis = Axis.X;
                wall = "Box".equals(edgeType) ? str.getBorders().north : "Nothing".equals(edgeType) ? defaultWall : null;
                neededAttachForX = alwaysAttachForX || (wall != null && (CircuitType.SERIAL.equals(circuitType) ? MomUtils.isCurrentMaximumForWall(Axis.X, wall, wallAxis)
                        : MomUtils.isElectricFieldMaximumForWall(Axis.X, wall, wallAxis)));
                neededAttachForY = alwaysAttachForY || (wall != null && (CircuitType.SERIAL.equals(circuitType) ? MomUtils.isCurrentMaximumForWall(Axis.Y, wall, wallAxis)
                        : MomUtils.isElectricFieldMaximumForWall(Axis.Y, wall, wallAxis)));
                DoubleToVector f = Maths.vector(
                        !neededAttachForX ? Maths.DZEROXY : new Rooftop2DFunctionXY(dd, invariance, RooftopType.NORTH),
                        !neededAttachForY ? Maths.DZEROXY : new Rooftop2DFunctionXY(dd, invariance, RooftopType.NORTH))
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zoneProperties).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_BORDER_SOUTH: {
                Domain dd = zoneDomain;
                wall = "Box".equals(edgeType) ? str.getBorders().south : "Nothing".equals(edgeType) ? defaultWall : null;
                Axis wallAxis = Axis.X;
                neededAttachForX = alwaysAttachForX || (wall != null && (CircuitType.SERIAL.equals(circuitType) ? MomUtils.isCurrentMaximumForWall(Axis.X, wall, wallAxis)
                        : MomUtils.isElectricFieldMaximumForWall(Axis.X, wall, wallAxis)));
                neededAttachForY = alwaysAttachForY || (wall != null && (CircuitType.SERIAL.equals(circuitType) ? MomUtils.isCurrentMaximumForWall(Axis.Y, wall, wallAxis)
                        : MomUtils.isElectricFieldMaximumForWall(Axis.Y, wall, wallAxis)));
                AbstractDoubleToDouble fct = new Rooftop2DFunctionXY(dd, invariance, RooftopType.SOUTH);
                //DFunctionXY fct=new DCstFunctionXY(new DomainXY(dd.xmin, dd.ymin + dd.height / 2, dd.width, dd.height / 2, DomainXY.Type.LENGTH));
                DoubleToVector f = Maths.vector(
                        !neededAttachForX ? Maths.DZEROXY : fct,
                        !neededAttachForY ? Maths.DZEROXY : fct)
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zoneProperties).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_BORDER_EAST: {
                Domain dd = zoneDomain;
                wall = "Box".equals(edgeType) ? str.getBorders().east : "Nothing".equals(edgeType) ? defaultWall : null;
                Axis wallAxis = Axis.Y;
                neededAttachForX = alwaysAttachForX || (wall != null && (CircuitType.SERIAL.equals(circuitType) ? MomUtils.isCurrentMaximumForWall(Axis.X, wall, wallAxis)
                        : MomUtils.isElectricFieldMaximumForWall(Axis.X, wall, wallAxis)));
                neededAttachForY = alwaysAttachForY || (wall != null && (CircuitType.SERIAL.equals(circuitType) ? MomUtils.isCurrentMaximumForWall(Axis.Y, wall, wallAxis)
                        : MomUtils.isElectricFieldMaximumForWall(Axis.Y, wall, wallAxis)));
                DoubleToVector f = Maths.vector(
                        !neededAttachForX ? Maths.DZEROXY : new Rooftop2DFunctionXY(dd, invariance, RooftopType.EAST),
                        !neededAttachForY ? Maths.DZEROXY : new Rooftop2DFunctionXY(dd, invariance, RooftopType.EAST))
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zoneProperties).toDV();
//                f.setProperties(properties);
                return f;
            }
            case MeshZoneType.ID_BORDER_WEST: {
                Domain dd = zoneDomain;
                wall = "Box".equals(edgeType) ? str.getBorders().west : "Nothing".equals(edgeType) ? defaultWall : null;
                Axis wallAxis = Axis.Y;
                neededAttachForX = alwaysAttachForX || (wall != null && (CircuitType.SERIAL.equals(circuitType) ? MomUtils.isCurrentMaximumForWall(Axis.X, wall, wallAxis)
                        : MomUtils.isElectricFieldMaximumForWall(Axis.X, wall, wallAxis)));
                neededAttachForY = alwaysAttachForY || (wall != null && (CircuitType.SERIAL.equals(circuitType) ? MomUtils.isCurrentMaximumForWall(Axis.Y, wall, wallAxis)
                        : MomUtils.isElectricFieldMaximumForWall(Axis.Y, wall, wallAxis)));
                DoubleToVector f = Maths.vector(
                        !neededAttachForX ? Maths.DZEROXY : new Rooftop2DFunctionXY(dd, invariance, RooftopType.WEST),
                        !neededAttachForY ? Maths.DZEROXY : new Rooftop2DFunctionXY(dd, invariance, RooftopType.WEST))
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperty("p", index)
                        .setProperties(zoneProperties).toDV();
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
        }
        throw new IllegalArgumentException("Unknown ZoneType " + zoneType);
    }
}
