package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.complex2complex.DefaultCustomCCFunctionXExpr;

public class CustomCCFunctionXDefinition implements CustomFunctionDefinition {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final CustomCCFunctionX eval;

    public CustomCCFunctionXDefinition(String name, CustomCCFunctionX eval) {
        this.name = name;
        this.eval = eval;
    }

    public String getName() {
        return name;
    }

    public CustomCCFunctionX getEval() {
        return eval;
    }

    public CustomCCFunctionXExpr fct() {
        return compose(Maths.X);
    }

    public CustomCCFunctionXExpr compose(Expr expr) {
        return new DefaultCustomCCFunctionXExpr(expr.toDC(), this);
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

        CustomCCFunctionXDefinition that = (CustomCCFunctionXDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return eval != null ? eval.equals(that.eval) : that.eval == null;
    }
}
