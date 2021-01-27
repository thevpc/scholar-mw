package net.thevpc.scholar.hadrumaths;

public class RightArrowUplet2<A, B> extends UpletFactory.DUplet2<A, B> {
    public RightArrowUplet2(A a, B b) {
        super(a, b);
    }

    @Override
    public String getName() {
        return "->";
    }

    public static class Double extends RightArrowUplet2<java.lang.Double, java.lang.Double> {
        public Double(double aDouble, double aDouble2) {
            super(aDouble, aDouble2);
        }
    }

    public static class Complex extends RightArrowUplet2<net.thevpc.scholar.hadrumaths.Complex, net.thevpc.scholar.hadrumaths.Complex> {
        public Complex(net.thevpc.scholar.hadrumaths.Complex v1, net.thevpc.scholar.hadrumaths.Complex v2) {
            super(v1, v2);
        }
    }

    public static class Expr extends RightArrowUplet2<net.thevpc.scholar.hadrumaths.Expr, net.thevpc.scholar.hadrumaths.Expr> {
        public Expr(net.thevpc.scholar.hadrumaths.Expr v1, net.thevpc.scholar.hadrumaths.Expr v2) {
            super(v1, v2);
        }
    }
}
