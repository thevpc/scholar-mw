package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;

import java.util.Collection;
import java.util.Set;

/**
 * Created by vpc on 6/4/17.
 */
public class VDiscretePlotModel implements PlotModel {
    private String title;
    private Set<ExternalLibrary> preferredLibraries;
    private VDiscrete[] vdiscretes;

    public VDiscretePlotModel() {
    }

    public String getTitle() {
        return title;
    }

    public VDiscretePlotModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public Set<ExternalLibrary> getPreferredLibraries() {
        return preferredLibraries;
    }

    public VDiscretePlotModel setPreferredLibraries(Set<ExternalLibrary> preferredLibraries) {
        this.preferredLibraries = preferredLibraries;
        return this;
    }

    public VDiscrete[] getVdiscretes() {
        return vdiscretes;
    }

    public VDiscretePlotModel setVdiscretes(Collection<VDiscrete> vdiscretes) {
        return setVdiscretes(vdiscretes.toArray(new VDiscrete[vdiscretes.size()]));
    }

    public VDiscretePlotModel setVdiscretes(VDiscrete... vdiscretes) {
        this.vdiscretes = vdiscretes;
        return this;
    }
}
