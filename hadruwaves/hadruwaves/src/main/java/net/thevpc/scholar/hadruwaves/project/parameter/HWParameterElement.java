package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.WritableString;
import net.thevpc.common.props.WritableValue;
import net.thevpc.nuts.elem.NToElement;
import net.thevpc.scholar.hadruwaves.project.HWProject;

public interface HWParameterElement extends NToElement {
    WritableString name();

    WritableString description();

    ObservableValue<HWProject> project();

    ObservableValue<HWParameterFolder> parent();

    ObservableValue<String> parentPath();

    void remove();
}
