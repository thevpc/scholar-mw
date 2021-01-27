package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.PlatformUtils;
import net.thevpc.common.util.TypeName;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.Any;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class MathsExpr {
    public static Vector<Expr> edotmul(Vector<Expr>... arr) {
        TypeName cls = arr[0].getComponentType();
        for (int i = 0; i < arr.length; i++) {
            cls = PlatformUtils.lowestCommonAncestor(cls, arr[i].getComponentType());
        }
        VectorSpace<Expr> componentVectorSpace = Maths.getVectorSpace(cls);
        return new ReadOnlyVector<Expr>(arr[0].getComponentType(), arr[0].isRow(), new VectorModel<Expr>() {
            @Override
            public int size() {
                return arr[0].size();
            }

            @Override
            public Expr get(int index) {
                Expr e = arr[0].get(index);
                for (int i = 1; i < arr.length; i++) {
                    Vector<Expr> v = arr[i];
                    e = componentVectorSpace.mul(e, v.get(index));
                }
                return e;
            }
        });
    }

    public static Vector<Expr> edotdiv(Vector<Expr>... arr) {
        VectorSpace<Expr> componentVectorSpace = arr[0].getComponentVectorSpace();
        return new ReadOnlyVector<>(arr[0].getComponentType(), arr[0].isRow(), new VectorModel<Expr>() {
            @Override
            public int size() {
                return arr[0].size();
            }

            @Override
            public Expr get(int index) {
                Expr e = arr[0].get(index);
                for (int i = 1; i < arr.length; i++) {
                    Vector<Expr> v = arr[i];
                    e = componentVectorSpace.div(e, v.get(index));
                }
                return e;
            }
        });
    }

    public static <T> T sum(TypeName<T> type, T... arr) {
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

    public static Expr sum(Expr... arr) {
        int len = arr.length;
        if (len == 0) {
            return Maths.CZERO;
        }
        if (arr.length == 1) {
            if (arr[0] instanceof CDiscrete) {
                return ((CDiscrete) arr[0]).sum();
            }
            if (arr[0] instanceof VDiscrete) {
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
                e2 = Any.unwrap(e2);
                if (e2 instanceof Plus) {
                    t.addAll(e2.getChildren());
                } else {
                    if (e2.isNarrow(ExprType.COMPLEX_EXPR) && e2.getDomain().isUnbounded()) {
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
        return Plus.of(all.toArray(new Expr[0]));
    }

    public static <T> T sum(TypeName<T> type, int size, VectorCell<T> arr) {
        return sum(type, new VectorModelFromCell<>(size, arr));
    }

    public static <T> T sum(TypeName<T> type, VectorModel<T> arr) {
        if (Maths.$COMPLEX.isAssignableFrom(type)) {
            return (T) Maths.csum((VectorModel<Complex>) arr);
        }
        if (Maths.$EXPR.isAssignableFrom(type)) {
            return (T) esum((VectorModel<Expr>) arr);
        }
        VectorSpace<T> s = Maths.getVectorSpace(type);
        T a = s.zero();
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            a = s.add(a, arr.get(i));
        }
        return a;
    }

    public static Expr esum(VectorModel<Expr> arr) {
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
                    t.addAll(e2.getChildren());
                } else {
                    if (e2.isNarrow(ExprType.COMPLEX_EXPR) && e.getDomain().isUnbounded()) {
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
        return Plus.of(all.toArray(new Expr[0]));
    }

    public static <T> T mul(TypeName<T> type, T... arr) {
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

    public static Expr mul(Expr... e) {
        return emul(new VectorModel<Expr>() {
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

    public static Expr emul(VectorModel<Expr> arr) {
        int len = arr.size();
        switch (len){
            case 0:return Maths.ZERO;
            case 1:return arr.get(0);
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
                    t.addAll(e2.getChildren());
                } else {
                    if (e2.isNarrow(ExprType.COMPLEX_EXPR)) {
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
        Complex cc = c.toComplex();
        Expr vv = cc.isReal()? Maths.expr(cc.toDouble()):cc;
        Expr complexExpr = d == null ? vv : vv.mul(d);
        if (all.isEmpty()) {
            return complexExpr;
        }
        if (!(complexExpr.getDomain().isUnbounded() && complexExpr.isNarrow(ExprType.DOUBLE_NBR) && complexExpr.toDouble()==1)) {
            all.add(0, complexExpr);
        }
        switch (all.size()){
            case 0:return Maths.ZERO;
            case 1:return all.get(0);
        }
        return Mul.of(all.toArray(new Expr[0]));
    }

    public static <T> T mul(TypeName<T> type, VectorModel<T> arr) {
        if (Maths.$COMPLEX.isAssignableFrom(type)) {
            return (T) cmul((VectorModel<Complex>) arr);
        }
        if (Maths.$EXPR.isAssignableFrom(type)) {
            return (T) emul((VectorModel<Expr>) arr);
        }
        VectorSpace<T> s = Maths.getVectorSpace(type);
        T a = s.one();
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            a = s.mul(a, arr.get(i));
        }
        return a;
    }

    public static Complex cmul(VectorModel<Complex> arr) {
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

    public static Expr add(Expr... a) {
        switch (a.length) {
            case 0: {
                throw new IllegalArgumentException("Missing arguments for add");
            }
            case 1: {
                return a[0];
            }
            case 2: {
                return Plus.of(a[0], a[1]);
            }
            default: {
                Plus p = Plus.of(a[0], a[1]);
                for (int i = 2; i < a.length; i++) {
                    p = Plus.of(p, a[i]);
                }
                return p;
            }
        }
    }
}
