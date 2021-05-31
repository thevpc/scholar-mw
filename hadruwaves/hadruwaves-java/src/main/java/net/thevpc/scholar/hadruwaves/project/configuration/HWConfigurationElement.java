package net.thevpc.scholar.hadruwaves.project.configuration;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.UserObjects;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadruwaves.project.HWProjectElement;

public interface HWConfigurationElement extends HWProjectElement {
    WritableString description();

    ObservableValue<HWConfigurationFolder> parent();

    ObservableValue<String> parentPath();

    UserObjects userObjects();
}
