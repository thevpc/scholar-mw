package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.impl.*;
import net.vpc.scholar.hadrumaths.format.params.*;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.ClassMap;
import net.vpc.scholar.hadrumaths.util.LRUMap;

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
    private static final ClassMap<Formatter> map = new ClassMap<Formatter>(Object.class, Formatter.class, 30);
    private static final LRUMap<String, SimpleDateFormat> dateFormats = new LRUMap<String, SimpleDateFormat>(10);
    public static XFormat X = new XFormat("X");
    public static YFormat Y = new YFormat("Y");
    public static ZFormat Z = new ZFormat("Z");
    public static RequireParenthesesFormat REQUIRED_PARS = RequireParenthesesFormat.INSTANCE;
    public static DomainFormat NO_DOMAIN = new DomainFormat(DomainFormat.Type.NONE,true);
    public static DomainFormat GATE_DOMAIN = new DomainFormat(DomainFormat.Type.GATE,false);
    public static DomainFormat NON_FULL_GATE_DOMAIN = new DomainFormat(DomainFormat.Type.GATE,true);
    public static ProductFormat PRODUCT_STAR = new ProductFormat("*");
    public static ProductFormat PRODUCT_NONE = new ProductFormat(null);
    public static ProductFormat PRODUCT_DOTSTAR = new ProductFormat(".*");
    public static FormatParamSet toStringFormat = new FormatParamSet();
    private static PropertyChangeListener cacheEnabledListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            boolean b = (boolean) evt.getNewValue();
            if (!b) {
                dateFormats.clear();
            }
        }
    };

    static {
        dateFormats.put("date", DATE_FORMAT);
        dateFormats.put("datetime", DATE_TIME_FORMAT);
        dateFormats.put("timestamp", TIMESTAMP_FORMAT);
        Maths.Config.addConfigChangeListener("cacheEnabled", cacheEnabledListener);
    }

    static {
        register(Matrix.class, new MatrixFormatter());
        register(DCxy.class, new CFunctionXYFormatter());
//        register(DCxyAbstractSum.class, new CAbstractSumFunctionXYFormatter());
        register(Linear.class, new LinearFormatter());
        register(CosXCosY.class, new CosCosFormatter());
        register(CosXPlusY.class, new CosXPlusYFormatter());
        register(DDxyAbstractSum.class, new DAbstractSumFunctionXYFormatter());
//        register(DomainX.class, new DomainXFormatter());
        register(Domain.class, new DomainFormatter());
        register(DomainExpr.class, new DomainExprFormatter());
        register(DoubleToVector.class, new VDCxyFormatter());
        register(Inv.class, new InvFormatter());
        register(Neg.class, new NegFormatter());
        register(Plus.class, new PlusFormatter());
        register(Pow.class, new PowFormatter());
        register(Mul.class, new MulFormatter());
        register(Div.class, new DivFormatter());
        register(Sub.class, new SubFormatter());
        register(Any.class, new AnyFormatter());
        register(DDx.class, new DDxFormatter());
        register(DDy.class, new DDyFormatter());
        register(DDz.class, new DDzFormatter());
        register(UFunction.class, new UFunctionFormatter());
//        register(DDxToDDxy.class, new DDxToDDxyFormatter());
        register(Complex.class, new ComplexFormatter());
        register(DDyIntegralX.class, new DDyIntegralXFormatter());
        register(AbstractComposedFunction.class, new GenericFunctionFormatter());
        register(ParamExpr.class, new ParamFormatter());
        register(DoubleValue.class, new DoubleValueFormatter());
//        register(DoubleX.class, new DoubleXFormatter());
        register(Double.class, new net.vpc.scholar.hadrumaths.format.impl.DoubleFormatter());
        register(Number.class, new NumberFormatter());
//        register(DDxyToDDx.class, new DDxyToDDxFormatter());
        register(ComplexValue.class, new ComplexXYFormatter());
        register(YY.class, new YYFormatter());
        register(XX.class, new XXFormatter());
        register(AxisTransform.class, new AxisTransformFormatter());
        register(Shape.class, new DDxyPolygonFormatter());
        register(Discrete.class, new DiscreteFormatter());
        register(Polygon.class, new PolygonFormatter());
        register(Polyhedron.class, new PolyhedronFormatter());
        register(String.class, new StringFormatter());
        register(DDzIntegralXY.class, new DDzIntegralXYFormatter());
        register(VDiscrete.class, new VDiscreteFormatter());
        register(Real.class, new RealFormatter());
        register(Imag.class, new ImagFormatter());
        register(ComparatorExpr.class, new Formatter() {
            @Override
            public String format(Object o, FormatParamSet format) {
                StringBuilder sb = new StringBuilder();
                format(sb, o, format);
                return sb.toString();
            }

            @Override
            public void format(StringBuilder sb, Object o, FormatParamSet format) {
                sb.append(o.toString());
            }
        });
        register(NotExpr.class, new Formatter() {
            @Override
            public String format(Object o, FormatParamSet format) {
                StringBuilder sb = new StringBuilder();
                format(sb, o, format);
                return sb.toString();
            }

            @Override
            public void format(StringBuilder sb, Object o, FormatParamSet format) {
                sb.append(format(o, format));
            }
        });
    }

    private FormatFactory() {
    }

    public static void register(Class clz, Formatter t) {
        map.put(clz, t);
    }

    public static String toString(Object o) {
        return format(o, toStringFormat);
    }

