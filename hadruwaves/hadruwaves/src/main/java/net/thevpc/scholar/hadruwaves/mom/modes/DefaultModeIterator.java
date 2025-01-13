package net.thevpc.scholar.hadruwaves.mom.modes;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadruwaves.ModeIndex;
import net.thevpc.scholar.hadruwaves.ModeIterator;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;

import java.util.Iterator;

public class DefaultModeIterator implements ModeIterator {

    public DefaultModeIterator() {
    }

    public Iterator<ModeIndex> iterator(ModeFunctions fn) {
        switch (fn.getEnv().getBorders()){
            case PPPP:{
                return new PeriodicModeIterator(fn.getAvailableModes(),fn.getPolarization());
            }
        }
        return new PositiveModeIterator2(fn.getAvailableModes(),fn.getHintInvariantAxis(),fn.getEnv().getBorders());
    }

    @Override
    public boolean isAbsoluteIterator(ModeFunctions fn) {
        return true;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofFunction(getClass().getSimpleName()).build();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultModeIterator that = (DefaultModeIterator) o;

        return true;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}