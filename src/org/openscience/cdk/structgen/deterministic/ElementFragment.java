package org.openscience.cdk.structgen.deterministic;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IChemObjectBuilder;

/**
 * Fragments that are just single elements (that is, single atoms).
 * 
 * @author maclean
 *
 */
public class ElementFragment extends AbstractFragment {
    
    /**
     * The element symbol
     */
    private String elementSymbol;
    
    /**
     * @param count
     * @param elementSymbol
     */
    public ElementFragment(int count, String elementSymbol) {
        super(count);
        this.elementSymbol = elementSymbol;
    }
    
    /**
     * @param atom
     * @param count
     */
    public ElementFragment(IAtom atom, int count) {
        this(count, atom.getSymbol());
    }

    /**
     * @param elementSymbol
     * @return
     */
    public boolean hasElementSymbol(String elementSymbol) {
        return this.elementSymbol.equals(elementSymbol);
    }
    
    /* (non-Javadoc)
     * @see org.openscience.cdk.deterministic.Fragment#copy()
     */
    public Fragment copy() {
        return new ElementFragment(super.getCount(), elementSymbol);
    }
    
    /* (non-Javadoc)
     * @see org.openscience.cdk.deterministic.Fragment#makeAtom(org.openscience.cdk.interfaces.IChemObjectBuilder)
     */
    public IAtom makeAtom(IChemObjectBuilder builder) {
        decreaseCount();
        return builder.newInstance(IAtom.class, elementSymbol);
    }
    
    public String toString() {
        return elementSymbol + "x" + getCount();
    }
}
