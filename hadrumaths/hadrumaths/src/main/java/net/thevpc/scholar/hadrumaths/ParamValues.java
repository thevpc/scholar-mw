package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.Param;

import java.util.Set;

public interface ParamValues {
    static ParamValues empty() {
        return new DefaultParamValues();
    }

    static ParamValues of(String name, double e) {
        return new DefaultParamValues().set(name, e);
    }

    static ParamValues of(String name, Complex e) {
        return new DefaultParamValues().set(name, e);
    }

    static ParamValues of(String name, Expr e) {
        return new DefaultParamValues().set(name, e);
    }

    static ParamValues of(String n1, Expr e1, String n2, Expr e2) {
        return new DefaultParamValues().set(n1, e1).set(n2, e2);
    }

    ParamValues set(String name, Expr d);

    static ParamValues of(String n1, Expr e1, String n2, Expr e2, String n3, Expr e3) {
        return new DefaultParamValues().set(n1, e1).set(n2, e2).set(n3, e3);
    }

    static ParamValues of(String n1, Expr e1, String n2, Expr e2, String n3, Expr e3, String n4, Expr e4) {
        return new DefaultParamValues().set(n1, e1).set(n2, e2).set(n3, e3).set(n4, e4);
    }

    static ParamValues of(String n1, Expr e1, String n2, Expr e2, String n3, Expr e3, String n4, Expr e4, String n5, Expr e5) {
        return new DefaultParamValues().set(n1, e1).set(n2, e2).set(n3, e3).set(n4, e4).set(n5, e5);
    }

    static ParamValues of(Param name, double e) {
        return new DefaultParamValues().set(name, e);
    }

    static ParamValues of(Param name, Complex e) {
        return new DefaultParamValues().set(name, e);
    }

    static ParamValues of(Param name, Expr e) {
        return new DefaultParamValues().set(name, e);
    }

    ParamValues set(Param name, Expr d);

    static ParamValues of(Param n1, Expr e1, Param n2, Expr e2) {
        return new DefaultParamValues().set(n1, e1).set(n2, e2);
    }

    static ParamValues of(Param n1, Expr e1, Param n2, Expr e2, Param n3, Expr e3) {
        return new DefaultParamValues().set(n1, e1).set(n2, e2).set(n3, e3);
    }

    static ParamValues of(Param n1, Expr e1, Param n2, Expr e2, Param n3, Expr e3, Param n4, Expr e4) {
        return new DefaultParamValues().set(n1, e1).set(n2, e2).set(n3, e3).set(n4, e4);
    }

    static ParamValues of(Param n1, Expr e1, Param n2, Expr e2, Param n3, Expr e3, Param n4, Expr e4, Param n5, Expr e5) {
        return new DefaultParamValues().set(n1, e1).set(n2, e2).set(n3, e3).set(n4, e4).set(n5, e5);
    }

    ParamValues set(String name, double d);

    ParamValues set(Param p, double d);

    ParamValues set(Param p, Complex d);

    ParamValues set(String name, Complex d);

    ParamValues remove(String name);

    Set<Param> getParams();

    boolean contains(String name);

    Expr getValue(String name);
}
