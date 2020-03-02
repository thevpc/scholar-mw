package net.vpc.scholar.hadruwaves.project.result;

import net.vpc.common.prpbind.WritablePValue;

public interface HWSResultElement {
    WritablePValue<String> parentPath();

    WritablePValue<String> name();

    WritablePValue<String> description();
}
