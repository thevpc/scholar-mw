package net.thevpc.scholar.hadruwaves.solvers;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableValue;

public abstract class AbstractHWSolverTemplate implements HWSolverTemplate {

    private WritableValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritableValue<String> description = Props.of("description").valueOf(String.class, null);

    public WritableValue<String> name() {
        return name;
    }

    public WritableValue<String> description() {
        return description;
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }

}
