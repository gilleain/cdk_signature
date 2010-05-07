package org.openscience.cdk.deterministic;

public class BondCreationEvent {

    public final Graph parent;
    
    public final Graph child;
    
    public BondCreationEvent(Graph parent, Graph child) {
        this.parent = parent;
        this.child = child;
    }
}
