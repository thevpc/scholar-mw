package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.complex2complex.DefaultCustomCCFunctionXYExpr;

public class CustomCCFunctionXYDefinition implements CustomFunctionDefinition {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final FunctionCCXY eval;

    public CustomCCFunctionXYDefinition(String name, FunctionCCXY eval) {
        this.name = name;
        this.eval = eval;
    }

    public String getName() {
        return name;
    }

    public FunctionCCXY getEval() {
        return eval;
    }

    public CustomCCFunctionXYExpr fct() {
        return compose(Maths.X, Maths.Y);
    }

    public CustomCCFunctionXYExpr compose(Expr xexpr, Expr yexpr) {
        return new DefaultCustomCCFunctionXYExpr(xexpr.toDC(), yexpr.toDC(), this);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (eval != null ? eval.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomCCFunctionXYDefinition that = (CustomCCFunctionXYDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return eval != null ? eval.equals(that.eval) : that.eval == null;
    }
}
