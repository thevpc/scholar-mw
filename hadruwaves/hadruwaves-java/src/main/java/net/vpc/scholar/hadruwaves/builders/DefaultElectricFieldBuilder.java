package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.ApertureType;
import net.vpc.scholar.hadruwaves.mom.DefaultElectricFieldCartesianBuilder;
import net.vpc.scholar.hadruwaves.mom.DefaultElectricFieldSphericalBuilder;
import net.vpc.scholar.hadruwaves.mom.ElectricFieldPart;
import net.vpc.scholar.hadruwaves.str.MWStructure;

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
        if (getMonitor() != null) {
            p.monitor(getMonitor());
        }
        if (getConvergenceEvaluator() != null) {
            p.converge(getConvergenceEvaluator());
        }
        return p;
    }

    @Override
    public ElectricFieldCartesianBuilder cartesian() {
        DefaultElectricFieldCartesianBuilder p = new DefaultElectricFieldCartesianBuilder(getStructure(), part);
        if (getMonitor() != null) {
            p.monitor(getMonitor());
        }
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
    public ElectricFieldBuilder monitor(TaskMonitorManager monitor) {
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
