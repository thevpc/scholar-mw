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
public class EvalExprError extends EvalExprResult {
    
    public EvalExprError(String expression, String errorMessage) {
        super(expression, null, null, null, false, true, errorMessage);
    }
    
}
