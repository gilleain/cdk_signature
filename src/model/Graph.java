package model;

import java.util.ArrayList;

/**
 * A simple graph class that does only what I need :) To eventually be replaced
 * by just using an Atom Container directly.
 * 
 * @author maclean
 *
 */
public class Graph {
    
    private ArrayList<Node> nodes = new ArrayList<Node>();
    
    private ArrayList<Arc> arcs = new ArrayList<Arc>();

    public Graph(String nodeLabel, int nodeCount) {
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node(nodeLabel));
        }
    }
    
    public Graph(Graph other) {
        for (Node node : other.nodes) {
            this.nodes.add(new Node(node));
        }
        
        for (Arc arc : other.arcs) {
            Node left = this.nodes.get(other.indexOf(arc.getLeft()));
            Node right = this.nodes.get(other.indexOf(arc.getRight()));
            this.arcs.add(new Arc(left, right, arc.getOrder()));
        }
    }
    
    public boolean inPartialOrder() {
        for (int i = 1; i < this.arcs.size(); i++) {
            Arc a = this.arcs.get(i - 1);
            Arc b = this.arcs.get(i);
            int al = indexOf(a.getLeft());
            int ar = indexOf(a.getRight());
            int bl = indexOf(b.getLeft());
            int br = indexOf(b.getRight());
            if (al < bl || (al == bl && ar < br)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object o) {
        if (o instanceof Graph) {
            Graph other = (Graph) o;
            for (Arc arc : arcs) {
                int l = indexOf(arc.getLeft());
                int r = indexOf(arc.getRight());
                if (hasMatchingArc(l, r, other)) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    public boolean hasMatchingArc(int l, int r, Graph other) {
        for (Arc otherArc : other.arcs) {
            if (other.indexOf(otherArc.getLeft()) == l &&
                    other.indexOf(otherArc.getRight()) == r) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<Group>();
        for (Node node : nodes) {
            int degree = getDegree(node);
            Group group = getGroupWithDegree(groups, degree);
            if (group == null) {
                group = new Group(this, degree);
                groups.add(group);
            }
            group.memberIndices.add(nodes.indexOf(node));
        }
        return groups;
    }
    
    private Group getGroupWithDegree(ArrayList<Group> groups, int degree) {
        for (Group group : groups) {
            if (group.degree == degree) {
                return group;
            }
        }
        return null;
    }
    
    public int getDegree(Node node) {
        int degree = 0;
        for (Arc arc : arcs) {
            if (arc.contains(node)) degree++;
        }
        return degree;
    }
    
    public Node getNode(int i) {
        return nodes.get(i);
    }
    
    public int indexOf(Node node) {
        return nodes.indexOf(node);
    }
    
    public void addArc(int leftIndex, int rightIndex) {
        this.addArc(leftIndex, rightIndex, 1);
    }
    
    public void addArc(int leftIndex, int rightIndex, int order) {
        Node left = nodes.get(leftIndex);
        Node right = nodes.get(rightIndex);
        addArc(left, right, order);
    }
    
    public void addArc(Node left, Node right) {
        this.addArc(left, right, 1);
    }
    
    public void addArc(Node left, Node right, int order) {
        Arc arc = new Arc(left, right, order);
        arc.isNew = true;
        arcs.add(arc);
    }
    
    public Iterable<Node> nodes() {
        return nodes;
    }
    
    public Iterable<Arc> arcs() {
        return arcs;
    }
    
    public int nodeCount() {
        return nodes.size();
    }
    
    public int arcCount() {
        return arcs.size();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Arc arc : arcs) {
            sb.append(indexOf(arc.getLeft()));
            sb.append(":");
            sb.append(indexOf(arc.getRight()));
            sb.append("(");
            sb.append(arc.getOrder());
            sb.append(") ");
        }
        int lastChar = sb.length() - 1;
        if (lastChar > 0) {
            sb.deleteCharAt(lastChar);
        }
        return sb.toString();
    }
    
    public boolean noArcBetween(int leftIndex, int rightIndex) {
        Node left = nodes.get(leftIndex);
        Node right = nodes.get(rightIndex);
        for (Arc arc : arcs) {
            if (arc.contains(left) && arc.contains(right)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean inAnEdge(Node node) {
        for (Arc arc : this.arcs) {
            if (arc.contains(node)) {
                return true;
            }
        }
        return false;
    }

    public boolean fullyConnected() {
        for (Node node : this.nodes) {
            if (this.inAnEdge(node)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
