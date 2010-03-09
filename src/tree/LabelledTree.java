package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple labelled tree that can be checked to see if it is canonical by 
 * checking that each node has children with combined labels in lexicographic
 * order. A 'combined' label is the label of the node, combined with the labels
 * of the children. 
 * 
 * @author maclean
 *
 */
public class LabelledTree {
    
    /**
     * A node of the tree, with label, and references to its children
     */
    public class Node {
        
        /**
         * The label of the node
         */
        public String label;
        
        /**
         * The children of the node
         */
        public List<Node> children;
        
        /**
         * The parent of this node
         */
        public Node parent;
        
        public boolean visited;
        
        public int index;
        
        /**
         * For making the root.
         * 
         * @param label
         */
        public Node(String label) {
            this(label, null, 0);
        }
        
        /**
         * Specify the parent.
         */
        public Node(Node nodeToCopy, Node parent) {
            this(nodeToCopy.label, parent, nodeToCopy.index);
        }
        
        public Node(String label, Node parent, int index) {
            this.label = label;
            this.children = new ArrayList<Node>();
            this.parent = parent;
            this.visited = false;
            this.index = index;
        }
        
        /**
         * The number of children, plus the parent, unless this node is root.
         * 
         * @return the number of node neighbours in the tree
         */
        public int numberOfNeighbours() {
            if (this.parent == null) {
                return this.children.size();
            } else {
                return this.children.size() + 1;
            }
        }
        
        public Node getLastChild() {
            return this.children.get(children.size() - 1);
        }
        
        public boolean shouldVisitParent() {
            return this.parent != null && !this.parent.visited;
        }
        
        public boolean allChildrenVisited() {
            for (Node child : children) {
                if (!child.visited) return false;
            }
            return true;
        }
        
        public boolean hasNeighboursToVisit() {
            return children.size() > 0 && !allChildrenVisited();
        }
        
        public boolean isLeaf() {
            return this.children.size() == 0;
        }

        public String startLabel() {
            if (hasNeighboursToVisit() || shouldVisitParent()) {
                return this.label + "(";
            } else {
                return this.label;
            }
        }
        
        public String endLabel(String labelSoFar, boolean visitedParent) {
            if (this.children.size() > 0 || visitedParent) {
                return labelSoFar + ")";
            } else {
                return labelSoFar;
            }
        }
        
        public String toString() {
            return this.label + this.index;
        }
    }
    
    public class Orbit {
        
        public String label;
        
        public int count;
        
        public List<Integer> nodeIndices;
        
        public Orbit(String label, int count) {
            this.label = label;
            this.count = count;
            this.nodeIndices = new ArrayList<Integer>();
        }
        
        public Orbit(Orbit other) {
            this.label = other.label;
            this.count = other.count;
            this.nodeIndices = new ArrayList<Integer>();
            for (int nodeIndex : other.nodeIndices) {
                this.nodeIndices.add(nodeIndex);
            }
        }
        
        public void convertCountToIndex(int index) {
            this.count--;
            this.nodeIndices.add(index);
        }
        
        public boolean isEmpty() {
            return this.count == 0 && this.nodeIndices.isEmpty();
        }
        
        public boolean allUsed() {
            return this.count == 0;
        }
        
        public String toString() {
            return this.label + " " + this.count + " " + this.nodeIndices;
        }
    }
    
    private Map<String, Orbit> orbits;
    
    /**
     * The root node of the tree
     */
    private Node root;
    
    /**
     * All the nodes of the tree, in preorder traversal order
     */
    private List<Node> nodes;
    
    /**
     * The longest distance from the root to the leaves
     */
    private int height;
    
    public LabelledTree(Map<String, Integer> maxCounts) {
        this.orbits = new HashMap<String, Orbit>();
        for (String label : maxCounts.keySet()) {
            orbits.put(label, new Orbit(label, maxCounts.get(label)));
        }
        this.nodes = new ArrayList<Node>();
    }
    
    public LabelledTree(String rootLabel) {
        this.root = new Node(rootLabel);
        this.nodes = new ArrayList<Node>();
        this.nodes.add(root);
        this.height = 0;
    }
    
    public LabelledTree(char rootLabel) {
        this(String.valueOf(rootLabel));
    }
    
