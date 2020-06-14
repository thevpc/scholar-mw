/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.project;

/**
 *
 * @author vpc
 */
public class EvalExprSuccess extends EvalExprResult {
    
    public EvalExprSuccess(String expression, String formattedString, Object value, Object unitAwareValue, boolean hasUnit) {
        super(expression, formattedString, value, unitAwareValue, hasUnit, false, null);
    }
    
}
