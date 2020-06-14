/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.conv.Imag;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Div;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Minus;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vpc
 */
public class ImagSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ImagSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Imag.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        Imag ee = (Imag) e;
        RewriteResult rbase = ruleset.rewrite(ee.getArg(), targetExprType);
//        if(rbase instanceof Imag){
//            return rbase;
//        }
        Expr value = rbase.isUnmodified() ? ee.getArg() : rbase.getValue();
        Expr ac0 = Expressions.toConstantExprOrNull(value);
        if (ac0 != null) {
            return RewriteResult.bestEffort(new DefaultComplexValue(Complex.of(ac0.toComplex().getImag()), ac0.getDomain()));
        }
        switch (value.getType()) {
            case DOUBLE_DOUBLE: {
                return RewriteResult.bestEffort(Maths.DZERO(value.getDomain().getDimension()));
            }
        }
        if (Maths.isReal(value)) {
            return RewriteResult.bestEffort(Maths.DZEROXY);
        }
        if (value instanceof Mul) {
            return RewriteResult.newVal(RealSimplifyRule.getMulRealImag((Mul) value)[1]);
        }
        if (value instanceof Plus) {
            List<Expr> e1 = value.getChildren();
            List<Expr> e2 = new ArrayList<Expr>();
            for (Expr expr : e1) {
                e2.add(Maths.imag(expr));
            }
            return RewriteResult.newVal(Maths.sum(e2.toArray(new Expr[0])));
        }
        if (value instanceof Minus) {
            return RewriteResult.newVal(Minus.of(Maths.imag(value.getChild(0)), Maths.imag(value.getChild(1))));
        }
        if (value instanceof Div) {
            Expr first = ((Div) value).getFirst();
            Expr second = ((Div) value).getSecond();
            if (first.isNarrow(ExprType.DOUBLE_COMPLEX) && second.isNarrow(ExprType.DOUBLE_COMPLEX)) {
                DoubleToComplex a = first.toDC();
                DoubleToComplex b = second.toDC();
                boolean aReal = a.getImagDD().isZero();
                boolean aImag = a.getRealDD().isZero();
                boolean bReal = b.getImagDD().isZero();
                boolean bImag = b.getRealDD().isZero();
                if ((aReal && bReal) || (aImag && bImag)) {
                    return RewriteResult.bestEffort(Maths.DZEROXY);
                } else if ((aReal && bImag) || (bReal && aImag)) {
                    return rbase;
                }
            }
        }
        DoubleToDouble r = value.toDC().getImagDD();
        if (!(r instanceof Imag)) {
            return RewriteResult.newVal(r);
        }
//        if(rbase instanceof Inv){
//            Expr a = ((Inv) rbase).getChild(0);
//            if(Maths.isReal(a)){
//                return rbase;
//            }
//        }
        return RewriteResult.unmodified();
    }


}
