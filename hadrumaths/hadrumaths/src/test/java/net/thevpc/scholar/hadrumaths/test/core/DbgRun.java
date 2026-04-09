package net.thevpc.scholar.hadrumaths.test.core;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import org.junit.jupiter.api.Test;
public class DbgRun {
    @Test
    public void run() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr f1 = Maths.X.mul(Maths.Y).mul(d);
        Expr f2 = Maths.X.mul(Maths.Y).mul(d);
        
        System.out.println("f1 = " + f1);
        System.out.println("f2 = " + f2);
        
        // Let's manually trigger toCanonicalScalarProductPair
        net.thevpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator sp = new net.thevpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator(true, null);
        Complex c = Maths.scalarProduct(f1, f2).toComplex();
        System.out.println("Result: " + c);
        // print what simplification yields
        Expr m = f1.mul(f2);
        System.out.println("m = " + m);
        Expr f1opt = sp.getSimplifier().rewriteOrSame(m, null);
        System.out.println("Rewritten f1*f2: " + f1opt.getClass() + " -> " + f1opt);
    }
}
