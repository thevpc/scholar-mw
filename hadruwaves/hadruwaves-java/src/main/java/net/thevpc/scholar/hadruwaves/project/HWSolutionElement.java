package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.PValue;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.tson.TsonSerializable;

public interface HWSolutionElement extends TsonSerializable{
    WritablePValue<String> name();

    WritablePValue<String> description();

    WritablePValue<String> parentPath();

    PValue<HWSolution> solution();

    PValue<HWSolutionFolder> parent();

}
