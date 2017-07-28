package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.DMatrix;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 30 mai 2007 00:49:05
 */
public class DLineEQ {
    public double a;
    public double b;
    public double c;


    public DLineEQ(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getValue(double x, double y) {
        return a * x + b * y + c;
    }

    public double getValue(Point p) {
        return getValue(p.x, p.y);
    }

    public DLineEQ(Point p1, Point p2) {
        if (p1.x == p2.x && p1.y == p2.y) {
            this.a = 0;
            this.b = 0;
            this.c = 0;
        } else if (p1.x == p2.x) {
            this.a = 1;
            this.b = 0;
            this.c = -p1.x;
        } else if (p1.y == p2.y) {
            this.a = 0;
            this.b = 1;
            this.c = -p1.y;
        } else {
            this.a = 1;
            this.b = -(p2.x - p1.x) / (p2.y - p1.y);
            this.c = -p1.x - b * p1.y;
        }

    }

    public Point intersect(DLineEQ line) {
        DMatrix c = new DMatrix(new double[][]{{line.a, line.b}, {this.a, this.b}});
        DMatrix r;
        try {
            r = c.solve(new DMatrix(new double[][]{{-line.c}, {-this.c}}));
        } catch (Exception e) {
            return null;
        }
        return Point.create(r.get(0, 0), r.get(1, 0));
    }
}
