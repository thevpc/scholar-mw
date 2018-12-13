package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;

import java.util.Collection;
import java.util.Set;

/**
 * Created by vpc on 6/4/17.
 */
public class VDiscretePlotModel extends BasePlotModel {
    private ComplexAsDouble converter;
    private Set<ExternalLibrary> preferredLibraries;
    private VDiscrete[] vdiscretes;

    public VDiscretePlotModel() {
    }

    public VDiscretePlotModel setConverter(ComplexAsDouble zDoubleFunction) {
        Object old = this.converter;
        this.converter = zDoubleFunction;
//        firePropertyChange("converter", old, this.converter);
        return this;
    }

    public ComplexAsDouble getConverter() {
        return converter;
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
