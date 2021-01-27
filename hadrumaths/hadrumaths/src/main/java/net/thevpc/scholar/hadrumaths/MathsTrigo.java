package net.thevpc.scholar.hadrumaths;

class MathsTrigo {
    /**
     * sqrt(a^2 + b^2) without under/overflow.
     *
     * @param a
     * @param b
     * @return
     */
    public static double hypot(double a, double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        } else {
            r = 0.0;
        }
        return r;
    }

    //	public static double sinh(double x){
//		return (Math.exp(x) - Math.exp(-x))/2;
//	}
//
//	public static double cosh(double x){
//		return (Math.exp(x) + Math.exp(-x))/2;
//	}
//
//	public static double tanh(double x){
//		double a=Math.exp(x);
//		double b=Math.exp(-x);
//		return (a - b)/(a + b);
//	}
//
//	public static double cotanh(double x){
//		double a=Math.exp(x);
//		double b=Math.exp(-x);
//		return (a + b)/(a - b);
//	}

    /**
     * ----------- from colt Evaluates the series of Chebyshev polynomials Ti at
     * argument x/2. The series is given by
     * <pre>
     *        N-1
     *         - '
     *  y  =   >   coef[i] T (x/2)
     *         -            i
     *        i=0
     * </pre> Coefficients are stored in reverse order, i.e. the zero order term
     * is last in the array. Note N is the number of coefficients, not the
     * order.
     * <p>
     * If coefficients are for the interval a to b, x must have been transformed
     * to x -> 2(2x - b - a)/(b-a) before entering the routine. This maps x from
     * (a, b) to (-1, 1), over which the Chebyshev polynomials are defined.
     * <p>
     * If the coefficients are for the inverted interval, in which (a, b) is
     * mapped to (1/b, 1/a), the transformation required is x -> 2(2ab/x - b -
     * a)/(b-a). If b is infinity, this becomes x -> 4a/x - 1.
     * <p>
     * SPEED:
     * <p>
     * Taking advantage of the recurrence properties of the Chebyshev
     * polynomials, the routine requires one more addition per loop than
     * evaluating a nested polynomial of the same degree.
     *
     * @param x    argument to the polynomial.
     * @param coef the coefficients of the polynomial.
     * @param N    the number of coefficients.
     */
    public static double chbevl(double x, double[] coef, int N) throws ArithmeticException {
        double b0, b1, b2;

        int p = 0;
        int i;

        b0 = coef[p++];
        b1 = 0.0;
        i = N - 1;

        do {
            b2 = b1;
            b1 = b0;
            b0 = x * b1 - b2 + coef[p++];
        } while (--i > 0);

        return (0.5 * (b0 - b2));
    }

    public static double sin2(double d) {
        if (d == 0) {
            return 0.0;
        }
        double f = d / Math.PI;
        if (Maths.isInt(f)) {
            return 0;
        }
        return Math.sin(d);
    }

    public static double cos2(double d) {
        if (d == 0) {
            return 1;
        }
        double f = d / (Math.PI / 2);
        if (Maths.isInt(f)) {
            int r = ((int) Math.floor(Math.abs(f))) % 4;
            switch (r) {
                case 0: {
                    return 1;
                }
                case 1: {
                    return 0;
                }
                case 2: {
                    return -1;
                }
                case 3: {
                    return 0;
                }
                default: {
                    return Math.cos(d);
                }
            }
        }
        return Math.cos(d);
    }
}
