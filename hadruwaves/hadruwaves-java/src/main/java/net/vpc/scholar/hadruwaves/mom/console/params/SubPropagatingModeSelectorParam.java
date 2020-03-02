package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel.MomStructureFractalZop;

public class SubPropagatingModeSelectorParam extends AbstractCParam implements Cloneable {


    public SubPropagatingModeSelectorParam() {
        super("SubNbProp");
    }

    @Override
    public void configure(Object source, Object value) {
        if (source instanceof MomStructureFractalZop) {
            MomStructureFractalZop z=(MomStructureFractalZop) source;
            z.setSubPropagatingModeSelector((ModalSources) value);
        }
    }
}
