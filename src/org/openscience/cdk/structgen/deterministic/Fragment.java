package org.openscience.cdk.structgen.deterministic;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IChemObjectBuilder;

public interface Fragment {
    
    public void increaseCount();
    
    public void decreaseCount();
    
    public Fragment copy();

    public IAtom makeAtom(IChemObjectBuilder builder);
    
    public boolean isEmpty();
}
