package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.ApertureType;
import net.thevpc.scholar.hadruwaves.mom.DefaultElectricFieldCartesianBuilder;
import net.thevpc.scholar.hadruwaves.mom.DefaultElectricFieldSphericalBuilder;
import net.thevpc.scholar.hadruwaves.mom.ElectricFieldPart;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

public class DefaultElectricFieldBuilder extends AbstractValueBuilder implements ElectricFieldBuilder {
    private ElectricFieldPart part = ElectricFieldPart.FULL;
    private ApertureType apertureType = ApertureType.PEC;

    public DefaultElectricFieldBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public ElectricFieldBuilder electricPart(ElectricFieldPart p) {
        if (p == null) {
            this.part = ElectricFieldPart.FULL;
        } else {
            this.part = p;
        }
        return this;
    }

    @Override
    public ElectricFieldBuilder electricPart(ApertureType p) {
        if (p == null) {
            this.apertureType = ApertureType.PEC;
        } else {
            this.apertureType = p;
        }
        return this;
    }

    @Override
    public ElectricFieldSphericalBuilder spherical() {
        DefaultElectricFieldSphericalBuilder p = new DefaultElectricFieldSphericalBuilder(getStructure(), part, apertureType);
        p.monitor(getMonitor());
        if (getConvergenceEvaluator() != null) {
            p.converge(getConvergenceEvaluator());
        }
        return p;
    }

    @Override
    public ElectricFieldCartesianBuilder cartesian() {
        DefaultElectricFieldCartesianBuilder p = new DefaultElectricFieldCartesianBuilder(getStructure(), part);
        p.monitor(getMonitor());
        if (getConvergenceEvaluator() != null) {
            p.converge(getConvergenceEvaluator());
        }
        return p;
    }

    @Override
    public ElectricFieldBuilder monitor(ProgressMonitor monitor) {
        return (ElectricFieldBuilder) super.monitor(monitor);
    }

    @Override
    public ElectricFieldBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
        return (ElectricFieldBuilder) super.monitor(monitor);
    }

    @Override
    public ElectricFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (ElectricFieldBuilder) super.converge(convergenceEvaluator);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
