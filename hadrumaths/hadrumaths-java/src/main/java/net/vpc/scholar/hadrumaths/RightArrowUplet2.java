package net.vpc.scholar.hadrumaths;

public class RightArrowUplet2<A, B> extends UpletFactory.DUplet2<A, B> {
    public RightArrowUplet2(A a, B b) {
        super(a, b);
    }

    @Override
    public String getName() {
        return "->";
    }

    public static class Double extends RightArrowUplet2<java.lang.Double, java.lang.Double> {
        public Double(java.lang.Double aDouble, java.lang.Double aDouble2) {
            super(aDouble, aDouble2);
        }
    }

    public static class Complex extends RightArrowUplet2<net.vpc.scholar.hadrumaths.Complex, net.vpc.scholar.hadrumaths.Complex> {
        public Complex(net.vpc.scholar.hadrumaths.Complex v1, net.vpc.scholar.hadrumaths.Complex v2) {
            super(v1, v2);
        }
    }

    public static class Expr extends RightArrowUplet2<net.vpc.scholar.hadrumaths.Expr, net.vpc.scholar.hadrumaths.Expr> {
        public Expr(net.vpc.scholar.hadrumaths.Expr v1, net.vpc.scholar.hadrumaths.Expr v2) {
            super(v1, v2);
        }
    }
}
