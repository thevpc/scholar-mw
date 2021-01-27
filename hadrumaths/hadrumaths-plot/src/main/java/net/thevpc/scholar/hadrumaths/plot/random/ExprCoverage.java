package net.thevpc.scholar.hadrumaths.plot.random;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ExprCoverage {
    private Set<Class> coveredClasses = new HashSet<>();
    private Set<Class> uncoveredClasses = new HashSet<>();

    public void coverClass(Class covered) {
        coveredClasses.add(covered);
        uncoveredClasses.remove(covered);
    }

    public void uncoverClass(Class covered) {
        uncoveredClasses.add(covered);
        coveredClasses.remove(covered);
    }

    public void reset(Class uncovered) {
        uncoverClass(uncovered);
    }

    public void reset(Collection<Class> uncovered) {
        uncoveredClasses.addAll(uncovered);
        coveredClasses.removeAll(uncovered);
    }

    public Set<Class> getCoveredClasses() {
        return coveredClasses;
    }

    public Set<Class> getUncoveredClasses() {
        return uncoveredClasses;
    }
}
