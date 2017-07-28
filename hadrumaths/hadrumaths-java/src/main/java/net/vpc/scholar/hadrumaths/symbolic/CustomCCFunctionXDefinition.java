package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

public class CustomCCFunctionXDefinition implements CustomFunctionDefinition{
    private String name;
    private CustomCCFunctionX eval;

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

    public Expr apply(Expr expr){
        return new CustomCCFunctionXExpr(expr,this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomCCFunctionXDefinition that = (CustomCCFunctionXDefinition) o;

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
