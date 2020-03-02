package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.prpbind.WritablePValue;

public interface HWSolutionElement {
    WritablePValue<String> name();

    WritablePValue<String> description();

    WritablePValue<String> parentPath();

}
