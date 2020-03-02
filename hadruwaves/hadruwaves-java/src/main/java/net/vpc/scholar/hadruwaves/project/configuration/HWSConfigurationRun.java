package net.vpc.scholar.hadruwaves.project.configuration;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePMap;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadruwaves.project.result.HWSResults;

public class HWSConfigurationRun implements TsonSerializable, HWSConfigurationElement {
    private WritablePValue<String> filePath = Props.of("filePath").valueOf(String.class, null);
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private WritablePValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);
    /**
     * name to expression map
     */
    private WritablePMap<String, String> parameters = Props.of("parameters").mapOf(String.class, String.class);
    private HWSResults results = new HWSResults();

    public HWSConfigurationRun() {
    }

    public HWSConfigurationRun(String name) {
        name().set(name);
    }

    public WritablePMap<String, String> parameters() {
        return parameters;
    }

    public HWSResults results() {
        return results;
    }


    @Override
    public WritablePValue<String> name() {
        return name;
    }

    @Override
    public WritablePValue<String> description() {
        return description;
    }

    public WritablePValue<String> parentPath() {
        return parentPath;
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
