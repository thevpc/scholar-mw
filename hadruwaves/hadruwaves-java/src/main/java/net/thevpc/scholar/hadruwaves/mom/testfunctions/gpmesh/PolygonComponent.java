package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.geom.DefaultGeometryList;
import net.thevpc.scholar.hadrumaths.geom.GeometryList;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.EchelonPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.thevpc.scholar.hadrumaths.DomainScaleTool;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

/**
 * @author : vpc
 * @creationtime 18 janv. 2006 14:47:42
 */
public class PolygonComponent extends JComponent {
    public static void main(String[] args) {
        GeometryList geometryList = new DefaultGeometryList();
        Domain globalDomain = Domain.ofBounds(0, 100, 0, 100);
        geometryList.add(globalDomain.toPolygon());
        MeshConsDesAlgo meshalgo = new MeshConsDesAlgo(3);
        EchelonPattern pattern = new EchelonPattern();


        PolygonListMeshInfo r = new PolygonListMeshInfo(geometryList, globalDomain, meshalgo, pattern);
        System.out.println(r.meshZones);

        JPanel p = new JPanel(new BorderLayout());
        p.add(new PolygonComponent(
                geometryList, meshalgo, pattern, globalDomain
        ));
        p.setSize(new java.awt.Dimension(400, 400));
        JFrame f = new JFrame();
        f.add(p);
        f.setVisible(true);
    }

    private double zoomX = 0;
    private double zoomY = 0;
    private double translateX = 0;
    private double translateY = 0;
    private double rotateX = 0;
    private Color polygonForegroundColor = Color.YELLOW;
    private Color polygonEdgeColor = Color.BLACK;
    private Color gridForegroundColor = Color.BLUE;
    private Color gridMeshMaxColor = Color.DARK_GRAY;
    private Color gridMeshMinColor = Color.GRAY;
    private Color gridEdgeColor = Color.RED;
    private AffineTransform transform;
    private boolean drawMain = true;
    private boolean drawAttachX = true;
    private boolean drawAttachY = true;
    private boolean drawAttachNorth = true;
    private boolean drawAttachSouth = true;
    private boolean drawAttachEast = true;
    private boolean drawAttachWest = true;
    private PolygonListMeshInfo meshInfo;

    public PolygonComponent(PolygonListMeshInfo meshInfo) {
        this.meshInfo = meshInfo;
        rebuild();
    }

    public PolygonComponent(GeometryList geometryList, MeshAlgo meshalgo, GpPattern pattern, Domain globalDomain) {
        meshInfo = new PolygonListMeshInfo(geometryList, globalDomain, meshalgo, pattern);
        rebuild();
    }


