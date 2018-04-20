package net.vpc.scholar.hadrumaths.integration;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

public interface DIntegralXY {
    double integrateX(DoubleToDouble f, double xmin, double xmax);

    double integrateX(DoubleToDouble f, double y, double xmin, double xmax);

    double integrateY(DoubleToDouble f, double x, double ymin, double ymax);

    double integrateXY(DoubleToDouble f, double xmin, double xmax, double ymin, double ymax);

    double integrateXYZ(DoubleToDouble f, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax);
}
