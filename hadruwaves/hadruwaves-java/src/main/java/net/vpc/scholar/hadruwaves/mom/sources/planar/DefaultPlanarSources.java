package net.vpc.scholar.hadruwaves.mom.sources.planar;

import java.util.ArrayList;
import java.util.Arrays;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.mom.sources.*;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 * @author vpc
 */
public final class DefaultPlanarSources implements PlanarSources, Cloneable {

    private List<PlanarSource> all = new ArrayList<PlanarSource>();

    public DefaultPlanarSources(Collection<PlanarSource> sources) {
        this(sources.toArray(new PlanarSource[sources.size()]));
    }

    public DefaultPlanarSources(PlanarSource... sources) {
        this.all.addAll(Arrays.asList(sources));
    }

    public void add(Source source) {
        add((PlanarSource) source);
    }

    public PlanarSources add(PlanarSource source) {
        all.add(source);
        return this;
    }

    public PlanarSource[] getPlanarSources() {
        return all.toArray(new PlanarSource[all.size()]);
    }

    public String dump() {
        return new Dumper(this).add("sources", all).toString();
    }

    @Override
    public DefaultPlanarSources clone() {
        try {
            List<PlanarSource> all2 = new ArrayList<PlanarSource>();
            for (PlanarSource p : all) {
                all2.add(p.clone());
            }
            DefaultPlanarSources ss = (DefaultPlanarSources) super.clone();
            ss.all = all2;
            return ss;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DefaultPlanarSources.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public void setStructure(MomStructure str) {
        //
    }

    /**
     *
     * @return all functions of getPlanarSources();
     */
    public DoubleToVector[] getSourceFunctions() {
        PlanarSource[] sources = getPlanarSources();
        DoubleToVector[] fcts = new DoubleToVector[sources.length];
        for (int i = 0; i < sources.length; i++) {
            fcts[i] = sources[i].getFunction();
        }
        return fcts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultPlanarSources that = (DefaultPlanarSources) o;

        return all != null ? all.equals(that.all) : that.all == null;
    }

    @Override
    public int hashCode() {
        return all != null ? all.hashCode() : 0;
    }
}