    public void paint(Graphics g) {
        super.paint(g);
        Domain to = meshInfo.to(getBounds());
//        DomainScaleTool ds=DomainScaleTool.createIdentity();
        DomainScaleTool ds = DomainScaleTool.create(meshInfo.from, to);

//        GeometryList polygonListForPaint=new DefaultGeometryList();
//
//        Domain from = polygons.getBounds() == null ? globalDomain : polygons.getBounds();
//        Domain to=null;
//        int precision=100;
//        Rectangle currBounds = getBounds();
//        if(currBounds.width<currBounds.height){
//            precision=currBounds.width-3;
//        }else{
//            precision=currBounds.height-3;
//        }
//        if(from.xwidth<=from.ywidth){
//            to=Domain.forBounds(0,precision *from.xwidth/from.ywidth,0,precision);
//        }else{
//            to=Domain.forBounds(0,precision,0,precision*from.ywidth/from.xwidth);
//        }
//        DomainScaleTool ds=DomainScaleTool.create(from,to);
//        synchronized (polygons) {
//            for (Polygon polygon : polygons) {
//                polygonListForPaint.add(ds.rescale(polygon));
//            }
//        }
////        Domain b = polygons.getBounds();
////        if(b==null){
////            b=globalDomain;
////        }
//        Collection<MeshZone> meshZones = new ArrayList<MeshZone>();
//        for (Polygon polygon : polygonListForPaint) {
//            meshZones.addAll(meshalgo.meshPolygon(polygon));
//        }
//        meshZones = pattern.eval(meshZones, to);

        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.RED);
        g2d.drawRect((int) to.xmin(), (int) to.ymin(), (int) to.xwidth(), (int) to.ywidth());
        if (transform != null) {
            g2d.setTransform(transform);
        }
        for (Geometry polygon : meshInfo.polygons) {
            if (polygonForegroundColor != null) {
                Area area2 = new Area(ds.rescale(polygon).getPath());;
                g.setColor(polygonForegroundColor);
                g2d.fill(area2);
            }
        }
        for (MeshZone a : meshInfo.meshZones) {
            if (!a.isEnabled()) {
                continue;
            }
            if (gridForegroundColor != null) {
                Composite oldComposite = g2d.getComposite();
                int typeId = a.getType().getValue();
//                    Area area =a.getGeometry();//ds.rescale(a.getGeometry());
                Area area = new Area(ds.rescale(a.getGeometry()).getPath());
                switch (typeId) {
                    case MeshZoneType.ID_MAIN: {
                        if (drawMain) {
                            g.setColor(gridForegroundColor);
                            g2d.fill(area);
                        }
                        break;
                    }
                    case MeshZoneType.ID_BORDER_EAST:
                    case MeshZoneType.ID_BORDER_WEST:
                    case MeshZoneType.ID_ATTACHX: {
                        if (
                                (drawAttachEast && typeId == MeshZoneType.ID_BORDER_EAST)
                                        || (drawAttachWest && typeId == MeshZoneType.ID_BORDER_WEST)
                                        || (drawAttachX && typeId == MeshZoneType.ID_ATTACHX)
                                ) {
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                            Rectangle rectangle = area.getBounds();
                            g2d.setPaint(new GradientPaint(
                                    (float) rectangle.getMinX(),
                                    (float) (rectangle.getMinY() + rectangle.getHeight() / 2.0),
                                    Color.MAGENTA,
                                    (float) rectangle.getMaxX(),
                                    (float) (rectangle.getMinY() + rectangle.getHeight() / 2.0),
                                    Color.WHITE
                            ));
                            g2d.fill(area);
                            g2d.setComposite(oldComposite);
                            int lines = 7;
                            g2d.setColor(Color.BLACK);
                            for (int i = 0; i <= lines; i++) {
                                g2d.drawLine(
                                        (int) (rectangle.getMinX()),
                                        (int) (rectangle.getMinY() + i * rectangle.getHeight() / lines),
                                        (int) (rectangle.getMaxX()),
                                        (int) (rectangle.getMinY() + i * rectangle.getHeight() / lines)
                                );
                                if (typeId == MeshZoneType.ID_ATTACHX || typeId == MeshZoneType.ID_BORDER_WEST) {
                                    g2d.drawOval(
                                            (int) (rectangle.getMinX() - 1),
                                            (int) (-2 + rectangle.getMinY() + i * rectangle.getHeight() / lines),
                                            3,
                                            3
                                    );
                                }
                                if (typeId == MeshZoneType.ID_ATTACHX || typeId == MeshZoneType.ID_BORDER_EAST) {
                                    g2d.drawOval(
                                            (int) (rectangle.getMaxX() - 1),
                                            (int) (-2 + rectangle.getMinY() + i * rectangle.getHeight() / lines),
                                            3,
                                            3
                                    );
                                }
                            }
                        }
                        break;
                    }
                    case MeshZoneType.ID_BORDER_NORTH:
                    case MeshZoneType.ID_BORDER_SOUTH:
                    case MeshZoneType.ID_ATTACHY: {
                        if (
                                (drawAttachNorth && typeId == MeshZoneType.ID_BORDER_NORTH)
                                        || (drawAttachSouth && typeId == MeshZoneType.ID_BORDER_SOUTH)
                                        || (drawAttachY && typeId == MeshZoneType.ID_ATTACHY)
                                ) {
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                            Rectangle rectangle = area.getBounds();
                            g2d.setPaint(new GradientPaint(
                                    (float) (rectangle.getMinX() + rectangle.getWidth() / 2.0),
                                    (float) rectangle.getMinY(),
                                    Color.MAGENTA,
                                    (float) (rectangle.getMinX() + rectangle.getHeight() / 2.0),
                                    (float) rectangle.getMaxY(),
                                    Color.WHITE
                            ));
                            g2d.fill(area);
                            g2d.setComposite(oldComposite);
                            int lines = 7;
                            g2d.setColor(Color.BLACK);
                            for (int i = 0; i <= lines; i++) {
                                g2d.drawLine(
                                        (int) (rectangle.getMinX() + i * rectangle.getWidth() / lines),
                                        (int) (rectangle.getMinY()),
                                        (int) (rectangle.getMinX() + i * rectangle.getWidth() / lines),
                                        (int) (rectangle.getMaxY())
                                );
                                if (typeId == MeshZoneType.ID_ATTACHY || typeId == MeshZoneType.ID_BORDER_NORTH) {
                                    g2d.drawOval(
                                            (int) (-2 + rectangle.getMinX() + i * rectangle.getWidth() / lines),
                                            (int) (rectangle.getMinY() - 1),
                                            3,
                                            3
                                    );
                                }
                                if (typeId == MeshZoneType.ID_ATTACHY || typeId == MeshZoneType.ID_BORDER_SOUTH) {
                                    g2d.drawOval(
                                            (int) (-2 + rectangle.getMinX() + i * rectangle.getWidth() / lines),
                                            (int) (rectangle.getMaxY() - 1),
                                            3,
                                            3
                                    );
                                }
                            }
                        }
                        break;
                    }
                }
                g2d.setComposite(oldComposite);
            }
        }
        for (Geometry polygon : meshInfo.polygons) {
            Area area2 = new Area(ds.rescale(polygon).getPath());
            Domain dom = polygon.getDomain();
            MeshAlgoRect _meshalgo = null;
            if (meshInfo.meshalgo instanceof MeshAlgoRect) {
                _meshalgo = (MeshAlgoRect) meshInfo.meshalgo;
                double maxAbsoluteSizeX = _meshalgo.getMaxRelativeSizeX() * dom.xwidth();
                double maxAbsoluteSizeY = _meshalgo.getMaxRelativeSizeY() * dom.ywidth();

                double minAbsoluteSizeX = _meshalgo.getMinRelativeSizeX() * dom.xwidth();
                double minAbsoluteSizeY = _meshalgo.getMinRelativeSizeY() * dom.ywidth();
                if (gridMeshMinColor != null) {
                    int maxx = ((int) Math.round(dom.xwidth() / minAbsoluteSizeX));
                    int maxy = ((int) Math.round(dom.ywidth() / minAbsoluteSizeY));
                    g.setColor(gridMeshMinColor);
                    for (int i = 0; i <= maxx; i++) {
                        g.drawLine(
                                (int) (dom.xmin ()+ (i * minAbsoluteSizeX)),
                                (int) (dom.ymin()),
                                (int) (dom.xmin ()+ (i * minAbsoluteSizeX)),
                                (int) (dom.ymax())
                        );
                    }
                    for (int i = 0; i <= maxy; i++) {
                        g.drawLine(
                                (int) (dom.xmin()),
                                (int) (dom.ymin ()+ (i * minAbsoluteSizeY)),
                                (int) (dom.xmax()),
                                (int) (dom.ymin ()+ (i * minAbsoluteSizeY))
                        );
                    }
                }
                if (gridMeshMaxColor != null) {
                    int maxx = ((int) Math.round(dom.xwidth() / maxAbsoluteSizeX));
                    int maxy = ((int) Math.round(dom.ywidth() / maxAbsoluteSizeY));
                    g.setColor(gridMeshMaxColor);
                    for (int i = 0; i <= maxx; i++) {
                        g.drawLine(
                                (int) (dom.xmin ()+ (i * maxAbsoluteSizeX)),
                                (int) (dom.ymin()),
                                (int) (dom.xmin ()+ (i * maxAbsoluteSizeX)),
                                (int) (dom.ymax())
                        );
                    }
                    for (int i = 0; i <= maxy; i++) {
                        g.drawLine(
                                (int) (dom.xmin()),
                                (int) (dom.ymin ()+ (i * maxAbsoluteSizeY)),
                                (int) (dom.xmax()),
                                (int) (dom.ymin ()+ (i * maxAbsoluteSizeY))
                        );
                    }
                }
            }

            if (polygonEdgeColor != null) {
                g.setColor(polygonEdgeColor);
                g2d.draw(area2);
            }
        }

        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
        for (MeshZone a : meshInfo.meshZones) {
            if (!a.isEnabled()) {
                continue;
            }
            Area area = new Area(ds.rescale(a.getGeometry()).getPath());
            int typeId = a.getType().getValue();
            if (
                    (drawAttachNorth && typeId == MeshZoneType.ID_BORDER_NORTH)
                            || (drawAttachSouth && typeId == MeshZoneType.ID_BORDER_SOUTH)
                            || (drawAttachY && typeId == MeshZoneType.ID_ATTACHY)
                            || (drawAttachEast && typeId == MeshZoneType.ID_BORDER_EAST)
                            || (drawAttachWest && typeId == MeshZoneType.ID_BORDER_WEST)
                            || (drawAttachX && typeId == MeshZoneType.ID_ATTACHX)
                            || (drawMain && typeId == MeshZoneType.ID_MAIN)
                    ) {
                if (gridEdgeColor != null) {
                    switch (a.getType().getValue()) {
                        case MeshZoneType.ID_MAIN: {
                            g.setColor(gridEdgeColor);
                            g2d.draw(area);
                            break;
                        }
                        case MeshZoneType.ID_ATTACHX: {
                            g.setColor(gridEdgeColor);
                            g2d.draw(area);
                            break;
                        }
                        case MeshZoneType.ID_ATTACHY: {
                            g.setColor(gridEdgeColor);
                            g2d.draw(area);
                            break;
                        }
                    }
                }
            }
        }
        g2d.setStroke(oldStroke);


    }

    public PolygonListMeshInfo getMeshInfo() {
        return meshInfo;
    }

    public void setMeshInfo(PolygonListMeshInfo meshInfo) {
        this.meshInfo = meshInfo;
        rebuild();
        repaint();
    }
    //    public void paint2(Graphics g) {
