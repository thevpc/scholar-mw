/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.solver;

import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.common.props.WritablePList;
import net.vpc.common.props.WritablePList;
import net.vpc.scholar.hadruwaves.mom.CircuitType;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;
import net.vpc.scholar.hadruwaves.solvers.AbstractHWSolverTemplate;
import net.vpc.scholar.hadruwaves.solvers.HWSolver;

/**
 *
 * @author vpc
 */
public class HWSolverTemplateMoM extends AbstractHWSolverTemplate {

    private WritablePExpression<Integer> modesCount = Props2.of("modesCount").exprIntOf(1000);
    private WritablePExpression<Double> frequency = Props2.of("frequency").exprFreqOf(1.0);
    private WritablePExpression<CircuitType> circuitType = Props2.of("circuitType").exprEnumOf(CircuitType.class, CircuitType.SERIAL);
    private WritablePList<MomSolverTestTemplate> testFunctions = Props2.of("testFunctions").listOf(MomSolverTestTemplate.class);

    public HWSolverTemplateMoM() {
        name().set("Method of Moments Solver");
        description().set("Method of Moments Solver");
    }

    public WritablePExpression<CircuitType> circuitType() {
        return circuitType;
    }

    @Override
    public HWSolver eval(HWConfigurationRun configuration, ProgressMonitorFactory taskMonitorManager) {
        HWSolverMoM m = new HWSolverMoM(this, configuration,taskMonitorManager);
        m.build();
        return m;
    }

    public WritablePList<MomSolverTestTemplate> testFunctions() {
        return testFunctions;
    }

    public WritablePExpression<Integer> modesCount() {
        return modesCount;
    }

    public WritablePExpression<Double> frequency() {
        return frequency;
    }

}
