package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

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
 *
 */
public final class SinN1S0Pattern extends RectMeshAttachGpPattern {
    private int max;


    public SinN1S0Pattern(int complexity) {
        super(false);
        this.max = complexity;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = super.toElement().toObject().get().builder();
        h.add("max", NElementHelper.elem(max));
        return h.build();
    }

    public DoubleToVector createFunction(int q0, Domain globalDomain, MeshZone zone, MomStructure str) {
        int q=q0;
        int q_i=-1;
        int q_d= q0 /3;
        int q_r= q0 %3;
        if(q_r==0){
            q=q_d*2;
            q_i =1;
        }else{
            q=q_d*2+1;
            q_i =q_r==1?1:-1;
        }
        Domain d=zone.getDomain();
        double qPIbyH=q * PI / d.ywidth() /2;
        Expr f = Maths.vector(
                FunctionFactory.sinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        q_i * qPIbyH,
                        -q_i * qPIbyH * (q_i < 0 ? d.ymax (): d.ymin()),
                        d
                ),
                FunctionFactory.sinY(
                        1 / sqrt(d.xwidth() * d.ywidth()),
                        q_i * qPIbyH,
                        -q_i * qPIbyH * (q_i < 0 ? d.ymax (): d.ymin()),
                        d
                )
        );
        return f.setTitle("0,sinY"+ q_i +"(" + q + "y)")
        .setProperty("Type", "SinY")
        .setProperty("q", q0).toDV();
    }

    public int getCount() {
        return max;
    }

}
