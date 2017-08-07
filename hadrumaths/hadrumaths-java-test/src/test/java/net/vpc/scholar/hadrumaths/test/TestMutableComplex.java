package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MutableComplex;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by vpc on 1/1/17.
 */
public class TestMutableComplex {
    @Test
    public void test1(){
        Complex c= Maths.randomComplex();
        MutableComplex m=MutableComplex.forComplex(c);
        for (int i = 0; i < 1000; i++) {
            Complex c2= Maths.randomComplex();
            switch (Maths.randomInt(5)){
                case 0:{
                    System.out.println("Check mul");
                    c=c.mul(c2);
                    m.mul(c2);
                    break;
                }
                case 1:{
                    System.out.println("Check add");
                    c=c.add(c2);
                    m.add(c2);
                    break;
                }
                case 2:{
                    System.out.println("Check exp");
                    c=c.exp();
                    m.exp();
                    break;
                }
                case 3:{
                    System.out.println("Check addProduct(,)");
                    Complex c3= Maths.randomComplex();
                    c=c.add(c2.mul(c3));
                    m.addProduct(c2,c3);
                    break;
                }
                case 4:{
                    System.out.println("Check addProduct(,,)");
                    Complex c3= Maths.randomComplex();
                    Complex c4= Maths.randomComplex();
                    c=c.add(c2.mul(c3).mul(c4));
                    m.addProduct(c2,c3,c4);
                    break;
                }
            }
            Complex mc = m.toComplex();
            boolean ignoreAssert=false;
            if(!c.equals(mc)){
                c.equals(mc);
                if(c.isNaN() && c.isNaN()){
                    ignoreAssert=true;
                }else {
                    System.out.println("Why in test");
                }
            }
            if(!ignoreAssert) {
                Assert.assertEquals(c, mc);
            }
        }
    }
}
