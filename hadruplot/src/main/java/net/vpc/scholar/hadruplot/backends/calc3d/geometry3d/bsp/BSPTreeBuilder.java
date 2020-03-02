package net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.bsp;

import java.util.ArrayList;
import java.util.List;

import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.*;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;

/**
 * The BSPTreeBuilder class builds a BSP tree from a list of Elements. Adds
 * polygons at node and all other primitiveElement3DS at nodes (since primitiveElement3DS other than
 * polygons can not hide each other)
 * 
 * Currently, the builder does not try to optimize the order of the partitions,
 * and could be optimized by choosing partitions in an order that minimizes
 * polygon splits and provides a more balanced, complete tree.
 */

public class BSPTreeBuilder {

	/**
	 * The bsp tree currently being built.
	 */
	public BSPTree currentTree;

	/**
	 * Builds a BSP tree.
	 */
	public BSPTree build(List<PrimitiveElement3D> primitiveElement3DS) {
		currentTree = new BSPTree(createNewNode(primitiveElement3DS));
		buildNode(currentTree.getRoot());
		return currentTree;
	}

	/**
	 * Builds a node in the BSP tree.
	 */
	protected void buildNode(BSPTree.Node node) {

		// nothing to build if it's a leaf (it is no partition plane)
		if (node.isLeaf) {
			return;
		}

		// classify all polygons relative to the partition
		// (front, back, or collinear)
		ArrayList<PrimitiveElement3D> collinearList = new ArrayList<PrimitiveElement3D>();
		ArrayList<PrimitiveElement3D> frontList = new ArrayList<PrimitiveElement3D>();
		ArrayList<PrimitiveElement3D> backList = new ArrayList<PrimitiveElement3D>();
		List<PrimitiveElement3D> allPrimitiveElement3DS = node.primitiveElement3DS;
		// remove all primitiveElement3DS from list as we are going to build node
		// and we will add primitiveElement3DS according to whether they are front of back
		// to partition
		node.primitiveElement3DS = null;
		collinearList.add(allPrimitiveElement3DS.get(0));
		for (int i = 1; i < allPrimitiveElement3DS.size(); i++) {
			PrimitiveElement3D primitiveElement3D = allPrimitiveElement3DS.get(i);
			if (primitiveElement3D ==null)continue;
			if ((primitiveElement3D.prefs().isSplittable()==false) && (allPrimitiveElement3DS.get(0).prefs().isSplittable()==false)){
				collinearList.add(primitiveElement3D);
				continue;
			}
			int side = node.partition.getSide(primitiveElement3D);
			if (side == Plane3D.COLLINEAR) {
				collinearList.add(primitiveElement3D);
			} else if (side == Plane3D.FRONT) {
				frontList.add(primitiveElement3D);
			} else if (side == Plane3D.BACK) {
				backList.add(primitiveElement3D);
			} else if (side == Plane3D.SPANNING) {
				// The primitiveElement3DS which can split are Polygon , lines and arrow
				if (primitiveElement3D instanceof PrimitiveElement3DPoly) {
					PrimitiveElement3DPoly e = (PrimitiveElement3DPoly) primitiveElement3D;
					PrimitiveElement3DPoly front = new PrimitiveElement3DPoly();
					front.prefs().setFillColor( primitiveElement3D.prefs().getFillColor());
					front.prefs().setCurveWidth(primitiveElement3D.prefs().getCurveWidth());
					front.setFilled(primitiveElement3D.isFilled());
					front.prefs().setDashed(primitiveElement3D.prefs().isDashed());
					front.prefs().setSplittable(primitiveElement3D.prefs().isSplittable());
					front.prefs().setBackColor(e.prefs().getBackColor());
					front.drawContours=e.drawContours;
					PrimitiveElement3DPoly back = new PrimitiveElement3DPoly();
					front.prefs().setLineColor(e.prefs().getLineColor());
					back.prefs().setLineColor(e.prefs().getLineColor());
					back.prefs().setFillColor( primitiveElement3D.prefs().getFillColor());
					back.prefs().setCurveWidth(primitiveElement3D.prefs().getCurveWidth());
					back.setFilled(primitiveElement3D.isFilled());
					back.prefs().setDashed(primitiveElement3D.prefs().isDashed());
					back.prefs().setBackColor(e.prefs().getBackColor());
					back.prefs().setSplittable(primitiveElement3D.prefs().isSplittable());
					front.prefs().setDashed(e.prefs().isDashed());
					back.prefs().setDashed(e.prefs().isDashed());
					back.drawContours=e.drawContours;
					
					node.partition.splitPoly(e.vertices, front.vertices,
							back.vertices);

					if (front.vertices.size() > 2) {
						front.reCalculateNormalandCentre();
						if (null!=front.normal)frontList.add(front);
					}
					if (back.vertices.size() > 2) {
						back.reCalculateNormalandCentre();
						if (null!=back.normal)backList.add(back);
					}

				} else if (primitiveElement3D instanceof PrimitiveElement3DCurve) {
					PrimitiveElement3DCurve e = (PrimitiveElement3DCurve) primitiveElement3D;
					Vector3D vi = node.partition.getIntersection(e.p1, e.p2);
					PrimitiveElement3DCurve front, back;
					{
						if (node.partition.getSideThick(e.p1) == Plane3D.FRONT) {
							front = new PrimitiveElement3DCurve(e.p1, vi);
							back = new PrimitiveElement3DCurve(e.p2, vi);
						} else {
							back = new PrimitiveElement3DCurve(e.p1, vi);
							front = new PrimitiveElement3DCurve(e.p2, vi);
						}
						front.prefs().setFillColor( e.prefs().getFillColor());
						front.prefs().setBackColor(e.prefs().getBackColor());
						back.prefs().setFillColor( e.prefs().getFillColor());
						front.prefs().setLineColor( e.prefs().getLineColor());
						back.prefs().setLineColor( e.prefs().getLineColor());
						front.prefs().setCurveWidth(e.prefs().getCurveWidth());
						back.prefs().setCurveWidth(e.prefs().getCurveWidth());
						front.setRenderable(e.isRenderable());
						back.setRenderable(e.isRenderable());
						back.prefs().setBackColor(e.prefs().getBackColor());
						front.prefs().setDashed(e.prefs().isDashed());
						back.prefs().setDashed(e.prefs().isDashed());
						frontList.add(front);
						backList.add(back);
					}
				} else if (primitiveElement3D instanceof PrimitiveElement3DArrow) {
					PrimitiveElement3DArrow e = (PrimitiveElement3DArrow) primitiveElement3D;
					Vector3D vi = node.partition.getIntersection(e.p1, e.p2);
					{   
						PrimitiveElement3D front,back;
						if (node.partition.getSideThick(e.p1) == Plane3D.FRONT) {
							
							if (e instanceof PrimitiveElement3DRuler){
								front = new PrimitiveElement3DRuler(e.p1, vi,(PrimitiveElement3DRuler) e);
								back = new PrimitiveElement3DRuler( vi,e.p2,(PrimitiveElement3DRuler) e);
							}else{
							    front = new PrimitiveElement3DCurve(e.p1, vi);
							    back = new PrimitiveElement3DArrow( vi,e.p2);
							    ((PrimitiveElement3DArrow)back).setArrowSize(e.getArrowSize());
							}
							front.prefs().setFillColor( e.prefs().getFillColor());
							front.prefs().setFillColor( e.prefs().getFillColor());
							front.prefs().setLineColor( e.prefs().getLineColor());
							front.prefs().setLineColor( e.prefs().getLineColor());
							back.prefs().setLineColor( e.prefs().getLineColor());
							back.prefs().setCurveWidth(e.prefs().getCurveWidth());
							back.prefs().setBackColor(e.prefs().getBackColor());
							front.prefs().setCurveWidth(e.prefs().getCurveWidth());
							front.prefs().setDashed(e.prefs().isDashed());
							back.prefs().setDashed(e.prefs().isDashed());
							frontList.add(front);
							backList.add(back);
						} else {
							
							if (e instanceof PrimitiveElement3DRuler){
								front = new PrimitiveElement3DRuler(vi, e.p2,(PrimitiveElement3DRuler) e);
								back = new PrimitiveElement3DRuler( e.p1,vi,(PrimitiveElement3DRuler) e);
							}else{
							    front =  new PrimitiveElement3DArrow(vi,e.p2);
							    back = new PrimitiveElement3DCurve(e.p1, vi);
							    ((PrimitiveElement3DArrow)front).setArrowSize(e.getArrowSize());
							}
							// back = new PrimitiveElement3DCurve(e.p1, vi);
							// front = new PrimitiveElement3DArrow(vi,e.p2);
							front.prefs().setFillColor( e.prefs().getFillColor());
							back.prefs().setFillColor( e.prefs().getFillColor());
							front.prefs().setLineColor( e.prefs().getLineColor());
							back.prefs().setLineColor( e.prefs().getLineColor());
							front.prefs().setCurveWidth(e.prefs().getCurveWidth());
							back.prefs().setCurveWidth(e.prefs().getCurveWidth());
							front.prefs().setDashed(e.prefs().isDashed());
							back.prefs().setDashed(e.prefs().isDashed());
							
							frontList.add(front);
							backList.add(back);
						}
						
					}
				
				}

			}
		}

		if (allPrimitiveElement3DS.get(0).prefs().isSplittable()==false){
			collinearList.addAll(backList);
			collinearList.addAll(frontList);
			frontList.clear();
			backList.clear();
		}
		// clean and assign lists
		collinearList.trimToSize();
		frontList.trimToSize();
		backList.trimToSize();
		node.primitiveElement3DS = collinearList;
		// build front and back nodes
		if (frontList.size() > 0) {
			node.front = createNewNode(frontList);
			buildNode(node.front);
		}
		if (backList.size() > 0) {
			node.back = createNewNode(backList);
			buildNode(node.back);
		}

	}

