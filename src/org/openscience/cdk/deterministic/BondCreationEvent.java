package org.openscience.cdk.deterministic;

public class BondCreationEvent {

    public final SimpleGraph parent;
    
    public final SimpleGraph child;
    
    public BondCreationEvent(SimpleGraph parent, SimpleGraph child) {
        this.parent = parent;
        this.child = child;
    }
}
