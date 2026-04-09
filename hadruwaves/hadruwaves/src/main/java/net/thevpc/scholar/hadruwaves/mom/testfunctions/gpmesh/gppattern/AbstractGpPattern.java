package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import java.util.ArrayList;
import net.thevpc.common.mon.ProgressMonitor;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.log.NLogger;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.Domain;

import java.util.List;
import net.thevpc.nuts.text.NMsg;

import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 29 mai 2007 23:08:11
 */
public abstract class AbstractGpPattern implements GpPattern, Cloneable {

    protected AbstractGpPattern() {
    }

    public List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds) {
        return zones;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = NElement.ofObjectBuilder(getClass().getSimpleName());
        return h.build();
    }

    public DoubleToVector[] createFunctions(Domain globalDomain, MeshZone zone, ProgressMonitor monitor, MomStructure str, NLogger logger) {
        List<DoubleToVector> all = new ArrayList<>();
        int c = getCount();
        for (int i = 0; i < c; i++) {
            DoubleToVector z;
            try {
                z = createFunction(i, globalDomain, zone, str);
            } catch (Exception ex) {
                logger.log(NMsg.ofC("[%s] %s produced error function at %s : ex", getClass().getSimpleName(), i, ex));
                continue;
            }
            if (z == null) {
                logger.log(NMsg.ofC("DoubleToVector%s produced null function at %s", getClass().getSimpleName(), i));
                continue;
            }
            all.add(z);
        }
        return all.toArray(new DoubleToVector[0]);
    }

    protected abstract DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str);

}
