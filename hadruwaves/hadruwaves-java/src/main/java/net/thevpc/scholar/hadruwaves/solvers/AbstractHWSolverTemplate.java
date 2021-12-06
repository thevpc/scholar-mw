package net.thevpc.scholar.hadruwaves.solvers;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableString;

public abstract class AbstractHWSolverTemplate implements HWSolverTemplate {

    private WritableString name = Props.of("name").stringOf(null);
    private WritableString description = Props.of("description").stringOf(null);

    public WritableString name() {
        return name;
    }

    public WritableString description() {
        return description;
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }

}
