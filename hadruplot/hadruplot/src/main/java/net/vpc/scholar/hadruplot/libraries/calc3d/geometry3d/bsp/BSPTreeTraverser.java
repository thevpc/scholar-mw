package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.bsp;

import java.util.ArrayList;

import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Plane3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

/**
 * A BSPTreeTraverer traverses a 2D BSP tree either with a in-order or
 * draw-order (front-to-back) order. Visited polygons are signaled using a
 * BSPTreeTraverseListener.
 */

public class BSPTreeTraverser {

	private boolean traversing;

	private double x;
	private double y;
	private double z;

	// private GameObjectManager objectManager;

	private BSPTreeTraverseListener listener;

	/**
	 * Creates a new BSPTreeTraverser with no BSPTreeTraverseListener.
	 */
	public BSPTreeTraverser() {
		this(null);
	}

	/**
	 * Creates a new BSPTreeTraverser with the specified
	 * BSPTreeTraverseListener.
	 */
	public BSPTreeTraverser(BSPTreeTraverseListener listener) {
		setListener(listener);
	}

	/**
	 * Sets the BSPTreeTraverseListener to use during traversals.
	 */
	public void setListener(BSPTreeTraverseListener listener) {
		this.listener = listener;
	}

	/**
	 * Traverses a tree in draw-order (front-to-back) using the specified view
	 * location.
	 */
	public void traverse(BSPTree tree, Vector3D viewLocation) {
		x = viewLocation.getX();
		y = viewLocation.getY();
		z = viewLocation.getZ();
		traversing = true;
		traverseDrawOrder(tree.getRoot());
	}

	/**
	 * Traverses the tree starting from its root
	 * @return 
	 */
	public ArrayList<PrimitiveElement3D> getAllElementsofTree(BSPTree tree) {
		//traversing = true;
		ArrayList <PrimitiveElement3D> primitiveElement3DS =new ArrayList <PrimitiveElement3D>();
		AddallElementsOfNode(tree.getRoot(), primitiveElement3DS);
		return primitiveElement3DS;
	}

	/**
	 * Testing mehod.
	 */
	private void AddallElementsOfNode(BSPTree.Node node,ArrayList <PrimitiveElement3D> primitiveElement3DS) {
		if ( node != null) {
			AddallElementsOfNode(node.back, primitiveElement3DS);
			AddElementsofNode(node, primitiveElement3DS);
			AddallElementsOfNode(node.front, primitiveElement3DS);
		}
	}

	private void AddElementsofNode(BSPTree.Node node,ArrayList <PrimitiveElement3D> primitiveElement3DS) {
		if (node.primitiveElement3DS == null) return;
		primitiveElement3DS.addAll(node.primitiveElement3DS);
	}
	
	/**
	 * Traverses a tree in in-order.
	 */
	public void traverse(BSPTree tree) {
		traversing = true;
		traverseInOrder(tree.getRoot());
	}

	/**
	 * Traverses a node in draw-order (front-to-back) using the current view
	 * location.
	 */
	private void traverseDrawOrder(BSPTree.Node node) {
		if (traversing && node != null) {

			if (node.isLeaf==true) {
				// no partition, just handle polygons
				visitNode(node);
				
			} else if (node.partition.getSideThick(x, y, z) == Plane3D.COLLINEAR) {
					traverseDrawOrder(node.back);
					visitNode(node);
					traverseDrawOrder(node.front);
			} else if (node.partition.getSideThin(x, y, z) == Plane3D.BACK) {
				traverseDrawOrder(node.front);
				visitNode(node);
				traverseDrawOrder(node.back);
			} else {
				traverseDrawOrder(node.back);
				visitNode(node);
				traverseDrawOrder(node.front);
			}
	    }

	}

	/**
	 * Traverses a node in in-order.
	 */
	private void traverseInOrder(BSPTree.Node node) {
		if (traversing && node != null) {
			traverseInOrder(node.front);
			visitNode(node);
			traverseInOrder(node.back);
		}
	}

	/**
	 * Visits a node in the tree. The BSPTreeTraverseListener's visitPolygon()
	 * method is called for every polygon in the node.
	 */
	private void visitNode(BSPTree.Node node) {
		if (!traversing || node.primitiveElement3DS == null) return;
		// visit every primitiveElement3D of polygon, Listener manages rendering of primitiveElement3DS
		listener.visitElements(node.primitiveElement3DS);
	}

}
