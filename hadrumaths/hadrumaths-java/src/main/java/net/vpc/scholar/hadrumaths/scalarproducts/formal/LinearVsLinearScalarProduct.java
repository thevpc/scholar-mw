package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.Linear;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class LinearVsLinearScalarProduct implements FormalScalarProductHelper {
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !obj.getClass().equals(getClass())){
            return false;
        }
        return true;
    }
    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        return primi_linear2(domain,(Linear) f1,(Linear) f2);
//        double b1 = domain.xmin;
//        double b2 = domain.xmax;
//        double b3 = domain.ymin;
//        double b4 = domain.ymax;
//        DLinearFunction f1ok = (DLinearFunction) f1;
//        DLinearFunction f2ok = (DLinearFunction) f2;
//        return _ps_ll_x(b1, b3, f1ok, f2ok)
//                + _ps_ll_x(b2, b4, f1ok, f2ok)
//                - _ps_ll_x(b1, b4, f1ok, f2ok)
//                - _ps_ll_x(b2, b3, f1ok, f2ok);
    }

//    private static double _ps_ll_x(double x, double y, DLinearFunction f1, DLinearFunction f2) {
//        if (f1.a == 0 && f2.a == 0) {
//            return f1.b * f2.b * x * y;
//
////        } else if (f1.a != 0 && f2.a == 0) {
//        } else if (f2.a == 0) {
//            return f2.b * x * y * (0.5 * f1.a * x + f1.b);
//
////        } else if (f1.a == 0 && f2.a != 0) {
//        } else if (f1.a == 0) {
//            return f1.b * x * y * (0.5 * f2.a * x + f2.b);
//
//        } else {//none is null
//            return x * y * (1.0 / 3.0 * f1.a * f2.a * x * x + 0.5 * (f1.b * f2.a + f1.a * f2.b) * x + f1.b * f2.b);
//        }
//    }

