package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

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

    public Dumper getDumper() {
        Dumper h = super.getDumper();
        h.add("max",max);
        return h;
    }


    public DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        Domain domain2 = zone.getDomain();
        DoubleToVector f= Maths.vector(
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
