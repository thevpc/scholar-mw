package net.vpc.scholar.hadrumaths.interop.matlab;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.interop.matlab.impl.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.ClassMap;
import net.vpc.scholar.hadrumaths.AbstractFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.interop.matlab.impl.PlusExprToMatlabString;
import net.vpc.scholar.hadrumaths.interop.matlab.params.*;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

//import net.vpc.scholar.math.functions.dfx.DDxyToDDx;

//import net.vpc.scholar.math.functions.DomainX;
//import net.vpc.scholar.math.functions.dfx.DDxIntegralX;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 23 juil. 2005 Time: 11:34:42 To
 * change this template use File | Settings | File Templates.
 */
public class MatlabFactory extends AbstractFactory {

    private static final ClassMap<ToMatlabString> map = new ClassMap<ToMatlabString>(Object.class, ToMatlabString.class,12);

    static {
        register(Matrix.class, new MatrixToMatlabString());
        register(DoubleToComplex.class, new CFunctionXYToMatlabString());
//        register(DCxyAbstractSum.class, new CAbstractSumFunctionXYToMatlabString());
        register(Plus.class, new PlusExprToMatlabString());
//        register(DDxIntegralX.class, new DFunctionIntegralXToMatlabString());
//        register(DDxSymmetric.class, new DFunctionSymmetricXToMatlabString());
//        register(DDxToDDxy.class, new DFunctionXYFromXToMatlabString());
        register(Linear.class, new DLinearFunctionXYToMatlabString());
        register(DDy.class, new DFunctionYFromXYToMatlabString());
//        register(DDxyToDDx.class, new DFunctionXFromXYToMatlabString());
        register(Mul.class, new DFunctionProductXYToMatlabString());
        register(DoubleValue.class, new DCstFunctionXYToMatlabString());
        register(CosXCosY.class, new DCosCosFunctionXYToMatlabString());
        register(DDxyAbstractSum.class, new DAbstractSumFunctionXYToMatlabString());
//        register(DomainX.class, new DomainXToMatlabString());
        register(Domain.class, new DomainXYToMatlabString());
    }

    public static MatlabXFormat X = new MatlabXFormat("x");
    public static MatlabYFormat Y = new MatlabYFormat("y");
    public static MatlabZFormat Z = new MatlabZFormat("z");
    public static MatlabDomainFormat NO_DOMAIN = new MatlabDomainFormat(MatlabDomainFormat.Type.NONE);
    public static MatlabDomainFormat GATE_DOMAIN = new MatlabDomainFormat(MatlabDomainFormat.Type.GATE);
    public static MatlabVectorizeFormat VECTORIZE = new MatlabVectorizeFormat();

    private MatlabFactory() {
    }

    //    public static void main(String[] args) {
//        //String inStr = "alpha*n";
//        String inStr = "1/8*a*alpha/n/pi*(-i*alpha^k+4*alpha^k*n*pi+i*exp(-4*i*pi*alpha^(-k)*n*alpha^k*sin(1/2*k*pi)^2-4*i*pi*alpha^(-k)*n*alpha^k*cos(1/2*k*pi)^2)*alpha^k)";
//        System.out.println(matlabToLatex(inStr));
//    }
    public static void register(Class clz, ToMatlabString t) {
        map.put(clz, t);
    }

    @SuppressWarnings("unchecked")
    public static String toMatlabString(Object o, ToMatlabStringParam... format) {
        ToMatlabString best = map.getRequired(o.getClass());
        return best.toMatlabString(o, format);
    }

