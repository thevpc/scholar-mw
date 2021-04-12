package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.WritableValue;
import net.thevpc.tson.TsonSerializable;

public interface HWSolutionElement extends TsonSerializable{
    WritableValue<String> name();

    WritableValue<String> description();

    WritableValue<String> parentPath();

    ObservableValue<HWSolution> solution();

    ObservableValue<HWSolutionFolder> parent();

}
