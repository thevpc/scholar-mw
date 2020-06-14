package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.props.PValue;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadruwaves.project.HWProject;

public interface HWParameterElement extends TsonSerializable {
    WritablePValue<String> name();

    WritablePValue<String> description();

    PValue<HWProject> project();

    PValue<HWParameterFolder> parent();

    PValue<String> parentPath();

    void remove();
}
