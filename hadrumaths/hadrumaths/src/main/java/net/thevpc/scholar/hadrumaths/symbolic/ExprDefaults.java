package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.symbolic.conv.DefaultDoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2matrix.DefaultDoubleToMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.DefaultDoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.Any;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.*;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.*;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.*;

import java.util.*;
import java.util.stream.Collectors;

public class ExprDefaults {

    public static Expr updateTitleVars(Expr b, Map<String, String> paramValNames) {
        String btitle = b.getTitle();
        if (btitle != null && btitle.contains("$")) {
            for (Map.Entry<String, String> e : paramValNames.entrySet()) {
                String key = e.getKey();
                if (key != null && key.length() > 0) {
                    String value = e.getKey();
                    if (value == null) {
                        value = "";
                    }
                    btitle = btitle.replace("${" + key + "}", value);
                }
            }
            b.setTitle(btitle);
        }
        return b;
    }

    public static boolean isNaNAny(Expr[] expressions) {
        for (Expr expression : expressions) {
            if (expression.isNaN()) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasParamsAny(List<Expr> expressions) {
        for (Expr expression : expressions) {
            if (expression.hasParams()) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasParamsAny(Expr[] expressions) {
        for (Expr expression : expressions) {
            if (expression.hasParams()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isZeroAny(List<Expr> expressions) {
        for (Expr expression : expressions) {
            if (expression.isZero()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isZeroAny(Expr[] expressions) {
        for (Expr expression : expressions) {
            if (expression.isZero()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInvariantAll(List<Expr> expressions, Axis axis) {
        for (Expr expression : expressions) {
            if (!expression.isInvariant(axis)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEvaluatableAll(List<Expr> expressions) {
        for (Expr expression : expressions) {
            if (!expression.isEvaluatable()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInvariantAll(Expr[] expressions, Axis axis) {
        for (Expr expression : expressions) {
            if (!expression.isInvariant(axis)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInfiniteAny(List<Expr> expressions) {
        for (Expr expression : expressions) {
            if (expression.isInfinite()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInfiniteAny(Expr[] expressions) {
        for (Expr expression : expressions) {
            if (expression.isInfinite()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isZeroAll(List<Expr> expressions) {
        for (Expr expression : expressions) {
            if (!expression.isZero()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isZeroAll(Expr[] expressions) {
        for (Expr expression : expressions) {
            if (!expression.isZero()) {
                return false;
            }
        }
        return true;
    }

//    public static boolean isSymmetricAll(List<DoubleToDouble> expressions, Axis axis) {
//        for (DoubleToDouble expression : expressions) {
//            if (!expression.isSymmetric(axis)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static boolean isSymmetricAll(Expr[] expressions, Axis axis) {
//        for (Expr expression : expressions) {
//            if (!expression.isSymmetric(axis)) {
//                return false;
//            }
//        }
//        return true;
//    }
    public static Domain expandDomainForExpressions(Expr[] subExpressions) {
        Domain d = subExpressions[0].getDomain();
        for (Expr expression : subExpressions) {
            d = d.expand(expression.getDomain());
        }
        return d;
    }

    public static Domain expandDomainForExpressions(List<Expr> subExpressions) {
        Domain d = subExpressions.get(0).getDomain();
        for (Expr expression : subExpressions) {
            d = d.expand(expression.getDomain());
        }
        return d;
    }

    public static Domain intersectDomainForExpressions(List<Expr> subExpressions) {
        Domain d = subExpressions.get(0).getDomain();
        for (Expr expression : subExpressions) {
            d = d.intersect(expression.getDomain());
        }
        return d;
    }

    public static Expr compose(Expr initialExpr, Expr xreplacement) {
        return initialExpr.compose(xreplacement, null, null);
    }

    public static Expr compose(Expr initialExpr, Expr xreplacement, Expr yreplacement) {
        return initialExpr.compose(xreplacement, yreplacement, null);
    }

    public static Expr compose(Expr initialExpr, Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        Expr[] a = initialExpr.getChildren().toArray(new Expr[0]);
        Expr[] b = new Expr[a.length];
        boolean updated = false;
        for (int i = 0; i < a.length; i++) {
            Expr ai = a[i];
            Expr bi = ai.compose(xreplacement, yreplacement, zreplacement);
            if (bi != null && bi != ai) {
                updated = true;
            }
            b[i] = bi;
        }
        if (updated) {
            Expr e = initialExpr.newInstance(b);
            e = ExprDefaults.copyProperties(initialExpr, e);
            return e;
        }
        return initialExpr;
    }

    public static Expr copyProperties(Expr a, Expr b) {
        if (a != b) {
            if (a.hasProperties()) {
                b = b.setProperties(a.getProperties());
            }
            if (a.getTitle() != null) {
                b = b.setTitle(a.getTitle());
            }
        }
        return b;
    }

    public static Expr setParams(Expr initialExpr, ParamValues values) {
        List<Expr> subExpressions = initialExpr.getChildren();
        BooleanRef defined = BooleanMarker.ref();
        Expr[] newVals = setParams(subExpressions.toArray(new Expr[0]), values, defined);
        if (defined.get()) {
            Expr nv = initialExpr.newInstance(newVals);
            Expr e = ExprDefaults.copyProperties(initialExpr, nv);
            return ExprDefaults.updateTitleVars(e, values);
        }
        return initialExpr;
    }

    public static Expr setParam(Expr initialExpr, String name, double value) {
        List<Expr> subExpressions = initialExpr.getChildren();
        BooleanRef defined = BooleanMarker.ref();
        Expr[] newVals = setParam(subExpressions.toArray(new Expr[0]), name, value, defined);
        if (defined.get()) {
            Expr nv = initialExpr.newInstance(newVals);
            Expr e = ExprDefaults.copyProperties(initialExpr, nv);
            return ExprDefaults.updateTitleVars(e, name, value);
        }
        return initialExpr;
    }

    public static Expr[] setParam(Expr[] a, String name, double value, BooleanMarker defined) {
        Expr[] b = new Expr[a.length];
        boolean updated = false;
        for (int i = 0; i < a.length; i++) {
            Expr ai = a[i];
            Expr bi = ai.setParam(name, value);
            if (bi == null) {
                b[i] = ai;
            } else if (bi != ai) {
                updated = true;
            }
            b[i] = bi;
        }
        if (updated) {
            defined.set();
            return b;
        }
        return b;
    }

    public static Expr[] setParams(Expr[] a, ParamValues values, BooleanMarker defined) {
        Expr[] b = new Expr[a.length];
        boolean updated = false;
        for (int i = 0; i < a.length; i++) {
            Expr ai = a[i];
            Expr bi = ai.setParams(values);
            if (bi == null) {
                b[i] = ai;
            } else if (bi != ai) {
                updated = true;
            }
            b[i] = bi;
        }
        if (updated) {
            defined.set();
            return b;
        }
        return b;
    }

    public static Expr updateTitleVars(Expr b, String paramName, double paramValue) {
        String btitle = b.getTitle();
        if (btitle != null) {
            if (btitle.indexOf('$') >= 0) {
                String paramValueS = String.valueOf(paramValue);
                String key = "${" + paramName + "}";
                String btitle2 = btitle.replace(key, paramValueS);
                if (paramName.length() == 1) {
                    btitle2 = btitle2.replace("$" + paramName, paramValueS);
                }
                if (!btitle.equals(btitle2)) {
                    return b.setTitle(btitle);
                }
            }
        }
        return b;
    }

    public static Expr updateTitleVars(Expr b, ParamValues params) {
        String btitle = b.getTitle();
        if (btitle != null) {
            if (btitle.indexOf('$') >= 0) {
                String btitle2 = btitle;
                for (Param param : params.getParams()) {
                    String paramName = param.getName();
                    String paramValueS = String.valueOf(paramName);
                    String key = "${" + paramName + "}";
                    btitle2 = btitle2.replace(key, paramValueS);
                    if (paramName.length() == 1) {
                        btitle2 = btitle2.replace("$" + paramName, paramValueS);
                    }
                }
                if (!btitle.equals(btitle2)) {
                    return b.setTitle(btitle);
                }
            }
        }
        return b;
    }

    public static Set<Param> getParams(Expr me) {
        Set<Param> s = null;
        for (Expr expr : me.getChildren()) {
            Set<Param> p = expr.getParams();
            if (!p.isEmpty()) {
                if (s == null) {
                    s = new HashSet<>();
                }
                s.addAll(p);
            }
        }
        return s == null ? Collections.EMPTY_SET : s;
    }

    public static Expr setParam(Expr initialExpr, Param name, double value) {
        List<Expr> subExpressions = initialExpr.getChildren();
        BooleanRef defined = BooleanMarker.ref();
        Expr[] newVals = setParam(subExpressions.toArray(new Expr[0]), name.getName(), value, defined);
        if (defined.get()) {
            Expr nv = initialExpr.newInstance(newVals);
            Expr e = ExprDefaults.copyProperties(initialExpr, nv);
            return ExprDefaults.updateTitleVars(e, name.getName(), value);
        }
        return initialExpr;
    }

    public static Expr setParam(Expr initialExpr, Param name, Expr value) {
        List<Expr> subExpressions = initialExpr.getChildren();
        BooleanRef defined = BooleanMarker.ref();
        Expr[] newVals = setParam(subExpressions.toArray(new Expr[0]), name.getName(), value, defined);
        if (defined.get()) {
            Expr nv = initialExpr.newInstance(newVals);
            Expr e = ExprDefaults.copyProperties(initialExpr, nv);
            return ExprDefaults.updateTitleVars(e, name.getName(), value);
        }
        return initialExpr;
    }

    public static Expr[] setParam(Expr[] a, String name, Expr value, BooleanMarker defined) {
        Expr[] b = new Expr[a.length];
        boolean updated = false;
        for (int i = 0; i < a.length; i++) {
            Expr ai = a[i];
            Expr bi = ai.setParam(name, value);
            if (bi == null) {
                b[i] = ai;
            } else if (bi != ai) {
                updated = true;
            }
            b[i] = bi;
        }
        if (updated) {
            defined.set();
            return b;
        }
        return b;
    }

    public static Expr updateTitleVars(Expr b, String paramName, Expr paramValue) {
        String paramValueS = paramName == null ? "" : paramValue.toString();
        String btitle = b.getTitle();
        String key = "${" + paramName + "}";
        if (btitle != null && btitle.contains(key)) {
            return b.setTitle(btitle.replace(key, paramValueS));
        }
        return b;
    }

    public static Expr setParam(Expr initialExpr, String name, Expr value) {
        List<Expr> subExpressions = initialExpr.getChildren();
        BooleanRef defined = BooleanMarker.ref();
        Expr[] newVals = setParam(subExpressions.toArray(new Expr[0]), name, value, defined);
        if (defined.get()) {
            Expr nv = initialExpr.newInstance(newVals);
            Expr e = ExprDefaults.copyProperties(initialExpr, nv);
            return ExprDefaults.updateTitleVars(e, name, value);
        }
        return initialExpr;
    }

    public static Expr copyProperties(Expr me, Expr to, String name, Expr value) {
        Expr e = ExprDefaults.copyProperties(me, to);
        return ExprDefaults.updateTitleVars(e, name, value);
    }

    public static boolean is(ExprType tt, Collection<Expr> expressions) {
        for (Expr e : expressions) {
            if (!e.is(tt)) {
                return false;
            }
        }
        return expressions.size() > 0;
    }

    public static boolean is(ExprType tt, Expr... expressions) {
        for (Expr e : expressions) {
            if (!e.is(tt)) {
                return false;
            }
        }
        return expressions.length > 0;
    }

    public static ExprType widest(Collection<Expr> expressions) {
        ExprType a = ExprType.DOUBLE_DOUBLE;
        for (Expr e : expressions) {
            a = widest(a, e.getType());
        }
        return a;
    }

    public static ExprType widest(ExprType first, ExprType second) {
        if (first == second) {
            return first;
        }
        switch (first) {
            case DOUBLE_NBR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return second;
                    case COMPLEX_NBR:
                        return second;
                    case CVECTOR_NBR:
                        return second;
                    case CMATRIX_NBR:
                        return second;
                    case DOUBLE_EXPR:
                        return second;
                    case COMPLEX_EXPR:
                        return second;
                    case CVECTOR_EXPR:
                        return second;
                    case CMATRIX_EXPR:
                        return second;
                    case DOUBLE_DOUBLE:
                        return second;
                    case DOUBLE_COMPLEX:
                        return second;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return second;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return second;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return second;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return second;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return second;
                }
            }
            case COMPLEX_NBR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return first;
                    case COMPLEX_NBR:
                        return second;
                    case CVECTOR_NBR:
                        return second;
                    case CMATRIX_NBR:
                        return second;
                    case DOUBLE_EXPR:
                        return ExprType.COMPLEX_EXPR;
                    case COMPLEX_EXPR:
                        return second;
                    case CVECTOR_EXPR:
                        return second;
                    case CMATRIX_EXPR:
                        return second;
                    case DOUBLE_DOUBLE:
                        return ExprType.DOUBLE_COMPLEX;
                    case DOUBLE_COMPLEX:
                        return second;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return second;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return second;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return second;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return second;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return second;
                }
            }
            case CVECTOR_NBR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return first;
                    case COMPLEX_NBR:
                        return first;
                    case CVECTOR_NBR:
                        return second;
                    case CMATRIX_NBR:
                        return second;
                    case DOUBLE_EXPR:
                        return ExprType.CVECTOR_EXPR;
                    case COMPLEX_EXPR:
                        return ExprType.CVECTOR_EXPR;
                    case CVECTOR_EXPR:
                        return ExprType.CVECTOR_EXPR;
                    case CMATRIX_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case DOUBLE_DOUBLE:
                        return first;
                    case DOUBLE_COMPLEX:
                        return first;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return second;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return second;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return second;
                }
            }
            case CMATRIX_NBR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return first;
                    case COMPLEX_NBR:
                        return first;
                    case CVECTOR_NBR:
                        return second;
                    case CMATRIX_NBR:
                        return first;
                    case DOUBLE_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case COMPLEX_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case CVECTOR_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case CMATRIX_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case DOUBLE_DOUBLE:
                        return first;
                    case DOUBLE_COMPLEX:
                        return first;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return second;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return second;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return second;
                }
            }
            case DOUBLE_EXPR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return ExprType.DOUBLE_EXPR;
                    case COMPLEX_NBR:
                        return ExprType.COMPLEX_EXPR;
                    case CVECTOR_NBR:
                        return ExprType.CVECTOR_EXPR;
                    case CMATRIX_NBR:
                        return ExprType.CMATRIX_EXPR;
                    case DOUBLE_EXPR:
                        return first;
                    case COMPLEX_EXPR:
                        return second;
                    case CVECTOR_EXPR:
                        return second;
                    case CMATRIX_EXPR:
                        return second;
                    case DOUBLE_DOUBLE:
                        return first;
                    case DOUBLE_COMPLEX:
                        return second;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return second;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return second;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return second;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return second;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return second;
                }
            }
            case COMPLEX_EXPR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return ExprType.COMPLEX_EXPR;
                    case COMPLEX_NBR:
                        return ExprType.COMPLEX_EXPR;
                    case CVECTOR_NBR:
                        return ExprType.CVECTOR_EXPR;
                    case CMATRIX_NBR:
                        return ExprType.CMATRIX_EXPR;
                    case DOUBLE_EXPR:
                        return first;
                    case COMPLEX_EXPR:
                        return first;
                    case CVECTOR_EXPR:
                        return second;
                    case CMATRIX_EXPR:
                        return second;
                    case DOUBLE_DOUBLE:
                        return ExprType.DOUBLE_COMPLEX;
                    case DOUBLE_COMPLEX:
                        return ExprType.DOUBLE_COMPLEX;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return ExprType.DOUBLE_CVECTOR;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_COMPLEX;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
                }
            }
            case CVECTOR_EXPR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return ExprType.CVECTOR_EXPR;
                    case COMPLEX_NBR:
                        return ExprType.CVECTOR_EXPR;
                    case CVECTOR_NBR:
                        return ExprType.CVECTOR_EXPR;
                    case CMATRIX_NBR:
                        return ExprType.CMATRIX_EXPR;
                    case DOUBLE_EXPR:
                        return ExprType.CVECTOR_EXPR;
                    case COMPLEX_EXPR:
                        return ExprType.CVECTOR_EXPR;
                    case CVECTOR_EXPR:
                        return ExprType.CVECTOR_EXPR;
                    case CMATRIX_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case DOUBLE_DOUBLE:
                        return ExprType.DOUBLE_CVECTOR;
                    case DOUBLE_COMPLEX:
                        return ExprType.DOUBLE_CVECTOR;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return ExprType.DOUBLE_CVECTOR;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
                }
            }
            case CMATRIX_EXPR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return ExprType.CMATRIX_EXPR;
                    case COMPLEX_NBR:
                        return ExprType.CMATRIX_EXPR;
                    case CVECTOR_NBR:
                        return ExprType.CMATRIX_EXPR;
                    case CMATRIX_NBR:
                        return ExprType.CMATRIX_EXPR;
                    case DOUBLE_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case COMPLEX_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case CVECTOR_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case CMATRIX_EXPR:
                        return ExprType.CMATRIX_EXPR;
                    case DOUBLE_DOUBLE:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_COMPLEX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return ExprType.DOUBLE_CMATRIX;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
                }
            }
            case DOUBLE_DOUBLE: {
                switch (second) {
                    case DOUBLE_NBR:
                        return ExprType.DOUBLE_DOUBLE;
                    case COMPLEX_NBR:
                        return ExprType.DOUBLE_COMPLEX;
                    case CVECTOR_NBR:
                        return ExprType.DOUBLE_CVECTOR;
                    case CMATRIX_NBR:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_EXPR:
                        return ExprType.DOUBLE_DOUBLE;
                    case COMPLEX_EXPR:
                        return ExprType.DOUBLE_COMPLEX;
                    case CVECTOR_EXPR:
                        return ExprType.DOUBLE_CVECTOR;
                    case CMATRIX_EXPR:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_DOUBLE:
                        return ExprType.DOUBLE_DOUBLE;
                    case DOUBLE_COMPLEX:
                        return ExprType.DOUBLE_COMPLEX;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return ExprType.DOUBLE_CVECTOR;
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_COMPLEX;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
                }
            }
            case DOUBLE_COMPLEX: {
                switch (second) {
                    case DOUBLE_NBR:
                        return ExprType.DOUBLE_COMPLEX;
                    case COMPLEX_NBR:
                        return ExprType.DOUBLE_COMPLEX;
                    case CVECTOR_NBR:
                        return ExprType.DOUBLE_CVECTOR;
                    case CMATRIX_NBR:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_EXPR:
                        return ExprType.DOUBLE_COMPLEX;
                    case COMPLEX_EXPR:
                        return ExprType.DOUBLE_COMPLEX;
                    case CVECTOR_EXPR:
                        return ExprType.DOUBLE_CVECTOR;
                    case CMATRIX_EXPR:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_DOUBLE:
                        return ExprType.DOUBLE_COMPLEX;
                    case DOUBLE_COMPLEX:
                        return ExprType.DOUBLE_COMPLEX;
//                    case DBL_TO_VECT_DBL:return second;
                    case DOUBLE_CVECTOR:
                        return ExprType.DOUBLE_CVECTOR;
//                    case DBL_TO_MATX_DBL:return second;
                    case DOUBLE_CMATRIX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case CMX_TO_SCLR_DBL:return second;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_COMPLEX;
////                    case CMX_TO_VECT_DBL:return second;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return second;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
                }
            }
            case DOUBLE_CVECTOR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return ExprType.DOUBLE_CVECTOR;
                    case COMPLEX_NBR:
                        return ExprType.DOUBLE_CVECTOR;
                    case CVECTOR_NBR:
                        return ExprType.DOUBLE_CVECTOR;
                    case CMATRIX_NBR:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_EXPR:
                        return ExprType.DOUBLE_CVECTOR;
                    case COMPLEX_EXPR:
                        return ExprType.DOUBLE_CVECTOR;
                    case CVECTOR_EXPR:
                        return ExprType.DOUBLE_CVECTOR;
                    case CMATRIX_EXPR:
                        return ExprType.DOUBLE_CVECTOR;
                    case DOUBLE_DOUBLE:
                        return ExprType.DOUBLE_CVECTOR;
                    case DOUBLE_COMPLEX:
                        return ExprType.DOUBLE_CVECTOR;
//                    case DBL_TO_VECT_DBL:return second;
                    case DOUBLE_CVECTOR:
                        return ExprType.DOUBLE_CVECTOR;
//                    case DBL_TO_MATX_DBL:return second;
                    case DOUBLE_CMATRIX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case CMX_TO_SCLR_DBL:return second;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_VECT_DBL:return second;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return second;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
                }
            }
            case DOUBLE_CMATRIX: {
                switch (second) {
                    case DOUBLE_NBR:
                        return ExprType.DOUBLE_CMATRIX;
                    case COMPLEX_NBR:
                        return ExprType.DOUBLE_CMATRIX;
                    case CVECTOR_NBR:
                        return ExprType.DOUBLE_CMATRIX;
                    case CMATRIX_NBR:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_EXPR:
                        return ExprType.DOUBLE_CMATRIX;
                    case COMPLEX_EXPR:
                        return ExprType.DOUBLE_CMATRIX;
                    case CVECTOR_EXPR:
                        return ExprType.DOUBLE_CMATRIX;
                    case CMATRIX_EXPR:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_DOUBLE:
                        return ExprType.DOUBLE_CMATRIX;
                    case DOUBLE_COMPLEX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case DBL_TO_VECT_DBL:return second;
                    case DOUBLE_CVECTOR:
                        return ExprType.DOUBLE_CMATRIX;
//                    case DBL_TO_MATX_DBL:return second;
                    case DOUBLE_CMATRIX:
                        return ExprType.DOUBLE_CMATRIX;
//                    case CMX_TO_SCLR_DBL:return second;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_VECT_DBL:return second;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_MATX_DBL:return second;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
                }
            }
//            case COMPLEX_COMPLEX: {
//                switch (second) {
//                    case DOUBLE_NBR:
//                        return ExprType.COMPLEX_COMPLEX;
//                    case COMPLEX_NBR:
//                        return ExprType.COMPLEX_COMPLEX;
//                    case CVECTOR_NBR:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case CMATRIX_NBR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case DOUBLE_EXPR:
//                        return ExprType.COMPLEX_COMPLEX;
//                    case COMPLEX_EXPR:
//                        return ExprType.COMPLEX_COMPLEX;
//                    case CVECTOR_EXPR:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case CMATRIX_EXPR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case DOUBLE_DOUBLE:
//                        return ExprType.COMPLEX_COMPLEX;
//                    case DOUBLE_COMPLEX:
//                        return ExprType.COMPLEX_COMPLEX;
////                    case DBL_TO_VECT_DBL:return second;
//                    case DOUBLE_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case DBL_TO_MATX_DBL:return second;
//                    case DOUBLE_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_SCLR_DBL:return second;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_COMPLEX;
////                    case CMX_TO_VECT_DBL:return second;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return second;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
//                }
//            }
//            case COMPLEX_CVECTOR: {
//                switch (second) {
//                    case DOUBLE_NBR:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case COMPLEX_NBR:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case CVECTOR_NBR:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case CMATRIX_NBR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case DOUBLE_EXPR:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case COMPLEX_EXPR:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case CVECTOR_EXPR:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case CMATRIX_EXPR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case DOUBLE_DOUBLE:
//                        return ExprType.COMPLEX_CVECTOR;
//                    case DOUBLE_COMPLEX:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case DBL_TO_VECT_DBL:return second;
//                    case DOUBLE_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case DBL_TO_MATX_DBL:return second;
//                    case DOUBLE_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_SCLR_DBL:return second;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_VECT_DBL:return second;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CVECTOR;
////                    case CMX_TO_MATX_DBL:return second;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
//                }
//            }
//            case COMPLEX_CMATRIX: {
//                switch (second) {
//                    case DOUBLE_NBR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case COMPLEX_NBR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case CVECTOR_NBR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case CMATRIX_NBR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case DOUBLE_EXPR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case COMPLEX_EXPR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case CVECTOR_EXPR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case CMATRIX_EXPR:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case DOUBLE_DOUBLE:
//                        return ExprType.COMPLEX_CMATRIX;
//                    case DOUBLE_COMPLEX:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case DBL_TO_VECT_DBL:return second;
//                    case DOUBLE_CVECTOR:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case DBL_TO_MATX_DBL:return second;
//                    case DOUBLE_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_SCLR_DBL:return second;
//                    case COMPLEX_COMPLEX:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_VECT_DBL:return second;
//                    case COMPLEX_CVECTOR:
//                        return ExprType.COMPLEX_CMATRIX;
////                    case CMX_TO_MATX_DBL:return second;
//                    case COMPLEX_CMATRIX:
//                        return ExprType.COMPLEX_CMATRIX;
//                }
//            }
        }
        throw new IllegalArgumentException("Unsupported switch case");
    }

    public static ExprType narrowest(Expr e) {
        return e.getNarrowType();
    }

    public static ExprType narrowest(Collection<Expr> expressions) {
        ExprType a = ExprType.DOUBLE_DOUBLE;
        for (Expr e : expressions) {
            a = widest(a, e.getNarrowType());
        }
        return a;
    }

    public static ExprType narrowest(Expr... expressions) {
        ExprType a = ExprType.DOUBLE_DOUBLE;
        for (Expr e : expressions) {
            a = widest(a, e.getNarrowType());
        }
        return a;
    }

    public static ExprType widest(Expr... expressions) {
        ExprType a = ExprType.DOUBLE_DOUBLE;
        for (Expr e : expressions) {
            if (e == null || e.getType() == null) {
                //System.out.println("Why");
            }else {
                a = widest(a, e.getType());
            }
        }
        return a;
    }

    public static boolean is(Expr first, ExprType second) {
        return first.getType() == second;
    }

    public static DoubleToDouble narrowDD(Expr from) {
        ExprType to = ExprType.DOUBLE_DOUBLE;
        from.cast(to);
        ExprType first = from.getType();
        switch (first) {
            case DOUBLE_DOUBLE: {
                return (DoubleToDouble) from;
            }
        }
        throw new IllegalArgumentException("Unsupported narrow(" + first + "," + to + ")");
    }

    public static DoubleToComplex narrowDC(Expr from) {
        ExprType to = ExprType.DOUBLE_COMPLEX;
        from.cast(to);
        ExprType first = from.getType();
        switch (first) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new DefaultDoubleToComplex((DoubleToDouble) from);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return (DoubleToComplex) from;
            }
        }
        throw new IllegalArgumentException("Unsupported narrow(" + first + "," + to + ")");
    }

    public static DoubleToVector narrowDV(Expr from) {
        ExprType to = ExprType.DOUBLE_CVECTOR;
        from.cast(to);
        ExprType first = from.getType();
        switch (first) {
            case DOUBLE_DOUBLE: {
                return DefaultDoubleToVector.of((DoubleToDouble) from);
            }
            case DOUBLE_COMPLEX: {
                return DefaultDoubleToVector.of((DoubleToComplex) from);
            }
            case DOUBLE_CVECTOR: {
                return (DoubleToVector) from;
            }
        }
        throw new IllegalArgumentException("Unsupported narrow(" + first + "," + to + ")");
    }

    public static DoubleToMatrix narrowDM(Expr from) {
        ExprType to = ExprType.DOUBLE_CMATRIX;
        from.cast(to);
        ExprType first = from.getType();
        switch (first) {
            case DOUBLE_DOUBLE: {
                return DefaultDoubleToMatrix.of((DoubleToDouble) from);
            }
            case DOUBLE_COMPLEX: {
                return DefaultDoubleToMatrix.of((DoubleToComplex) from);
            }
            case DOUBLE_CVECTOR: {
                return DefaultDoubleToMatrix.of((DoubleToVector) from);
            }
            case DOUBLE_CMATRIX: {
                return (DoubleToMatrix) from;
            }
        }
        throw new IllegalArgumentException("Unsupported narrow(" + first + "," + to + ")");
    }

    /**
     * tries to if cast down is possible
     *
     * @param other
     * @return
     */
    public static boolean isNarrow(Expr me, ExprNumberType other) {
        for (ExprType exprType : toExprDefinitionType(other)) {
            if (me.isNarrow(exprType)) {
                return true;
            }
        }
        return false;
    }

    public static ExprType[] toExprDefinitionType(ExprNumberType other) {
        switch (other) {
            case DOUBLE_TYPE:
                return new ExprType[]{ExprType.DOUBLE_NBR, ExprType.DOUBLE_EXPR, ExprType.DOUBLE_DOUBLE};
            case COMPLEX_TYPE:
                return new ExprType[]{
                    ExprType.COMPLEX_NBR, ExprType.CVECTOR_NBR, ExprType.CMATRIX_NBR,
                    ExprType.COMPLEX_EXPR, ExprType.CVECTOR_EXPR, ExprType.CMATRIX_EXPR,
                    ExprType.COMPLEX_EXPR, ExprType.DOUBLE_COMPLEX};
        }
        return new ExprType[0];
    }

    public static boolean is(Expr me, ExprNumberType other) {
        return me.getType().out().nbr() == other;
//        for (ExprDefinitionType exprDefinitionType : toExprDefinitionType(other)) {
//            if (me.is(exprDefinitionType)) {
//                return true;
//            }
//        }
//        return false;
    }

    /**
     * tries to if cast down is possible
     *
     * @param other
     * @return
     */
    public static Expr narrow(Expr me, ExprNumberType other) {
        for (ExprType exprType : toExprDefinitionType(other)) {
            if (me.isNarrow(exprType)) {
                return narrow(me, exprType);
            }
        }
        throw new ClassCastException("Unable to Cast to " + other + " :: " + me.getClass().getName() + " = " + me.toString());
    }

    public static Expr narrow(Expr me, ExprType to) {
        me.cast(to);
        ExprType first = me.getType();
        if (first == to) {
            return me;
        }
        first = me.getType();
        switch (first) {
            case DOUBLE_NBR: {
                switch (to) {
                    case DOUBLE_NBR:
                        return me;
                    case COMPLEX_NBR:
                        return Complex.of(me.toDouble());
                    case CVECTOR_NBR:
                        return Maths.vector(Complex.of(me.toDouble()));
                    case CMATRIX_NBR:
                        return (Expr) Maths.matrix(new Complex[][]{{Complex.of(me.toDouble())}});
                    case DOUBLE_EXPR:
                        return me;
                    case COMPLEX_EXPR:
                        return Complex.of(me.toDouble());
                    case CVECTOR_EXPR:
                        return Maths.vector(Complex.of(me.toDouble()));
                    case CMATRIX_EXPR:
                        return (Expr) Maths.matrix(new Complex[][]{{Complex.of(me.toDouble())}});
                    case DOUBLE_DOUBLE:
                        return me.toDD();
                    case DOUBLE_COMPLEX:
                        return new DefaultDoubleToComplex(me.toDD());
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return DefaultDoubleToVector.of(me.toDD());
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return DefaultDoubleToMatrix.of(me.toDD());
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        break;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        break;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case COMPLEX_NBR: {
                switch (to) {
                    case DOUBLE_NBR:
                        return Maths.expr(me.toDouble());
                    case COMPLEX_NBR:
                        return me;
                    case CVECTOR_NBR:
                        return DefaultDoubleToVector.of(me.toDC());
                    case CMATRIX_NBR:
                        return DefaultDoubleToMatrix.of(me.toDC());
                    case DOUBLE_EXPR:
                        return Maths.expr(me.toDouble());
                    case COMPLEX_EXPR:
                        return me;
                    case CVECTOR_EXPR:
                        return DefaultDoubleToMatrix.of(me.toDC());
                    case CMATRIX_EXPR:
                        return DefaultDoubleToMatrix.of(me.toDC());
                    case DOUBLE_DOUBLE:
                        break;
                    case DOUBLE_COMPLEX:
                        return (DoubleToComplex) me;
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return DefaultDoubleToVector.of(me.toDC());
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return DefaultDoubleToMatrix.of(me.toDC());
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        break;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        break;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case CVECTOR_NBR: {
                switch (to) {
                    case DOUBLE_NBR:
                        break;
                    case COMPLEX_NBR:
                        break;
                    case CVECTOR_NBR:
                        return me;
                    case CMATRIX_NBR:
                        return DefaultDoubleToMatrix.of((DoubleToVector) me);
                    case DOUBLE_EXPR:
                        break;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        return me;
                    case CMATRIX_EXPR:
                        return DefaultDoubleToMatrix.of((DoubleToVector) me);
                    case DOUBLE_DOUBLE:
                        break;
                    case DOUBLE_COMPLEX:
                        break;
                    case DOUBLE_CVECTOR:
                        break;
                    case DOUBLE_CMATRIX:
                        break;
//                    case COMPLEX_COMPLEX:
//                        break;
//                    case COMPLEX_CVECTOR:
//                        break;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case CMATRIX_NBR: {
                switch (to) {
                    case DOUBLE_NBR:
                        break;
                    case COMPLEX_NBR:
                        break;
                    case CVECTOR_NBR:
                        break;
                    case CMATRIX_NBR:
                        return me;
                    case DOUBLE_EXPR:
                        break;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        break;
                    case CMATRIX_EXPR:
                        return me;
                    case DOUBLE_DOUBLE:
                        break;
                    case DOUBLE_COMPLEX:
                        break;
                    case DOUBLE_CVECTOR:
                        break;
                    case DOUBLE_CMATRIX:
                        break;
//                    case COMPLEX_COMPLEX:
//                        break;
//                    case COMPLEX_CVECTOR:
//                        break;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case DOUBLE_EXPR: {
                switch (to) {
                    case DOUBLE_NBR:
                        break;
                    case COMPLEX_NBR:
                        break;
                    case CVECTOR_NBR:
                        break;
                    case CMATRIX_NBR:
                        break;
                    case DOUBLE_EXPR:
                        return me;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        break;
                    case CMATRIX_EXPR:
                        break;
                    case DOUBLE_DOUBLE:
                        return me.toDD();
                    case DOUBLE_COMPLEX:
                        return new DefaultDoubleToComplex(me.toDD());
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return DefaultDoubleToVector.of(me.toDD());
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return DefaultDoubleToMatrix.of(me.toDD());
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        break;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        break;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case COMPLEX_EXPR: {
                switch (to) {
                    case DOUBLE_EXPR:
                        return DefaultComplexValue.of(me.toDouble(), me.getDomain());
                    case COMPLEX_EXPR:
                        return me;
                    case CVECTOR_EXPR:
                        break;
                    case CMATRIX_EXPR:
                        break;
                    case DOUBLE_DOUBLE:
                        return DefaultComplexValue.of(me.toDouble(), me.getDomain());
                    case DOUBLE_COMPLEX:
                        return me.toDC();
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return DefaultDoubleToVector.of(me.toDC());
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return DefaultDoubleToMatrix.of(me.toDC());
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        break;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        break;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case CVECTOR_EXPR: {
                switch (to) {
                    case DOUBLE_EXPR:
                        break;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        return me;
                    case CMATRIX_EXPR:
                        break;
                    case DOUBLE_DOUBLE:
                        break;
                    case DOUBLE_COMPLEX:
                        break;
                    case DOUBLE_CVECTOR:
                        break;
                    case DOUBLE_CMATRIX:
                        break;
//                    case COMPLEX_COMPLEX:
//                        break;
//                    case COMPLEX_CVECTOR:
//                        break;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case CMATRIX_EXPR: {
                switch (to) {
                    case DOUBLE_EXPR:
                        break;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        break;
                    case CMATRIX_EXPR:
                        return me;
                    case DOUBLE_DOUBLE:
                        break;
                    case DOUBLE_COMPLEX:
                        break;
                    case DOUBLE_CVECTOR:
                        break;
                    case DOUBLE_CMATRIX:
                        break;
//                    case COMPLEX_COMPLEX:
//                        break;
//                    case COMPLEX_CVECTOR:
//                        break;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case DOUBLE_DOUBLE: {
                DoubleToDouble u = (DoubleToDouble) me;
                switch (to) {
                    case DOUBLE_EXPR:
                        break;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        break;
                    case CMATRIX_EXPR:
                        break;
                    case DOUBLE_DOUBLE:
                        return me;
                    case DOUBLE_COMPLEX:
                        return new DefaultDoubleToComplex(u);
//                    case DBL_TO_VECT_DBL:return first;
                    case DOUBLE_CVECTOR:
                        return DefaultDoubleToVector.of(new DefaultDoubleToComplex(u));
//                    case DBL_TO_MATX_DBL:return first;
                    case DOUBLE_CMATRIX:
                        return DefaultDoubleToMatrix.of(u);
//                    case CMX_TO_SCLR_DBL:return first;
//                    case COMPLEX_COMPLEX:
//                        break;
////                    case CMX_TO_VECT_DBL:return first;
//                    case COMPLEX_CVECTOR:
//                        break;
////                    case CMX_TO_MATX_DBL:return first;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case DOUBLE_COMPLEX: {
                DoubleToComplex u = (DoubleToComplex) me;
                switch (to) {
                    case DOUBLE_EXPR:
                        break;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        break;
                    case CMATRIX_EXPR:
                        break;
                    case DOUBLE_DOUBLE:
                        break;
                    case DOUBLE_COMPLEX:
                        return u;
//                    case DBL_TO_VECT_DBL:return to;
                    case DOUBLE_CVECTOR:
                        return DefaultDoubleToVector.of(u);
//                    case DBL_TO_MATX_DBL:return to;
                    case DOUBLE_CMATRIX:
                        return DefaultDoubleToMatrix.of(u);
//                    case CMX_TO_SCLR_DBL:return to;
//                    case COMPLEX_COMPLEX:
//                        break;
////                    case CMX_TO_VECT_DBL:return to;
//                    case COMPLEX_CVECTOR:
//                        break;
////                    case CMX_TO_MATX_DBL:return to;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case DOUBLE_CVECTOR: {
                DoubleToVector u = (DoubleToVector) me;
                switch (to) {
                    case DOUBLE_EXPR:
                        break;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        break;
                    case CMATRIX_EXPR:
                        break;
                    case DOUBLE_DOUBLE:
                        break;
                    case DOUBLE_COMPLEX:
                        break;
//                    case DBL_TO_VECT_DBL:return to;
                    case DOUBLE_CVECTOR:
                        return u;
//                    case DBL_TO_MATX_DBL:return to;
                    case DOUBLE_CMATRIX:
                        return DefaultDoubleToMatrix.of(u);
//                    case CMX_TO_SCLR_DBL:return to;
//                    case COMPLEX_COMPLEX:
//                        break;
////                    case CMX_TO_VECT_DBL:return to;
//                    case COMPLEX_CVECTOR:
//                        break;
////                    case CMX_TO_MATX_DBL:return to;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }
            case DOUBLE_CMATRIX: {
                DoubleToMatrix u = (DoubleToMatrix) me;
                switch (to) {
                    case DOUBLE_EXPR:
                        break;
                    case COMPLEX_EXPR:
                        break;
                    case CVECTOR_EXPR:
                        break;
                    case CMATRIX_EXPR:
                        break;
                    case DOUBLE_DOUBLE:
                        break;
                    case DOUBLE_COMPLEX:
                        break;
//                    case DBL_TO_VECT_DBL:return to;
                    case DOUBLE_CVECTOR: {
                        ComponentDimension d = me.toDM().getComponentDimension();
                        if (d.columns == 1 || d.rows == 1) {
                            return DefaultDoubleToMatrix.of(me.toDM()).toVector();
                        }
                        break;
                    }
//                    case DBL_TO_MATX_DBL:return to;
                    case DOUBLE_CMATRIX:
                        return u;
//                    case CMX_TO_SCLR_DBL:return to;
//                    case COMPLEX_COMPLEX:
//                        break;
////                    case CMX_TO_VECT_DBL:return to;
//                    case COMPLEX_CVECTOR:
//                        break;
////                    case CMX_TO_MATX_DBL:return to;
//                    case COMPLEX_CMATRIX:
//                        break;
                }
                break;
            }

        }
        throw new ClassCastException("Unable to Cast " + first + " to " + to + " :: " + me.getClass().getName() + " = " + me.toString());
    }

    public static boolean isNarrow(Expr me, ExprType other) {
        return isNarrow(me.getNarrowType(), other);
    }

    public static boolean isNarrow(ExprType first, ExprType second) {
        if (first == second) {
            return true;
        }
        switch (first) {
            case DOUBLE_NBR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return true;
                    case COMPLEX_NBR:
                        return true;
                    case CVECTOR_NBR:
                        return true;
                    case CMATRIX_NBR:
                        return true;
                    case DOUBLE_EXPR:
                        return true;
                    case COMPLEX_EXPR:
                        return true;
                    case CVECTOR_EXPR:
                        return true;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return true;
                    case DOUBLE_COMPLEX:
                        return true;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
////                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return true;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case COMPLEX_NBR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return true;
                    case CVECTOR_NBR:
                        return true;
                    case CMATRIX_NBR:
                        return true;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return true;
                    case CVECTOR_EXPR:
                        return true;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return true;
                    case DOUBLE_COMPLEX:
                        return true;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return true;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case CVECTOR_NBR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return true;
                    case CMATRIX_NBR:
                        return true;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return false;
                    case CVECTOR_EXPR:
                        return true;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return false;
                    case DOUBLE_COMPLEX:
                        return false;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return false;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case CMATRIX_NBR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return true;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return false;
                    case CVECTOR_EXPR:
                        return false;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return false;
                    case DOUBLE_COMPLEX:
                        return false;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return false;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return false;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return false;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case DOUBLE_EXPR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return false;
                    case DOUBLE_EXPR:
                        return true;
                    case COMPLEX_EXPR:
                        return true;
                    case CVECTOR_EXPR:
                        return true;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return true;
                    case DOUBLE_COMPLEX:
                        return true;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return true;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case COMPLEX_EXPR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return false;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return true;
                    case CVECTOR_EXPR:
                        return true;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return false;
                    case DOUBLE_COMPLEX:
                        return true;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return true;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case CVECTOR_EXPR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return false;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return false;
                    case CVECTOR_EXPR:
                        return true;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return false;
                    case DOUBLE_COMPLEX:
                        return false;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return false;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case CMATRIX_EXPR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return false;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return false;
                    case CVECTOR_EXPR:
                        return false;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return false;
                    case DOUBLE_COMPLEX:
                        return false;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return false;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return false;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return false;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case DOUBLE_DOUBLE: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return false;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return false;
                    case CVECTOR_EXPR:
                        return false;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return true;
                    case DOUBLE_COMPLEX:
                        return true;
//                    case DBL_TO_VECT_DBL:return true;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return true;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return true;
//                    case COMPLEX_COMPLEX:
//                        return true;
////                    case CMX_TO_VECT_DBL:return true;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return true;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case DOUBLE_COMPLEX: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return false;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return false;
                    case CVECTOR_EXPR:
                        return false;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return false;
                    case DOUBLE_COMPLEX:
                        return true;
//                    case DBL_TO_VECT_DBL:return false;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return false;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return false;
//                    case COMPLEX_COMPLEX:
//                        return true;
////                    case CMX_TO_VECT_DBL:return false;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return false;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case DOUBLE_CVECTOR: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return false;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return false;
                    case CVECTOR_EXPR:
                        return false;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return false;
                    case DOUBLE_COMPLEX:
                        return false;
//                    case DBL_TO_VECT_DBL:return false;
                    case DOUBLE_CVECTOR:
                        return true;
//                    case DBL_TO_MATX_DBL:return false;
                    case DOUBLE_CMATRIX:
                        return true;
//                    case CMX_TO_SCLR_DBL:return false;
//                    case COMPLEX_COMPLEX:
//                        return false;
////                    case CMX_TO_VECT_DBL:return false;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return false;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
            case DOUBLE_CMATRIX: {
                switch (second) {
                    case DOUBLE_NBR:
                        return false;
                    case COMPLEX_NBR:
                        return false;
                    case CVECTOR_NBR:
                        return false;
                    case CMATRIX_NBR:
                        return false;
                    case DOUBLE_EXPR:
                        return false;
                    case COMPLEX_EXPR:
                        return false;
                    case CVECTOR_EXPR:
                        return false;
                    case CMATRIX_EXPR:
                        return true;
                    case DOUBLE_DOUBLE:
                        return false;
                    case DOUBLE_COMPLEX:
                        return false;
//                    case DBL_TO_VECT_DBL:return false;
                    case DOUBLE_CVECTOR:
                        return false;
//                    case DBL_TO_MATX_DBL:return false;
                    case DOUBLE_CMATRIX:
                        return true;
////                    case CMX_TO_SCLR_DBL:return false;
//                    case COMPLEX_COMPLEX:
//                        return false;
////                    case CMX_TO_VECT_DBL:return false;
//                    case COMPLEX_CVECTOR:
//                        return false;
////                    case CMX_TO_MATX_DBL:return false;
//                    case COMPLEX_CMATRIX:
//                        return true;
                }
            }
//            case COMPLEX_COMPLEX: {
//                switch (second) {
//                    case DOUBLE_NBR:
//                        return false;
//                    case COMPLEX_NBR:
//                        return false;
//                    case CVECTOR_NBR:
//                        return false;
//                    case CMATRIX_NBR:
//                        return false;
//                    case DOUBLE_EXPR:
//                        return false;
//                    case COMPLEX_EXPR:
//                        return false;
//                    case CVECTOR_EXPR:
//                        return false;
//                    case CMATRIX_EXPR:
//                        return true;
//                    case DOUBLE_DOUBLE:
//                        return false;
//                    case DOUBLE_COMPLEX:
//                        return false;
////                    case DBL_TO_VECT_DBL:return false;
//                    case DOUBLE_CVECTOR:
//                        return false;
////                    case DBL_TO_MATX_DBL:return false;
//                    case DOUBLE_CMATRIX:
//                        return false;
////                    case CMX_TO_SCLR_DBL:return false;
//                    case COMPLEX_COMPLEX:
//                        return true;
////                    case CMX_TO_VECT_DBL:return false;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return false;
//                    case COMPLEX_CMATRIX:
//                        return true;
//                }
//            }
//            case COMPLEX_CVECTOR: {
//                switch (second) {
//                    case DOUBLE_NBR:
//                        return false;
//                    case COMPLEX_NBR:
//                        return false;
//                    case CVECTOR_NBR:
//                        return false;
//                    case CMATRIX_NBR:
//                        return false;
//                    case DOUBLE_EXPR:
//                        return false;
//                    case COMPLEX_EXPR:
//                        return false;
//                    case CVECTOR_EXPR:
//                        return false;
//                    case CMATRIX_EXPR:
//                        return true;
//                    case DOUBLE_DOUBLE:
//                        return false;
//                    case DOUBLE_COMPLEX:
//                        return false;
////                    case DBL_TO_VECT_DBL:return false;
//                    case DOUBLE_CVECTOR:
//                        return false;
////                    case DBL_TO_MATX_DBL:return false;
//                    case DOUBLE_CMATRIX:
//                        return false;
////                    case CMX_TO_SCLR_DBL:return false;
//                    case COMPLEX_COMPLEX:
//                        return false;
////                    case CMX_TO_VECT_DBL:return false;
//                    case COMPLEX_CVECTOR:
//                        return true;
////                    case CMX_TO_MATX_DBL:return false;
//                    case COMPLEX_CMATRIX:
//                        return true;
//                }
//            }
//            case COMPLEX_CMATRIX: {
//                switch (second) {
//                    case DOUBLE_NBR:
//                        return false;
//                    case COMPLEX_NBR:
//                        return false;
//                    case CVECTOR_NBR:
//                        return false;
//                    case CMATRIX_NBR:
//                        return false;
//                    case DOUBLE_EXPR:
//                        return false;
//                    case COMPLEX_EXPR:
//                        return false;
//                    case CVECTOR_EXPR:
//                        return false;
//                    case CMATRIX_EXPR:
//                        return true;
//                    case DOUBLE_DOUBLE:
//                        return false;
//                    case DOUBLE_COMPLEX:
//                        return false;
////                    case DBL_TO_VECT_DBL:return false;
//                    case DOUBLE_CVECTOR:
//                        return false;
////                    case DBL_TO_MATX_DBL:return false;
//                    case DOUBLE_CMATRIX:
//                        return false;
////                    case CMX_TO_SCLR_DBL:return false;
//                    case COMPLEX_COMPLEX:
//                        return false;
////                    case CMX_TO_VECT_DBL:return false;
//                    case COMPLEX_CVECTOR:
//                        return false;
////                    case CMX_TO_MATX_DBL:return false;
//                    case COMPLEX_CMATRIX:
//                        return true;
//                }
//            }
        }
        return false;
    }

    /**
     * tries to if cast down is possible
     *
     * @param other
     * @return
     */
    public static boolean isNarrow(Expr me, ExprDim other) {
        for (ExprType exprType : toExprDefinitionType(other)) {
            if (me.isNarrow(exprType)) {
                return true;
            }
        }
        return false;
    }

    public static ExprType[] toExprDefinitionType(ExprDim other) {
        switch (other) {
            case SCALAR:
                return new ExprType[]{ExprType.DOUBLE_NBR, ExprType.COMPLEX_NBR, ExprType.DOUBLE_EXPR, ExprType.COMPLEX_EXPR, ExprType.DOUBLE_DOUBLE/*, ExprType.COMPLEX_COMPLEX*/};
            case VECTOR:
                return new ExprType[]{ExprType.CVECTOR_NBR, ExprType.CVECTOR_EXPR, ExprType.DOUBLE_CVECTOR/*, ExprType.COMPLEX_CVECTOR, ExprType.COMPLEX_CMATRIX*/};
            case MATRIX:
                return new ExprType[]{ExprType.CMATRIX_NBR, ExprType.CMATRIX_EXPR, ExprType.DOUBLE_CMATRIX/*, ExprType.COMPLEX_CMATRIX*/};
        }
        return new ExprType[0];
    }

    public static boolean is(Expr me, ExprDim other) {
        for (ExprType exprType : toExprDefinitionType(other)) {
            if (me.is(exprType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * tries to if cast down is possible
     *
     * @param other
     * @return
     */
    public static Expr narrow(Expr me, ExprDim other) {
        for (ExprType exprType : toExprDefinitionType(other)) {
            if (me.isNarrow(exprType)) {
                return narrow(me, exprType);
            }
        }
        throw new ClassCastException("Unable to Cast to " + other + " :: " + me.getClass().getName() + " = " + me.toString());
    }

    public static boolean isNarrowAll(ExprType other, Collection<Expr> expressions) {
        for (Expr expression : expressions) {
            if (!expression.isNarrow(other)) {
                return false;
            }
        }
        return true;
    }

    public static Expr[] narrowAll(ExprType other, Expr[] expressions) {
        return Arrays.stream(expressions).map(x -> x.narrow(other)).toArray(Expr[]::new);
    }

    public static List<Expr> narrowAll(ExprType other, List<Expr> expressions) {
        return expressions.stream().map(x -> x.narrow(other)).collect(Collectors.toList());
    }

    public static Expr[] narrowAllArr(ExprType other, List<Expr> expressions) {
        return expressions.stream().map(x -> x.narrow(other)).toArray(Expr[]::new);
    }

    public static boolean isNarrowAll(ExprType other, Expr[] expressions) {
        for (Expr expression : expressions) {
            if (!expression.isNarrow(other)) {
                return false;
            }
        }
        return true;
    }

    public static String getTitle(Expr me) {
        return null;
    }

    public static Expr mul(Expr me, int other) {
        return me.mul((double) other);
    }

    public static Expr add(Expr me, double other) {
        return Plus.of(me, Maths.expr(other));
    }

    public static Expr add(Expr me, Expr other) {
        return Plus.of(me, other);
    }

    public static Expr neg(Expr me) {
        return Neg.of(me);
    }

    public static Expr inv(Expr me) {
        return Inv.of(me);
    }

    public static Expr not(Expr me) {
        return NotExpr.of(me);
    }

    public static Expr conj(Expr me) {
        return Conj.of(me);
    }

    public static Expr cos(Expr me) {
        return Cos.of(me);
    }

    public static Expr cosh(Expr me) {
        return Cosh.of(me);
    }

    public static Expr sin(Expr me) {
        return Sin.of(me);
    }

    public static Expr sincard(Expr me) {
        return Sincard.of(me);
    }

    public static Expr sinh(Expr me) {
        return Sinh.of(me);
    }

    public static Expr tan(Expr me) {
        return Tan.of(me);
    }

    public static Expr cotan(Expr me) {
        return Cotan.of(me);
    }

    public static Expr tanh(Expr me) {
        return Tanh.of(me);
    }

    public static Expr cotanh(Expr me) {
        return Cotanh.of(me);
    }

    public static Expr atan(Expr me) {
        return Atan.of(me);
    }

    public static Expr acotan(Expr me) {
        return Acotan.of(me);
    }

    public static Expr atanh(Expr me) {
        throw new IllegalArgumentException("Not yet supported");
    }

    public static Expr acos(Expr me) {
        return Acos.of(me);
    }

    public static Expr asin(Expr me) {
        return Asin.of(me);
    }

    public static Expr asinh(Expr me) {
        return Asinh.of(me);
    }

    public static Expr acosh(Expr me) {
        return Acosh.of(me);
    }

    public static Expr rdiv(Expr me, double other) {
        return Maths.expr(other).div(me);
    }

    public static Expr rmul(Expr me, double other) {
        return Maths.expr(other).mul(me);
    }

    public static Expr radd(Expr me, double other) {
        return Maths.expr(other).plus(me);
    }

    public static Expr rsub(Expr me, double other) {
        return Maths.expr(other).sub(me);
    }

    public static Expr mul(Expr me, Domain domain) {
        if (domain.dimension() <= me.getDomain().dimension()) {
            if (domain.isUnbounded()) {
                return me;
            }
        }
        return Mul.of(domain, me);
    }

    public static Expr mul(Expr me, double other) {
        return Mul.of(Maths.expr(other), me);
    }

    public static Expr mul(Expr me, Complex other) {
        if (other.isNarrow(ExprType.DOUBLE_EXPR)) {
            if (other.getDomain().isUnbounded()) {
                return Mul.of(me, Maths.expr(other.getReal()));
            } else {
                return Mul.of(me, Maths.expr(other.getReal())).mul(other.getDomain());
            }
        }
        return Mul.of(other, me);
    }

    public static Expr simplify(Expr me, SimplifyOptions options) {
        if (options == null) {
            options = SimplifyOptions.DEFAULT;
        }
        String t = me.getTitle();
        Expr o = ExpressionRewriterFactory.getComputationSimplifier().rewriteOrSame(me, options.getTargetExprType());
        if (t != null && options.isPreserveRootName()) {
            return o.setTitle(t);
        }
        return o;
    }

    public static double norm(Expr a) {
        //TODO conjugate a
        Expr aCong = a.conj();
        Expr c = Maths.Config.getScalarProductOperator().eval(a, aCong);
        return c.sqrt().toComplex().absdbl();
    }

    public static Expr normalize(Expr me) {
        double n = (1.0 / Maths.norm(me));
        if (n == 1) {
            return me;
        }
        Expr mul = me.mul(n);
        //preserve names and properties
        mul = ExprDefaults.copyProperties(me, mul);
        return mul;
    }

    public static TsonElement toTsonElement(Expr me, TsonObjectContext context) {
        return Tson.ofFunction().name("expr").add(
                Tson.of(me.toString())
        ).build();
    }

    public static Integer getIntProperty(Expr me, String name) {
        Number property = (Number) me.getProperty(name);
        return property == null ? null : property.intValue();
    }

    public static Long getLongProperty(Expr me, String name) {
        Number property = (Number) me.getProperty(name);
        return property == null ? null : property.longValue();
    }

    public static Double getDoubleProperty(Expr me, String name) {
        Number property = (Number) me.getProperty(name);
        return property == null ? null : property.doubleValue();
    }

    public static String getStringProperty(Expr me, String name) {
        Object property = me.getProperty(name);
        return property == null ? null : property.toString();
    }

    public static Expr setProperties(Expr me, Map<String, Object> map) {
        return me.setProperties(map, false);
    }

    public static Expr setMergedProperties(Expr me, Map<String, Object> map) {
        return me.setProperties(map, true);
    }

    public static Expr setProperties(Expr me, Map<String, Object> map, boolean merge) {
        if (map == null || map.isEmpty()) {
            return me;
        }
        return Any.of(me, me.getTitle(), map);
    }

    public static Expr setProperty(Expr me, String name, Object value) {
        HashMap<String, Object> m = new HashMap<>(1);
        m.put(name, value);
        return me.setProperties(m, true);
    }

    public static boolean hasProperties(Expr me) {
        return false;
    }

    public static Object getProperty(Expr me, String name) {
        return null;
    }

    public static Map<String, Object> getProperties(Expr me) {
        return Collections.EMPTY_MAP;
    }

    public static double toDouble(Expr me) {
        return me.toComplex().toDouble();
    }

    public static Expr setTitle(Expr me, String name) {
        if (name != null) {
            return Any.of(me, name, me.getProperties());
        } else {
            return me;
        }
    }

    public static Expr mul(Expr me, Expr other) {
//        switch (other.getType()) {
//            case DOUBLE_EXPR: {
//                if (other.getDomain().isUnbounded()) {
//                    return me.mul(other.toDouble());
//                }
//                return me.mul(other.toDouble()).mul(other.getDomain());
//            }
//            case COMPLEX_EXPR: {
//                if (other.getDomain().isUnbounded()) {
//                    return me.mul(other.toComplex());
//                }
//                return me.mul(other.toComplex()).mul(other.getDomain());
//            }
//        }
        return Mul.of(me, other);
    }

    public static Expr add(Expr me, int other) {
        return Plus.of(me, Maths.expr(other));
    }

    public static Expr div(Expr me, int other) {
        return Div.of(me, Maths.expr(other));
    }

    public static Expr div(Expr me, double other) {
        return Div.of(me, Maths.expr(other));
    }

    public static Expr div(Expr me, Expr other) {
        return Div.of(me, other);
    }

    public static Expr rem(Expr me, Expr other) {
        return Reminder.of(me, other);
    }

    public static Expr pow(Expr me, Expr other) {
        return Pow.of(me, other);
    }

    public static Expr sub(Expr me, int other) {
        return Minus.of(me, Maths.expr(other));
    }

    public static Expr sub(Expr me, double other) {
        return Minus.of(me, Maths.expr(other));
    }

    public static Expr sub(Expr me, Expr other) {
        return Minus.of(me, other);
    }

    public static Expr eq(Expr me, double other) {
        return eq(me, Maths.expr(other));
    }

    public static Expr eq(Expr me, Expr other) {
        return EqExpr.of(me.toDD(), other.toDD());
    }

    public static Expr ne(Expr me, double other) {
        return ne(me, Maths.expr(other));
    }

    public static Expr ne(Expr me, Expr other) {
        return NeExpr.of(me.toDD(), other.toDD());
    }

    public static Expr lt(Expr me, double other) {
        return lt(me, Maths.expr(other));
    }

    public static Expr lt(Expr me, Expr other) {
        return LtExpr.of(me.toDD(), other.toDD());
    }

    public static Expr lte(Expr me, double other) {
        return lte(me, Maths.expr(other));
    }

    public static Expr lte(Expr me, Expr other) {
        return LteExpr.of(me.toDD(), other.toDD());
    }

    public static Expr gt(Expr me, double other) {
        return gt(me, Maths.expr(other));
    }

    public static Expr gt(Expr me, Expr other) {
        return GtExpr.of(me.toDD(), other.toDD());
    }

    public static Expr gte(Expr me, double other) {
        return gte(me, Maths.expr(other));
    }

    public static Expr gte(Expr me, Expr other) {
        return GteExpr.of(me.toDD(), other.toDD());
    }

    public static Expr or(Expr me, double other) {
        return or(me, Maths.expr(other));
    }

    public static Expr or(Expr me, Expr other) {
        return OrExpr.of(me.toDD(), other.toDD());
    }

    public static Expr and(Expr me, double other) {
        return and(me, Maths.expr(other));
    }

    public static Expr and(Expr me, Expr other) {
        return AndExpr.of(me.toDD(), other.toDD());
    }

    public static Expr mul(Expr me, Geometry domain) {
        return Mul.of(me, Maths.expr(domain));
    }

    public static String toString(Expr me) {
        return FormatFactory.toString(me);
    }

    public static boolean isNaN(Expr me) {
        return ExprDefaults.isNaNAny(me.getChildren());
    }

    public static boolean isNaNAny(List<Expr> expressions) {
        for (Expr expression : expressions) {
            if (expression.isNaN()) {
                return true;
            }
        }
        return false;
    }

    public static void formatFunction(String name, List arguments, ObjectFormatContext context) {
        context.append(name).append("(");
        ObjectFormatParamSet format = context.getParams();
        if (arguments.size() == 1) {
            format = format.remove(FormatFactory.REQUIRED_PARS);
            context.format(arguments.get(0), format);
        } else {
            format = format.add(FormatFactory.REQUIRED_PARS);
            for (int i = 0; i < arguments.size(); i++) {
                Object a = arguments.get(i);
                if (i > 0) {
                    context.append(", ");
                }
                context.format(a, format);
            }
        }
        context.append(")");
    }

    public static Expr getChild(int index, List<Expr> one) {
        return one.get(index);
    }

    public static Expr getChild(int index, Expr[] one) {
        return one[index];
    }

    public static Expr getChild(int index, Expr one) {
        if (index != 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return one;
    }

    public static Expr getChild(int index, Expr one, Expr two) {
        switch (index) {
            case 0:
                return one;
            case 1:
                return two;
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public static Expr getChild(int index, Expr one, Expr two, Expr three) {
        switch (index) {
            case 0:
                return one;
            case 1:
                return two;
            case 2:
                return three;
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public static NumberExpr toNumber(Expr e) {
        Complex c = e.toComplex();
        if (c.isReal()) {
            return Maths.expr(c.toDouble()).toNumber();
        }
        return c;
    }

    public static Expr sqrt(Expr me) {
        return Sqrt.of(me);
    }

    public static Expr sqrt(Expr me, int n) {
        return Sqrtn.of(me, n);
    }

    public static Expr sqr(Expr me) {
        return Sqrt.of(me);
    }

    public static Expr log(Expr me) {
        return Log.of(me);
    }

    public static Expr log10(Expr me) {
        return Log10.of(me);
    }

    public static Expr abs(Expr me) {
        return Abs.of(me);
    }

    public static Expr db(Expr me) {
        return Db.of(me);
    }

    public static Expr db2(Expr me) {
        return Db2.of(me);
    }

    public static Expr exp(Expr me) {
        return Exp.of(me);
    }

    public static Expr imag(Expr me) {
        throw new IllegalArgumentException("Unsupported Imag");
    }

    public static Expr real(Expr me) {
        throw new IllegalArgumentException("Unsupported Real");
    }
}
