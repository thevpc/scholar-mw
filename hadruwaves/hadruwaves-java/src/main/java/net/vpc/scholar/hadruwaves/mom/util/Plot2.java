package net.vpc.scholar.hadruwaves.mom.util;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.PolygonPlot;

import javax.swing.*;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 16 juin 2007 00:36:44
 */
public class Plot2 {
    public static void show(String title, MomStructure str){
        JFrame jFrame = new JFrame(title);
        GpAdaptiveMesh functions = (GpAdaptiveMesh) str.testFunctions();
        jFrame.add(new PolygonPlot(functions.getPolygons()[0],functions.getMeshAlgo(),functions.getPattern(),str.getDomain()));
        jFrame.setVisible(true);
    }
    public static void show(String title, GeometryList geometryList, MeshAlgo algo, GpPattern pattern, Domain domain){
        JFrame jFrame = new JFrame(title);
        jFrame.add(new PolygonPlot(geometryList,algo==null?MeshAlgoRect.RECT_ALGO_LOW_RESOLUTION:algo,pattern,domain));
        jFrame.setVisible(true);
    }

}
