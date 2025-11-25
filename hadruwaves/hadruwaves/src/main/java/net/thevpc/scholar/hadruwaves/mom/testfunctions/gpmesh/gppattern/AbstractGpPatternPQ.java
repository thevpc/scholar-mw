package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

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

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = super.toElement().toObject().get().builder();
        h.add("max", NElementHelper.elem(max));
        return h.build();
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
