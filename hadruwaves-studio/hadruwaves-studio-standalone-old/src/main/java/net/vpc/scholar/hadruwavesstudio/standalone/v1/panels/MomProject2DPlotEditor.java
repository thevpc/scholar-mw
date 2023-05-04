package net.thevpc.scholar.hadruwavesstudio.standalone.v1.panels;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import javax.swing.SwingUtilities;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.DataTypeEditorFactory;
import net.vpc.lib.pheromone.application.swing.EditComponentsPanel;
import net.vpc.lib.pheromone.application.swing.JOptionPane2;

import net.vpc.lib.pheromone.ariana.util.Resources;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.AreaRenderer;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.DefaultAreaRenderer;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.GridConfigEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials.AreaMaterialEditor;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.util.config.Configuration;
import net.thevpc.scholar.hadruwaves.mom.ProjectType;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.ModalSourceMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.PecMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.PlanarSourceMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.SurfaceImpedanceMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.common.Area;
import net.thevpc.scholar.hadruwaves.mom.project.common.AreaGroup;
import net.thevpc.scholar.hadruwaves.mom.project.common.AreaZone;
import net.vpc.upa.types.ConstraintsException;

public class MomProject2DPlotEditor extends JPanel {

    Structure2DPlotPanel structure2DPlotPanel;
    StatusBar statusBar;
    JToggleButton drawPickupButton = new JToggleButton("Pointer");
    JToggleButton drawSrcButton = new JToggleButton("Source");
    JToggleButton drawMetalButton = new JToggleButton("Metal");
    JToggleButton drawZsButton = new JToggleButton("Zs");
    JButton zommInButton = new JButton("Zoom +");
    JButton zommOutButton = new JButton("Zoom -");
    JButton zoomNone = new JButton("No Zoom");
    JButton groupButton = new JButton("Grouper");
    JButton ungroupButton = new JButton("Degrouper");
    ButtonGroup editTypeButtonGroup = new ButtonGroup();
    private Color componentFocusedColor = Color.RED;
    private Color componentBorderColor = Color.BLACK;
    private MomProject structure;
    private Point oldPosition;
    private Collection<AreaZone> motionAreas = new HashSet<AreaZone>();
    private Collection<AreaZone> selectedZones = new HashSet<AreaZone>();
    private Collection<Area> cached_selectedAreas = null;
    private Area[] showTitleAreas = null;
    private Point showTitleAreasPos = null;
    private RectInfo[] oldRealPos;
    private RectInfo selectionArea;
    private AreaRenderer areaRenderer;
    private static BasicStroke STROKE_DASHED_1 = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{6}, 0);
    private static BasicStroke STROKE_DASHED_2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{12}, 0);

    private static class RectInfo {

        public RectInfo(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
        double x;
        double y;
        double w;
        double h;
    }
    private int resizeType = Cursor.DEFAULT_CURSOR;
    private MomProjectEditor structureEditor;
    private Structure2DPlotPopupMenu schemaMenu;
    private GridConfigEditor gridConfigEditor;
    private boolean blink = false;
    private boolean startMotion = false;
    private double zoomFactor = 1;
    private int preferredInitialDimesion = 400;
    private int precisionPoint = 2;

    public static enum EditType {

        PICKUP, DRAW_SRC, DRAW_METAL, DRAW_ZS
    }
    private EditType editType = EditType.PICKUP;

    public MomProject2DPlotEditor(MomProjectEditor structureEditor) {
        super(new BorderLayout());
        areaRenderer = new DefaultAreaRenderer(structureEditor);
        schemaMenu = new Structure2DPlotPopupMenu(this);
        gridConfigEditor = new GridConfigEditor(structureEditor);
        this.structureEditor = structureEditor;
        structure = structureEditor.getProject0();
        add(createPlotArea(), BorderLayout.CENTER);
        add(createToolbar(), BorderLayout.NORTH);
        add(createStatusbar(), BorderLayout.SOUTH);
        setZoomFactor(1);
    }

    protected JComponent createToolbar() {

        JToolBar jToolBar = new JToolBar(JToolBar.HORIZONTAL);
        Font f = new JButton().getFont();
        f = f.deriveFont(Font.PLAIN, f.getSize() * 0.7f);

        drawPickupButton.setFont(f);
        drawSrcButton.setFont(f);
        drawMetalButton.setFont(f);
        drawZsButton.setFont(f);
        zommInButton.setFont(f);
        zommOutButton.setFont(f);
        zoomNone.setFont(f);
        groupButton.setFont(f);
        ungroupButton.setFont(f);

        editTypeButtonGroup.add(drawPickupButton);
        editTypeButtonGroup.add(drawSrcButton);
        editTypeButtonGroup.add(drawMetalButton);
        editTypeButtonGroup.add(drawZsButton);

        jToolBar.add(drawPickupButton);
        jToolBar.add(drawSrcButton);
        jToolBar.add(drawMetalButton);
        jToolBar.add(drawZsButton);
        jToolBar.addSeparator();
        jToolBar.add(zommInButton);
        jToolBar.add(zommOutButton);
        jToolBar.add(zoomNone);
        jToolBar.add(groupButton);
        jToolBar.add(ungroupButton);

        drawPickupButton.setSelected(getEditType() == EditType.PICKUP);
        drawPickupButton.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                setEditType(EditType.PICKUP);
            }
        });
        drawSrcButton.setSelected(getEditType() == EditType.DRAW_SRC);
        drawSrcButton.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                setEditType(EditType.DRAW_SRC);
            }
        });
        drawMetalButton.setSelected(getEditType() == EditType.DRAW_METAL);
        drawMetalButton.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                setEditType(EditType.DRAW_METAL);
            }
        });
        drawZsButton.setSelected(getEditType() == EditType.DRAW_ZS);
        drawZsButton.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                setEditType(EditType.DRAW_ZS);
            }
        });
        zommInButton.setIcon(Resources.loadImageIcon("/images/net/vpc/report/ZoomIn.gif"));
        zommOutButton.setIcon(Resources.loadImageIcon("/images/net/vpc/report/ZoomOut.gif"));
        zoomNone.setIcon(Resources.loadImageIcon("/images/net/vpc/swing/FunctionNone.gif"));
        drawPickupButton.setIcon(Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
        drawSrcButton.setIcon(Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Source.gif"));
        drawMetalButton.setIcon(Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Metal.gif"));
        drawZsButton.setIcon(Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Zs.gif"));
        groupButton.setIcon(Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Group.gif"));
        ungroupButton.setIcon(Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Ungroup.gif"));

        zommInButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }
        });
        zommOutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
        });
        zoomNone.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                noZoom();
            }
        });
        groupButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedAreasDoGroup();
            }
        });
        ungroupButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedAreasDoUngroup();
            }
        });
        return jToolBar;
    }

    protected JComponent createPlotArea() {
        structure2DPlotPanel = new Structure2DPlotPanel();
        Box b = Box.createHorizontalBox();
        b.add(structure2DPlotPanel);
        JScrollPane jScrollPane = new JScrollPane(b);
        jScrollPane.setPreferredSize(new Dimension(400, 400));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        return jScrollPane;
    }

    protected JComponent createStatusbar() {
        this.statusBar = new StatusBar();
        structure2DPlotPanel.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                processMouseDragged_statusbar(e);
            }

            public void mouseMoved(MouseEvent e) {
                processMouseMoved_statusbar(e);
            }
        });
        structure2DPlotPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setXPosition(Double.NaN);
                statusBar.setYPosition(Double.NaN);
            }
        });
        return statusBar;
    }

    public MomProject getStructure() {
        return structure;
    }

    public double getPinnedX(double realX) {
        double gx = gridConfigEditor.getGridXValue();
        Domain dd = structure.getDomain();
        double dw = (float) dd.getXwidth();
        if (gx == 0 || dw == 0) {
            return realX;
        }
        return Math.round((realX - dd.getXMin()) / gx) * gx + dd.getXMin();
    }

    public double getPinnedY(double realY) {
        double gy = gridConfigEditor.getGridYValue();
        Domain dd = structure.getDomain();
        double dh = (float) dd.getYwidth();
        if (gy == 0 || dh == 0) {
            return realY;
        }
        return Math.round((realY - dd.getYMin()) / gy) * gy + dd.getYMin();
    }

    public void configure() {
        JOptionPane.showMessageDialog(this, gridConfigEditor, "Parametres de la grille", JOptionPane.PLAIN_MESSAGE);
    }

    public void zoomIn() {
        setZoomFactor(getZoomFactor() + 0.5);
    }

    public void zoomOut() {
        setZoomFactor(getZoomFactor() - 0.5);
    }

    public void noZoom() {
        setZoomFactor(1);
    }

    public void ensurePin(Area a) {
        Domain dd = a.getDomain();
        a.setX(getPinnedX(dd.getXMin()));
        a.setY(getPinnedY(dd.getYMin()));
        a.setWidth(getPinnedX(dd.getXMin() + dd.getXwidth()) - dd.getXMin());
        if (dd.getXwidth() <= 0) {
            a.setWidth(gridConfigEditor.getGridXValue());
        }
        a.setHeight(getPinnedY(dd.getYMin() + dd.getYwidth()) - dd.getYMin());
        if (dd.getYwidth() <= 0) {
            a.setHeight(gridConfigEditor.getGridYValue());
        }
    }

    public void selectedAreasEnsurePin() {
        for (Iterator iterator = getAllSelectedAreas().iterator(); iterator.hasNext();) {
            Area area = (Area) iterator.next();
            ensurePin(area);
        }
    }

    public double[] real4relativeXfactors() {
        Dimension size = structure2DPlotPanel.getSize();
        int xsize = size.width - 8;
        int ysize = size.height - 8;
        Domain dd = structure.getDomain();

        double dw = dd.getXwidth();
        double dh = dd.getYwidth();
        return new double[]{dw / xsize, dh / ysize};
    }

    public double[] relative2Real(Point relativePoint) {
        Point p = new Point(relativePoint.x - 4, relativePoint.y - 4);
        double[] f = real4relativeXfactors();
        Domain dd = structure.getDomain();
        return new double[]{
            dd.getXMin() + p.x * f[0],
            dd.getYMin() + p.y * f[1]
        };
    }

    public double[] relative2RealUnit(Point relativePoint) {
        double[] r = relative2Real(relativePoint);
        double u = structure.getDimensionUnit();
        r[0] = (r[0] / u);
        r[1] = (r[1] / u);
        return r;
    }

    public void showSelectDialog() {
        JPanel box = new JPanel(new BorderLayout());

        Area[] sources = structure.getSources(Boolean.TRUE).toArray(new Area[structure.getSources(Boolean.TRUE).size()]);
        JCheckBox[] sourcesCheckBoxes = new JCheckBox[sources.length];
        Box boxS = Box.createVerticalBox();
        for (int i = 0; i < sourcesCheckBoxes.length; i++) {
            sourcesCheckBoxes[i] = new JCheckBox(sources[i].getName());
            if (getAllSelectedAreas().contains(sources[i])) {
                sourcesCheckBoxes[i].setSelected(true);
            }
            boxS.add(sourcesCheckBoxes[i]);
        }
        boxS.setBorder(BorderFactory.createTitledBorder("Sources"));

        Area[] metals = structure.getMetals(Boolean.TRUE).toArray(new Area[structure.getMetals(Boolean.TRUE).size()]);
        JCheckBox[] metalsCheckBoxes = new JCheckBox[metals.length];
        Box boxM = Box.createVerticalBox();
        for (int i = 0; i < metalsCheckBoxes.length; i++) {
            metalsCheckBoxes[i] = new JCheckBox(metals[i].getName());
            if (getAllSelectedAreas().contains(metals[i])) {
                metalsCheckBoxes[i].setSelected(true);
            }
            boxM.add(metalsCheckBoxes[i]);
        }
        boxM.setBorder(BorderFactory.createTitledBorder(structureEditor.getResources().get("metals")));

        Area[] zs = structure.getImpedanceSurfaces(Boolean.TRUE).toArray(new Area[structure.getImpedanceSurfaces(Boolean.TRUE).size()]);
        JCheckBox[] zsCheckBoxes = new JCheckBox[zs.length];
        Box zsBox = Box.createVerticalBox();
        for (int i = 0; i < zsCheckBoxes.length; i++) {
            zsCheckBoxes[i] = new JCheckBox(zs[i].getName());
            if (getAllSelectedAreas().contains(zs[i])) {
                zsCheckBoxes[i].setSelected(true);
            }
            zsBox.add(metalsCheckBoxes[i]);
        }
        zsBox.setBorder(BorderFactory.createTitledBorder(structureEditor.getResources().get("surfaceImpedances")));

        box.add(boxS, BorderLayout.NORTH);
        box.add(boxM, BorderLayout.CENTER);
        box.add(zsBox, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(box);
        scrollPane.setPreferredSize(new Dimension(150, 300));
        int ret = JOptionPane.showConfirmDialog(this, scrollPane, "Selectionner ...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ret == JOptionPane.OK_OPTION) {
            clearSelection();
            for (int i = 0; i < sourcesCheckBoxes.length; i++) {
                if (sourcesCheckBoxes[i].isSelected()) {
                    selectZone(sources[i]);
                    structureEditor.setSelectedArea(sources[i]);
                }
            }
            for (int i = 0; i < metalsCheckBoxes.length; i++) {
                if (metalsCheckBoxes[i].isSelected()) {
                    selectZone(metals[i]);
                    structureEditor.setSelectedArea(metals[i]);
                }
            }
            for (int i = 0; i < zsCheckBoxes.length; i++) {
                if (zsCheckBoxes[i].isSelected()) {
                    selectZone(zs[i]);
                    structureEditor.setSelectedArea(zs[i]);
                }
            }
        }
        repaint();
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor2) {
        Dimension old = structure2DPlotPanel.getSize();
        zoomFactor = zoomFactor2;

        Domain dd = structure.getDomain();
        double structureFactor = (dd.getXwidth() > 0 && dd.getYwidth() > 0) ? (dd.getYwidth() / dd.getXwidth())
                : 1;
        Dimension newSize = (preferredInitialDimesion <= 1) ? new Dimension((int) (zoomFactor * preferredInitialDimesion),
                (int) (zoomFactor * preferredInitialDimesion * structureFactor))
                : new Dimension((int) (zoomFactor * preferredInitialDimesion / structureFactor),
                        (int) (zoomFactor * preferredInitialDimesion));

        structure2DPlotPanel.setSize(newSize);
        structure2DPlotPanel.setPreferredSize(newSize);
        structure2DPlotPanel.setMaximumSize(newSize);
        //structure2DPlotPanel.firePropertyChange("size", old, getSize());
        structure2DPlotPanel.repaint();
    }

    public void deleteArea(Area area) {
        deselectZone(area);
        structureEditor.removeArea(area);
    }

    public void editArea(Area area) {
        structureEditor.showEdit(area);
    }

    public void splitMetal(Area area) {
        double gx = gridConfigEditor.getGridXValue();
        double gy = gridConfigEditor.getGridYValue();
        if (gx == 0 || gy == 0) {
            return;
        }
        ensurePin(area);
        deleteArea(area);

        Domain dd = area.getDomain();
        int nx = (int) Math.round(dd.getXwidth() / gx);

        int ny = (int) Math.round(dd.getYwidth() / gy);

//        Area[] all=new Area[nx*ny];
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
//                int p = i * nx + ny;
                Area area2 = (Area) area.clone();
                area2.setName(area.getName() + "(" + (i + 1) + "," + (j + 1) + ")");
                area2.setWidth(gx);
                area2.setHeight(gy);
                area2.setX(dd.getXMin() + i * gx);
                area2.setY(dd.getYMin() + j * gy);
                addMetal(area2);
            }
        }
    }

    public void splitMetal(Area area, int nx, int ny) {
//        if(gx==0 || gy==0){
//            return;
//        }
//        ensurePin(area);
        deleteArea(area);
        Domain dd = area.getDomain();
        double gx = dd.getXwidth() / nx;
        double gy = dd.getYwidth() / ny;
//
//        int nx=(int)Math.round(area.width/gx);
//
//        int ny=(int)Math.round(area.height/gy);

//        Area[] all=new Area[nx*ny];
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
//                int p = i * nx + ny;
                Area area2 = (Area) area.clone();
                addMetal(area2);
                area2.setName(area.getName() + "(" + (i + 1) + "," + (j + 1) + ")");
                area2.setWidth(gx);
                area2.setHeight(gy);
                area2.setX(dd.getXMin() + i * gx);
                area2.setY(dd.getYMin() + j * gy);
            }
        }
    }

    public void addSource() {
        if (ProjectType.PLANAR_STRUCTURE.equals(structureEditor.getProjectType())) {
            structureEditor.showAddArea(new PlanarSourceMaterial());
        } else {
            structureEditor.showAddArea(new ModalSourceMaterial());
        }
    }

    public void addMetal() {
        structureEditor.showAddArea(new PecMaterial());
    }

    public void addImpedanceSurface() {
        structureEditor.showAddArea(new SurfaceImpedanceMaterial());
    }

    public Area addMetalAt(Point point) {
        return addAreaAt(new PecMaterial(), point);
    }

    public Area addSourceAt(Point point) {
        return addAreaAt(new PlanarSourceMaterial(), point);
    }

    public Area addImpedanceSurfaceAt(Point point) {
        return addAreaAt(new SurfaceImpedanceMaterial(), point);
    }

    public Area addAreaAt(AreaMaterial material, Point point) {
        double[] r = relative2Real(point);
        Area a = new Area();
        a.setEnabled(true);
        a.setMaterial(material);
        a.setName(material.getName());
        structureEditor.addArea(a);
        a.setX(r[0]);
        a.setY(r[1]);
        double unit = 0;//Maths.min(Maths.min(structure.width / 8, structure.height / 8), structure.getDimensionUnit());
        a.setWidth(unit);
        a.setHeight(unit);
        setEditType(EditType.PICKUP);
        clearSelection();
        selectZone(a);
        motionAreas.clear();
        motionAreas.add(a);
        resizeType = getResizeType(a);
        setCursor(Cursor.getPredefinedCursor(resizeType));
        return a;
    }

    public int getResizeType(AreaZone s) {
        int rtype = Cursor.DEFAULT_CURSOR;
        double[] d = real4relativeXfactors();
        Domain dd = structure.getDomain();
        Domain sd = s.getDomain();
        double Edist = Math.abs(showTitleAreasPos.x - 4 - (sd.getXMin() - dd.getXMin() + sd.getXwidth()) / d[0]);
        double Wdist = Math.abs(showTitleAreasPos.x - 4 - (sd.getXMin() - dd.getXMin()) / d[0]);
        double Sdist = Math.abs(showTitleAreasPos.y - 4 - (sd.getYMin() - dd.getYMin() + sd.getYwidth()) / d[1]);
        double Ndist = Math.abs(showTitleAreasPos.y - 4 - (sd.getYMin() - dd.getYMin()) / d[1]);
        boolean E = Math.abs(showTitleAreasPos.x - 4 - (sd.getXMin() - dd.getXMin() + sd.getXwidth()) / d[0]) <= precisionPoint;
        boolean W = Math.abs(showTitleAreasPos.x - 4 - (sd.getXMin() - dd.getXMin()) / d[0]) <= precisionPoint;
        boolean S = Math.abs(showTitleAreasPos.y - 4 - (sd.getYMin() - dd.getYMin() + sd.getYwidth()) / d[1]) <= precisionPoint;
        boolean N = Math.abs(showTitleAreasPos.y - 4 - (sd.getYMin() - dd.getYMin()) / d[1]) <= precisionPoint;
//                    StringBuffer debug=new StringBuffer();
//
//                    debug.append(Math.abs(showTitleAreasPos.x - 4 - (s.getX() - structure.getDielectric().x + s.getWidth()) / d[0]));
//                    debug.append(";");
//                    debug.append(Math.abs(showTitleAreasPos.x - 4 - (s.getX() - structure.getDielectric().x) / d[0]));
//                    debug.append(";");
//                    debug.append(Math.abs(showTitleAreasPos.y - 4 - (s.getY() - structure.getDielectric().y + s.getHeight()) / d[1]));
//                    debug.append(";");
//                    debug.append(Math.abs(showTitleAreasPos.y - 4 - (s.getY() - structure.getDielectric().y) / d[1]));
//                    debug.append("=>");
//
//                    if(E){debug.append("E");}
//                    if(W){debug.append("W");}
//                    if(S){debug.append("S");}
//                    if(N){debug.append("N");}
//                    debug.append(".");
//                    System.out.println(debug);
        if (N & S) {
            if (Ndist < Sdist) {
                S = false;
            } else {
                N = false;
            }
        }
        if (E & W) {
            if (Wdist < Edist) {
                E = false;
            } else {
                W = false;
            }
        }
        if (S & E) {
            rtype = Cursor.SE_RESIZE_CURSOR;
        } else if (N & E) {
            rtype = Cursor.NE_RESIZE_CURSOR;
        } else if (S & W) {
            rtype = Cursor.SW_RESIZE_CURSOR;
        } else if (N & W) {
            rtype = Cursor.NW_RESIZE_CURSOR;
        } else if (S) {
            rtype = Cursor.S_RESIZE_CURSOR;
        } else if (N) {
            rtype = Cursor.N_RESIZE_CURSOR;
        } else if (E) {
            rtype = Cursor.E_RESIZE_CURSOR;
        } else if (W) {
            rtype = Cursor.W_RESIZE_CURSOR;
        }
        return rtype;
    }

    public void doEndMotion() {
        for (AreaZone areaZone : motionAreas) {
            areaZone.recompile();
        }

        motionAreas.clear();
        oldPosition = null;
        oldRealPos = null;
        repaint();
    }

    public void doEndSelection(boolean clearSelection) {
        double sx1 = selectionArea.x;
        double sy1 = selectionArea.y;
        double sx2 = selectionArea.x + selectionArea.w;
        double sy2 = selectionArea.y + selectionArea.h;
        if (clearSelection) {
            clearSelection();
        }
        for (Area area : getAllEnabledAreas()) {
            Domain dd = area.getDomain();
            double ax1 = dd.getXMin();
            double ay1 = dd.getYMin();
            double ax2 = dd.getXMin() + dd.getXwidth();
            double ay2 = dd.getYMin() + dd.getYwidth();
            if (ax1 >= sx1 && ax2 <= sx2 && ay1 >= sy1 && ay2 <= sy2) {
                selectZone(area);
            }
        }
        selectionArea = null;
        repaint();
    }

    public void doSelectAreaAt(Point point, int clickCount, boolean clearSelection) {
        Area[] a = getAllAreas(point);
        int c = clickCount;
        if (c == 0) {
            return;
        }
        if (c <= a.length) {
            c = c - 1;
        } else {
            c = -1;
        }
        if (!clearSelection) {
            if (c >= 0 && c < a.length) {
                if (isSelectedZone(a[c])) {
                    deselectZone(a[c]);
                } else {
                    selectZone(a[c]);
                }
            }
        } else {
            clearSelection();
            if (c != -1) {
                selectZone(a[c]);
            }
        }
        repaint();

    }

    public void showPopupMenu(Point p) {
        schemaMenu.configure(getAllAreas(p));
        schemaMenu.show(structure2DPlotPanel, p.x, p.y);
    }

    public void doRememberSelectedAreasForMoveAt(Point point) {
        if (motionAreas.size() > 0) {
            startMotion = true;
            oldPosition = new Point(point);
            oldRealPos = new RectInfo[motionAreas.size()];
            int i = 0;
            for (Iterator<AreaZone> iterator = motionAreas.iterator(); iterator.hasNext();) {
                AreaZone areaZone = iterator.next();
                Domain sd = areaZone.getDomain();
                oldRealPos[i++] = new RectInfo(sd.getXMin(), sd.getYMin(), sd.getXwidth(), sd.getYwidth());
            }
            repaint();
        } else {
            double[] r = relative2Real(point);
            selectionArea = new RectInfo(r[0], r[1], 0, 0);
            oldPosition = new Point(point);
            repaint();
        }
    }

    public void doSelectAreasForMoveAt(Point point) {
        if (motionAreas.size() == 0 && getAllSelectedAreas().size() > 0) {
            Area[] all = getAllAreas(point);
            boolean someSelected = false;
            for (int i = 0; i < all.length; i++) {
                if (getAllSelectedAreas().contains(all[i])) {
                    someSelected = true;
                    break;
                }
            }
            if (someSelected) {
                motionAreas.clear();
                motionAreas.addAll(getSelection());
            }
        }
    }

