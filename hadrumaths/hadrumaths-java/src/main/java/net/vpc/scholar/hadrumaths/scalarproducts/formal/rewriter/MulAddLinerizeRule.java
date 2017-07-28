/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.Mul;
import net.vpc.scholar.hadrumaths.symbolic.Plus;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vpc
 */
public class MulAddLinerizeRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new MulAddLinerizeRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Mul.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        Mul ee = (Mul) e;
        Expr r=null;
        for (Expr expression : ee.getSubExpressions()) {
            expression = ruleset.rewriteOrSame(expression);
            r=(r==null)?expression:mul(r,expression,ruleset);
        }
        if(r==null){
            throw new IllegalArgumentException("Unexpected");
        }
        if(r.equals(e)){
            return RewriteResult.unmodified(e);
        }
        return RewriteResult.newVal(r);
    }

    private Expr mul(Expr a, Expr b,ExpressionRewriter ruleset) {
        if (a instanceof Plus && b instanceof Plus) {
            Plus ap = ((Plus) a);
            Plus bp = ((Plus) b);
            List<Expr> n = new ArrayList<Expr>();
            List<Expr> apSimpl = new ArrayList<Expr>();
            for (Expr ee : ap.getSubExpressions()) {
                apSimpl.add(ruleset.rewriteOrSame(ee));
            }
                List<Expr> bpSimpl = new ArrayList<Expr>();
            for (Expr ee : bp.getSubExpressions()) {
                bpSimpl.add(ruleset.rewriteOrSame(ee));
            }

            for (Expr ta : apSimpl) {
                for (Expr tb : bpSimpl) {
                    n.add(new Mul(ta,tb));
                }
            }
            return new Plus(n.toArray(new Expr[n.size()]));
        }else if (a instanceof Plus) {
            Plus ap = ((Plus) a);
            List<Expr> n = new ArrayList<Expr>();
            for (Expr ta : ap.getSubExpressions()) {
                ta=ruleset.rewriteOrSame(ta);
                n.add(new Mul(ta, b));
            }
            return new Plus(n.toArray(new Expr[n.size()]));
        }else if(b instanceof Plus){
            Plus bp = ((Plus) b);
            List<Expr> n = new ArrayList<Expr>();
            for (Expr tb : bp.getSubExpressions()) {
                tb=ruleset.rewriteOrSame(tb);
                n.add(new Mul(a,tb));
            }
            return new Plus(n.toArray(new Expr[n.size()]));
        }
        return new Mul(a,b);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

}
