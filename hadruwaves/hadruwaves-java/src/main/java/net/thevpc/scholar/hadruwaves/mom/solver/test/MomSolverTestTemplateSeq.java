/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.thevpc.common.props.PMapEntry;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePLMap;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.mom.solver.AbstractMomSolverTestTemplate;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverMoM;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;

/**
 *
 * @author vpc
 */
public class MomSolverTestTemplateSeq extends AbstractMomSolverTestTemplate {

    private final WritablePExpression<Expr> expr = Props2.of("expr").exprOf(Maths.expr(0));
    private final WritablePLMap<String, ParamSeqTemplate> params = Props.of("params").lmapOf(String.class, ParamSeqTemplate.class, x -> x.name().get());
    private final WritablePExpression<Expr> condition = Props2.of("condition").exprOf(Maths.expr(1));

    public MomSolverTestTemplateSeq() {
    }

    public MomSolverTestTemplateSeq(String name, String expr) {
        this.name().set(name);
        this.expr().set(expr);
    }

    public WritablePExpression<Expr> expr() {
        return expr;
    }

    public WritablePLMap<String, ParamSeqTemplate> params() {
        return params;
    }

    public WritablePExpression<Expr> condition() {
        return condition;
    }

    @Override
    public Expr[] generate(HWSolverMoM solver) {

        List<BoundValue<Double>> a = new ArrayList<>();
        for (PMapEntry<String, ParamSeqTemplate> param : params()) {
            BoundValue<Double>[] next = param.getValue().evalBoundValues(solver.configuration());
            if (a.isEmpty()) {
                a.addAll(Arrays.asList(next));
            } else {
                List<BoundValue<Double>> b = new ArrayList<>();
                for (BoundValue<Double> a1 : a) {
                    for (BoundValue<Double> n1 : next) {
                        b.add(a1.cross(n1));
                    }
                }
                a = b;
            }
        }
        List<Expr> ret = new ArrayList<Expr>();
        Expr expr = solver.configuration().evalExpr(expr().get(), Maths.expr(1));
        Expr cond = solver.configuration().evalExpr(condition().get(), Maths.expr(1));
        for (BoundValue<Double> b : a) {
            boolean cond2 = setParams(cond, b.getParamValues()).simplify().toBoolean();
            if (cond2) {
                ret.add(setParams(expr, b.getParamValues()));
            }
        }
        return ret.toArray(new Expr[0]);
    }

    private Expr setParams(Expr e, Map<String, Double> paramValues) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : paramValues.entrySet()) {
            e = e.setParam(entry.getKey(), entry.getValue());
            if(sb.length()>0){
                sb.append(",");
            }
            sb.append(entry.getKey()).append("=").append(((Number) (entry.getValue())).intValue());
        }
        if(sb.length()>0){
            sb.insert(0,"(");
            sb.append(")");
        }
        e=e.title(name().get() + sb.toString());
        return e;
    }

}
