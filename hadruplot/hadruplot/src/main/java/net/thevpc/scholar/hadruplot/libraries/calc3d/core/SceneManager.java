package net.thevpc.scholar.hadruplot.libraries.calc3d.core;

import net.thevpc.scholar.hadruplot.libraries.calc3d.engine3d.Scene3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.thevpc.common.swing.color.ColorUtils;

public class SceneManager {

    /**
     * object to store axes info
     */
    private static AxesDefinition axesDefinition;
    private Globalsettings settings = new Globalsettings();
    private ArrayList<Element3D> element3Ds = new ArrayList<>();
    /**
     * bounding box of scene, all primitiveElement3D are clipped against this
     * box
     */
    private Box3D box;
    /**
     * Clipper for the bounding box
     */
    private Clip clip;
    /**
     * Elements represetenting axes
     */
    private PrimitiveElement3DCollection axis3D, box3D, gridXY;

    public SceneManager() {
        box = new Box3D(-1, 1, -1, 1, -1, 1);
        clip = new Clip(box);
        axesDefinition = new AxesDefinition();
    }

    public void createDemo() {
        //Add demoPoints
        Element3DPoint p1, p2;
        p1 = new Element3DPoint(new Vector3D(0.1, 0.2, 0.7), "A", Color.red);
        p2 = new Element3DPoint(new Vector3D(0.6, 0.6, -0.6), "B", Color.blue);
        p1.setName("A " + p1.getDefinition());
        p1.setText("A " + p1.getDefinition());
        //addElement(p1);
        //vpc
        addElement(p1);
        //element3Ds.add(p2);
        //Add demoLine
        Element3DLine l1;
        l1 = new Element3DLine(new Vector3D(-1, -1, -1), new Vector3D(1, 1, 3));
        l1.prefs().setFillColor(Color.green);
        l1.prefs().setLineColor(Color.black);
        l1.prefs().setCurveWidth(2);
        //Add demoCurve
        Element3DCurve ec = new Element3DCurve();
        ec.setExprX("3*sin(t)");
        ec.setExprY("3*cos(t)");
        ec.setExprZ("t/2");
        ec.setMin_t(-10);
        ec.setMax_t(10);
        ec.setNumSegments(100);
        ec.prefs().setFillColor(Color.red.darker());
        ec.prefs().setBackColor(Color.orange);
        ec.prefs().setCurveWidth(2);
        ec.setName("Helix");
        addElement(ec);

        //Add demoCurve
        Element3DVectorField ef = new Element3DVectorField();
        ef.setExprX("x/sqrt(x*x+y*y+z*z)");
        ef.setExprY("y/sqrt(x*x+y*y+z*z)");
        ef.setExprZ("z/sqrt(x*x+y*y+z*z)");
        ef.prefs().setFillColor(Color.red);
        ef.prefs().setBackColor(Color.orange);
        ef.prefs().setCurveWidth(1);
        ef.setName("Radial Field");
        addElement(ef);
        //addElement(ec);

        //addElement(new Element3DObject(4,3,1,9));
        //addElement(new Element3DObject(1));
        //addElement(new Element3DObject(2));
        //addElement(ec);
        //Add Demo Plane
        Element3DPlane ep = new Element3DPlane(1, 0, 1, 0);
        ep.prefs().setFillColor(Color.pink);
        //ep.setName("saas");
        initialiseElement3D(ep);
        //addElement(ep);
        //Add Demo Vector
        Element3DVector ev = new Element3DVector(1, 0.3, 0.8);
        ev.prefs().setFillColor(Color.black);
        ev.setName("Vector3D " + ev.getDefinition());
        addElement(ev);
        //Add Demo line
        Element3DLineSegment ls = new Element3DLineSegment(p1, p2);
        //element3Ds.add(ls);

        //Add Surface
        Element3DSurface es = new Element3DSurface("x-y");//0.3*(x*sin(5*y)+y*sin(5*x))");//"2*3*x*3*y/(2^((3*x)^2+(3*y)^2))");//
        es.prefs().setFillColor(Color.green);
        es.prefs().setLineColor(Color.gray);
        es.setxGrids(10);
        es.setyGrids(10);
        addElement(es);
        //boxVisible=true;
        //Add demoCurve
        Element3DCurve ec1 = new Element3DCurve();
        ec1.setExprX("0.3*sin(12*t)");
        ec1.setExprY("t");
        ec1.setExprZ("t");
        ec1.setMin_t(-1);
        ec1.setMax_t(1);
        ec1.setNumSegments(50);
        ec1.prefs().setLineColor(Color.red.darker());
        ec1.prefs().setCurveWidth(1);
        //	primitiveElement3DS.add(ec1);
        Element3Dimplicit2D eic = new Element3Dimplicit2D("r-3*sin(t)");
        eic.setFuncType(1);
        eic.prefs().setCurveWidth(2);
        eic.prefs().setLineColor(Color.red);
        eic.prefs().setFillColor(Color.red);

        //element3Ds.add(eic);
        Element3Dfunction ef1 = new Element3Dfunction("Pi=3.14;ir=.3+.1*sin(4*Pi*u)\nr=ir*sin(2*Pi*v)+.5\nx=r*sin(2*Pi*u)\ny=r*cos(2*Pi*u)\nz=1.5*ir*cos(Pi*v)\nx=x*5;y=y*5;z=z*5;");//"x=cos(u)\n y=sin(u)\n z=v");
        ef1.setName("Open pot");
        ef1.prefs().setLineColor(new Color(109, 95, 163));
        ef1.prefs().setFillmode(0);
        addElement(ef1);
        //Element3Dfunction ef1=new Element3Dfunction("u=-2+4*u; v=-2+4*v\n\nx=u-(u*u*u/3)+u*v*v\ny=v-(v*v*v/3)+u*u*v\nz=u*u-v*v\n\nn=10; x=x/n; y=y/n; z=z/n");
        Element3Dfunction ef2 = new Element3Dfunction("u=u*6.28;v=v*6.28;x=(3 + sin(v) + cos(u)) * sin(2 * v);y=(3 + sin(v) + cos(u)) * cos(2 * v);z=sin(u) + 2 * cos(v);x=x/1.3;y=y/1.3;z=z/1.3;");
        ef2.setName("Closed loop");
        ef2.setuGrids(10);
        ef2.setvGrids(30);
        ef2.prefs().setFillmode(0);
        ef2.prefs().setLineColor(Color.gray);

        addElement(ef2);
        Element3DImplicit ei = new Element3DImplicit("x*x+y*y+z*z-9");
        //	ei.setName("kauu");
        initialiseElement3D(ei);
        addElement(ei);
        //element3Ds.add(new Element3DImplicitCurve("x*x+y*y+z*z-0.81"));
        //Element3DObject eo=new Element3DObject(4,0.6,1)	;

        //primitiveElement3DS.add(eo);
        Element3DParametricSurface eps = new Element3DParametricSurface();
        addElement(eps);

        Element3Dimplicit2D eic2 = new Element3Dimplicit2D("2*x*x+4*y*y-2*x*y-1");//"x*x+y*y-2*x*y-x-0.5");//0.3*(x*sin(5*y)+y*cos(5*x))");
        addElement(eic2);

        Element3Dcartesian2D ecc = new Element3Dcartesian2D("sin(x)");
        ecc.setName("curve2D-SineCurve");
        addElement(ecc);

    }

