package net.thevpc.scholar.hadrumaths.scalarproducts;

import net.thevpc.scholar.hadrumaths.BooleanMarker;
import net.thevpc.scholar.hadrumaths.BooleanRef;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

public class ScalarProductHelper {
    public static double integrateOverTriangleUsingGaussianQuadrature(HTriangle t, DoubleToDouble f, DoubleToDouble g) {
        // 1. Quadrature weights and coordinates for a Unit Triangle
        // Coordinates (alpha, beta, gamma) where gamma = 1 - alpha - beta

        // The weights (w)
        double w0 = 0.2250000000000000; // Centroid
        double w1 = 0.1323941527885062; // Inner set (3 points)
        double w2 = 0.1259391805448272; // Outer set (3 points)

        // Barycentric coordinates for the 3 sets
        double a1 = 0.0597158717897698;
        double b1 = 0.4701420641051151;

        double a2 = 0.7974269853530873;
        double b2 = 0.1012865073234563;

        // Define the 7 points in (xi, eta) space
        double[][] qPoints = {
                {1.0 / 3.0, 1.0 / 3.0, w0}, // Point 1 (Centroid)
                {a1, b1, w1}, {b1, a1, w1}, {1 - a1 - b1, a1, w1}, // Points 2,3,4
                {a2, b2, w2}, {b2, a2, w2}, {1 - a2 - b2, a2, w2}  // Points 5,6,7
        };

        double integralSum = 0;
        BooleanRef defined = BooleanMarker.ref();

        // The Jacobian for a triangle is 2 * Surface
        // Since weights for unit triangle sum to 0.5, we multiply by (2 * Surface)
        double jacobian = 2.0 * t.area();

        for (double[] qp : qPoints) {
            double xi = qp[0];
            double eta = qp[1];
            double weight = qp[2];

            // 2. Map Unit Triangle (0,0)-(1,0)-(0,1) to your Triangle (p1, p2, p3)
            // x = x1 + xi*(x2-x1) + eta*(x3-x1)
            double x = t.p1().x + xi * (t.p2().x - t.p1().x) + eta * (t.p3().x - t.p1().x);
            double y = t.p1().y + xi * (t.p2().y - t.p1().y) + eta * (t.p3().y - t.p1().y);

            // 3. Evaluate the functions
            // RWG evalDouble (you provided this earlier in the RWG class)
            defined.unset();
            double fv = g.evalDouble(x, y,defined);
            if (!defined.get()) continue;
            defined.unset();
            double gv = f.evalDouble(x, y, defined);
            if (!defined.get()) continue;
            integralSum += weight * fv * gv;
        }
        return integralSum * jacobian;
    }
}
