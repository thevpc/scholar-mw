package net.thevpc.scholar.hadrumaths.plot.random;


import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NPairElement;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.AbsoluteSamples;
import net.thevpc.scholar.hadruplot.Plot;

import javax.swing.*;

public class ValDiffHelper {
    private int maxDiffErrors = 5;
    private int plotDiffCount = 1;
    private boolean showSuccess;
    private ErrorList errorList;

    public ValDiffHelper(ErrorList errorList) {
        this.errorList = errorList;
    }

    public void showDiff(String message, Complex[] c3, Complex[] v3c, double[] x) {
        int errors = 0;
        for (int j = 0; j < c3.length; j++) {
            if (!c3[j].equals(v3c[j])) {
                System.err.println(message + " : x[" + j + "]=" + x[j] + "  : " + v3c[j] + "  <>  " + c3[j]);
                errors++;
                if (errors > maxDiffErrors) {
                    return;
                }
            }
        }

    }

    public void plotDiff(String id, String a, String b, Object v1, Object v2, AbsoluteSamples bs, NPairElement... texts) {
        Plot.cd("/diff/" + plotDiffCount + "/" + id).title(a).samples(bs).plot(v1);
        Plot.cd("/diff/" + plotDiffCount + "/" + id).title(b).samples(bs).plot(v2);
        for (int i = 0; i < texts.length; i++) {
            NPairElement text = texts[i];
            Plot.cd("/diff/" + plotDiffCount + "/" + id).title(text.key().asStringValue().get()).plot(new JScrollPane(new JTextArea(text.value().asStringValue().get())));
        }
        plotDiffCount++;
    }

    public void showDiff(String message, Complex[][] c3, Complex[][] v3c, double[] x, double[] y) {
        int errors = 0;
        for (int j = 0; j < c3.length; j++) {
            for (int k = 0; k < c3[j].length; k++) {
                if (!c3[j][k].equals(v3c[j][k])) {
                    System.err.println(message + " : x[" + k + "]=" + x[k] + ",y[" + j + "]=" + y[j] + "  : " + v3c[j][k] + "  <>  " + c3[j][k]);
                    errors++;
                    if (errors > maxDiffErrors) {
                        return;
                    }
                }
            }
        }

    }

    public void showDiff(String message, Complex[][][] c3, Complex[][][] v3c, double[] x, double[] y, double[] z) {
        int errors = 0;
        for (int i = 0; i < c3.length; i++) {
            for (int j = 0; j < c3[i].length; j++) {
                for (int k = 0; k < c3[i][j].length; k++) {
                    if (!c3[i][j][k].equals(v3c[i][j][k])) {
                        System.err.println(message + " : x[" + k + "]=" + x[k] + ",y[" + j + "]=" + y[j] + ",z[" + i + "]=" + z[i] + "  : " + v3c[i][j][k] + "  <>  " + c3[i][j][k]);
                        errors++;
                        if (errors > maxDiffErrors) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void normalize(double[] doublesB) {
        for (int k = 0; k < doublesB.length; k++) {
            if (doublesB[k] == 0) {
                doublesB[k] = 0;
            }
        }
    }

    public void showDiff(String message, double[] c3, double[] v3c, double[] x) {
        int errors = 0;
        for (int j = 0; j < c3.length; j++) {
            if (Double.compare(c3[j], v3c[j]) != 0) {
                System.err.println(message + " : x[" + j + "]=" + x[j] + "  : " + v3c[j] + "  <>  " + c3[j]);
                errors++;
                if (errors > maxDiffErrors) {
                    return;
                }
            }
        }
    }

    public static void normalize(double[][] doublesB) {
        for (int j = 0; j < doublesB.length; j++) {
            for (int k = 0; k < doublesB[j].length; k++) {
                if (doublesB[j][k] == 0) {
                    doublesB[j][k] = 0;
                }
            }
        }
    }

    public void showDiff(String message, double[][] c3, double[][] v3c, double[] x, double[] y) {
        int errors = 0;
        for (int j = 0; j < c3.length; j++) {
            for (int k = 0; k < c3[j].length; k++) {
                if (Double.compare(c3[j][k], v3c[j][k]) != 0) {
                    System.err.println(message + " : x[" + k + "]=" + x[k] + ",y[" + j + "]=" + y[j] + "  : " + v3c[j][k] + "  <>  " + c3[j][k]);
                    errors++;
                    if (errors > maxDiffErrors) {
                        return;
                    }
                }
            }
        }
    }

    public static void normalize(double[][][] doublesB) {
        for (int i = 0; i < doublesB.length; i++) {
            for (int j = 0; j < doublesB[i].length; j++) {
                for (int k = 0; k < doublesB[i][j].length; k++) {
                    if (doublesB[i][j][k] == 0) {
                        doublesB[i][j][k] = 0;
                    }
                }
            }
        }
    }

    public void showDiff(String message, double[][][] c3, double[][][] v3c, double[] x, double[] y, double[] z) {
        int errors = 0;
        for (int i = 0; i < c3.length; i++) {
            for (int j = 0; j < c3[i].length; j++) {
                for (int k = 0; k < c3[i][j].length; k++) {
                    if (Double.compare(c3[i][j][k], v3c[i][j][k]) != 0) {
                        System.err.println(message + " : x[" + k + "]=" + x[k] + ",y[" + j + "]=" + y[j] + ",z[" + i + "]=" + z[i] + "  : " + v3c[i][j][k] + "  <>  " + c3[i][j][k]);
                        errors++;
                        if (errors > maxDiffErrors) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void normalize(Complex[] doublesB) {
        for (int k = 0; k < doublesB.length; k++) {
            if (doublesB[k].isZero()) {
                doublesB[k] = Complex.ZERO;
            }
        }
    }

    public static void normalize(Complex[][] doublesB) {
        for (int j = 0; j < doublesB.length; j++) {
            for (int k = 0; k < doublesB[j].length; k++) {
                if (doublesB[j][k].isZero()) {
                    doublesB[j][k] = Complex.ZERO;
                }
            }
        }
    }

    public static void normalize(Complex[][][] doublesB) {
        for (int i = 0; i < doublesB.length; i++) {
            for (int j = 0; j < doublesB[i].length; j++) {
                for (int k = 0; k < doublesB[i][j].length; k++) {
                    if (doublesB[i][j][k].isZero()) {
                        doublesB[i][j][k] = Complex.ZERO;
                    }
                }
            }
        }
    }

    public boolean checkAndSay(double[] doublesA, double[] doublesB, double[] x, Expr expr, Domain domain) {
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            this.plotDiff("Incompatibility", "DD_X(Bulk)", "DD_X(Each)", doublesA, doublesB, AbsoluteSamples.absolute(x),
                    NElement.ofPair("expr", NElement.ofString(expr.toString())),
                    NElement.ofPair("domain", NElement.ofString(domain.toString())));
            showDiff("DD_X Diff", doublesA, doublesB, x);
            errorList.addInstanceError(expr, "Invalid values");
            return false;
        } else {
            if (showSuccess) {
                Plot.title("(OK)DD_X:" + expr).xsamples(x).plot((Object) doublesA);
            }
            return true;
        }
    }

    public boolean isShowSuccess() {
        return showSuccess;
    }

    public ValDiffHelper setShowSuccess(boolean showSuccess) {
        this.showSuccess = showSuccess;
        return this;
    }

}
