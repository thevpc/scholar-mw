package net.vpc.scholar.hadruplot.libraries.calc3d.elements;

import java.awt.Color;

import net.vpc.scholar.hadruplot.libraries.calc3d.engine3d.Camera3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3DCollection;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.mathparser.Calculable;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;

public class Element3DImplicitCurve extends Element3D {
	private static Logger LOG = Logger.getLogger(Camera3D.class.getName());
	private transient Calculable calc;
	private String expr = "";
	double minX, maxX, minY, maxY, minZ, maxZ;
	
	public Element3DImplicitCurve(String expr) {
		this.expr = expr;this.minX = -1;
		this.minY = -1;
		this.minZ = -1;
		this.maxX = 1;
		this.maxY = 1;
		this.maxZ = 1;
	}

	@Override
	public String getDefinition() {
		return expr;
	}

	@Override
	public PrimitiveElement3D createElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrimitiveElement3D createElement(Clip clip) {
		PrimitiveElement3DCollection curve2D = new PrimitiveElement3DCollection();
		int n=50;
		double dx,dy,dz;
		dx=(maxX-minX)/n;
		dy=(maxY-minY)/n;
		dz=(maxZ-minZ)/n;	
		for (double x = minX; x < maxX-dx; x +=dx) {
			for (double y = minY; y < maxY-dy; y += dy) {
					Vector3D[] vertices=new Vector3D[4];
					double[] values= new double[4];
					double x1,y1;
					x1=x;y1=y;
					vertices[0]=new Vector3D(x1,y1,0);
					values[0]= f(x1,y1);
					x1=x;y1=y+dy;
					vertices[1]=new Vector3D(x1,y1,0);
					values[1]= f(x1,y1);
					x1=x+dx;y1=y+dy;
					vertices[2]=new Vector3D(x1,y1,0);
					values[2]= f(x1,y1);
					x1=x+dx;y1=y;
					vertices[3]=new Vector3D(x1,y1,0);
					values[3]= f(x1,y1);
				
                    GRID grid =new GRID();
                    grid.p=vertices;
                    grid.val=values;
                    Polygonise(grid,0,curve2D);
			}
		}
		
		return curve2D;
	}

	public double f(double x,double y){
		x=x*2;y=y*2;
	   return  Math.pow((x*x+y*y-1),3) - x*x*y*y*y;
			   //Math.pow(x, 5)+Math.pow(y, 5)-2*x*x+5*x*y+2*y*y;  
	}
	
	/*
	 * Given a grid cell and an isolevel, calculate the triangular facets
	 * required to represent the isosurface through the cell. Return the number
	 * of triangular facets, the array "triangles" will be loaded up with the
	 * vertices at most 5 triangular facets. 0 will be returned if the grid cell
	 * is either totally above of totally below the isolevel.
	 */
	public int Polygonise(GRID grid, double isolevel,
			PrimitiveElement3DCollection lines) {
		int i, nlines;
		int squareindex;
		Vector3D[] vertlist = new Vector3D[4];
		// For any edge, if one vertex is inside of the surface and the other is outside of the surface
	//  then the edge intersects the surface
	// For each of the 4 vertices of the cube can be two possible states : either inside or outside of the surface
	// For any cube the are 2^8=256 possible sets of vertex states
	// This table lists the edges intersected by the surface for all 256 possible vertex states
	// There are 12 edges.  For each entry in the table, if edge #n is intersected, then bit #n is set to 1
		int[] edgeTable ={0x0,  0x9,  0x3,  0xa,  
				          0x6,  0xf, 0x5,  0xc, 
				          0xc, 0x5,  0xf, 0x6,
				          0xa, 0x3,  0x9,  0x0};
	
	//  For each of the possible vertex states listed in aiCubeEdgeFlags there is a specific triangulation
	//  of the edge intersection points.  a2iTriangleConnectionTable lists all of them in the form of
	//  0-5 edge triples with the list terminated by the invalid value -1.
	//  For example: a2iTriangleConnectionTable[3] list the 2 triangles formed when corner[0] 
	//  and corner[1] are inside of the surface, but the rest of the cube is not.
	//
		int[][] lineTable = {
				{ -1, -1, -1, -1 },
				{  3,  0, -1, -1 },
				{  0,  1, -1, -1 },
				{  3,  1, -1, -1 },
				{  2,  1, -1, -1 },
				{  3,  2,  1,  0 },
				{  2,  0, -1, -1 },
				{  3,  2, -1, -1 },
				{  3,  2, -1, -1 },
				{  2,  0, -1, -1 },
				{  3,  2,  1,  0 },
				{  2,  1, -1, -1 },
				{  3,  1, -1, -1 },
				{  1,  0, -1, -1 },
				{  3,  0, -1, -1 },
				{ -1, -1, -1, -1 }    };
				

		/*
		 * Determine the index into the edge table which tells us which vertices
		 * are inside of the surface
		 */
		squareindex = 0;
		if (grid.val[0] < isolevel)
			squareindex |= 1;
		if (grid.val[1] < isolevel)
			squareindex |= 2;
		if (grid.val[2] < isolevel)
			squareindex |= 4;
		if (grid.val[3] < isolevel)
			squareindex |= 8;
		
		/* Cube is entirely in/out of the surface */
		if (edgeTable[squareindex] == 0)
			return (0);

		/* Find the vertices where the surface intersects the cube */
		if ((edgeTable[squareindex] & 1) != 0)
			vertlist[0] = VertexInterp(isolevel, grid.p[0], grid.p[1],
					grid.val[0], grid.val[1]);
		if ((edgeTable[squareindex] & 2) != 0)
			vertlist[1] = VertexInterp(isolevel, grid.p[1], grid.p[2],
					grid.val[1], grid.val[2]);
		if ((edgeTable[squareindex] & 4) != 0)
			vertlist[2] = VertexInterp(isolevel, grid.p[2], grid.p[3],
					grid.val[2], grid.val[3]);
		if ((edgeTable[squareindex] & 8) != 0)
			vertlist[3] = VertexInterp(isolevel, grid.p[3], grid.p[0],
					grid.val[3], grid.val[0]);
		

		/* Create the triangle */
		nlines = 0;
		for (i = 0; (lineTable[squareindex][i] != -1); i += 2) {
			PrimitiveElement3DCurve e = new PrimitiveElement3DCurve(vertlist[lineTable[squareindex][i]],vertlist[lineTable[squareindex][i + 1]]);
			e.prefs().setFillColor(Color.orange);
		    e.prefs().setCurveWidth(2);
		    if(T!=null)e.transform(T);
			lines.addElement(e);
			if (i>=2)break;
			nlines++;
		}

		return (nlines);
	}

	/*
	 * Linearly interpolate the position where an isosurface cuts an edge
	 * between two vertices, each with their own scalar value
	 */
	Vector3D VertexInterp(double isolevel, Vector3D p1, Vector3D p2,
			double valp1, double valp2)
	{
		double mu;
		Vector3D p;

		if (Math.abs(isolevel - valp1) < 0.00001)
			return (p1);
		if (Math.abs(isolevel - valp2) < 0.00001)
			return (p2);
		if (Math.abs(valp1 - valp2) < 0.00001)
			return (p1);
		mu = (isolevel - valp1) / (valp2 - valp1);
		p = Vector3D.intepolate(p1, p2, mu);
		p.setX(p1.getX() + mu * (p2.getX() - p1.getX()));
		p.setY(p1.getY() + mu * (p2.getY() - p1.getY()));
		p.setZ(p1.getZ() + mu * (p2.getZ() - p1.getZ()));

		return (p);
	}

}
