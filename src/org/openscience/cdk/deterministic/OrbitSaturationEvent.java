package org.openscience.cdk.deterministic;

/**
 * Record the saturation of an entire orbit.
 * 
 * @author maclean
 *
 */
public class OrbitSaturationEvent {
    
    public final Graph graph;
    
    public final String orbitSignature;
    
    public OrbitSaturationEvent(Graph graph, String orbitSignature) {
        this.graph = graph;
        this.orbitSignature = orbitSignature;
    }

}
