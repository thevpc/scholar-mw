package net.thevpc.scholar.hadrumaths.test.scalarproducts.formal;

//package scalarproducts.formal;
//
//import net.thevpc.scholar.math.functions.DomainXY;
//import net.thevpc.scholar.math.functions.dfxy.DCosCosFunctionXY;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import net.thevpc.scholar.math.interop.matlab.MatlabFactory;
//import static java.lang.Math.PI;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 31 juil. 2007 21:53:39
// */
//public class TestScalarProduct {
//    public static void main(String[] args) {
//        BufferedReader fr = null;
//        try {
//            //fr=new BufferedReader(new FileReader("c:/xxx.txt"));
//            DomainXY domain = new DomainXY(-1.3 * PI, -PI / 3.5, 2.3 * PI, +PI * 5.7);
//            int counter = 0;
//            for (int m1 = 1; m1 <= 1; m1++) {
//                for (int a1 = -1; a1 <= 1; a1++) {
//                    for (int b1 = 0; b1 <= 0; b1++) {
//                        for (int c1 = -1; c1 <= 1; c1++) {
//                            for (int d1 = 0; d1 <= 0; d1++) {
//                                for (int m2 = 1; m2 <= 1; m2++) {
//                                    for (int a2 = -1; a2 <= 1; a2++) {
//                                        for (int b2 = 0; b2 <= 0; b2++) {
//                                            for (int c2 = -1; c2 <= 1; c2++) {
//                                                for (int d2 = 0; d2 <= 0; d2++) {
//                                                    counter++;
//                                                    double dbl =
//                                                            CosCosVsCosCosScalarProduct.compute(domain,
//                                                                    new DCosCosFunctionXY(m1, a1, b1, c1, d1, domain),
//                                                                    new DCosCosFunctionXY(m2, a2, b2, c2, d2, domain));
//                                                    if (Double.isNaN(dbl)) {
//                                                        System.out.printf(" (%d, %d, %d, %d, %d) (%d, %d, %d, %d, %d) %n", m1, a1, b1, c1, d1, m2, a2, b2, c2, d2);
//                                                        CosCosVsCosCosScalarProduct.compute(domain,
//                                                                new DCosCosFunctionXY(m1, a1, b1, c1, d1, domain),
//                                                                new DCosCosFunctionXY(m2, a2, b2, c2, d2, domain));
//                                                    }
//                                                    //MatlabFactory.toMatlabString(d2, format)
//                                                    System.out.println("[" + counter + "] " + dbl + " = (" + m1 + ", " + a1 + ", " + b1 + ", " + c1 + ", " + d1 + ") (" + m2 + ", " + a2 + ", " + b2 + ", " + c2 + ", " + d2 + ") " + MatlabFactory.toMatlabString(new DCosCosFunctionXY(m1, a1, b1, c1, d1, domain)) + " * " + MatlabFactory.toMatlabString(new DCosCosFunctionXY(m2, a2, b2, c2, d2, domain)));
//                                                    //System.out.println(dbl);
////                                                    ArrayList sl=new ArrayList();
////                                                    String line=fr.readLine();
////                                                    for (StringTokenizer stringTokenizer = new StringTokenizer(line," ;\t"); stringTokenizer.hasMoreTokens();) {
////                                                        sl.add(stringTokenizer.nextToken().trim());
////                                                    }
////
////                                                    String[] sss= (String[]) sl.toArray(new String[sl.size()]);
//// ;
////                                                    double l=Double.parseDouble(sss[0].trim());
////                                                    if(l!=dbl){
////                                                        System.out.printf("%f <> %f  for (%d, %d, %d, %d, %d) (%d, %d, %d, %d, %d) %s %n",l,dbl,m1, a1, b1, c1, d1, m2, a2, b2, c2, d2,sss[11]);
////                                                    }else{
////                                                        //System.out.printf("%f == %f  for (%d, %d, %d, %d, %d) (%d, %d, %d, %d, %d) %s %n",l,dbl,m1, a1, b1, c1, d1, m2, a2, b2, c2, d2,sss[11]);
////                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fr != null) {
//                    fr.close();
//                }
//            } catch (IOException e) {
//              //
//            }
//        }
//    }
//}
