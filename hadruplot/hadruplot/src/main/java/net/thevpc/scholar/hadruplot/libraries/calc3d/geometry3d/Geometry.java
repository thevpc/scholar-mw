package net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import net.thevpc.scholar.hadruplot.libraries.calc3d.core.LocMessages;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Epsilon;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

/**
 * Contains static methods to perform standard geometric operations.
 * @author William Bittle
 * @version 3.0.1
 * @since 1.0.0
 */
public class Geometry {
	/** 2 * PI constant */
	public static final double TWO_PI = 2.0 * Math.PI;
	/** The value of 1/3 */
	private static final double INV_3 = 1.0 / 3.0;
	
	/** The value of the inverse of the square root of 3; 1/sqrt(3) */
	private static final double INV_SQRT_3 = 1.0 / Math.sqrt(3.0);
	
	/**
	 * Returns the area weighted centroid for the given points.
	 * <p>
	 * A {@link Polygon}'s centroid must be computed by the area weighted method since the
	 * average method can be bias to one side if there are more points on that one
	 * side than another.
	 * <p>
	 * Finding the area of a {@link Polygon} can be done by using the following
	 * summation:
	 * <pre>
	 * 0.5 * &sum;(x<sub>i</sub> * y<sub>i + 1</sub> - x<sub>i + 1</sub> * y<sub>i</sub>)
	 * </pre>
	 * Finding the area weighted centroid can be done by using the following
	 * summation:
	 * <pre>
	 * 1 / (6 * A) * &sum;(p<sub>i</sub> + p<sub>i + 1</sub>) * (x<sub>i</sub> * y<sub>i + 1</sub> - x<sub>i + 1</sub> * y<sub>i</sub>)
	 * </pre>
	 * @param points the {@link Polygon} points
	 * @return {@link Vector2} the area weighted centroid
	 * @throws NullPointerException if points is null or an primitiveElement3D of points is null
	 * @throws IllegalArgumentException if points is empty
	 */
	public static final Vector3D getAreaWeightedCenter(List<Vector3D> points) {
		// check for null list
		if (points == null) throw new NullPointerException(LocMessages.getString("geometry.nullPointList"));
		// check for empty list
		if (points.isEmpty()) throw new IllegalArgumentException(LocMessages.getString("geometry.invalidSizePointList1"));
		// check for list of one point
		int size = points.size();
		if (size == 1) {
			Vector3D p = points.get(0);
			// check for null
			if (p == null) throw new NullPointerException(LocMessages.getString("geometry.nullPointListElements"));
			return p.clone();
		}
		// otherwise perform the computation
		Vector3D center = new Vector3D();
		double area = 0.0;
		// loop through the vertices
		for (int i = 0; i < size; i++) {
			// get two verticies
			Vector3D p1 = points.get(i);
			Vector3D p2 = i + 1 < size ? points.get(i + 1) : points.get(0);
			// check for null
			if (p1 == null || p2 == null) throw new NullPointerException(LocMessages.getString("geometry.nullPointListElements"));
			// perform the cross product (yi * x(i+1) - y(i+1) * xi)
			double d = p1.CrossProduct(p2).getLength();
			// multiply by half
			double triangleArea = 0.5 * d;
			// add it to the total area
			area += triangleArea;

			// area weighted centroid
			// (p1 + p2) * (D / 3)
			// = (x1 + x2) * (yi * x(i+1) - y(i+1) * xi) / 3
			// we will divide by the total area later
			center.add(p1.add(p2).scale(INV_3).scale(triangleArea));
		}
		// check for zero area
		if (Math.abs(area) <= Epsilon.E) {
			// zero area can only happen if all the points are the same point
			// in which case just return a copy of the first
			return points.get(0).clone();
		}
		// finish the centroid calculation by dividing by the total area
		center.scaleEq(1.0 / area);
		// return the center
		return center;
	}

	
	/**
	 * Returns a new {@link Polygon} with the given vertices.
	 * <p>
	 * This method makes a copy of both the array and the vertices within the array to
	 * create the new {@link Polygon}.
	 * <p>
	 * The center of the {@link Polygon} will be computed using the area weighted method.
	 * @param vertices the array of vertices
	 * @return {@link Polygon}
	 * @throws NullPointerException if vertices is null or an primitiveElement3D of vertices is null
	 * @throws IllegalArgumentException if vertices contains less than 3 non-null vertices
	 * @see #createPolygonAtOrigin(Vector3D...) to create a new {@link Polygon} that is centered on the origin
	 */
	public static final PrimitiveElement3DPoly createPolygon(Vector3D... vertices) {
		// check the vertices array
		if (vertices == null) throw new NullPointerException(LocMessages.getString("geometry.nullVerticesArray"));
		// loop over the points an copy them
		int size = vertices.length;
		// check the size
		Vector3D[] verts = new Vector3D[size];
		for (int i = 0; i < size; i++) {
			Vector3D vertex = vertices[i];
			// check for null points
			if (vertex != null) {
				verts[i] = vertex.clone();
			} else {
				throw new NullPointerException(LocMessages.getString("geometry.nullPolygonPoint"));
			}
		}
		return new PrimitiveElement3DPoly(verts);
	}
	


