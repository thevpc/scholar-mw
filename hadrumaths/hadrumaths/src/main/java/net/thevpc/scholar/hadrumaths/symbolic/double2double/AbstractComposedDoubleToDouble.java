package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.thevpc.scholar.hadrumaths.symbolic.ExprDefaults;

@IgnoreRandomGeneration
public abstract class AbstractComposedDoubleToDouble extends AbstractDoubleToDouble {
    @Override
    public boolean isInvariant(Axis axis) {
        return ExprDefaults.isInvariantAll(getChildren(), axis);
    }

}
