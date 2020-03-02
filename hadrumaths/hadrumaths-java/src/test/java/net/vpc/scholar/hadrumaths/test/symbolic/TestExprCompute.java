package net.vpc.scholar.hadrumaths.test.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.Any;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * Created by vpc on 5/21/17.
 */
public class TestExprCompute {
    @Test
    public void testCompute1() {
        //cos((2*n+1)/2*x*pi)*sin((2*n+1)/2*y*xpi)
        Any n=any(param("n"));
        Any m=any(param("m"));
        Any one = any(1);
        Any two = any(Complex.of(2));
        Any three = any(3);
        Any a= (two.mul(n).add(one).div(two).mul(PI).mul(X).cos()).mul((three.mul(m).sub(one).div(two).mul(PI).mul(Y).sin()));
        Domain d=Domain.ofPoints(0,0,Math.PI,Math.PI);
        double[] xtimes = d.xtimes(150);
        double[] ytimes = d.ytimes(150);
        double nn=1;
        double mm=1;
        double[][] val1 = a.setParam("n",nn).setParam("m",mm).toDD().evalDouble(xtimes, ytimes, d, null);
        double[][] val2 = eval(xtimes, ytimes, mm, nn, d);
//        Plot.plot(val1);
//        Plot.plot(val2);
//        if(true){
//            Object tt=new Object();
//            synchronized (tt){
//                try {
//                    tt.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        Assertions.assertArrayEquals(pzero(val1),pzero(val2));
//        Plot.asMatrix().plot(val1);
//        Plot.asMatrix().plot(val2);
//        Object o=new Object();
//        synchronized (o){
//            try {
//                o.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
    private double[][] pzero(double[][] r){
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[i].length; j++) {
                double v=r[i][j];
                if(v==0 || -v==0){
                    r[i][j]=0;
                }
            }
        }
        return r;
    }

    public double[][] eval(double[] x, double[] y, double n, double m, Domain d){
        double[][] r=new double[y.length][x.length];
        for (int i = 0; i < y.length-1; i++) {
            double y0=y[i];
            for (int j = 0; j < x.length-1; j++) {
                double x0=x[j];
                r[i][j]=cos((2*n+1)/2*PI*x0)*sin((3*m-1)/2*PI*y0);
            }
        }
        return r;
    }
}
