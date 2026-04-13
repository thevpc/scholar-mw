package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 16 oct. 2006 12:08:02
 */
public abstract class AbstractGpPatternPQ2 extends RectMeshAttachGpPattern implements Cloneable {
    private final int xcols;
    private final int ycols;
    private final int xcount;
    private final int ycount;

    protected AbstractGpPatternPQ2(HintAxisType preferredAxisType, int xcount, int ycount) {
        super(preferredAxisType, false);
        if (xcount < 0) {
            throw new IllegalArgumentException("xcount must be positive");
        }
        if (ycount < 0) {
            throw new IllegalArgumentException("ycount must be positive");
        }
        if (xcount == 0 && ycount == 0) {
            throw new IllegalArgumentException("xcount and ycount cannot be 0");
        }

        this.xcount = xcount;
        this.ycount = ycount;
        this.xcols = (int)Math.ceil(Math.sqrt(xcount));
        this.ycols = (int)Math.ceil(Math.sqrt(ycount));
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = super.toElement().toObject().get().builder();
        h.add("xcount", NElementHelper.elem(xcount));
        h.add("ycount", NElementHelper.elem(ycount));
        return h.build();
    }

    public final DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str, HintAxisType preferredAxisType) {
        int p;
        int q;
        Axis a;
        if (index < xcount) {
            p = index / xcols;
            q = index % xcols;
            a = Axis.X;
        } else {
            int j = index - xcount;
            p = j / ycols;
            q = j % ycols;
            a = Axis.Y;
        }
        return createFunction(index, p, q, a, zone.getDomain(), globalDomain, preferredAxisType, str);
    }

    public abstract DoubleToVector createFunction(int index, int p, int q, Axis axis, Domain d, Domain globalDomain, HintAxisType preferredAxisType, MomStructure str);

    public int getCount() {
        return xcount + ycount;
    }

}
