package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.bsp;
import java.util.List;

import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.PrimitiveElement3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d.Plane3D;

/**
    The BSPTree class represents a 2D Binary Space Partitioned
    tree of polygons. The BSPTree is built using a BSPTreeBuilder
    class, and can be traversed using BSPTreeTraverser class.
*/
public class BSPTree {

    /**
        A Node of the tree. All children of the node are either
        to the front or back of the node's partition.
    */
    public static class Node {
        public Node front;
        public Node back;
        /**Plane needed for partitioning  primitiveElement3DS conatained by node in front or back.*/
        public Plane3D partition;
        /**the list of primitiveElement3DS is a list of all the wall polygons collinear with the partition.*/
        public List<PrimitiveElement3D> primitiveElement3DS;
        /** A Leaf of the tree. A leaf has no partition or front or back nodes. */
        boolean isLeaf=false;
    }

    /** Root node (Starting node of tree), tree building and traversing both start from this node*/
    private Node root;

    /**
        Creates a new BSPTree with the specified root node.
    */
    public BSPTree(Node root) {
       this.root = root;
    }

    /**
        Gets the root node of this tree.
    */
    public Node getRoot() {
        return root;
    }
}