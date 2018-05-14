package net.vpc.scholar.hadrumaths.test;

import junit.framework.Assert;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.MutableComplex;
import org.junit.Test;

public class TestComplex2 {

    @Test
    public void testMul() {
        double[] V={Double.NaN,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,Double.MAX_VALUE,Double.MIN_VALUE,0,1,-1};
        for (int i = 0; i < V.length; i++) {
            for (int j = 0; j < V.length; j++) {
                Complex c1=Complex.valueOf(V[i],V[j]);
                for (int k = 0; k < V.length; k++) {
                    for (int l = 0; l < V.length; l++) {
                        Complex c2=Complex.valueOf(V[k],V[l]);
                        try {
                            checkMul(c1, c2);
                        }catch (Throwable ex){
                            checkMul(c1, c2);
                        }
                    }
                }
            }
        }
    }
    private void checkMul(Complex c1,Complex c2){
        Complex m1=c1.mul(c2);

        MutableComplex d1=MutableComplex.forComplex(c1);
        MutableComplex d2=MutableComplex.forComplex(c2);
        d1.mul(d2);
        Complex m2 = d1.toComplex();
        if(!m1.equals(m2)){
            System.err.println("Error : ("+c1+") * ("+c2+") = "+m1+" <> "+m2);
        }else{
            System.out.println("OK : ("+c1+") * ("+c2+") = "+m1);
        }
        Assert.assertEquals(m1,m2);
    }
}
