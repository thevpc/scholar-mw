package net.vpc.scholar.hadruwaves.solvers;

import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;

public abstract class AbstractHWSolverTemplate implements HWSolverTemplate {

    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);

    public WritablePValue<String> name() {
        return name;
    }

    public WritablePValue<String> description() {
        return description;
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }

}
