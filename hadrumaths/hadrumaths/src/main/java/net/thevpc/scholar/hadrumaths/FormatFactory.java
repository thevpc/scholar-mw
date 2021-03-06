package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.collections.ClassMap;
import net.thevpc.common.collections.LRUMap;
import net.thevpc.scholar.hadrumaths.format.*;
import net.thevpc.scholar.hadrumaths.format.impl.*;
import net.thevpc.scholar.hadrumaths.format.params.*;
import net.thevpc.scholar.hadrumaths.geom.Polygon;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.conv.DefaultDoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Imag;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Real;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CExp;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.Any;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.AbstractComparatorExpr;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.Neg;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 23 juil. 2005 Time: 11:34:42 To
 * change this template use File | Settings | File Templates.
 */
public class FormatFactory extends AbstractFactory {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final ClassMap<ObjectFormat> map = new ClassMap<ObjectFormat>(Object.class, ObjectFormat.class, 30);
    private static final LRUMap<String, SimpleDateFormat> dateFormats = new LRUMap<String, SimpleDateFormat>(10);
    private static final PropertyChangeListener cacheEnabledListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            boolean b = (boolean) evt.getNewValue();
            if (!b) {
                dateFormats.clear();
            }
        }
    };
    public static XObjectFormatParam X = new XObjectFormatParam("X");
    public static YObjectFormatParam Y = new YObjectFormatParam("Y");
    public static ZObjectFormatParam Z = new ZObjectFormatParam("Z");
    public static RequireParenthesesObjectFormatParam REQUIRED_PARS = RequireParenthesesObjectFormatParam.INSTANCE;
    public static RequireFloatFormatParam REQUIRED_FLOAT = RequireFloatFormatParam.INSTANCE;
    public static ComplexIObjectFormatParam I = ComplexIObjectFormatParam.I;
    public static ComplexIObjectFormatParam I_J = ComplexIObjectFormatParam.J;
    public static ComplexIObjectFormatParam I_HAT = ComplexIObjectFormatParam.I_HAT;
    public static DomainObjectFormatParam NO_DOMAIN = new DomainObjectFormatParam(DomainObjectFormatParam.Type.NONE, true);
    public static DomainObjectFormatParam GATE_DOMAIN = new DomainObjectFormatParam(DomainObjectFormatParam.Type.GATE, false);
    public static DomainObjectFormatParam NON_FULL_GATE_DOMAIN = new DomainObjectFormatParam(DomainObjectFormatParam.Type.GATE, true);
    public static ProductObjectFormatParam PRODUCT_STAR = new ProductObjectFormatParam("*");
    public static ProductObjectFormatParam PRODUCT_NONE = new ProductObjectFormatParam(null);
    public static ProductObjectFormatParam PRODUCT_DOTSTAR = new ProductObjectFormatParam(".*");
    public static ObjectFormatParamSet toStringFormat = new ObjectFormatParamSet();

    static {
        dateFormats.put("date", DATE_FORMAT);
        dateFormats.put("datetime", DATE_TIME_FORMAT);
        dateFormats.put("timestamp", TIMESTAMP_FORMAT);
        Maths.Config.addConfigChangeListener("cacheEnabled", cacheEnabledListener);
    }

    static {
        register(Enum.class, new AbstractObjectFormat() {
            @Override
            public void format(Object o, ObjectFormatContext context) {
                context.append(o);
            }
        });
        register(ComplexMatrix.class, new MatrixObjectFormat());
        register(DefaultDoubleToComplex.class, new CFunctionXYObjectFormat());
        register(CExp.class, new CExpObjectFormat());
//        register(DCxyAbstractSum.class, new CAbstractSumFunctionXYFormatter());
        register(Linear.class, new LinearObjectFormat());
        register(CosXCosY.class, new CosCosObjectFormat());
        register(CosXPlusY.class, new CosXPlusYObjectFormat());
//        register(DomainX.class, new DomainXFormatter());
        register(Domain.class, new DomainObjectFormat());
        register(DomainExpr.class, new DomainExprObjectFormat());
        register(DoubleToVector.class, new DoubleToVectorObjectFormat());
        register(DoubleToMatrix.class, new DoubleToMatrixObjectFormat());
        register(Inv.class, new InvObjectFormat());
        register(Neg.class, new NegObjectFormat());
        register(Plus.class, new PlusObjectFormat());
        register(Pow.class, new PowObjectFormat());
        register(Mul.class, new MulObjectFormat());
        register(Div.class, new DivObjectFormat());
        register(Reminder.class, new ReminderObjectFormat());
        register(Minus.class, new SubObjectFormat());
        register(Any.class, new AnyObjectFormat());
        register(AbstractComparatorExpr.class, new ComparatorExprObjectFormat());
        register(UFunction.class, new UFunctionObjectFormat());
//        register(DDxToDDxy.class, new DDxToDDxyFormatter());
        register(Complex.class, new ComplexObjectFormat());
        register(DDyIntegralX.class, new DDyIntegralXObjectFormat());
        register(DoubleParam.class, new ParamObjectFormat());
        register(Param.class, new ParamObjectFormat());
        register(DoubleValue.class, new DoubleValueObjectFormat());
//        register(DoubleX.class, new DoubleXFormatter());
        register(Double.class, new DoubleObjectFormat());
        register(Number.class, new NumberObjectFormat());
//        register(DDxyToDDx.class, new DDxyToDDxFormatter());
        register(DefaultComplexValue.class, new ComplexXYObjectFormat());
        register(YY.class, new YYObjectFormat());
        register(XX.class, new XXObjectFormat());
        register(ZZ.class, new ZZObjectFormat());
        register(Shape2D.class, new Shape2DObjectFormat());
        register(CDiscrete.class, new CDiscreteObjectFormat());
        register(Polygon.class, new PolygonObjectFormat());
        register(Polyhedron.class, new PolyhedronObjectFormat());
        register(String.class, new StringObjectFormat());
        register(DDzIntegralXY.class, new DDzIntegralXYObjectFormat());
        register(VDiscrete.class, new VDiscreteObjectFormat());
        register(Real.class, new RealObjectFormat());
        register(Imag.class, new ImagObjectFormat());
        register(FunctionExpr.class, (ObjectFormat<FunctionExpr>) (o, context) -> ExprDefaults.formatFunction(o.getName(), o.getChildren(), context));
        register(Vector.class,(ObjectFormat<Vector>)(o, context) -> {
            context.append("{");
            boolean first=true;
            for (Object o1 : o) {
                if(first){
                    first=false;
                }else{
                    context.append(",");
                }
                context.format(o1);
            }
            context.append("}");
        });
//        register(AbstractComparatorExpr.class, new ObjectFormat() {
//            @Override
//            public String format(Object o, ObjectFormatParamSet format) {
//                StringBuilder sb = new StringBuilder();
//                format(sb, o, format);
//                return sb.toString();
//            }
//
//            @Override
//            public void format(StringBuilder sb, Object o, ObjectFormatParamSet format) {
//                sb.append(o.toString());
//            }
//        });

    }

    private FormatFactory() {
    }

    public static void register(Class clz, ObjectFormat t) {
        map.put(clz, t);
    }

    public static String toString(Object o) {
        return format(o, toStringFormat);
    }

