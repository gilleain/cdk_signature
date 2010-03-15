package org.openscience.cdk.group;

import java.util.Iterator;

import cages.Permutor;

public class GraphPermutor extends Permutor implements Iterator<Graph> {
    
    private Graph graph;

    public GraphPermutor(Graph graph) {
        super(graph.getVertexCount());
        this.graph = graph;
    }

    public Graph next() {
        return fromPermutation(getNextPermutation());
    }
    
    public Graph fromPermutation(int[] p) {
        Graph permutation = new Graph();
        for (Graph.Edge edge : graph.edges) {
            int pa = p[edge.a];
            int pb = p[edge.b];
            permutation.makeEdge(pa, pb, edge.o);
        }
        return permutation;
    }

    public void remove() {
        //
    }

}
