package model;

import org.openscience.cdk.interfaces.IMolecule;

public class GraphMoleculePair {
    
    public Graph graph;
    
    public IMolecule molecule;
    
    public GraphMoleculePair(Graph graph, IMolecule molecule) {
        this.graph = graph;
        this.molecule = molecule;
    }

}
