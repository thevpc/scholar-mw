package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

public class CustomDCFunctionXYDefinition implements CustomFunctionDefinition {
    private static final long serialVersionUID = 1L;
    private String name;
    private CustomDCFunctionXY eval;

    public CustomDCFunctionXYDefinition(String name, CustomDCFunctionXY eval) {
        this.name = name;
        this.eval = eval;
    }

    public String getName() {
        return name;
    }

    public CustomDCFunctionXY getEval() {
        return eval;
    }

    public Expr apply(Expr xexpr, Expr yexpr) {
        return new CustomDCFunctionXYExpr(xexpr, yexpr, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomDCFunctionXYDefinition that = (CustomDCFunctionXYDefinition) o;

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
