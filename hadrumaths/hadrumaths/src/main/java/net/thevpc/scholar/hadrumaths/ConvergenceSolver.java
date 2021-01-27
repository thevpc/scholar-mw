package net.thevpc.scholar.hadrumaths;

import net.hl.lang.IntToDoubleFunction;

public abstract class ConvergenceSolver {
    private IntToDoubleFunction f;
    private int min;
    public ConvergenceSolver(IntToDoubleFunction f, int min) {
        this.f = f;
        this.min = min;
    }


    public IntToDoubleFunction getF() {
        return f;
    }

    public ConvergenceSolver setF(IntToDoubleFunction f) {
        this.f = f;
        return this;
    }

    public int getMin() {
        return min;
    }

    public ConvergenceSolver setMin(int min) {
        this.min = min;
        return this;
    }


    public abstract int solve();

    public static class Max extends ConvergenceSolver {
        private int times;
        private double err;
        private int conv0=5;

        public Max(IntToDoubleFunction f, int from, int times, double err) {
            super(f, from);
            this.times = times;
            this.err = err;
        }

        @Override
        public int solve() {

            int min = getMin();
            IntToDoubleFunction f = getF();
            double bg=Double.NaN;
            boolean someResult=false;
            int conv=5;
            for (int i = 0; i < times; i++) {
                int x = min + i;
                double g = f.applyAsDouble(x);
                if (!Double.isNaN(g) && !Double.isInfinite(g)) {
                    if(someResult){
                        double nerr=Maths.rerr(g,bg);
                        bg = g;
                        if(nerr<=err){
                            conv--;
                            if(conv<=0){
                                return x;
                            }
                        }else{
                            conv=conv0;
                        }
                    }else{
                        someResult=true;
                        bg=g;
                        conv--;
                        if(conv<=0){
                            return x;
                        }
                    }
                }
            }
            return -1;
        }
    }

    public static class Infinite extends ConvergenceSolver {
        private double err;
        private int conv0=5;

        public Infinite(IntToDoubleFunction f, int from, double err) {
            super(f, from);
            this.err = err;
        }

        @Override
        public int solve() {

            int min = getMin();
            IntToDoubleFunction f = getF();
            double bg=Double.NaN;
            boolean someResult=false;
            int conv=5;
            int i=min;
            while (true) {
                double g = f.applyAsDouble(i);
                if (!Double.isNaN(g) && !Double.isInfinite(g)) {
                    if(someResult){
                        double nerr=Maths.rerr(g,bg);
                        bg = g;
                        if(nerr<=err){
                            conv--;
                            if(conv<=0){
                                return i;
                            }
                        }else{
                            conv=conv0;
                        }
                    }else{
                        someResult=true;
                        bg=g;
                        conv--;
                    }
                }
                i++;
            }
        }
    }
}