//        super.paint(g);
//        GeometryList polygons=new DefaultGeometryList();
//
//        Domain from = polygons.getBounds() == null ? globalDomain : polygons.getBounds();
//        Domain to=null;
//        int precision=100;
//        Rectangle currBounds = getBounds();
//        if(currBounds.width<currBounds.height){
//            precision=currBounds.width-3;
//        }else{
//            precision=currBounds.height-3;
//        }
//        if(from.xwidth<=from.ywidth){
//            to=Domain.forBounds(0,precision *from.xwidth/from.ywidth,0,precision);
//        }else{
//            to=Domain.forBounds(0,precision,0,precision*from.ywidth/from.xwidth);
//        }
//        DomainScaleTool ds=DomainScaleTool.create(from,to);
//        synchronized (polygons) {
//            for (Polygon polygon : polygons) {
//                polygons.add(ds.rescale(polygon));
//            }
//        }
//        Graphics2D g2d = (Graphics2D) g;
//        g.setColor(Color.RED);
////        Domain b = polygons.getBounds();
////        if(b==null){
////            b=globalDomain;
////        }
//        g2d.drawRect((int) to.xmin, (int) to.ymin, (int) to.xwidth, (int) to.ywidth);
//        Collection<MeshZone> meshZones = new ArrayList<MeshZone>();
//        for (Polygon polygon : polygons) {
//            meshZones.addAll(meshalgo.meshPolygon(polygon));
//        }
//        meshZones = pattern.eval(meshZones, to);
//        synchronized (polygons) {
//            if (eval != null) {
//                g2d.setTransform(eval);
//            }
//            for (Polygon polygon : polygons) {
//                Area area2 = polygon.toArea();
//                if (polygonForegroundColor != null) {
//                    g.setColor(polygonForegroundColor);
//                    g2d.fill(area2);
//                }
//            }
//            for (MeshZone a : meshZones) {
//                if (gridForegroundColor != null) {
//                    Composite oldComposite = g2d.getComposite();
//                    int typeId = a.getType().getValue();
//                    switch (typeId) {
//                        case MeshZoneType.ID_MAIN: {
//                            if (drawMain) {
//                                g.setColor(gridForegroundColor);
//                                g2d.fill(a.getGeometry());
//                            }
//                            break;
//                        }
//                        case MeshZoneType.ID_BORDER_EAST:
//                        case MeshZoneType.ID_BORDER_WEST:
//                        case MeshZoneType.ID_ATTACHX: {
//                            if (
//                                    (drawAttachEast && typeId == MeshZoneType.ID_BORDER_EAST)
//                                    ||(drawAttachWest && typeId == MeshZoneType.ID_BORDER_WEST)
//                                    ||(drawAttachX && typeId == MeshZoneType.ID_ATTACHX)
//                               ) {
//                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
//                                Rectangle rectangle = a.getGeometry().getBounds();
//                                g2d.setPaint(new GradientPaint(
//                                        (float) rectangle.getMinX(),
//                                        (float) (rectangle.getMinY() + rectangle.getHeight() / 2.0),
//                                        Color.MAGENTA,
//                                        (float) rectangle.getMaxX(),
//                                        (float) (rectangle.getMinY() + rectangle.getHeight() / 2.0),
//                                        Color.WHITE
//                                ));
//                                g2d.fill(a.getGeometry());
//                                g2d.setComposite(oldComposite);
//                                int lines = 7;
//                                g2d.setColor(Color.BLACK);
//                                for (int i = 0; i <= lines; i++) {
//                                    g2d.drawLine(
//                                            (int) (rectangle.getMinX()),
//                                            (int) (rectangle.getMinY() + i * rectangle.getHeight() / lines),
//                                            (int) (rectangle.getMaxX()),
//                                            (int) (rectangle.getMinY() + i * rectangle.getHeight() / lines)
//                                    );
//                                    if (typeId == MeshZoneType.ID_ATTACHX || typeId == MeshZoneType.ID_BORDER_WEST) {
//                                        g2d.drawOval(
//                                                (int) (rectangle.getMinX() - 1),
//                                                (int) (-2 + rectangle.getMinY() + i * rectangle.getHeight() / lines),
//                                                3,
//                                                3
//                                        );
//                                    }
//                                    if (typeId == MeshZoneType.ID_ATTACHX || typeId == MeshZoneType.ID_BORDER_EAST) {
//                                        g2d.drawOval(
//                                                (int) (rectangle.getMaxX() - 1),
//                                                (int) (-2 + rectangle.getMinY() + i * rectangle.getHeight() / lines),
//                                                3,
//                                                3
//                                        );
//                                    }
//                                }
//                            }
//                            break;
//                        }
//                        case MeshZoneType.ID_BORDER_NORTH:
//                        case MeshZoneType.ID_BORDER_SOUTH:
//                        case MeshZoneType.ID_ATTACHY: {
//                            if (
//                                    (drawAttachNorth && typeId == MeshZoneType.ID_BORDER_NORTH)
//                                    ||(drawAttachSouth && typeId == MeshZoneType.ID_BORDER_SOUTH)
//                                    ||(drawAttachY && typeId == MeshZoneType.ID_ATTACHY)
//                               ) {
//                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
//                            Rectangle rectangle = a.getGeometry().getBounds();
//                            g2d.setPaint(new GradientPaint(
//                                    (float) (rectangle.getMinX() + rectangle.getWidth() / 2.0),
//                                    (float) rectangle.getMinY(),
//                                    Color.MAGENTA,
//                                    (float) (rectangle.getMinX() + rectangle.getHeight() / 2.0),
//                                    (float) rectangle.getMaxY(),
//                                    Color.WHITE
//                            ));
//                            g2d.fill(a.getGeometry());
//                            g2d.setComposite(oldComposite);
//                            int lines = 7;
//                            g2d.setColor(Color.BLACK);
//                            for (int i = 0; i <= lines; i++) {
//                                g2d.drawLine(
//                                        (int) (rectangle.getMinX() + i * rectangle.getWidth() / lines),
//                                        (int) (rectangle.getMinY()),
//                                        (int) (rectangle.getMinX() + i * rectangle.getWidth() / lines),
//                                        (int) (rectangle.getMaxY())
//                                );
//                                if (typeId == MeshZoneType.ID_ATTACHY || typeId == MeshZoneType.ID_BORDER_NORTH) {
//                                    g2d.drawOval(
//                                            (int) (-2 + rectangle.getMinX() + i * rectangle.getWidth() / lines),
//                                            (int) (rectangle.getMinY() - 1),
//                                            3,
//                                            3
//                                    );
//                                }
//                                if (typeId == MeshZoneType.ID_ATTACHY || typeId == MeshZoneType.ID_BORDER_SOUTH) {
//                                    g2d.drawOval(
//                                            (int) (-2 + rectangle.getMinX() + i * rectangle.getWidth() / lines),
//                                            (int) (rectangle.getMaxY() - 1),
//                                            3,
//                                            3
//                                    );
//                                }
//                            }
//                            }
//                            break;
//                        }
//                    }
//                    g2d.setComposite(oldComposite);
//                }
//            }
//            for (Polygon polygon : polygons) {
//                Area area2 = polygon.toArea();
//                Domain dom = polygon.getDomain();
//                MeshAlgoRect _meshalgo = null;
//                if (meshalgo instanceof MeshAlgoRect) {
//                    _meshalgo = (MeshAlgoRect) meshalgo;
//                    double maxAbsoluteSizeX = _meshalgo.getMaxRelativeSizeX() * dom.xwidth;
//                    double maxAbsoluteSizeY = _meshalgo.getMaxRelativeSizeY() * dom.ywidth;
//
//                    double minAbsoluteSizeX = _meshalgo.getMinRelativeSizeX() * dom.xwidth;
//                    double minAbsoluteSizeY = _meshalgo.getMinRelativeSizeY() * dom.ywidth;
//                    if (gridMeshMinColor != null) {
//                        int maxx = ((int) Math.round(dom.xwidth / minAbsoluteSizeX));
//                        int maxy = ((int) Math.round(dom.ywidth / minAbsoluteSizeY));
//                        g.setColor(gridMeshMinColor);
//                        for (int i = 0; i <= maxx; i++) {
//                            g.drawLine(
//                                    (int) (dom.xmin + (i * minAbsoluteSizeX)),
//                                    (int) (dom.ymin),
//                                    (int) (dom.xmin + (i * minAbsoluteSizeX)),
//                                    (int) (dom.ymax)
//                            );
//                        }
//                        for (int i = 0; i <= maxy; i++) {
//                            g.drawLine(
//                                    (int) (dom.xmin),
//                                    (int) (dom.ymin + (i * minAbsoluteSizeY)),
//                                    (int) (dom.xmax),
//                                    (int) (dom.ymin + (i * minAbsoluteSizeY))
//                            );
//                        }
//                    }
//                    if (gridMeshMaxColor != null) {
//                        int maxx = ((int) Math.round(dom.xwidth / maxAbsoluteSizeX));
//                        int maxy = ((int) Math.round(dom.ywidth / maxAbsoluteSizeY));
//                        g.setColor(gridMeshMaxColor);
//                        for (int i = 0; i <= maxx; i++) {
//                            g.drawLine(
//                                    (int) (dom.xmin + (i * maxAbsoluteSizeX)),
//                                    (int) (dom.ymin),
//                                    (int) (dom.xmin + (i * maxAbsoluteSizeX)),
//                                    (int) (dom.ymax)
//                            );
//                        }
//                        for (int i = 0; i <= maxy; i++) {
//                            g.drawLine(
//                                    (int) (dom.xmin),
//                                    (int) (dom.ymin + (i * maxAbsoluteSizeY)),
//                                    (int) (dom.xmax),
//                                    (int) (dom.ymin + (i * maxAbsoluteSizeY))
//                            );
//                        }
//                    }
//                }
//
//                if (polygonEdgeColor != null) {
//                    g.setColor(polygonEdgeColor);
//                    g2d.draw(area2);
//                }
//            }
//
//            Stroke oldStroke = g2d.getStroke();
//            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
//            for (MeshZone a : meshZones) {
//                int typeId=a.getType().getValue();
//                if (
//                        (drawAttachNorth && typeId == MeshZoneType.ID_BORDER_NORTH)
//                        ||(drawAttachSouth && typeId == MeshZoneType.ID_BORDER_SOUTH)
//                        ||(drawAttachY && typeId == MeshZoneType.ID_ATTACHY)
//                        ||(drawAttachEast && typeId == MeshZoneType.ID_BORDER_EAST)
//                        ||(drawAttachWest && typeId == MeshZoneType.ID_BORDER_WEST)
//                        ||(drawAttachX && typeId == MeshZoneType.ID_ATTACHX)
//                        ||(drawMain && typeId == MeshZoneType.ID_MAIN)
//                   ) {
//                if (gridEdgeColor != null) {
//                    switch (a.getType().getValue()) {
//                        case MeshZoneType.ID_MAIN: {
//                            g.setColor(gridEdgeColor);
//                            g2d.draw(a.getGeometry());
//                            break;
//                        }
//                        case MeshZoneType.ID_ATTACHX: {
////                                g.setColor(gridEdgeColor);
////                                g2d.draw(a.getGeometry());
//                            break;
//                        }
//                        case MeshZoneType.ID_ATTACHY: {
////                                g.setColor(gridEdgeColor);
////                                g2d.draw(a.getGeometry());
//                            break;
//                        }
//                    }
//                }
//                }
//            }
//            g2d.setStroke(oldStroke);
//
//        }
//    }

