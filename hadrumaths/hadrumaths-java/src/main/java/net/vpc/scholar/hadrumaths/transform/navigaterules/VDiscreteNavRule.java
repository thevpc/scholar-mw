/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class VDiscreteNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new VDiscreteNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{VDiscrete.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        VDiscrete ee=(VDiscrete)e;
        int length = ee.getComponentDimension().rows;
        Expr[] updated = new Expr[length];
        int bestEfforts=0;
        boolean changed = false;
        for (int i = 0; i < updated.length; i++) {
            Expr s1 = ee.getComponent(Axis.values()[i]);
            RewriteResult s2 = ruleset.rewrite(s1);
            if (s2!=null) {
                changed = true;
                updated[i] = s2.getValue();
                if(s2.isBestEffort()){
                    bestEfforts++;
                }
            }else{
                bestEfforts++;
                updated[i] = s1;
            }
        }
        if (changed) {
            Expr e2=null;
            switch (length){
                case 1:{
                    e2 = new VDiscrete((Discrete) updated[0]);
                    break;
                }
                case 2:{
                    e2 = new VDiscrete((Discrete) updated[0], (Discrete) updated[1]);
                    break;
                }
                case 3:{
                    e2 = new VDiscrete((Discrete) updated[0], (Discrete) updated[1],(Discrete) updated[2]);
                    break;
                }
            }
            e2= Any.copyProperties(e, e2);
            return bestEfforts==length? RewriteResult.bestEffort(e2) : RewriteResult.newVal(e2);
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