    private void initialiseElement3D(Element3D element3D) {
        setValidName(element3D);
        Color fc = ColorUtils.getRandomColor(0.5f, 1.0f);
        Color oc = fc.darker();
        element3D.prefs().setLineColor(oc);
        element3D.prefs().setFillColor(fc);
    }

    /**
     * Adds Element3D to list of Element3D and also creates its renderable
     * primitiveElement3D
     *
     * @param element3D
     */
    public void addElement(Element3D element3D) {
        setValidName(element3D);
        element3Ds.add(element3D);
        element3D.setSceneManager(this);
        element3D.createElement(clip);
    }

    /**
     * Sets the name by appending name of object by numbers such that no 2
     * objects have same name
     *
     * @param element3D the element3D whose name is to be validated and set to
     * new name if required
     */
    public void setValidName(Element3D element3D) {

        if (element3D.getName().trim().equalsIgnoreCase("")) {
            element3D.setName(commonUtils.getobject3DName(element3D));
        }
        String tempName = element3D.getName();
        boolean isValid = false;
        int i = 0;

        while (isValid == false) {
            isValid = true;
            for (Element3D e : element3Ds) {
                if (tempName.equalsIgnoreCase(e.getName())) {
                    i++;
                    tempName = element3D.getName() + i;
                    isValid = false;
                    break;
                }
            }
        }
        element3D.setName(tempName);
    }

    /**
     * Removes primitiveElement3D if found in list
     */
    public void removeElement(Element3D element3D) {
        element3Ds.remove(element3D);
    }

    public boolean removeElementIf(Predicate<? super Element3D> element3D) {
        return element3Ds.removeIf(element3D);
    }

