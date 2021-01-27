package net.thevpc.scholar.hadruwaves.project.configuration;

import net.thevpc.common.props.PValue;
import net.thevpc.common.props.UserObjects;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.scholar.hadruwaves.project.HWProjectElement;

public interface HWConfigurationElement extends HWProjectElement {
    WritablePValue<String> description();

    PValue<HWConfigurationFolder> parent();

    PValue<String> parentPath();

    UserObjects userObjects();
}
