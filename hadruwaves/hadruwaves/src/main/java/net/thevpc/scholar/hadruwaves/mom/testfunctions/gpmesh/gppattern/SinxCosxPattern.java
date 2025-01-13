package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:42:19
 */
public final class SinxCosxPattern extends RectMeshAttachGpPattern {
    private int max;

    public SinxCosxPattern(int complexity) {
        super(false);
        this.max = complexity;
    }

    public int getCount() {
        return max;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("max", context.elem(max));
        return h.build();
    }


    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        Domain domain2 = zone.getDomain();
        Expr f= Maths.vector(
                (
                        FunctionFactory.sinX(
                                1 / Math.sqrt(domain2.xwidth() * domain2.ywidth()),
                                index * Math.PI / domain2.xwidth(),
                                -index * Math.PI * domain2.xmin() / domain2.xwidth(),
                                domain2
                        )),
                (FunctionFactory.cosX(
                        1 / Math.sqrt(domain2.xwidth() * domain2.ywidth()),
                        index * Math.PI / domain2.xwidth(),
                        -index * Math.PI * domain2.xmin() / domain2.xwidth(),
                        domain2
                ))
        );
        return f.setTitle("sico(" + index + ")").toDV();
    }

}
