package net.thevpc.scholar.hadruwaves.solvers;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableValue;

public abstract class AbstractHWSolverTemplate implements HWSolverTemplate {

    private WritableString name = Props.of("name").valueOf(String.class, null);
    private WritableString description = Props.of("description").valueOf(String.class, null);

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
