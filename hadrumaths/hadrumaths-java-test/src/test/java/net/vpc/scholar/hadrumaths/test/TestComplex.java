package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.Complex;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2007 21:46:18
 */
public class TestComplex {
    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            Complex c = new Complex(i);
            System.out.println(c.cotanh()+":"+c.cosh().div(c.sinh()));
        }
    }

}
