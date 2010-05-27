package org.openscience.cdk.structgen.deterministic;

import java.util.Arrays;
import java.util.Comparator;

import signature.DAGVisitor;
import signature.DAG.Node;

public class CanonicalDAGVisitor implements DAGVisitor {

    public int[] labels;
    
    public int index;
    
    public Comparator<Node> comparator;
    
    public CanonicalDAGVisitor(int n) {
//    public CanonicalDAGVisitor(int n, Comparator<Node> comparator) {
        this.labels = new int[n];
        Arrays.fill(labels, -1);
        this.index = 0;
//        this.comparator = comparator;
    }
    
    public void visit(Node node) {
        if (labels[node.vertexIndex] == -1) {
            labels[node.vertexIndex] = index;
            index++;
        }
//        Collections.sort(node.children, comparator);
        for (Node child : node.children) {
            child.accept(this);
        }
    }
    
}
