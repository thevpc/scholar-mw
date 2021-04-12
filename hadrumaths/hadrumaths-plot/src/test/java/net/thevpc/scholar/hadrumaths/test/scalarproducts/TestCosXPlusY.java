package net.thevpc.scholar.hadrumaths.test.scalarproducts;

import junit.framework.Assert;
import net.thevpc.common.time.Chronometer;

import static net.thevpc.scholar.hadrumaths.io.HadrumathsIOUtils.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.Domain;

import static net.thevpc.scholar.hadrumaths.Maths.*;
import net.thevpc.scholar.hadrumaths.ScalarProductOperatorFactory;

/**
 * Created by vpc on 11/17/16.
 */
public class TestCosXPlusY {

    @Test
    public void testCosCosVsCosXPlusY() {
        Maths.Config.setCacheExpressionPropertiesEnabled(false);
        System.out.println("testCosCosVsCosXPlusY");
        //problems
        // cos(((0.9169317480222497 * X) + (0.8897143110485595 * Y)) + 0.10546599924098332) ** cos((0.9091685583092961 * X) + 0.979857203027707) * cos((0 * Y) + 0.9945462742048392)    :: Domain(x=0.0->47.62612253690951;y=0.0->30.5050548874369)

        Chronometer ch1 = Chronometer.start();
        List<Expr> e1list = new ArrayList<Expr>();
        List<Expr> e2list = new ArrayList<Expr>();
        List<Complex> v1list = new ArrayList<Complex>();
        List<Complex> v2list = new ArrayList<Complex>();
        List<Domain> vdomains = new ArrayList<Domain>();

        int maxIterations = 10;
        for (int i = 0; i < maxIterations; i++) {
            double xmin = randomBoolean() ? 0 : randomDouble(100);
            double xlen = randomDouble(100);
            double ymin = randomBoolean() ? 0 : randomDouble(100);
            double ylen = randomDouble(100);
            double a1 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double a2 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double a3 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double b1 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double b2 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double b3 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double b4 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;

            Domain d = Domain.ofWidth(xmin, xlen, ymin, ylen);
            Expr e1 = cos(add(mul(Complex.of(a1), X), mul(Complex.of(a2), Y), Complex.of(a3)));
            Expr e2 = mul(cos(add(mul(Complex.of(b1), X), Complex.of(b2))), cos(add(mul(Complex.of(b3), Y), Complex.of(b4))));
            e1list.add(e1);
            e2list.add(e2);
            vdomains.add(d);
        }
        compareResults(e1list, e2list, v1list, v2list, vdomains, maxIterations);
    }

    @Test
    public void testCosXPlusYVsCosXPlusY() {
        Config.setCacheEnabled(false);
        System.out.println("testCosXPlusYVsCosXPlusY");

        Chronometer ch1 = Chronometer.start();
        List<Expr> e1list = new ArrayList<Expr>();
        List<Expr> e2list = new ArrayList<Expr>();
        List<Complex> v1list = new ArrayList<Complex>();
        List<Complex> v2list = new ArrayList<Complex>();
        List<Domain> vdomains = new ArrayList<Domain>();

        int maxIterations = 10;
        for (int i = 0; i < maxIterations; i++) {
            double xmin = randomBoolean() ? 0 : randomDouble(100);
            double xlen = randomDouble(100);
            double ymin = randomBoolean() ? 0 : randomDouble(100);
            double ylen = randomDouble(100);
            double a1 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double a2 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double a3 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double b1 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double b2 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;
            double b3 = randomBoolean() ? 0 : randomDouble(1);//Math.random() * 100;

            Domain d = Domain.ofWidth(xmin, xlen, ymin, ylen);
            Expr e1 = cos(add(mul(Complex.of(a1), X), mul(Complex.of(a2), Y), Complex.of(a3)));
            Expr e2 = cos(add(mul(Complex.of(b1), X), mul(Complex.of(b2), Y), Complex.of(b3)));
            e1list.add(e1);
            e2list.add(e2);
            vdomains.add(d);
        }
        compareResults(e1list, e2list, v1list, v2list, vdomains, maxIterations);
    }