    public boolean removeAll() {
        boolean some=false;
        for (Element3D e : element3Ds.toArray(new Element3D[0])) {
            e.setSceneManager(null);
            e.setSceneManager(null);
            some=true;
        }
        element3Ds.clear();
        return some;
    }

    public java.util.List<Element3D> find(Predicate<? super Element3D> element3D) {
        java.util.List<Element3D> ll = new ArrayList<>();
        for (Element3D d : element3Ds) {
            if (element3D.test(d)) {
                ll.add(d);
            }
        }
        return ll;
    }

    public void setElement3DVisible(Element3D element3D, boolean visible) {
        element3D.prefs().setVisible(visible);
    }

    /**
     * Returns the arraylist containing all primitiveElement3DS;
     *
     * @return Arraylist
     */
    public ArrayList<Element3D> getElement3DList() {
        return element3Ds;
    }

    /**
     * Returns primitiveElement3D at given index if exits else null;
     *
     * @param index
     * @return
     */
    public Element3D getElement3D(int index) {
        if ((index >= 0) & (index < element3Ds.size())) {
            return element3Ds.get(index);
        } else {
            return null;
        }
    }

    /**
     * Returns primitiveElement3D at given index if exits else null;
     *
     * @param index
     * @return
     */
    public void setElement3D(int index, Element3D element) {
        if ((index >= 0) & (index < element3Ds.size())) {
            element3Ds.remove(index);
            element3Ds.add(index, element);
            element.createElement(clip);
        }

    }

    /**
     * returns number of element3Ds conatained by sceneManager
     *
     * @return
     */
    public int getElementCount() {
        return element3Ds.size();
    }

    /**
     * Creates the scene (for 3D Engine) based on Element3Ds of this manager
     *
     * @param reCreateEachElement Setting this true forces each Element3D to
     * reCreate its renderable primitiveElement3D
     * @return
     */
    public Scene3D createScene(boolean reCreateEachElement) {
        Scene3D scene = new Scene3D();
        Object3D<PrimitiveElement3D> object3D = new Object3D<PrimitiveElement3D>();
        //Add axes whichever enable
        createAxes();
        //createBox(Color.blue);
        createXYGrid(Color.green.darker().darker());
        if (settings.axisVisible) {
            for (PrimitiveElement3D e : axis3D.primitiveElement3DS) {
                object3D.addElement(e);
            }
        }

        //if (boxVisible)		for (PrimitiveElement3D e:box3D.primitiveElement3DS)	object3D.addElement(e);
        if (settings.gridXYVisible) {
            for (PrimitiveElement3D e : gridXY.primitiveElement3DS) {
                object3D.addElement(e);
            }
        }

        //Add plane first so that number of intersection of polygons in bsp may be minimum
        for (Element3D element3D : element3Ds) {
            if (null == element3D) {
                continue;
            }
            if ((element3D.prefs().isVisible() == false) && (!reCreateEachElement)) {
                continue;
            }
            if (element3D instanceof Element3DPlane || element3D instanceof Element3DPolygon) {
                //Create PrimitiveElement3D again only when needed
                PrimitiveElement3D e = reCreateEachElement ? element3D.createElement(clip) : element3D.getPrimitiveElement3D();
                if ((e == null) && !reCreateEachElement) {
                    e = element3D.createElement(clip);
                }
                if ((null != e) && element3D.prefs().isVisible()) {
                    object3D.addElement(e);
                }
            }
        }

        //Add primitiveElement3DS other than Plane
        for (Element3D element3D : element3Ds) {
            if (null == element3D) {
                continue;
            }
            if ((element3D.prefs().isVisible() == false) && (!reCreateEachElement)) {
                continue;
            }
            if (element3D instanceof Element3DPlane) {
                continue;
            }
            //Create PrimitiveElement3D again only when needed
            PrimitiveElement3D e = reCreateEachElement ? element3D.createElement(clip) : element3D.getPrimitiveElement3D();
            if ((e == null) && !reCreateEachElement) {
                e = element3D.createElement(clip);
            }
            if ((e == null) || !element3D.prefs().isVisible()) {
                continue;
            }
            if (e instanceof PrimitiveElement3DCollection) {
                for (PrimitiveElement3D e1 : ((PrimitiveElement3DCollection) e).primitiveElement3DS) {
                    if (null != e1) {
                        object3D.addElement(e1);
                    }
                }
            } else {
                if (null != e) {
                    object3D.addElement(e);
                }
            }
        }

        //Add all primitiveElement3DS to scene
        scene.addObject3D(object3D);
        return scene;

    }

