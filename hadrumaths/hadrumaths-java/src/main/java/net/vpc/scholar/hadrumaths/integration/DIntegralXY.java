package net.vpc.scholar.hadrumaths.integration;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

public interface DIntegralXY {
    public double integrateX(DoubleToDouble f, double xmin, double xmax);

    public double integrateX(DoubleToDouble f, double y, double xmin, double xmax);

    public double integrateY(DoubleToDouble f, double x, double ymin, double ymax);

    public double integrateXY(DoubleToDouble f, double xmin, double xmax, double ymin, double ymax);

    public double integrateXYZ(DoubleToDouble f, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax);
}
