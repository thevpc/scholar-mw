/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.solver.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.geom.DefaultGeometryList;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgoType;
import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadruwaves.mom.solver.AbstractMomSolverTestTemplate;
import net.vpc.scholar.hadruwaves.mom.solver.HWSolverMoM;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpModes;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpPolyedron;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpRWG;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpRooftop;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPatternType;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectPolygon;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

/**
 *
 * @author vpc
 */
public class MomSolverTestTemplateMesh extends AbstractMomSolverTestTemplate {

    public static final MeshAlgo MESH_RECT = new MeshAlgoRect();

    /**
     * functions pattern
     */
    private WritablePExpression<GpPatternType> pattern = Props2.of("pattern").exprEnumOf(GpPatternType.class, null);

    /**
     * functions count ~ complexity.
     */
    private WritablePExpression<Integer> complexity = Props2.of("complexity").exprIntOf(1);

    private WritablePExpression<MeshAlgoType> mesh = Props2.of("mesh").exprEnumOf(MeshAlgoType.class, null);
    private WritablePExpression<Integer> meshMinPrecision = Props2.of("meshMinPrecision").exprIntOf(1);
    private WritablePExpression<Integer> meshMaxPrecision = Props2.of("meshMaxPrecision").exprIntOf(1);

    private WritablePExpression<TestFunctionsSymmetry> symmetry = Props2.of("symmetry").exprEnumOf(TestFunctionsSymmetry.class, null);
    private WritablePExpression<Axis> invariance = Props2.of("invariance").exprEnumOf(Axis.class, null);

    public WritablePExpression<GpPatternType> pattern() {
        return pattern;
    }

    public WritablePExpression<Integer> complexity() {
        return complexity;
    }

    public WritablePExpression<MeshAlgoType> mesh() {
        return mesh;
    }

    public WritablePExpression<Integer> meshMinPrecision() {
        return meshMinPrecision;
    }

    public WritablePExpression<Integer> meshMaxPrecision() {
        return meshMaxPrecision;
    }

    public WritablePExpression<TestFunctionsSymmetry> symmetry() {
        return symmetry;
    }

    public WritablePExpression<Axis> invariance() {
        return invariance;
    }

    private Polygon[] toPolygonEff(HWConfigurationRun configuration) {
        List<Polygon> all = new ArrayList<>();
        for (HWProjectPolygon e : polygons()) {
            all.add(e.eval(configuration));
        }
        return all.toArray(new Polygon[0]);
    }

    @Override
    public Expr[] generate(HWSolverMoM solver) {
        HWConfigurationRun configuration = solver.configuration();
        GpPatternType pt = pattern.eval(configuration);
        if (pt == null) {
            pt = GpPatternType.RWG;
        }
        GeometryList gl = new DefaultGeometryList((Geometry[]) toPolygonEff(configuration));
        switch (pt) {
            case MODES: {
                GpModes m = new GpModes(gl, symmetry.eval(configuration), complexity.eval(configuration));
                m.setInvariance(invariance.eval(configuration));
                return m.arr();
            }
            case RECT: {
                //TODO fix me, shoud support rect!!
                GpModes m = new GpModes(gl, symmetry.eval(configuration), complexity.eval(configuration));
                m.setInvariance(invariance.eval(configuration));
                return m.arr();
            }
            case ROOFTOP: {
                GpRooftop m = new GpRooftop(gl, symmetry.eval(configuration), new GridPrecision(meshMinPrecision.eval(configuration), meshMaxPrecision.eval(configuration)));
                m.setInvariance(invariance.eval(configuration));
                return m.arr();
            }
            case RWG: {
                GpRWG m = new GpRWG(gl, symmetry.eval(configuration), complexity.eval(configuration));
                m.setInvariance(invariance.eval(configuration));
                return m.arr();
            }
            case POLYEDRON: {
                GpPolyedron m = new GpPolyedron(gl, symmetry.eval(configuration), complexity.eval(configuration));
                m.setInvariance(invariance.eval(configuration));
                return m.arr();
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    private Expr setParams(Expr e, Map<String, Double> paramValues) {
        for (Map.Entry<String, Double> entry : paramValues.entrySet()) {
            e = e.setParam(entry.getKey(), entry.getValue());
        }
        return e;
    }

}
