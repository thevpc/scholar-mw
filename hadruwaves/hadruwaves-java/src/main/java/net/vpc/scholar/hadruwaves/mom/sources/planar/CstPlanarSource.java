/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.sources.planar;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.dump.Dumper;
import net.vpc.scholar.hadrumaths.dump.Dumpable;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * @author vpc
 */
public class CstPlanarSource implements PlanarSource, Dumpable, Cloneable {

    private GeometryList geometryList;
    private Complex characteristicImpedance;
    private double xvalue;
    private double yvalue;
    private Axis polarization;

    public CstPlanarSource(double xvalue, double yvalue, Complex characteristicImpedance, Axis polarization, GeometryList geometryList) {
        this.geometryList = geometryList;
        this.characteristicImpedance = characteristicImpedance;
        this.xvalue = xvalue;
        this.yvalue = yvalue;
        this.polarization = polarization;
    }


    public Complex getCharacteristicImpedance() {
        return characteristicImpedance;
    }

    public DoubleToVector getFunction() {
        ArrayList<DoubleToComplex> allx = new ArrayList<DoubleToComplex>();
        ArrayList<DoubleToComplex> ally = new ArrayList<DoubleToComplex>();
        Collection<MeshZone> allZonesInit = new ArrayList<MeshZone>();
        for (Geometry polygon : geometryList) {
            MeshAlgo m = null;
            if (polygon.isRectangular()) {
                m = MeshAlgoRect.RECT_ALGO_LOW_RESOLUTION;
            } else {
                m = new MeshConsDesAlgo(30);
            }
            allZonesInit.addAll(m.meshPolygon(polygon));
        }

        Domain globalBounds = geometryList.getDomain();
        ArrayList<MeshZone> allZones = new ArrayList<MeshZone>();
        for (MeshZone zone : allZonesInit) {
            zone.setDomainRelative(globalBounds, globalBounds);
            allZones.add(zone);
        }

        DoubleToComplex xf = null;
        DoubleToComplex yf = null;
        for (MeshZone zone : allZonesInit) {
            switch (zone.getShape()) {
                case RECTANGLE: {
                    xf = (polarization == null || polarization.equals(Axis.X)) ? (DoubleValue.valueOf(xvalue, zone.getDomain())).toDC() : FunctionFactory.CZEROXY;
                    yf = (polarization == null || polarization.equals(Axis.Y)) ? (DoubleValue.valueOf(yvalue, zone.getDomain())).toDC() : FunctionFactory.CZEROXY;
                    break;
                }
                default: {
                    xf = (polarization == null || polarization.equals(Axis.X)) ? expr(xvalue, zone.getPolygon()).toDC() : FunctionFactory.CZEROXY;
                    yf = (polarization == null || polarization.equals(Axis.Y)) ? expr(yvalue, zone.getPolygon()).toDC() : FunctionFactory.CZEROXY;
                }
            }
            if (!xf.isZero()) {
                allx.add(xf);
            }
            if (!yf.isZero()) {
                ally.add(yf);
            }
        }
        DoubleToComplex fx = allx.isEmpty()?FunctionFactory.CZEROXY: allx.size()==1? allx.get(0): (Maths.sum(allx.toArray(new Expr[allx.size()])).toDC());
        DoubleToComplex fy = ally.isEmpty()?FunctionFactory.CZEROXY: ally.size()==1? ally.get(0): (Maths.sum(ally.toArray(new Expr[ally.size()])).toDC());
        return Maths.vector(fx, fy);
    }

    public Axis getPolarization() {
        return polarization;
    }

    public GeometryList getGeometryList() {
        return geometryList;
    }

    public double getXvalue() {
        return xvalue;
    }

    public double getYvalue() {
        return yvalue;
    }

    public Dumper getDumpHelper() {
        return new Dumper(this)
                .add("xvalue", xvalue)
                .add("yvalue", yvalue)
                .add("polarization", polarization)
                .add("characteristicImpedance", characteristicImpedance)
                .add("geometryList", geometryList);
    }

    public String dump() {
        return getDumpHelper().toString();
    }

    @Override
    public PlanarSource clone() {
        try {
            CstPlanarSource d = (CstPlanarSource) super.clone();
            d.geometryList = d.geometryList.clone();
            return d;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CstPlanarSource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CstPlanarSource that = (CstPlanarSource) o;

        if (Double.compare(that.xvalue, xvalue) != 0) return false;
        if (Double.compare(that.yvalue, yvalue) != 0) return false;
        if (geometryList != null ? !geometryList.equals(that.geometryList) : that.geometryList != null) return false;
        if (characteristicImpedance != null ? !characteristicImpedance.equals(that.characteristicImpedance) : that.characteristicImpedance != null)
            return false;
        return polarization == that.polarization;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = geometryList != null ? geometryList.hashCode() : 0;
        result = 31 * result + (characteristicImpedance != null ? characteristicImpedance.hashCode() : 0);
        temp = Double.doubleToLongBits(xvalue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yvalue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (polarization != null ? polarization.hashCode() : 0);
        return result;
    }
}
