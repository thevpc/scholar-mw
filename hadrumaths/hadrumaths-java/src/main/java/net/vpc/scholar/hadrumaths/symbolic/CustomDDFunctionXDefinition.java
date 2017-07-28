package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

public class CustomDDFunctionXDefinition implements CustomFunctionDefinition{
    private String name;
    private CustomDDFunctionX eval;

    public CustomDDFunctionXDefinition(String name, CustomDDFunctionX eval) {
        this.name = name;
        this.eval = eval;
    }

    public String getName() {
        return name;
    }

    public CustomDDFunctionX getEval() {
        return eval;
    }

    public Expr apply(Expr expr){
        return new CustomDDFunctionXExpr(expr,this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomDDFunctionXDefinition that = (CustomDDFunctionXDefinition) o;

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
