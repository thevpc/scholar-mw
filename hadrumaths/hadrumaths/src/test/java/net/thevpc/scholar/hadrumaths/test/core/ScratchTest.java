package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import org.junit.jupiter.api.Test;

public class ScratchTest {
    @Test
    public void testScratch() {
        try {
            Maths.Config.setCacheEnabled(false);
            Domain d = Domain.ofBounds(0, 2 * Math.PI);
            Expr f1 = Maths.X.cos().mul(d);
            Expr f2 = Maths.X.sin().mul(d);
            System.out.println("f1 = " + f1.getClass().getName() + " : " + f1);
            System.out.println("f2 = " + f2.getClass().getName() + " : " + f2);
            Complex sp = Maths.scalarProduct(f1, f2).toComplex();
            System.out.println("SP = " + sp);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
