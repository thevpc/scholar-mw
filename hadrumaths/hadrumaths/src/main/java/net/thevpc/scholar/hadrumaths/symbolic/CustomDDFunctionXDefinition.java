package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DefaultCustomDDFunctionXExpr;

public class CustomDDFunctionXDefinition implements CustomFunctionDefinition {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final FunctionDDX eval;

    public CustomDDFunctionXDefinition(String name, FunctionDDX eval) {
        this.name = name;
        this.eval = eval;
    }

    public String getName() {
        return name;
    }

    public FunctionDDX getEval() {
        return eval;
    }

    public CustomDDFunctionXExpr fct() {
        return compose(Maths.X);
    }

    public CustomDDFunctionXExpr compose(Expr expr) {
        return new DefaultCustomDDFunctionXExpr(expr.toDD(), this);
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

        CustomDDFunctionXDefinition that = (CustomDDFunctionXDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return eval != null ? eval.equals(that.eval) : that.eval == null;
    }
}
