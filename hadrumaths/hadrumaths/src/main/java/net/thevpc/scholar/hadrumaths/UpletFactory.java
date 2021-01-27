package net.thevpc.scholar.hadrumaths;

public class UpletFactory {
    public static <A, B> RightArrowUplet2<A, B> rightArrawUplet(A a, B b) {
        return new RightArrowUplet2<A, B>(a, b);
    }

    public static RightArrowUplet2.Double rightArrawUplet(double a, double b) {
        return new RightArrowUplet2.Double(a, b);
    }

    public static RightArrowUplet2.Complex rightArrawUplet(Complex a, Complex b) {
        return new RightArrowUplet2.Complex(a, b);
    }

    public static RightArrowUplet2.Expr rightArrawUplet(Expr a, Expr b) {
        return new RightArrowUplet2.Expr(a, b);
    }

    public static abstract class DUplet2<A, B> implements Uplet2<A, B> {
        private final A a;
        private final B b;

        public DUplet2(A a, B b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public A getFirst() {
            return a;
        }

        @Override
        public B getSecond() {
            return b;
        }

        @Override
        public String toString() {
            return a + " " + getName() + " " + b;
        }

        public abstract String getName();
    }
}