    private PrimitiveElement3DCollection createAxes() {
        Color color = getSettings().axisColor;

        double minX, maxX, minY, maxY, minZ, maxZ;
        Box3D box = getSettings().axesBox;
        minX = getSettings().inverseMapX(box.getMinX());
        maxX = getSettings().inverseMapX(box.getMaxX());
        minY = getSettings().inverseMapX(box.getMinY());
        maxY = getSettings().inverseMapX(box.getMaxY());
        minZ = getSettings().inverseMapX(box.getMinZ());
        maxZ = getSettings().inverseMapX(box.getMaxZ());
        double n = 10; //no of parts axis is divided
        int i;
        axis3D = new PrimitiveElement3DCollection();
        if (getSettings().xAxisVisible & (maxX > 0)) {
            for (i = 0; i < n - 1; i++) {
                PrimitiveElement3DCurve ex = new PrimitiveElement3DCurve(new Vector3D(i / n * maxX, 0, 0), new Vector3D((i + 1) / n * maxX, 0, 0));
                ex.prefs().setLineColor(color);
                ex.prefs().setCurveWidth(getSettings().axisWidth);
                ex.prefs().setFillColor(color);
                axis3D.addElement(ex);
            }
            PrimitiveElement3DArrow ex = new PrimitiveElement3DArrow(new Vector3D(i / n * maxX, 0, 0), new Vector3D((i + 1) / n * maxX, 0, 0));
            ex.prefs().setLineColor(color);
            ex.prefs().setCurveWidth(getSettings().axisWidth);
            ex.prefs().setFillColor(color);
            ex.setArrowSize(3);
            axis3D.addElement(ex);
            axis3D.addElement(new PrimitiveElement3DString("x", new Vector3D(maxX + 0.1, 0, 0), new Vector3D(maxX + 0.2, 0, 0), color));
        }

        if (getSettings().yAxisVisible & (maxY > 0)) {
            for (i = 0; i < n - 1; i++) {
                PrimitiveElement3DCurve ey = new PrimitiveElement3DCurve(new Vector3D(0, i / n * maxY, 0), new Vector3D(0, (i + 1) / n * maxY, 0));
                ey.prefs().setLineColor(color);
                ey.prefs().setCurveWidth(getSettings().axisWidth);
                ey.prefs().setFillColor(color);
                axis3D.addElement(ey);
            }
            PrimitiveElement3DArrow ey = new PrimitiveElement3DArrow(new Vector3D(0, i / n * maxY, 0), new Vector3D(0, (i + 1) / n * maxY, 0));
            ey.prefs().setLineColor(color);
            ey.prefs().setCurveWidth(getSettings().axisWidth);
            ey.prefs().setFillColor(color);
            ey.setArrowSize(3);
            axis3D.addElement(ey);
            axis3D.addElement(new PrimitiveElement3DString("y", new Vector3D(0, maxY + 0.1, 0), new Vector3D(0, maxY + 0.2, 0), color));
        }

        if (getSettings().zAxisVisible & (maxZ > 0)) {
            for (i = 0; i < n - 1; i++) {
                PrimitiveElement3DCurve ez = new PrimitiveElement3DCurve(new Vector3D(0, 0, i / n * maxZ), new Vector3D(0, 0, (i + 1) / n * maxZ));
                ez.prefs().setLineColor(color);
                ez.prefs().setCurveWidth(getSettings().axisWidth);
                ez.prefs().setFillColor(color);
                axis3D.addElement(ez);
            }
            PrimitiveElement3DArrow ez = new PrimitiveElement3DArrow(new Vector3D(0, 0, i / n * maxZ), new Vector3D(0, 0, (i + 1) / n * maxZ));
            ez.prefs().setLineColor(color);
            ez.prefs().setCurveWidth(getSettings().axisWidth);
            ez.prefs().setFillColor(color);
            ez.setArrowSize(3);
            axis3D.addElement(ez);
            axis3D.addElement(new PrimitiveElement3DString("z", new Vector3D(0, 0, maxZ + 0.1), new Vector3D(0, 0, maxZ + 0.2), color));
        }

        if (getSettings().xAxisVisible & (minX < 0)) {
            for (i = 0; i < n - 1; i++) {
                PrimitiveElement3DCurve ex = new PrimitiveElement3DCurve(new Vector3D(-i / n * Math.abs(minX), 0, 0), new Vector3D(-(i + 1) / n * Math.abs(minX), 0, 0));
                ex.prefs().setLineColor(color);
                ex.prefs().setCurveWidth(getSettings().axisWidth);
                ex.prefs().setFillColor(color);
                axis3D.addElement(ex);
            }
            PrimitiveElement3DArrow ex = new PrimitiveElement3DArrow(new Vector3D(-i / n * Math.abs(minX), 0, 0), new Vector3D(-(i + 1) / n * Math.abs(minX), 0, 0));
            ex.prefs().setLineColor(color);
            ex.prefs().setCurveWidth(getSettings().axisWidth);
            ex.prefs().setFillColor(color);
            ex.setArrowSize(3);
            axis3D.addElement(ex);
            axis3D.addElement(new PrimitiveElement3DString("-x", new Vector3D(-Math.abs(minX) - 0.1, 0, 0), new Vector3D(-Math.abs(minX) - 0.2, 0, 0), color));
        }

        if (getSettings().yAxisVisible & (minY < 0)) {
            for (i = 0; i < n - 1; i++) {
                PrimitiveElement3DCurve ey = new PrimitiveElement3DCurve(new Vector3D(0, -i / n * Math.abs(minY), 0), new Vector3D(0, -(i + 1) / n * Math.abs(minY), 0));
                ey.prefs().setLineColor(color);
                ey.prefs().setCurveWidth(getSettings().axisWidth);
                ey.prefs().setFillColor(color);
                axis3D.addElement(ey);
            }
            PrimitiveElement3DArrow ey = new PrimitiveElement3DArrow(new Vector3D(0, -i / n * Math.abs(minY), 0), new Vector3D(0, -(i + 1) / n * Math.abs(minY), 0));
            ey.prefs().setLineColor(color);
            ey.prefs().setCurveWidth(getSettings().axisWidth);
            ey.prefs().setFillColor(color);
            ey.setArrowSize(3);
            axis3D.addElement(ey);
            axis3D.addElement(new PrimitiveElement3DString("-y", new Vector3D(0, -Math.abs(minY) - 0.1, 0), new Vector3D(0, -Math.abs(minY) - 0.2, 0), color));
        }

        if (getSettings().zAxisVisible & (minZ < 0)) {
            for (i = 0; i < n - 1; i++) {
                PrimitiveElement3DCurve ez = new PrimitiveElement3DCurve(new Vector3D(0, 0, -i / n * Math.abs(minZ)), new Vector3D(0, 0, -(i + 1) / n * Math.abs(minZ)));
                ez.prefs().setLineColor(color);
                ez.prefs().setCurveWidth(getSettings().axisWidth);
                ez.prefs().setFillColor(color);
                axis3D.addElement(ez);
            }
            PrimitiveElement3DArrow ez = new PrimitiveElement3DArrow(new Vector3D(0, 0, -i / n * Math.abs(minZ)), new Vector3D(0, 0, -(i + 1) / n * Math.abs(minZ)));
            ez.prefs().setLineColor(color);
            ez.prefs().setCurveWidth(getSettings().axisWidth);
            ez.prefs().setFillColor(color);
            ez.setArrowSize(3);
            axis3D.addElement(ez);
            axis3D.addElement(new PrimitiveElement3DString("-z", new Vector3D(0, 0, -Math.abs(minZ) - 0.1), new Vector3D(0, 0, -Math.abs(minZ) - 0.2), color));
        }

        /*
		 * Draw ticks
		
		if (Globalsettings.xAxisVisible & (maxX>0)){
		    n=Globalsettings.axisTicks;
		    for ( i = 1; i < n; i++) {
		    PrimitiveElement3DCurve e = new PrimitiveElement3DCurve(new Vector3D(-0.035, 0,i/n*Globalsettings.inverseMapZ(maxZ)),new Vector3D(0.035, 0,i/n*Globalsettings.inverseMapZ(maxZ)));
			e.setLineColor(color);
			axis3D.addElement(e);
			  e = new PrimitiveElement3DCurve(new Vector3D(i/n*Globalsettings.inverseMapZ(maxZ),-0.035,0),new Vector3D(i/n*Globalsettings.inverseMapZ(maxZ),0.035, 0));
				e.setLineColor(color);
				axis3D.addElement(e);
				 e = new PrimitiveElement3DCurve(new Vector3D(-0.035,i/n*Globalsettings.inverseMapZ(maxZ),0),new Vector3D(0.035,i/n*Globalsettings.inverseMapZ(maxZ), 0));
					e.setLineColor(color);
					axis3D.addElement(e);
				 
		    
		    }
		}
         */
        return axis3D;
    }

