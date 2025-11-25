package net.thevpc.scholar.hadruwaves.mom.modes;


import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadruwaves.ModeIndex;
import net.thevpc.scholar.hadruwaves.ModeIterator;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;

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
    public NElement toElement() {
        return NElement.ofUplet(getClass().getSimpleName());
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
