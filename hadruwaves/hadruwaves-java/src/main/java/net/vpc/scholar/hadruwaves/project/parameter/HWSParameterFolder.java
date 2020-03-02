package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;

public class HWSParameterFolder implements HWSParameterElement {
    private WritablePValue<String> name = Props.of("name").valueOf( String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf( String.class, null);
    private WritablePValue<String> parentPath = Props.of("parentPath").valueOf( String.class, null);

    public WritablePValue<String> parentPath() {
        return parentPath;
    }

    public HWSParameterFolder(String name) {
        name().set(name);
    }

    @Override
    public WritablePValue<String> name() {
        return name;
    }

    @Override
    public WritablePValue<String> description() {
        return description;
    }
}
