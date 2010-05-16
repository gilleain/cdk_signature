package org.openscience.cdk.structgen.deterministic;

/**
 * Mainly intended for visualisation or debugging of the enumerator, listeners
 * that implement this interface can capture information about the parent/child
 * relationships and the bond addition.
 * 
 * @author maclean
 *
 */
public interface BondCreationListener {
    
    /**
     * A bond has been added. 
     */
    public void bondAdded(BondCreationEvent bondCreationEvent);

}
