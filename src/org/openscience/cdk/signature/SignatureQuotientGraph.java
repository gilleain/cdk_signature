package org.openscience.cdk.signature;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import signature.AbstractQuotientGraph;

public class SignatureQuotientGraph extends AbstractQuotientGraph {
    
    private IAtomContainer atomContainer;
    
    public SignatureQuotientGraph(IAtomContainer atomContainer) {
        this(atomContainer, -1);
    }
    
    public SignatureQuotientGraph(IAtomContainer atomContainer, int height) {
        this.atomContainer = atomContainer;
        CDKMoleculeSignature moleculeSignature = 
            new CDKMoleculeSignature(atomContainer);
        super.construct(moleculeSignature.getSymmetryClasses(height));
    }

    @Override
    public boolean isConnected(int i, int j) {
        IAtom a = atomContainer.getAtom(i);
        IAtom b = atomContainer.getAtom(j);
        return atomContainer.getBond(a, b) != null;
    }

}