    /**
     * XY plane with grids
     *
     * @param color
     * @return
     */
    private PrimitiveElement3DCollection createXYGrid(Color color) {
        color = Color.DARK_GRAY;//.brighter().brighter();
        gridXY = new PrimitiveElement3DCollection();
        double minX, minY, maxX, maxY;
        box = getSettings().mappedClipBox;
        minX = box.getMinX();
        minY = box.getMinY();
        //minZ = box.getMinZ();
        maxX = box.getMaxX();
        maxY = box.getMaxY();
        //maxZ = box.getMaxZ();
        int numGrids = 8;
        double x, y, dx, dy;
        dx = (maxX - minX) / (numGrids);
        dy = (maxY - minY) / (numGrids);
        double gridXYdz = -0.02;
        double gridXYdx = 0.12;
        gridXYdz = 0;

        gridXYdx = 0;
        for (y = minY + dy; y < maxY; y += dy) {
            Vector3D v1 = new Vector3D(minX, y, gridXYdz);
            Vector3D v2 = new Vector3D(maxX, y, gridXYdz);
            PrimitiveElement3DCurve ec = new PrimitiveElement3DCurve(v1, v2);
            ec.prefs().setDashed(true);
            ec.prefs().setLineColor(color);
            ec.prefs().setFillColor(color);
            ec.prefs().setBackColor(color);
            gridXY.addElement(ec);
            if (getSettings().mapCliptoY(y) == 0) {
                continue;
            }
            PrimitiveElement3DString es = new PrimitiveElement3DString(doubletoString(getSettings().mapCliptoY(y)), new Vector3D(maxX + 0.12, y, 0), new Vector3D(maxX + 0.13, y, 0), Color.black);
            gridXY.addElement(es);
        }

        for (x = minX + dx; x < maxX; x += dx) {
            Vector3D v1 = new Vector3D(x, minY, gridXYdz);
            Vector3D v2 = new Vector3D(x, maxY, gridXYdz);
            PrimitiveElement3DCurve ec = new PrimitiveElement3DCurve(v1, v2);
            ec.prefs().setDashed(true);
            ec.prefs().setLineColor(color);
            ec.prefs().setFillColor(color);
            ec.prefs().setBackColor(color);
            gridXY.addElement(ec);
            if (getSettings().mapCliptoY(x) == 0) {
                continue;
            }
            PrimitiveElement3DString es = new PrimitiveElement3DString(doubletoString(getSettings().mapCliptoX(x)), new Vector3D(x, maxY + 0.12, 0), new Vector3D(x, maxY + 0.13, 0), Color.black);
            gridXY.addElement(es);
        }

        PrimitiveElement3D xyPlane = clip.getClippedPolygonfromPlane(new Plane3D(0, 0, 1, 0));//0.04
        xyPlane.prefs().setFillColor(new Color(0, 120, 0, 110));
        xyPlane.prefs().setBackColor(new Color(0, 120, 0, 100));
        xyPlane.setFilled(true);
        gridXY.addElement(xyPlane);
        return gridXY;
    }

