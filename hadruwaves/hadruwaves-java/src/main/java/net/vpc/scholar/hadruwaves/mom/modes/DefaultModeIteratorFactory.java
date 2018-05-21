package net.vpc.scholar.hadruwaves.mom.modes;

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

    @Override
    public boolean isAbsoluteIterator(ModeFunctions fn) {
        return true;
    }

    public String dump() {
        return getClass().getSimpleName();
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