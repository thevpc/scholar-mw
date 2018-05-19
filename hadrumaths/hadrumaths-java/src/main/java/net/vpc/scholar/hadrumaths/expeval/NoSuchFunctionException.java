/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval;

/**
 * @author vpc
 */
public class NoSuchFunctionException extends ExpressionEvaluatorException {
    private String functionName;

    public NoSuchFunctionException(String functionName) {
        super("Function " + functionName + " not found");
        this.functionName = functionName;
    }

    public String getVarName() {
        return functionName;
    }


}
