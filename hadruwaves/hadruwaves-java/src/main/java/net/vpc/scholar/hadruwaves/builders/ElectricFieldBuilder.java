package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.ApertureType;
import net.vpc.scholar.hadruwaves.mom.ElectricFieldPart;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ElectricFieldBuilder extends ValueBuilder {
    ElectricFieldBuilder monitor(TaskMonitorManager monitor);

    ElectricFieldBuilder electricPart(ElectricFieldPart p);

    ElectricFieldBuilder electricPart(ApertureType p);

    ElectricFieldBuilder monitor(ProgressMonitor monitor);

    ElectricFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ElectricFieldSphericalBuilder spherical();

    ElectricFieldCartesianBuilder cartesian();


}
