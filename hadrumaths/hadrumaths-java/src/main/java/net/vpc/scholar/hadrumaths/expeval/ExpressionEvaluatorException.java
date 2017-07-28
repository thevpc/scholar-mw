/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval;

/**
 *
 * @author vpc
 */
public class ExpressionEvaluatorException extends RuntimeException{

    public ExpressionEvaluatorException(Throwable cause) {
        super(cause);
    }

    public ExpressionEvaluatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionEvaluatorException(String message) {
        super(message);
    }

    public ExpressionEvaluatorException() {
    }

}
