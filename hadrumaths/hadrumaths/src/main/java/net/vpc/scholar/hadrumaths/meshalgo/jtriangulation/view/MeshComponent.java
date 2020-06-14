//package net.vpc.scholar.math.meshalgo.jtriangulation.view;
//
//import net.vpc.scholar.math.meshalgo.jtriangulation.algo.Triangle;
//import net.vpc.scholar.math.meshalgo.ZonePlusMailler;
//import net.vpc.scholar.math.meshalgo.triconsdes.MeshConsDesAlgo;
//import net.vpc.scholar.math.meshalgo.triconsdes.OptionsConsDes;
//import net.vpc.scholar.math.meshalgo.triconsdes.ConsDesAirPrecision;
//import net.vpc.scholar.math.meshalgo.triconsdes.ConsDesNbrePrecision;
//import net.vpc.scholar.math.meshalgo.triflip.FlipAirPrecision;
//import net.vpc.scholar.math.meshalgo.triflip.MeshFlipAlgo;
//import net.vpc.scholar.math.meshalgo.triflip.OptionFlip;
//import net.vpc.scholar.math.meshalgo.triflip.FlipCotePrecision;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.util.ArrayList;
//
///**
// * Created by IntelliJ IDEA.
// * User: EZZET
// * Date: 24 mars 2007
// * Time: 10:50:44
// * To change this template use File | Settings | File Templates.
// */
//public class MeshComponent extends JComponent{
//    private MutableMeshComponentModel meshComponentModel;
//    ArrayList<Point> points=new ArrayList<Point>();
//    int choix,typeoption;
//    double nconsdes,nflip,mconsdes,mflip;
//    public MeshComponent() {
//        meshComponentModel=new MutableMeshComponentModel();
//        choix=0;
//       // etat=1;
//        meshComponentModel.addPropetyChangeListener(new PropertyChangeListener() {
//            public void propertyChange(PropertyChangeEvent evt) {
//                repaint();
//            }
//        });
//       addMouseListener(new MouseListener() {
//            public void mouseClicked(MouseEvent e) {
//                points.add(e.getPoint());
//                if(choix==0){
//                meshComponentModel.addPointBase(e.getPoint());
//                }
//                else{
//                meshComponentModel.addPointPricise(e.getPoint());
//                }
//               // repaint();
//                //System.out.println("mouseClicked e = " + e);
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            public void mousePressed(MouseEvent e) {
//               // System.out.println("mousePressed e = " + e);
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            public void mouseReleased(MouseEvent e) {
//              // System.out.println("mouseReleased e = " + e);
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            public void mouseEntered(MouseEvent e) {
//                //System.out.println("mouseEntered e = " + e);
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            public void mouseExited(MouseEvent e) {
//               // System.out.println("mouseExited e = " + e);
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//        setPreferredSize(new Dimension(820,600));
//    }
//
//
//    public MeshComponentModel getMeshComponentModel() {
//        return meshComponentModel;
//    }
//
//    public void setMeshComponentModel(MutableMeshComponentModel meshComponentModel) {
//        this.meshComponentModel = meshComponentModel;
//        repaint();
//    }
//    public void delete(){
//        this.meshComponentModel.deleteMesh();
//   while(!points.isEmpty()){
//       points.removeProperty(0);
//   }
//        repaint();
//    }
//
//    public void setNflip(double nflip) {
//        this.nflip = nflip;
//    }
//
//    public void setNconsdes(double nconsdes) {
//        this.nconsdes = nconsdes;
//    }
//
//    public void setMflip(double mflip) {
//        this.mflip = mflip;
//    }
//
//    public void setMconsdes(double mconsdes) {
//        this.mconsdes = mconsdes;
//    }
//
//    public void setTypeoption(int typeoption) {
//        this.typeoption = typeoption;
//    }
//
//    public void insertZonePlusMailles(){
//        meshComponentModel.insertPrecisePolygons(new Polygon());
//        choix=1;
//    }
//    public void insertZoneBase(){
//        meshComponentModel.insertBasePolygons(new Polygon());
//        choix=0;
//    }
//
//
//    public void triangulter(){
//        int valid=0;
//        System.out.println(nconsdes+"*******"+mconsdes);
//        OptionsConsDes op=new OptionsConsDes();
//        MeshConsDesAlgo m = new MeshConsDesAlgo();
//        System.out.println("introduiser les zones les plus maill�es:");
//           Polygon[] P=(Polygon[]) meshComponentModel.getPrecisePolygons().toArray(new Polygon[ meshComponentModel.getPrecisePolygons().size()]);
//        if(typeoption==0){
//         op.setPrecision(new ConsDesAirPrecision(nconsdes));
//        }else{
//        op.setPrecision(new ConsDesNbrePrecision((long)nconsdes));
//        }
//        op.setZone(new ZonePlusMailler(P,mconsdes));
//        for(int i=0;i<meshComponentModel.getBasePolygons().get(0).xpoints.length;i++){
//            if((meshComponentModel.getBasePolygons().get(0).xpoints[i]!=0)||(meshComponentModel.getBasePolygons().get(0).ypoints[i]!=0)){
//                valid=valid+1;
//            }
//
//        }
//        if(valid>2){
//        m.setOption(op);
//            System.out.println("ezzet");
//          Polygon[] poly = (Polygon[]) meshComponentModel.getBasePolygons().toArray(new Polygon[ meshComponentModel.getBasePolygons().size()]);
//           Triangle[] triangles1 = m.mesh(poly);
//            System.out.println("le nombre de triangle est"+triangles1.length);
//           meshComponentModel.setMesh(triangles1);
//        }
//    }
//    public void triangulter1(){
//        int valid=0;
//        System.out.println(nflip+"*******"+mflip);
//        OptionFlip op=new OptionFlip();
//        MeshFlipAlgo m = new MeshFlipAlgo();
//        System.out.println("introduiser les zones les plus maill�es:");
//           Polygon[] P=(Polygon[]) meshComponentModel.getPrecisePolygons().toArray(new Polygon[ meshComponentModel.getPrecisePolygons().size()]);
//        op.setZone(new ZonePlusMailler(P,mflip));
//        //op.setPrecision(new FlipCotePrecision(nflip));
//       if(typeoption==0){
//        op.setPrecision(new FlipAirPrecision(nflip));
//       }else{
//          op.setPrecision(new FlipCotePrecision(nflip));
//       }
//         for(int i=0;i<meshComponentModel.getBasePolygons().get(0).xpoints.length;i++){
//            if((meshComponentModel.getBasePolygons().get(0).xpoints[i]!=0)||(meshComponentModel.getBasePolygons().get(0).ypoints[i]!=0)){
//                valid=valid+1;
//            }
//
//        }
//        if(valid>2){
//        m.setOption(op);
//            System.out.println("ezzet");
//          Polygon[] poly = (Polygon[]) meshComponentModel.getBasePolygons().toArray(new Polygon[ meshComponentModel.getBasePolygons().size()]);
//           Triangle[] triangles2 = m.mesh(poly);
//            System.out.println("ezzetezzet");
//           meshComponentModel.setMesh(triangles2);
//        }
//    }
//
//    public void paint(Graphics g) {
//        super.paint(g);
//          g.setColor(Color.orange);
//        for (Point p : points) {
//            g.fillOval(p.x,p.y,2,3);
//        }
//        g.setColor(Color.RED);
//        for(int i=0;i<meshComponentModel.getPrecisePolygons().size();i++){
//        g.drawPolygon((Polygon)meshComponentModel.getPrecisePolygons().get(i));
//        }
//        g.setColor(Color.BLACK);
//         for(int i=0;i<meshComponentModel.getBasePolygons().size();i++){
//             g.drawPolygon((Polygon)meshComponentModel.getBasePolygons().get(i));//To change body of overridden methods use File | Settings | File Templates.
//        }
//               if( meshComponentModel.getMesh()!=null){
//           g.setColor(Color.blue);
//        for (int i = 0; i < meshComponentModel.getMesh().length; i++) {
//            Triangle v =  meshComponentModel.getMesh()[i];
//            g.drawPolygon(
//                    new int[]{(int) v.p1.x, (int) v.p2.x, (int) v.p3.x},
//                    new int[]{(int) v.p1.y, (int) v.p2.y, (int) v.p3.y},
//                    3
//            );
//       }
//       }
//
//    }
//
//}
