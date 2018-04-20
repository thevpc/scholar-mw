package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.ModeIterator;
import net.vpc.scholar.hadruwaves.ModeIteratorFactory;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;

public class SimpleModeIteratorFactory implements ModeIteratorFactory {

    public SimpleModeIteratorFactory() {
    }

    public ModeIterator iterator(ModeFunctions fn) {
        switch (fn.getBorders()){
            case PPPP:{
                return new PeriodicModeIterator(fn.getAvailableModes(),((DefaultBoxModeFunctions)fn).getAxis());
            }
        }
        return new SimplePositiveModeIterator(fn.getAvailableModes(),fn.getHintInvariantAxis(),fn.getBorders(),fn.getMaxSize());
    }

    @Override
    public boolean isAbsoluteIterator(ModeFunctions fn) {
        return false;
    }

    public String dump() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleModeIteratorFactory that = (SimpleModeIteratorFactory) o;

        return true;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}