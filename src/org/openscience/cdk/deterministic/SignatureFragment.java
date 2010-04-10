package org.openscience.cdk.deterministic;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.signature.MoleculeSignature;

/**
 * A fragment is a number of identical atom containers, represented by the 
 * atom container and a count. 
 * 
 * @author maclean
 *
 */
public class SignatureFragment extends AbstractFragment {
    
    /**
     * The canonical signature string of this fragment
     */
    private String signatureString;
    
    /**
     * The atoms and bonds of the fragment
     */
    private IAtomContainer fragmentAtomContainer;
    
    /**
     * Make a fragment from a signature string.
     * 
     * @param signatureString
     * @param count
     */
    public SignatureFragment(int count, String signatureString) {
        super(count);
        this.signatureString = signatureString;
        this.fragmentAtomContainer = 
            MoleculeSignature.fromSignatureString(signatureString);
    }
    
    public SignatureFragment(int count, IAtomContainer fragmentAtomContainer) {
        super(count);
        this.fragmentAtomContainer = fragmentAtomContainer;
        this.signatureString = 
            new MoleculeSignature(this.fragmentAtomContainer)
                .toCanonicalString();
    }
    
    public boolean hasSignatureString(String otherSignatureString) {
        return this.signatureString.equals(otherSignatureString);
    }
    
    public Fragment copy() {
        return new SignatureFragment(super.getCount(), signatureString);
    }
    
    public IAtom makeAtom(IChemObjectBuilder builder) {
        return null;    // TODO
    }
}
