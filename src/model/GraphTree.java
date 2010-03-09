package model;

import java.util.ArrayList;

public class GraphTree {
    
    public class TreeNode {
        
        public ArrayList<TreeNode> children = new ArrayList<TreeNode>();
        
        public Graph graph;
        
        public TreeNode parent;
        
        public TreeNode(Graph graph, TreeNode parent) {
            this.graph = graph;
            this.parent = parent;
        }
        
        public boolean isLeaf() {
            return children.size() == 0;
        }
        
    }
    
    private TreeNode root;
    
    public GraphTree() {
        root = null;
    }
    
    public GraphTree(Graph graph) {
        root = new TreeNode(graph, null);
    }
    
    public TreeNode getRoot() {
        return this.root;
    }
    
    public ArrayList<Graph> getLeaves() {
        return getLeaves(root, new ArrayList<Graph>());
    }
    
    private ArrayList<Graph> getLeaves(TreeNode node, ArrayList<Graph> leaves) {
        if (node.isLeaf()) {
            leaves.add(node.graph);
        } else {
            for (TreeNode child : node.children) {
                getLeaves(child, leaves);
            }
        }
        return leaves;
    }
    
    public ArrayList<TreeNode> getLeafNodes() {
        return getLeafNodes(root, new ArrayList<TreeNode>());
    }
    
    private ArrayList<TreeNode> getLeafNodes(TreeNode node, ArrayList<TreeNode> leaves) {
        if (node.isLeaf()) {
            leaves.add(node);
        } else {
            for (TreeNode child : node.children) {
                getLeafNodes(child, leaves);
            }
        }
        return leaves;
    }
    
    public void addGraphToParent(Graph child, Graph parent) {
        if (root == null) {
            root = new TreeNode(child, null);
        } else {
            TreeNode treeNode = find(parent);
            treeNode.children.add(new TreeNode(child, treeNode));
        }
    }
    
    public TreeNode find(Graph graph) {
        return find(graph, root);
    }
    
    private TreeNode find(Graph graph, TreeNode node) {
        if (node.graph == graph) {
            return node;
        } else {
            for (TreeNode child : node.children) {
                TreeNode childSearch = find(graph, child); 
                if (childSearch != null) {
                    return childSearch;
                }
            }
        }
        return null;
    }
    
    public Iterable<Graph> graphs() {
        ArrayList<Graph> graphs = new ArrayList<Graph>();
        
        graphs.add(root.graph);
        getGraphs(root, graphs);
        
        return graphs;
    }
    
    private void getGraphs(TreeNode currentNode, ArrayList<Graph> graphs) {
        for (TreeNode child : currentNode.children) {
            graphs.add(child.graph);
            getGraphs(child, graphs);
        }
    }
    
    public String toString() {
        return toString(root, new StringBuffer()).toString();
    }
    
    private StringBuffer toString(TreeNode node, StringBuffer buffer) {
        buffer.append("[").append(node.graph.toString()).append("]");
        for (TreeNode child : node.children) {
            toString(child, buffer);
        }
        return buffer;
    }

}
