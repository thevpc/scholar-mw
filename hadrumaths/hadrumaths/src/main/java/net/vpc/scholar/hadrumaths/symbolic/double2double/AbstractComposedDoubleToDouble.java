package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;

@IgnoreRandomGeneration
public abstract class AbstractComposedDoubleToDouble extends AbstractDoubleToDouble {
    @Override
    public boolean isInvariant(Axis axis) {
        return ExprDefaults.isInvariantAll(getChildren(), axis);
    }

}
