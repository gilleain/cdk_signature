package org.openscience.cdk.structgen.deterministic;

/**
 * Listen to bond rejection events - bonds will be rejected if they are not
 * signature compatible or not canonical.
 * 
 * @author maclean
 *
 */
public interface BondRejectionListener {
    
    /**
     * @param bondRejectionEvent
     */
    public void bondRejected(BondRejectionEvent bondRejectionEvent);

}
