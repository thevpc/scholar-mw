/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.solver.test;

import java.util.ArrayList;
import java.util.List;
import net.vpc.common.props.WritablePList;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadruwaves.mom.solver.AbstractMomSolverTestTemplate;
import net.vpc.scholar.hadruwaves.mom.solver.HWSolverMoM;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

/**
 *
 * @author vpc
 */
public class MomSolverTestTemplateList extends AbstractMomSolverTestTemplate {

    private final WritablePList<WritablePExpression<Expr>> expressions = Props2.of("expressions").listOf((Class) WritablePList.class);
    private final WritablePExpression<Integer> complexity = Props2.of("complexity").exprIntOf(0);

    public WritablePExpression<Integer> complexity() {
        return complexity;
    }

    public WritablePList<WritablePExpression<Expr>> expressions() {
        return expressions;
    }

    @Override
    public Expr[] generate(HWSolverMoM solver) {
        HWConfigurationRun configuration = solver.configuration();
        int max = ((Number)complexity.eval(configuration)).intValue();
        if (max <= 0) {
            max = expressions.size();
        }
        if (max > expressions.size()) {
            max = expressions.size();
        }
        int index = 0;
        List<Expr> all = new ArrayList<Expr>();
        for (WritablePExpression<Expr> expression : expressions) {
            if (index >= max) {
                break;
            }
            all.add(expression.eval(configuration));
            index++;
        }
        return all.toArray(new Expr[0]);
    }

}
