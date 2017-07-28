//package test;
//
//import net.vpc.scholar.math.symbolic.SymExpression;
//import net.vpc.scholar.math.symbolic.SymVar;
//import net.vpc.scholar.math.symbolic.SymMatrix;
//import net.vpc.scholar.math.symbolic.symexprerssions.SymSin;
//import net.vpc.scholar.math.symbolic.symexprerssions.SymRot;
//import net.vpc.scholar.math.symbolic.symexprerssions.SymCos;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 31 juil. 2007 21:56:38
// */
//public class TestSymMatrix {
//    public static void main(String[] args) {
//        SymExpression xs = new SymSin(SymVar.x.add(SymVar.y)).div(SymVar.x);
//        SymMatrix m=new SymMatrix(new SymExpression[][]{
//                {xs, SymVar.y.mul(SymVar.x), SymVar.y},
//                {SymVar.x, SymVar.y, new SymCos(SymVar.y)}
//        });
//        SymMatrix m2=new SymMatrix(new SymExpression[][]{
//                {xs},
//                {new SymCos(SymVar.y)},
//                {new SymSin(SymVar.x)},
//        });
//        System.out.println("m = \n" + m);
//        System.out.println("m = \n" + new SymRot(m2).simplify());
//        System.out.println("dm/dx = \n" + xs.diff("x"));
//        System.out.println(xs);
//    }
//}