    public LabelledTree(LabelledTree other) {
        this.nodes = new ArrayList<Node>();
        this.root = copy(null, other.root);
        this.orbits = new HashMap<String, Orbit>();
        for (String label : other.orbits.keySet()) {
            this.orbits.put(label, new Orbit(other.orbits.get(label)));
        }
    }
    
    public void makeRoot(String label) {
        this.root = new Node(label);
        this.nodes.add(root);
    }
    
    public int getIndexForOrbitLabel(String label, Map<String, Integer> degreeMap) {
        for (int nodeIndex : orbits.get(label).nodeIndices) {
            if (isSaturated(nodeIndex, degreeMap)) {
                continue;
            } else {
                return nodeIndex;
            }
        }
        return -1;
    }
    
    public List<String> unusedLabels() {
        List<String> labels = new ArrayList<String>();
        for (String label : orbits.keySet()) {
            if (orbits.get(label).allUsed()) {
                continue;
            } else {
                labels.add(label);
            }
        }
        return labels;
    }

    public boolean isSaturated(int nodeIndex, Map<String, Integer> degreeMap) {
        Node node = nodes.get(nodeIndex);
        return degreeMap.get(node.label) <= node.numberOfNeighbours();
    }

    public boolean isComplete() {
        for (String label : orbits.keySet()) {
            if (orbits.get(label).allUsed()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    
    public boolean isEmptyOrbit(String orbitLabel) {
//        return orbits.get(orbitLabel).isEmpty();
        return orbits.get(orbitLabel).allUsed();
    }

    public String getUnsaturatedOrbitLabel(Map<String, Integer> degreeMap) {
        for (String label : orbits.keySet()) {
            if (!orbitSaturated(label)) {
                return label;
            }
        }
        return "";
    }
    
    private boolean orbitSaturated(String orbitLabel) {
        return orbits.get(orbitLabel).allUsed();
    }

    private Node copy(Node parentInThisTree, Node nodeToCopy) {
        Node thisNode = new Node(nodeToCopy, parentInThisTree);
        this.nodes.add(thisNode);
        for (Node childToCopy : nodeToCopy.children) {
            Node child = copy(thisNode, childToCopy);
            thisNode.children.add(child);
        }
        return thisNode;
    }
    
    public LabelledTree.Node getNode(int nodeIndex) {
        return nodes.get(nodeIndex);
    }
    
    public List<LabelledTree.Node> getNodes() {
        return this.nodes;
    }
    
    private int indexOfRightmostChild(Node node) {
        if (node.isLeaf()) {
            return node.index;
        } else {
            return indexOfRightmostChild(node.getLastChild());
        }
    }
    
    public void registerLabelInOrbit(String label, int index) {
        orbits.get(label).convertCountToIndex(index);
    }

    public LabelledTree.Node makeChild(int nodeIndex, String label) {
        Node parent = this.nodes.get(nodeIndex); 
        int childIndex = indexOfRightmostChild(parent) + 1;
        Node child = new Node(label, parent, childIndex);
        parent.children.add(child);
        this.nodes.add(childIndex, child);
        return child;
    }
    
    public LabelledTree growByChild(int nodeIndex, String label) {
        LabelledTree treeCopy = new LabelledTree(this);
        Node parent = treeCopy.nodes.get(nodeIndex);
        int childIndex = indexOfRightmostChild(parent) + 1;
        Node child = new Node(label, parent, childIndex);
        parent.children.add(child);
        if (childIndex < treeCopy.nodes.size()) {
            treeCopy.nodes.add(childIndex, child);
            for (int i = childIndex + 1; i < treeCopy.nodes.size(); i++) {
                treeCopy.nodes.get(i).index++;
            }
        } else {
            treeCopy.nodes.add(child);
        }
        treeCopy.registerLabelInOrbit(label, childIndex);
        return treeCopy;
    }
    
    public int numberOfNeighboursOfNode(int i) {
        return this.nodes.get(i).numberOfNeighbours();
    }

    public boolean isCanonical() {
        if (size() == 0) return true;
        resetVisitedFlags();
//        String rootString = checkForCanonicity(root);
        String rootString = toCanonicalString(root);
        if (rootString == null) {
            return false;
        } else {
            resetVisitedFlags();
            for (int i = 1; i < this.nodes.size(); i++) {
                Node node = this.nodes.get(i);
                String nodeString = checkForCanonicity(node);
//                String nodeString = toCanonicalString(node);
                if (nodeString != null && nodeString.compareTo(rootString) < 0) {
                    return false;
                }
                resetVisitedFlags();
            }
        }
        return true;
    }
    
    public String getCanonicalString(int nodeIndex) {
        resetVisitedFlags();
        return toCanonicalString(nodes.get(nodeIndex));
    }
    
    public void resetVisitedFlags() {
        for (Node node : nodes) {
            node.visited = false;
        }
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int size() {
        return this.nodes.size();
    }
    
    public static LabelledTree fromString(String s) {
        LabelledTree tree = null;
        LabelledTree.Node parent = null;
        LabelledTree.Node current = null;
        int currentHeight = 1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                parent = current;
                currentHeight++;
                tree.height = Math.max(currentHeight, tree.height);
            } else if (c == ')') {
                parent = parent.parent;
                currentHeight--;
            } else {
                if (tree == null) {
                    tree = new LabelledTree(c);
                    parent = tree.root;
                    current = parent;
                } else {
                    int parentIndex = tree.nodes.indexOf(parent);
                    current = tree.makeChild(parentIndex, String.valueOf(c));
                }
            }
        }
        return tree;
    }

    /**
     * Checks that the current node is canonical by checking that the labels of
     * the children are canonical. Since this is a recursive definition, it has
     * to signal that the labels are not sorted by returning null. There might
     * be a better non-recursive way to do this.
     * 
     * @param node the current node
     * @return the combined label of this node if it is canonical, or null if it
     *         isn't
     */
    private String checkForCanonicity(Node node) {
        node.visited = true;
        
        String label = node.startLabel();
        
        String previousChildLabel = null;
        for (Node child : node.children) {
            if (child.visited) continue;
            String childLabel = checkForCanonicity(child);
            if (childLabel == null 
                    || (previousChildLabel != null 
                            && previousChildLabel.compareTo(childLabel) > 0)) {
                return null;
            } else {
                label += childLabel;
            }
            previousChildLabel = childLabel;
        }
        boolean visitParent = node.shouldVisitParent();
        if (node.shouldVisitParent()) {
            String parentLabel = checkForCanonicity(node.parent);
            if (parentLabel == null || (previousChildLabel != null 
                            && previousChildLabel.compareTo(parentLabel) > 0)) {
                return null;
            } else {
                label += parentLabel;
            }
        }
        
       return node.endLabel(label, visitParent);
    }
    
    /**
     * Gets the canonical string form of this tree. For each node of the tree
     * the child labels are sorted and concatenated, and this combined label is
     * passed in turn to the parent. The result is a 'sorted' tree as a string. 
     * 
     * @return the canonical string form
     */
    public String toCanonicalString() {
        resetVisitedFlags();
        return toCanonicalString(root);
    }

    /**
     * Combine the labels of this node and the sorted labels of its children.
     * 
     * @param node the current node of the tree
     * @return a string that represents the canonical form of the subtree rooted
     *         at this node
     */
    private String toCanonicalString(Node node) {
        node.visited = true;
        String label = node.startLabel();
        List<String> childLabels = new ArrayList<String>();
        for (Node child : node.children) {
            if (child.visited) continue;
            childLabels.add(toCanonicalString(child));
        }
        boolean visitParent = node.shouldVisitParent(); 
        if (visitParent) {
            String parentLabel = toCanonicalString(node.parent);
            childLabels.add(parentLabel);
        }
        Collections.sort(childLabels);
        for (String childLabel : childLabels) {
            label += childLabel;
        }
        return node.endLabel(label, visitParent);
    }
    
    public String orbitStrings() {
        return orbits.toString();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (root == null) return "";
        resetVisitedFlags();
        return toString(root);
    }
    
    /**
     * Gets the tree as a string, in unsorted preorder traversal sequence. That
     * is, depth-first, from the first child to the last. 
     * 
     * @param node the current
     * @return
     */
    private String toString(Node node) {
        node.visited = true;
        String label = node.startLabel();
        for (Node child : node.children) {
            label += toString(child);
        }
        return node.endLabel(label, false);
    }

}