    private String doubletoString(double d) {
        return String.format("%.1f", ((int) (d * 10)) / 10.0);//String.format("%.0G", d);
    }

    /**
     * 3D axes
     *
     * @param color
     * @return
     */
    private PrimitiveElement3DCollection createAxes1(Color color) {
        axis3D = new PrimitiveElement3DCollection();
        final String[] axisNames = {"X", "Y", "Z"};
        for (int i = 0; i < 3; i++) {
            if (!axesDefinition.shown[i]) {
                continue;
            }
            Vector3D axisVector = axesDefinition.axisVectors[i];
            double min = axesDefinition.min[i], max = axesDefinition.max[i];
            // create the axis line segments
            double incr = axesDefinition.incr;
            for (double v = min; v < max - 1e-8; v += incr) {
                axis3D.addElement(new PrimitiveElement3DCurve(axisVector.scale(v),
                        axisVector.scale(v + incr), axesDefinition.color, axesDefinition.width, true));
            }
            // create the tick mark labels
            for (double v = Math.ceil(min / axesDefinition.tickDensity) * axesDefinition.tickDensity; v < max;
                    v += axesDefinition.tickDensity) {
                double pos = (double) Math.round(v * 1000) / 1000;
                //  if (pos!=0) axis3D.addElement(new PrimitiveElement3DString(String.valueOf(pos),
                //	axisVector.scale(v),axesDefinition.color,axisVector));
            }
            // create the arrows
            Vector3D planeVec = axesDefinition.axisVectors[i == 0 ? 2 : 0];
            axis3D.addElement(new PrimitiveElement3DCurve(axisVector.scale(max),
                    axisVector.scale(max * .97).subtract(planeVec.scale(.03)),
                    axesDefinition.color, axesDefinition.width, true));
            axis3D.addElement(new PrimitiveElement3DCurve(axisVector.scale(max),
                    axisVector.scale(max * .97).add(planeVec.scale(.03)),
                    axesDefinition.color, axesDefinition.width, true));
            axis3D.addElement(new PrimitiveElement3DString(axisNames[i], axisVector.scale(max * 1.1), axisVector.scale(max * 1.2), axesDefinition.color));

        }
        return axis3D;
    }

