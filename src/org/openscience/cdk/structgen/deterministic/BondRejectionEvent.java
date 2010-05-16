package org.openscience.cdk.structgen.deterministic;

/**
 * A bond has been rejected by the generator. 
 * 
 * @author maclean
 *
 */
public class BondRejectionEvent {
    
    public final Graph graph;
    
    public BondRejectionEvent(Graph graph) {
        this.graph = graph;
    }

}
