package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.Domain;

import java.util.List;

import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 29 mai 2007 23:08:11
 */
public abstract class AbstractGpPattern implements GpPattern {
    protected AbstractGpPattern() {
    }

    public List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds) {
        return zones;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = Tson.ofObjectBuilder(getClass().getSimpleName());
        return h.build();
    }

    public DoubleToVector[] createFunctions(Domain globalDomain, MeshZone zone, ProgressMonitor monitor, MomStructure str) {
        DoubleToVector[] all = new DoubleToVector[getCount()];
        for (int i = 0; i < all.length; i++) {
            all[i] = createFunction(i, globalDomain, zone, str);
        }
        return all;
    }

    protected abstract DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str);

}
