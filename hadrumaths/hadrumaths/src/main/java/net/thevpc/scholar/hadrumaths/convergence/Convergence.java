/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.scholar.hadrumaths.convergence;

import net.thevpc.nuts.time.NChronometer;
import net.thevpc.nuts.util.NAssert;
import net.thevpc.scholar.hadrumaths.Equalator;
import net.thevpc.scholar.hadrumaths.EqualatorResult;

/**
 *
 * @author vpc
 */
public class Convergence {

    private static class ConvergencePartialResultImpl<T> implements ConvergencePartialResult<T> {

        T value;
        long iter;

        @Override
        public T getLastValue() {
            return value;
        }

        @Override
        public long getIteration() {
            return iter;
        }

        @Override
        public String toString() {
            return "ConvergencePartialResult{" + "value=" + value + ", iter=" + iter + '}';
        }
        
    }

    public static <T> ConvergenceResult<T> compute(ConvergenceConfig<T> conf) {
        ConvergenceEvaluator<T> it = NAssert.requireNonNull(conf.getEval(), "iteration");
        Equalator<T> eps = NAssert.requireNonNull(conf.getEqualator(), "equalator");
        long start = conf.getStartEpoch();
        long maxIterations0 = conf.getMaxEpochs();
        long remainingIterations = maxIterations0;
        ConvergenceListener listener = conf.getListener();
        FixedSizeList<T> li = new FixedSizeList<>(conf.getStability());
        long maxTimeMs = conf.getMaxTimeMs();
        if (listener == null) {
            if (remainingIterations <= 0 && maxTimeMs <= 0) {
                long k = start;
                NChronometer ch = NChronometer.startNow();
                EqualatorResult lastRes;
                do {
                    li.add(it.next(k++));
                    lastRes = evalConverged(li, eps);
                } while (!lastRes.isEquals());
                ch.stop();
                return new ConvergenceResult<T>(li.last(), conf, k - 1, lastRes, ch.getDuration());
            } else {
                long k = start;
                EqualatorResult lastRes;
                NChronometer ch = NChronometer.startNow();
                do {
                    li.add(it.next(k++));
                    remainingIterations--;
                    lastRes = evalConverged(li, eps);
                } while ((maxIterations0 <= 0 || remainingIterations > 0)
                        && !lastRes.isEquals()
                        && (maxTimeMs <= 0 || ch.getDurationNanos() < maxTimeMs));
                ch.stop();
                return new ConvergenceResult<T>(li.last(), conf, k - 1, lastRes, ch.getDuration());
            }
        } else {
            ConvergencePartialResultImpl<T> cc = new ConvergencePartialResultImpl<>();
            cc.iter = start;
            EqualatorResult lastRes;
            NChronometer ch = NChronometer.startNow();
            do {
                cc.value = it.next(cc.iter++);
                li.add(cc.value);
                remainingIterations--;
                lastRes = evalConverged(li, eps);
                listener.next(cc);
            } while ((maxIterations0 <= 0 || remainingIterations > 0)
                    && !lastRes.isEquals()
                    && (maxTimeMs <= 0 || ch.getDurationNanos() < maxTimeMs));
            ch.stop();
            return new ConvergenceResult<T>(li.last(), conf, cc.iter - 1, lastRes, ch.getDuration());
        }
    }

    public static <T> EqualatorResult evalConverged(FixedSizeList<T> li, Equalator<T> e) {
        if (li.isFull()) {
            EqualatorResult worst = null;
            for (int i = 0; i < li.capacity() - 1; i++) {
                T o = li.get(i);
                T n = li.get(i + 1);
                EqualatorResult y = e.equals(o, n);
                if (worst == null) {
                    worst = y;
                } else {
                    worst = worst.max(y);
                }
            }
            return worst;
        } else {
            return EqualatorResult.DIFFERENT;
        }
    }

    public static boolean isConverged(FixedSizeDoubleList li, double eps) {
        if (li.isFull()) {
            for (int i = 0; i < li.capacity() - 1; i++) {
                double o = li.get(i);
                double n = li.get(i + 1);
                if (!dblEq(o, n, eps)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean dblEq(double a, double b, double relativePrecition) {
        if (a == b) {
            return true;
        }
        if (Double.isNaN(a) && Double.isNaN(b)) {
            return true;
        }
        if (Double.isNaN(a) || Double.isNaN(b)) {
            return false;
        }
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            return false;
        }
        if (Math.abs(b - a / a) >= relativePrecition) {
            return false;
        }
        return true;
    }
}
