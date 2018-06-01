package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.ReflectUtils;
import net.vpc.common.util.TypeReference;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class MathsExpr {
    public static TVector<Expr> edotmul(TVector<Expr>... arr) {
        TypeReference cls = arr[0].getComponentType();
        for (int i = 0; i < arr.length; i++) {
            cls = ReflectUtils.lowestCommonAncestor(cls, arr[i].getComponentType());
        }
        VectorSpace<Expr> componentVectorSpace = Maths.getVectorSpace(cls);
        return new ReadOnlyTVector<>(arr[0].getComponentType(), arr[0].isRow(), new TVectorModel<Expr>() {
            @Override
            public int size() {
                return arr[0].size();
            }

            @Override
            public Expr get(int index) {
                Expr e = arr[0].get(index);
                for (int i = 1; i < arr.length; i++) {
                    TVector<Expr> v = arr[i];
                    e = componentVectorSpace.mul(e, v.get(index));
                }
                return e;
            }
        });
    }

    public static TVector<Expr> edotdiv(TVector<Expr>... arr) {
        VectorSpace<Expr> componentVectorSpace = arr[0].getComponentVectorSpace();
        return new ReadOnlyTVector<>(arr[0].getComponentType(), arr[0].isRow(), new TVectorModel<Expr>() {
            @Override
            public int size() {
                return arr[0].size();
            }

            @Override
            public Expr get(int index) {
                Expr e = arr[0].get(index);
                for (int i = 1; i < arr.length; i++) {
                    TVector<Expr> v = arr[i];
                    e = componentVectorSpace.div(e, v.get(index));
                }
                return e;
            }
        });
    }

    public static Complex cmul(TVectorModel<Complex> arr) {
        int len = arr.size();
        if (len == 0) {
            return Maths.CZERO;
        }
        MutableComplex c = new MutableComplex(1, 0);
        for (int i = 0; i < len; i++) {
            Complex complex = arr.get(i);
            if (complex.isZero()) {
                return Maths.CZERO;
            }
            c.mul(complex);
        }
        return c.toComplex();
    }

    public static Expr emul(TVectorModel<Expr> arr) {
        int len = arr.size();
        if (len == 0) {
            return Maths.CZERO;
        }
        List<Expr> all = new ArrayList<>();
        Domain d = null;
        MutableComplex c = new MutableComplex(1, 0);
        Queue<Expr> t = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            Expr e = arr.get(i);
            t.add(e);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                if (e2 instanceof Mul) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplexExpr()) {
                        Complex v = e2.toComplex();
                        if (c.isZero()) {
                            return Maths.CZERO;
                        }
                        c.mul(v);
                        if (d == null) {
                            d = e2.getDomain();
                        } else {
                            d = d.intersect(e2.getDomain());
                        }
                    } else {
                        all.add(e2);
                    }
                }
            }
        }
        Complex complex = c.toComplex();
        Expr complexExpr = d == null ? complex : complex.mul(d);
        if (all.isEmpty()) {
            return complexExpr;
        }
        if (!complexExpr.equals(Maths.CONE)) {
            all.add(0, complexExpr);
        }
        return new Mul(all.toArray(new Expr[all.size()]));
    }

    public static Expr mul(Expr... e) {
        return emul(new TVectorModel<Expr>() {
            @Override
            public int size() {
                return e.length;
            }

            @Override
            public Expr get(int index) {
                return e[index];
            }
        });
    }
    public static Expr esum(TVectorModel<Expr> arr) {
        int len = arr.size();
        if (len == 0) {
            return Maths.CZERO;
        }
        List<Expr> all = new ArrayList<>();
        MutableComplex c = new MutableComplex();
        Queue<Expr> t = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            Expr e = arr.get(i);
            t.add(e);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                if (e2 instanceof Plus) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplex()) {
                        c.add(e2.toComplex());
                    } else {
                        all.add(e2);
                    }
                }
            }
        }
        if (all.isEmpty()) {
            return c.toComplex();
        }
        if (!c.isZero()) {
            all.add(c.toComplex());
        }
        return new Plus(all);
    }

    public static Expr sum(Expr... arr) {
        int len = arr.length;
        if (len == 0) {
            return Maths.CZERO;
        }
        if(arr.length==1){
            if(arr[0] instanceof Discrete){
                return ((Discrete) arr[0]).sum();
            }
            if(arr[0] instanceof VDiscrete){
                return ((VDiscrete) arr[0]).sum();
            }
        }
        List<Expr> all = new ArrayList<>();
        MutableComplex c = new MutableComplex();
        Queue<Expr> t = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            Expr e = arr[i];
            t.add(e);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                e2=Any.unwrap(e2);
                if (e2 instanceof Plus) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplex()) {
                        c.add(e2.toComplex());
                    } else {
                        all.add(e2);
                    }
                }
            }
        }
        if (all.isEmpty()) {
            return c.toComplex();
        }
        if (!c.isZero()) {
            all.add(c.toComplex());
        }
        if (all.size() == 1) {
            return all.get(0);
        }
        return new Plus(all);
    }

    public static <T> T sum(TypeReference<T> type, T... arr) {
        if (Maths.$COMPLEX.isAssignableFrom(type)) {
            return (T) sum((Complex[]) arr);
        }
        if (Maths.$EXPR.isAssignableFrom(type)) {
            return (T) sum((Expr[]) arr);
        }
        VectorSpace<T> s = Maths.getVectorSpace(type);
        T a = s.zero();
        for (int i = 0; i < arr.length; i++) {
            a = s.add(a, arr[i]);
        }
        return a;
    }

    public static <T> T sum(TypeReference<T> type, TVectorModel<T> arr) {
        if (Maths.$COMPLEX.isAssignableFrom(type)) {
            return (T) Maths.csum((TVectorModel<Complex>) arr);
        }
        if (Maths.$EXPR.isAssignableFrom(type)) {
            return (T) esum((TVectorModel<Expr>) arr);
        }
        VectorSpace<T> s = Maths.getVectorSpace(type);
        T a = s.zero();
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            a = s.add(a, arr.get(i));
        }
        return a;
    }

    public static <T> T sum(TypeReference<T> type, int size, TVectorCell<T> arr) {
        return sum(type, new TVectorModelFromCell<>(size, arr));
    }

    public static <T> T mul(TypeReference<T> type, T... arr) {
        if (Maths.$COMPLEX.isAssignableFrom(type)) {
            return (T) mul((Complex[]) arr);
        }
        if (Maths.$EXPR.isAssignableFrom(type)) {
            return (T) mul((Expr[]) arr);
        }
        VectorSpace<T> s = Maths.getVectorSpace(type);
        T a = s.one();
        for (int i = 0; i < arr.length; i++) {
            a = s.mul(a, arr[i]);
        }
        return a;
    }

    public static <T> T mul(TypeReference<T> type, TVectorModel<T> arr) {
        if (Maths.$COMPLEX.isAssignableFrom(type)) {
            return (T) cmul((TVectorModel<Complex>) arr);
        }
        if (Maths.$EXPR.isAssignableFrom(type)) {
            return (T) emul((TVectorModel<Expr>) arr);
        }
        VectorSpace<T> s = Maths.getVectorSpace(type);
        T a = s.one();
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            a = s.mul(a, arr.get(i));
        }
        return a;
    }

    public static Expr add(Expr... a) {
        switch (a.length) {
            case 0: {
                throw new IllegalArgumentException("Missing arguments for add");
            }
            case 1: {
                return a[0];
            }
            case 2: {
                return new Plus(a[0], a[1]);
            }
            default: {
                Plus p = new Plus(a[0], a[1]);
                for (int i = 2; i < a.length; i++) {
                    p = new Plus(p, a[i]);
                }
                return p;
            }
        }
    }
}