//    public void paint_old(Graphics g) {
//        super.paint(g);
//        Graphics2D g2d = (Graphics2D) g;
//        g.setColor(Color.RED);
//        Domain b = polygons.getBounds();
//        g2d.drawRect((int) b.xmin, (int) b.ymin, (int) b.xwidth, (int) b.ywidth);
//        synchronized (polygons) {
//            for (Polygon polygon : polygons) {
//                if (eval != null) {
//                    g2d.setTransform(eval);
//                }
//                Area area2 = polygon.toArea();
//                if (polygonForegroundColor != null) {
//                    g.setColor(polygonForegroundColor);
//                    g2d.fill(area2);
//                }
//                Collection<MeshZone> adaptativeAreas = meshalgo.meshPolygon(polygon);
//                for (MeshZone a : adaptativeAreas) {
//                    if (gridForegroundColor != null) {
//                        Composite oldComposite = g2d.getComposite();
//                        switch (a.getType().getValue()) {
//                            case MeshZoneType.ID_MAIN: {
//                                g.setColor(gridForegroundColor);
//                                g2d.fill(a.getGeometry());
//                                break;
//                            }
//                            case MeshZoneType.ID_ATTACHX: {
//                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
//                                Rectangle rectangle = a.getGeometry().getBounds();
//                                g2d.setPaint(new GradientPaint(
//                                        (float) rectangle.getMinX(),
//                                        (float) (rectangle.getMinY() + rectangle.getHeight() / 2.0),
//                                        Color.MAGENTA,
//                                        (float) rectangle.getMaxX(),
//                                        (float) (rectangle.getMinY() + rectangle.getHeight() / 2.0),
//                                        Color.WHITE
//                                ));
//                                g2d.fill(a.getGeometry());
//                                g2d.setComposite(oldComposite);
//                                int lines = 7;
//                                g2d.setColor(Color.BLACK);
//                                for (int i = 0; i <= lines; i++) {
//                                    g2d.drawLine(
//                                            (int) (rectangle.getMinX()),
//                                            (int) (rectangle.getMinY() + i * rectangle.getHeight() / lines),
//                                            (int) (rectangle.getMaxX()),
//                                            (int) (rectangle.getMinY() + i * rectangle.getHeight() / lines)
//                                    );
//                                    g2d.drawOval(
//                                            (int) (rectangle.getMinX() - 1),
//                                            (int) (-2 + rectangle.getMinY() + i * rectangle.getHeight() / lines),
//                                            3,
//                                            3
//                                    );
//                                    g2d.drawOval(
//                                            (int) (rectangle.getMaxX() - 1),
//                                            (int) (-2 + rectangle.getMinY() + i * rectangle.getHeight() / lines),
//                                            3,
//                                            3
//                                    );
//                                }
//                                break;
//                            }
//                            case MeshZoneType.ID_ATTACHY: {
//                                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
//                                Rectangle rectangle = a.getGeometry().getBounds();
//                                g2d.setPaint(new GradientPaint(
//                                        (float) (rectangle.getMinX() + rectangle.getWidth() / 2.0),
//                                        (float) rectangle.getMinY(),
//                                        Color.MAGENTA,
//                                        (float) (rectangle.getMinX() + rectangle.getHeight() / 2.0),
//                                        (float) rectangle.getMaxY(),
//                                        Color.WHITE
//                                ));
//                                g2d.fill(a.getGeometry());
//                                g2d.setComposite(oldComposite);
//                                int lines = 7;
//                                g2d.setColor(Color.BLACK);
//                                for (int i = 0; i <= lines; i++) {
//                                    g2d.drawLine(
//                                            (int) (rectangle.getMinX() + i * rectangle.getWidth() / lines),
//                                            (int) (rectangle.getMinY()),
//                                            (int) (rectangle.getMinX() + i * rectangle.getWidth() / lines),
//                                            (int) (rectangle.getMaxY())
//                                    );
//                                    g2d.drawOval(
//                                            (int) (-2 + rectangle.getMinX() + i * rectangle.getWidth() / lines),
//                                            (int) (rectangle.getMinY() - 1),
//                                            3,
//                                            3
//                                    );
//                                    g2d.drawOval(
//                                            (int) (-2 + rectangle.getMinX() + i * rectangle.getWidth() / lines),
//                                            (int) (rectangle.getMaxY() - 1),
//                                            3,
//                                            3
//                                    );
//                                }
//                                break;
//                            }
//                        }
//                        g2d.setComposite(oldComposite);
//                    }
//                }
//                Domain dom = polygon.getDomain();
//                MeshAlgoRect _meshalgo = null;
//                if (meshalgo instanceof MeshAlgoRect) {
//                    _meshalgo = (MeshAlgoRect) meshalgo;
//                    double maxAbsoluteSizeX = _meshalgo.getMaxRelativeSizeX() * dom.xwidth;
//                    double maxAbsoluteSizeY = _meshalgo.getMaxRelativeSizeY() * dom.ywidth;
//
//                    double minAbsoluteSizeX = _meshalgo.getMinRelativeSizeX() * dom.xwidth;
//                    double minAbsoluteSizeY = _meshalgo.getMinRelativeSizeY() * dom.ywidth;
//                    if (gridMeshMinColor != null) {
//                        int maxx = ((int) Math.round(dom.xwidth / minAbsoluteSizeX));
//                        int maxy = ((int) Math.round(dom.ywidth / minAbsoluteSizeY));
//                        g.setColor(gridMeshMinColor);
//                        for (int i = 0; i <= maxx; i++) {
//                            g.drawLine(
//                                    (int) (dom.xmin + (i * minAbsoluteSizeX)),
//                                    (int) (dom.ymin),
//                                    (int) (dom.xmin + (i * minAbsoluteSizeX)),
//                                    (int) (dom.ymax)
//                            );
//                        }
//                        for (int i = 0; i <= maxy; i++) {
//                            g.drawLine(
//                                    (int) (dom.xmin),
//                                    (int) (dom.ymin + (i * minAbsoluteSizeY)),
//                                    (int) (dom.xmax),
//                                    (int) (dom.ymin + (i * minAbsoluteSizeY))
//                            );
//                        }
//                    }
//                    if (gridMeshMaxColor != null) {
//                        int maxx = ((int) Math.round(dom.xwidth / maxAbsoluteSizeX));
//                        int maxy = ((int) Math.round(dom.ywidth / maxAbsoluteSizeY));
//                        g.setColor(gridMeshMaxColor);
//                        for (int i = 0; i <= maxx; i++) {
//                            g.drawLine(
//                                    (int) (dom.xmin + (i * maxAbsoluteSizeX)),
//                                    (int) (dom.ymin),
//                                    (int) (dom.xmin + (i * maxAbsoluteSizeX)),
//                                    (int) (dom.ymax)
//                            );
//                        }
//                        for (int i = 0; i <= maxy; i++) {
//                            g.drawLine(
//                                    (int) (dom.xmin),
//                                    (int) (dom.ymin + (i * maxAbsoluteSizeY)),
//                                    (int) (dom.xmax),
//                                    (int) (dom.ymin + (i * maxAbsoluteSizeY))
//                            );
//                        }
//                    }
//                }
//
//                if (polygonEdgeColor != null) {
//                    g.setColor(polygonEdgeColor);
//                    g2d.draw(area2);
//                }
//                Stroke oldStroke = g2d.getStroke();
//                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
//                for (MeshZone a : adaptativeAreas) {
//                    if (gridEdgeColor != null) {
//                        switch (a.getType().getValue()) {
//                            case MeshZoneType.ID_MAIN: {
//                                g.setColor(gridEdgeColor);
//                                g2d.draw(a.getGeometry());
//                                break;
//                            }
//                            case MeshZoneType.ID_ATTACHX: {
////                                g.setColor(gridEdgeColor);
////                                g2d.draw(a.getGeometry());
//                                break;
//                            }
//                            case MeshZoneType.ID_ATTACHY: {
////                                g.setColor(gridEdgeColor);
////                                g2d.draw(a.getGeometry());
//                                break;
//                            }
//                        }
//                    }
//                }
//                g2d.setStroke(oldStroke);
//
//
//            }
//        }
//    }

    public Geometry getPolygon(int index) {
        return meshInfo.polygons.get(index);
    }

    public java.util.List<MeshZone> getMeshZones() {
        return meshInfo.meshZones;
    }

    public GeometryList getPolygonList() {
        return meshInfo.polygons;
    }

    public void updateMesh() {
        meshInfo.rebuild();
        rebuild();
        repaint();
    }

    public void setPolygonList(GeometryList geometryList) {
        meshInfo = new PolygonListMeshInfo(geometryList, meshInfo.from, meshInfo.meshalgo, meshInfo.pattern);
        rebuild();
        repaint();
    }

    public Color getPolygonForegroundColor() {
        return polygonForegroundColor;
    }

    public void setPolygonForegroundColor(Color polygonForegroundColor) {
        this.polygonForegroundColor = polygonForegroundColor;
        rebuild();
        repaint();
    }

    public Color getPolygonEdgeColor() {
        return polygonEdgeColor;
    }

    public void setPolygonEdgeColor(Color polygonEdgeColor) {
        this.polygonEdgeColor = polygonEdgeColor;
        rebuild();
        repaint();
    }

    public Color getGridForegroundColor() {
        return gridForegroundColor;
    }

    public void setGridForegroundColor(Color gridForegroundColor) {
        this.gridForegroundColor = gridForegroundColor;
        rebuild();
        repaint();
    }

    public Color getGridEdgeColor() {
        return gridEdgeColor;
    }

    public void setGridEdgeColor(Color gridEdgeColor) {
        this.gridEdgeColor = gridEdgeColor;
        rebuild();
        repaint();
    }

    public double getZoomX() {
        return zoomX;
    }

    public void setZoomX(double zoomX) {
        this.zoomX = zoomX;
        rebuild();
        repaint();
    }

    public double getZoomY() {
        return zoomY;
    }

    public void setZoomY(double zoomY) {
        this.zoomY = zoomY;
        rebuild();
        repaint();
    }

    public double getTranslateX() {
        return translateX;
    }

    public void setTranslateX(double translateX) {
        this.translateX = translateX;
        rebuild();
        repaint();
    }

    public double getTranslateY() {
        return translateY;
    }

    public void setTranslateY(double translateY) {
        this.translateY = translateY;
        rebuild();
        repaint();
    }

    public double getRotateX() {
        return rotateX;
    }

    public void setRotateX(double rotateX) {
        this.rotateX = rotateX;
        rebuild();
        repaint();
    }

    private void rebuild() {
        transform = null;
        if (translateX != 0 || translateY != 0) {
//            if(eval==null){
            transform = AffineTransform.getTranslateInstance(translateX, translateY);
//            }else{
//                eval.translate(translateX,translateY);
//            }
        }
        if (rotateX != 0) {
            if (transform == null) {
                transform = AffineTransform.getRotateInstance(rotateX);
            } else {
                transform.rotate(rotateX);
            }
        }
        if (zoomX > 0 || zoomY > 0) {
            transform = AffineTransform.getScaleInstance(zoomX == 0 ? 1 : zoomX, zoomY == 0 ? 1 : zoomY);
        }
    }

    public Color getGridMeshMaxColor() {
        return gridMeshMaxColor;
    }

    public void setGridMeshMaxColor(Color gridMeshMaxColor) {
        this.gridMeshMaxColor = gridMeshMaxColor;
    }

    public Color getGridMeshMinColor() {
        return gridMeshMinColor;
    }

    public void setGridMeshMinColor(Color gridMeshMinColor) {
        this.gridMeshMinColor = gridMeshMinColor;
    }

    public boolean isDrawMain() {
        return drawMain;
    }

    public void setDrawMain(boolean drawMain) {
        this.drawMain = drawMain;
    }

    public boolean isDrawAttachX() {
        return drawAttachX;
    }

    public void setDrawAttachX(boolean drawAttachX) {
        this.drawAttachX = drawAttachX;
    }

    public boolean isDrawAttachY() {
        return drawAttachY;
    }

    public void setDrawAttachY(boolean drawAttachY) {
        this.drawAttachY = drawAttachY;
    }

    public boolean isDrawAttachNorth() {
        return drawAttachNorth;
    }

    public void setDrawAttachNorth(boolean drawAttachNorth) {
        this.drawAttachNorth = drawAttachNorth;
    }

    public boolean isDrawAttachSouth() {
        return drawAttachSouth;
    }

    public void setDrawAttachSouth(boolean drawAttachSouth) {
        this.drawAttachSouth = drawAttachSouth;
    }

    public boolean isDrawAttachEast() {
        return drawAttachEast;
    }

    public void setDrawAttachEast(boolean drawAttachEast) {
        this.drawAttachEast = drawAttachEast;
    }

    public boolean isDrawAttachWest() {
        return drawAttachWest;
    }

    public void setDrawAttachWest(boolean drawAttachWest) {
        this.drawAttachWest = drawAttachWest;
    }
}
