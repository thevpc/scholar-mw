package net.thevpc.scholar.hadrumaths.plot.random;

import java.lang.reflect.Method;

public class TestDeclaredMethods {
    public static void main(String[] args) {
        try {
            for (Method declaredMethod : Class.forName("net.thevpc.scholar.hadrumaths.symbolic.polymorph.AnyDoubleToDouble").getDeclaredMethods()) {
                System.out.println(declaredMethod);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void doit(){

    }

    public static class A extends TestDeclaredMethods{

    }
}
