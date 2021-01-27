package net.thevpc.scholar.hadrumaths;

import net.hl.lang.DoubleToDoubleFunction;

public abstract class DoubleSolverX {
    private DoubleToDoubleFunction f;
    private double min;
    private double max;

    public DoubleSolverX(DoubleToDoubleFunction f, double min, double max) {
        this.f = f;
        this.min = min;
        this.max = max;
    }


    public DoubleToDoubleFunction getF() {
        return f;
    }

    public DoubleSolverX setF(DoubleToDoubleFunction f) {
        this.f = f;
        return this;
    }

    public double getMin() {
        return min;
    }

    public DoubleSolverX setMin(double min) {
        this.min = min;
        return this;
    }

    public double getMax() {
        return max;
    }

    public DoubleSolverX setMax(double max) {
        this.max = max;
        return this;
    }
    public abstract double solve();

    public static class Times extends DoubleSolverX{
        private int times;

        public Times(DoubleToDoubleFunction f, double from, double to, int times) {
            super(f, from, to);
            this.times = times;
        }

        @Override
        public double solve() {

            double min = getMin();
            double step = (getMax() - min) / (times - 1);
            DoubleToDoubleFunction f = getF();
            double bx=Double.NaN;
            double bg=Double.NaN;
            boolean someResult=false;
            for (int i = 0; i < times; i++) {
                double x = min + i * step;
                double g = Maths.abs(f.applyAsDouble(x));
                if (!java.lang.Double.isNaN(g) && !java.lang.Double.isInfinite(g)) {
                    if(someResult){
                        if(g<bg){
                            bx=x;
                            bg=g;
                        }
                    }else{
                        someResult=true;
                        bx=x;
                        bg=g;
                    }
                }
            }
            return bx;
        }
    }
}