//STARTING---------------------------------------
 // THIS FILE WAS GENERATED AUTOMATICALLY.
 // DO NOT EDIT MANUALLY.
 // INTEGRATION FOR (f_a*x+f_b*y+f_c)*(g_a*x+g_b*y+g_c)
   public static double primi_linear2(Domain domain,Linear f,Linear g){
  double value;
  double b1 = domain.xmin();
  double b2 = domain.xmax();
  double b3 = domain.ymin();
  double b4 = domain.ymax();

  if (f.a != 0 && f.b != 0 && g.a != 0 && g.b != 0){
 //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*f.b*y*y/4.0+x*x*g.a*f.c*y/2.0+x*x*f.a*g.b*y*y/4.0+x*x*f.a*g.c*y/2.0+x*f.b*g.b*y*y*y/3.0+x*y*y*f.c*g.b/2.0+x*y*y*f.b*g.c/2.0+x*f.c*g.c*y;
    double t0;
          t0 = g.a*f.a*b1*b1*b1*b3/3.0+b1*b1*g.a*f.b*b3*b3/4.0+b1*b1*g.a*f.c*b3/2.0+b1*b1*f.a*g.b*b3*b3/4.0+b1*b1*f.a*g.c*b3/2.0+b1*f.b*g.b*b3*b3*b3/3.0+b1*b3*b3*f.c*g.b/2.0+b1*b3*b3*f.b*g.c/2.0+b1*f.c*g.c*b3;
    value  = t0;
          t0 = g.a*f.a*b2*b2*b2*b4/3.0+b2*b2*g.a*f.b*b4*b4/4.0+b2*b2*g.a*f.c*b4/2.0+b2*b2*f.a*g.b*b4*b4/4.0+b2*b2*f.a*g.c*b4/2.0+b2*f.b*g.b*b4*b4*b4/3.0+b2*b4*b4*f.c*g.b/2.0+b2*b4*b4*f.b*g.c/2.0+b2*f.c*g.c*b4;
    value += t0;
          t0 = g.a*f.a*b1*b1*b1*b4/3.0+b1*b1*g.a*f.b*b4*b4/4.0+b1*b1*g.a*f.c*b4/2.0+b1*b1*f.a*g.b*b4*b4/4.0+b1*b1*f.a*g.c*b4/2.0+b1*f.b*g.b*b4*b4*b4/3.0+b1*b4*b4*f.c*g.b/2.0+b1*b4*b4*f.b*g.c/2.0+b1*f.c*g.c*b4;
    value -= t0;
          t0 = g.a*f.a*b2*b2*b2*b3/3.0+b2*b2*g.a*f.b*b3*b3/4.0+b2*b2*g.a*f.c*b3/2.0+b2*b2*f.a*g.b*b3*b3/4.0+b2*b2*f.a*g.c*b3/2.0+b2*f.b*g.b*b3*b3*b3/3.0+b2*b3*b3*f.c*g.b/2.0+b2*b3*b3*f.b*g.c/2.0+b2*f.c*g.c*b3;
    value -= t0;
    return value;

  }else if (f.a == 0 && f.b != 0 && g.a != 0 && g.b != 0){
 //       t0 = x*f.b*g.b*y*y*y/3.0+x*y*y*f.c*g.b/2.0+x*x*g.a*f.b*y*y/4.0+x*y*y*f.b*g.c/2.0+x*x*g.a*f.c*y/2.0+x*f.c*g.c*y;
    double t0;
          t0 = b1*f.b*g.b*b3*b3*b3/3.0+b1*b3*b3*f.c*g.b/2.0+b1*b1*g.a*f.b*b3*b3/4.0+b1*b3*b3*f.b*g.c/2.0+b1*b1*g.a*f.c*b3/2.0+b1*f.c*g.c*b3;
    value  = t0;
          t0 = b2*f.b*g.b*b4*b4*b4/3.0+b2*b4*b4*f.c*g.b/2.0+b2*b2*g.a*f.b*b4*b4/4.0+b2*b4*b4*f.b*g.c/2.0+b2*b2*g.a*f.c*b4/2.0+b2*f.c*g.c*b4;
    value += t0;
          t0 = b1*f.b*g.b*b4*b4*b4/3.0+b1*b4*b4*f.c*g.b/2.0+b1*b1*g.a*f.b*b4*b4/4.0+b1*b4*b4*f.b*g.c/2.0+b1*b1*g.a*f.c*b4/2.0+b1*f.c*g.c*b4;
    value -= t0;
          t0 = b2*f.b*g.b*b3*b3*b3/3.0+b2*b3*b3*f.c*g.b/2.0+b2*b2*g.a*f.b*b3*b3/4.0+b2*b3*b3*f.b*g.c/2.0+b2*b2*g.a*f.c*b3/2.0+b2*f.c*g.c*b3;
    value -= t0;
    return value;

  }else if (f.a != 0 && f.b == 0 && g.a != 0 && g.b != 0){
 //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*f.c*y/2.0+x*x*f.a*g.b*y*y/4.0+x*x*f.a*g.c*y/2.0+x*y*y*f.c*g.b/2.0+x*f.c*g.c*y;
    double t0;
          t0 = g.a*f.a*b1*b1*b1*b3/3.0+b1*b1*g.a*f.c*b3/2.0+b1*b1*f.a*g.b*b3*b3/4.0+b1*b1*f.a*g.c*b3/2.0+b1*b3*b3*f.c*g.b/2.0+b1*f.c*g.c*b3;
    value  = t0;
          t0 = g.a*f.a*b2*b2*b2*b4/3.0+b2*b2*g.a*f.c*b4/2.0+b2*b2*f.a*g.b*b4*b4/4.0+b2*b2*f.a*g.c*b4/2.0+b2*b4*b4*f.c*g.b/2.0+b2*f.c*g.c*b4;
    value += t0;
          t0 = g.a*f.a*b1*b1*b1*b4/3.0+b1*b1*g.a*f.c*b4/2.0+b1*b1*f.a*g.b*b4*b4/4.0+b1*b1*f.a*g.c*b4/2.0+b1*b4*b4*f.c*g.b/2.0+b1*f.c*g.c*b4;
    value -= t0;
          t0 = g.a*f.a*b2*b2*b2*b3/3.0+b2*b2*g.a*f.c*b3/2.0+b2*b2*f.a*g.b*b3*b3/4.0+b2*b2*f.a*g.c*b3/2.0+b2*b3*b3*f.c*g.b/2.0+b2*f.c*g.c*b3;
    value -= t0;
    return value;

  }else if (f.a == 0 && f.b == 0 && g.a != 0 && g.b != 0){
 //       t0 = f.c*x*y*(g.a*x+g.b*y+2.0*g.c)/2.0;
    double t0;
          t0 = f.c*b1*b3*(g.a*b1+g.b*b3+2.0*g.c)/2.0;
    value  = t0;
          t0 = f.c*b2*b4*(g.a*b2+g.b*b4+2.0*g.c)/2.0;
    value += t0;
          t0 = f.c*b1*b4*(g.a*b1+g.b*b4+2.0*g.c)/2.0;
    value -= t0;
          t0 = f.c*b2*b3*(g.a*b2+g.b*b3+2.0*g.c)/2.0;
    value -= t0;
    return value;

  }else if (f.a != 0 && f.b != 0 && g.a == 0 && g.b != 0){
 //       t0 = x*f.b*g.b*y*y*y/3.0+x*y*y*f.b*g.c/2.0+x*x*f.a*g.b*y*y/4.0+x*y*y*f.c*g.b/2.0+x*x*f.a*g.c*y/2.0+x*f.c*g.c*y;
    double t0;
          t0 = b1*f.b*g.b*b3*b3*b3/3.0+b1*b3*b3*f.b*g.c/2.0+b1*b1*f.a*g.b*b3*b3/4.0+b1*b3*b3*f.c*g.b/2.0+b1*b1*f.a*g.c*b3/2.0+b1*f.c*g.c*b3;
    value  = t0;
          t0 = b2*f.b*g.b*b4*b4*b4/3.0+b2*b4*b4*f.b*g.c/2.0+b2*b2*f.a*g.b*b4*b4/4.0+b2*b4*b4*f.c*g.b/2.0+b2*b2*f.a*g.c*b4/2.0+b2*f.c*g.c*b4;
    value += t0;
          t0 = b1*f.b*g.b*b4*b4*b4/3.0+b1*b4*b4*f.b*g.c/2.0+b1*b1*f.a*g.b*b4*b4/4.0+b1*b4*b4*f.c*g.b/2.0+b1*b1*f.a*g.c*b4/2.0+b1*f.c*g.c*b4;
    value -= t0;
          t0 = b2*f.b*g.b*b3*b3*b3/3.0+b2*b3*b3*f.b*g.c/2.0+b2*b2*f.a*g.b*b3*b3/4.0+b2*b3*b3*f.c*g.b/2.0+b2*b2*f.a*g.c*b3/2.0+b2*f.c*g.c*b3;
    value -= t0;
    return value;

  }else if (f.a == 0 && f.b != 0 && g.a == 0 && g.b != 0){
 //       t0 = x*y*(2.0*f.b*g.b*y*y+3.0*y*f.c*g.b+3.0*y*f.b*g.c+6.0*f.c*g.c)/6.0;
    double t0;
          t0 = b1*b3*(2.0*f.b*g.b*b3*b3+3.0*b3*f.c*g.b+3.0*b3*f.b*g.c+6.0*f.c*g.c)/6.0;
    value  = t0;
          t0 = b2*b4*(2.0*f.b*g.b*b4*b4+3.0*b4*f.c*g.b+3.0*b4*f.b*g.c+6.0*f.c*g.c)/6.0;
    value += t0;
          t0 = b1*b4*(2.0*f.b*g.b*b4*b4+3.0*b4*f.c*g.b+3.0*b4*f.b*g.c+6.0*f.c*g.c)/6.0;
    value -= t0;
          t0 = b2*b3*(2.0*f.b*g.b*b3*b3+3.0*b3*f.c*g.b+3.0*b3*f.b*g.c+6.0*f.c*g.c)/6.0;
    value -= t0;
    return value;

  }else if (f.a != 0 && f.b == 0 && g.a == 0 && g.b != 0){
 //       t0 = x*(f.a*x+2.0*f.c)*y*(g.b*y+2.0*g.c)/4.0;
    double t0;
          t0 = b1*(f.a*b1+2.0*f.c)*b3*(g.b*b3+2.0*g.c)/4.0;
    value  = t0;
          t0 = b2*(f.a*b2+2.0*f.c)*b4*(g.b*b4+2.0*g.c)/4.0;
    value += t0;
          t0 = b1*(f.a*b1+2.0*f.c)*b4*(g.b*b4+2.0*g.c)/4.0;
    value -= t0;
          t0 = b2*(f.a*b2+2.0*f.c)*b3*(g.b*b3+2.0*g.c)/4.0;
    value -= t0;
    return value;

  }else if (f.a == 0 && f.b == 0 && g.a == 0 && g.b != 0){
 //       t0 = f.c*x*y*(g.b*y+2.0*g.c)/2.0;
    double t0;
          t0 = f.c*b1*b3*(g.b*b3+2.0*g.c)/2.0;
    value  = t0;
          t0 = f.c*b2*b4*(g.b*b4+2.0*g.c)/2.0;
    value += t0;
          t0 = f.c*b1*b4*(g.b*b4+2.0*g.c)/2.0;
    value -= t0;
          t0 = f.c*b2*b3*(g.b*b3+2.0*g.c)/2.0;
    value -= t0;
    return value;

  }else if (f.a != 0 && f.b != 0 && g.a != 0 && g.b == 0){
 //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*f.b*y*y/4.0+x*x*g.a*f.c*y/2.0+x*x*f.a*g.c*y/2.0+x*y*y*f.b*g.c/2.0+x*f.c*g.c*y;
    double t0;
          t0 = g.a*f.a*b1*b1*b1*b3/3.0+b1*b1*g.a*f.b*b3*b3/4.0+b1*b1*g.a*f.c*b3/2.0+b1*b1*f.a*g.c*b3/2.0+b1*b3*b3*f.b*g.c/2.0+b1*f.c*g.c*b3;
    value  = t0;
          t0 = g.a*f.a*b2*b2*b2*b4/3.0+b2*b2*g.a*f.b*b4*b4/4.0+b2*b2*g.a*f.c*b4/2.0+b2*b2*f.a*g.c*b4/2.0+b2*b4*b4*f.b*g.c/2.0+b2*f.c*g.c*b4;
    value += t0;
          t0 = g.a*f.a*b1*b1*b1*b4/3.0+b1*b1*g.a*f.b*b4*b4/4.0+b1*b1*g.a*f.c*b4/2.0+b1*b1*f.a*g.c*b4/2.0+b1*b4*b4*f.b*g.c/2.0+b1*f.c*g.c*b4;
    value -= t0;
          t0 = g.a*f.a*b2*b2*b2*b3/3.0+b2*b2*g.a*f.b*b3*b3/4.0+b2*b2*g.a*f.c*b3/2.0+b2*b2*f.a*g.c*b3/2.0+b2*b3*b3*f.b*g.c/2.0+b2*f.c*g.c*b3;
    value -= t0;
    return value;

  }else if (f.a == 0 && f.b != 0 && g.a != 0 && g.b == 0){
 //       t0 = x*(g.a*x+2.0*g.c)*y*(f.b*y+2.0*f.c)/4.0;
    double t0;
          t0 = b1*(g.a*b1+2.0*g.c)*b3*(f.b*b3+2.0*f.c)/4.0;
    value  = t0;
          t0 = b2*(g.a*b2+2.0*g.c)*b4*(f.b*b4+2.0*f.c)/4.0;
    value += t0;
          t0 = b1*(g.a*b1+2.0*g.c)*b4*(f.b*b4+2.0*f.c)/4.0;
    value -= t0;
          t0 = b2*(g.a*b2+2.0*g.c)*b3*(f.b*b3+2.0*f.c)/4.0;
    value -= t0;
    return value;

  }else if (f.a != 0 && f.b == 0 && g.a != 0 && g.b == 0){
 //       t0 = x*(2.0*f.a*g.a*x*x+3.0*x*f.c*g.a+3.0*x*f.a*g.c+6.0*f.c*g.c)*y/6.0;
    double t0;
          t0 = b1*(2.0*f.a*g.a*b1*b1+3.0*b1*f.c*g.a+3.0*b1*f.a*g.c+6.0*f.c*g.c)*b3/6.0;
    value  = t0;
          t0 = b2*(2.0*f.a*g.a*b2*b2+3.0*b2*f.c*g.a+3.0*b2*f.a*g.c+6.0*f.c*g.c)*b4/6.0;
    value += t0;
          t0 = b1*(2.0*f.a*g.a*b1*b1+3.0*b1*f.c*g.a+3.0*b1*f.a*g.c+6.0*f.c*g.c)*b4/6.0;
    value -= t0;
          t0 = b2*(2.0*f.a*g.a*b2*b2+3.0*b2*f.c*g.a+3.0*b2*f.a*g.c+6.0*f.c*g.c)*b3/6.0;
    value -= t0;
    return value;

  }else if (f.a == 0 && f.b == 0 && g.a != 0 && g.b == 0){
 //       t0 = f.c*x*(g.a*x+2.0*g.c)*y/2.0;
    double t0;
          t0 = f.c*b1*(g.a*b1+2.0*g.c)*b3/2.0;
    value  = t0;
          t0 = f.c*b2*(g.a*b2+2.0*g.c)*b4/2.0;
    value += t0;
          t0 = f.c*b1*(g.a*b1+2.0*g.c)*b4/2.0;
    value -= t0;
          t0 = f.c*b2*(g.a*b2+2.0*g.c)*b3/2.0;
    value -= t0;
    return value;

  }else if (f.a != 0 && f.b != 0 && g.a == 0 && g.b == 0){
 //       t0 = g.c*x*y*(f.a*x+f.b*y+2.0*f.c)/2.0;
    double t0;
          t0 = g.c*b1*b3*(f.a*b1+f.b*b3+2.0*f.c)/2.0;
    value  = t0;
          t0 = g.c*b2*b4*(f.a*b2+f.b*b4+2.0*f.c)/2.0;
    value += t0;
          t0 = g.c*b1*b4*(f.a*b1+f.b*b4+2.0*f.c)/2.0;
    value -= t0;
          t0 = g.c*b2*b3*(f.a*b2+f.b*b3+2.0*f.c)/2.0;
    value -= t0;
    return value;

  }else if (f.a == 0 && f.b != 0 && g.a == 0 && g.b == 0){
 //       t0 = g.c*x*y*(f.b*y+2.0*f.c)/2.0;
    double t0;
          t0 = g.c*b1*b3*(f.b*b3+2.0*f.c)/2.0;
    value  = t0;
          t0 = g.c*b2*b4*(f.b*b4+2.0*f.c)/2.0;
    value += t0;
          t0 = g.c*b1*b4*(f.b*b4+2.0*f.c)/2.0;
    value -= t0;
          t0 = g.c*b2*b3*(f.b*b3+2.0*f.c)/2.0;
    value -= t0;
    return value;

  }else if (f.a != 0 && f.b == 0 && g.a == 0 && g.b == 0){
 //       t0 = g.c*x*(f.a*x+2.0*f.c)*y/2.0;
    double t0;
          t0 = g.c*b1*(f.a*b1+2.0*f.c)*b3/2.0;
    value  = t0;
          t0 = g.c*b2*(f.a*b2+2.0*f.c)*b4/2.0;
    value += t0;
          t0 = g.c*b1*(f.a*b1+2.0*f.c)*b4/2.0;
    value -= t0;
          t0 = g.c*b2*(f.a*b2+2.0*f.c)*b3/2.0;
    value -= t0;
    return value;

  }else{ //all are nulls
 //       t0 = x*f.c*g.c*y;
    double t0;
          t0 = b1*f.c*g.c*b3;
    value  = t0;
          t0 = b2*f.c*g.c*b4;
    value += t0;
          t0 = b1*f.c*g.c*b4;
    value -= t0;
          t0 = b2*f.c*g.c*b3;
    value -= t0;
    return value;
  }
}
//ENDING---------------------------------------
}
