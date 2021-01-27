//package net.thevpc.scholar.math.meshalgo.jtriangulation.view;
//
//import net.thevpc.scholar.math.meshalgo.jtriangulation.algo.Triangle;
//
//import java.awt.*;
//import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeSupport;
//import java.util.ArrayList;
//
///**
// * Created by IntelliJ IDEA.
// * User: EZZET
// * Date: 24 mars 2007
// * Time: 11:13:31
// * To change this template use File | Settings | File Templates.
// */
//public class MutableMeshComponentModel extends DefaultMeshComponentModel {
//    PropertyChangeSupport support;
//    int u;
//    int v;
//
//    public MutableMeshComponentModel() {
//        this.support = new PropertyChangeSupport(this);
//        basePolygons = new ArrayList();
//        basePolygons.add(new Polygon(new int[]{0,0,0,0},new int[]{0,0,0,0},4));
//        precisePolygons = new ArrayList();
//        precisePolygons.add(new Polygon(new int[]{0,0,0,0},new int[]{0,0,0,0},4));
//        indexBasePolygon = 0;
//        indexPrecisePolygon = 0;
//        u = 0;
//        v = 0;
//    }
//
//    public void deleteMesh(){
//        mesh=null;
//        while(!basePolygons.isEmpty()){
//                basePolygons.removeProperty(0);
//        }
//        while(!precisePolygons.isEmpty()){
//                precisePolygons.removeProperty(0);
//        }
//        if(!basePolygons.isEmpty()||!precisePolygons.isEmpty()){
//        }
//
//        basePolygons.add(new Polygon(new int[]{0,0,0,0},new int[]{0,0,0,0},4));
//        precisePolygons.add(new Polygon(new int[]{0,0,0,0},new int[]{0,0,0,0},4));
//        indexBasePolygon = 0;
//        indexPrecisePolygon = 0;
//        u = 0;
//        v = 0;
//
//    }
//
//    public void addPointBase(Point point) {
//
//        if (u < 3) {
//            basePolygons.get(indexBasePolygon).npoints = u + 1;
//            basePolygons.get(indexBasePolygon).xpoints[u] = (int) point.getX();
//            basePolygons.get(indexBasePolygon).ypoints[u] = (int) point.getY();
//            u++;
//        } else {
//            basePolygons.get(indexBasePolygon).addPoint((int) point.getX(), (int) point.getY());
//        }
//
//        support.firePropertyChange("addPoint", null, point);
//    }
//
//    public void addPointPricise(Point point) {
//        if (v < 3) {
//            precisePolygons.get(indexPrecisePolygon).npoints = v + 1;
//            precisePolygons.get(indexPrecisePolygon).xpoints[v] = (int) point.getX();
//            precisePolygons.get(indexPrecisePolygon).ypoints[v] = (int) point.getY();
//            v++;
//        } else {
//            precisePolygons.get(indexPrecisePolygon).addPoint((int) point.getX(), (int) point.getY());
//        }
//
//        support.firePropertyChange("addPoint", null, point);
//    }
//
//    public void addPropetyChangeListener(PropertyChangeListener listener) {
//        support.addPropertyChangeListener(listener);
//    }
//
//    public void addPropetyChangeListener(String property, PropertyChangeListener listener) {
//        support.addPropertyChangeListener(property, listener);
//    }
//
//    public void setBasePolygons(ArrayList<Polygon> basePolygons) {
//        ArrayList<Polygon> oldPolygons = this.basePolygons;
//        this.basePolygons = basePolygons;
//        support.firePropertyChange("newPolygon", oldPolygons, basePolygons);
//    }
//
//    public void insertPrecisePolygons(Polygon precisePolygon) {
//        if (precisePolygons.get(0).xpoints[0] != 0) {
//            this.precisePolygons.add(precisePolygon);
//            indexPrecisePolygon = indexPrecisePolygon + 1;
//            support.firePropertyChange("newPolygon", null, precisePolygon);
//        }
//    }
//
//    public void insertBasePolygons(Polygon basePolygon) {
//        if (basePolygons.get(0).xpoints[0] != 0) {
//            this.basePolygons.add(basePolygon);
//            indexBasePolygon = indexBasePolygon + 1;
//            support.firePropertyChange("newPolygon", null, basePolygon);
//        }
//    }
//
//    public void setMesh(Triangle[] mesh) {
//        Triangle[] oldMesh = this.mesh;
//        this.mesh = mesh;
//        support.firePropertyChange("newMesh", oldMesh, mesh);
//    }
//}