    @Test
    public void testCosXPlusYVsLinear() {
        System.out.println("testCosXPlusYVsLinear");
        List<Expr> e1list = new ArrayList<Expr>();
        List<Expr> e2list = new ArrayList<Expr>();
        List<Complex> v1list = new ArrayList<Complex>();
        List<Complex> v2list = new ArrayList<Complex>();
        List<Domain> vdomains = new ArrayList<Domain>();

        int maxIterations = 2;
        for (int i = 0; i < maxIterations; i++) {
            double xmin = randomBoolean() ? 0 : randomDouble(4);
            double xlen = randomDouble(100);
            double ymin = randomBoolean() ? 0 : randomDouble(4);
            double ylen = randomDouble(100);
            double a1 = randomBoolean() ? 0 : randomDouble(4);//Math.random() * 100;
            double a2 = randomBoolean() ? 0 : randomDouble(4);//Math.random() * 100;
            double b1 = randomBoolean() ? 0 : randomDouble(4);//Math.random() * 100;
            double b2 = randomBoolean() ? 0 : randomDouble(4);//Math.random() * 100;
            double b3 = randomBoolean() ? 0 : randomDouble(4);//Math.random() * 100;

            Domain d = Domain.ofWidth(xmin, xlen, ymin, ylen);
            Expr e1 = cos(add(mul(Complex.of(a1), X), mul(Complex.of(a2), Y)));
            Expr e2 = (add(add(mul(Complex.of(b1), X), mul(Complex.of(b2), Y)), Complex.of(b3)));
            e1list.add(e1);
            e2list.add(e2);
            vdomains.add(d);
        }
        compareResults(e1list, e2list, v1list, v2list, vdomains, maxIterations);
        //Assertions.assertEquals();
    }

//    @Test
//    public void replay() {
//        try {
//            Maths.Config.setCacheEnabled(false);
//            Object[] o = (Object[]) HadrumathsIOUtils.loadObject(System.getProperty("user.home") + "/err.sv");
//            Expr e1=(Expr) o[0];
//            Expr e2=(Expr) o[1];
//            Domain dom=(Domain) o[2];
//            System.out.println(e1);
//            System.out.println("\t\t"+e1.simplify());
//            System.out.println(e2);
//            System.out.println("\t\t"+e2.simplify());
//            System.out.println(dom);
//            Complex v1 = ScalarProductOperatorFactory.hardFormal().eval(true, dom, e1, e2);
//            Complex v2 = ScalarProductOperatorFactory.quad().eval(true, dom, e1, e2);
//            Complex v3 = ScalarProductOperatorFactory.hardFormal().eval(true, dom, e1.simplify(), e2.simplify());
//            Complex v4 = ScalarProductOperatorFactory.quad().eval(true, dom, e1.simplify(), e2.simplify());
//            System.out.println(v1 + " ; " + v2 + " ; " + v3 + " ; " + v4 + " ; ");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void compareResults(List<Expr> e1list, List<Expr> e2list, List<Complex> v1list, List<Complex> v2list, List<Domain> vdomains, int maxIterations) {
        Chronometer ch1 = Chronometer.start();
//        for (int i = 0; i < maxIterations; i++) {
//            Expr e1 = e1list.get(i);
//            Expr e2 = e2list.get(i);
//            System.out.println("hardFormal " + i + " " + e1 + " ** " + e2+"    :: "+vdomains.get(i));
//            FormalScalarProductOperator scalarProductOperator = (FormalScalarProductOperator) ScalarProductOperatorFactory.hardFormal();
//            System.out.println("\t " + i + " " + scalarProductOperator.getExpressionRewriter().rewriteOrSame(e1) + " ** " + scalarProductOperator.getExpressionRewriter().rewriteOrSame(e2)+"    :: "+vdomains.get(i));
//            v1list.add(scalarProductOperator.process(vdomains.get(i), e1, e2));
//        }
        for (int i = 0; i < maxIterations; i++) {
            Expr e1 = e1list.get(i);
            Expr e2 = e2list.get(i);
            System.out.println("hardFormal " + i + " " + e1 + " *** " + e2 + "    :: " + vdomains.get(i));
            v1list.add(ScalarProductOperatorFactory.hardFormal().eval(vdomains.get(i), e1, e2).toComplex());
        }
        ch1.stop();
        System.out.println(ch1);
        Chronometer ch2 = Chronometer.start();
        for (int i = 0; i < maxIterations; i++) {
            Expr e1 = e1list.get(i);
            Expr e2 = e2list.get(i);
            System.out.println("quad " + i + " " + e1 + " ** " + e2 + "    :: " + vdomains.get(i));
            v2list.add(ScalarProductOperatorFactory.quad().eval(vdomains.get(i), e1, e2).toComplex());
        }
        ch2.stop();
        System.out.println(ch2);

        for (int i = 0; i < maxIterations; i++) {
            Complex a = v1list.get(i);
            Complex b = v2list.get(i);
            double err = a.getError(b);
            boolean okBool = false;
            String ok = "ERROR";
            if (err < 0.01) {
                ok = "OK   ";
                okBool = true;
            } else if (err < 1 && a.absdbl() < 1E-5 && b.absdbl() < 1E-5) {
                ok = "OK ? ";
            }
            System.out.println(ok + " : " + ((err * 100)) + "% : formal=" + a + " ; quad=" + b);
            if (!okBool) {
                Expr e1 = e1list.get(i);
                Expr e2 = e2list.get(i);
                Domain dom = vdomains.get(i);
                System.out.println("\t " + i + " " + e1 + " ** " + e2 + "    :: " + dom);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Assert.fail();
                System.err.println("FAIL...");
                if (err * 100 > 1) {
                    Complex v1 = ScalarProductOperatorFactory.hardFormal().eval(dom, e1, e2).toComplex();
                    Complex v2 = ScalarProductOperatorFactory.quad().eval(dom, e1, e2).toComplex();
                    Complex v3 = ScalarProductOperatorFactory.hardFormal().eval(dom, e1.simplify(), e2.simplify()).toComplex();
                    Complex v4 = ScalarProductOperatorFactory.quad().eval(dom, e1.simplify(), e2.simplify()).toComplex();
                    System.out.println(v1 + " ; " + v2 + " ; " + v3 + " ; " + v4 + " ; ");
                    saveObject(System.getProperty("user.home") + "/err.sv", new Object[]{e1,e2,dom});
                    v1 = ScalarProductOperatorFactory.hardFormal().eval(dom, e1, e2).toComplex();
                    Assert.fail("FAIL...");
                }
            }
        }
        System.out.println(ch1 + " vs " + ch2);
    }

}
