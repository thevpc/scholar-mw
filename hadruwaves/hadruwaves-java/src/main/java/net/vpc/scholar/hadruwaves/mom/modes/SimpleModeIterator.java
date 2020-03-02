package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadruwaves.ModeIndex;
import net.vpc.scholar.hadruwaves.ModeIterator;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;

import java.util.Iterator;

public class SimpleModeIterator implements ModeIterator {

    public SimpleModeIterator() {
    }

    public Iterator<ModeIndex> iterator(ModeFunctions fn) {
        switch (fn.getEnv().getBorders()) {
            case PPPP: {
                return new PeriodicModeIterator(fn.getAvailableModes(), fn.getPolarization());
            }
        }
        return new SimplePositiveModeIterator(fn.getAvailableModes(), fn.getHintInvariantAxis(), fn.getEnv().getBorders(), fn.getSize());
    }

    @Override
    public boolean isAbsoluteIterator(ModeFunctions fn) {
        return false;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleModeIterator that = (SimpleModeIterator) o;

        return true;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}