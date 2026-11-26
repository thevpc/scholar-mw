package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Random;

/**
 * Property test over randomly generated real expressions. For every generated expression:
 * <ul>
 *   <li>it stays real-typed ({@code toDD()} must work);</li>
 *   <li>{@code simplify()} returns the same values;</li>
 *   <li>a Java serialization round-trip is equal and evaluates the same;</li>
 *   <li>the symbolic derivative matches a central difference on X.</li>
 * </ul>
 * The generator avoids poles (division is always by {@code g^2+1}) and samples in [-2,2].
 */
public class RandomExprPropertyTest {

    static double tol(double v) {
        return 1e-6 * Math.max(1.0, Math.abs(v));
    }

    static Expr random(Random r, int depth) {
        if (depth <= 0) {
            switch (r.nextInt(4)) {
                case 0:
                    return Maths.X;
                case 1:
                    return Maths.Y;
                case 2:
                    return Maths.expr(1.0);
                default:
                    return Maths.expr((r.nextInt(7) - 3) * 0.5);
            }
        }
        switch (r.nextInt(10)) {
            case 0:
                return random(r, depth - 1).plus(random(r, depth - 1));
            case 1:
                return random(r, depth - 1).sub(random(r, depth - 1));
            case 2:
                return random(r, depth - 1).mul(random(r, depth - 1));
            case 3: {
                Expr g = random(r, depth - 1);
                return random(r, depth - 1).div(g.mul(g).plus(1));
            }
            case 4:
                return random(r, depth - 1).sin();
            case 5:
                return random(r, depth - 1).cos();
            case 6:
                return random(r, depth - 1).neg();
            case 7:
                return random(r, depth - 1).exp();
            case 8:
                return random(r, depth - 1).sqr();
            default:
                return random(r, depth - 1).mul(Maths.expr(1.5));
        }
    }

    static <T> T roundTrip(T o) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(o);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            return (T) ois.readObject();
        }
    }

    @Test
    public void randomExpressionsKeepTheirProperties() throws Exception {
        Random r = new Random(20261125);
        int checked = 0;
        for (int n = 0; n < 40; n++) {
            Expr e = random(r, 3);
            String tag = e.toString();
            double[] xs = {-1.7, -0.6, 0.0, 0.8, 1.9};
            double[] ys = {0.9, -1.1, 0.3, -0.4, 1.2};

            // 1. stays real
            Assertions.assertTrue(e.getType().isDoubleOutput(), "real expression expected: " + tag);
            for (int i = 0; i < xs.length; i++) {
                double v = e.toDD().evalDouble(xs[i], ys[i]);
                Assertions.assertFalse(Double.isNaN(v), "NaN at generation: " + tag);
            }

            // 2. simplify preserves values (it may hand back a complex-typed node for a real
            //    expression; the imaginary part must then be zero)
            Expr s = e.simplify();
            for (int i = 0; i < xs.length; i++) {
                Complex vs = s.toDC().evalComplex(xs[i], ys[i]);
                Assertions.assertEquals(0.0, vs.getImag(), tol(1.0), "simplify must stay real: " + tag);
                Assertions.assertEquals(e.toDD().evalDouble(xs[i], ys[i]),
                        vs.getReal(), tol(1.0), "simplify value: " + tag);
            }

            // 3. serialization round-trip
            Expr e2 = roundTrip(e);
            Assertions.assertEquals(e.toString(), e2.toString(), "printed form after round-trip: " + tag);
            Assertions.assertEquals(e.hashCode(), e2.hashCode(), "hash after round-trip: " + tag);
            for (int i = 0; i < xs.length; i++) {
                Assertions.assertEquals(e.toDD().evalDouble(xs[i], ys[i]),
                        e2.toDD().evalDouble(xs[i], ys[i]), 1e-12, "value after round-trip: " + tag);
            }

            // 4. derivative vs central difference
            Expr d = Maths.derive(e, Axis.X);
            double h = 1e-5;
            for (int i = 0; i < xs.length; i++) {
                double numeric = (e.toDD().evalDouble(xs[i] + h, ys[i]) - e.toDD().evalDouble(xs[i] - h, ys[i])) / (2 * h);
                double symbolic = d.toDD().evalDouble(xs[i], ys[i]);
                Assertions.assertEquals(numeric, symbolic, 1e-4 * Math.max(1.0, Math.abs(numeric)),
                        "derivative of (" + tag + ") at (" + xs[i] + "," + ys[i] + ") -> " + d);
            }
            checked++;
        }
        Assertions.assertEquals(40, checked);
    }
}
