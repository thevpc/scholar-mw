package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.PValue;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.tson.TsonSerializable;
import net.thevpc.scholar.hadruwaves.project.HWProject;

public interface HWParameterElement extends TsonSerializable {
    WritablePValue<String> name();

    WritablePValue<String> description();

    PValue<HWProject> project();

    PValue<HWParameterFolder> parent();

    PValue<String> parentPath();

    void remove();
}
