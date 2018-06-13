package net.vpc.scholar.hadrumaths.plot.mesh;


import net.vpc.common.strings.StringUtils;
import net.vpc.scholar.hadrumaths.MinMax;
import net.vpc.scholar.hadrumaths.plot.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Mesh3DPlot extends JPanel implements PlotComponentPanel {

    DefaultMesh3DModel model3d;
    Mesh3DComponent canv;
    //    JButton buttonHome;
//    JCheckBox checkboxPersp;
//    JCheckBox checkboxNodes;
//    JCheckBox checkboxTransp;
//    JCheckBox checkboxDrawLines;
//    JCheckBox checkboxDrawSurfaces;
    JLabel titleLabel;
    MeshAction buttonInAction;


    ;
    Mesh3DMatrix orot;
    Mesh3DMatrix otrans;
    Mesh3DMatrix

            oscale;
    double ominScale;
    double omaxScale;
    Mesh3DObject feObject;
    PlotModelProvider plotModelProvider;

    //    public Mesh3DPlot(String title, double[] x, double[] y, double[][] z) {
//        this(title, x, y, z, null, null);
//    }
//
//    public Mesh3DPlot(ValuesPlotModel model) {
//        this(model, null);
//    }
//
    public Mesh3DPlot(PlotModelProvider modelProvider, JColorPalette colorPalette) {
        this((ValuesPlotModel) modelProvider.getModel(), colorPalette);
    }

    public Mesh3DPlot(ValuesPlotModel model, JColorPalette colorPalette) {
        this(new ValuesPlotXYDoubleModelFace(model, null), colorPalette, null);
    }

    public Mesh3DPlot(ValuesPlotXYDoubleModelFace model, JColorPalette colorPalette, PlotModelProvider plotModelProvider) {

        if (colorPalette == null) {
            colorPalette = HSBColorPalette.DEFAULT_PALETTE;
        }

        double[] x = model.getX();
        double[] y = model.getY();
        double[][] z = model.getZ();
        Mesh3DObject p1 = new Mesh3DObject(null, model.getX().length * model.getY().length, (model.getX().length - 1) * (model.getY().length - 1));

        titleLabel = new JLabel(StringUtils.trim(model.getTitle()), SwingConstants.CENTER);

        MinMax minMax = new MinMax();
        MinMax absVertZMinMax = new MinMax();
        MinMax vertXYMinMax = new MinMax();
        absVertZMinMax.registerAbsValues(z);
        vertXYMinMax.registerValues(model.getX());
        vertXYMinMax.registerValues(y);
        double zFactor = 1;
        if (!vertXYMinMax.isNaN()) {
            zFactor *= (vertXYMinMax.getMax() - vertXYMinMax.getMin()) / 2;
        }
        if (!absVertZMinMax.isNaN() && absVertZMinMax.getMax() > 0) {
            zFactor /= absVertZMinMax.getMax();
        }

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < x.length; j++) {
                p1.addVertex(new Mesh3DVector(x[j], y[i], z[i][j] * zFactor));//0
            }
        }

        Mesh3DFace f;

        for (int i = 0; i < y.length - 1; i++) {
            for (int j = 0; j < x.length - 1; j++) {
                f = p1.face[p1.addFace(4, Color.BLUE)];
                f.addVertex(p1.vert[(i + 0) * x.length + (j + 0)]);
                f.addVertex(p1.vert[(i + 0) * x.length + (j + 1)]);
                f.addVertex(p1.vert[(i + 1) * x.length + (j + 1)]);
                f.addVertex(p1.vert[(i + 1) * x.length + (j + 0)]);
                minMax.registerValue(f.getRealCenter().z);

            }
        }

        for (int i = 0; i < p1.face.length; i++) {
            Mesh3DFace face3d = p1.face[i];
            float r = minMax.getRatio(face3d.getRealCenter().z);
//            System.out.println(r + " : " + face3d.getRealCenter().z + " : " + minMax.getMin() + " : " + minMax.getMax());
            face3d.setColor(colorPalette.getColor(r));
            //face3d.setColor(new Color(Color.HSBtoRGB(H * (r), S, B)));
        }

        this.feObject = p1;
        init();
    }

    //    public Mesh3DPlot() {
