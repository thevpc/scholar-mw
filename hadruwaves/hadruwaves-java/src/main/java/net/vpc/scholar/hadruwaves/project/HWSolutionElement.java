package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.props.PValue;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.tson.TsonSerializable;

public interface HWSolutionElement extends TsonSerializable{
    WritablePValue<String> name();

    WritablePValue<String> description();

    WritablePValue<String> parentPath();

    PValue<HWSolution> solution();

    PValue<HWSolutionFolder> parent();

}
