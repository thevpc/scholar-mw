package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Cos;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * Created by vpc on 11/17/16.
 */
public class TestCosXPlusY {

    @Test
    public void testCosCosVsCosXPlusY() {
        System.out.println("testCosCosVsCosXPlusY");
    //problems
        // cos(((0.9169317480222497 * X) + (0.8897143110485595 * Y)) + 0.10546599924098332) ** cos((0.9091685583092961 * X) + 0.979857203027707) * cos((0 * Y) + 0.9945462742048392)    :: Domain(x=0.0->47.62612253690951;y=0.0->30.5050548874369)


        Chronometer ch1 = new Chronometer();
        List<Expr> e1list = new ArrayList<Expr>();
        List<Expr> e2list = new ArrayList<Expr>();
        List<Complex> v1list = new ArrayList<Complex>();
        List<Complex> v2list = new ArrayList<Complex>();
        List<Domain> vdomains = new ArrayList<Domain>();

        int maxIterations = 10;
        for (int i = 0; i < maxIterations; i++) {
            double xmin = randomBoolean()? 0: randomDouble(100);
            double xlen = randomDouble(100);
            double ymin = randomBoolean()? 0: randomDouble(100);
            double ylen = randomDouble(100);
            double a1 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double a2 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double a3 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double b1 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double b2 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double b3 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double b4 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;


            Domain d = Domain.forWidth(xmin, xlen, ymin, ylen);
            Expr e1 = new Cos(add(mul(new Complex(a1), X), mul(new Complex(a2), Y),new Complex(a3)));
            Expr e2 = mul(new Cos(add(mul(new Complex(b1), X),new Complex(b2))),new Cos(add(mul(new Complex(b3), Y),new Complex(b4))));
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

        Chronometer ch1 = new Chronometer();
        List<Expr> e1list = new ArrayList<Expr>();
        List<Expr> e2list = new ArrayList<Expr>();
        List<Complex> v1list = new ArrayList<Complex>();
        List<Complex> v2list = new ArrayList<Complex>();
        List<Domain> vdomains = new ArrayList<Domain>();

        int maxIterations = 10;
        for (int i = 0; i < maxIterations; i++) {
            double xmin = randomBoolean()? 0: randomDouble(100);
            double xlen = randomDouble(100);
            double ymin = randomBoolean()? 0: randomDouble(100);
            double ylen = randomDouble(100);
            double a1 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double a2 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double a3 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double b1 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double b2 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;
            double b3 = randomBoolean()? 0: randomDouble(1);//Math.random() * 100;

            Domain d = Domain.forWidth(xmin, xlen, ymin, ylen);
            Expr e1 = new Cos(add(mul(new Complex(a1), X), mul(new Complex(a2), Y),new Complex(a3)));
            Expr e2 = new Cos(add(mul(new Complex(b1), X), mul(new Complex(b2), Y),new Complex(b3)));
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
            double xmin = randomBoolean()? 0: randomDouble(4);
            double xlen = randomDouble(100);
            double ymin = randomBoolean()? 0: randomDouble(4);
            double ylen = randomDouble(100);
            double a1 = randomBoolean()? 0: randomDouble(4);//Math.random() * 100;
            double a2 = randomBoolean()? 0: randomDouble(4);//Math.random() * 100;
            double b1 = randomBoolean()? 0: randomDouble(4);//Math.random() * 100;
            double b2 = randomBoolean()? 0: randomDouble(4);//Math.random() * 100;
            double b3 = randomBoolean()? 0: randomDouble(4);//Math.random() * 100;


            Domain d = Domain.forWidth(xmin, xlen, ymin, ylen);
            Expr e1 = new Cos(add(mul(new Complex(a1), X), mul(new Complex(a2), Y)));
            Expr e2 = (add(add(mul(new Complex(b1), X), mul(new Complex(b2), Y)), new Complex(b3)));
            e1list.add(e1);
            e2list.add(e2);
            vdomains.add(d);
        }
        compareResults(e1list, e2list, v1list, v2list, vdomains, maxIterations);
        //Assert.assertEquals();
    }

    private void compareResults(List<Expr> e1list, List<Expr> e2list, List<Complex> v1list, List<Complex> v2list, List<Domain> vdomains, int maxIterations) {
        Chronometer ch1 = new Chronometer();
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
            System.out.println("hardFormal " + i + " " + e1 + " ** " + e2+"    :: "+vdomains.get(i));
            v1list.add(ScalarProductOperatorFactory.hardFormal().eval(vdomains.get(i), e1, e2));
        }
        ch1.stop();
        System.out.println(ch1);
        Chronometer ch2 = new Chronometer();
        for (int i = 0; i < maxIterations; i++) {
            Expr e1 = e1list.get(i);
            Expr e2 = e2list.get(i);
            System.out.println("quad " + i + " " + e1 + " ** " + e2+"    :: "+vdomains.get(i));
            v2list.add(ScalarProductOperatorFactory.quad().eval(vdomains.get(i), e1, e2));
        }
        ch2.stop();
        System.out.println(ch2);

        for (int i = 0; i < maxIterations; i++) {
            Complex a = v1list.get(i);
            Complex b = v2list.get(i);
            double err = a.getError(b);
            boolean okBool=false;
            String ok="ERROR";
            if(err<0.01){
                ok="OK   ";
                okBool=true;
            }else if(err<1 && a.absdbl()<1E-5 && b.absdbl()<1E-5){
                ok="OK ? ";
            }
            System.out.println(ok+" : "+((err*100)) +"% : formal="+a+" ; quad="+b);
            if(!okBool){
                Expr e1 = e1list.get(i);
                Expr e2 = e2list.get(i);
                System.out.println("\t " + i + " " + e1 + " ** " + e2+"    :: "+vdomains.get(i));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Assert.fail();
                System.err.println("FAIL...");
            }
        }
        System.out.println(ch1 + " vs " + ch2);
    }



}
