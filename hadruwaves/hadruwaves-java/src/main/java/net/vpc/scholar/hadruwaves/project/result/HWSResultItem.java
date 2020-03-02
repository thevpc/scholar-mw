package net.vpc.scholar.hadruwaves.project.result;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;

public class HWSResultItem implements TsonSerializable, HWSResultElement {
    private WritablePValue<String> filePath = Props.of("filePath").valueOf( String.class, null);
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf( String.class, null);
    private WritablePValue<String> parentPath = Props.of("parentPath").valueOf( String.class, null);

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

    public WritablePValue<String> filePath() {
        return filePath;
    }

    public TsonElement toTsonElement() {
        return Tson.obj("configuration")
                .add("filePath", filePath().get())
                .build();
    }
}
