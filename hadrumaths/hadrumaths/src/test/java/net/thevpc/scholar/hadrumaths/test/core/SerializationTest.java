package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * Expressions, domains, numbers, vectors and matrices are Serializable and are used as cache
 * keys. A round-trip must preserve both the structure (equals/hashCode) and the values.
 */
public class SerializationTest {

    @SuppressWarnings("unchecked")
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
    public void complexRoundTrip() throws Exception {
        Complex[] values = {
                Complex.ZERO, Complex.ONE, Complex.I, Complex.MINUS_I,
                Complex.of(1, 2), Complex.of(-3.5, 0), Complex.of(0, -2.25),
                Complex.of(Double.NaN), Complex.of(-4, -0.0)};
        for (Complex c : values) {
            Complex r = roundTrip(c);
            Assertions.assertEquals(c, r, "value " + c);
            Assertions.assertEquals(c.hashCode(), r.hashCode(), "hash " + c);
        }
    }

    @Test
    public void domainRoundTrip() throws Exception {
        Domain[] ds = {
                Domain.ofBounds(0, 1),
                Domain.ofBounds(0, 1, -2, 3),
                Domain.ofBounds(0, 1, -2, 3, -4, 5),
                Domain.FULLX, Domain.EMPTYXY};
        for (Domain d : ds) {
            Domain r = roundTrip(d);
            Assertions.assertEquals(d, r, "domain " + d);
            Assertions.assertEquals(d.xmin(), r.xmin(), 0.0);
            Assertions.assertEquals(d.dimension(), r.dimension());
        }
    }

    @Test
    public void expressionRoundTrip() throws Exception {
        Domain d = Domain.ofBounds(-1, 2, 0, 1);
        Domain d1 = Domain.ofBounds(0, 1, 0, 1);
        Domain d2 = Domain.ofBounds(1, 2, 0, 1);
        Expr[] exprs = {
                Maths.X,
                Maths.Y,
                Maths.expr(2.5),
                Maths.expr(Complex.of(1, 2)),
                Maths.X.mul(2).plus(Maths.Y).sub(1),
                Maths.X.sin(),
                Maths.X.exp().mul(Maths.Y.cos()),
                FunctionFactory.cosXcosY(1.3, 1.1, 0.2, 2.0, -0.3, d),
                Maths.expr(1.0, d1).plus(Maths.expr(2.0, d2)),
                Maths.X.mul(d),
                Maths.expr(2.0, Domain.ofBounds(0.5, 0.5, 0, 1)),   // empty support
        };
        double[] xs = {-0.5, 0.25, 1.5};
        double[] ys = {0.2, 0.75, 0.9};
        for (Expr e : exprs) {
            Expr r = roundTrip(e);
            Assertions.assertEquals(e.toString(), r.toString(), "printed form of " + e);
            Assertions.assertEquals(e, r, "structure of " + e);
            Assertions.assertEquals(e.hashCode(), r.hashCode(), "hash of " + e);
            for (int i = 0; i < xs.length; i++) {
                Complex a = e.toDC().evalComplex(xs[i], ys[i]);
                Complex b = r.toDC().evalComplex(xs[i], ys[i]);
                Assertions.assertEquals(a.getReal(), b.getReal(), 1e-12, "re of " + e);
                Assertions.assertEquals(a.getImag(), b.getImag(), 1e-12, "im of " + e);
            }
        }
    }

    @Test
    public void vectorExpressionRoundTrip() throws Exception {
        DoubleToVector v = Maths.vector(Maths.X, Maths.Y.mul(2)).toDV();
        Expr r = roundTrip(v);
        Assertions.assertEquals(v.getDomain(), r.getDomain());
        Assertions.assertEquals(0.5, r.toDV().getComponent(Axis.X).toDD().evalDouble(0.5, 0.75), 1e-12);
        Assertions.assertEquals(1.5, r.toDV().getComponent(Axis.Y).toDD().evalDouble(0.5, 0.75), 1e-12);
    }

    @Test
    public void matrixAndVectorRoundTrip() throws Exception {
        ComplexMatrix m = Maths.matrix(new Complex[][]{
                {Complex.of(1, 2), Complex.of(-3, 0)},
                {Complex.of(0, 0.5), Complex.of(4, -1)}});
        ComplexMatrix mr = roundTrip(m);
        Assertions.assertEquals(m.getRowCount(), mr.getRowCount());
        Assertions.assertEquals(m.getColumnCount(), mr.getColumnCount());
        for (int i = 0; i < m.getRowCount(); i++) {
            for (int j = 0; j < m.getColumnCount(); j++) {
                Assertions.assertEquals(m.get(i, j), mr.get(i, j), "m[" + i + "," + j + "]");
            }
        }

        ComplexVector v = Maths.columnVector(Complex.of(1, 1), Complex.of(2, -1));
        ComplexVector vr = roundTrip(v);
        Assertions.assertEquals(v.size(), vr.size());
        for (int i = 0; i < v.size(); i++) {
            Assertions.assertEquals(v.get(i), vr.get(i), "v[" + i + "]");
        }
    }
}
