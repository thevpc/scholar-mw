package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.List;

import net.vpc.scholar.hadruwaves.mom.MomStructure;

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

    public Dumper getDumper() {
        return new Dumper(this, Dumper.Type.SIMPLE);
    }


    public String dump() {
        return getDumper().toString();
    }

    public DoubleToVector[] createFunctions(Domain globalDomain, MeshZone zone, ProgressMonitor monitor, MomStructure str) {
        DoubleToVector[] all=new DoubleToVector[getCount()];
        for (int i = 0; i < all.length; i++) {
            all[i]=createFunction(i, globalDomain, zone, str);
        }
        return all;
    }

    protected abstract DoubleToVector createFunction(int index, Domain globalDomain, MeshZone zone, MomStructure str);

}