    public static String dblquadString(DoubleToComplex f, boolean useGate) {
        if (useGate) {
            Domain d = f.getDomain();
            return ("dblquad('"
                    + toMatlabString(f, VECTORIZE) + "',"
                    + d.xmin() + ","
                    + d.xmax() + ","
                    + d.ymin() + ","
                    + d.ymax()
                    + ")");
        } else {
//            f = f.simplify();
            DoubleToDouble r = f.getRealDD();
            DoubleToDouble i = f.getImagDD();
            StringBuilder sb = new StringBuilder();
            DoubleToDouble[] s = null;

            s = new DoubleToDouble[]{r};
            if (r instanceof DDxyAbstractSum) {
                s = ((DDxyAbstractSum) r).getSegments();
            }
            for (int j = 0; j < s.length; j++) {
                Domain d = s[j].getDomain();
                if (sb.length() > 0) {
                    sb.append(" + ");
                }
                String fs = toMatlabString(s[j], NO_DOMAIN, VECTORIZE);
                fs = " (0 .* y + 0 .* x) + " + fs;
                sb.append("dblquad('").append(fs).append("',").append(d.xmin()).append(",").append(d.xmax()).append(",").append(d.ymin()).append(",").append(d.ymax()).append(")");
            }

            s = new DoubleToDouble[]{i};
            if (i instanceof DDxyAbstractSum) {
                s = ((DDxyAbstractSum) i).getSegments();
            }
            for (int j = 0; j < s.length; j++) {
                Domain d = s[j].getDomain();
                if (sb.length() > 0) {
                    sb.append(" + ");
                }
                String fs = toMatlabString(s[j], NO_DOMAIN, VECTORIZE);
                fs = " (0 .* y + 0 .* x) + " + fs;
                sb.append("i .* dblquad('").append(fs).append("',").append(d.xmin()).append(",").append(d.xmax()).append(",").append(d.ymin()).append(",").append(d.ymax()).append(")");
            }
            return sb.toString();
        }
    }

    public static String symdblquadString(DoubleToComplex f) {
//        f = f.simplify();
        DoubleToDouble r = f.getRealDD();
        DoubleToDouble i = f.getImagDD();
        StringBuilder sb = new StringBuilder();
        DoubleToDouble[] s = null;

        s = new DoubleToDouble[]{r};
        if (r instanceof DDxyAbstractSum) {
            s = ((DDxyAbstractSum) r).getSegments();
        }
        for (int j = 0; j < s.length; j++) {
            Domain d = s[j].getDomain();
            if (sb.length() > 0) {
                sb.append(" + ");
            }
            sb.append("symdblquad('").append(toMatlabString(s[j], NO_DOMAIN)).append("',").append(d.xmin()).append(",").append(d.xmax()).append(",").append(d.ymin()).append(",").append(d.ymax()).append(")");
        }

        s = new DoubleToDouble[]{i};
        if (i instanceof DDxyAbstractSum) {
            s = ((DDxyAbstractSum) i).getSegments();
        }
        for (int j = 0; j < s.length; j++) {
            Domain d = s[j].getDomain();
            if (sb.length() > 0) {
                sb.append(" + ");
            }
            sb.append("i * symdblquad('").append(toMatlabString(s[j], NO_DOMAIN)).append("',").append(d.xmin()).append(",").append(d.xmax()).append(",").append(d.ymin()).append(",").append(d.ymax()).append(")");
        }
        return sb.toString();
    }

