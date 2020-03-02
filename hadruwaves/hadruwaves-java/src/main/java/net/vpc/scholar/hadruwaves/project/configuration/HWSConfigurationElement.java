package net.vpc.scholar.hadruwaves.project.configuration;

import net.vpc.common.prpbind.WritablePValue;

public interface HWSConfigurationElement {
    WritablePValue<String> name();

    WritablePValue<String> description();

    WritablePValue<String> parentPath();
}
