package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

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

import net.vpc.scholar.hadrumaths.symbolic.double2double.Rooftop2DFunctionXY;
import net.vpc.scholar.hadrumaths.symbolic.double2double.RooftopType;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:41:33
 */
public final class Rooftop2DSimplePattern extends RectMeshAttachGpPattern {
    private RooftopType type;

    public Rooftop2DSimplePattern(MeshZoneTypeFilter filter) {
        super(filter);
    }


    public Rooftop2DSimplePattern(RooftopType type) {
        super(MeshZoneType.FILTER_M);
        this.type = type;
    }
    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("type", context.elem(type));
        return h.build();
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
                        new Rooftop2DFunctionXY(zone.getDomain(), null, type)
                        ,
                        new Rooftop2DFunctionXY(zone.getDomain(), null, type)
                )
                        .setProperty("Type", getClass().getSimpleName())
                        .setProperties(zone.getProperties()).toDV();
//                f.setProperties(properties);
                return f;
            }
        }
        throw new IllegalArgumentException("Unknown ZoneType " + zone.getType());
    }
}
