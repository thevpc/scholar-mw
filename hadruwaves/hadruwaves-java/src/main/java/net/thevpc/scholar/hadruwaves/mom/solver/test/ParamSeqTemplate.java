/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver.test;

import java.util.ArrayList;
import net.thevpc.common.props.WritableString;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;

/**
 *
 * @author vpc
 */
public class ParamSeqTemplate {

    private WritableString name = Props2.of("name").stringOf(null);
    private WritablePExpression<Expr> from = Props2.of("from").exprOf(Maths.expr(0));
    private WritablePExpression<Expr> to = Props2.of("to").exprOf(Maths.expr(0));
    private WritablePExpression<Expr> condition = Props2.of("condition").exprOf(Maths.expr(1));

    public ParamSeqTemplate() {
    }

    public ParamSeqTemplate(String name,String from,String to) {
        this(name,from,to,null);
    }
    
    public ParamSeqTemplate(String name,String from,String to,String cond) {
        this.name.set(name);
        this.from.set(from);
        this.to.set(to);
        this.condition.set(cond);
    }

    public WritableString name() {
        return name;
    }

    public WritablePExpression<Expr> from() {
        return from;
    }

    public WritablePExpression<Expr> to() {
        return to;
    }

    public WritablePExpression<Expr> condition() {
        return condition;
    }

    public BoundValue<Double>[] evalBoundValues(HWConfigurationRun configuration) {
        double from = configuration.evalDouble(this.from.get(), 0.0);
        double to = configuration.evalDouble(this.to.get(), from);
        double[] all = Maths.dsteps(from, to);
        java.util.List<BoundValue> ret = new ArrayList<BoundValue>();
        String c = condition.get();
        Expr condExpr = configuration.evalExpr(c, Maths.expr(1));
        String nn = name().get();
        for (int i = 0; i < all.length; i++) {
            double nv = all[i];
            if (condExpr.setParam(nn, i).simplify().toDouble() != 0) {
                ret.add(new BoundValue(nv, nn, nv));
            }
        }
        return ret.toArray(new BoundValue[0]);
    }

}
