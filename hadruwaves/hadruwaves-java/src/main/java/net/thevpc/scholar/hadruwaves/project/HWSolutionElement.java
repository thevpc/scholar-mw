package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.WritableString;
import net.thevpc.common.props.WritableValue;
import net.thevpc.tson.TsonSerializable;

public interface HWSolutionElement extends TsonSerializable{
    WritableString name();

    WritableString description();

    WritableString parentPath();

    ObservableValue<HWSolution> solution();

    ObservableValue<HWSolutionFolder> parent();

}
