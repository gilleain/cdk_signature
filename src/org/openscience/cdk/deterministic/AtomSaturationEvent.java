package org.openscience.cdk.deterministic;

/**
 * Represents the stage of structure generation where all possible bonds from an
 * atom have been added, saturating it.
 * 
 * @author maclean
 *
 */
public class AtomSaturationEvent {
    
    public final Graph graph;
    
    public final int saturatedAtomIndex;
    
    public AtomSaturationEvent(Graph graph, int saturatedAtomIndex) {
        this.graph = graph;
        this.saturatedAtomIndex = saturatedAtomIndex;
    }

}
