package org.openscience.cdk.signature;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;

import signature.AbstractGraphSignature;
import signature.AbstractVertexSignature;
import signature.ColoredTree;
import signature.SymmetryClass;

/**
 * A signature for an entire molecule.
 * 
 * @cdk.module signature
 * @author maclean
 *
 */
public class CDKMoleculeSignature extends AbstractGraphSignature {
    
    private IAtomContainer molecule;
    
    /**
     * Creates a signature that represents this molecule.
     * 
     * @param molecule the molecule to convert to a signature
     */
    public CDKMoleculeSignature(IAtomContainer molecule) {
        super();
        this.molecule = molecule;
    }
    
    /**
     * Creates a signature with a maximum height of <code>height</code>
     * for molecule <code>molecule</code>.
     *  
     * @param molecule the molecule to convert to a signature
     * @param height the maximum height of the signature
     */
    public CDKMoleculeSignature(IMolecule molecule, int height) {
        super(height);
        this.molecule = molecule;
    }

    @Override
    public int getVertexCount() {
        return this.molecule.getAtomCount();
    }

    @Override
    public String signatureStringForVertex(int vertexIndex) {
        CDKAtomSignature atomSignature;
        int height = super.getHeight();
        if (height == -1) {
            atomSignature = new CDKAtomSignature(vertexIndex, this.molecule);
        } else {
            atomSignature = 
                new CDKAtomSignature(vertexIndex, height, this.molecule);
        }
        return atomSignature.toCanonicalString();
    }

    @Override
    public String signatureStringForVertex(int vertexIndex, int height) {
        CDKAtomSignature atomSignature = 
            new CDKAtomSignature(vertexIndex, height, this.molecule);
        return atomSignature.toCanonicalString();
    }

    @Override
    public AbstractVertexSignature signatureForVertex(int vertexIndex) {
        return new CDKAtomSignature(vertexIndex, this.molecule);
    }

    /**
     * Calculates the orbits of the atoms of the molecule. 
     * 
     * @return a list of orbits
     */
    public List<Orbit> calculateOrbits() {
        List<Orbit> orbits = new ArrayList<Orbit>();
        List<SymmetryClass> symmetryClasses = super.getSymmetryClasses();
        for (SymmetryClass symmetryClass : symmetryClasses) {
            Orbit orbit = new Orbit(symmetryClass.getSignatureString(), -1);
            for (int atomIndex : symmetryClass) {
                orbit.addAtom(atomIndex);
            }
            orbits.add(orbit);
        }
        return orbits;
    }
    
    /**
     * Builder for molecules (rather, for atom containers) from signature 
     * strings.
     * 
     * @param signatureString the signature string to use
     * @return an atom container
     */
    public static IAtomContainer fromSignatureString(String signatureString) {
        ColoredTree tree = CDKAtomSignature.parse(signatureString);
        CDKMoleculeFromSignatureBuilder builder =
            new CDKMoleculeFromSignatureBuilder();
        builder.makeFromColoredTree(tree);
        return builder.getAtomContainer();
    }

    public String toCanonicalSignatureString(int height) {
        String canonicalSignature = null;
        for (int i = 0; i < getVertexCount(); i++) {
            String signatureForI = signatureStringForVertex(i, height);
            if (canonicalSignature == null || 
                    canonicalSignature.compareTo(signatureForI) < 0) {
                canonicalSignature = signatureForI;
            }
        }
        return canonicalSignature;
    }
}
