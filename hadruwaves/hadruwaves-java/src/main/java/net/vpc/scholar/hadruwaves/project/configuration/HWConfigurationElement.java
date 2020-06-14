package net.vpc.scholar.hadruwaves.project.configuration;

import net.vpc.common.props.PValue;
import net.vpc.common.props.UserObjects;
import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadruwaves.project.HWProjectElement;

public interface HWConfigurationElement extends HWProjectElement {
    WritablePValue<String> description();

    PValue<HWConfigurationFolder> parent();

    PValue<String> parentPath();

    UserObjects userObjects();
}