//    public void addImpedanceSurfaceAt(Point point) {
//        double[] r = relative2Real(point);
//        Area a = AreaManager.getCategoryDesc(AreaManager.ZS_CATEGORY)
//                .getDefaultAreaType().createInsance();
//        structureEditor.getZsList().addArea(a);
//        a.setX(r[0]);
//        a.setY(r[1]);
//        clearSelection();
//        selectZone(a);
//        motionAreas.clear();
//        motionAreas.add(a);
//        resizeType = Cursor.SE_RESIZE_CURSOR;
//        setCursor(Cursor.getPredefinedCursor(resizeType));
//    }
//
//    public void addSourceAt(Point point) {
//        double[] r = relative2Real(point);
//        Area a = AreaManager.getCategoryDesc(AreaManager.SOURCE_CATEGORY)
//                .getDefaultAreaType().createInsance();
//        structureEditor.getSourcesList().addArea(a);
//        a.setX(r[0]);
//        a.setY(r[1]);
//        clearSelection();
//        selectZone(a);
//        motionAreas.clear();
//        motionAreas.add(a);
//        resizeType = Cursor.SE_RESIZE_CURSOR;
//        setCursor(Cursor.getPredefinedCursor(resizeType));
//    }
    public void addMetal(Area area) {
        structureEditor.addArea(area);
    }

    public void selectedAreasDoDelete() {
        ArrayList<Area> toRemove = new ArrayList<Area>(getAllSelectedAreas());
        clearSelection();
        for (Iterator iterator = toRemove.iterator(); iterator.hasNext();) {
            Area area = (Area) iterator.next();
            deleteArea(area);
        }
        cached_selectedAreas = null;
    }

    public void selectedAll() {
        clearSelection();
        for (Area area : structure.findAreas(null, Boolean.TRUE)) {
            selectZone(area);
        }
    }

    public void selectedAreasDoOpenEditors() {

        Collection<Area> allSelectedAreas = getAllSelectedAreas();
        for (Area allSelectedArea : allSelectedAreas) {
            Area area = (Area) allSelectedArea;
            editArea(area);
            return;//edit only one
        }
    }

    public void selectedAreasDoSplit() {
        Area[] areas = (Area[]) getAllSelectedAreas().toArray(new Area[getAllSelectedAreas().size()]);
        for (int i = 0; i < areas.length; i++) {
            splitMetal(areas[i]);
        }
    }

    public void selectedAreasDoSplit(int nx, int ny) {
        Area[] areas = (Area[]) getAllSelectedAreas().toArray(new Area[getAllSelectedAreas().size()]);
        for (int i = 0; i < areas.length; i++) {
            splitMetal(areas[i], nx, ny);
        }
    }

    public void showSplitSelectedAreas() {
        DataTypeEditor x = DataTypeEditorFactory.forInt("Nbr cellules en X",new Integer(1), new Integer(10000), false);
        DataTypeEditor y = DataTypeEditorFactory.forInt("Nbr cellules en Y",new Integer(1), new Integer(10000), false);
        EditComponentsPanel editComponentsPanel = new EditComponentsPanel(new DataTypeEditor[]{x, y}, 1, false);
        int ret = JOptionPane.showConfirmDialog(this, editComponentsPanel, "Eclater ...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ret != JOptionPane.OK_OPTION) {
            return;
        }
        int nx = 0;
        int ny = 0;
        try {
            nx = ((Integer) x.getHelper().getObject()).intValue();
            ny = ((Integer) y.getHelper().getObject()).intValue();
        } catch (ConstraintsException e) {
            JOptionPane2.showErrorDialog(this, e);
            return;
        }
        selectedAreasDoSplit(nx, ny);
    }

    public EditType getEditType() {
        return editType;
    }

    public void setEditType(EditType editType) {
        EditType old = this.editType;
        this.editType = editType;
        if (!old.equals(editType)) {
            switch (editType) {
                case DRAW_METAL: {
                    if (!drawMetalButton.isSelected()) {
                        drawMetalButton.setSelected(true);
                    }
                    break;
                }
                case DRAW_SRC: {
                    if (!drawSrcButton.isSelected()) {
                        drawSrcButton.setSelected(true);
                    }
                    break;
                }
                case DRAW_ZS: {
                    if (!drawZsButton.isSelected()) {
                        drawZsButton.setSelected(true);
                    }
                    break;
                }
                case PICKUP: {
                    if (!drawPickupButton.isSelected()) {
                        drawPickupButton.setSelected(true);
                    }
                    break;
                }
            }
        }
        firePropertyChange("editType", old, editType);
    }

    public Collection<Area> getAllSelectedAreas() {
        if (cached_selectedAreas != null) {
            return cached_selectedAreas;
        }
        HashSet<Area> a = new HashSet<Area>();
        for (AreaZone area : selectedZones) {
            AreaGroup g = area.getTopLevelGroup();
            if (g != null) {
                a.addAll(Arrays.asList(g.getAllInnerAreas()));
            } else if (area instanceof AreaGroup) {
                a.addAll(Arrays.asList(((AreaGroup) area).getAllInnerAreas()));
            } else if (area instanceof Area) {
                a.add((Area) area);
            }
        }
        cached_selectedAreas = a;
        return cached_selectedAreas;
    }

    public void clearSelection() {
        selectedZones.clear();
        cached_selectedAreas = null;
    }

    public void selectZone(AreaZone z) {
        if (z instanceof Area) {
            structureEditor.setSelectedArea((Area) z);
            AreaGroup g = z.getTopLevelGroup();
            if (g != null) {
                z = g;
            }
            selectedZones.add(z);
            cached_selectedAreas = null;
        }
    }

    public void deselectZone(AreaZone z) {
        AreaGroup g = z.getTopLevelGroup();
        if (g != null) {
            z = g;
        }
        selectedZones.remove(z);
        cached_selectedAreas = null;
    }

    public Collection<AreaZone> getSelection() {
        return selectedZones;
    }

    public boolean isSelectedArea(Area a) {
        return getAllSelectedAreas().contains(a);
    }

    public boolean isSelectedZone(AreaZone z) {
        return selectedZones.contains(z);
    }

    public void loadConfiguration(Configuration conf, String key) {
        gridConfigEditor.loadConfiguration(conf, key + ".grid");
    }

    public void storeConfiguration(Configuration conf, String key) {
        gridConfigEditor.storeConfiguration(conf, key + ".grid");
    }

    private class StatusBarElement extends JLabel {

        public StatusBarElement(String text) {
            super(text);
            Dimension d = new Dimension(200, 20);
            setPreferredSize(d);
            setMaximumSize(d);
            setMinimumSize(d);
        }
    }

    private class StatusBar extends JPanel {

        StatusBarElement xPosition = new StatusBarElement("x = ?");
        StatusBarElement yPosition = new StatusBarElement("y = ?");

        public StatusBar() {
            super();
//
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//            setFloatable(false);
            setBorder(BorderFactory.createEtchedBorder());
            add(xPosition);
            addSeparator2();
            add(yPosition);
            add(Box.createHorizontalGlue());
            add(Box.createVerticalStrut(20));
        }

        private void addSeparator2() {
            JComponent c = null;
            c = (JComponent) Box.createRigidArea(new Dimension(6, 20));
            add(c);
            c = (JComponent) Box.createRigidArea(new Dimension(2, 20));
            c.setBorder(BorderFactory.createEtchedBorder());
            add(c);
            c = (JComponent) Box.createRigidArea(new Dimension(6, 20));
            add(c);
        }

        public void setXPosition(double d) {
            MomProject structure = getStructure();
            if (structure == null) {
                d = Double.NaN;
            } else {
                d = (d / getStructure().getDimensionUnit());
            }
            if (!Double.isNaN(d)) {
                xPosition.setText("x = " + d);
            } else {
                xPosition.setText("");
            }
            xPosition.setToolTipText(xPosition.getText());
        }

        public void setYPosition(double d) {
            MomProject structure = getStructure();
            if (structure == null) {
                d = Double.NaN;
            } else {
                d = (d / getStructure().getDimensionUnit());
            }
            if (!Double.isNaN(d)) {
                yPosition.setText("y = " + d);
            } else {
                yPosition.setText("");
            }
            yPosition.setToolTipText(yPosition.getText());
        }
    }

    public void selectedAreasMoveRight() {
        if (getAllSelectedAreas().size() > 0) {
            double[] b = relative2Real(new Point(1, 1));
            for (Area area : getAllSelectedAreas()) {
                Domain dd = area.getDomain();
                area.setX(dd.getXMin()- 1 * b[0]);
            }
            repaint();
        }
    }

    public void selectedAreasMoveLeft() {
        if (getAllSelectedAreas().size() > 0) {
            double[] b = relative2Real(new Point(1, 1));
            for (Area area : getAllSelectedAreas()) {
                Domain dd = area.getDomain();
                area.setX(dd.getXMin() + 1 * b[0]);
            }
            repaint();
        }
    }

    public void selectedAreasMoveUp() {
        if (getAllSelectedAreas().size() > 0) {
            double[] b = relative2Real(new Point(1, 1));
            for (Area area : getAllSelectedAreas()) {
                Domain dd = area.getDomain();
                area.setY(dd.getYMin() + 1 * b[1]);
            }
            repaint();
        }
    }

    public void selectedAreasMoveDown() {
        if (getAllSelectedAreas().size() > 0) {
            double[] b = relative2Real(new Point(1, 1));
            for (Area area : getAllSelectedAreas()) {
                Domain dd = area.getDomain();
                area.setY(dd.getYMin() - 1 * b[1]);
            }
            repaint();
        }
    }

    public void selectedAreasDoGroupOrUngroup() {
        if (getAllSelectedAreas().size() > 1) {
            selectedAreasDoGroup();
        } else if (getAllSelectedAreas().size() == 1) {
            selectedAreasDoUngroup();
        }
    }

    public Area getArea(Point p) {
        p = new Point(p.x - 4, p.y - 4);
        Dimension size = structure2DPlotPanel.getSize();
        int xsize = size.width - 8;
        int ysize = size.height - 8;
        Domain sd = structure.getDomain();
        double dx = (float) sd.getXMin();
        double dy = (float) sd.getYMin();
        double dw = (float) sd.getXwidth();
        double dh = (float) sd.getYwidth();
        Collection<Area> all = getAllEnabledAreas();
        for (Area a : all) {
            Domain ad = a.getDomain();
            int xx = (int) ((ad.getXMin() - dx) / dw * xsize);
            int yy = (int) ((ad.getYMin() - dy) / dh * ysize);
            int wx = (int) (ad.getXwidth() / dw * xsize);
            int hy = (int) (ad.getYwidth() / dh * ysize);
            if ((p.x >= xx && p.x <= (xx + wx)) && (p.y >= yy && p.y <= (yy + hy))) {
                return a;
            }
        }
        return null;
    }

    public void selectedAreasDoUngroup() {
        if (getAllSelectedAreas().size() >= 1) {
            HashSet<AreaGroup> groupzz = new HashSet<AreaGroup>();
            for (AreaZone area : getSelection()) {
                AreaGroup g = area.getTopLevelGroup();
                if (g != null && !groupzz.contains(g)) {
                    groupzz.add(g);
                }
            }
            for (AreaGroup areaGroup : groupzz) {
                areaGroup.removeGroupButRetainChildren();
            }
            clearSelection();
//            structureEditor.setStructure(structure);
            structureEditor.updateView();
            repaint();
        }
    }

    public Collection<Area> getAllEnabledAreas() {
        return structure.findAreas(null, Boolean.TRUE);
    }

    public void selectedAreasDoGroup() {
        if (getAllSelectedAreas().size() > 1) {

            HashSet<Area> areazz = new HashSet<Area>();
            HashSet<AreaGroup> groupzz = new HashSet<AreaGroup>();
            for (AreaZone area : getSelection()) {
                AreaGroup g = area.getTopLevelGroup();
                if (g != null && !groupzz.contains(g)) {
                    groupzz.add(g);
                } else if (g == null && area instanceof AreaGroup) {
                    groupzz.add((AreaGroup) area);
                } else if (g == null && area instanceof Area && !areazz.contains((Area) area)) {
                    areazz.add((Area) area);
                }
            }
            AreaGroup ng = structure.getRootAreaGroup().addNewGroup();
            for (Area area : areazz) {
                area.getParentGroup().removeArea(area);
                ng.addArea(area);
            }
            for (AreaGroup areaGroup : groupzz) {
                areaGroup.getParentGroup().removeAreaGroup(areaGroup);
                ng.addAreaGroup(areaGroup);
            }
            clearSelection();
            selectZone(ng);
//            structureEditor.setStructure(structure);
            structureEditor.updateView();
            repaint();
        }
    }

    public void help() {
        JOptionPane.showMessageDialog(this,
                "" + "<HTML>" + "<Table>" + "<TR><TD>H</TD><TD>Show thos help</TD></TR>" + "<TR><TD>CTRL-A</TD><TD>Select ALL</TD></TR>" + "<TR><TD>P</TD><TD>Pointer Mode</TD></TR>" + "<TR><TD>M</TD><TD>Metal Mode</TD></TR>" + "<TR><TD>S</TD><TD>Source Mode</TD></TR>" + "<TR><TD>Z</TD><TD>Zs Mode</TD></TR>" + "<TR><TD>E</TD><TD>Edit Selected</TD></TR>" + "<TR><TD>DELETE</TD><TD>Delete Selected</TD></TR>" + "<TR><TD>G</TD><TD>Group/ungroup Selected</TD></TR>" + "<TR><TD>A</TD><TD>Align And Pin to Grid Selected</TD></TR>" + "<TR><TD>C</TD><TD>Configure Grid</TD></TR>" + "<TR><TD>ARROWS</TD><TD>Move Selected</TD></TR>" + "<TR><TD>CTRL LEFT CLICK</TD><TD>Select/Deselect</TD></TR>" + "<TR><TD>CTRL LEFT DRAG</TD><TD>Clone Selected</TD></TR>" + "</Table>" + "</HTML>",
                "Help", JOptionPane.PLAIN_MESSAGE);
    }

    public Area[] getAllAreas(Point p) {
        return getAllAreas(p, false);
    }

    public Area[] getAllAreas(Point p, boolean includeBox) {
        return getAllAreas(p, includeBox, precisionPoint);
    }

    public Area[] getAllAreas(Point p, boolean includeBox, int delta) {
        ArrayList<Area> allAreasVector = new ArrayList<Area>();
        p = new Point(p.x - 4, p.y - 4);
        Dimension size = structure2DPlotPanel.getSize();
        int xsize = size.width - 8;
        int ysize = size.height - 8;
        Domain sd = structure.getDomain();
        double dx = (float) sd.getXMin();
        double dy = (float) sd.getYMin();
        double dw = (float) sd.getXwidth();
        double dh = (float) sd.getYwidth();
        Collection<Area> all = getAllEnabledAreas();
        for (Area a : all) {
            Domain dd = a.getDomain();
            int xx = (int) ((dd.getXMin() - dx) / dw * xsize);
            int yy = (int) ((dd.getYMin() - dy) / dh * ysize);
            int wx = (int) (dd.getXwidth() / dw * xsize);
            int hy = (int) (dd.getYwidth() / dh * ysize);
            if ((p.x >= (xx - precisionPoint) && p.x <= (xx + wx + precisionPoint)) && (p.y >= (yy - precisionPoint) && p.y <= (yy + hy + precisionPoint))) {
                allAreasVector.add(a);
            }
        }
        return (Area[]) allAreasVector.toArray(new Area[allAreasVector.size()]);
    }

    public void processKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //System.out.println("e = " + e);
        switch (e.getKeyChar()) {
            case '+': {
                if (e.getModifiers() == 0) {
                    zoomIn();
                    return;
                }
            }
            case '-': {
                if (e.getModifiers() == 0) {
                    zoomOut();
                    return;
                }
            }
            case '=': {
                if (e.getModifiers() == 0) {
                    noZoom();
                    return;
                }
            }
        }
        switch (key) {
            case KeyEvent.VK_DELETE: {
                if (e.getModifiers() == 0) {
                    selectedAreasDoDelete();
                }
                break;
            }
            case KeyEvent.VK_S: {
                if (e.getModifiers() == 0) {
                    drawSrcButton.setSelected(true);
                }
                break;
            }
            case KeyEvent.VK_Z: {
                if (e.getModifiers() == 0) {
                    drawZsButton.setSelected(true);
                }
                break;
            }
            case KeyEvent.VK_M: {
                if (e.getModifiers() == 0) {
                    drawMetalButton.setSelected(true);
                }
                break;
            }
            case KeyEvent.VK_P: {
                if (e.getModifiers() == 0) {
                    drawPickupButton.setSelected(true);
                }
                break;
            }
            case KeyEvent.VK_A: {
                if (e.isControlDown()) {
                    selectedAll();
                } else if (e.getModifiers() == 0) {
                    selectedAreasEnsurePin();
                }
                break;
            }
            case KeyEvent.VK_O: {
                selectedAreasDoOpenEditors();
                break;
            }
            case KeyEvent.VK_E: {
                selectedAreasDoOpenEditors();
                break;
            }
            case KeyEvent.VK_RIGHT: {
                if (e.getModifiers() == 0) {
                    selectedAreasMoveRight();
                }
                break;
            }
            case KeyEvent.VK_LEFT: {
                if (e.getModifiers() == 0) {
                    selectedAreasMoveLeft();
                }
                break;
            }
            case KeyEvent.VK_UP: {
                if (e.getModifiers() == 0) {
                    selectedAreasMoveUp();
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                if (e.getModifiers() == 0) {
                    selectedAreasMoveDown();
                }
                break;
            }
            case KeyEvent.VK_G: {
                if (e.getModifiers() == 0) {
                    selectedAreasDoGroupOrUngroup();
                }
                break;
            }
            case KeyEvent.VK_C: {
                if (e.getModifiers() == 0) {
                    configure();
                }
                break;
            }
            case KeyEvent.VK_H: {
                if (e.getModifiers() == 0) {
                    help();
                }
                break;
            }
        }
    }

    private class Structure2DPlotPanel extends JComponent {
//    static BufferedImage bi = new BufferedImage(2,1,BufferedImage.TYPE_INT_RGB);
//    static{
//        bi.setRGB(0, 0, 0xff00ff00); bi.setRGB(1, 0, 0xffff0000);
//    }
//    static TexturePaint tp1 = new TexturePaint(bi,new Rectangle(0,0,2,1));
//
//    static{
//        bi = new BufferedImage(2,1,BufferedImage.TYPE_INT_RGB);
//        bi.setRGB(0, 0, 0xff0000ff); bi.setRGB(1, 0, 0xffff0000);
//    }
//    static TexturePaint tp2 = new TexturePaint(bi,new Rectangle(0,0,2,1));

        public Structure2DPlotPanel() {
//        Dimension ddd=new Dimension(600,600);
//        setPreferredSize(ddd);
//        setMinimumSize(ddd);
//        setMaximumSize(ddd);

//        super.addInputMethodListener();
//        super.getActionMap();
//        super.getInputMap();
//        super.disableEvents();
//        super.enableEvents();
            enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_FOCUS_EVENT_MASK);
            setRequestFocusEnabled(true);
            setFocusTraversalKeysEnabled(false);
            addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    processKeyPressed(e);
                }

                public void keyReleased(KeyEvent e) {
                }
            });
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    processMouseClicked_structure2DPlotPanel(e);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    processMousePressed_structure2DPlotPanel(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    processMouseReleased_structure2DPlotPanel(e);
                }
            });

            addMouseMotionListener(new MouseMotionListener() {

                public void mouseDragged(MouseEvent e) {
                    processMouseDragged_structure2DPlotPanel(e);
                }

                public void mouseMoved(MouseEvent e) {
                    processMouseMoved_structure2DPlotPanel(e);
                }
            });
            Timer timer = new Timer(500, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    blink = !blink;
                    repaint();
                }
            });
            timer.start();

        }

