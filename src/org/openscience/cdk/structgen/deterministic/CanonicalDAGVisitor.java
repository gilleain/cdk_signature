package org.openscience.cdk.structgen.deterministic;

import signature.DAGVisitor;
import signature.DAG.Node;

public class CanonicalDAGVisitor implements DAGVisitor {

    public int[] labels;
    
    public int index;
    
    public CanonicalDAGVisitor(int n) {
        this.labels = new int[n];
        this.index = 0;
    }
    
    public void visit(Node node) {
        if (index < labels.length) {
            labels[index] = node.vertexIndex;
        }
        index++;
        for (Node child : node.children) {
            child.accept(this);
        }
    }
    
}
