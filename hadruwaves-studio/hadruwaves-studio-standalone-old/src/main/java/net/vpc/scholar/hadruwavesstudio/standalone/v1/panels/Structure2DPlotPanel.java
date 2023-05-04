package net.thevpc.scholar.hadruwavesstudio.standalone.v1.panels;

//package org.vpc.tmwlab.panels;
//
//import java.awt.AWTEvent;
//import java.awt.BasicStroke;
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Cursor;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Point;
//import java.awt.Stroke;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseMotionListener;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Iterator;
//
//import javax.swing.BorderFactory;
//import javax.swing.Box;
//import javax.swing.JCheckBox;
//import javax.swing.JComponent;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.SwingUtilities;
//import javax.swing.Timer;
//
//import org.vpc.application.Application;
//import org.vpc.config.Configuration;
//import org.vpc.lib.util.ConstraintsException;
//import org.vpc.momlib.planar.Structure;
//import org.vpc.momlib.planar.StructureContext;
//import org.vpc.tmwlab.editors.GridConfigEditor;
//import org.vpc.tmwlab.editors.StructureEditor;
//import org.vpc.momlib.planar.common.Area;
//import org.vpc.momlib.planar.common.AreaGroup;
//import org.vpc.momlib.planar.common.AreaManager;
//import org.vpc.momlib.planar.common.AreaZone;
//import org.vpc.momlib.planar.common.BoxArea;
//import org.vpc.swing.DataTypeEditor;
//import org.vpc.swing.EditComponentsPanel;
//import org.vpc.swing.JOptionPane2;
//import net.vpc.types.TypesFactory;
//
///**
// * Created by IntelliJ IDEA.
// * User: TAHA
// * Date: 11 mars 2004
// * Time: 21:44:57
// * To change this template use File | Settings | File Templates.
// */
//public class Structure2DPlotPanel extends JComponent {
////    static BufferedImage bi = new BufferedImage(2,1,BufferedImage.TYPE_INT_RGB);
////    static{
////        bi.setRGB(0, 0, 0xff00ff00); bi.setRGB(1, 0, 0xffff0000);
////    }
////    static TexturePaint tp1 = new TexturePaint(bi,new Rectangle(0,0,2,1));
////
////    static{
////        bi = new BufferedImage(2,1,BufferedImage.TYPE_INT_RGB);
////        bi.setRGB(0, 0, 0xff0000ff); bi.setRGB(1, 0, 0xffff0000);
////    }
////    static TexturePaint tp2 = new TexturePaint(bi,new Rectangle(0,0,2,1));
//    private Structure structure;
//    private Point oldPosition;
//    private Collection<AreaZone> motionAreas = new HashSet<AreaZone>();
//    private Collection<AreaZone> selectedZones = new HashSet<AreaZone>();
//    private Collection<Area> cached_selectedAreas = null;
//    private Area[] showTitleAreas = null;
//    private Point showTitleAreasPos = null;
//    private RectInfo[] oldRealPos;
//    private RectInfo selectionArea;
//    private static BasicStroke STROKE_DASHED_1 = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{6}, 0);
//    private static BasicStroke STROKE_DASHED_2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{12}, 0);
//
//    private static class RectInfo {
//
//        public RectInfo(double x, double y, double w, double h) {
//            this.x = x;
//            this.y = y;
//            this.w = w;
//            this.h = h;
//        }
//        double x;
//        double y;
//        double w;
//        double h;
//    }
//    private int resizeType = Cursor.DEFAULT_CURSOR;
//    private StructureEditor structureEditor;
//    private Structure2DPlotPopupMenu schemaMenu;
//    private GridConfigEditor gridConfigEditor;
//    private boolean blink = false;
//    private boolean startMotion = false;
//    private double zoomFactor = 1;
//    private int preferredInitialDimesion = 400;
//    private int precisionPoint = 2;
//
//    public static enum EditType {
//
//        PICKUP, DRAW_SRC, DRAW_METAL, DRAW_ZS
//    }
//    private EditType editType = EditType.PICKUP;
//
//    public void help() {
//        JOptionPane.showMessageDialog(this, 
//                ""
//                +"<HTML>"
//                +"<Table>"
//                +"<TR><TD>H</TD><TD>Show thos help</TD></TR>"
//                +"<TR><TD>CTR-LA</TD><TD>Select ALL</TD></TR>"
//                +"<TR><TD>ALT-ENTER</TD><TD>Edit Selected</TD></TR>"
//                +"<TR><TD>DELETE</TD><TD>Delete Selected</TD></TR>"
//                +"<TR><TD>G</TD><TD>Group/ungroup Selected</TD></TR>"
//                +"<TR><TD>P</TD><TD>Pin to Grid Selected</TD></TR>"
//                +"<TR><TD>C</TD><TD>Configure Grid</TD></TR>"
//                +"<TR><TD>ARROWS</TD><TD>Move Selected</TD></TR>"
//                +"<TR><TD>CTRL LEFT CLICK</TD><TD>Select/Deselect</TD></TR>"
//                +"<TR><TD>CTRL LEFT DRAG</TD><TD>Clone Selected</TD></TR>"
//                +"</Table>"
//                +"</HTML>"
//                
//                , "Help", JOptionPane.PLAIN_MESSAGE);
//    }
//    public Structure2DPlotPanel(StructureEditor theStructureEditor) {
//        structure = theStructureEditor.getStructure0();
////        Dimension ddd=new Dimension(600,600);
////        setPreferredSize(ddd);
////        setMinimumSize(ddd);
////        setMaximumSize(ddd);
//
//        this.structureEditor = theStructureEditor;
//        this.schemaMenu = new Structure2DPlotPopupMenu(this);
//        gridConfigEditor = new GridConfigEditor(structureEditor);
////        super.addInputMethodListener();
////        super.getActionMap();
////        super.getInputMap();
////        super.disableEvents();
////        super.enableEvents();
//        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_FOCUS_EVENT_MASK);
//        setRequestFocusEnabled(true);
//        setFocusTraversalKeysEnabled(false);
//        addKeyListener(new KeyListener() {
//
//            public void keyTyped(KeyEvent e) {
//            }
//
//            public void keyPressed(KeyEvent e) {
//                int key = e.getKeyCode();
//                switch (key) {
//                    case KeyEvent.VK_DELETE: {
//                        selectedAreasDoDelete();
//                        break;
//                    }
//                    case KeyEvent.VK_A: {
//                        if (e.isControlDown()) {
//                            selectedAll();
//                        }
//                        break;
//                    }
//                    case KeyEvent.VK_ENTER: {
//                        if (e.isAltDown()) {
//                            selectedAreasDoOpenEditors();
//                        }
//                        break;
//                    }
//                    case KeyEvent.VK_RIGHT: {
//                        selectedAreasMoveRight();
//                        break;
//                    }
//                    case KeyEvent.VK_LEFT: {
//                        selectedAreasMoveLeft();
//                        break;
//                    }
//                    case KeyEvent.VK_UP: {
//                        selectedAreasMoveUp();
//                        break;
//                    }
//                    case KeyEvent.VK_DOWN: {
//                        selectedAreasMoveDown();
//                        break;
//                    }
//                    case KeyEvent.VK_G: {
//                        selectedAreasDoGroupOrUngroup();
//                        break;
//                    }
//                    case KeyEvent.VK_P: {
//                        selectedAreasEnsurePin();
//                        break;
//                    }
//                    case KeyEvent.VK_C: {
//                        configure();
//                        break;
//                    }
//                    case KeyEvent.VK_H: {
//                        help();
//                        break;
//                    }
//                }
//            }
//
//            public void keyReleased(KeyEvent e) {
//            }
//        });
//        addMouseListener(new MouseAdapter() {
//
//            public void mouseClicked(MouseEvent e) {
//                requestFocus();
//                if (SwingUtilities.isRightMouseButton(e)) {
//                    if (e.getClickCount() == 1) {
//                        showPopupMenu(e.getPoint());
//                    }
//                    return;
//                }
//                doSelectAreaAt(e.getPoint(), e.getClickCount(), !e.isControlDown());
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                if (editType == EditType.DRAW_METAL) {
//                    addMetalAt(e.getPoint());
//                } else if (editType == EditType.DRAW_SRC) {
//                    addSourceAt(e.getPoint());
//                } else if (editType == EditType.DRAW_ZS) {
//                    addImpedanceSurfaceAt(e.getPoint());
//                } else if (editType == EditType.PICKUP) {
//                    doSelectAreasForMoveAt(e.getPoint());
//                }
//                doRememberSelectedAreasForMoveAt(e.getPoint());
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                if (selectionArea != null) {
//                    doEndSelection(!e.isControlDown());
//                } else if (motionAreas.size() > 0) {
//                    doEndMotion();
//                }
//            }
//        });
//
//        addMouseMotionListener(new MouseMotionListener() {
//
//            public void mouseDragged(MouseEvent e) {
//                BoxArea dielectric = structure.getBox();
//                if (dielectric == null) {
//                    return;
//                }
//                if (motionAreas.size() > 0) {
//                    AreaZone[] mzones = (AreaZone[]) motionAreas.toArray(new AreaZone[motionAreas.size()]);
//                    if (SwingUtilities.isRightMouseButton(e) || (SwingUtilities.isLeftMouseButton(e) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))) {
//                        if (startMotion) {
//                            motionAreas.clear();
////                            ArrayList<Area> copies = new ArrayList<Area>();
//                            for (int i = 0; i < mzones.length; i++) {
//                                if (mzones[i] instanceof Area) {
//                                    Area c = (Area) mzones[i].clone();
//                                    motionAreas.add(c);
////                                    c.setParentGroup(null);
//                                    structureEditor.addArea(c);
//                                } else {
//                                    AreaGroup c = (AreaGroup) mzones[i].clone();
//                                    c.setParentGroup(null);
//                                    c.setName(structure.getRootAreaGroup().generateNewChildName());
//                                    structure.getRootAreaGroup().addAreaGroup(c);
//                                    c.setStructure(structure);
//                                    motionAreas.add(c);
//                                }
//                            }
//                            clearSelection();
//                            for (AreaZone areaZone : motionAreas) {
//                                selectZone(areaZone);
//                            }
//                        }
//                    }
//                    startMotion = false;
//                    Point newPosition = new Point(e.getPoint());
//                    double[] d = real4relativeXfactors();
//                    if (mzones.length == 1) {
//                        double deltaX = d[0] * (newPosition.getX() - oldPosition.getX());
//                        double deltaY = d[1] * (newPosition.getY() - oldPosition.getY());
//                        //System.out.println("deltaX = " + deltaX + " ; deltaY = " + deltaY);
//                        if (resizeType == Cursor.DEFAULT_CURSOR) {
//                            mzones[0].setX(oldRealPos[0].x + d[0] * (newPosition.getX() - oldPosition.getX()));
//                            mzones[0].setY(oldRealPos[0].y + d[1] * (newPosition.getY() - oldPosition.getY()));
//                        } else {
//
//                            if (resizeType == Cursor.E_RESIZE_CURSOR || resizeType == Cursor.SE_RESIZE_CURSOR || resizeType == Cursor.NE_RESIZE_CURSOR) {
//                                double w = oldRealPos[0].w + deltaX;
//                                if (w >= 0) {
//                                    mzones[0].setWidth(w);
//                                } else {
//                                    mzones[0].setWidth(-w);
//                                    mzones[0].setX(oldRealPos[0].x + w);
//                                }
//                            }
//
//                            if (resizeType == Cursor.W_RESIZE_CURSOR || resizeType == Cursor.SW_RESIZE_CURSOR || resizeType == Cursor.NW_RESIZE_CURSOR) {
//                                double w = oldRealPos[0].w - deltaX;
//                                if (w >= 0) {
//                                    mzones[0].setWidth(w);
//                                    mzones[0].setX(oldRealPos[0].x + deltaX);
//                                } else {
//                                    mzones[0].setWidth(-w);
//                                    mzones[0].setX(oldRealPos[0].x + oldRealPos[0].w);
//                                }
//                            }
//
//                            if (resizeType == Cursor.S_RESIZE_CURSOR || resizeType == Cursor.SE_RESIZE_CURSOR || resizeType == Cursor.SW_RESIZE_CURSOR) {
//                                double h = oldRealPos[0].h + deltaY;
//                                if (h >= 0) {
//                                    mzones[0].setHeight(h);
//                                } else {
//                                    mzones[0].setHeight(-h);
//                                    mzones[0].setY(oldRealPos[0].y + h);
//                                }
//                            }
//
//                            if (resizeType == Cursor.N_RESIZE_CURSOR || resizeType == Cursor.NW_RESIZE_CURSOR || resizeType == Cursor.NE_RESIZE_CURSOR) {
//                                double h = oldRealPos[0].h - deltaY;
//                                if (h >= 0) {
//                                    mzones[0].setHeight(h);
//                                    mzones[0].setY(oldRealPos[0].y + deltaY);
//                                } else {
//                                    mzones[0].setHeight(-h);
//                                    mzones[0].setY(oldRealPos[0].y + oldRealPos[0].h);
//                                }
//                            }
//                        }
//                    } else {
//                        for (int i = 0; i < mzones.length; i++) {
//                            mzones[i].setX(oldRealPos[i].x + d[0] * (newPosition.getX() - oldPosition.getX()));
//                            mzones[i].setY(oldRealPos[i].y + d[1] * (newPosition.getY() - oldPosition.getY()));
//                        }
//                    }
//                    showTitleAreas = getAllAreas(e.getPoint());
//                    showTitleAreasPos = e.getPoint();
//                    repaint();
//                }
//
//                if (selectionArea != null && SwingUtilities.isLeftMouseButton(e)) {
//                    Point newPosition = new Point(e.getPoint());
//                    double[] d = real4relativeXfactors();
//
//                    double deltaX = d[0] * (newPosition.getX() - oldPosition.getX());
//                    double deltaY = d[1] * (newPosition.getY() - oldPosition.getY());
//                    //System.out.println("deltaX = " + deltaX + " ; deltaY = " + deltaY);
//
//                    if (deltaX >= 0) {
//                        selectionArea.w = (deltaX);
//                    } else {
//                        selectionArea.w = (-deltaX);
//                        selectionArea.x = (relative2Real(oldPosition)[0] + deltaX);
//                    }
//
//
//                    if (deltaY >= 0) {
//                        selectionArea.h = (deltaY);
//                    } else {
//                        selectionArea.h = (-deltaY);
//                        selectionArea.y = (relative2Real(oldPosition)[1] + deltaY);
//                    }
//                    showTitleAreas = getAllAreas(e.getPoint());
//                    showTitleAreasPos = e.getPoint();
//                    repaint();
//                }
//            }
//
//            public void mouseMoved(MouseEvent e) {
//                showTitleAreas = getAllAreas(e.getPoint());
//                showTitleAreasPos = e.getPoint();
//                BoxArea dielectric = structure.getBox();
//                if (dielectric == null) {
//                    return;
//                }
//
//                resizeType = Cursor.DEFAULT_CURSOR;
//                if (getSelection().size() == 1) {
//                    resizeType = getResizeType((AreaZone) getSelection().toArray()[0]);
//                }
//                setCursor(Cursor.getPredefinedCursor(resizeType));
//                repaint();
//            }
//        });
//        setZoomFactor(1);
//        Timer timer = new Timer(500, new ActionListener() {
//
//            public void actionPerformedImpl(ActionEvent e) {
//                blink = !blink;
//                repaint();
//            }
//        });
//        timer.start();
//
//    }
//
//    public int getResizeType(AreaZone s) {
//        int rtype = Cursor.DEFAULT_CURSOR;
//        double[] d = real4relativeXfactors();
//        double Edist = Math.abs(showTitleAreasPos.x - 4 - (s.getX() - structure.getBox().x + s.getWidth()) / d[0]);
//        double Wdist = Math.abs(showTitleAreasPos.x - 4 - (s.getX() - structure.getBox().x) / d[0]);
//        double Sdist = Math.abs(showTitleAreasPos.y - 4 - (s.getY() - structure.getBox().y + s.getHeight()) / d[1]);
//        double Ndist = Math.abs(showTitleAreasPos.y - 4 - (s.getY() - structure.getBox().y) / d[1]);
//        boolean E = Math.abs(showTitleAreasPos.x - 4 - (s.getX() - structure.getBox().x + s.getWidth()) / d[0]) <= precisionPoint;
//        boolean W = Math.abs(showTitleAreasPos.x - 4 - (s.getX() - structure.getBox().x) / d[0]) <= precisionPoint;
//        boolean S = Math.abs(showTitleAreasPos.y - 4 - (s.getY() - structure.getBox().y + s.getHeight()) / d[1]) <= precisionPoint;
//        boolean N = Math.abs(showTitleAreasPos.y - 4 - (s.getY() - structure.getBox().y) / d[1]) <= precisionPoint;
////                    StringBuffer debug=new StringBuffer();
////
////                    debug.append(Math.abs(showTitleAreasPos.x - 4 - (s.getX() - structure.getDielectric().x + s.getWidth()) / d[0]));
////                    debug.append(";");
////                    debug.append(Math.abs(showTitleAreasPos.x - 4 - (s.getX() - structure.getDielectric().x) / d[0]));
////                    debug.append(";");
////                    debug.append(Math.abs(showTitleAreasPos.y - 4 - (s.getY() - structure.getDielectric().y + s.getHeight()) / d[1]));
////                    debug.append(";");
////                    debug.append(Math.abs(showTitleAreasPos.y - 4 - (s.getY() - structure.getDielectric().y) / d[1]));
////                    debug.append("=>");
////
////                    if(E){debug.append("E");}
////                    if(W){debug.append("W");}
////                    if(S){debug.append("S");}
////                    if(N){debug.append("N");}
////                    debug.append(".");
////                    System.out.println(debug);
//        if (N & S) {
//            if (Ndist < Sdist) {
//                S = false;
//            } else {
//                N = false;
//            }
//        }
//        if (E & W) {
//            if (Wdist < Edist) {
//                E = false;
//            } else {
//                W = false;
//            }
//        }
//        if (S & E) {
//            rtype = Cursor.SE_RESIZE_CURSOR;
//        } else if (N & E) {
//            rtype = Cursor.NE_RESIZE_CURSOR;
//        } else if (S & W) {
//            rtype = Cursor.SW_RESIZE_CURSOR;
//        } else if (N & W) {
//            rtype = Cursor.NW_RESIZE_CURSOR;
//        } else if (S) {
//            rtype = Cursor.S_RESIZE_CURSOR;
//        } else if (N) {
//            rtype = Cursor.N_RESIZE_CURSOR;
//        } else if (E) {
//            rtype = Cursor.E_RESIZE_CURSOR;
//        } else if (W) {
//            rtype = Cursor.W_RESIZE_CURSOR;
//        }
//        return rtype;
//    }
//
//    public void doSelectAreaAt(Point point, int clickCount, boolean clearSelection) {
//        Area[] a = getAllAreas(point);
//        int c = clickCount;
//        if (c == 0) {
//            return;
//        }
//        if (c <= a.length) {
//            c = c - 1;
//        } else {
//            c = -1;
//        }
//        if (!clearSelection) {
//            if (c >= 0 && c < a.length) {
//                if (isSelectedZone(a[c])) {
//                    deselectZone(a[c]);
//                } else {
//                    selectZone(a[c]);
//                }
//            }
//        } else {
//            clearSelection();
//            if (c != -1) {
//                selectZone(a[c]);
//            }
//        }
//        repaint();
//
//    }
//
//    public void showPopupMenu(Point p) {
//        schemaMenu.configure(getAllAreas(p));
//        schemaMenu.show(Structure2DPlotPanel.this, p.x, p.y);
//    }
//
//    public void selectedAreasMoveRight() {
//        if (getAllSelectedAreas().size() > 0) {
//            double[] b = relative2Real(new Point(1, 1));
//            for (Area area : getAllSelectedAreas()) {
//                area.setX(area.x - 1 * b[0]);
//            }
//            repaint();
//        }
//    }
//
//    public void selectedAreasMoveLeft() {
//        if (getAllSelectedAreas().size() > 0) {
//            double[] b = relative2Real(new Point(1, 1));
//            for (Area area : getAllSelectedAreas()) {
//                area.setX(area.x + 1 * b[0]);
//            }
//            repaint();
//        }
//    }
//
//    public void selectedAreasMoveUp() {
//        if (getAllSelectedAreas().size() > 0) {
//            double[] b = relative2Real(new Point(1, 1));
//            for (Area area : getAllSelectedAreas()) {
//                area.setY(area.y + 1 * b[1]);
//            }
//            repaint();
//        }
//    }
//
//    public void selectedAreasMoveDown() {
//        if (getAllSelectedAreas().size() > 0) {
//            double[] b = relative2Real(new Point(1, 1));
//            for (Area area : getAllSelectedAreas()) {
//                area.setY(area.y - 1 * b[1]);
//            }
//            repaint();
//        }
//    }
//
//    public void selectedAreasDoGroupOrUngroup() {
//        if (getAllSelectedAreas().size() > 1) {
//            selectedAreasDoGroup();
//        } else if (getAllSelectedAreas().size() == 1) {
//            selectedAreasDoUngroup();
//        }
//    }
//
//    public void selectedAreasDoGroup() {
//        if (getAllSelectedAreas().size() > 1) {
//
//            HashSet<Area> areazz = new HashSet<Area>();
//            HashSet<AreaGroup> groupzz = new HashSet<AreaGroup>();
//            for (AreaZone area : getSelection()) {
//                AreaGroup g = area.getTopLevelGroup();
//                if (g != null && !groupzz.contains(g)) {
//                    groupzz.add(g);
//                } else if (g == null && area instanceof AreaGroup) {
//                    groupzz.add((AreaGroup) area);
//                } else if (g == null && area instanceof Area && !areazz.contains((Area) area)) {
//                    areazz.add((Area) area);
//                }
//            }
//            AreaGroup ng = structure.getRootAreaGroup().addNewGroup();
//            for (Area area : areazz) {
//                area.getParentGroup().removeArea(area);
//                ng.addArea(area);
//            }
//            for (AreaGroup areaGroup : groupzz) {
//                areaGroup.getParentGroup().removeAreaGroup(areaGroup);
//                ng.addAreaGroup(areaGroup);
//            }
//            clearSelection();
//            selectZone(ng);
////            structureEditor.setStructure(structure);
//            structureEditor.updateView();
//            repaint();
//        }
//    }
//
//    public void selectedAreasDoUngroup() {
//        if (getAllSelectedAreas().size() >= 1) {
//            HashSet<AreaGroup> groupzz = new HashSet<AreaGroup>();
//            for (AreaZone area : getSelection()) {
//                AreaGroup g = area.getTopLevelGroup();
//                if (g != null && !groupzz.contains(g)) {
//                    groupzz.add(g);
//                }
//            }
//            for (AreaGroup areaGroup : groupzz) {
//                areaGroup.removeGroupButRetainChildren();
//            }
//            clearSelection();
////            structureEditor.setStructure(structure);
//            structureEditor.updateView();
//            repaint();
//        }
//    }
//
//    public double[] real4relativeXfactors() {
//        Dimension size = getSize();
//        int xsize = size.width - 8;
//        int ysize = size.height - 8;
//        BoxArea dielectric = structure.getBox();
//        if (dielectric == null) {
//            return new double[]{0, 0};
//        }
//
//        double dw = dielectric.width;
//        double dh = dielectric.height;
//        return new double[]{dw / xsize, dh / ysize};
//    }
//
//    public double[] relative2Real(Point relativePoint) {
//        Point p = new Point(relativePoint.x - 4, relativePoint.y - 4);
//        BoxArea dielectric = structure.getBox();
//        if (dielectric == null) {
//            return new double[]{0, 0};
//        }
//        double[] f = real4relativeXfactors();
//        return new double[]{
//            dielectric.x + p.x * f[0],
//            dielectric.y + p.y * f[1]
//        };
//    }
//
//    public double[] relative2RealUnit(Point relativePoint) {
//        double[] r = relative2Real(relativePoint);
//        StructureContext c = structure.getStructureContext();
//        double u = c.getDimensionUnit();
//        r[0] = (r[0] / u);
//        r[1] = (r[1] / u);
//        return r;
//    }
//
//    public Area[] getAllAreas(Point p) {
//        return getAllAreas(p, false);
//    }
//
//    public Area[] getAllAreas(Point p, boolean includeBox) {
//        return getAllAreas(p, includeBox, precisionPoint);
//    }
//
//    public Area[] getAllAreas(Point p, boolean includeBox, int delta) {
//        ArrayList<Area> allAreasVector = new ArrayList<Area>();
//        p = new Point(p.x - 4, p.y - 4);
//        Dimension size = getSize();
//        int xsize = size.width - 8;
//        int ysize = size.height - 8;
//        BoxArea dielectric = structure.getBox();
//        if (dielectric != null) {
//            double dx = (float) dielectric.x;
//            double dy = (float) dielectric.y;
//            double dw = (float) dielectric.width;
//            double dh = (float) dielectric.height;
//            Collection<Area> all = getAllEnabledAreas();
//            for (Area a : all) {
//                if (!includeBox && (a instanceof BoxArea)) {
//                    continue;
//                }
//                int xx = (int) ((a.x - dx) / dw * xsize);
//                int yy = (int) ((a.y - dy) / dh * ysize);
//                int wx = (int) (a.width / dw * xsize);
//                int hy = (int) (a.height / dh * ysize);
//                if ((p.x >= (xx - precisionPoint) && p.x <= (xx + wx + precisionPoint)) && (p.y >= (yy - precisionPoint) && p.y <= (yy + hy + precisionPoint))) {
//                    allAreasVector.add(a);
//                }
//            }
//        }
//        return (Area[]) allAreasVector.toArray(new Area[allAreasVector.size()]);
//    }
//
//    public Collection<Area> getAllEnabledAreas() {
//        return structure.findAreas(null, Boolean.TRUE);
//    }
//    
//    public void selectedAll(){
//        clearSelection();
//        for (Area area : structure.findAreas(null, Boolean.TRUE)) {
//            if(!(area instanceof BoxArea)){
//                selectZone(area);
//            }
//        }
//    }
//
////    public Dimension getSize(){
////        Dielectric dielectric=structure==null?null:structure.getDielectric();
////        double min=Maths.min(dielectric.width,dielectric.height);
////        double max=Maths.min(dielectric.width,dielectric.height);
////        double ff=1;
////        if(max<100){
////            ff=100/max;
////        }
////        return dielectric==null?getPreferredSize():new Dimension(
////                (int) (dielectric.width*ff*zoomFactor),
////                (int) (dielectric.height*ff*zoomFactor)
////        );
////    }
//    public Area getArea(Point p) {
//        p = new Point(p.x - 4, p.y - 4);
//        Dimension size = getSize();
//        int xsize = size.width - 8;
//        int ysize = size.height - 8;
//        BoxArea dielectric = structure.getBox();
//        if (dielectric != null) {
//            double dx = (float) dielectric.x;
//            double dy = (float) dielectric.y;
//            double dw = (float) dielectric.width;
//            double dh = (float) dielectric.height;
//            Collection<Area> all = getAllEnabledAreas();
//            for (Area a : all) {
//                int xx = (int) ((a.x - dx) / dw * xsize);
//                int yy = (int) ((a.y - dy) / dh * ysize);
//                int wx = (int) (a.width / dw * xsize);
//                int hy = (int) (a.height / dh * ysize);
//                if ((p.x >= xx && p.x <= (xx + wx)) && (p.y >= yy && p.y <= (yy + hy))) {
//                    return a;
//                }
//            }
//        }
//        return null;
//    }
//
//    public void paint(Graphics g) {
//        try {
//            Graphics2D g2d = (Graphics2D) g;
//            Dimension size = getSize();
////        int xsize=size.width-8;
////        int ysize=size.height-8;
//            g2d.setColor(Color.BLACK);
//            g2d.fillRect(0, 0, size.width, size.height);
//            if (hasFocus()) {
//                g2d.setColor(Color.RED);
//                g2d.drawRect(0, 0, size.width, size.height);
//            }
//            g2d.translate(4, 4);
//            if (size != null) {
//                BoxArea dielectric = structure.getBox();
//                if (dielectric != null) {
//                    paintBox(dielectric, g2d);
////                g2d.setColor(Color.RED);
////                g2d.drawRect(0,0,xsize,ysize);
//                    Collection<Area> all = getAllEnabledAreas();
//                    for (Area a : all) {
//                        a.paintArea(g2d, getSize(),
//                                getAllSelectedAreas().contains(a) && blink,
//                                0.4f);
//                    }
//                    if (showTitleAreasPos != null) {
//                        for (int i = 0; i < showTitleAreas.length; i++) {
//                            g2d.setColor(showTitleAreas[i].getColor());
//                            g2d.drawString(showTitleAreas[i].getName(),
//                                    showTitleAreasPos.x + 6,
//                                    showTitleAreasPos.y + (i * 10) + 6);
//                        }
//                    }
//                    paintSelect(g2d);
//                    return;
//                }
//                g2d.drawString("EMPTY", 15, 15);
//                paintSelect(g2d);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.paint(g);
//    }
//
////    private void paintArea(Area a, Graphics2D g2d) {
////        paintArea(a, g2d, 0.4f);
////    }
////
////    public void pinComponents(){
////        Dielectric d=structure.getDielectric();
////        Dimension size=getSize();
////        int xsize=size.width-8;
////        int ysize=size.height-8;
////        double gx=d.getGridX();
////        double gy=d.getGridY();
////        double[] f=real2relativeXfactors();
////        if(gx>0 && gy>0){
////            int x=(int) (gx/f[0]);
////            int y=(int) (gy/f[1]);
////            if(x>5 && y>5){
////                Area[] a= structure.getEnabledMetals();
////                for(int i=0;i<a.length;i++){
////
////                }
////            }
////        }
////    }
//    private void paintBox(BoxArea d, Graphics2D g2d) {
//        Dimension size = getSize();
//        int xsize = size.width - 8;
//        int ysize = size.height - 8;
//        d.paintArea(g2d, getSize(), false, 1f);
//        GridConfigEditor gce = gridConfigEditor;
//        double gx = -1;
//        double gy = -1;
//        try {
//            gx = gce.getGridXValue();
//            gy = gce.getGridYValue();
//        } catch (Exception e) {
//        }
//        double[] f = real4relativeXfactors();
//        if (gx > 0 && gy > 0) {
//            g2d.setColor(gce.getGridColorValue());
//            int x = (int) (gx / f[0]);
//            int y = (int) (gy / f[1]);
//            if (x > 2 && y > 2) {
//                double yy = 0;
////                yy = gy;
////                while (yy < d.height) {
////                    double xx = gx;
////                    while (xx < d.width) {
////                        g2d.drawLine((int) (xx / d.width * xsize) - 1,
////                                (int) (yy / d.height * ysize),
////                                (int) (xx / d.width * xsize) + 1,
////                                (int) (yy / d.height * ysize));
////                        g2d.drawLine((int) (xx / d.width * xsize),
////                                (int) (yy / d.height * ysize) - 1,
////                                (int) (xx / d.width * xsize),
////                                (int) (yy / d.height * ysize) + 1);
////                        xx += gx;
////                    }
////                    yy += gy;
////                }
////                Stroke oldStroke=g2d.getStroke();
////                g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0.2f,new float[]{3f,6f},0f));
//                if (y > 2) {
//                    yy = gy;
//                    while (yy < d.height) {
//                        g2d.drawLine(0,
//                                (int) (yy / d.height * ysize),
//                                xsize,
//                                (int) (yy / d.height * ysize));
//                        yy += gy;
//                    }
//                }
//                if (x > 2) {
//                    double xx = gx;
//                    while (xx < d.width) {
//                        g2d.drawLine((int) (xx / d.width * xsize),
//                                0,
//                                (int) (xx / d.width * xsize),
//                                ysize);
//                        xx += gx;
//                    }
//                }
////                g2d.setStroke(oldStroke);
//            }
//        }
//    }
//
//    public double getPinnedX(double realX) {
//        double gx = gridConfigEditor.getGridXValue();
//        BoxArea dielectric = structure.getBox();
//        if (dielectric == null) {
//            return realX;
//        }
//        double dw = (float) dielectric.width;
//        if (gx == 0 || dw == 0) {
//            return realX;
//        }
//        return Math.round((realX - dielectric.x) / gx) * gx + dielectric.x;
//    }
//
//    public double getPinnedY(double realY) {
//        double gy = gridConfigEditor.getGridYValue();
//        BoxArea dielectric = structure.getBox();
//        if (dielectric == null) {
//            return realY;
//        }
//        double dh = (float) dielectric.height;
//        if (gy == 0 || dh == 0) {
//            return realY;
//        }
//        return Math.round((realY - dielectric.y) / gy) * gy + dielectric.y;
//    }
//
//    private void paintSelect(Graphics2D g2d) {
//        if (selectionArea == null) {
//            return;
//        }
//        Dimension size = getSize();
//        if (structure.getBox() == null) {
//            return;
//        }
//        int xsize = size.width - 8;
//        int ysize = size.height - 8;
//        BoxArea dielectric = structure.getBox();
//        double dx = dielectric.x;
//        double dy = dielectric.y;
//        double dw = dielectric.width;
//        double dh = dielectric.height;
//        double ax = selectionArea.x;
//        double ay = selectionArea.y;
//        double aw = selectionArea.w;
//        double ah = selectionArea.h;
//        Color c = Color.BLUE;
////        float dash[] = {0.7f, 0.3f};
//
//        g2d.setColor(c);
//        Stroke oldStroke = g2d.getStroke();
//        g2d.setStroke(blink ? STROKE_DASHED_1 : STROKE_DASHED_2);
//        g2d.drawRect((int) ((ax - dx) / dw * xsize),
//                (int) ((ay - dy) / dh * ysize),
//                (int) (aw / dw * xsize),
//                (int) (ah / dh * ysize));
//        g2d.setStroke(oldStroke);
////        BufferedImage bi = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
////        bi.setRGB(0, 0, c.getRGB());
////        bi.setRGB(1, 0, 0xffff0000);
////        Composite oldComposite = g2d.getComposite();
////        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
////        g2d.fillRect((int) ((ax - dx) / dw * xsize),
////                (int) ((ay - dy) / dh * ysize),
////                (int) (aw / dw * xsize),
////                (int) (ah / dh * ysize));
////        g2d.setComposite(oldComposite);
//    }
//
//    public Structure getStructure() {
//        return structure;
//    }
//
//    public void loadConfiguration(Configuration conf, String key) {
//        gridConfigEditor.loadConfiguration(conf, key + ".grid");
//    }
//
//    public void storeConfiguration(Configuration conf, String key) {
//        gridConfigEditor.storeConfiguration(conf, key + ".grid");
//    }
//
//    public void configure() {
//        JOptionPane.showMessageDialog(this, gridConfigEditor, "Parametres de la grille", JOptionPane.PLAIN_MESSAGE);
//    }
//    
//
//    public void zoomIn() {
//        setZoomFactor(getZoomFactor() + 0.5);
//    }
//
//    public void zoomOut() {
//        setZoomFactor(getZoomFactor() - 0.5);
//    }
//
//    public void noZoom() {
//        setZoomFactor(1);
//    }
//
//    public void ensurePin(Area a) {
//        a.setX(getPinnedX(a.x));
//        a.setY(getPinnedY(a.y));
//        a.setWidth(getPinnedX(a.x + a.width) - a.x);
//        if (a.width <= 0) {
//            a.setWidth(gridConfigEditor.getGridXValue());
//        }
//        a.setHeight(getPinnedY(a.y + a.height) - a.y);
//        if (a.height <= 0) {
//            a.setHeight(gridConfigEditor.getGridYValue());
//        }
//    }
//
//    public void selectedAreasEnsurePin() {
//        for (Iterator iterator = getAllSelectedAreas().iterator(); iterator.hasNext();) {
//            Area area = (Area) iterator.next();
//            ensurePin(area);
//        }
//    }
//
//    public void showSelectDialog() {
//        JPanel box = new JPanel(new BorderLayout());
//
//        Area[] sources = structure.getSources(Boolean.TRUE).toArray(new Area[structure.getSources(Boolean.TRUE).size()]);
//        JCheckBox[] sourcesCheckBoxes = new JCheckBox[sources.length];
//        Box boxS = Box.createVerticalBox();
//        for (int i = 0; i < sourcesCheckBoxes.length; i++) {
//            sourcesCheckBoxes[i] = new JCheckBox(sources[i].getName());
//            if (getAllSelectedAreas().contains(sources[i])) {
//                sourcesCheckBoxes[i].setSelected(true);
//            }
//            boxS.add(sourcesCheckBoxes[i]);
//        }
//        boxS.setBorder(BorderFactory.createTitledBorder("Sources"));
//
//        Area[] metals = structure.getMetals(Boolean.TRUE).toArray(new Area[structure.getMetals(Boolean.TRUE).size()]);
//        JCheckBox[] metalsCheckBoxes = new JCheckBox[metals.length];
//        Box boxM = Box.createVerticalBox();
//        for (int i = 0; i < metalsCheckBoxes.length; i++) {
//            metalsCheckBoxes[i] = new JCheckBox(metals[i].getName());
//            if (getAllSelectedAreas().contains(metals[i])) {
//                metalsCheckBoxes[i].setSelected(true);
//            }
//            boxM.add(metalsCheckBoxes[i]);
//        }
//        boxM.setBorder(BorderFactory.createTitledBorder(Application.getResources().get("metals")));
//
//        Area[] zs = structure.getImpedanceSurfaces(Boolean.TRUE).toArray(new Area[structure.getImpedanceSurfaces(Boolean.TRUE).size()]);
//        JCheckBox[] zsCheckBoxes = new JCheckBox[zs.length];
//        Box zsBox = Box.createVerticalBox();
//        for (int i = 0; i < zsCheckBoxes.length; i++) {
//            zsCheckBoxes[i] = new JCheckBox(zs[i].getName());
//            if (getAllSelectedAreas().contains(zs[i])) {
//                zsCheckBoxes[i].setSelected(true);
//            }
//            zsBox.add(metalsCheckBoxes[i]);
//        }
//        zsBox.setBorder(BorderFactory.createTitledBorder(Application.getResources().get("surfaceImpedances")));
//
//        box.add(boxS, BorderLayout.NORTH);
//        box.add(boxM, BorderLayout.CENTER);
//        box.add(zsBox, BorderLayout.CENTER);
//        JScrollPane scrollPane = new JScrollPane(box);
//        scrollPane.setPreferredSize(new Dimension(150, 300));
//        int ret = JOptionPane.showConfirmDialog(this, scrollPane, "Selectionner ...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//        if (ret == JOptionPane.OK_OPTION) {
//            clearSelection();
//            for (int i = 0; i < sourcesCheckBoxes.length; i++) {
//                if (sourcesCheckBoxes[i].isSelected()) {
//                    selectZone(sources[i]);
//                    structureEditor.setSelectedArea(sources[i]);
//                }
//            }
//            for (int i = 0; i < metalsCheckBoxes.length; i++) {
//                if (metalsCheckBoxes[i].isSelected()) {
//                    selectZone(metals[i]);
//                    structureEditor.setSelectedArea(metals[i]);
//                }
//            }
//            for (int i = 0; i < zsCheckBoxes.length; i++) {
//                if (zsCheckBoxes[i].isSelected()) {
//                    selectZone(zs[i]);
//                    structureEditor.setSelectedArea(zs[i]);
//                }
//            }
//        }
//        repaint();
//    }
//
//    public double getZoomFactor() {
//        return zoomFactor;
//    }
//
//    public void setZoomFactor(double zoomFactor) {
//        Dimension old = getSize();
//        this.zoomFactor = zoomFactor;
//        BoxArea dielectric = structure.getBox();
//        if (dielectric == null) {
//            return;
//        }
//
//        double structureFactor = (dielectric.width > 0 && dielectric.height > 0) ? (dielectric.height / dielectric.width)
//                : 1;
//        Dimension newSize = (preferredInitialDimesion <= 1) ? new Dimension((int) (this.zoomFactor * preferredInitialDimesion),
//                (int) (this.zoomFactor * preferredInitialDimesion * structureFactor))
//                : new Dimension((int) (this.zoomFactor * preferredInitialDimesion / structureFactor),
//                (int) (this.zoomFactor * preferredInitialDimesion));
//
//        setSize(newSize);
//        setPreferredSize(newSize);
//        setMaximumSize(newSize);
//        firePropertyChange("size", old, getSize());
//        repaint();
//    }
//
//    public void deleteArea(Area area) {
//        deselectZone(area);
//        structureEditor.removeArea(area);
//    }
//
//    public void editArea(Area area) {
//        structureEditor.showEditArea(area);
//    }
//
//    public void splitMetal(Area area) {
//        double gx = gridConfigEditor.getGridXValue();
//        double gy = gridConfigEditor.getGridYValue();
//        if (gx == 0 || gy == 0) {
//            return;
//        }
//        ensurePin(area);
//        deleteArea(area);
//
//        int nx = (int) Math.round(area.width / gx);
//
//        int ny = (int) Math.round(area.height / gy);
//
////        Area[] all=new Area[nx*ny];
//        for (int i = 0; i < nx; i++) {
//            for (int j = 0; j < ny; j++) {
////                int p = i * nx + ny;
//                Area area2 = (Area) area.clone();
//                area2.setName(area.getName() + "(" + (i + 1) + "," + (j + 1) + ")");
//                area2.setWidth(gx);
//                area2.setHeight(gy);
//                area2.setX(area.x + i * gx);
//                area2.setY(area.y + j * gy);
//                addMetal(area2);
//            }
//        }
//    }
//
//    public void splitMetal(Area area, int nx, int ny) {
////        if(gx==0 || gy==0){
////            return;
////        }
////        ensurePin(area);
//        deleteArea(area);
//        double gx = area.width / nx;
//        double gy = area.height / ny;
////
////        int nx=(int)Math.round(area.width/gx);
////
////        int ny=(int)Math.round(area.height/gy);
//
////        Area[] all=new Area[nx*ny];
//        for (int i = 0; i < nx; i++) {
//            for (int j = 0; j < ny; j++) {
////                int p = i * nx + ny;
//                Area area2 = (Area) area.clone();
//                area2.setName(area.getName() + "(" + (i + 1) + "," + (j + 1) + ")");
//                area2.setWidth(gx);
//                area2.setHeight(gy);
//                area2.setX(area.x + i * gx);
//                area2.setY(area.y + j * gy);
//                addMetal(area2);
//            }
//        }
//    }
//
//    public void addSource() {
//        structureEditor.showAddArea(AreaManager.SOURCE_CATEGORY);
//    }
//
//    public void addMetal() {
//        structureEditor.showAddArea(AreaManager.METAL_CATEGORY);
//    }
//
//    public void addImpedanceSurface() {
//        structureEditor.showAddArea(AreaManager.ZS_CATEGORY);
//    }
//
//    public Area addMetalAt(Point point) {
//        return addAreaAt(AreaManager.METAL_CATEGORY, point);
//    }
//
//    public Area addSourceAt(Point point) {
//        return addAreaAt(AreaManager.SOURCE_CATEGORY, point);
//    }
//
//    public Area addImpedanceSurfaceAt(Point point) {
//        return addAreaAt(AreaManager.ZS_CATEGORY, point);
//    }
//
//    public Area addAreaAt(String categoryType, Point point) {
//        double[] r = relative2Real(point);
//        Area a = AreaManager.getCategoryDesc(categoryType).getDefaultAreaType().createInsance();
//        structureEditor.addArea(a);
//        a.setX(r[0]);
//        a.setY(r[1]);
//        double unit = Maths.min(Maths.min(structure.getBox().getWidth() / 8, structure.getBox().getHeight() / 8), structure.getStructureContext().getDimensionUnit());
//        a.setWidth(unit);
//        a.setHeight(unit);
//        setEditType(EditType.PICKUP);
//        clearSelection();
//        selectZone(a);
//        motionAreas.clear();
//        motionAreas.add(a);
//        resizeType = getResizeType(a);
//        setCursor(Cursor.getPredefinedCursor(resizeType));
//        return a;
//    }
//
//    public void doEndMotion() {
//        for (AreaZone areaZone : motionAreas) {
//            areaZone.recompile();
//        }
//
//        motionAreas.clear();
//        oldPosition = null;
//        oldRealPos = null;
//        repaint();
//    }
//
//    public void doEndSelection(boolean clearSelection) {
//        double sx1 = selectionArea.x;
//        double sy1 = selectionArea.y;
//        double sx2 = selectionArea.x + selectionArea.w;
//        double sy2 = selectionArea.y + selectionArea.h;
//        if (clearSelection) {
//            clearSelection();
//        }
//        for (Area area : getAllEnabledAreas()) {
//            double ax1 = area.x;
//            double ay1 = area.y;
//            double ax2 = area.x + area.width;
//            double ay2 = area.y + area.height;
//            if (ax1 >= sx1 && ax2 <= sx2 && ay1 >= sy1 && ay2 <= sy2) {
//                selectZone(area);
//            }
//        }
//        selectionArea = null;
//        repaint();
//    }
//
//    public void doRememberSelectedAreasForMoveAt(Point point) {
//        if (motionAreas.size() > 0) {
//            startMotion = true;
//            oldPosition = new Point(point);
//            oldRealPos = new RectInfo[motionAreas.size()];
//            int i = 0;
//            for (Iterator<AreaZone> iterator = motionAreas.iterator(); iterator.hasNext();) {
//                AreaZone areaZone = iterator.next();
//                oldRealPos[i++] = new RectInfo(areaZone.getX(), areaZone.getY(), areaZone.getWidth(), areaZone.getHeight());
//            }
//            repaint();
//        } else {
//            double[] r = relative2Real(point);
//            selectionArea = new RectInfo(r[0], r[1], 0, 0);
//            oldPosition = new Point(point);
//            repaint();
//        }
//    }
//
//    public void doSelectAreasForMoveAt(Point point) {
//        if (motionAreas.size() == 0 && getAllSelectedAreas().size() > 0) {
//            Area[] all = getAllAreas(point);
//            boolean someSelected = false;
//            for (int i = 0; i < all.length; i++) {
//                if (getAllSelectedAreas().contains(all[i])) {
//                    someSelected = true;
//                    break;
//                }
//            }
//            if (someSelected) {
//                motionAreas.clear();
//                motionAreas.addAll(getSelection());
//            }
//        }
//    }
//
////    public void addImpedanceSurfaceAt(Point point) {
////        double[] r = relative2Real(point);
////        Area a = AreaManager.getCategoryDesc(AreaManager.ZS_CATEGORY)
////                .getDefaultAreaType().createInsance();
////        structureEditor.getZsList().addArea(a);
////        a.setX(r[0]);
////        a.setY(r[1]);
////        clearSelection();
////        selectZone(a);
////        motionAreas.clear();
////        motionAreas.add(a);
////        resizeType = Cursor.SE_RESIZE_CURSOR;
////        setCursor(Cursor.getPredefinedCursor(resizeType));
////    }
////
////    public void addSourceAt(Point point) {
////        double[] r = relative2Real(point);
////        Area a = AreaManager.getCategoryDesc(AreaManager.SOURCE_CATEGORY)
////                .getDefaultAreaType().createInsance();
////        structureEditor.getSourcesList().addArea(a);
////        a.setX(r[0]);
////        a.setY(r[1]);
////        clearSelection();
////        selectZone(a);
////        motionAreas.clear();
////        motionAreas.add(a);
////        resizeType = Cursor.SE_RESIZE_CURSOR;
////        setCursor(Cursor.getPredefinedCursor(resizeType));
////    }
//    public void addMetal(Area area) {
//        structureEditor.addArea(area);
//    }
//
//    public void selectedAreasDoDelete() {
//        ArrayList<Area> toRemove = new ArrayList<Area>(getAllSelectedAreas());
//        clearSelection();
//        for (Iterator iterator = toRemove.iterator(); iterator.hasNext();) {
//            Area area = (Area) iterator.next();
//            deleteArea(area);
//        }
//        cached_selectedAreas = null;
//    }
//
//    public void selectedAreasDoOpenEditors() {
//
//        Collection<Area> allSelectedAreas = getAllSelectedAreas();
//        if (allSelectedAreas.size() > 0) {
//            for (Area allSelectedArea : allSelectedAreas) {
//                Area area = (Area) allSelectedArea;
//                editArea(area);
//                return;//edit only one
//            }
//        } else {
//            editArea(structure.getBox());
//        }
//    }
//
//    public void selectedAreasDoSplit() {
//        Area[] areas = (Area[]) getAllSelectedAreas().toArray(new Area[getAllSelectedAreas().size()]);
//        for (int i = 0; i < areas.length; i++) {
//            splitMetal(areas[i]);
//        }
//    }
//
//    public void selectedAreasDoSplit(int nx, int ny) {
//        Area[] areas = (Area[]) getAllSelectedAreas().toArray(new Area[getAllSelectedAreas().size()]);
//        for (int i = 0; i < areas.length; i++) {
//            splitMetal(areas[i], nx, ny);
//        }
//    }
//
//    public void showSplitSelectedAreas() {
//        DataTypeEditor x = TypesFactory.forInt(new Integer(1), new Integer(10000), false).createComponent("Nbr cellules en X");
//        DataTypeEditor y = TypesFactory.forInt(new Integer(1), new Integer(10000), false).createComponent("Nbr cellules en Y");
//        EditComponentsPanel editComponentsPanel = new EditComponentsPanel(new DataTypeEditor[]{x, y}, 1, false);
//        int ret = JOptionPane.showConfirmDialog(this, editComponentsPanel, "Eclater ...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
//        if (ret != JOptionPane.OK_OPTION) {
//            return;
//        }
//        int nx = 0;
//        int ny = 0;
//        try {
//            nx = ((Integer) x.getObject()).intValue();
//            ny = ((Integer) y.getObject()).intValue();
//        } catch (ConstraintsException e) {
//            JOptionPane2.showErrorDialog(this, e);
//            return;
//        }
//        selectedAreasDoSplit(nx, ny);
//    }
//
//    public EditType getEditType() {
//        return editType;
//    }
//
//    public void setEditType(EditType editType) {
//        EditType old = this.editType;
//        this.editType = editType;
//        firePropertyChange("editType", old, editType);
//    }
//
//    public Collection<Area> getAllSelectedAreas() {
//        if (cached_selectedAreas != null) {
//            return cached_selectedAreas;
//        }
//        HashSet<Area> a = new HashSet<Area>();
//        for (AreaZone area : selectedZones) {
//            AreaGroup g = area.getTopLevelGroup();
//            if (g != null) {
//                a.addAll(Arrays.asList(g.getAllInnerAreas()));
//            } else if (area instanceof AreaGroup) {
//                a.addAll(Arrays.asList(((AreaGroup) area).getAllInnerAreas()));
//            } else if (area instanceof Area) {
//                a.add((Area) area);
//            }
//        }
//        cached_selectedAreas = a;
//        return cached_selectedAreas;
//    }
//
//    public void clearSelection() {
//        selectedZones.clear();
//        cached_selectedAreas = null;
//    }
//
//    public void selectZone(AreaZone z) {
//        structureEditor.setSelectedArea((Area) z);
//        AreaGroup g = z.getTopLevelGroup();
//        if (g != null) {
//            z = g;
//        }
//        selectedZones.add(z);
//        cached_selectedAreas = null;
//    }
//
//    public void deselectZone(AreaZone z) {
//        AreaGroup g = z.getTopLevelGroup();
//        if (g != null) {
//            z = g;
//        }
//        selectedZones.remove(z);
//        cached_selectedAreas = null;
//    }
//
//    public Collection<AreaZone> getSelection() {
//        return selectedZones;
//    }
//
//    public boolean isSelectedArea(Area a) {
//        return getAllSelectedAreas().contains(a);
//    }
//
//    public boolean isSelectedZone(AreaZone z) {
//        return selectedZones.contains(z);
//    }
//}
