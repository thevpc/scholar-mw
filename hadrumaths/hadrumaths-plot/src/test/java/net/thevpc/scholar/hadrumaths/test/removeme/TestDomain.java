package net.thevpc.scholar.hadrumaths.test.removeme;

import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.geom.Polygon;
import net.thevpc.scholar.hadrumaths.AbsoluteSamples;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.thevpc.scholar.hadruplot.Plot;

import java.util.HashMap;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.GeometryFactory;

import net.thevpc.scholar.hadrumaths.Maths;
import static net.thevpc.scholar.hadrumaths.Maths.X;
import static net.thevpc.scholar.hadrumaths.Maths.Y;
import static net.thevpc.scholar.hadrumaths.Maths.expr;
import static net.thevpc.scholar.hadrumaths.Maths.inv;
import static net.thevpc.scholar.hadrumaths.Maths.matrix;
import static net.thevpc.scholar.hadrumaths.Maths.mul;
import static net.thevpc.scholar.hadrumaths.Maths.minus;

/**
 * Created by vpc on 7/20/15.
 */
public class TestDomain {


    public static void main(String[] args) {
        Mul m = Mul.of(Maths.expr(3, Maths.domain(0, 1, 0, 1)), Maths.X);
        Plus p=Plus.of(m);
        System.out.println(m);
        System.out.println(p);
    }
    public static void main2(String[] args) {
        HashMap<String,String> a=new HashMap<String, String>(800000);
        for (int i = 1; i < 20000; i++) {
            for (int j = 1; j < 20000; j++) {
                String x=i+","+(i*31+j);
                String kk=x+"<="+i+","+j;
                if(a.containsKey(x)){
                    throw new RuntimeException("Collide for "+kk+" vs "+a.get(x));
                }
                a.put(x,kk);
            }
        }
    }
    public static void main_i(String[] args) {
        Polygon polygon = GeometryFactory.createPolygon(new Point[]{
                new Point(0, 1),
                new Point(1, 0),
                new Point(0, -1),
                new Point(-1, 0),
        });
        Domain domain = polygon.getDomain();
        System.out.println(domain);
        Expr a = mul(inv(mul(minus(X, expr(0.75)), minus(Y, expr(0.75)))),expr(Domain.ofBounds(-1, 1, -1, 1)));
        Expr b = mul(mul(expr(polygon),expr(1000000)), inv(mul(minus(X, expr(0.75)), minus(Y, expr(0.75)))));
//        Expr b = mul(mul(expr(polygon),expr(1000000)));
//        Expr a = expr(polygon);
//        Expr a = mul(expr(1), inv(mul(sub(X, expr(0.75)), sub(Y, expr(0.75)))));
        AbsoluteSamples times = domain.times(50);
        double[] x = times.getX();
        double[] y = times.getY();
        ComplexMatrix c = matrix(30, 30, (row, column) -> {
            return Complex.of(Maths.sin(row* Maths.PI/10) * Maths.cos(column* Maths.PI/10));
//                return new Complex((row*Math.PI/10) * (column*Math.PI/10));
        });
        System.out.println(c);
//        DoubleToDouble b = a.toDD();
//        Out<Range> ranges = new Out<Range>();
//        double[][] doubles = b.computeDouble(x, y, null, ranges);
//        Matrix matrix = Matrix.matrix(doubles);
//        System.out.println(matrix);
//        Plot.plot(matrix);
        Plot.plot(a);
        Plot.plot(b);
//        Plot.plot(c);
        Plot.asMatrix().plot(c);
        Plot.asHeatMap().plot(c);
        Plot.asMesh().plot(c);
    }
}
