package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneTypeFilter;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import net.thevpc.scholar.hadrumaths.symbolic.double2double.Rooftop2DFunctionXY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RooftopType;

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