	/**
	 * Returns a new {@link Polygon} object with count number of points, where the
	 * points are evenly distributed around the unit circle.  The resulting {@link Polygon}
	 * will be centered on the origin.
	 * <p>
	 * The radius parameter is the distance from the center of the polygon to each vertex.
	 * @see #createUnitCirclePolygon(int, double, double)
	 * @param count the number of vertices
	 * @param radius the radius from the center to each vertex in meters
	 * @return {@link Polygon}
	 * @throws IllegalArgumentException if count is less than 3 or radius is less than or equal to zero
	 */
	public static final PrimitiveElement3DPoly createUnitCirclePolygon(int count, double radius) {
		return Geometry.createUnitCirclePolygon(count, radius, 0.0);
	}

	/**
	 * Returns a new {@link Polygon} object with count number of points, where the
	 * points are evenly distributed around the unit circle.  The resulting {@link Polygon}
	 * will be centered on the origin.
	 * <p>
	 * The radius parameter is the distance from the center of the polygon to each vertex.
	 * <p>
	 * The theta parameter is a vertex angle offset used to rotate all the vertices
	 * by the given amount.
	 * @param count the number of vertices
	 * @param radius the radius from the center to each vertex in meters
	 * @param theta the vertex angle offset in radians
	 * @return {@link Polygon}
	 * @throws IllegalArgumentException if count is less than 3 or radius is less than or equal to zero
	 */
	public static final PrimitiveElement3DPoly createUnitCirclePolygon(int count, double radius, double theta) {
		// check the count
		if (count < 3) throw new IllegalArgumentException(LocMessages.getString("geometry.invalidVerticesSize"));
		// check the radius
		if (radius <= 0.0) throw new IllegalArgumentException(LocMessages.getString("geometry.invalidRadius"));
		Vector3D[] verts = new Vector3D[count];
		double angle = Geometry.TWO_PI / count;
		for (int i = count - 1; i >= 0; i--) {
			verts[i] = new Vector3D(Math.cos(angle * i + theta) * radius, Math.sin(angle * i + theta) * radius,0);
		}
		return new PrimitiveElement3DPoly(verts);
	}
	

	

	
	/**
	 * Returns a new list containing the 'cleansed' version of the given listing of polygon points.
	 * <p>
	 * This method ensures the polygon has CCW winding, removes colinear vertices, and removes coincident vertices.
	 * <p>
	 * If the given list is empty, the list is returned.
	 * @param points the list polygon points
	 * @return List&lt;{@link Vector3D}&gt;
	 * @throws NullPointerException if points is null or if points contains null primitiveElement3DS
	 */
	public static final List<Vector3D> cleanse(List<Vector3D> points) {
		// check for null list
		if (points == null) throw new NullPointerException(LocMessages.getString("geometry.nullPointList"));
		// get the size of the points list
		int size = points.size();
		// check the size
		if (size == 0) return points;
		// create a result list
		List<Vector3D> result = new ArrayList<Vector3D>(size);
		
		double winding = 0.0;
		
		// loop over the points
		for (int i = 0; i < size; i++) {
			// get the current point
			Vector3D point = points.get(i);
			
			// get the adjacent points
			Vector3D prev = points.get(i - 1 < 0 ? size - 1 : i - 1);
			Vector3D next = points.get(i + 1 == size ? 0 : i + 1);
			
			// check for null
			if (point == null || prev == null || next == null)
				throw new NullPointerException(LocMessages.getString("geometry.nullPointListElements"));
			
			// is this point equal to the next?
			Vector3D diff = point.subtract(next);
			if (diff.isZero()) {
				// then skip this point
				continue;
			}
			
			// create the edge vectors
			Vector3D prevToPoint = point.subtract(prev);
			Vector3D pointToNext = next.subtract(point);
			
			// check if the previous point is equal to this point
			
			// since the next point is not equal to this point
			// if this is true we still need to add the point because
			// it is the last of a string of coincident vertices
			if (!prevToPoint.isZero()) {
				// compute the cross product
				double cross = prevToPoint.CrossProduct(pointToNext).getLengthSq();
				
				// if the cross product is near zero then point is a colinear point
				if (Math.abs(cross) <= Epsilon.E) {
					continue;
				}
			}
			
			// otherwise the point is valid
			result.add(point);
		}
		
		return result;
	}
	
	/**
	 * Returns a new array containing the 'cleansed' version of the given array of polygon points.
	 * <p>
	 * This method ensures the polygon has CCW winding, removes colinear vertices, and removes coincident vertices.
	 * @param points the list polygon points
	 * @return {@link Vector3D}[]
	 * @throws NullPointerException if points is null or points contains null primitiveElement3DS
	 */
	public static Vector3D[] cleanse(Vector3D... points) {
		// check for null
		if (points == null) throw new NullPointerException(LocMessages.getString("geometry.nullPointArray"));
		// create a list from the array
		List<Vector3D> pointList = Arrays.asList(points);
		// cleanse the list
		List<Vector3D> resultList = Geometry.cleanse(pointList);
		// convert it back to an array
		Vector3D[] result = new Vector3D[resultList.size()];
		resultList.toArray(result);
		// return the result
		return result;
	}
}
