/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ToCosXCosYRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ToCosXCosYRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Cos.class,Sin.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if(e instanceof Cos){
            Cos c=(Cos) e;
            Expr t = c.getArgument();
            RewriteResult rw = ruleset.rewrite(t);
            t= rw.getValue();
            Linear r=Linear.castOrConvert(t);
            if(r!=null){
                if(r.getA()==0){
                    return RewriteResult.newVal(new CosXCosY(1,0,0,r.getB(),r.getC(),(e).getDomain().intersect((t).getDomain())));
                }else if(r.getB()==0){
                    return RewriteResult.newVal(new CosXCosY(1,r.getA(),r.getC(),0,0,(e).getDomain().intersect((t).getDomain())));
                }else{
                    return RewriteResult.newVal(new CosXPlusY(1,r.getA(),r.getB(),r.getC(),(e).getDomain().intersect((t).getDomain())));
                }
            }
            if(rw.isRewritten()) {
                return RewriteResult.newVal(new Cos(t));
            }
        }else if(e instanceof Sin){
            Sin c=(Sin) e;
            Expr t = c.getArgument();
            RewriteResult rw = ruleset.rewrite(t);
            t= rw.getValue();
            Linear r=Linear.castOrConvert(t);
            if(r!=null){
                if(r.getA()==0){
                    return RewriteResult.newVal(new CosXCosY(1,0,0,r.getB(),r.getC()-Maths.PI/2,(e).getDomain().intersect(( t).getDomain())));
                }else if(r.getB()==0){
                    return RewriteResult.newVal(new CosXCosY(1,r.getA(),r.getC()-Maths.PI/2,0,0,(e).getDomain().intersect((t).getDomain())));
                }else{
                    return RewriteResult.newVal(new CosXPlusY(1,r.getA(),r.getA(),r.getC()-Maths.PI/2,(e).getDomain().intersect(( t).getDomain())));
                }
            }
            if(rw.isRewritten()) {
                return RewriteResult.newVal(new Sin(t));
            }
        }
        return RewriteResult.unmodified(e);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !obj.getClass().equals(getClass())){
            return false;
        }
        return true;
    }

}
