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
        return graph.getPermutedGraph(getNextPermutation());
    }
    
    public void remove() {
        //
    }

}
