package net.vpc.scholar.hadrumaths.symbolic.old;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 20 juil. 2007 08:04:33
 */
public class SymStringContext implements Cloneable{
    public static final int ADD_PRECEDENCE=1;
    public static final int MUL_PRECEDENCE=2;
    public static final int POW_PRECEDENCE=3;
    public static final int NO_PRECEDENCE=-1;
    public static final int UNIT_MINUS_PRECEDENCE=4;
    private int operatorPrecedence;
    private boolean preferParentheses;

    public SymStringContext(int operatorPrecedence, boolean preferParentheses) {
        this.operatorPrecedence = operatorPrecedence;
        this.preferParentheses = preferParentheses;
    }

    public int getOperatorPrecedence() {
        return operatorPrecedence;
    }

    public void setOperatorPrecedence(int operatorPrecedence) {
        this.operatorPrecedence = operatorPrecedence;
    }

    public boolean isPreferParentheses(int precedence) {
        return isPreferParentheses() || getOperatorPrecedence() > precedence;
    }
    
    public boolean isPreferParentheses() {
        return preferParentheses;
    }

    public void setPreferParentheses(boolean preferParentheses) {
        this.preferParentheses = preferParentheses;
    }

    public SymStringContext clone(){
        try {
            return (SymStringContext) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