//    public static String dblquadString(DefaultDoubleToComplex f, boolean useGate) {
//        if (useGate) {
//            DomainXY d = f.getDomain();
//            return ("dblquad('"
//                    + format(f, PRODUCT_DOTSTAR) + "',"
//                    + d.xmin + ","
//                    + d.xmax + ","
//                    + d.ymin + ","
//                    + d.ymax
//                    + ")");
//        } else {
//            f = f.simplify();
//            DDxy r = f.getReal();
//            DDxy i = f.getImag();
//            StringBuilder sb = new StringBuilder();
//            DDxy[] s = null;
//
//            s = new DDxy[]{r};
//            if (r instanceof DDxyAbstractSum) {
//                s = ((DDxyAbstractSum) r).getSegments();
//            }
//            for (int j = 0; j < s.length; j++) {
//                DomainXY d = s[j].getDomain();
//                if (sb.length() > 0) {
//                    sb.append(" + ");
//                }
//                String fs = format(s[j], NO_DOMAIN, PRODUCT_DOTSTAR);
//                fs = " (0 .* y + 0 .* x) + " + fs;
//                sb.append("dblquad('").append(fs).append("',").append(d.xmin).append(",").append(d.xmax).append(",").append(d.ymin).append(",").append(d.ymax).append(")");
//            }
//
//            s = new DDxy[]{i};
//            if (i instanceof DDxyAbstractSum) {
//                s = ((DDxyAbstractSum) i).getSegments();
//            }
//            for (int j = 0; j < s.length; j++) {
//                DomainXY d = s[j].getDomain();
//                if (sb.length() > 0) {
//                    sb.append(" + ");
//                }
//                String fs = format(s[j], NO_DOMAIN, PRODUCT_DOTSTAR);
//                fs = " (0 .* y + 0 .* x) + " + fs;
//                sb.append("i .* dblquad('").append(fs).append("',").append(d.xmin).append(",").append(d.xmax).append(",").append(d.ymin).append(",").append(d.ymax).append(")");
//            }
//            return sb.toString();
//        }
//    }
//
//    public static String symdblquadString(DefaultDoubleToComplex f) {
//        f = f.simplify();
//        DDxy r = f.getReal();
//        DDxy i = f.getImag();
//        StringBuilder sb = new StringBuilder();
//        DDxy[] s = null;
//
//        s = new DDxy[]{r};
//        if (r instanceof DDxyAbstractSum) {
//            s = ((DDxyAbstractSum) r).getSegments();
//        }
//        for (int j = 0; j < s.length; j++) {
//            DomainXY d = s[j].getDomain();
//            if (sb.length() > 0) {
//                sb.append(" + ");
//            }
//            sb.append("symdblquad('").append(format(s[j], NO_DOMAIN)).append("',").append(d.xmin).append(",").append(d.xmax).append(",").append(d.ymin).append(",").append(d.ymax).append(")");
//        }
//
//        s = new DDxy[]{i};
//        if (i instanceof DDxyAbstractSum) {
//            s = ((DDxyAbstractSum) i).getSegments();
//        }
//        for (int j = 0; j < s.length; j++) {
//            DomainXY d = s[j].getDomain();
//            if (sb.length() > 0) {
//                sb.append(" + ");
//            }
//            sb.append("i * symdblquad('").append(format(s[j], NO_DOMAIN)).append("',").append(d.xmin).append(",").append(d.xmax).append(",").append(d.ymin).append(",").append(d.ymax).append(")");
//        }
//        return sb.toString();
//    }
//
//    public static String scalarProductFormatter(DomainXY domain0, DDxy f1, DDxy f2, ObjectFormatParam... format) {
//        FormatParamArray formatArray = new FormatParamArray(format);
//        DomainXY domain = domain0 == null ? f1.intersect(f2) : f1.intersect(f2, domain0);
//        if (domain.isEmpty()) {
//            return "0";
//        }
//        ArrayList<DDxy> a1 = linearize(new DDxy[]{f1}, null);
//        ArrayList<DDxy> a2 = linearize(new DDxy[]{f2}, null);
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < a1.size(); i++) {
//            for (int j = 0; j < a2.size(); j++) {
//                if (i > 0 || j > 0) {
//                    sb.append(" + ");
//                }
//                sb.append("dblquad('(0.0 * x +0.0 * y) + (");
//                sb.append(format(a1.get(i), formatArray.clone().set(NO_DOMAIN).toArray()));
//                sb.append(") * (");
//                sb.append(format(a2.get(j), formatArray.clone().set(NO_DOMAIN).toArray()));
//                sb.append(")', ").append(domain.xmin);
//                sb.append(", ").append(domain.xmax);
//                sb.append(", ").append(domain.ymin);
//                sb.append(", ").append(domain.ymax);
//                sb.append(")");
//            }
//        }
//        return sb.toString().replace("*", ".*");
//
//    }
//
//    private static ArrayList<DDxy> linearize(DDxy[] sum, ArrayList<DDxy> putInto) {
//        if (putInto == null) {
//            putInto = new ArrayList<DDxy>();
//        }
//        for (int i = 0; i < sum.length; i++) {
//            DDxy dFunction = sum[i];
//            if (dFunction instanceof DDxyAbstractSum) {
//                linearize(((DDxyAbstractSum) dFunction).getSegments(), putInto);
//            } else {
//                putInto.add(dFunction);
//            }
//        }
//        return putInto;
//    }
//
//    public static String formatScalarProduct(DomainXY domain0, DefaultDoubleToComplex f1, DefaultDoubleToComplex f2, ObjectFormatParam... format) {
//        FormatParamArray formatArray = new FormatParamArray(format);
//        DomainXY domain = domain0 == null ? f1.intersect(f2) : f1.intersect(f2, domain0);
//        if (domain.isEmpty()) {
//            return "0";
//        }
//        ArrayList<DefaultDoubleToComplex> a1 = linearize(new DefaultDoubleToComplex[]{f1}, null);
//        ArrayList<DefaultDoubleToComplex> a2 = linearize(new DefaultDoubleToComplex[]{f2}, null);
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < a1.size(); i++) {
//            for (int j = 0; j < a2.size(); j++) {
//                if (i > 0 || j > 0) {
//                    sb.append(" + ");
//                }
//                String s1 = format(a1.get(i), formatArray.clone().set(NO_DOMAIN).toArray());
//                String s2 = format(a2.get(j), formatArray.clone().set(NO_DOMAIN).toArray());
//                sb.append("dblquad('(0.0 * x +0.0 * y) + (");
//                sb.append(s1);
//                sb.append(") * (");
//                sb.append(s2);
//                sb.append(")', ").append(domain.xmin);
//                sb.append(", ").append(domain.xmax);
//                sb.append(", ").append(domain.ymin);
//                sb.append(", ").append(domain.ymax);
//                sb.append(")");
//            }
//        }
//        return sb.toString().replace("*", ".*");
//
//    }
//
//    private static ArrayList<DefaultDoubleToComplex> linearize(DefaultDoubleToComplex[] sum, ArrayList<DefaultDoubleToComplex> putInto) {
//        if (putInto == null) {
//            putInto = new ArrayList<DefaultDoubleToComplex>();
//        }
//        for (int i = 0; i < sum.length; i++) {
//            DefaultDoubleToComplex cFunctionXY = sum[i];
//            if (cFunctionXY instanceof DCxyAbstractSum) {
//                linearize(((DCxyAbstractSum) cFunctionXY).getSegments(), putInto);
//            } else {
//                if (cFunctionXY.getReal() != FunctionFactory.DZEROXY) {
//                    putInto.add(new DefaultDoubleToComplex(cFunctionXY.getReal(), FunctionFactory.DZEROXY));
//                }
//                if (cFunctionXY.getImag() != FunctionFactory.DZEROXY) {
//                    putInto.add(new DefaultDoubleToComplex(FunctionFactory.DZEROXY, cFunctionXY.getImag()));
//                }
//            }
//        }
//        return putInto;
//    }

    public static String format(Object o, ObjectFormatParamSet format) {
        ObjectFormat best = map.getRequired(o.getClass());
        StringBuilder sb = new StringBuilder();
        DefaultObjectFormatContext u = new DefaultObjectFormatContext(sb, format);
        best.format(o, u);
        return sb.toString();
    }

    public static String format(Object o) {
        return format(o, ObjectFormatParamSet.EMPTY);
    }

    @SuppressWarnings("unchecked")
    public static String format(Object o, ObjectFormatParam... format) {
        return format(o, new ObjectFormatParamSet(format));
    }

    public static String toParamString(double b, DoubleObjectFormatParam df, boolean prefixWithSign, boolean zeroIsEmpty, boolean prefixWithSpace) {
        StringBuilder sb = new StringBuilder();
        if (b == 0) {
            return zeroIsEmpty ? "" : ((prefixWithSign ? "+" : "") + "0.0");
        }
        if (prefixWithSign) {
            if (prefixWithSpace) {
//                sb.append(" ");
            }
            double b0 = b;
            if (b < 0) {
                sb.append("-");
//                sb.append(" ");
                b0 = -b;
            } else if (b > 0 || Double.isNaN(b)) {
                sb.append("+");
//                sb.append(" ");
            }
            if (df != null) {
                sb.append(df.getFormat().format(b0));
            } else {
                sb.append(b0);
            }
        } else {
            if (prefixWithSpace) {
//                sb.append(" ");
            }
            if (df != null) {
                sb.append(df.getFormat().format(b));
            } else {
                sb.append(b);
            }
        }
        return sb.toString();
    }

    public static String format(Date d, String format) {
        return getDateFormat(format).format(d);
    }

    public static SimpleDateFormat getDateFormat(String format) {
        SimpleDateFormat found = dateFormats.get(format);
        if (found == null) {
            if ("date".equals(format)) {
                found = DATE_FORMAT;
            } else if ("datetime".equals(format)) {
                found = DATE_TIME_FORMAT;
            } else if ("datetime".equals(format)) {
                found = TIMESTAMP_FORMAT;
            } else {
                found = new SimpleDateFormat(format);
            }
            dateFormats.put(format, found);
        }
        return found;
    }

//    public static String formatArg(Expr e, ObjectFormatParamSet format) {
//        String s = FormatFactory.format(e, format);
//        if (needsParams(s)) {
//            return ("(" + s + ")");
//        } else {
//            return s;
//        }
//    }

//    private static boolean needsParams(String s) {
//        int pars = 0;
//        int braks = 0;
//        boolean inNumber = true;
//        for (char c : s.toCharArray()) {
//            if (c == '(') {
//                inNumber = false;
//                pars++;
//            } else if (c == ')') {
//                inNumber = false;
//                pars--;
//            } else if (c == '[') {
//                inNumber = false;
//                braks++;
//            } else if (c == ']') {
//                inNumber = false;
//                braks--;
//            } else {
//                if (pars == 0 && braks == 0) {
//                    if (c >= '0' && c <= '9') {
//                        inNumber = true;
//                    } else if (c == '.') {
//                        if (inNumber) {
//                            //ok
//                        } else {
//                            return true;
//                        }
//                    } else {
//                        inNumber = false;
//                        if (!Character.isAlphabetic(c)) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }

    public static boolean requireAppendDomain(Expr o, ObjectFormatParamSet format) {
        return (!o.getDomain().isUnbounded() && !format.containsParam(FormatFactory.NO_DOMAIN));
    }

//    public static boolean appendStarredDomain(StringBuilder sb, Expr o, ObjectFormatParamSet format) {
//        if (!o.getDomain().isFull() && !format.containsParam(FormatFactory.NO_DOMAIN)) {
//
//            ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
////            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
//            String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
//            sb.append(mul);
//            FormatFactory.format(sb, o.getDomain(), format);
//            return true;
//        }
//        return false;
//    }

    public static boolean appendStarredDomain(ObjectFormatContext context, Expr o, ObjectFormatParamSet format) {
        if (!o.getDomain().isUnbounded() && !format.containsParam(FormatFactory.NO_DOMAIN)) {

            ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
//            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
            String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
            context.append(mul);
            context.format(o.getDomain(), format);
            return true;
        }
        return false;
    }

//    public static boolean appendNonStarredDomain(StringBuilder sb, Expr o, ObjectFormatParamSet format) {
//        if (!o.getDomain().isFull() && !format.containsParam(FormatFactory.NO_DOMAIN)) {
//            FormatFactory.format(sb, o.getDomain(), format);
//            return true;
//        }
//        return false;
//    }

//    public static void format(StringBuilder sb, Object o, ObjectFormatParamSet format) {
//        DefaultObjectFormatContext u = new DefaultObjectFormatContext(sb, format);
//        format(o, u);
//    }

    public static void format(Object o, ObjectFormatContext context) {
        ObjectFormat best = map.getRequired(o.getClass());
        best.format(o, context);
    }

    public static boolean appendNonStarredDomain(ObjectFormatContext context, Expr o, ObjectFormatParamSet format) {
        if (!o.getDomain().isUnbounded() && !format.containsParam(FormatFactory.NO_DOMAIN)) {
            context.format(o.getDomain(), format);
            return true;
        }
        return false;
    }
}
