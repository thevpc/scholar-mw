package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 16 oct. 2006 12:08:02
 */
public abstract class AbstractGpFnPatternPQ extends RectMeshAttachGpPattern {
    private int max;
    private ModeType[] modes;

    protected AbstractGpFnPatternPQ(int complexity, ModeType[] modes) {
        super(false);
        this.max = complexity;
        this.modes = modes==null? ModeType.values():modes;
    }


    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("max", context.elem(max));
        h.add("modes", context.elem(modes));
        return h.build();
    }

    public final DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str) {
        int modeIndex = index % modes.length;
        ModeType mode = modes[modeIndex];
        int p = (index / modes.length) / max;
        int q = (index / modes.length) % (max);
        return createFunction(index, mode, p, q, zone.getDomain(), globalDomain);
    }

    protected boolean doAcceptMode(ModeType m) {
        for (ModeType mode : modes) {
            if (m.equals(mode)) {
                return true;
            }
        }
        return false;
    }

    public abstract DoubleToVector createFunction(int index, ModeType mode, int p, int q, Domain d, Domain globalDomain);

    public int getCount() {
        return max * max * modes.length;//max *max;
    }

}