//    public Dimension getSize(){
//        Dielectric dielectric=structure==null?null:structure.getDielectric();
//        double min=Maths.min(dielectric.width,dielectric.height);
//        double max=Maths.min(dielectric.width,dielectric.height);
//        double ff=1;
//        if(max<100){
//            ff=100/max;
//        }
//        return dielectric==null?getPreferredSize():new Dimension(
//                (int) (dielectric.width*ff*zoomFactor),
//                (int) (dielectric.height*ff*zoomFactor)
//        );
//    }
        @Override
        public void paint(Graphics g) {
            try {
                Graphics2D g2d = (Graphics2D) g;
                Dimension size = getSize();
//        int xsize=size.width-8;
//        int ysize=size.height-8;
                g2d.setColor(componentBorderColor);
                g2d.fillRect(0, 0, size.width, size.height);
                if (hasFocus()) {
                    g2d.setColor(componentFocusedColor);
                    g2d.drawRect(0, 0, size.width, size.height);
                }
                g2d.translate(4, 4);
                if (size != null) {
                    paintBox(g2d);
//                g2d.setColor(Color.RED);
//                g2d.drawRect(0,0,xsize,ysize);
                    Collection<Area> all = getAllEnabledAreas();
                    for (Area a : all) {
                        areaRenderer.paintArea(a, structure, g2d, getSize(),
                                getAllSelectedAreas().contains(a) && blink);
                    }
                    if (showTitleAreasPos != null) {
                        for (int i = 0; i < showTitleAreas.length; i++) {
                            Color cc = showTitleAreas[i].getColor();
                            if (cc == null) {
                                AreaMaterialEditor ed = (AreaMaterialEditor) structureEditor.getUIFactory(showTitleAreas[i].getMaterial().getId());
                                cc = ed.getDefaultColor();
                            }
                            g2d.setColor(cc);
                            g2d.drawString(showTitleAreas[i].getName(),
                                    showTitleAreasPos.x + 6,
                                    showTitleAreasPos.y + (i * 10) + 6);
                        }
                    }
                    paintSelect(g2d);
                    return;
                }
                g2d.drawString("EMPTY", 15, 15);
                paintSelect(g2d);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.paint(g);
        }

//    private void paintArea(Area a, Graphics2D g2d) {
//        paintArea(a, g2d, 0.4f);
//    }
//
//    public void pinComponents(){
//        Dielectric d=structure.getDielectric();
//        Dimension size=getSize();
//        int xsize=size.width-8;
//        int ysize=size.height-8;
//        double gx=d.getGridX();
//        double gy=d.getGridY();
//        double[] f=real2relativeXfactors();
//        if(gx>0 && gy>0){
//            int x=(int) (gx/f[0]);
//            int y=(int) (gy/f[1]);
//            if(x>5 && y>5){
//                Area[] a= structure.getEnabledMetals();
//                for(int i=0;i<a.length;i++){
//
//                }
//            }
//        }
//    }
        public void paintArea(Domain domain, Color backColor, Graphics2D g2d, Dimension boxSize, boolean selected, float transparency) {
            if (structure == null) {
                return;
            }
            Dimension size = boxSize;
            int xsize = size.width - 8;
            int ysize = size.height - 8;
            Domain sd = structure.getDomain();
            double dx = sd.getXMin();
            double dy = sd.getYMin();
            double dw = sd.getXwidth();
            double dh = sd.getYwidth();
            if (dw < 0) {
                dx = dx + dw;
                dw = -dw;
            }
            if (dh < 0) {
                dy = dy + dh;
                dh = -dh;
            }
            double ax = domain.getXMin();
            double ay = domain.getYMin();
            double aw = domain.getXwidth();
            double ah = domain.getYwidth();
            if (aw < 0) {
                ax = ax + aw;
                aw = -aw;
            }
            if (ah < 0) {
                ay = ay + ah;
                ah = -ah;
            }
            g2d.setColor(backColor);
            //Stroke oldStroke = g2d.getStroke();

            //g2d.setStroke(selected ? STROKE_BOLD : STROKE_THIN);
            g2d.drawRect((int) ((ax - dx) / dw * xsize),
                    (int) ((ay - dy) / dh * ysize),
                    (int) (aw / dw * xsize),
                    (int) (ah / dh * ysize));
            if (selected) {
                g2d.drawRect((int) ((ax - dx) / dw * xsize) + 1,
                        (int) ((ay - dy) / dh * ysize) + 1,
                        (int) (aw / dw * xsize) - 2,
                        (int) (ah / dh * ysize) - 2);
            }
            //g2d.setStroke(oldStroke);
//        BufferedImage bi = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
//        bi.setRGB(0, 0, this.color.getRGB());
//        bi.setRGB(1, 0, 0xffff0000);
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
            g2d.fillRect((int) ((ax - dx) / dw * xsize),
                    (int) ((ay - dy) / dh * ysize),
                    (int) (aw / dw * xsize),
                    (int) (ah / dh * ysize));
            g2d.setComposite(oldComposite);
        }

        private void paintBox(Graphics2D g2d) {
            Dimension size = getSize();
            int xsize = size.width - 8;
            int ysize = size.height - 8;
            Domain sd = structure.getDomain();
            paintArea(sd, Color.WHITE, g2d, getSize(), false, 1f);
            GridConfigEditor gce = gridConfigEditor;
            double gx = -1;
            double gy = -1;
            try {
                gx = gce.getGridXValue();
                gy = gce.getGridYValue();
            } catch (Exception e) {
            }
            double[] f = real4relativeXfactors();
            if (gx > 0 && gy > 0) {
                g2d.setColor(gce.getGridColorValue());
                int x = (int) (gx / f[0]);
                int y = (int) (gy / f[1]);
                if (x > 2 && y > 2) {
                    double yy = 0;
//                yy = gy;
//                while (yy < d.height) {
//                    double xx = gx;
//                    while (xx < d.width) {
//                        g2d.drawLine((int) (xx / d.width * xsize) - 1,
//                                (int) (yy / d.height * ysize),
//                                (int) (xx / d.width * xsize) + 1,
//                                (int) (yy / d.height * ysize));
//                        g2d.drawLine((int) (xx / d.width * xsize),
//                                (int) (yy / d.height * ysize) - 1,
//                                (int) (xx / d.width * xsize),
//                                (int) (yy / d.height * ysize) + 1);
//                        xx += gx;
//                    }
//                    yy += gy;
//                }
//                Stroke oldStroke=g2d.getStroke();
//                g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0.2f,new float[]{3f,6f},0f));
                    if (y > 2) {
                        yy = gy;
                        while (yy < sd.getYwidth()) {
                            g2d.drawLine(0,
                                    (int) (yy / sd.getYwidth() * ysize),
                                    xsize,
                                    (int) (yy / sd.getYwidth() * ysize));
                            yy += gy;
                        }
                    }
                    if (x > 2) {
                        double xx = gx;
                        while (xx < sd.getXwidth()) {
                            g2d.drawLine((int) (xx / sd.getXwidth() * xsize),
                                    0,
                                    (int) (xx / sd.getXwidth() * xsize),
                                    ysize);
                            xx += gx;
                        }
                    }
//                g2d.setStroke(oldStroke);
                }
            }
        }

        private void paintSelect(Graphics2D g2d) {
            if (selectionArea == null) {
                return;
            }
            Dimension size = getSize();
            int xsize = size.width - 8;
            int ysize = size.height - 8;
            Domain dd = structure.getDomain();
            double dx = dd.getXMin();
            double dy = dd.getYMin();
            double dw = dd.getXwidth();
            double dh = dd.getYwidth();
            double ax = selectionArea.x;
            double ay = selectionArea.y;
            double aw = selectionArea.w;
            double ah = selectionArea.h;
            Color c = Color.BLUE;
//        float dash[] = {0.7f, 0.3f};

            g2d.setColor(c);
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(blink ? STROKE_DASHED_1 : STROKE_DASHED_2);
            g2d.drawRect((int) ((ax - dx) / dw * xsize),
                    (int) ((ay - dy) / dh * ysize),
                    (int) (aw / dw * xsize),
                    (int) (ah / dh * ysize));
            g2d.setStroke(oldStroke);
//        BufferedImage bi = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
//        bi.setRGB(0, 0, c.getRGB());
//        bi.setRGB(1, 0, 0xffff0000);
//        Composite oldComposite = g2d.getComposite();
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
//        g2d.fillRect((int) ((ax - dx) / dw * xsize),
//                (int) ((ay - dy) / dh * ysize),
//                (int) (aw / dw * xsize),
//                (int) (ah / dh * ysize));
//        g2d.setComposite(oldComposite);
        }
    }

    public void processMouseMoved_structure2DPlotPanel(MouseEvent e) {
        showTitleAreas = getAllAreas(e.getPoint());
        showTitleAreasPos = e.getPoint();

        resizeType = Cursor.DEFAULT_CURSOR;
        if (getSelection().size() == 1) {
            AreaZone az = (AreaZone) getSelection().toArray()[0];
            az.setContext(structure);
            if (az instanceof Area) {
                ((Area) az).setProject(structure);
            }
            resizeType = getResizeType(az);
        }
        setCursor(Cursor.getPredefinedCursor(resizeType));
        structure2DPlotPanel.repaint();
    }

    public void processMouseDragged_structure2DPlotPanel(MouseEvent e) {
        if (motionAreas.size() > 0) {
            AreaZone[] mzones = (AreaZone[]) motionAreas.toArray(new AreaZone[motionAreas.size()]);
            if (SwingUtilities.isRightMouseButton(e) || (SwingUtilities.isLeftMouseButton(e) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))) {
                if (startMotion) {
                    motionAreas.clear();
//                            ArrayList<Area> copies = new ArrayList<Area>();
                    for (int i = 0; i < mzones.length; i++) {
                        if (mzones[i] instanceof Area) {
                            Area c = (Area) mzones[i].clone();
                            motionAreas.add(c);
//                                    c.setParentGroup(null);
                            structureEditor.addArea(c);
                        } else {
                            AreaGroup c = (AreaGroup) mzones[i].clone();
                            c.setParentGroup(null);
                            c.setName(structure.getRootAreaGroup().generateNewChildName());
                            structure.getRootAreaGroup().addAreaGroup(c);
                            c.setProject(structure);
                            motionAreas.add(c);
                        }
                    }
                    clearSelection();
                    for (AreaZone areaZone : motionAreas) {
                        selectZone(areaZone);
                    }
                }
            }
            startMotion = false;
            Point newPosition = new Point(e.getPoint());
            double[] d = real4relativeXfactors();
            if (mzones.length == 1) {
                double deltaX = d[0] * (newPosition.getX() - oldPosition.getX());
                double deltaY = d[1] * (newPosition.getY() - oldPosition.getY());
                //System.out.println("deltaX = " + deltaX + " ; deltaY = " + deltaY);
                if (resizeType == Cursor.DEFAULT_CURSOR) {
                    mzones[0].setX(oldRealPos[0].x + d[0] * (newPosition.getX() - oldPosition.getX()));
                    mzones[0].setY(oldRealPos[0].y + d[1] * (newPosition.getY() - oldPosition.getY()));
                } else {

                    if (resizeType == Cursor.E_RESIZE_CURSOR || resizeType == Cursor.SE_RESIZE_CURSOR || resizeType == Cursor.NE_RESIZE_CURSOR) {
                        double w = oldRealPos[0].w + deltaX;
                        if (w >= 0) {
                            mzones[0].setWidth(w);
                        } else {
                            mzones[0].setWidth(-w);
                            mzones[0].setX(oldRealPos[0].x + w);
                        }
                    }

                    if (resizeType == Cursor.W_RESIZE_CURSOR || resizeType == Cursor.SW_RESIZE_CURSOR || resizeType == Cursor.NW_RESIZE_CURSOR) {
                        double w = oldRealPos[0].w - deltaX;
                        if (w >= 0) {
                            mzones[0].setWidth(w);
                            mzones[0].setX(oldRealPos[0].x + deltaX);
                        } else {
                            mzones[0].setWidth(-w);
                            mzones[0].setX(oldRealPos[0].x + oldRealPos[0].w);
                        }
                    }

                    if (resizeType == Cursor.S_RESIZE_CURSOR || resizeType == Cursor.SE_RESIZE_CURSOR || resizeType == Cursor.SW_RESIZE_CURSOR) {
                        double h = oldRealPos[0].h + deltaY;
                        if (h >= 0) {
                            mzones[0].setHeight(h);
                        } else {
                            mzones[0].setHeight(-h);
                            mzones[0].setY(oldRealPos[0].y + h);
                        }
                    }

                    if (resizeType == Cursor.N_RESIZE_CURSOR || resizeType == Cursor.NW_RESIZE_CURSOR || resizeType == Cursor.NE_RESIZE_CURSOR) {
                        double h = oldRealPos[0].h - deltaY;
                        if (h >= 0) {
                            mzones[0].setHeight(h);
                            mzones[0].setY(oldRealPos[0].y + deltaY);
                        } else {
                            mzones[0].setHeight(-h);
                            mzones[0].setY(oldRealPos[0].y + oldRealPos[0].h);
                        }
                    }
                }
            } else {
                for (int i = 0; i < mzones.length; i++) {
                    mzones[i].setX(oldRealPos[i].x + d[0] * (newPosition.getX() - oldPosition.getX()));
                    mzones[i].setY(oldRealPos[i].y + d[1] * (newPosition.getY() - oldPosition.getY()));
                }
            }
            showTitleAreas = getAllAreas(e.getPoint());
            showTitleAreasPos = e.getPoint();
            structure2DPlotPanel.repaint();
        }

        if (selectionArea != null && SwingUtilities.isLeftMouseButton(e)) {
            Point newPosition = new Point(e.getPoint());
            double[] d = real4relativeXfactors();

            double deltaX = d[0] * (newPosition.getX() - oldPosition.getX());
            double deltaY = d[1] * (newPosition.getY() - oldPosition.getY());
            //System.out.println("deltaX = " + deltaX + " ; deltaY = " + deltaY);

            if (deltaX >= 0) {
                selectionArea.w = (deltaX);
            } else {
                selectionArea.w = (-deltaX);
                selectionArea.x = (relative2Real(oldPosition)[0] + deltaX);
            }

            if (deltaY >= 0) {
                selectionArea.h = (deltaY);
            } else {
                selectionArea.h = (-deltaY);
                selectionArea.y = (relative2Real(oldPosition)[1] + deltaY);
            }
            showTitleAreas = getAllAreas(e.getPoint());
            showTitleAreasPos = e.getPoint();
            structure2DPlotPanel.repaint();
        }

    }

    public void processMouseDragged_statusbar(MouseEvent e) {
        AreaZone[] selected = getSelection().toArray(new AreaZone[getSelection().size()]);
        if (selected.length > 0) {
            Domain sd = selected[0].getDomain();
            statusBar.setXPosition(sd.getXMin());
            statusBar.setYPosition(sd.getYMin());
        } else {
            double[] p = relative2Real(e.getPoint());
            if (p == null) {
                statusBar.setXPosition(Double.NaN);
                statusBar.setYPosition(Double.NaN);
            } else {
                statusBar.setXPosition(p[0]);
                statusBar.setYPosition(p[1]);
            }
        }
    }

    public void processMouseMoved_statusbar(MouseEvent e) {
        double[] p = relative2Real(e.getPoint());
        if (p == null) {
            statusBar.setXPosition(Double.NaN);
            statusBar.setYPosition(Double.NaN);
        } else {
            statusBar.setXPosition(p[0]);
            statusBar.setYPosition(p[1]);
        }
    }

    public void processMousePressed_structure2DPlotPanel(MouseEvent e) {
        switch (editType) {
            case DRAW_METAL: {
                addMetalAt(e.getPoint());
                break;
            }
            case DRAW_SRC: {
                addSourceAt(e.getPoint());
                break;
            }
            case DRAW_ZS: {
                addImpedanceSurfaceAt(e.getPoint());
                break;
            }
            case PICKUP: {
                doSelectAreasForMoveAt(e.getPoint());
                break;
            }
        }
        doRememberSelectedAreasForMoveAt(e.getPoint());
    }

    public void processMouseReleased_structure2DPlotPanel(MouseEvent e) {
        if (selectionArea != null) {
            doEndSelection(!e.isControlDown());
        } else if (motionAreas.size() > 0) {
            doEndMotion();
        }
    }

    /**
     * if left mouse click, select area if right mouse click, show popup
     *
     * @param e
     */
    public void processMouseClicked_structure2DPlotPanel(MouseEvent e) {
        structure2DPlotPanel.requestFocus();
        if (SwingUtilities.isRightMouseButton(e)) {
            if (e.getClickCount() == 1) {
                showPopupMenu(e.getPoint());
            }
            return;
        }
        doSelectAreaAt(e.getPoint(), e.getClickCount(), !e.isControlDown());
    }
}
