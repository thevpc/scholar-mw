package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 16 oct. 2006 12:08:02
 */
public abstract class AbstractGpPatternPQ extends RectMeshAttachGpPattern{
    private int max;

    protected AbstractGpPatternPQ(int complexity) {
        super(false);
        this.max = complexity;
    }
    public Dumper getDumper() {
        Dumper h = super.getDumper();
        h.add("max",max);
        return h;
    }

    public final DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        int p=index/max;
        int q=index%max;
        return createFunction(index, p, q,zone.getDomain(), globalDomain,str);
    }

    public abstract DoubleToVector createFunction(int index, int p, int q, Domain d, Domain globalDomain, MomStructure str);

    public int getCount() {
        return max*max;//max *max;
    }

}
