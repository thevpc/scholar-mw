package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.AbstractFactory;
import net.vpc.scholar.hadruwaves.mom.sources.modal.PropagatingIndexModeComparator;
import net.vpc.scholar.hadruwaves.mom.sources.modal.UserModalSources;
import net.vpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

/**
 * Created by vpc on 2/6/17.
 */
public class ModeInfoComparatorFactory extends AbstractFactory {
    public static ModeInfoComparator createInitialIndexModeComparator() {
        return new InitialIndexModeComparator();
    }

    public static ModeInfoComparator createCutoffModeComparator() {
        return new CutoffModeComparator();
    }


    public static ModeInfoComparator createPropagatingIndexModeComparator(UserModalSources sources) {
        return new PropagatingIndexModeComparator(sources);
    }
}
