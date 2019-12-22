package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

public class CustomDCFunctionXDefinition implements CustomFunctionDefinition {
    private static final long serialVersionUID = 1L;
    private String name;
    private CustomDCFunctionX eval;

    public CustomDCFunctionXDefinition(String name, CustomDCFunctionX eval) {
        this.name = name;
        this.eval = eval;
    }

    public String getName() {
        return name;
    }

    public CustomDCFunctionX getEval() {
        return eval;
    }

    public CustomDCFunctionXExpr fct() {
        return compose(MathsBase.X);
    }

    public CustomDCFunctionXExpr compose(Expr expr) {
        return new DefaultCustomDCFunctionXExpr(expr, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomDCFunctionXDefinition that = (CustomDCFunctionXDefinition) o;

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
