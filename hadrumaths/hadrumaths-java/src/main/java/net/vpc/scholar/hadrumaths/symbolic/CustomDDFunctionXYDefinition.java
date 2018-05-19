package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

public class CustomDDFunctionXYDefinition implements CustomFunctionDefinition {
    private static final long serialVersionUID = 1L;
    private String name;
    private CustomDDFunctionXY eval;

    public CustomDDFunctionXYDefinition(String name, CustomDDFunctionXY eval) {
        this.name = name;
        this.eval = eval;
    }

    public String getName() {
        return name;
    }

    public CustomDDFunctionXY getEval() {
        return eval;
    }

    public Expr apply(Expr xexpr, Expr yexpr) {
        return new CustomDDFunctionXYExpr(xexpr.toDD(), yexpr.toDD(), this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomDDFunctionXYDefinition that = (CustomDDFunctionXYDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return eval != null ? eval.equals(that.eval) : that.eval == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (eval != null ? eval.hashCode() : 0);
        return result;
    }
}