//        Mesh3DObject p1 = new Mesh3DObject(null, 8, 3);
//        p1.addVertex(new Vector3d(0, 0, 0));//0
//        p1.addVertex(new Vector3d(1, 0, 0));//1
//        p1.addVertex(new Vector3d(1, 1, 0));//2
//        p1.addVertex(new Vector3d(0, 1, 0));//3
//        p1.addVertex(new Vector3d(0, 0, 1));//4
//        p1.addVertex(new Vector3d(1, 0, 1));//5
//        p1.addVertex(new Vector3d(1, 1, 1));//6
//        p1.addVertex(new Vector3d(0, 1, 1));//7
//        Mesh3DFace f;
//        //bas
//        f = p1.face[p1.addFace(4, Color.RED)];
//        f.addVertex(p1.vert[0]);
//        f.addVertex(p1.vert[1]);
//        f.addVertex(p1.vert[2]);
//        f.addVertex(p1.vert[3]);
//        //haut
//        f = p1.face[p1.addFace(4, Color.YELLOW)];
//        f.addVertex(p1.vert[4]);
//        f.addVertex(p1.vert[5]);
//        f.addVertex(p1.vert[6]);
//        f.addVertex(p1.vert[7]);
//
//        //face
//        f = p1.face[p1.addFace(4, Color.BLUE)];
//        f.addVertex(p1.vert[0]);
//        f.addVertex(p1.vert[1]);
//        f.addVertex(p1.vert[5]);
//        f.addVertex(p1.vert[4]);
////        f=cantor_peigne.face[cantor_peigne.addFace(4,Color.GREEN)]  ;f.addVertex(cantor_peigne.vert[1]);f.addVertex(cantor_peigne.vert[2]);f.addVertex(cantor_peigne.vert[5]);f.addVertex(cantor_peigne.vert[6]);
////        f=cantor_peigne.face[cantor_peigne.addFace(4,Color.MAGENTA)];f.addVertex(cantor_peigne.vert[2]);f.addVertex(cantor_peigne.vert[3]);f.addVertex(cantor_peigne.vert[6]);f.addVertex(cantor_peigne.vert[7]);
////        f=cantor_peigne.face[cantor_peigne.addFace(4,Color.CYAN)]   ;f.addVertex(cantor_peigne.vert[3]);f.addVertex(cantor_peigne.vert[0]);f.addVertex(cantor_peigne.vert[7]);f.addVertex(cantor_peigne.vert[4]);
//        this.feObject = p1;
//        dsteps();
//    }
    public Mesh3DPlot(Mesh3DObject p1) {
        this.feObject = p1;
        init();
    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return canv.getComponentPopupMenu();
    }

    private void init() {

        model3d = new Mesh3DModel(getSize().width, getSize().height, feObject);

        if (model3d != null) {
            setLayout(new BorderLayout());
            setBackground(Color.white);
            canv = new CanvasBrowser3d(this);
            add(titleLabel, BorderLayout.NORTH);
            add(canv, BorderLayout.CENTER);
            JPopupMenu componentPopupMenu = canv.getComponentPopupMenu();
            if (componentPopupMenu == null) {
                componentPopupMenu = new JPopupMenu();
                canv.setComponentPopupMenu(componentPopupMenu);
            } else {
                componentPopupMenu.addSeparator();
            }
            JCheckBoxMenuItem i = new JCheckBoxMenuItem(new PerspAction());
            i.setSelected(model3d.persp);
            componentPopupMenu.add(i);

            i = new JCheckBoxMenuItem(new VisibleNodesAction());
            i.setSelected(model3d.visibleNodes);
            componentPopupMenu.add(i);

            i = new JCheckBoxMenuItem(new TransparentAction());
            i.setSelected(model3d.transparent);
            componentPopupMenu.add(i);

            i = new JCheckBoxMenuItem(new DrawLinesAction());
            i.setSelected(model3d.drawLines);
            componentPopupMenu.add(i);

            i = new JCheckBoxMenuItem(new DrawSurfacesAction());
            i.setSelected(model3d.drawSurfaces);
            componentPopupMenu.add(i);

            componentPopupMenu.addSeparator();

            componentPopupMenu.add(new JMenuItem(new ResetAction()));

            validate();
            orot = new Mesh3DMatrix(((Mesh3DTransformable) (model3d)).rot);
            otrans = new Mesh3DMatrix(((Mesh3DTransformable) (model3d)).trans);
            oscale = new Mesh3DMatrix(((Mesh3DTransformable) (model3d)).scale);
            ominScale = model3d.minScale;
            omaxScale = model3d.maxScale;
        }
    }

    public JPopupMenu getComponentPopupMenu() {
        return canv.getComponentPopupMenu();
    }

    public void setComponentPopupMenu(JPopupMenu popup) {
        canv.setComponentPopupMenu(popup);
    }

    private void doRevalidate() {
        canv.invalidate();
        canv.repaint();
        invalidate();
        repaint();
        Container parent = getParent();
        if (parent != null) {
            parent.invalidate();
            parent.repaint();
        }
    }

    public enum MeshAction {

        ROTATION, PAN, ZOOM
    }

    private class PerspAction extends AbstractPlotAction {

        public PerspAction() {
            super("Perspectives");
        }

        public void actionPerformed(ActionEvent e) {
            AbstractButton b = (AbstractButton) e.getSource();
            model3d.setPersp(b.isSelected());
            doRevalidate();
        }
    }

    private class VisibleNodesAction extends AbstractPlotAction {

        public VisibleNodesAction() {
            super("Noeuds Visibles");
        }

        public void actionPerformed(ActionEvent e) {
            AbstractButton b = (AbstractButton) e.getSource();
            model3d.setVisibleNodes(b.isSelected());
            doRevalidate();
        }
    }

    private class TransparentAction extends AbstractPlotAction {

        public TransparentAction() {
            super("Transparent");
        }

        public void actionPerformed(ActionEvent e) {
            AbstractButton b = (AbstractButton) e.getSource();
            model3d.setTransparent(b.isSelected());
            doRevalidate();
        }
    }

    private class DrawLinesAction extends AbstractPlotAction {

        public DrawLinesAction() {
            super("Lignes");
        }

        public void actionPerformed(ActionEvent e) {
            AbstractButton b = (AbstractButton) e.getSource();
            model3d.setDrawLines(b.isSelected());
            doRevalidate();
        }
    }

    private class DrawSurfacesAction extends AbstractPlotAction {

        public DrawSurfacesAction() {
            super("Surfaces");
        }

        public void actionPerformed(ActionEvent e) {
            AbstractButton b = (AbstractButton) e.getSource();
            model3d.setDrawSurfaces(b.isSelected());
            doRevalidate();
        }
    }

    private class ResetAction extends AbstractPlotAction {

        public ResetAction() {
            super("Reinitialize");
        }

        public void actionPerformed(ActionEvent e) {
            ((Mesh3DTransformable) (model3d)).rot.setValues(orot);
            ((Mesh3DTransformable) (model3d)).trans.setValues(otrans);
            if (model3d.persp) {
                model3d.minScale = ominScale;
                model3d.maxScale = omaxScale;
                model3d.computeMatrix();
            } else {
                ((Mesh3DTransformable) (model3d)).scale.setValues(oscale);
            }
            doRevalidate();
        }
    }
}
