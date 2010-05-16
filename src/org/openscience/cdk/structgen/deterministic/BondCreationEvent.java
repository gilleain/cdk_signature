package org.openscience.cdk.structgen.deterministic;

/**
 * A singular event of creating a bond.
 * 
 * @author maclean
 *
 */
public class BondCreationEvent {

    public final Graph parent;
    
    public final Graph child;
    
    public BondCreationEvent(Graph parent, Graph child) {
        this.parent = parent;
        this.child = child;
    }
}
