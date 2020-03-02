package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.prpbind.WritablePValue;

public interface HWSParameterElement {
    WritablePValue<String> name();

    WritablePValue<String> description();

    WritablePValue<String> parentPath();
}