    public static String scalarProductToMatlabString(Domain domain0, DoubleToDouble f1, DoubleToDouble f2, ToMatlabStringParam... format) {
        ToMatlabStringParamArray formatArray = new ToMatlabStringParamArray(format);
        Domain domain = domain0 == null ? f1.getDomain().intersect(f2.getDomain()) : f1.getDomain().intersect(f2.getDomain()).intersect(domain0);
        if (domain.isEmpty()) {
            return "0";
        }
        ArrayList<DoubleToDouble> a1 = linearize(new DoubleToDouble[]{f1}, null);
        ArrayList<DoubleToDouble> a2 = linearize(new DoubleToDouble[]{f2}, null);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a1.size(); i++) {
            for (int j = 0; j < a2.size(); j++) {
                if (i > 0 || j > 0) {
                    sb.append(" + ");
                }
                sb.append("dblquad('(0.0 * x +0.0 * y) + (");
                sb.append(toMatlabString(a1.get(i), formatArray.clone().set(MatlabFactory.NO_DOMAIN).toArray()));
                sb.append(") * (");
                sb.append(toMatlabString(a2.get(j), formatArray.clone().set(MatlabFactory.NO_DOMAIN).toArray()));
                sb.append(")', ").append(domain.xmin());
                sb.append(", ").append(domain.xmax());
                sb.append(", ").append(domain.ymin());
                sb.append(", ").append(domain.ymax());
                sb.append(")");
            }
        }
        return sb.toString().replace("*", ".*");

    }

    private static ArrayList<DoubleToDouble> linearize(DoubleToDouble[] sum, ArrayList<DoubleToDouble> putInto) {
        if (putInto == null) {
            putInto = new ArrayList<DoubleToDouble>();
        }
        for (int i = 0; i < sum.length; i++) {
            DoubleToDouble dFunction = sum[i];
            if (dFunction instanceof DDxyAbstractSum) {
                linearize(((DDxyAbstractSum) dFunction).getSegments(), putInto);
            } else {
                putInto.add(dFunction);
            }
        }
        return putInto;
    }

    public static String scalarProductToMatlabString(Domain domain0, DoubleToComplex f1, DoubleToComplex f2, ToMatlabStringParam... format) {
        ToMatlabStringParamArray formatArray = new ToMatlabStringParamArray(format);
        Domain domain = domain0 == null ? f1.getDomain().intersect(f2.getDomain()) : f1.getDomain().intersect(f2.getDomain()).intersect(domain0);
        if (domain.isEmpty()) {
            return "0";
        }
        ArrayList<DoubleToComplex> a1 = linearize(new DoubleToComplex[]{f1}, null);
        ArrayList<DoubleToComplex> a2 = linearize(new DoubleToComplex[]{f2}, null);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a1.size(); i++) {
            for (int j = 0; j < a2.size(); j++) {
                if (i > 0 || j > 0) {
                    sb.append(" + ");
                }
                String s1 = toMatlabString(a1.get(i), formatArray.clone().set(MatlabFactory.NO_DOMAIN).toArray());
                String s2 = toMatlabString(a2.get(j), formatArray.clone().set(MatlabFactory.NO_DOMAIN).toArray());
                sb.append("dblquad('(0.0 * x +0.0 * y) + (");
                sb.append(s1);
                sb.append(") * (");
                sb.append(s2);
                sb.append(")', ").append(domain.xmin());
                sb.append(", ").append(domain.xmax());
                sb.append(", ").append(domain.ymin());
                sb.append(", ").append(domain.ymax());
                sb.append(")");
            }
        }
        return sb.toString().replace("*", ".*");

    }

    private static ArrayList<DoubleToComplex> linearize(DoubleToComplex[] sum, ArrayList<DoubleToComplex> putInto) {
        if (putInto == null) {
            putInto = new ArrayList<DoubleToComplex>();
        }
        for (int i = 0; i < sum.length; i++) {
            DoubleToComplex cFunctionXY = sum[i];
            if (cFunctionXY.getRealDD() != FunctionFactory.DZEROXY) {
                putInto.add(Maths.complex(cFunctionXY.getRealDD(), FunctionFactory.DZEROXY));
            }
            if (cFunctionXY.getImagDD() != FunctionFactory.DZEROXY) {
                putInto.add(Maths.complex(FunctionFactory.DZEROXY, cFunctionXY.getImagDD()));
            }
        }
        return putInto;
    }

    public static String toParamString(double b, MatlabDoubleFormat df, boolean prefixWithSign, boolean zeroIsEmpty) {
        StringBuilder sb = new StringBuilder();
        if (b == 0) {
            return zeroIsEmpty ? "" : ((prefixWithSign ? "+" : "") + "0.0");
        }
        if (prefixWithSign) {
            double b0 = b;
            if (b < 0) {
                sb.append(" - ");
                b0 = -b;
            } else if (b > 0) {
                sb.append(" + ");
            }
            if (df != null) {
                sb.append(df.getFormat().format(b0));
            } else {
                sb.append(b0);
            }
        } else {
            if (df != null) {
                sb.append(df.getFormat().format(b));
            } else {
                sb.append(b);
            }
        }
        return sb.toString();
    }

    public static String latexToMatlab(String inStr) {
        inStr = inStr.replace("\\left(", "(");
        inStr = inStr.replace("\\right)", ")");
        inStr = inStr.replace("\\pi", "pi");
        inStr = inStr.replace("\\alpha", "alpha");
        inStr = inStr.replace("\\gamma", "gamma");
        inStr = inStr.replace("{", "(");
        inStr = inStr.replace(")", "}");
        return inStr;
    }

    public static String matlabToLatex(String inStr) {
        StringBuilder sb = new StringBuilder();
        try {
            StreamTokenizer tok = new StreamTokenizer(new StringReader(inStr));
            tok.resetSyntax();
            tok.wordChars('a', 'z');
            tok.wordChars('A', 'Z');
            tok.wordChars('0', '9');
            tok.wordChars(128 + 32, 255);
            tok.whitespaceChars(0, ' ');
            //tok.parseNumbers();
            int nt;
            Properties replacementMap = new Properties();
            replacementMap.put("(", "{\\left(");
            replacementMap.put(")", "\\right)}");
            replacementMap.put("pi", "\\pi");
            replacementMap.put("alpha", "\\alpha");
            replacementMap.put("beta", "\\beta");
            replacementMap.put("gamma", "\\gamma");
            replacementMap.put("exp", "\\exp");
            replacementMap.put("sin", "\\sin");
            replacementMap.put("cos", "\\cos");
            replacementMap.put("*", "");
            int[] braces = new int[3];
            final int PAR = 0;
            final int CROC = 1;
            final int ACC = 2;
            final int BRAC2 = 3;
            String lastNZWord = "";
            String lastWord = "";
            while ((nt = tok.nextToken()) != StreamTokenizer.TT_EOF) {
                switch (nt) {
                    case StreamTokenizer.TT_WORD: {
                        String o = lastNZWord;
                        lastWord = (replacementMap.getProperty(String.valueOf(tok.sval), String.valueOf(tok.sval)));
                        if (lastWord.length() > 0
                                && ((Character.isLetter(lastWord.charAt(0))
                                && o.length() > 0 && o.startsWith("\\")
                                && Character.isLetter(o.charAt(o.length() - 1)))
                                || (Character.isDigit(lastWord.charAt(0))
                                && o.length() > 0 && ((o.startsWith("\\") && Character.isLetter(o.charAt(o.length() - 1)))
                                || (Character.isDigit(o.charAt(o.length() - 1))))))) {
                            sb.append(".");
                        }
                        break;
                    }
                    case StreamTokenizer.TT_NUMBER: {
                        String o = lastNZWord;
                        lastWord = (String.valueOf(tok.nval));
                        if (lastWord.length() > 0 && Character.isDigit(lastWord.charAt(0)) && o.length() > 0 && o.startsWith("\\") && Character.isLetter(o.charAt(o.length() - 1))) {
                            sb.append(".");
                        }
                        break;
                    }
                    case StreamTokenizer.TT_EOL: {
                        lastWord = ("\n");
                        break;
                    }
                    case '(': {
                        lastWord = (replacementMap.getProperty(String.valueOf((char) tok.ttype), String.valueOf((char) tok.ttype)));
                        braces[PAR]++;
                        break;
                    }
                    case ')': {
                        lastWord = (replacementMap.getProperty(String.valueOf((char) tok.ttype), String.valueOf((char) tok.ttype)));
                        braces[PAR]--;
                        break;
                    }
                    case '{': {
                        lastWord = (replacementMap.getProperty(String.valueOf((char) tok.ttype), String.valueOf((char) tok.ttype)));
                        braces[ACC]++;
                        break;
                    }
                    case '}': {
                        lastWord = (replacementMap.getProperty(String.valueOf((char) tok.ttype), String.valueOf((char) tok.ttype)));
                        braces[ACC]--;
                        break;
                    }
                    case '[': {
                        lastWord = (replacementMap.getProperty(String.valueOf((char) tok.ttype), String.valueOf((char) tok.ttype)));
                        braces[CROC]++;
                        break;
                    }
                    case ']': {
                        lastWord = (replacementMap.getProperty(String.valueOf((char) tok.ttype), String.valueOf((char) tok.ttype)));
                        braces[CROC]--;
                        break;
                    }
                    default: {
                        String kk = String.valueOf((char) nt);
                        String rr = replacementMap.getProperty(kk, kk);
                        lastWord = (rr);
                    }
                }
                if (lastWord.length() > 0) {
                    lastNZWord = lastWord;
                }
                sb.append(lastWord);
            }
        } catch (IOException ex) {
            Logger.getLogger(MatlabFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
}
