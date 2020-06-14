package net.vpc.scholar.hadruplot.libraries.calc3d.vpc.element3d;

import net.vpc.scholar.hadruplot.libraries.calc3d.elements.ColorModel;
import net.vpc.scholar.hadruplot.libraries.calc3d.engine3d.Camera3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Calc3dLibraryUtils;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.*;

import java.awt.*;

/**
 * Class for Elements3DSurfacd representing Surface in 3D space
 * 
 * @author mahesh
 * 
 */
public class Element3DSurface2 extends Element3Db {

	/**
	 *
	 */
	private static final long serialVersionUID = -8584466900933868834L;
	private static Logger LOG = Logger.getLogger(Camera3D.class.getName());
	/** Dimension bounding the surfce */
	double minX, maxX, minY, maxY, minZ, maxZ;
	private Vector3D[][] points;
	/**no of divisions/grids for surface*/
//	private int xGrids = 20, yGrids = 20;
	public Element3DSurface2(Vector3D[][] points) {
		this.minX = -1;
		this.minY = -1;
		this.minZ = -1;
		this.maxX = 1;
		this.maxY = 1;
		this.maxZ = 1;
		this.points = points;
		this.prefs().setFillmode(2);
	}

	public Element3DSurface2(Vector3D[][] points, Box3D box) {
		this.minX = box.getMinX();
		this.minY = box.getMinY();
		this.minZ = box.getMinZ();
		this.maxX = box.getMaxX();
		this.maxY = box.getMaxY();
		this.maxZ = box.getMaxZ();
		this.points = points;
	}

	@Override
	public PrimitiveElement3D createElement() {
		primitiveElement3D =createSurface(null);
		return primitiveElement3D;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		primitiveElement3D =createSurface(clip);
		return primitiveElement3D;
	}

	@Override
	public String getDefinition() {
		return "Custom Surface";
	}

	public Box3D getBox() {
		return new Box3D(minX, maxX, minY, maxY, minZ, maxZ);
	}

	public void setBox(Box3D box) {
		this.minX = box.getMinX();
		this.minY = box.getMinY();
		this.minZ = box.getMinZ();
		this.maxX = box.getMaxX();
		this.maxY = box.getMaxY();
		this.maxZ = box.getMaxZ();
	}

	private PrimitiveElement3DCollection createSurface(Clip clip) {
		int i, j;
		double x, y, z;
       
		PrimitiveElement3DCollection surface3D = new PrimitiveElement3DCollection();
		Vector3D[][] points = Calc3dLibraryUtils.inverseMap(this,this.points);

		Vector3D v1, v2, v3, v4;
		for (i = 0; i < points[0].length-1; i++) {
			Inner:
			for (j = 0; j < points.length-1; j++) {

				v1 = points[j][i];
				v2 = points[j+1][i];
				v3 = points[j+1][i+1];
				v4 = points[j][i+1];
				if(Calc3dLibraryUtils.isFinite(v1) && Calc3dLibraryUtils.isFinite(v2) && Calc3dLibraryUtils.isFinite(v3) && Calc3dLibraryUtils.isFinite(v4)){
					PrimitiveElement3DPoly element = new PrimitiveElement3DPoly();
					element.addPoint(v1);
					element.addPoint(v2);
					element.addPoint(v3);
					element.addPoint(v4);
					if (T!=null)element.transform(T);
					element.drawContours=true;
					float xc=1.0f*i/points[0].length;
					float yc=1.0f*j/points.length;
					if (prefs().getFillmode()==1)
					{
						element.setFilled(false);
						element.prefs().setLineColor(new Color(xc, 0, yc));
						element.prefs().setCurveWidth(Math.max(1,prefs().getCurveWidth()));
						element.prefs().setFillColor(new Color(xc, 0, j / yc));
					}
					else if (prefs().getFillmode()==3){
						element.prefs().setFillColor(prefs().getFillColor());
						element.prefs().setFillColor(ColorModel.getPreset(2).getPolygonColor((float) element.getCentre().getZ()));
						element.prefs().setBackColor(Color.gray);
						element.prefs().setLineColor(prefs().getLineColor());
						element.prefs().setCurveWidth(prefs().getCurveWidth());
					}
					else if(prefs().getFillmode()==2){
						element.prefs().setFillColor(prefs().getFillColor());
						element.prefs().setBackColor(prefs().getBackColor());
						element.prefs().setLineColor(prefs().getLineColor());
						element.prefs().setCurveWidth(prefs().getCurveWidth());
					}
					else{
						element.prefs().setFillColor(new Color(xc, 0, yc));
						//primitiveElement3D.setFillColor( Color.getHSBColor((float) (0.3+0.7*(z-minZ)/(maxZ - minZ)), 1f, 1f));
						element.prefs().setBackColor(Color.gray);
						element.prefs().setLineColor(prefs().getLineColor());
						element.prefs().setCurveWidth(prefs().getCurveWidth());
					}
					if (null != clip) {
						PrimitiveElement3DPoly clippoly = new PrimitiveElement3DPoly();
						if (clip.getClippedPoly(element.vertices, clippoly.vertices) != 2) {
							clippoly.reCalculateNormalandCentre();
							clippoly.prefs().setFillColor(element.prefs().getFillColor());
							clippoly.prefs().setBackColor(element.prefs().getBackColor());
							clippoly.prefs().setLineColor(element.prefs().getLineColor());
							clippoly.prefs().setCurveWidth(element.prefs().getCurveWidth());
							if (prefs().getFillmode()==1)clippoly.setFilled(false); else clippoly.setFilled(true);
							clippoly.prefs().setSplittable(prefs().isSplittable());
							clippoly.drawContours=true;
							if (clippoly.vertices.size()>2)surface3D.addElement(clippoly);
						}
					} else {
						surface3D.addElement(element);
					}
				}

			}
		}
		primitiveElement3D =surface3D;
		return (surface3D.primitiveElement3DS.size() > 0) ? surface3D : null;
	}
}
