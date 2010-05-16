package org.openscience.cdk.structgen.deterministic;

/**
 * Both element and molecule fragments keep track of how many there are left to
 * use.
 * 
 * @author maclean
 *
 */
public abstract class AbstractFragment implements Fragment {
    
    /**
     * The number of unused fragments of this type
     */
    private int count;
    
    /**
     * Start with this many fragments.
     * 
     * @param count
     */
    public AbstractFragment(int count) {
        this.count = count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    /* (non-Javadoc)
     * @see org.openscience.cdk.deterministic.Fragment#decreaseCount()
     */
    public void decreaseCount() {
        this.count--;
    }
    
    /* (non-Javadoc)
     * @see org.openscience.cdk.deterministic.Fragment#increaseCount()
     */
    public void increaseCount() {
        this.count++;
    }
    
    public int getCount() {
        return this.count;
    }
    
}
