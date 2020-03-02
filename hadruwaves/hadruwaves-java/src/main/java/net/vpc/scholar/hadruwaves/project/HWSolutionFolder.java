package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;

public class HWSolutionFolder implements HWSolutionElement {
    private WritablePValue<String> name = Props.of("name").valueOf( String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf( String.class, null);
    private WritablePValue<String> parentPath = Props.of("parentPath").valueOf( String.class, null);

    public HWSolutionFolder(String name) {
        name().set(name);
    }


    public WritablePValue<String> parentPath() {
        return parentPath;
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
