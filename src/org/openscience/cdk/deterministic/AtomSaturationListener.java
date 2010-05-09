package org.openscience.cdk.deterministic;

/**
 * Listen to atom saturation 'events' - in other words, an atom of a structure
 * has been completely saturated by connecting as many possible other atoms.
 * 
 * @author maclean
 *
 */
public interface AtomSaturationListener {

    
    /**
     * An atom has been completely saturated.
     * 
     * @param atomSaturationEvent
     */
    public void atomSaturated(AtomSaturationEvent atomSaturationEvent);
    
}
