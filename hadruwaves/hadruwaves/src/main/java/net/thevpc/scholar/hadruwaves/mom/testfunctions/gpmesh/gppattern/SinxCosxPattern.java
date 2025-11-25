package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
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
    public NElement toElement() {
        NObjectElementBuilder h = super.toElement().toObject().get().builder();
        h.add("max", NElementHelper.elem(max));
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