    /**
     * Bounding box of 3D axes
     *
     * @param color
     * @return
     */
    private PrimitiveElement3DCollection createBox(Color color) {
        box3D = new PrimitiveElement3DCollection();
        Vector3D v1, v2;
        double minX, minY, minZ, maxX, maxY, maxZ;
        minX = box.getMinX();
        minY = box.getMinY();
        minZ = box.getMinZ();
        maxX = box.getMaxX();
        maxY = box.getMaxY();
        maxZ = box.getMaxZ();

        //Plane y=ymax
        v1 = new Vector3D(maxX, maxY, minZ);
        v2 = new Vector3D(maxX, maxY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(maxX, maxY, maxZ);
        v2 = new Vector3D(minX, maxY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, maxY, maxZ);
        v2 = new Vector3D(minX, maxY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, maxY, minZ);
        v2 = new Vector3D(maxX, maxY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        //Plane y=ymin
        v1 = new Vector3D(maxX, minY, minZ);
        v2 = new Vector3D(maxX, minY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(maxX, minY, maxZ);
        v2 = new Vector3D(minX, minY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, minY, maxZ);
        v2 = new Vector3D(minX, minY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, minY, minZ);
        v2 = new Vector3D(maxX, minY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        //Plane x=xmax
        v1 = new Vector3D(maxX, maxY, minZ);
        v2 = new Vector3D(maxX, maxY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(maxX, maxY, maxZ);
        v2 = new Vector3D(maxX, minY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(maxX, minY, maxZ);
        v2 = new Vector3D(maxX, minY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(maxX, minY, minZ);
        v2 = new Vector3D(maxX, maxY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        //Plane x=xmin
        v1 = new Vector3D(minX, maxY, minZ);
        v2 = new Vector3D(minX, maxY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, maxY, maxZ);
        v2 = new Vector3D(minX, minY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, minY, maxZ);
        v2 = new Vector3D(minX, minY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, minY, minZ);
        v2 = new Vector3D(minX, maxY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        //plane z=zmax
        v1 = new Vector3D(maxX, maxY, maxZ);
        v2 = new Vector3D(minX, maxY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, maxY, maxZ);
        v2 = new Vector3D(minX, minY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, minY, maxZ);
        v2 = new Vector3D(maxX, minY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(maxX, minY, maxZ);
        v2 = new Vector3D(maxX, maxY, maxZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        //Plane z=zmin
        v1 = new Vector3D(maxX, maxY, minZ);
        v2 = new Vector3D(minX, maxY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, maxY, minZ);
        v2 = new Vector3D(minX, minY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(minX, minY, minZ);
        v2 = new Vector3D(maxX, minY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));
        v1 = new Vector3D(maxX, minY, minZ);
        v2 = new Vector3D(maxX, maxY, minZ);
        box3D.addElement(new PrimitiveElement3DCurve(v1, v2, color, 1, true));

        PrimitiveElement3DRuler erx = new PrimitiveElement3DRuler(new Vector3D(-1, -1, -1), new Vector3D(1, -1, -1), new Vector3D(1, 0, -1), 0, 1, 5, 5);
        PrimitiveElement3DRuler ery = new PrimitiveElement3DRuler(new Vector3D(-1, -1, -1), new Vector3D(-1, 1, -1), new Vector3D(1, 0, -1), 0, 1, 5, 5);
        erx.prefs().setSplittable(false);
        ery.prefs().setSplittable(false);

        //box3D.addElement(erx);
        //box3D.addElement(ery);
        float div = 5;
        int subdiv = 5;
        float delta, ticklength;
        delta = 2 / (div * subdiv);
        for (int i = 0; i < div; i++) {
            for (int j = 0; j < subdiv; j++) {
                ticklength = (j == 0) ? 0.07f : 0.04f;
                PrimitiveElement3DCurve c = new PrimitiveElement3DCurve(new Vector3D(-1 + (i * subdiv + j) * delta, -1, -1), new Vector3D(-1 + (i * subdiv + j) * delta, -1 + ticklength, -1));
                c.prefs().setLineColor(color);
                box3D.addElement(c);
                if ((i > 0) && (j == 0)) {
                    box3D.addElement(new PrimitiveElement3DString(String.format("%.1G", getSettings().mapX(-1 + (i * subdiv + j) * delta)), new Vector3D(-1 + (i * subdiv + j) * delta, -1, -1), new Vector3D(0, 0, 0), color.darker()));
                }
            }
            PrimitiveElement3DCurve c = new PrimitiveElement3DCurve(new Vector3D(1, -1, -1), new Vector3D(1, -1 + 0.07, -1));
            box3D.addElement(c);
        }

        for (int i = 0; i < div; i++) {
            for (int j = 0; j < subdiv; j++) {
                ticklength = (j == 0) ? 0.07f : 0.04f;
                PrimitiveElement3DCurve c = new PrimitiveElement3DCurve(new Vector3D(-1, -1 + (i * subdiv + j) * delta, -1), new Vector3D(-1 + ticklength, -1 + (i * subdiv + j) * delta, -1));
                c.prefs().setLineColor(color);
                box3D.addElement(c);
                if ((i > 0) && (j == 0)) {
                    box3D.addElement(new PrimitiveElement3DString(String.format("%.1G", getSettings().mapY(-1 + (i * subdiv + j) * delta)), new Vector3D(-1, -1 + (i * subdiv + j) * delta, -1), new Vector3D(0, 0, 0), color.darker()));
                }

            }
            PrimitiveElement3DCurve c = new PrimitiveElement3DCurve(new Vector3D(-1, 1, -1), new Vector3D(-1 + 0.07, 1, -1));
            box3D.addElement(c);
        }

        for (int i = 0; i < div; i++) {
            for (int j = 0; j < subdiv; j++) {
                ticklength = (j == 0) ? 0.07f : 0.04f;
                PrimitiveElement3DCurve c = new PrimitiveElement3DCurve(new Vector3D(-1, 1, -1 + (i * subdiv + j) * delta), new Vector3D(-1 + ticklength, 1, -1 + (i * subdiv + j) * delta));
                c.prefs().setLineColor(color);
                box3D.addElement(c);
                if ((i > 0) && (j == 0)) {
                    box3D.addElement(new PrimitiveElement3DString(String.format("%.1G", getSettings().mapZ(-1 + (i * subdiv + j) * delta)), new Vector3D(-1, 1, -1 + (i * subdiv + j) * delta), new Vector3D(0, 0, 0), color.darker()));
                }

            }
            PrimitiveElement3DCurve c = new PrimitiveElement3DCurve(new Vector3D(-1, 1, 1), new Vector3D(-1 + 0.07, 1, 1));
            box3D.addElement(c);
        }
        return box3D;
    }

    /**
     * Returns true if axes will be rendered.
     */
    public boolean isAxisVisible() {
        return settings.axisVisible;
    }

    /**
     * Sets the axisRenderable flag.
     *
     * @param axisVisible true if axis are to be displayed
     */
    public void setAxisVisible(boolean axisVisible) {
        settings.axisVisible = axisVisible;
    }

    /**
     * Returns true if bounding Box will be rendered.
     */
    public boolean isBoxVisible() {
        return settings.boxVisible;
    }

    /**
     * Sets the boxRenderable flag.
     *
     * @param boxVisible true if bounding box is to be displayed
     */
    public void setBoxVisible(boolean boxVisible) {
        settings.boxVisible = boxVisible;
    }

    public boolean isGridXYVisible() {
        return settings.gridXYVisible;
    }

    public void setGridXYVisible(boolean gridXYVisible) {
        this.settings.gridXYVisible = gridXYVisible;
    }

    /**
     * @return the clips
     */
    public Clip getClip() {
        return clip;
    }

    /**
     * @param clips the clips to set
     */
    public void setClip(Clip clips) {
        this.clip = clips;
    }

    public Globalsettings getSettings() {
        return settings;
    }
}

/**
 * Class to store axes informations
 *
 * @author mahesh
 */
class AxesDefinition {

    public final Vector3D[] axisVectors = {new Vector3D(1, 0, 0), new Vector3D(0, 1, 0), new Vector3D(0, 0, 1)};
    public double[] min = {0, 0, 0}, max = {1, 1, 1};
    public double incr = 0.2; //0.05
    public double tickDensity = .2;
    public Color color = Color.black;
    public int width = 2;
    boolean[] shown = {true, true, true};
}
