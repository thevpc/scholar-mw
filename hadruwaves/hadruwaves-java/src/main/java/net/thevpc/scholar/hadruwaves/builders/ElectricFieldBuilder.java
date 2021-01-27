package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.ApertureType;
import net.thevpc.scholar.hadruwaves.mom.ElectricFieldPart;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ElectricFieldBuilder extends ValueBuilder {
    ElectricFieldBuilder monitor(ProgressMonitorFactory monitor);

    ElectricFieldBuilder electricPart(ElectricFieldPart p);

    ElectricFieldBuilder electricPart(ApertureType p);

    ElectricFieldBuilder monitor(ProgressMonitor monitor);

    ElectricFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ElectricFieldSphericalBuilder spherical();

    ElectricFieldCartesianBuilder cartesian();


}
