package org.openscience.cdk.signature;

import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import signature.AbstractVertexSignature;

/**
 * The signature for a molecule rooted at a particular atom.
 * 
 * @cdk.module signature
 * @author maclean
 *
 */
public class CDKAtomSignature extends AbstractVertexSignature {
    
    private IAtomContainer molecule;
    
    /**
     * Create an atom signature starting at <code>atomIndex</code>.
     * 
     * @param atomIndex the index of the atom that roots this signature
     * @param molecule the molecule to create the signature from
     */
    public CDKAtomSignature(int atomIndex, IAtomContainer molecule) {
        super("[", "]");
        this.molecule = molecule;
        super.create(atomIndex);
    }
    
    /**
     * Create an atom signature starting at <code>atomIndex</code> and with a
     * maximum height of <code>h</code>.
     * 
     * @param atomIndex the index of the atom that roots this signature
     * @param height the maximum height of the signature 
     * @param molecule the molecule to create the signature from
     */
    public CDKAtomSignature(int atomIndex, int height, IAtomContainer molecule) {
        super("[", "]");
        this.molecule = molecule;
        super.create(atomIndex, height);
    }

    @Override
    public int[] getConnected(int vertexIndex) {
        IAtom atom  = this.molecule.getAtom(vertexIndex);
        List<IAtom> connected = this.molecule.getConnectedAtomsList(atom);
        int[] connectedIndices = new int[connected.size()];
        int i = 0;
        for (IAtom otherAtom : connected) {
            connectedIndices[i++] = this.molecule.getAtomNumber(otherAtom);
        }
        return connectedIndices;
    }

    @Override
    public String getEdgeSymbol(int vertexIndex, int otherVertexIndex) {
        IAtom atomA = this.molecule.getAtom(vertexIndex);
        IAtom atomB = this.molecule.getAtom(otherVertexIndex);
        IBond bond = this.molecule.getBond(atomA, atomB);
        if (bond != null) {
            switch (bond.getOrder()) {
                case SINGLE: return "-";
                case DOUBLE: return "=";
                case TRIPLE: return "#";
                case QUADRUPLE: return "$";
                default: return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public String getVertexSymbol(int vertexIndex) {
        return this.molecule.getAtom(vertexIndex).getSymbol();
    }

}
