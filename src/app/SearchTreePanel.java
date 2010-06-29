package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import org.openscience.cdk.structgen.deterministic.Graph;

public class SearchTreePanel extends JPanel implements Scrollable {
    
    public class TreeLayout {
        
        public int totalLeafCount = 0;
        
        public int xSep = 10;
        
        public int ySep = 15;
        
        public void layoutTree(Node root, int width, int height) {
            int leafCount = root.countLeaves();
//            this.xSep = width / (maxDepth + 1);
//            this.ySep = height / (leafCount + 1);
            treeWidth = xSep * (maxDepth + 1);
            treeHeight = ySep * (leafCount + 1);
            System.out.println(
                    "Laying out " + xSep + " " + ySep + " " + leafCount
                    + " " + width + " " + height);
            layout(root);
        }
        
        public int layout(Node node) {
            node.x = node.depth * xSep;
            if (node.isLeaf()) {
                totalLeafCount += 1;
                node.y = totalLeafCount * ySep;
                System.out.println("LEAF x=" + node.x + " y=" + node.y + " d=" + node.depth);
                return node.y;
            } else {
                int min = 0;
                int max = 0;
                for (Node child : node.children) {
                    int childCenter = layout(child);
                    if (min == 0) {
                        min = childCenter;
                    }
                    max = childCenter;
                }
                if (min == max) {
                    node.y = min;
                } else {
                    node.y = min + (max - min) / 2;
                }
                System.out.println("NODE x=" + node.x + " y=" + node.y + " d=" + node.depth);
                return node.y;
            }
        }
        
    }

    private class Node {
        
        public Graph graph;
        
        public Node parent;
        
        public List<Node> children;
        
        public int x;
        
        public int y;
        
        private int depth;
        
        private boolean selected;
        
        private boolean solution;
        
        public Node(Node parent, Graph graph) {
            this.graph = graph;
            this.parent = parent;
            this.children = new ArrayList<Node>();
            if (parent == null) {
                depth = 0;
            } else {
                depth = parent.depth + 1;
            }
            selected = false;
            if (graph.isConnected() && graph.isFullySaturated()) {
                solution = true;
            } else {
                solution = false;
            }
        }
        
        public Node addChild(Graph child) {
            Node childNode = new Node(this, child);
            this.children.add(childNode);
            return childNode;
        }
        
        public void toBranch(List<Graph> branch) {
            branch.add(graph);
            if (parent != null) {
                parent.toBranch(branch);
            }
        }
        
        public void selectBranch(boolean value) {
            this.selected = value;
            if (parent != null) {
                parent.selectBranch(value);
            }
        }
        
        public int countLeaves() {
            if (this.isLeaf()) {
                return 1;
            } else {
                int c = 0;
                for (Node child : this.children) {
                    c += child.countLeaves();
                }
                return c;
            }
        }
        
        public boolean isLeaf() {
            return this.children.size() == 0;
        }
    }
    
    private int maxDepth;

    private Node root;
    
    private List<Node> nodes;
    
    private int set_width;
    
    private int set_height;
    
    private int treeWidth;
    
    private int treeHeight;
    
    private boolean laidOut = false;
    
    private Node selected;
    
    public SearchTreePanel(int width, int height) {
        nodes = new ArrayList<Node>();
        maxDepth = -1;
        set_width = width;
        set_height = height;
    }
    
    public void addRelation(Graph parent, Graph child) {
        int depth = -1;
        if (root == null) {
            root = new Node(null, child);
            nodes.add(root);
            depth = 0;
        } else {
            Node parentNode = findParentNode(parent);
            if (parentNode != null) {
                Node childNode = parentNode.addChild(child);
                nodes.add(childNode);
                depth = parentNode.depth + 1;
            }
        }
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }
    
    private Node findParentNode(Graph parent) {
        for (Node node : nodes) {
            if (node.graph == parent) return node;
        }
        return null;
    }
    
    public List<Graph> getBranch(Node tip) {
        List<Graph> branch = new ArrayList<Graph>();
        tip.toBranch(branch);
        return branch;
    }
    
    public void select(int x, int y) {
        // TODO : more efficient search
        double closestDistance = -1;
        Node closest = null;
        for (Node node : nodes) {
            double distanceSQ = ((node.x - x) * (node.x - x)) 
                              + ((node.y - y) * (node.y - y));  
            if (closest == null || distanceSQ < closestDistance) {
                closest = node;
                closestDistance = distanceSQ;
            }
        }
        if (selected != null) {
            selected.selectBranch(false);   
        }
        selected = closest;
        selected.selectBranch(true);
    }
    
    public void selectBranch(Node tip, boolean value) {
        tip.selectBranch(value);
    }
    
    public Graph getSelectedGraph() {
        if (selected == null) {
            return null;
        } else {
            return selected.graph;
        }
    }
    
    public List<Graph> getSelectedBranch() {
        if (selected == null) {
            return new ArrayList<Graph>();
        } else {
            return getBranch(selected);
        }
    }
    
    public void paint(Graphics g) {
        if (root == null) return;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, set_width, set_height);
       
        if (!laidOut) {
            TreeLayout layout = new TreeLayout();
            layout.layoutTree(root, this.set_width, this.set_height);
            setPreferredSize(new Dimension(treeWidth, treeHeight));
            System.out.println(
                    "setting preferred size " + treeWidth + " x " + treeHeight);
        }
        g.setColor(Color.BLACK);
        paint(g, root);
        if (!laidOut) {
            revalidate();
            laidOut = true;
        }
    }
    
    public void paint(Graphics g, Node node) {
        for (Node child : node.children) {
            if (node.selected && child.selected) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawLine(node.x, node.y, child.x, child.y);
            paint(g, child);
        }
        if (node.selected) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.BLACK);
        }
        int d = 4;
        int r = d / 2;
        if (node.solution) {
            g.setColor(Color.RED);
        }
        g.fillOval(node.x - r, node.y - r, d, d);
        g.setColor(Color.BLACK);
    }

    public void clear() {
        this.root = null;
        this.laidOut = false;
        this.nodes.clear();
        this.maxDepth = -1;
    }

    public Dimension getPreferredScrollableViewportSize() {
//        return new Dimension(set_width, set_height);
        return getPreferredSize();
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        return 10;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        return 1;
    }
}
