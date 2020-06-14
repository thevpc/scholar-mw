/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.double2double.CosXPlusY;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo.Cos;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo.Sin;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * @author vpc
 */
public class ToCosXCosYRule extends AbstractExpressionRewriterRule {
    public static final ExpressionRewriterRule INSTANCE = new ToCosXCosYRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Cos.class, Sin.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (e instanceof Cos) {
            Cos c = (Cos) e;
            Expr t = c.getChild(0);
            RewriteResult rw = ruleset.rewrite(t, null);
            t = rw.isUnmodified() ? t : rw.getValue();
            Linear r = Linear.castOrConvert(t);
            if (r != null) {
                if (r.getA() == 0) {
                    //amp * cos(a*X+b) * cos(c*Y+d)
                    //amp * cos(b) * cos(c*Y+d)
                    if (r.getB() == 0) {
                        return RewriteResult.bestEffort(Maths.expr(Maths.cos(r.getC()), e.getDomain().intersect((t).getDomain())));
                    }
                    return RewriteResult.bestEffort(new CosXCosY(1, 0, 0, r.getB(), r.getC(), (e).getDomain().intersect((t).getDomain())));
                } else if (r.getB() == 0) {
                    return RewriteResult.bestEffort(new CosXCosY(1, r.getA(), r.getC(), 0, 0, (e).getDomain().intersect((t).getDomain())));
                } else {
                    return RewriteResult.bestEffort(new CosXPlusY(1, r.getA(), r.getB(), r.getC(), (e).getDomain().intersect((t).getDomain())));
                }
            }
            if (rw.isRewritten()) {
                return RewriteResult.bestEffort(cos(t));
            }
        } else if (e instanceof Sin) {
            Sin c = (Sin) e;
            Expr t = c.getChild(0);
            RewriteResult rw = ruleset.rewrite(t, null);
            t = rw.isUnmodified() ? t : rw.getValue();
            Linear r = Linear.castOrConvert(t);
            if (r != null) {
                if (r.getA() == 0) {
                    if (r.getB() == 0) {
                        return RewriteResult.bestEffort(Maths.expr(Maths.sin(r.getC()), e.getDomain().intersect((t).getDomain())));
                    }
                    return RewriteResult.bestEffort(new CosXCosY(1, 0, 0, r.getB(), r.getC() - PI / 2, (e).getDomain().intersect((t).getDomain())));
                } else if (r.getB() == 0) {
                    return RewriteResult.bestEffort(new CosXCosY(1, r.getA(), r.getC() - PI / 2, 0, 0, (e).getDomain().intersect((t).getDomain())));
                } else {
                    return RewriteResult.bestEffort(new CosXPlusY(1, r.getA(), r.getA(), r.getC() - PI / 2, (e).getDomain().intersect((t).getDomain())));
                }
            }
            if (rw.isRewritten()) {
                return RewriteResult.bestEffort(sin(t));
            }
        }
        return RewriteResult.unmodified();
    }


}
