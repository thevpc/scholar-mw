package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadruwaves.ModeIndex;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.*;
import net.vpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.vpc.scholar.hadruwaves.mom.sources.planar.ExprPlanarSource;
import net.vpc.scholar.hadruwaves.mom.sources.planar.DefaultPlanarSources;

/**
 * Created by vpc on 1/25/14.
 */
public class SourceFactory extends AbstractFactory{

    public static PlanarSources createPlanarSources(PlanarSource... sources) {
        return new DefaultPlanarSources(sources);
    }

    public static ModalSources createModalSources(ModeIndex... userDefined) {
        return new UserModalSources(userDefined);
    }

    public static ModalSources createIndexModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new IndexModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createCutOffModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new CutOffModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createCutOffModalSourcesSingleMode(int defaultSourceCount, int... sourceCountPerDimension) {
        return new CutOffModalSourcesSingleMode(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createCutOffModalSourcesTE2n0(int defaultSourceCount, int... sourceCountPerDimension) {
        return new CutOffModalSourcesTE2n0(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createCutOffModalSourcesTM02n(int defaultSourceCount, int... sourceCountPerDimension) {
        return new CutOffModalSourcesTM02n(defaultSourceCount, sourceCountPerDimension);
    }

    public static PlanarSources createPlanarSources() {
        return new DefaultPlanarSources();
    }


    public static PlanarSources createPlanarSources(Expr fct, Complex characteristicImpedance) {
        return createPlanarSources().add(createPlanarSource(fct, characteristicImpedance));
    }

    public static PlanarSources createPlanarSources(double value, Complex characteristicImpedance, Axis polarization, Geometry geometry) {
        return createPlanarSources().add(createPlanarSource((polarization==null|| polarization==Axis.X)?value:0, (polarization==null|| polarization==Axis.Y)?value:0, characteristicImpedance, polarization, geometry));
    }

    public static PlanarSources createPlanarSources(double xvalue, double yvalue, Complex characteristicImpedance, Axis polarization, Geometry geometry) {
        return createPlanarSources().add(createPlanarSource(xvalue, yvalue, characteristicImpedance, polarization, geometry));
    }

    public static PlanarSource createPlanarSource(double xvalue, double yvalue, Complex characteristicImpedance, Axis polarization, Geometry geometry) {
        return new CstPlanarSource(xvalue, yvalue, characteristicImpedance, polarization, GeometryFactory.createPolygonList(geometry));
    }

    public static PlanarSources createPlanarSources(double value, Complex characteristicImpedance, Axis polarization, Domain geometry) {
        return createPlanarSources(value,characteristicImpedance,polarization,geometry.toGeometry());
    }

    public static PlanarSources createPlanarSources(double xvalue, double yvalue, Complex characteristicImpedance, Axis polarization, Domain geometry) {
        return createPlanarSources(xvalue,yvalue,characteristicImpedance,polarization,geometry.toGeometry());
    }

    public static PlanarSource createPlanarSource(double value, Complex characteristicImpedance, Axis polarization, Domain geometry) {
        return createPlanarSource(value,characteristicImpedance,polarization,geometry.toGeometry());
    }

    public static PlanarSource createPlanarSource(double value, Complex characteristicImpedance, Axis polarization, Geometry geometry) {
        return new CstPlanarSource((polarization==null|| polarization==Axis.X)?value:0, (polarization==null|| polarization==Axis.Y)?value:0, characteristicImpedance, polarization, GeometryFactory.createPolygonList(geometry));
    }

    public static PlanarSource createPlanarSource(Expr fct, Complex characteristicImpedance) {
        return createPlanarSource(fct,characteristicImpedance,null);
    }

    public static PlanarSource createPlanarSource(Expr fct, Complex characteristicImpedance,Geometry geometry) {
        return new ExprPlanarSource(fct, characteristicImpedance,geometry);
    }


    public static ModalSources createModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new IndexModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createInitialIndexModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new InitialIndexModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createGroupedCutOffModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new GroupedCutOffModalSources(defaultSourceCount, sourceCountPerDimension);
    }

}
