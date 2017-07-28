package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.ModeIterator;
import net.vpc.scholar.hadruwaves.ModeIteratorFactory;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;

public class DefaultModeIteratorFactory implements ModeIteratorFactory {

    public DefaultModeIteratorFactory() {
    }

    public ModeIterator iterator(ModeFunctions fn) {
        switch (fn.getBorders()){
            case PPPP:{
                return new PeriodicModeIterator(fn.getAvailableModes(),((DefaultBoxModeFunctions)fn).getAxis());
            }
        }
        return new PositiveModeIterator(fn.getAvailableModes(),fn.getHintInvariantAxis(),fn.getBorders());
    }

    public String dump() {
        Dumper h = new Dumper(this, Dumper.Type.SIMPLE);
        return h.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultModeIteratorFactory that = (DefaultModeIteratorFactory) o;

        return true;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}