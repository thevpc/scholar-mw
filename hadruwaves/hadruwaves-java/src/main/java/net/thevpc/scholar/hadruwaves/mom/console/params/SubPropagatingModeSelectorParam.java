package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.thevpc.scholar.hadruwaves.mom.str.zsfractalmodel.MomStructureFractalZop;

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
