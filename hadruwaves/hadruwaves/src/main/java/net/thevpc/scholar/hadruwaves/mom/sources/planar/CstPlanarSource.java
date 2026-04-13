/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.sources.planar;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.DefaultHGeometryList;
import net.thevpc.scholar.hadrumaths.geom.DefaultHPolygon;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.geom.HGeometryList;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.MeshTriangulationAlgo;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * @author vpc
 */
public class CstPlanarSource implements PlanarSource, Cloneable {

    private HGeometryList geometryList;
    private final Complex characteristicImpedance;
    private final double xvalue;
    private final double yvalue;
    private final Axis polarization;

    public static CstPlanarSource ofVoltage(double targetVoltage, Domain domain, Axis axis, Complex characteristicImpedance) {
        double gap = (axis == Axis.X) ? domain.xwidth() : domain.ywidth();

        double fieldStrength = targetVoltage / gap;

        return new CstPlanarSource(
                (axis == Axis.X) ? fieldStrength : 0,
                (axis == Axis.Y) ? fieldStrength : 0,
                characteristicImpedance,
                axis,
                new DefaultHGeometryList(domain, new DefaultHPolygon(domain))
        );
    }

    public CstPlanarSource(double xvalue, double yvalue, Complex characteristicImpedance, Axis polarization, HGeometryList geometryList) {
        this.geometryList = geometryList;
        this.characteristicImpedance = characteristicImpedance;
        this.xvalue = xvalue;
        this.yvalue = yvalue;
        this.polarization = polarization;
    }

    @Override
    public HGeometry getGeometry() {
        return geometryList;
    }

    public Complex getCharacteristicImpedance() {
        return characteristicImpedance;
    }

    public DoubleToVector getFunction() {
        ArrayList<DoubleToComplex> allx = new ArrayList<DoubleToComplex>();
        ArrayList<DoubleToComplex> ally = new ArrayList<DoubleToComplex>();
        Collection<MeshZone> allZonesInit = new ArrayList<MeshZone>();
        for (HGeometry polygon : geometryList) {
            MeshAlgo m = null;
            if (polygon.isRectangular()) {
                m = MeshAlgoRect.RECT_ALGO_LOW_RESOLUTION;
            } else {
                m = new MeshTriangulationAlgo(30);
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
                    xf = (polarization == null || polarization.equals(Axis.X)) ? (Maths.expr(xvalue, zone.getDomain())).toDC() : Maths.CZEROXY;
                    yf = (polarization == null || polarization.equals(Axis.Y)) ? (Maths.expr(yvalue, zone.getDomain())).toDC() : Maths.CZEROXY;
                    break;
                }
                default: {
                    xf = (polarization == null || polarization.equals(Axis.X)) ? expr(xvalue, zone.getPolygon()).toDC() : Maths.CZEROXY;
                    yf = (polarization == null || polarization.equals(Axis.Y)) ? expr(yvalue, zone.getPolygon()).toDC() : Maths.CZEROXY;
                }
            }
            if (!xf.isZero()) {
                allx.add(xf);
            }
            if (!yf.isZero()) {
                ally.add(yf);
            }
        }
        DoubleToComplex fx = allx.isEmpty() ? Maths.CZEROXY : allx.size() == 1 ? allx.get(0) : (Maths.sum(allx.toArray(new Expr[0])).toDC());
        DoubleToComplex fy = ally.isEmpty() ? Maths.CZEROXY : ally.size() == 1 ? ally.get(0) : (Maths.sum(ally.toArray(new Expr[0])).toDC());
        return Maths.vector(fx, fy).toDV();
    }

    public Axis getPolarization() {
        return polarization;
    }

    public HGeometryList getGeometryList() {
        return geometryList;
    }

    public double getXvalue() {
        return xvalue;
    }

    public double getYvalue() {
        return yvalue;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = NElement.ofObjectBuilder(getClass().getSimpleName());
        h.add("xvalue", NElementHelper.elem(xvalue));
        h.add("yvalue", NElementHelper.elem(yvalue));
        h.add("polarization", NElementHelper.elem(polarization));
        h.add("impedance", NElementHelper.elem(characteristicImpedance));
        h.add("geometries", NElementHelper.elem(geometryList));
        return h.build();
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
        if (!Objects.equals(geometryList, that.geometryList)) return false;
        if (!Objects.equals(characteristicImpedance, that.characteristicImpedance))
            return false;
        return polarization == that.polarization;
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = geometryList != null ? geometryList.hashCode() : 0;
        result = 31 * result + (characteristicImpedance != null ? characteristicImpedance.hashCode() : 0);
        result = 31 * result + Double.hashCode(xvalue);
        result = 31 * result + Double.hashCode(yvalue);
        result = 31 * result + (polarization != null ? polarization.hashCode() : 0);
        return result;
    }
}
