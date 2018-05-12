package net.vpc.scholar.hadrumaths.integration;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.Linear;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class LinearVsLinearIntegralXY  {
    private static final long serialVersionUID = 1L;
    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        return primi_linear2(domain,(Linear) f1,(Linear) f2);
    }

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
        double fa = f.a;
        double fb = f.b;
        double fc = f.c;
        double ga = g.a;
        double gb = g.b;
        double gc = g.c;

  if (  fa != 0 && fb != 0 && ga != 0 && gb != 0){
 //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*f.b*y*y/4.0+x*x*g.a*f.c*y/2.0+x*x*f.a*g.b*y*y/4.0+x*x*f.a*g.c*y/2.0+x*f.b*g.b*y*y*y/3.0+x*y*y*f.c*g.b/2.0+x*y*y*f.b*g.c/2.0+x*f.c*g.c*y;
    double t0;
          t0 = ga*fa*b1*b1*b1*b3/3.0+b1*b1*ga*fb*b3*b3/4.0+b1*b1*ga*fc*b3/2.0+b1*b1*fa*gb*b3*b3/4.0+b1*b1*fa*gc*b3/2.0+b1*fb*gb*b3*b3*b3/3.0+b1*b3*b3*fc*gb/2.0+b1*b3*b3*fb*gc/2.0+b1*fc*gc*b3;
    value  = t0;
          t0 = ga*fa*b2*b2*b2*b4/3.0+b2*b2*ga*fb*b4*b4/4.0+b2*b2*ga*fc*b4/2.0+b2*b2*fa*gb*b4*b4/4.0+b2*b2*fa*gc*b4/2.0+b2*fb*gb*b4*b4*b4/3.0+b2*b4*b4*fc*gb/2.0+b2*b4*b4*fb*gc/2.0+b2*fc*gc*b4;
    value += t0;
          t0 = ga*fa*b1*b1*b1*b4/3.0+b1*b1*ga*fb*b4*b4/4.0+b1*b1*ga*fc*b4/2.0+b1*b1*fa*gb*b4*b4/4.0+b1*b1*fa*gc*b4/2.0+b1*fb*gb*b4*b4*b4/3.0+b1*b4*b4*fc*gb/2.0+b1*b4*b4*fb*gc/2.0+b1*fc*gc*b4;
    value -= t0;
          t0 = ga*fa*b2*b2*b2*b3/3.0+b2*b2*ga*fb*b3*b3/4.0+b2*b2*ga*fc*b3/2.0+b2*b2*fa*gb*b3*b3/4.0+b2*b2*fa*gc*b3/2.0+b2*fb*gb*b3*b3*b3/3.0+b2*b3*b3*fc*gb/2.0+b2*b3*b3*fb*gc/2.0+b2*fc*gc*b3;
    value -= t0;
    return value;

  }else if (fa == 0 && fb != 0 && ga != 0 && gb != 0){
 //       t0 = x*f.b*g.b*y*y*y/3.0+x*y*y*f.c*g.b/2.0+x*x*g.a*f.b*y*y/4.0+x*y*y*f.b*g.c/2.0+x*x*g.a*f.c*y/2.0+x*f.c*g.c*y;
    double t0;
          t0 = b1*fb*gb*b3*b3*b3/3.0+b1*b3*b3*fc*gb/2.0+b1*b1*ga*fb*b3*b3/4.0+b1*b3*b3*fb*gc/2.0+b1*b1*ga*fc*b3/2.0+b1*fc*gc*b3;
    value  = t0;
          t0 = b2*fb*gb*b4*b4*b4/3.0+b2*b4*b4*fc*gb/2.0+b2*b2*ga*fb*b4*b4/4.0+b2*b4*b4*fb*gc/2.0+b2*b2*ga*fc*b4/2.0+b2*fc*gc*b4;
    value += t0;
          t0 = b1*fb*gb*b4*b4*b4/3.0+b1*b4*b4*fc*gb/2.0+b1*b1*ga*fb*b4*b4/4.0+b1*b4*b4*fb*gc/2.0+b1*b1*ga*fc*b4/2.0+b1*fc*gc*b4;
    value -= t0;
          t0 = b2*fb*gb*b3*b3*b3/3.0+b2*b3*b3*fc*gb/2.0+b2*b2*ga*fb*b3*b3/4.0+b2*b3*b3*fb*gc/2.0+b2*b2*ga*fc*b3/2.0+b2*fc*gc*b3;
    value -= t0;
    return value;

  }else if (fa != 0 && fb == 0 && ga != 0 && gb != 0){
 //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*f.c*y/2.0+x*x*f.a*g.b*y*y/4.0+x*x*f.a*g.c*y/2.0+x*y*y*f.c*g.b/2.0+x*f.c*g.c*y;
    double t0;
          t0 = ga*fa*b1*b1*b1*b3/3.0+b1*b1*ga*fc*b3/2.0+b1*b1*fa*gb*b3*b3/4.0+b1*b1*fa*gc*b3/2.0+b1*b3*b3*fc*gb/2.0+b1*fc*gc*b3;
    value  = t0;
          t0 = ga*fa*b2*b2*b2*b4/3.0+b2*b2*ga*fc*b4/2.0+b2*b2*fa*gb*b4*b4/4.0+b2*b2*fa*gc*b4/2.0+b2*b4*b4*fc*gb/2.0+b2*fc*gc*b4;
    value += t0;
          t0 = ga*fa*b1*b1*b1*b4/3.0+b1*b1*ga*fc*b4/2.0+b1*b1*fa*gb*b4*b4/4.0+b1*b1*fa*gc*b4/2.0+b1*b4*b4*fc*gb/2.0+b1*fc*gc*b4;
    value -= t0;
          t0 = ga*fa*b2*b2*b2*b3/3.0+b2*b2*ga*fc*b3/2.0+b2*b2*fa*gb*b3*b3/4.0+b2*b2*fa*gc*b3/2.0+b2*b3*b3*fc*gb/2.0+b2*fc*gc*b3;
    value -= t0;
    return value;

  }else if (fa == 0 && fb == 0 && ga != 0 && gb != 0){
 //       t0 = f.c*x*y*(g.a*x+g.b*y+2.0*g.c)/2.0;
    double t0;
          t0 = fc*b1*b3*(ga*b1+gb*b3+2.0*gc)/2.0;
    value  = t0;
          t0 = fc*b2*b4*(ga*b2+gb*b4+2.0*gc)/2.0;
    value += t0;
          t0 = fc*b1*b4*(ga*b1+gb*b4+2.0*gc)/2.0;
    value -= t0;
          t0 = fc*b2*b3*(ga*b2+gb*b3+2.0*gc)/2.0;
    value -= t0;
    return value;

  }else if (fa != 0 && fb != 0 && ga == 0 && gb != 0){
 //       t0 = x*f.b*g.b*y*y*y/3.0+x*y*y*f.b*g.c/2.0+x*x*f.a*g.b*y*y/4.0+x*y*y*f.c*g.b/2.0+x*x*f.a*g.c*y/2.0+x*f.c*g.c*y;
    double t0;
          t0 = b1*fb*gb*b3*b3*b3/3.0+b1*b3*b3*fb*gc/2.0+b1*b1*fa*gb*b3*b3/4.0+b1*b3*b3*fc*gb/2.0+b1*b1*fa*gc*b3/2.0+b1*fc*gc*b3;
    value  = t0;
          t0 = b2*fb*gb*b4*b4*b4/3.0+b2*b4*b4*fb*gc/2.0+b2*b2*fa*gb*b4*b4/4.0+b2*b4*b4*fc*gb/2.0+b2*b2*fa*gc*b4/2.0+b2*fc*gc*b4;
    value += t0;
          t0 = b1*fb*gb*b4*b4*b4/3.0+b1*b4*b4*fb*gc/2.0+b1*b1*fa*gb*b4*b4/4.0+b1*b4*b4*fc*gb/2.0+b1*b1*fa*gc*b4/2.0+b1*fc*gc*b4;
    value -= t0;
          t0 = b2*fb*gb*b3*b3*b3/3.0+b2*b3*b3*fb*gc/2.0+b2*b2*fa*gb*b3*b3/4.0+b2*b3*b3*fc*gb/2.0+b2*b2*fa*gc*b3/2.0+b2*fc*gc*b3;
    value -= t0;
    return value;

  }else if (fa == 0 && fb != 0 && ga == 0 && gb != 0){
 //       t0 = x*y*(2.0*f.b*g.b*y*y+3.0*y*f.c*g.b+3.0*y*f.b*g.c+6.0*f.c*g.c)/6.0;
    double t0;
          t0 = b1*b3*(2.0*fb*gb*b3*b3+3.0*b3*fc*gb+3.0*b3*fb*gc+6.0*fc*gc)/6.0;
    value  = t0;
          t0 = b2*b4*(2.0*fb*gb*b4*b4+3.0*b4*fc*gb+3.0*b4*fb*gc+6.0*fc*gc)/6.0;
    value += t0;
          t0 = b1*b4*(2.0*fb*gb*b4*b4+3.0*b4*fc*gb+3.0*b4*fb*gc+6.0*fc*gc)/6.0;
    value -= t0;
          t0 = b2*b3*(2.0*fb*gb*b3*b3+3.0*b3*fc*gb+3.0*b3*fb*gc+6.0*fc*gc)/6.0;
    value -= t0;
    return value;

  }else if (fa != 0 && fb == 0 && ga == 0 && gb != 0){
 //       t0 = x*(f.a*x+2.0*f.c)*y*(g.b*y+2.0*g.c)/4.0;
    double t0;
          t0 = b1*(fa*b1+2.0*fc)*b3*(gb*b3+2.0*gc)/4.0;
    value  = t0;
          t0 = b2*(fa*b2+2.0*fc)*b4*(gb*b4+2.0*gc)/4.0;
    value += t0;
          t0 = b1*(fa*b1+2.0*fc)*b4*(gb*b4+2.0*gc)/4.0;
    value -= t0;
          t0 = b2*(fa*b2+2.0*fc)*b3*(gb*b3+2.0*gc)/4.0;
    value -= t0;
    return value;

  }else if (fa == 0 && fb == 0 && ga == 0 && gb != 0){
 //       t0 = f.c*x*y*(g.b*y+2.0*g.c)/2.0;
    double t0;
          t0 = fc*b1*b3*(gb*b3+2.0*gc)/2.0;
    value  = t0;
          t0 = fc*b2*b4*(gb*b4+2.0*gc)/2.0;
    value += t0;
          t0 = fc*b1*b4*(gb*b4+2.0*gc)/2.0;
    value -= t0;
          t0 = fc*b2*b3*(gb*b3+2.0*gc)/2.0;
    value -= t0;
    return value;

  }else if (fa != 0 && fb != 0 && ga != 0 && gb == 0){
 //       t0 = g.a*f.a*x*x*x*y/3.0+x*x*g.a*f.b*y*y/4.0+x*x*g.a*f.c*y/2.0+x*x*f.a*g.c*y/2.0+x*y*y*f.b*g.c/2.0+x*f.c*g.c*y;
    double t0;
          t0 = ga*fa*b1*b1*b1*b3/3.0+b1*b1*ga*fb*b3*b3/4.0+b1*b1*ga*fc*b3/2.0+b1*b1*fa*gc*b3/2.0+b1*b3*b3*fb*gc/2.0+b1*fc*gc*b3;
    value  = t0;
          t0 = ga*fa*b2*b2*b2*b4/3.0+b2*b2*ga*fb*b4*b4/4.0+b2*b2*ga*fc*b4/2.0+b2*b2*fa*gc*b4/2.0+b2*b4*b4*fb*gc/2.0+b2*fc*gc*b4;
    value += t0;
          t0 = ga*fa*b1*b1*b1*b4/3.0+b1*b1*ga*fb*b4*b4/4.0+b1*b1*ga*fc*b4/2.0+b1*b1*fa*gc*b4/2.0+b1*b4*b4*fb*gc/2.0+b1*fc*gc*b4;
    value -= t0;
          t0 = ga*fa*b2*b2*b2*b3/3.0+b2*b2*ga*fb*b3*b3/4.0+b2*b2*ga*fc*b3/2.0+b2*b2*fa*gc*b3/2.0+b2*b3*b3*fb*gc/2.0+b2*fc*gc*b3;
    value -= t0;
    return value;

  }else if (fa == 0 && fb != 0 && ga != 0 && gb == 0){
 //       t0 = x*(g.a*x+2.0*g.c)*y*(f.b*y+2.0*f.c)/4.0;
    double t0;
          t0 = b1*(ga*b1+2.0*gc)*b3*(fb*b3+2.0*fc)/4.0;
    value  = t0;
          t0 = b2*(ga*b2+2.0*gc)*b4*(fb*b4+2.0*fc)/4.0;
    value += t0;
          t0 = b1*(ga*b1+2.0*gc)*b4*(fb*b4+2.0*fc)/4.0;
    value -= t0;
          t0 = b2*(ga*b2+2.0*gc)*b3*(fb*b3+2.0*fc)/4.0;
    value -= t0;
    return value;

  }else if (fa != 0 && fb == 0 && ga != 0 && gb == 0){
 //       t0 = x*(2.0*f.a*g.a*x*x+3.0*x*f.c*g.a+3.0*x*f.a*g.c+6.0*f.c*g.c)*y/6.0;
    double t0;
          t0 = b1*(2.0*fa*ga*b1*b1+3.0*b1*fc*ga+3.0*b1*fa*gc+6.0*fc*gc)*b3/6.0;
    value  = t0;
          t0 = b2*(2.0*fa*ga*b2*b2+3.0*b2*fc*ga+3.0*b2*fa*gc+6.0*fc*gc)*b4/6.0;
    value += t0;
          t0 = b1*(2.0*fa*ga*b1*b1+3.0*b1*fc*ga+3.0*b1*fa*gc+6.0*fc*gc)*b4/6.0;
    value -= t0;
          t0 = b2*(2.0*fa*ga*b2*b2+3.0*b2*fc*ga+3.0*b2*fa*gc+6.0*fc*gc)*b3/6.0;
    value -= t0;
    return value;

  }else if (fa == 0 && fb == 0 && ga != 0 && gb == 0){
 //       t0 = f.c*x*(g.a*x+2.0*g.c)*y/2.0;
    double t0;
          t0 = fc*b1*(ga*b1+2.0*gc)*b3/2.0;
    value  = t0;
          t0 = fc*b2*(ga*b2+2.0*gc)*b4/2.0;
    value += t0;
          t0 = fc*b1*(ga*b1+2.0*gc)*b4/2.0;
    value -= t0;
          t0 = fc*b2*(ga*b2+2.0*gc)*b3/2.0;
    value -= t0;
    return value;

  }else if (fa != 0 && fb != 0 && ga == 0 && gb == 0){
 //       t0 = g.c*x*y*(f.a*x+f.b*y+2.0*f.c)/2.0;
    double t0;
          t0 = gc*b1*b3*(fa*b1+fb*b3+2.0*fc)/2.0;
    value  = t0;
          t0 = gc*b2*b4*(fa*b2+fb*b4+2.0*fc)/2.0;
    value += t0;
          t0 = gc*b1*b4*(fa*b1+fb*b4+2.0*fc)/2.0;
    value -= t0;
          t0 = gc*b2*b3*(fa*b2+fb*b3+2.0*fc)/2.0;
    value -= t0;
    return value;

  }else if (fa == 0 && fb != 0 && ga == 0 && gb == 0){
 //       t0 = g.c*x*y*(f.b*y+2.0*f.c)/2.0;
    double t0;
          t0 = gc*b1*b3*(fb*b3+2.0*fc)/2.0;
    value  = t0;
          t0 = gc*b2*b4*(fb*b4+2.0*fc)/2.0;
    value += t0;
          t0 = gc*b1*b4*(fb*b4+2.0*fc)/2.0;
    value -= t0;
          t0 = gc*b2*b3*(fb*b3+2.0*fc)/2.0;
    value -= t0;
    return value;

  }else if (fa != 0 && fb == 0 && ga == 0 && gb == 0){
 //       t0 = g.c*x*(f.a*x+2.0*f.c)*y/2.0;
    double t0;
          t0 = gc*b1*(fa*b1+2.0*fc)*b3/2.0;
    value  = t0;
          t0 = gc*b2*(fa*b2+2.0*fc)*b4/2.0;
    value += t0;
          t0 = gc*b1*(fa*b1+2.0*fc)*b4/2.0;
    value -= t0;
          t0 = gc*b2*(fa*b2+2.0*fc)*b3/2.0;
    value -= t0;
    return value;

  }else{ //all are nulls
 //       t0 = x*f.c*g.c*y;
    double t0;
          t0 = b1*fc*gc*b3;
    value  = t0;
          t0 = b2*fc*gc*b4;
    value += t0;
          t0 = b1*fc*gc*b4;
    value -= t0;
          t0 = b2*fc*gc*b3;
    value -= t0;
    return value;
  }
}
//ENDING---------------------------------------
}
