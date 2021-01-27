/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Expressions;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.FunctionExpr;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Conj;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.List;

/**
 * @author vpc
 */
public class FunctionExprSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new FunctionExprSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{FunctionExpr.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {

        List<Expr> oldChildren = e.getChildren();
        if (oldChildren.isEmpty()) {
            return RewriteResult.unmodified();
        }
        Expr[] newChildren = new Expr[oldChildren.size()];
        boolean someUpdates = false;
        boolean constants = true;
        for (int i = 0; i < newChildren.length; i++) {
            Expr c = oldChildren.get(i);
            RewriteResult r = ruleset.rewrite(c, targetExprType);
            if (r.isRewritten()) {
                someUpdates = true;
                newChildren[i] = r.getValue();
            } else {
                newChildren[i] = c;
            }
            if (constants) {
                if (Expressions.toConstantExprOrNull(newChildren[i]) == null) {
                    constants = false;
                }
            }
        }
        if (constants) {
            switch (e.getType()) {
                case DOUBLE_NBR:
                case DOUBLE_EXPR:
                case DOUBLE_DOUBLE: {
                    DoubleToDouble fct = (DoubleToDouble) e;
                    switch (fct.domain().dimension()) {
                        case 1:
                            return RewriteResult.bestEffort(Maths.expr(fct.evalDouble(fct.domain().xvalue()), fct.getDomain()));
                        case 2:
                            return RewriteResult.bestEffort(Maths.expr(fct.evalDouble(fct.domain().xvalue(), fct.domain().yvalue()), fct.getDomain()));
                        case 3:
                            return RewriteResult.bestEffort(Maths.expr(fct.evalDouble(fct.domain().xvalue(), fct.domain().yvalue(), fct.domain().zvalue()), fct.getDomain()));
                    }
                    break;
                }
                case COMPLEX_NBR:
                case COMPLEX_EXPR:
                case DOUBLE_COMPLEX: {
                    DoubleToComplex fct = (DoubleToComplex) e;
                    switch (fct.domain().dimension()) {
                        case 1:
                            return RewriteResult.bestEffort(Maths.expr(fct.evalComplex(fct.domain().xvalue()), fct.getDomain()));
                        case 2:
                            return RewriteResult.bestEffort(Maths.expr(fct.evalComplex(fct.domain().xvalue(), fct.domain().yvalue()), fct.getDomain()));
                        case 3:
                            return RewriteResult.bestEffort(Maths.expr(fct.evalComplex(fct.domain().xvalue(), fct.domain().yvalue(), fct.domain().zvalue()), fct.getDomain()));
                    }
                    break;
                }
//                case CVECTOR_NBR:
//                case CVECTOR_EXPR:
//                case DOUBLE_CVECTOR: {
//                switch (fct.domain().dimension()) {
//                    switch (oldChildren.size()) {
//                        case 1:
//                            return RewriteResult.bestEffort(Maths.expr(fct.computeVector(newChildren[0].toDouble())),fct.getDomain()));
//                        case 2:
//                            return RewriteResult.bestEffort(Maths.expr(fct.computeVector(newChildren[0].toDouble(), newChildren[1].toDouble())),fct.getDomain()));
//                        case 3:
//                            return RewriteResult.bestEffort(Maths.expr(fct.computeVector(fct.domain().xvalue(), fct.domain().yvalue(), fct.domain().zvalue())),fct.getDomain()));
//                    }
//                    break;
//                }
//                case CMATRIX_NBR:
//                case CMATRIX_EXPR:
//                case DOUBLE_CMATRIX: {
//                    DoubleToMatrix fct = (DoubleToMatrix) e;
//                switch (fct.domain().dimension()) {
//                        case 1:
//                            return RewriteResult.bestEffort(Maths.expr(fct.computeMatrix(newChildren[0].toDouble())),fct.getDomain()));
//                        case 2:
//                            return RewriteResult.bestEffort(Maths.expr(fct.computeMatrix(newChildren[0].toDouble(), newChildren[1].toDouble())),fct.getDomain()));
//                        case 3:
//                            return RewriteResult.bestEffort(Maths.expr(fct.computeMatrix(fct.domain().xvalue(), fct.domain().yvalue(), fct.domain().zvalue())),fct.getDomain()));
//                    }
//                    break;
//                }
//                case COMPLEX_COMPLEX:{
//                    ComplexToComplex fct = (ComplexToComplex) e;
//                switch (fct.domain().dimension()) {
//                        case 1:
//                            return RewriteResult.bestEffort(fct.computeComplex(newChildren[0].toDouble()));
//                        case 2:
//                            return RewriteResult.bestEffort(fct.computeComplex(newChildren[0].toDouble(), newChildren[1].toDouble()));
//                        case 3:
//                            return RewriteResult.bestEffort(fct.computeComplex(fct.domain().xvalue(), fct.domain().yvalue(), fct.domain().zvalue()),fct.getDomain()));
//                    }
//                    break;
//                }
//                case COMPLEX_CVECTOR:{
//                    ComplexToVector fct = (ComplexToVector) e;
//                switch (fct.domain().dimension()) {
//                        case 1:
//                            return RewriteResult.bestEffort(fct.computeVector(newChildren[0].toDouble()));
//                        case 2:
//                            return RewriteResult.bestEffort(fct.computeVector(newChildren[0].toDouble(), newChildren[1].toDouble()));
//                        case 3:
//                            return RewriteResult.bestEffort(fct.computeVector(fct.domain().xvalue(), fct.domain().yvalue(), fct.domain().zvalue()));
//                    }
//                    break;
//                }
//                case COMPLEX_CMATRIX:{
//                    ComplexToMatrix fct = (ComplexToMatrix) e;
//                switch (fct.domain().dimension()) {
//                        case 1:
//                            return RewriteResult.bestEffort(fct.computeMatrix(newChildren[0].toDouble()));
//                        case 2:
//                            return RewriteResult.bestEffort(fct.computeMatrix(newChildren[0].toDouble(), newChildren[1].toDouble()));
//                        case 3:
//                            return RewriteResult.bestEffort(fct.computeMatrix(fct.domain().xvalue(), fct.domain().yvalue(), fct.domain().zvalue()));
//                    }
//                    break;
//                }
            }
        }
        if(e instanceof Conj){
            switch (e.getType()){
                case DOUBLE_NBR:
                case DOUBLE_EXPR:
                case DOUBLE_DOUBLE:{
                    return RewriteResult.bestEffort(newChildren[0]);
                }
            }
        }
        if (!someUpdates) {
            return RewriteResult.unmodified();
        }
        Expr u = e.newInstance(newChildren);
        if(u.equals(e)){
            return RewriteResult.unmodified();
        }
        return RewriteResult.bestEffort(u);
    }


}