//    public static String dblquadString(DCxy f, boolean useGate) {
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
//    public static String symdblquadString(DCxy f) {
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
//    public static String scalarProductFormatter(DomainXY domain0, DDxy f1, DDxy f2, FormatParam... format) {
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
//    public static String formatScalarProduct(DomainXY domain0, DCxy f1, DCxy f2, FormatParam... format) {
//        FormatParamArray formatArray = new FormatParamArray(format);
//        DomainXY domain = domain0 == null ? f1.intersect(f2) : f1.intersect(f2, domain0);
//        if (domain.isEmpty()) {
//            return "0";
//        }
//        ArrayList<DCxy> a1 = linearize(new DCxy[]{f1}, null);
//        ArrayList<DCxy> a2 = linearize(new DCxy[]{f2}, null);
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
//    private static ArrayList<DCxy> linearize(DCxy[] sum, ArrayList<DCxy> putInto) {
//        if (putInto == null) {
//            putInto = new ArrayList<DCxy>();
//        }
//        for (int i = 0; i < sum.length; i++) {
//            DCxy cFunctionXY = sum[i];
//            if (cFunctionXY instanceof DCxyAbstractSum) {
//                linearize(((DCxyAbstractSum) cFunctionXY).getSegments(), putInto);
//            } else {
//                if (cFunctionXY.getReal() != FunctionFactory.DZEROXY) {
//                    putInto.add(new DCxy(cFunctionXY.getReal(), FunctionFactory.DZEROXY));
//                }
//                if (cFunctionXY.getImag() != FunctionFactory.DZEROXY) {
//                    putInto.add(new DCxy(FunctionFactory.DZEROXY, cFunctionXY.getImag()));
//                }
//            }
//        }
//        return putInto;
//    }

    public static String format(Object o) {
        return format(o, FormatParamSet.EMPTY);
    }

    @SuppressWarnings("unchecked")
    public static String format(Object o, FormatParamSet format) {
        Formatter best = map.getRequired(o.getClass());
        return best.format(o, format);
    }

    public static void format(StringBuilder sb, Object o, FormatParamSet format) {
        Formatter best = map.getRequired(o.getClass());
        best.format(sb, o, format);
    }

    public static String toParamString(double b, DoubleFormat df, boolean prefixWithSign, boolean zeroIsEmpty,boolean prefixWithSpace) {
        StringBuilder sb = new StringBuilder();
        if (b == 0) {
            return zeroIsEmpty ? "" : ((prefixWithSign ? "+" : "") + "0.0");
        }
        if (prefixWithSign) {
            if(prefixWithSpace){
                sb.append(" ");
            }
            double b0 = b;
            if (b < 0) {
                sb.append("- ");
                b0 = -b;
            } else if (b > 0) {
                sb.append("+ ");
            }
            if (df != null) {
                sb.append(df.getFormat().format(b0));
            } else {
                sb.append(b0);
            }
        } else {
            if(prefixWithSpace){
                sb.append(" ");
            }
            if (df != null) {
                sb.append(df.getFormat().format(b));
            } else {
                sb.append(b);
            }
        }
        return sb.toString();
    }

//    public static String formatArg(Expr e, FormatParamSet format) {
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

    public static String format(Date d, String format) {
        return getDateFormat(format).format(d);
    }

    public static boolean requireAppendDomain(Expr o, FormatParamSet format) {
        return (!o.getDomain().isFull() && !format.containsParam(FormatFactory.NO_DOMAIN));
    }

    public static boolean appendStarredDomain(StringBuilder sb, Expr o, FormatParamSet format) {
        if (!o.getDomain().isFull() && !format.containsParam(FormatFactory.NO_DOMAIN)) {

            ProductFormat pp = format.getParam(FormatFactory.PRODUCT_STAR);
            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
            sb.append(mul);
            FormatFactory.format(sb, o.getDomain(), format);
            return true;
        }
        return false;
    }

    public static boolean appendNonStarredDomain(StringBuilder sb, Expr o, FormatParamSet format) {
        if (!o.getDomain().isFull() && !format.containsParam(FormatFactory.NO_DOMAIN)) {
            FormatFactory.format(sb, o.getDomain(), format);
            return true;
        }
        return false;
    }
}