	/**
	 * Creates a new node from a list of polygons. If none of the polygons are
	 * walls, a leaf is created.
	 */
	protected BSPTree.Node createNewNode(List<PrimitiveElement3D> primitiveElement3DS) {

		Plane3D partition = choosePartition(primitiveElement3DS);
		BSPTree.Node node = new BSPTree.Node();
		node.primitiveElement3DS = primitiveElement3DS;
		node.partition = partition;
		// no partition available, so it's a leaf
		if (partition == null)
			node.isLeaf = true;
		return node;
	}

	/**
	 * Chooses a polygon from a list of primitiveElement3DS (if exists) to use as a
	 * partition. This method just returns the first polygon found in list of
	 * primitiveElement3DS or null if none found. A smarter method would choose a partition
	 * that minimizes polygon splits and provides a more balanced, complete
	 * tree.
	 */
	protected Plane3D choosePartition(List<PrimitiveElement3D> primitiveElement3DS) {
		for (PrimitiveElement3D primitiveElement3D : primitiveElement3DS) {
			if (primitiveElement3D instanceof PrimitiveElement3DPoly) {
				PrimitiveElement3DPoly e=(PrimitiveElement3DPoly) primitiveElement3D;
				primitiveElement3DS.remove(primitiveElement3D);
				primitiveElement3DS.add(0,e);
				return ((PrimitiveElement3DPoly) primitiveElement3D).getPlane3D();
			}
		}
		// no Polygon found so no partition is there in list of primitiveElement3DS
		return null;
	}

}